/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;
import java.util.function.Predicate;
import objectos.way.Note.Marker;

final class AppNoteSink implements App.LoggerAdapter, Note.Sink {

  private final Clock clock;

  private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

  private final Predicate<Note> filter;

  private final Consumer<byte[]> sink;

  AppNoteSink(Clock clock, Predicate<Note> filter, Consumer<byte[]> sink) {
    this.clock = clock;

    this.filter = filter;

    this.sink = sink;
  }

  @Override
  public final boolean isEnabled(Note note) {
    if (note == null) {
      return false;
    }

    return filter.test(note);
  }

  @Override
  public final void send(Note.Int1 note, int value) {
    if (note == null) {
      return;
    }

    if (!filter.test(note)) {
      return;
    }

    StringBuilder out;
    out = new StringBuilder();

    format(out, note);

    formatInt(out, value);

    write(out);
  }

  @Override
  public final void send(Note.Int2 note, int value1, int value2) {
    if (note == null) {
      return;
    }

    if (!filter.test(note)) {
      return;
    }

    StringBuilder out;
    out = new StringBuilder();

    format(out, note);

    formatInt(out, value1);

    formatInt(out, value2);

    write(out);
  }

  @Override
  public final void send(Note.Int3 note, int value1, int value2, int value3) {
    if (note == null) {
      return;
    }

    if (!filter.test(note)) {
      return;
    }

    StringBuilder out;
    out = new StringBuilder();

    format(out, note);

    formatInt(out, value1);

    formatInt(out, value2);

    formatInt(out, value3);

    write(out);
  }

  @Override
  public final void send(Note.Long1 note, long value) {
    if (note == null) {
      return;
    }

    if (!filter.test(note)) {
      return;
    }

    StringBuilder out;
    out = new StringBuilder();

    format(out, note);

    formatLong(out, value);

    write(out);
  }

  @Override
  public final void send(Note.Long2 note, long value1, long value2) {
    if (note == null) {
      return;
    }

    if (!filter.test(note)) {
      return;
    }

    StringBuilder out;
    out = new StringBuilder();

    format(out, note);

    formatLong(out, value1);

    formatLong(out, value2);

    write(out);
  }

  @Override
  public final void send(Note.Ref0 note) {
    if (note == null) {
      return;
    }

    if (!filter.test(note)) {
      return;
    }

    StringBuilder out;
    out = new StringBuilder();

    format(out, note);

    write(out);
  }

  @Override
  public final <T1> void send(Note.Ref1<T1> note, T1 value) {
    if (note == null) {
      return;
    }

    if (!filter.test(note)) {
      return;
    }

    StringBuilder out;
    out = new StringBuilder();

    format(out, note);

    formatLastValue(out, value);

    write(out);
  }

  @Override
  public final <T1, T2> void send(Note.Ref2<T1, T2> note, T1 value1, T2 value2) {
    if (note == null) {
      return;
    }

    if (!filter.test(note)) {
      return;
    }

    StringBuilder out;
    out = new StringBuilder();

    format(out, note);

    formatValue(out, value1);

    formatLastValue(out, value2);

    write(out);
  }

  @Override
  public final <T1, T2, T3> void send(Note.Ref3<T1, T2, T3> note, T1 value1, T2 value2, T3 value3) {
    if (note == null) {
      return;
    }

    if (!filter.test(note)) {
      return;
    }

    StringBuilder out;
    out = new StringBuilder();

    format(out, note);

    formatValue(out, value1);

    formatValue(out, value2);

    formatLastValue(out, value3);

    write(out);
  }

  @Override
  public final void log(String name, Marker level, String message) {
    if (level == null) {
      return;
    }

    StringBuilder out;
    out = new StringBuilder();

    format(
        out,

        level,

        String.valueOf(name),

        String.valueOf(message)
    );

    write(out);
  }

  @Override
  public final void log(String name, Marker level, String message, Throwable t) {
    if (level == null) {
      return;
    }

    StringBuilder out;
    out = new StringBuilder();

    format(
        out,

        level,

        String.valueOf(name),

        String.valueOf(message)
    );

    if (t != null) {
      formatThrowable(out, t);
    }

    write(out);
  }

  private void format(StringBuilder out, Note note) {
    format(
        out,

        note.marker0,

        note.source,

        note.key
    );
  }

  private void format(StringBuilder out, Marker marker, String source, String key) {
    LocalDateTime date;
    date = LocalDateTime.now(clock);

    out.append(dateFormat.format(date));

    out.append(' ');

    String markerName;
    markerName = marker.name();

    pad(out, markerName, 5);

    out.append(" --- ");

    out.append('[');

    Thread thread;
    thread = Thread.currentThread();

    String threadName;
    threadName = thread.getName();

    pad(out, threadName, 15);

    out.append(']');

    out.append(' ');

    pad(out, source, 40);

    out.append(' ');
    out.append(':');
    out.append(' ');

    out.append(key);
  }

  private void formatInt(StringBuilder out, int value) {
    out.append(' ');

    out.append(value);
  }

  private void formatLong(StringBuilder out, long value) {
    out.append(' ');

    out.append(value);
  }

  private void formatValue(StringBuilder out, Object value) {
    out.append(' ');

    out.append(value);
  }

  private void formatLastValue(StringBuilder out, Object value) {
    if (value instanceof Throwable t) {
      formatThrowable(out, t);
    } else {
      formatValue(out, value);
    }
  }

  private void formatThrowable(StringBuilder out, Throwable t) {
    out.append('\n');

    StringBuilderWriter writer;
    writer = new StringBuilderWriter(out);

    PrintWriter printWriter;
    printWriter = new PrintWriter(writer);

    t.printStackTrace(printWriter);
  }

  private void pad(StringBuilder out, String value, int length) {
    String result;
    result = value;

    if (result.length() > length) {
      result = result.substring(0, length);
    }

    out.append(result);

    int pad;
    pad = length - result.length();

    for (int i = 0; i < pad; i++) {
      out.append(' ');
    }
  }

  private void write(StringBuilder out) {
    out.append('\n');

    String s;
    s = out.toString();

    byte[] bytes;
    bytes = s.getBytes(StandardCharsets.UTF_8);

    sink.accept(bytes);
  }

  private static class StringBuilderWriter extends Writer {

    private final StringBuilder out;

    public StringBuilderWriter(StringBuilder out) {
      this.out = out;
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
      out.append(cbuf, off, len);
    }

    @Override
    public void flush() {
      // noop, not buffered
    }

    @Override
    public void close() {
      // noop, in-memory only
    }

  }

}