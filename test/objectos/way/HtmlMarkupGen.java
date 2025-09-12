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

import java.util.Iterator;
import java.util.Set;

public class HtmlMarkupGen {
  private final StringBuilder fields = new StringBuilder();

  private final StringBuilder methods = new StringBuilder();

  public static void main(String[] args) {
    System.out.println(new HtmlMarkupGen());
  }

  @Override
  public final String toString() {
    prepare();

    return """
    %s
    %s
    """.formatted(fields, methods);
  }

  private void prepare() {
    way();

    testable();

    elements();

    attributes();

    ambiguous();

    text();
  }

  private void way() {
    methods.append("""

      //
      // WAY
      //
    """);

    for (HtmlSpec.AttributeSpec spec : HtmlSpec.dataOn()) {
      methods.append("""

        /// Renders the `%s` attribute with the specified script.
        /// @param script the script to be executed
        /// @return an instruction representing the attribute
        Html.Instruction.OfDataOn %s(Consumer<Script> script);
      """.formatted(spec.htmlName(), spec.methodName()));
    }

    for (HtmlSpec.MethodSpec spec : HtmlSpec.wayNodes()) {
      methodSpec(spec);
    }
  }

  private void testable() {
    methods.append("""

      //
      // TESTABLE
      //
    """);

    for (HtmlSpec.MethodSpec spec : HtmlSpec.testableNodes()) {
      methods.append('\n');

      final String javadocs;
      javadocs = spec.javadocs();

      final Iterator<String> lines;
      lines = javadocs.lines().iterator();

      final String first;
      first = lines.next();

      methods.append("  /// ");

      methods.append(first.substring(0, first.length() - 1));

      methods.append(" (optional operation).");

      while (lines.hasNext()) {
        methods.append('\n');

        methods.append("  /// ");

        methods.append(lines.next());
      }

      methods.append('\n');

      methods.append("  ");

      methods.append(spec.sig());

      methods.append(';');

      methods.append('\n');
    }
  }

  private void elements() {
    methods.append("""

      //
      // ELEMENTS
      //

      /// Renders an HTML element with the specified name and contents.
      /// @param name the element name
      /// @param contents the attributes and children of the element
      /// @return an instruction representing the element
      Html.Instruction.OfElement elem(Html.ElementName name, Html.Instruction... contents);

      /// Renders an HTML element with the specified name and text.
      /// @param name the element name
      /// @param text the text value of this element
      /// @return an instruction representing the element
      Html.Instruction.OfElement elem(Html.ElementName name, String text);

      /// Renders the `<!DOCTYPE html>` doctype.
      void doctype();
    """);

    final Set<String> ambiguousNames;
    ambiguousNames = HtmlSpec.ambiguousElemNames();

    for (HtmlSpec.ElementSpec elem : HtmlSpec.elements()) {
      final String htmlName;
      htmlName = elem.htmlName();

      if (ambiguousNames.contains(htmlName)) {
        methods.append("""

          /// Renders the `%s` element with the specified content.
          /// @param contents the attributes and children of the element
          /// @return an instruction representing the element.
          Html.Instruction.OfElement %s(Html.Instruction... contents);
        """.formatted(elem.htmlName(), elem.htmlName()));
      }

      else if (elem.endTag()) {
        methods.append("""

          /// Renders the `%1$s` element with the specified content.
          /// @param contents the attributes and children of the element
          /// @return an instruction representing the element.
          Html.Instruction.OfElement %2$s(Html.Instruction... contents);

          /// Renders the `%1$s` element with the specified text.
          /// @param text the text value of the element
          /// @return an instruction representing the element.
          Html.Instruction.OfElement %2$s(String text);
        """.formatted(elem.htmlName(), elem.htmlName(), elem.javaName()));
      }

      else {
        methods.append("""

          /// Renders the `%s` element with the specified content.
          /// @param contents the attributes of the element
          /// @return an instruction representing the element.
          Html.Instruction.OfElement %s(Html.Instruction.OfVoid... contents);
        """.formatted(elem.htmlName(), elem.htmlName()));
      }
    }

  }

  private void attributes() {
    methods.append("""

      //
      // ATTRIBUTES
      //

      /// Renders an attribute with the specified name.
      /// @param name the attribute name
      /// @return an instruction representing the attribute
      Html.Instruction.OfAttribute attr(Html.AttributeName name);

      /// Renders an attribute with the specified name and value.
      /// @param name the attribute name
      /// @param value the attribute value
      /// @return an instruction representing the attribute
      Html.Instruction.OfAttribute attr(Html.AttributeName name, String value);
    """);

    final Set<String> ambiguous;
    ambiguous = HtmlSpec.ambiguousAttrNames();

    for (HtmlSpec.AttributeSpec attr : HtmlSpec.attributes()) {
      if (ambiguous.contains(attr.htmlName())) {
        continue;
      }

      else if (attr.booleanAttribute()) {
        fields.append("""

          /// The `%s` boolean attribute.
          Html.AttributeObject %s = Html.AttributeObject.of(Html.AttributeName.%s);
        """.formatted(attr.htmlName(), attr.methodName(), attr.constantName()));
      }

      else {
        methods.append("""

          /// Renders the `%s` attribute with the specified value.
          /// @param value the attribute value
          /// @return an instruction representing the attribute
          Html.Instruction.OfAttribute %s(String value);
        """.formatted(attr.htmlName(), attr.methodName()));
      }
    }
  }

  private void ambiguous() {
    methods.append("""

      //
      // AMBIGUOUS
      //
    """);

    for (HtmlSpec.AmbiguousSpec spec : HtmlSpec.ambiguous()) {
      methods.append("""

        /// Renders the `%s` attribute or the `%s` element with the specified text.
        /// @param text the attribute value or the text content of the element
        /// @return an instruction representing the attribute or the element
        Html.Instruction.OfAmbiguous %s(String text);
      """.formatted(spec.attributeName(), spec.elementName(), spec.methodName(), spec.constantName()));
    }
  }

  private void text() {
    methods.append("""

      //
      // TEXT
      //
    """);

    for (HtmlSpec.MethodSpec spec : HtmlSpec.textNodes()) {
      methodSpec(spec);
    }
  }

  private void methodSpec(HtmlSpec.MethodSpec spec) {
    final String javadocs;
    javadocs = spec.javadocs();

    final Iterator<String> lines;
    lines = javadocs.lines().iterator();

    while (lines.hasNext()) {
      methods.append('\n');

      methods.append("  /// ");

      methods.append(lines.next());
    }

    methods.append('\n');

    methods.append("  ");

    methods.append(spec.sig());

    methods.append(';');

    methods.append('\n');
  }
}
