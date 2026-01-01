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
package objectos.way;

/// The **Objectos DOM** main class, part of Objectos HTML.
public final class Dom {

  /// An attribute of an HTML document.
  public sealed interface Attribute permits DomAttribute {

    /// Tests if the name of this attribute is equal to the specified value.
    /// @param value the value to compare against the name of this attribute
    /// @return `true` if the name of this attribute is equal to the
    ///         specified value or `false` otherwise
    default boolean hasName(String value) {
      return name().equals(value);
    }

    /// Returns `true` if this object represents a boolean attribute; `false` otherwise.
    /// @return `true` if this object represents a boolean attribute; `false` otherwise
    boolean booleanAttribute();

    /// Returns `true` if the value of this attribute should be enclosed in single quotes and `false` otherwise.
    /// @return `true` if the value of this attribute should be enclosed in single quotes and `false` otherwise
    boolean singleQuoted();

    /// Returns the name of this attribute.
    /// @return the name of this attribute.
    String name();

    /// Returns the value of this attribute or `null` if this is a boolean attribute.
    /// @return the value of this attribute or `null` if this is a boolean attribute
    String value();

  }

  /// A DOM-like view of an HTML document.
  /// It allows for a one-pass, one-direction traversal of the document.
  public sealed interface Document permits DomDocument {

    /// Models the document type tag of an HTML document.
    sealed interface Type extends Node permits DomDocument.Type {
      /// Returns the document type tag, such as `<!DOCTYPE html>`.
      /// @return the document type tag, such as `<!DOCTYPE html>`
      String value();
    }

    /// Creates a `Document` representing the HTML document declared by the specified markup.
    /// @param markup the markup instance
    /// @return a newly created document object
    static Dom.Document of(Html.Markup.OfHtml markup) {
      final HtmlMarkupOfHtml impl;
      impl = markup;

      return impl.compile();
    }

    /// Creates a `Document` representing the specified HTML template.
    /// @param template the HTML template
    /// @return a newly created document object
    static Dom.Document of(Html.Template template) {
      final Html.Markup.OfHtml html;
      html = new Html.Markup.OfHtml();

      template.renderHtml(html);

      return of(html);
    }

    /// Returns the nodes of this HTML document.
    /// Please note that the returned `Iterable` can be traversed only once.
    /// @return the nodes of this HTML document.
    Lang.IterableOnce<Node> nodes();

  }

  /// An element of an HTML document.
  public sealed interface Element extends Node permits DomElement {

    /// Returns the attributes of this element.
    /// @return the attributes of this element.
    Lang.IterableOnce<Dom.Attribute> attributes();

    /// Tests if the name of this element is equal to the specified value.
    /// @param value the value to compare against the name of this element
    /// @return `true` if the name of this element is equal to the
    ///         specified value or `false` otherwise
    default boolean hasName(String value) {
      return name().equals(value);
    }

    /// Returns the name of this element.
    /// @return the name of this element
    String name();

    /// Return the child nodes of this element.
    /// @return the child nodes of this element
    Lang.IterableOnce<Dom.Node> nodes();

    /// Returns `true` if this object represents a void element; `false` otherwise.
    /// @return `true` if this object represents a void element; `false` otherwise
    boolean voidElement();

  }

  /// A node of an HTML document.
  public sealed interface Node {}

  /// A raw HTML node of an HTML document.
  public sealed interface Raw extends Node permits DomRaw {

    /// Return the value of this raw HTML node.
    /// @return the value of this raw HTML node.
    String value();

  }

  /// A text node of an HTML document.
  public sealed interface Text extends Node permits DomText {

    /// Return the value of this text node.
    /// @return the value of this text node.
    String value();

  }

  private Dom() {}

}
