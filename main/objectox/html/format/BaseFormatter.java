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

public sealed abstract class BaseFormatter
    implements Formatter
    permits
    StandardFormatter,
    TestableFormatter {

  BaseFormatter() {}

  /// Formats the specified event into the specified output.
  ///
  /// @param event the event to be formatted
  /// @param out the output to append to
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

      case RenderTestable testable -> renderTestable(testable, out);

      case RenderText text -> renderText(text, out);
    }
  }

  abstract void beginStartTag(BeginStartTag event, Output out);

  abstract void endDocument(EndDocument event, Output out);

  abstract void endStartTag(EndStartTag event, Output out);

  abstract void renderAttribute(RenderAttribute attr, Output out);

  abstract void renderBooleanAttribute(RenderBooleanAttribute attr, Output out);

  abstract void renderDoctype(RenderDoctype event, Output out);

  abstract void renderEndTag(RenderEndTag endTag, Output out);

  abstract void renderJsActionAttribute(RenderJsActionAttribute attr, Output out);

  abstract void renderRaw(RenderRaw raw, Output out);

  abstract void renderTestable(RenderTestable event, Output out);

  abstract void renderText(RenderText text, Output out);

}
