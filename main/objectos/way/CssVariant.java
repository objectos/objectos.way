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
package objectos.way;

@SuppressWarnings("exports")
sealed interface CssVariant extends Comparable<CssVariant> {

  static CssVariant parse(String formatString) {
    int amper;
    amper = formatString.indexOf('&');

    if (amper < 0) {
      return new Invalid(formatString, "Format string must contain exactly one '&' character");
    }

    String before;
    before = formatString.substring(0, amper);

    String after;
    after = formatString.substring(amper + 1);

    int anotherAmper;
    anotherAmper = after.indexOf('&');

    if (anotherAmper > 0) {
      return new Invalid(formatString, "Format string must contain exactly one '&' character");
    }

    return new ClassNameFormat(before, after);
  }

  record AppendTo(int index, String selector) implements ClassNameVariant {
    @Override
    public final int compareTo(CssVariant o) {
      if (o instanceof MediaQuery) {
        return 1;
      }

      if (o instanceof AppendTo that) {
        return Integer.compare(index, that.index);
      }

      return 1;
    }

    @Override
    public final void writeClassName(StringBuilder out, int startIndex) {
      out.append(selector);
    }
  }

  record Breakpoint(int index, String name, String value) implements MediaQuery {
    @Override
    public final int compareTo(CssVariant o) {
      if (o instanceof Breakpoint that) {
        return Integer.compare(index, that.index);
      }

      return -1;
    }

    @Override
    public final void writeMediaQueryStart(StringBuilder out, CssIndentation indentation) {
      indentation.writeTo(out);

      out.append("@media (min-width: ");
      out.append(value);
      out.append(") {");
      out.append(System.lineSeparator());
    }
  }
  
  record ClassNameFormat(String before, String after) implements ClassNameVariant {
    @Override
    public final int compareTo(CssVariant o) {
      if (o instanceof ClassNameFormat) {
        return 0;
      }

      if (o instanceof AppendTo) {
        return -1;
      }

      return 1;
    }

    @Override
    public final void writeClassName(StringBuilder out, int startIndex) {
      String original;
      original = out.substring(startIndex, out.length());

      out.setLength(startIndex);

      out.append(before);
      out.append(original);
      out.append(after);
    }
  }
  
  record Invalid(String formatString, String reason) implements CssVariant {
    @Override
    public final int compareTo(CssVariant o) {
      if (o instanceof Invalid) {
        return 0;
      }

      return -1;
    }
  }
  
  sealed interface ClassNameVariant extends CssVariant {
    
    void writeClassName(StringBuilder out, int startIndex);
    
  }

  sealed interface MediaQuery extends CssVariant {

    void writeMediaQueryStart(StringBuilder out, CssIndentation indentation);

  }

}