/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package objectos.way;

import java.util.Objects;

final class CssVariant implements Comparable<CssVariant> {

  private enum Kind {

    AT_RULE,

    PREFIX,

    SUFFIX;

  }

  private final Kind kind;

  private final String value;

  private CssVariant(Kind kind, String value) {
    this.kind = kind;
    this.value = Objects.requireNonNull(value, "value == null");
  }

  public static CssVariant atRule(String value) {
    return new CssVariant(Kind.AT_RULE, value);
  }

  public static CssVariant prefix(String value) {
    return new CssVariant(Kind.PREFIX, value);
  }

  public static CssVariant suffix(String value) {
    return new CssVariant(Kind.SUFFIX, value);
  }

  @Override
  public final int compareTo(CssVariant o) {
    final int result;
    result = kind.compareTo(o.kind);

    if (result != 0) {
      return result;
    }

    return value.compareTo(o.value);
  }

  @Override
  public final boolean equals(Object obj) {
    return obj == this || obj instanceof CssVariant that
        && kind.equals(that.kind)
        && value.equals(that.value);
  }

  @Override
  public final int hashCode() {
    int result;
    result = 1;

    result = 31 * result + kind.hashCode();

    result = 31 * result + value.hashCode();

    return result;
  }

  public final CssVariant generateGroup() {
    return switch (kind) {
      case AT_RULE -> null;

      case PREFIX -> prefix(value + ".group ");

      case SUFFIX -> prefix(".group" + value + " ");
    };
  }

  public final boolean isAtRule() {
    return kind == Kind.AT_RULE;
  }

  public final int length() {
    return switch (kind) {
      case AT_RULE -> throw new UnsupportedOperationException(
          "length must not be invoked on an AT_RULE variant"
      );

      default -> value.length();
    };
  }

  public final void writeClassName(StringBuilder out, int startIndex) {
    switch (kind) {
      case AT_RULE -> throw new UnsupportedOperationException(
          "writeClassName must not be invoked on an AT_RULE variant"
      );

      case PREFIX -> {
        String original;
        original = out.substring(startIndex, out.length());

        out.setLength(startIndex);

        out.append(value);

        out.append(original);
      }

      case SUFFIX -> {
        String original;
        original = out.substring(startIndex, out.length());

        out.setLength(startIndex);

        out.append(original);

        out.append(value);
      }
    }
  }

  public final String value() {
    return value;
  }

}