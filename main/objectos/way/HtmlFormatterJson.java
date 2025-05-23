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

import java.io.IOException;

final class HtmlFormatterJson extends HtmlFormatter {

  @Override
  final void format(Html.Dom document, Appendable out) throws IOException {
    for (Html.Dom.Node node : document.nodes()) {
      node(out, node);
    }
  }

  private void node(Appendable out, Html.Dom.Node node) throws IOException {
    switch (node) {
      case Html.Dom.DocumentType doctype -> out.append("<!DOCTYPE html>");

      case Html.Dom.Element element -> element(out, element);

      case Html.Dom.Text text -> text(out, text);

      case Html.Dom.Raw raw -> raw(out, raw);

      default -> throw new UnsupportedOperationException(
          "Implement me :: type=" + node.getClass()
      );
    }
  }

  private void element(Appendable out, Html.Dom.Element element) throws IOException {
    String elementName;
    elementName = element.name();

    out.append('<');
    out.append(elementName);

    for (Html.Dom.Attribute attribute : element.attributes()) {
      attribute(out, attribute);
    }

    out.append('>');

    if (!element.isVoid()) {
      for (Html.Dom.Node node : element.nodes()) {
        node(out, node);
      }

      out.append('<');
      out.append('/');
      out.append(elementName);
      out.append('>');
    }
  }

  private void attribute(Appendable out, Html.Dom.Attribute attribute) throws IOException {
    String name;
    name = attribute.name();

    out.append(' ');
    out.append(name);

    if (attribute.booleanAttribute()) {
      return;
    }

    out.append('=');

    out.append('\'');

    attributeValue(out, attribute.value());

    out.append('\'');
  }

  private void attributeValue(Appendable out, String value) throws IOException {
    for (int idx = 0, len = value.length(); idx < len;) {
      char c;
      c = value.charAt(idx++);

      switch (c) {
        case '&' -> idx = ampersand(out, value, idx, len);

        case '\'' -> out.append("&#39;");

        default -> jsonEscapeIfNecessary(out, c);
      }
    }
  }

  private void text(Appendable out, Html.Dom.Text text) throws IOException {
    String value;
    value = text.value();

    for (int idx = 0, len = value.length(); idx < len;) {
      char c;
      c = value.charAt(idx++);

      switch (c) {
        case '&' -> idx = ampersand(out, value, idx, len);

        case '<' -> out.append("&lt;");

        case '>' -> out.append("&gt;");

        default -> jsonEscapeIfNecessary(out, c);
      }
    }
  }

  private void raw(Appendable out, Html.Dom.Raw raw) throws IOException {
    String value;
    value = raw.value();

    for (int idx = 0, len = value.length(); idx < len;) {
      char c;
      c = value.charAt(idx++);

      switch (c) {
        default -> jsonEscapeIfNecessary(out, c);
      }
    }
  }

  private static final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();

  private void jsonEscapeIfNecessary(Appendable out, char c) throws IOException {
    switch (c) {
      case '\b' -> out.append("\\b");
      case '\f' -> out.append("\\f");
      case '\n' -> out.append("\\n");
      case '\r' -> out.append("\\r");
      case '\t' -> out.append("\\t");
      case '\"' -> out.append("\\\"");
      default -> {
        if (c < 0x20) {
          out.append("\\u00");

          out.append(HEX_DIGITS[c >> 4]);
          out.append(HEX_DIGITS[c & 0xF]);
        } else {
          out.append(c);
        }
      }
    }
  }
}