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

public class MarkupGeneratedGen {

  private final StringBuilder methods = new StringBuilder();

  public static void main(String[] args) {
    System.out.println(new MarkupGeneratedGen());
  }

  @Override
  public final String toString() {
    prepare();

    return methods.toString();
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

    abstract ElementMarkup elem0(ElementName name, ElementValue... values);

    abstract ElementMarkup elem0(ElementName name, String text);

    /// Encodes an HTML element with the specified name and contents.
    /// @param name the element name
    /// @param contents the attributes and children of the element
    /// @return an object representing the encoded attribute
    public final ElementMarkup elem(ElementName name, Instruction... contents) {
      Objects.requireNonNull(name, "name == null");
      return elem0(name, contents);
    }

    /// Encodes an HTML element with the specified name and text.
    /// @param name the element name
    /// @param text the text value of this element
    /// @return an instruction representing the element
    public final ElementMarkup elem(ElementName name, String text) {
      Objects.requireNonNull(name, "name == null");
      return elem0(name, text);
    }

    /// Encodes the `<!DOCTYPE html>` doctype.
    public abstract void doctype();
    """);

    final Set<String> ambiguousNames;
    ambiguousNames = HtmlSpec.ambiguousElemNames();

    for (ElementSpec elem : HtmlSpec.elements()) {
      final String htmlName;
      htmlName = elem.htmlName();

      if (ambiguousNames.contains(htmlName)) {
        methods.append("""

        /// Encodes the `%s` element with the specified content.
        /// @param contents the attributes and children of the element
        /// @return an instruction representing the element.
        public final ElementMarkup %s(Instruction... contents) {
          return elem0(ElementNamePojo.%s, contents);
        }
        """.formatted(elem.htmlName(), elem.htmlName(), elem.javaName()));
      }

      else if (elem.endTag()) {
        methods.append("""

        /// Encodes the `%s` element with the specified content.
        /// @param contents the attributes and children of the element
        /// @return an instruction representing the element.
        public final ElementMarkup %s(Instruction... contents) {
          return elem0(ElementNamePojo.%s, contents);
        }

        /// Encodes the `%s` element with the specified text.
        /// @param text the text value of the element
        /// @return an instruction representing the element.
        public final ElementMarkup %s(String text) {
          return elem0(ElementNamePojo.%s, text);
        }
        """.formatted(
            elem.htmlName(), elem.htmlName(), elem.javaName(),
            elem.htmlName(), elem.htmlName(), elem.javaName()
        ));
      }

      else {
        methods.append("""

        /// Encodes the `%s` element with the specified content.
        /// @param contents the attributes of the element
        /// @return an instruction representing the element.
        public final ElementMarkup %s(Instruction.OfVoid... contents) {
          return elem0(ElementNamePojo.%s, contents);
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

    abstract AttributeInstruction attr0(AttributeName name);

    abstract AttributeInstruction attr0(AttributeName name, Object value);

    /// Encodes a boolean attribute with the specified name.
    /// @param name the attribute name
    /// @return an instruction representing the attribute
    public final BooleanAttribute attr(AttributeName name) {
      Objects.requireNonNull(name, "name == null");
      return attr0(name);
    }

    /// Encodes an attribute with the specified name and value.
    /// @param name the attribute name
    /// @param value the attribute value
    /// @return an instruction representing the attribute
    public final AttributeInstruction attr(AttributeName name, String value) {
      Objects.requireNonNull(name, "name == null");
      return attr0(name, value);
    }
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
      /// @return an instruction representing the attribute
      public final AttributeInstruction %s(String value) {
        return attr0(AttributeNamePojo.%s, value);
      }
      """.formatted(attr.htmlName(), attr.methodName(), attr.constantName()));

      if (!attr.eventAttribute()) {
        continue;
      }

      methods.append("""

      /// Encodes the `%s` attribute with the specified action.
      /// @param value the action to be executed
      /// @return an instruction representing the attribute
      public final AttributeInstruction %s(JsAction value) {
        return attr0(AttributeNamePojo.%s, value);
      }
      """.formatted(attr.htmlName(), attr.methodName(), attr.constantName()));
    }
  }

  private void ambiguous() {
    methods.append("""

    //
    // AMBIGUOUS
    //

    abstract AmbiguousInstruction ambiguous(Ambiguous name, String text);
    """);

    for (AmbiguousSpec spec : HtmlSpec.ambiguous()) {
      methods.append("""

      /// Encodes the `%s` attribute or the `%s` element with the specified text.
      /// @param text the attribute value or the text content of the element
      /// @return an instruction representing the attribute or the element
      public final AmbiguousInstruction %s(String text) {
        return ambiguous(Ambiguous.%s, text);
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

      methods.append("/// ");

      methods.append(lines.next());
    }

    methods.append('\n');

    methods.append("public abstract ");

    methods.append(spec.sig());

    methods.append(';');

    methods.append('\n');
  }

}
