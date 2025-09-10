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

import objectos.way.HtmlSpec.ElementSpec;

public class HtmlMarkupElementsGen {
  private String methods;

  public static void main(String[] args) {
    System.out.println(new HtmlMarkupElementsGen());
  }

  @Override
  public final String toString() {
    prepare();

    return """
    /// Declares the structure of an HTML document using pure Java (elements).
    public sealed static abstract class MarkupElements extends MarkupAttributes {

      MarkupElements() {}

      abstract Html.Instruction.OfAmbiguous ambiguous(HtmlAmbiguous name, String value);
    %s
    }
    """.formatted(methods);
  }

  private void prepare() {
    final StringBuilder methodsBuilder;
    methodsBuilder = new StringBuilder();

    for (ElementSpec elem : HtmlSpec.elements()) {
      final String htmlName;
      htmlName = elem.htmlName();

      if (HtmlSpec.AMBIGUOUS.contains(htmlName)) {
        methodsBuilder.append("""

          /// Declares the `%1$s` element with the specified content.
          /// @param contents the attributes and children of the element
          /// @return an instruction representing the element.
          @Override
          public final Html.Instruction.OfElement %2$s(Html.Instruction... contents) {
            return element(HtmlElementName.%3$s, contents);
          }

          /// Declares the `%1$s` element with the specified text.
          /// @param text the text value of the element
          /// @return an instruction representing the element.
          @Override
          public final Html.Instruction.OfAmbiguous %2$s(String text) {
            return ambiguous(HtmlAmbiguous.%3$s, text);
          }
        """.formatted(elem.htmlName(), elem.htmlName(), elem.javaName()));
      }

      else if (elem.endTag()) {
        methodsBuilder.append("""

          /// Declares the `%1$s` element with the specified content.
          /// @param contents the attributes and children of the element
          /// @return an instruction representing the element.
          @Override
          public final Html.Instruction.OfElement %2$s(Html.Instruction... contents) {
            return element(HtmlElementName.%3$s, contents);
          }

          /// Declares the `%1$s` element with the specified text.
          /// @param text the text value of the element
          /// @return an instruction representing the element.
          @Override
          public final Html.Instruction.OfElement %2$s(String text) {
            return element(HtmlElementName.%3$s, text);
          }
        """.formatted(elem.htmlName(), elem.htmlName(), elem.javaName()));
      }

      else {
        methodsBuilder.append("""

          /// Declares the `%1$s` element with the specified content.
          /// @param contents the attributes of the element
          /// @return an instruction representing the element.
          @Override
          public final Html.Instruction.OfElement %1$s(Html.Instruction.OfVoid... contents) {
            return element(HtmlElementName.%2$s, contents);
          }
        """.formatted(elem.htmlName(), elem.javaName()));
      }
    }

    methods = methodsBuilder.toString();
  }
}
