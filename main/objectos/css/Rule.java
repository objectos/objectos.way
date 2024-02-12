/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.css;

import java.util.List;

class Rule implements Comparable<Rule> {

  public static final Rule NOOP = new Rule(-1, "", List.of());

  final int index;

  final String className;

  final List<Variant> variants;

  Rule(int index, String className, List<Variant> variants) {
    this.index = index;

    this.className = className;

    this.variants = variants;
  }

  public final void accept(WayStyleGenRound gen) {
    int size;
    size = variants.size();

    switch (size) {
      case 0 -> {
        StringBuilder out;
        out = gen.topLevel();

        writeTo(out);
      }

      case 1 -> {
        Variant variant;
        variant = variants.getFirst();

        VariantKind kind;
        kind = variant.kind();

        if (kind.isMediaQuery()) {
          StringBuilder out;
          out = gen.mediaQuery(variant);

          out.append("  ");

          writeTo(out);
        }

        else {
          throw new UnsupportedOperationException("Implement me");
        }
      }

      default -> throw new UnsupportedOperationException("Implement me");
    }
  }

  @Override
  public final int compareTo(Rule o) {
    return Integer.compare(index, o.index);
  }

  @Override
  public String toString() {
    return className;
  }

  final void writeTo(StringBuilder out) {
    writeClassName(out, className);

    out.append(" { ");

    writeProperties(out);

    out.append(" }");

    out.append(System.lineSeparator());
  }

  private void writeClassName(StringBuilder out, String className) {
    int length;
    length = className.length();

    if (length == 0) {
      return;
    }

    out.append('.');

    int index;
    index = 0;

    boolean escaped;
    escaped = false;

    char first;
    first = className.charAt(index);

    if (0x30 <= first && first <= 0x39) {
      out.append("\\3");
      out.append(first);

      index++;

      escaped = true;
    }

    for (; index < length; index++) {
      char c;
      c = className.charAt(index);

      switch (c) {
        case ' ', ',', '.', '/', ':', '@', '[', ']' -> {
          out.append("\\");

          out.append(c);

          escaped = false;
        }

        case 'a', 'b', 'c', 'd', 'e', 'f',
             'A', 'B', 'C', 'D', 'E', 'F',
             '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
          if (escaped) {
            out.append(' ');
          }

          out.append(c);

          escaped = false;
        }

        default -> out.append(c);
      }
    }
  }

  void writeProperties(StringBuilder out) {}

  final void writePropertyValue(StringBuilder out, String property, String value) {
    out.append(property);

    out.append(": ");

    out.append(value);
  }

}