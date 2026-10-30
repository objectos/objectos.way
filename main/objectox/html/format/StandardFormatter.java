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

import objectos.html.stream.BeginStartTag;
import objectos.html.stream.EmitAttribute;
import objectos.html.stream.EmitBooleanAttribute;
import objectos.html.stream.EmitDoctype;
import objectos.html.stream.EmitEndTag;
import objectos.html.stream.EmitJsActionAttribute;
import objectos.html.stream.EmitRaw;
import objectos.html.stream.EmitTestable;
import objectos.html.stream.EmitText;
import objectos.html.stream.EndDocument;
import objectos.html.stream.EndStartTag;
import objectox.html.markup.CommonEscaper;

public final class StandardFormatter extends BaseFormatter {

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

  @Override
  final void beginStartTag(BeginStartTag event, Output out) {
    final String name;
    name = event.name();

    out.append('<');

    out.append(name);
  }

  @Override
  final void emitAttribute(EmitAttribute attr, Output out) {
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

  @Override
  final void emitBooleanAttribute(EmitBooleanAttribute attr, Output out) {
    final String name;
    name = attr.name();

    out.append(' ');

    out.append(name);
  }

  @Override
  final void emitDoctype(EmitDoctype event, Output out) {
    out.append("<!DOCTYPE html>");
  }

  @Override
  final void emitEndTag(EmitEndTag endTag, Output out) {
    final String name;
    name = endTag.name();

    out.append("</");

    out.append(name);

    out.append('>');
  }

  @Override
  final void emitJsActionAttribute(EmitJsActionAttribute attr, Output out) {
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

  @Override
  final void emitRaw(EmitRaw raw, Output out) {
    final String value;
    value = raw.value();

    out.append(value);
  }

  @Override
  final void emitTestable(EmitTestable event, Output out) {}

  @Override
  final void emitText(EmitText text, Output out) {
    final String value;
    value = text.value();

    final String escaped;
    escaped = TEXT_ESCAPER.escape(value);

    out.append(escaped);
  }

  @Override
  final void endDocument(EndDocument event, Output out) {
    if (event.empty()) {
      return;
    }

    out.append('\n');
  }

  @Override
  final void endStartTag(EndStartTag event, Output out) {
    out.append('>');
  }

}
