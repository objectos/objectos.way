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
package objectox.html;

import java.util.Iterator;
import java.util.Set;
import objectox.html.elem.ElementSpec;

public class MarkupImplGen {

  private final StringBuilder methods = new StringBuilder();

  public static void main(String[] args) {
    System.out.println(new MarkupImplGen());
  }

  @Override
  public final String toString() {
    prepare();

    return methods.toString();
  }

  private void prepare() {
    methods.append("""
    // BEGIN generated code
    """);

    way();

    testable();

    elements();

    attributes();

    ambiguous();

    text();

    methods.append("""
    // END generated code
    """);
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
    """);

    final Set<String> ambiguousNames;
    ambiguousNames = HtmlSpec.ambiguousElemNames();

    for (ElementSpec elem : HtmlSpec.elements()) {
      final String htmlName;
      htmlName = elem.htmlName();

      if (ambiguousNames.contains(htmlName)) {
        methods.append("""

        /// Encodes the `%s` element with the specified values.
        /// @param values the encoded attributes and encoded child nodes of the element
        /// @return an object representing the encoded element
        public final ElementMarkup %s(RegularElementValue... values) {
          return recorder.regularElement(ElementName.%s, values);
        }
        """.formatted(elem.htmlName(), elem.htmlName(), elem.javaName()));
      }

      else if (elem.endTag()) {
        methods.append("""

        /// Encodes the `%1$s` element with the specified values.
        /// @param values the encoded attributes and encoded child nodes of the element
        /// @return an object representing the encoded element
        public final ElementMarkup %2$s(RegularElementValue... values) {
          return recorder.regularElement(ElementName.%3$s, values);
        }

        /// Encodes the `%1$s` element with the specified text.
        /// @param text the text value of the element
        /// @return an object representing the encoded element
        public final ElementMarkup %2$s(String text) {
          return recorder.textElement(ElementName.%3$s, text);
        }
        """.formatted(elem.htmlName(), elem.htmlName(), elem.javaName()));
      }

      else {
        methods.append("""

        /// Encodes the `%s` element with the specified content.
        /// @param values the encoded attributes of the element
        /// @return an object representing the encoded element
        public final ElementMarkup %s(VoidElementValue... values) {
          return recorder.voidElement(ElementName.%s, values);
        }
        """.formatted(elem.htmlName(), elem.htmlName(), elem.javaName()));
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
      if (ambiguous.contains(attr.htmlName()) || attr.booleanAttribute()) {
        continue;
      }

      methods.append("""

      /// Encodes the `%s` attribute with the specified value.
      /// @param value the attribute value
      /// @return an object representing the encoded attribute
      public final AttributeMarkup %s(String value) {
        return recorder.attribute(AttributeName.%s, value);
      }
      """.formatted(attr.htmlName(), attr.methodName(), attr.constantName()));

      if (!attr.eventAttribute()) {
        continue;
      }

      methods.append("""

      /// Encodes the `%s` attribute with the specified action.
      /// @param action the action to be executed
      /// @return an object representing the encoded attribute
      public final AttributeMarkup %s(JsAction action) {
        return recorder.attribute(AttributeName.%s, action);
      }
      """.formatted(attr.htmlName(), attr.methodName(), attr.constantName()));
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
      /// @param value the attribute value or the text content of the element
      /// @return an object representing the encoded attribute or element
      public final AmbiguousMarkup %s(String value) {
        return recorder.ambiguous(AmbiguousName.%s, value);
      }
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

      methods.append("  /// ");

      methods.append(lines.next());
    }

    methods.append('\n');

    methods.append("  public final ");

    methods.append(spec.sig());

    methods.append('{');

    methods.append('\n');

    methods.append("    return recorder.");

    methods.append(spec.invocation());

    methods.append(';');

    methods.append('\n');

    methods.append("  }");

    methods.append('\n');
  }

}
