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
package objectox.html.format;

import objectos.html.format.Formatter;
import objectos.html.stream.BeginStartTag;
import objectos.html.stream.EndDocument;
import objectos.html.stream.EndStartTag;
import objectos.html.stream.Event;
import objectos.html.stream.RenderAttribute;
import objectos.html.stream.RenderBooleanAttribute;
import objectos.html.stream.RenderDoctype;
import objectos.html.stream.RenderEndTag;
import objectos.html.stream.RenderJsActionAttribute;
import objectos.html.stream.RenderRaw;
import objectos.html.stream.RenderTestable;
import objectos.html.stream.RenderText;
import objectox.html.markup.CommonEscaper;

public final class StandardFormatter implements Formatter {

  private static final CommonEscaper ATTRIBUTE_ESCAPER = CommonEscaper.create(opts -> {
    opts.add('&', "&amp;");
    opts.add('<', "&lt;");
    opts.add('>', "&gt;");
    opts.add('"', "&quot;");
  });

  private static final CommonEscaper JS_ACTION_ESCAPER = CommonEscaper.create(opts -> {
    opts.add('&', "&amp;");
    opts.add('<', "&lt;");
    opts.add('>', "&gt;");
    opts.add('\'', "&#39;");
  });

  private static final CommonEscaper TEXT_ESCAPER = CommonEscaper.create(opts -> {
    opts.add('&', "&amp;");
    opts.add('<', "&lt;");
    opts.add('>', "&gt;");
  });

  public StandardFormatter() {}

  public final void formatTo(Event event, Output out) {
    switch (event) {
      case BeginStartTag begin -> beginStartTag(begin, out);

      case EndDocument end -> endDocument(end, out);

      case EndStartTag end -> endStartTag(end, out);

      case RenderAttribute attr -> renderAttribute(attr, out);

      case RenderBooleanAttribute attr -> renderBooleanAttribute(attr, out);

      case RenderJsActionAttribute attr -> renderJsActionAttribute(attr, out);

      case RenderDoctype doctype -> renderDoctype(doctype, out);

      case RenderEndTag endTag -> renderEndTag(endTag, out);

      case RenderRaw raw -> renderRaw(raw, out);

      case RenderTestable _ -> {}

      case RenderText text -> renderText(text, out);
    }
  }

  private void beginStartTag(BeginStartTag event, Output out) {
    final String name;
    name = event.name();

    out.append('<');

    out.append(name);
  }

  private void endDocument(EndDocument event, Output out) {
    if (event.empty()) {
      return;
    }

    out.append('\n');
  }

  private void endStartTag(EndStartTag event, Output out) {
    out.append('>');
  }

  private void renderAttribute(RenderAttribute attr, Output out) {
    final String name;
    name = attr.name();

    out.append(' ');

    out.append(name);

    out.append('=');

    out.append('"');

    final String value;
    value = attr.value();

    final String escaped;
    escaped = ATTRIBUTE_ESCAPER.escape(value);

    out.append(escaped);

    out.append('"');
  }

  private void renderBooleanAttribute(RenderBooleanAttribute attr, Output out) {
    final String name;
    name = attr.name();

    out.append(' ');

    out.append(name);
  }

  private void renderDoctype(RenderDoctype event, Output out) {
    out.append("<!DOCTYPE html>");
  }

  private void renderEndTag(RenderEndTag endTag, Output out) {
    final String name;
    name = endTag.name();

    out.append("</");

    out.append(name);

    out.append('>');
  }

  private void renderJsActionAttribute(RenderJsActionAttribute attr, Output out) {
    final String name;
    name = attr.name();

    out.append(' ');

    out.append(name);

    out.append('=');

    out.append('\'');

    final String value;
    value = attr.value();

    final String escaped;
    escaped = JS_ACTION_ESCAPER.escape(value);

    out.append(escaped);

    out.append('\'');
  }

  private void renderRaw(RenderRaw raw, Output out) {
    final String value;
    value = raw.value();

    out.append(value);
  }

  private void renderText(RenderText text, Output out) {
    final String value;
    value = text.value();

    final String escaped;
    escaped = TEXT_ESCAPER.escape(value);

    out.append(escaped);
  }

}
