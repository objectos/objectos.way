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
import objectos.css.Variant.AppendTo;
import objectos.css.Variant.MediaQuery;
import objectos.css.WayStyleGenRound.Context;

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
    Context context;
    context = null;

    for (int i = 0, size = variants.size(); i < size; i++) {
      Variant variant;
      variant = variants.get(i);

      if (!(variant instanceof MediaQuery query)) {
        break;
      }

      if (context == null) {
        context = gen.contextOf(query);
      } else {
        context = context.contextOf(query);
      }
    }

    if (context != null) {
      context.add(this);
    } else {
      gen.topLevel(this);
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

  public final void writeTo(StringBuilder out, Indentation indentation) {
    indentation.writeTo(out);

    writeClassName(out);

    for (Variant variant : variants) {
      if (variant instanceof AppendTo to) {
        out.append(to.selector());
      }
    }

    writeBlock(out, indentation);

    out.append(System.lineSeparator());
  }

  void writeBlock(StringBuilder out, Indentation indentation) {
    out.append(" { ");

    writeProperties(out);

    out.append(" }");
  }

  private void writeClassName(StringBuilder out) {
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