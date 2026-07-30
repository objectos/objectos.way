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

import java.io.IOException;
import objectos.html.format.Formatter;
import objectos.html.stream.BeginDocument;
import objectos.html.stream.BeginStartTag;
import objectos.html.stream.EndDocument;
import objectos.html.stream.EndStartTag;
import objectos.html.stream.Event;
import objectos.html.stream.RenderAttribute;
import objectos.html.stream.RenderBooleanAttribute;
import objectos.html.stream.RenderEndTag;

public final class FormatterPojo implements Formatter {

  private final BeginDocumentFormatter beginDocumentFormatter;

  private final EndDocumentFormatter endDocumentFormatter;

  FormatterPojo(
      BeginDocumentFormatter beginDocumentFormatter,

      EndDocumentFormatter endDocumentFormatter
  ) {
    this.beginDocumentFormatter = beginDocumentFormatter;

    this.endDocumentFormatter = endDocumentFormatter;
  }

  @Override
  public final void formatTo(Event event, Appendable out) throws IOException {
    switch (event) {
      case BeginDocument begin -> beginDocumentFormatter.format(begin, out);

      case BeginStartTag begin -> beginStartTag(begin, out);

      case EndStartTag end -> endStartTag(end, out);

      case RenderAttribute attr -> format(attr, out);

      case RenderBooleanAttribute attr -> format(attr, out);

      case RenderEndTag endTag -> renderEndTag(endTag, out);

      case EndDocument end -> endDocumentFormatter.format(end, out);
    }
  }

  private void beginStartTag(BeginStartTag event, Appendable out) throws IOException {
    out.append('<');

    out.append(event.name());
  }

  private void endStartTag(EndStartTag event, Appendable out) throws IOException {
    out.append('>');
  }

  private void renderEndTag(RenderEndTag event, Appendable out) throws IOException {
    out.append("</");

    out.append(event.name());

    out.append('>');
  }

  private void format(Event event, Appendable out) {
    throw new UnsupportedOperationException("Implement me :: type=" + event.getClass().getSimpleName());
  }

}
