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

public class HtmlMarkupOfTestableGen {
  private String methods;

  public static void main(String[] args) {
    System.out.println(new HtmlMarkupOfTestableGen());
  }

  @Override
  public final String toString() {
    prepare();

    return """
    /// Declares the structure of an HTML document using pure Java (testable nodes).
    public sealed static abstract class MarkupOfTestable extends MarkupOfElement {
      MarkupOfTestable() {}
    %s
    }
    """.formatted(methods);
  }

  private void prepare() {
    final StringBuilder methodsBuilder;
    methodsBuilder = new StringBuilder();

    for (HtmlSpec.AmbiguousSpec spec : HtmlSpec.ambiguous()) {
      methodsBuilder.append("""

        /// Declares the `%s` attribute or the `%s` element with the specified text.
        /// @param text the attribute value or the text content of the element
        /// @return an instruction representing the attribute or the element
        public final Html.Instruction.OfAmbiguous %s(String text) {
          return ambiguous(HtmlAmbiguous.%s, text);
        }
      """.formatted(spec.attributeName(), spec.elementName(), spec.methodName(), spec.constantName()));
    }

    methods = methodsBuilder.toString();
  }
}
