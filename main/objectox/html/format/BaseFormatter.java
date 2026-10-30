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
import objectos.html.stream.Event;

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

      case EmitAttribute attr -> emitAttribute(attr, out);

      case EmitBooleanAttribute attr -> emitBooleanAttribute(attr, out);

      case EmitJsActionAttribute attr -> emitJsActionAttribute(attr, out);

      case EmitDoctype doctype -> emitDoctype(doctype, out);

      case EmitEndTag endTag -> emitEndTag(endTag, out);

      case EmitRaw raw -> emitRaw(raw, out);

      case EmitTestable testable -> emitTestable(testable, out);

      case EmitText text -> emitText(text, out);
    }
  }

  abstract void beginStartTag(BeginStartTag event, Output out);

  abstract void emitAttribute(EmitAttribute attr, Output out);

  abstract void emitBooleanAttribute(EmitBooleanAttribute attr, Output out);

  abstract void emitDoctype(EmitDoctype event, Output out);

  abstract void emitEndTag(EmitEndTag endTag, Output out);

  abstract void emitJsActionAttribute(EmitJsActionAttribute attr, Output out);

  abstract void emitRaw(EmitRaw raw, Output out);

  abstract void emitTestable(EmitTestable event, Output out);

  abstract void emitText(EmitText text, Output out);

  abstract void endDocument(EndDocument event, Output out);

  abstract void endStartTag(EndStartTag event, Output out);

}
