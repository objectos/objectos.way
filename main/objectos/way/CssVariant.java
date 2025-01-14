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

@SuppressWarnings("exports")
sealed interface CssVariant {

  record OfAtRule(String rule) implements Comparable<OfAtRule>, CssVariant {

    @Override
    public final int compareTo(OfAtRule o) {
      if (o instanceof OfAtRule that) {
        return rule.compareTo(that.rule);
      } else {
        return 1;
      }
    }

    public final void writeAtRuleStart(StringBuilder out, CssIndentation indentation) {
      indentation.writeTo(out);

      out.append(rule);
      out.append(" {");
      out.append(System.lineSeparator());
    }

  }

  sealed interface OfClassName extends CssVariant {

    void writeClassName(StringBuilder out, int startIndex);

    int length();

  }

  record Prefix(String value) implements OfClassName {
    @Override
    public final void writeClassName(StringBuilder out, int startIndex) {
      String original;
      original = out.substring(startIndex, out.length());

      out.setLength(startIndex);

      out.append(value);

      out.append(original);
    }

    @Override
    public final int length() {
      return value.length();
    }

    @Override
    public final CssVariant generateGroup() {
      return new Prefix(value + ".group ");
    }
  }

  record Suffix(String value) implements OfClassName {
    @Override
    public final void writeClassName(StringBuilder out, int startIndex) {
      String original;
      original = out.substring(startIndex, out.length());

      out.setLength(startIndex);

      out.append(original);

      out.append(value);
    }

    @Override
    public final int length() {
      return value.length();
    }

    @Override
    public final CssVariant generateGroup() {
      return new Prefix(".group" + value + " ");
    }
  }

  default CssVariant generateGroup() {
    return null;
  }

}