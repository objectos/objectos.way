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
import objectos.html.stream.EndDocument;
import objectos.html.stream.EndStartTag;
import objectos.html.stream.RenderAttribute;
import objectos.html.stream.RenderBooleanAttribute;
import objectos.html.stream.RenderDoctype;
import objectos.html.stream.RenderEndTag;
import objectos.html.stream.RenderJsActionAttribute;
import objectos.html.stream.RenderRaw;
import objectos.html.stream.RenderTestable;
import objectos.html.stream.RenderText;
import objectos.html.stream.TestableCell;
import objectos.html.stream.TestableEntryName;
import objectos.html.stream.TestableEntryValue;
import objectos.html.stream.TestableHeading;
import objectos.html.stream.TestableNewLine;
import objectos.html.stream.TestableProp;

public final class TestableFormatter extends BaseFormatter {

  public TestableFormatter() {}

  @Override
  final void beginStartTag(BeginStartTag event, Output out) {}

  @Override
  final void endDocument(EndDocument event, Output out) {}

  @Override
  final void endStartTag(EndStartTag event, Output out) {}

  @Override
  final void renderAttribute(RenderAttribute attr, Output out) {}

  @Override
  final void renderBooleanAttribute(RenderBooleanAttribute attr, Output out) {}

  @Override
  final void renderDoctype(RenderDoctype event, Output out) {}

  @Override
  final void renderEndTag(RenderEndTag endTag, Output out) {}

  @Override
  final void renderJsActionAttribute(RenderJsActionAttribute attr, Output out) {}

  @Override
  final void renderRaw(RenderRaw raw, Output out) {}

  @Override
  final void renderTestable(RenderTestable event, Output out) {
    switch (event) {
      case TestableCell _ -> {}

      case TestableEntryName entry -> renderEntryName(entry, out);

      case TestableEntryValue entry -> renderEntryValue(entry, out);

      case TestableHeading heading -> renderHeading(heading, out);

      case TestableNewLine _ -> renderNewLine(out);

      case TestableProp prop -> renderProp(prop, out);
    }
  }

  @Override
  final void renderText(RenderText text, Output out) {}

  private void renderEntryName(TestableEntryName entry, Output out) {
    final String value;
    value = entry.value();

    out.append(value);

    out.append(':');

    out.append(' ');
  }

  private void renderEntryValue(TestableEntryValue entry, Output out) {
    final String value;
    value = entry.value();

    out.append(value);

    out.append('\n');
  }

  private void renderHeading(TestableHeading heading, Output out) {
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

  private void renderNewLine(Output out) {
    out.append('\n');
  }

  private void renderProp(TestableProp prop, Output out) {
    final String key;
    key = prop.key();

    out.append(key);

    out.append(':');

    out.append(' ');

    final String value;
    value = prop.value();

    out.append(value);

    out.append('\n');
  }

}
