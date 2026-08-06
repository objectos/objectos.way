/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectos.html;

import java.util.Iterator;
import java.util.Set;
import objectox.html.AmbiguousSpec;
import objectox.html.AttributeSpec;
import objectox.html.HtmlSpec;
import objectox.html.MethodSpec;
import objectox.html.elem.ElementSpec;

public class MarkupGen {
  private final StringBuilder fields = new StringBuilder();

  private final StringBuilder methods = new StringBuilder();

  public static void main(String[] args) {
    System.out.println(new MarkupGen());
  }

  @Override
  public final String toString() {
    prepare();

    return """
    %s
    %s
    """.formatted(fields, methods).indent(2);
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

    // @formatter:off
    """);

    for (MethodSpec spec : HtmlSpec.wayNodes()) {
      methodSpec(spec);
    }

    methods.append("""

    // @formatter:on
    """);
  }

  private void testable() {
    methods.append("""

    //
    // TESTABLE
    //
    """);

    for (MethodSpec spec : HtmlSpec.testableNodes()) {
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

        methods.append("/// ");

        methods.append(lines.next());
      }

      methods.append('\n');

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
    /// @return an object representing the encoded element
    ElementMarkup elem(ElementName name, ElementContent... contents);

    /// Renders an HTML element with the specified name and text.
    /// @param name the element name
    /// @param text the text value of this element
    /// @return an object representing the encoded element
    ElementMarkup elem(ElementName name, String text);

    /// Renders the `<!DOCTYPE html>` doctype.
    void doctype();
    """);

    final Set<String> ambiguousNames;
    ambiguousNames = HtmlSpec.ambiguousElemNames();

    for (ElementSpec elem : HtmlSpec.elements()) {
      final String htmlName;
      htmlName = elem.htmlName();

      if (ambiguousNames.contains(htmlName)) {
        methods.append("""

        /// Renders the `%s` element with the specified content.
        /// @param contents the attributes and children of the element
        /// @return an object representing the encoded element
        ElementMarkup %s(ElementContent... contents);
        """.formatted(elem.htmlName(), elem.htmlName()));
      }

      else if (elem.endTag()) {
        methods.append("""

        /// Renders the `%1$s` element with the specified content.
        /// @param contents the attributes and children of the element
        /// @return an object representing the encoded element
        ElementMarkup %2$s(Instruction... contents);

        /// Renders the `%1$s` element with the specified text.
        /// @param text the text value of the element
        /// @return an object representing the encoded element
        ElementMarkup %2$s(String text);
        """.formatted(elem.htmlName(), elem.htmlName(), elem.javaName()));
      }

      else {
        methods.append("""

        /// Renders the `%s` element with the specified content.
        /// @param contents the attributes of the element
        /// @return an object representing the encoded element
        ElementMarkup %s(Instruction.OfVoid... contents);
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
    Attribute attr(AttributeName name);

    /// Renders an attribute with the specified name and value.
    /// @param name the attribute name
    /// @param value the attribute value
    /// @return an instruction representing the attribute
    Attribute attr(AttributeName name, String value);
    """);

    final Set<String> ambiguous;
    ambiguous = HtmlSpec.ambiguousAttrNames();

    for (AttributeSpec attr : HtmlSpec.attributes()) {
      if (ambiguous.contains(attr.htmlName())) {
        continue;
      }

      else if (attr.booleanAttribute()) {
        fields.append("""

        /// The `%s` boolean attribute.
        BooleanAttribute %s = BooleanAttribute.of(AttributeName.%s);
        """.formatted(attr.htmlName(), attr.methodName(), attr.constantName()));
      }

      else {
        methods.append("""

        /// Renders the `%s` attribute with the specified value.
        /// @param value the attribute value
        /// @return an instruction representing the attribute
        Attribute %s(String value);
        """.formatted(attr.htmlName(), attr.methodName()));

        if (attr.eventAttribute()) {
          methods.append("""

        /// Renders the `%s` attribute with the specified action.
        /// @param value the action to be executed
        /// @return an instruction representing the attribute
        Attribute %s(JsAction value);
        """.formatted(attr.htmlName(), attr.methodName()));
        }
      }
    }
  }

  private void ambiguous() {
    methods.append("""

    //
    // AMBIGUOUS
    //
    """);

    for (AmbiguousSpec spec : HtmlSpec.ambiguous()) {
      methods.append("""

      /// Renders the `%s` attribute or the `%s` element with the specified text.
      /// @param text the attribute value or the text content of the element
      /// @return an instruction representing the attribute or the element
      AmbiguousInstruction %s(String text);
      """.formatted(spec.attributeName(), spec.elementName(), spec.methodName(), spec.constantName()));
    }
  }

  private void text() {
    methods.append("""

    //
    // TEXT
    //
    """);

    for (MethodSpec spec : HtmlSpec.textNodes()) {
      methodSpec(spec);
    }
  }

  private void methodSpec(MethodSpec spec) {
    final String javadocs;
    javadocs = spec.javadocs();

    final Iterator<String> lines;
    lines = javadocs.lines().iterator();

    while (lines.hasNext()) {
      methods.append('\n');

      methods.append("/// ");

      methods.append(lines.next());
    }

    methods.append('\n');

    methods.append(spec.sig());

    methods.append(';');

    methods.append('\n');
  }
}
