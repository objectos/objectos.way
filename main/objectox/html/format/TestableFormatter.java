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
import objectos.html.stream.EmitTestableCell;
import objectos.html.stream.EmitTestableHeading;
import objectos.html.stream.EmitText;
import objectos.html.stream.EndDocument;
import objectos.html.stream.EndStartTag;

public final class TestableFormatter extends BaseFormatter {

  public TestableFormatter() {}

  @Override
  final void beginStartTag(BeginStartTag event, Output out) {}

  @Override
  final void emitAttribute(EmitAttribute attr, Output out) {}

  @Override
  final void emitBooleanAttribute(EmitBooleanAttribute attr, Output out) {}

  @Override
  final void emitDoctype(EmitDoctype event, Output out) {}

  @Override
  final void emitEndTag(EmitEndTag endTag, Output out) {}

  @Override
  final void emitJsActionAttribute(EmitJsActionAttribute attr, Output out) {}

  @Override
  final void emitRaw(EmitRaw raw, Output out) {}

  @Override
  final void emitTestable(EmitTestable event, Output out) {
    switch (event) {
      case EmitTestableCell _ -> {}

      case EmitTestableHeading heading -> emitHeading(heading, out);
    }
  }

  @Override
  final void emitText(EmitText text, Output out) {}

  @Override
  final void endDocument(EndDocument event, Output out) {}

  @Override
  final void endStartTag(EndStartTag event, Output out) {}

  private void emitHeading(EmitTestableHeading heading, Output out) {
    final int level;
    level = heading.heading();

    for (int i = 0; i < level; i++) {
      out.append('#');
    }

    out.append(' ');

    final String value;
    value = heading.value();

    out.append(value);

    out.append('\n');

    out.append('\n');
  }

}
