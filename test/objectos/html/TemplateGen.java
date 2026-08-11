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

public class TemplateGen {
  private final StringBuilder fields = new StringBuilder();

  private final StringBuilder methods = new StringBuilder();

  public static void main(String[] args) {
    System.out.println(new TemplateGen());
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
      methodSpec(spec);
    }
  }

  private void elements() {
    methods.append("""

    //
    // ELEMENTS
    //

    /// Encodes an HTML element with the specified name and values.
    /// @param name the element name
    /// @param values the encoded attributes and encoded child nodes of the element
    /// @return an object representing the encoded element
    protected final ElementMarkup elem(ElementName name, ElementValue... values) {
      return markup().elem(name, values);
    }

    /// Encodes an HTML element with the specified name and text.
    /// @param name the element name
    /// @param text the text value of this element
    /// @return an object representing the encoded element
    protected final ElementMarkup elem(ElementName name, String text) {
      return markup().elem(name, text);
    }

    /// Encodes the `<!DOCTYPE html>` doctype.
    protected final void doctype() {
      markup().doctype();
    }
    """);

    final Set<String> ambiguousNames;
    ambiguousNames = HtmlSpec.ambiguousElemNames();

    for (ElementSpec elem : HtmlSpec.elements()) {
      final String htmlName;
      htmlName = elem.htmlName();

      if (ambiguousNames.contains(htmlName)) {
        methods.append("""
        /// Encodes the `%1$s` element with the specified values.
        /// @param values the encoded attributes and encoded child nodes of the element
        /// @return an object representing the encoded element
        protected final ElementMarkup %2$s(RegularElementValue... values) {
          return markup().%2$s(values);
        }
        """.formatted(elem.htmlName(), elem.htmlName()));

      }

      else if (elem.endTag()) {
        methods.append("""

        /// Encodes the `%1$s` element with the specified values.
        /// @param values the encoded attributes and encoded child nodes of the element
        /// @return an object representing the encoded element
        protected final ElementMarkup %2$s(RegularElementValue... values) {
          return markup().%2$s(values);
        }

        /// Encodes the `%1$s` element with the specified text.
        /// @param text the text value of the element
        /// @return an object representing the encoded element
        protected final ElementMarkup %2$s(String text) {
          return markup().%2$s(text);
        }
        """.formatted(elem.htmlName(), elem.htmlName()));
      }

      else {
        methods.append("""

        /// Encodes the `%1$s` element with the specified content.
        /// @param values the encoded attributes of the element
        /// @return an object representing the encoded element
        protected final ElementMarkup %2$s(VoidElementValue... values) {
          return markup().%2$s(values);
        }
        """.formatted(elem.htmlName(), elem.htmlName()));
      }
    }

  }

  private void attributes() {
    methods.append("""

    //
    // ATTRIBUTES
    //
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
        protected static final BooleanAttribute %s = Markup.%s;
        """.formatted(attr.htmlName(), attr.methodName(), attr.methodName()));
      }

      else {
        methods.append("""

        /// Encodes the `%s` attribute with the specified value.
        /// @param value the attribute value
        /// @return an object representing the encoded attribute
        protected final Attribute %s(String value) {
          return markup().%s(value);
        }
        """.formatted(attr.htmlName(), attr.methodName(), attr.methodName()));

        if (attr.eventAttribute()) {
          methods.append("""

        /// Encodes the `%s` attribute with the specified action.
        /// @param value the action to be executed
        /// @return an object representing the encoded attribute
        protected final Attribute %s(JsAction value) {
          return markup().%s(value);
        }
        """.formatted(attr.htmlName(), attr.methodName(), attr.methodName()));
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

      /// Encodes the `%s` attribute or the `%s` element with the specified text.
      /// @param text the attribute value or the text content of the element
      /// @return an object representing the encoded attribute or element
      protected final AmbiguousMarkup %s(String text) {
        return markup().%s(text);
      }
      """.formatted(spec.attributeName(), spec.elementName(), spec.methodName(), spec.methodName()));
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

    methods.append("""
    protected final %s {
      return markup().%s;
    }
    """.formatted(spec.sig(), spec.invocation()));
  }
}
