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
import objectos.html.stream.EndStartTag;
import objectos.html.stream.Event;
import objectos.html.stream.RenderAttribute;
import objectos.html.stream.RenderBooleanAttribute;
import objectos.html.stream.RenderDoctype;
import objectos.html.stream.RenderEndTag;
import objectos.html.stream.RenderText;

public final class FormatterPojo implements Formatter {

  private final BeginStartTagFormatter beginStartTagFormatter;

  private final EndStartTagFormatter endStartTagFormatter;

  private final RenderAttributeFormatter renderAttributeFormatter;

  private final RenderBooleanAttributeFormatter renderBooleanAttributeFormatter;

  private final RenderDoctypeFormatter renderDoctypeFormatter;

  private final RenderEndTagFormatter renderEndTagFormatter;

  private final RenderTextFormatter renderTextFormatter;

  FormatterPojo(
      BeginStartTagFormatter beginStartTagFormatter,

      EndStartTagFormatter endStartTagFormatter,

      RenderAttributeFormatter renderAttributeFormatter,

      RenderBooleanAttributeFormatter renderBooleanAttributeFormatter,

      RenderDoctypeFormatter renderDoctypeFormatter,

      RenderEndTagFormatter renderEndTagFormatter,

      RenderTextFormatter renderTextFormatter
  ) {
    this.beginStartTagFormatter = beginStartTagFormatter;

    this.endStartTagFormatter = endStartTagFormatter;

    this.renderAttributeFormatter = renderAttributeFormatter;

    this.renderBooleanAttributeFormatter = renderBooleanAttributeFormatter;

    this.renderDoctypeFormatter = renderDoctypeFormatter;

    this.renderEndTagFormatter = renderEndTagFormatter;

    this.renderTextFormatter = renderTextFormatter;
  }

  public final void formatTo(Event event, Output out) {
    switch (event) {
      case BeginStartTag begin -> beginStartTagFormatter.format(begin, out);

      case EndStartTag end -> endStartTagFormatter.format(end, out);

      case RenderAttribute attr -> renderAttributeFormatter.format(attr, out);

      case RenderBooleanAttribute attr -> renderBooleanAttributeFormatter.format(attr, out);

      case RenderDoctype doctype -> renderDoctypeFormatter.format(doctype, out);

      case RenderEndTag endTag -> renderEndTagFormatter.format(endTag, out);

      case RenderText text -> renderTextFormatter.format(text, out);
    }
  }

}
