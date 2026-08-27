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
package objectox.html;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.Objects;
import objectos.html.stream.Event;
import objectox.html.format.FormatterImpl;
import objectox.html.format.Output;

public final class HtmlReader extends Reader {

  private boolean closed;

  private final Iterator<Event> events;

  private final FormatterImpl formatter;

  private String overflow = "";

  HtmlReader(Iterator<Event> events, FormatterImpl formatter) {
    this.events = events;

    this.formatter = formatter;
  }

  @Override
  public final void close() throws IOException {
    closed = true;
  }

  @Override
  public final int read(char[] cbuf, int off, int len) throws IOException {
    ensureOpen();

    Objects.checkFromIndexSize(off, len, cbuf.length);

    if (len == 0) {
      return 0;
    }

    final Output out;
    out = Output.wrap(cbuf, off, len);

    out.append(overflow);

    while (!out.exhausted() && events.hasNext()) {
      final Event event;
      event = events.next();

      formatter.formatTo(event, out);
    }

    overflow = out.overflow();

    return out.written();
  }

  private void ensureOpen() throws IOException {
    if (closed) {
      throw new IOException("Stream closed");
    }
  }

}
