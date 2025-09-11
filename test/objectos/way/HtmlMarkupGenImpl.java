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

import java.util.Set;

public class HtmlMarkupGenImpl {

  private final StringBuilder methods = new StringBuilder();

  public static void main(String[] args) {
    System.out.println(new HtmlMarkupGenImpl());
  }

  @Override
  public final String toString() {
    prepare();

    return methods.toString();
  }

  private void prepare() {
    elements();

    attributes();

    ambiguous();

    //text();
  }

  private void elements() {
    methods.append("""

      //
      // ELEMENTS
      //

      abstract Html.Instruction.OfElement elem0(Html.ElementName name, Html.Instruction... contents);

      abstract Html.Instruction.OfElement elem0(Html.ElementName name, String text);

      @Override
      public final Html.Instruction.OfElement elem(Html.ElementName name, Html.Instruction... contents) {
        Objects.requireNonNull(name, "name == null");
        return elem0(name, contents);
      }

      @Override
      public final Html.Instruction.OfElement elem(Html.ElementName name, String text) {
        Objects.requireNonNull(name, "name == null");
        return elem0(name, text);
      }
    """);

    final Set<String> ambiguousNames;
    ambiguousNames = HtmlSpec.ambiguousElemNames();

    for (HtmlSpec.ElementSpec elem : HtmlSpec.elements()) {
      final String htmlName;
      htmlName = elem.htmlName();

      if (ambiguousNames.contains(htmlName)) {
        methods.append("""

          @Override
          public final Html.Instruction.OfElement %s(Html.Instruction... contents) {
            return elem0(HtmlElementName.%s, contents);
          }
        """.formatted(elem.htmlName(), elem.javaName()));
      }

      else if (elem.endTag()) {
        methods.append("""

          @Override
          public final Html.Instruction.OfElement %s(Html.Instruction... contents) {
            return elem0(HtmlElementName.%s, contents);
          }

          @Override
          public final Html.Instruction.OfElement %s(String text) {
            return elem0(HtmlElementName.%s, text);
          }
        """.formatted(
            elem.htmlName(), elem.javaName(),
            elem.htmlName(), elem.javaName()
        ));
      }

      else {
        methods.append("""

          @Override
          public final Html.Instruction.OfElement %s(Html.Instruction.OfVoid... contents) {
            return elem0(HtmlElementName.%s, contents);
          }
        """.formatted(elem.htmlName(), elem.javaName()));
      }
    }

  }

  private void attributes() {
    methods.append("""

      //
      // ATTRIBUTES
      //

      abstract Html.Instruction.OfAttribute attr0(Html.AttributeName name);

      abstract Html.Instruction.OfAttribute attr0(Html.AttributeName name, Object value);

      @Override
      public final Html.Instruction.OfAttribute attr(Html.AttributeName name) {
        Objects.requireNonNull(name, "name == null");
        return attr0(name);
      }

      @Override
      public final Html.Instruction.OfAttribute attr(Html.AttributeName name, String value) {
        Objects.requireNonNull(name, "name == null");
        return attr0(name, value);
      }
    """);

    final Set<String> ambiguous;
    ambiguous = HtmlSpec.ambiguousAttrNames();

    for (HtmlSpec.AttributeSpec attr : HtmlSpec.attributes()) {
      if (ambiguous.contains(attr.htmlName()) || attr.booleanAttribute()) {
        continue;
      }

      methods.append("""

        @Override
        public final Html.Instruction.OfAttribute %s(String value) {
          return attr0(HtmlAttributeName.%s, value);
        }
      """.formatted(attr.methodName(), attr.constantName()));
    }
  }

  private void ambiguous() {
    methods.append("""

      //
      // AMBIGUOUS
      //

      abstract Html.Instruction.OfAmbiguous ambiguous(HtmlAmbiguous name, String text);
    """);

    for (HtmlSpec.AmbiguousSpec spec : HtmlSpec.ambiguous()) {
      methods.append("""

        @Override
        public final Html.Instruction.OfAmbiguous %s(String text) {
          return ambiguous(HtmlAmbiguous.%s, text);
        }
      """.formatted(spec.methodName(), spec.constantName()));
    }
  }

}
