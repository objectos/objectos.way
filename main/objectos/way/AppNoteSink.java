/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;
import objectos.way.Note.Long1Ref1;
import objectos.way.Note.Long1Ref2;
import objectos.way.Note.Marker;

sealed abstract class AppNoteSink implements App.NoteSink permits AppNoteSinkOfConsole, AppNoteSinkOfFile {

  private final Clock clock;

  private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

  private Predicate<Note> filter;

  private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

  private final Lock r = rwl.readLock();

  final Lock w = rwl.writeLock();

  AppNoteSink(Clock clock, Predicate<Note> filter) {
    this.clock = clock;

    this.filter = filter;
  }

  @Override
  public final void filter(Predicate<Note> value) {
    Objects.requireNonNull(value, "value == null");

    w.lock();
    try {
      filter = value;
    } finally {
      w.unlock();
    }
  }

  @Override
  public final boolean isEnabled(Note note) {
    if (note == null) {
      return false;
    }

    return test(note);
  }

  @Override
  public final void send(Note.Int1 note, int value) {
    if (note == null) {
      return;
    }

    if (!test(note)) {
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

    if (!test(note)) {
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

    if (!test(note)) {
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

    if (!test(note)) {
      return;
    }

    StringBuilder out;
    out = new StringBuilder();

    format(out, note);

    formatLong(out, value);

    write(out);
  }

  @Override
  public final <T1> void send(Long1Ref1<T1> note, long value1, T1 value2) {
    if (note == null) {
      return;
    }

    if (!test(note)) {
      return;
    }

    StringBuilder out;
    out = new StringBuilder();

    int length;
    length = format(out, note);

    formatLong(out, value1);

    formatLastValue(out, length, value2);

    write(out);
  }

  @Override
  public final <T1, T2> void send(Long1Ref2<T1, T2> note, long value1, T1 value2, T2 value3) {
    if (note == null) {
      return;
    }

    if (!test(note)) {
      return;
    }

    StringBuilder out;
    out = new StringBuilder();

    int length;
    length = format(out, note);

    formatLong(out, value1);

    length = formatValue(out, length, value2);

    formatLastValue(out, length, value3);

    write(out);
  }

  @Override
  public final void send(Note.Long2 note, long value1, long value2) {
    if (note == null) {
      return;
    }

    if (!test(note)) {
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

    if (!test(note)) {
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

    if (!test(note)) {
      return;
    }

    StringBuilder out;
    out = new StringBuilder();

    int length;
    length = format(out, note);

    formatLastValue(out, length, value);

    write(out);
  }

  @Override
  public final <T1, T2> void send(Note.Ref2<T1, T2> note, T1 value1, T2 value2) {
    if (note == null) {
      return;
    }

    if (!test(note)) {
      return;
    }

    StringBuilder out;
    out = new StringBuilder();

    int length;
    length = format(out, note);

    length = formatValue(out, length, value1);

    formatLastValue(out, length, value2);

    write(out);
  }

  @Override
  public final <T1, T2, T3> void send(Note.Ref3<T1, T2, T3> note, T1 value1, T2 value2, T3 value3) {
    if (note == null) {
      return;
    }

    if (!test(note)) {
      return;
    }

    StringBuilder out;
    out = new StringBuilder();

    int length;
    length = format(out, note);

    length = formatValue(out, length, value1);

    length = formatValue(out, length, value2);

    formatLastValue(out, length, value3);

    write(out);
  }

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

  abstract void writeBytes(byte[] bytes);

  private boolean test(Note note) {
    r.lock();
    try {
      return filter.test(note);
    } finally {
      r.unlock();
    }
  }

  private int format(StringBuilder out, Note note) {
    return format(
        out,

        note.marker(),

        note.source(),

        note.key()
    );
  }

  private int format(StringBuilder out, Marker marker, String source, String key) {
    final LocalDateTime date;
    date = LocalDateTime.now(clock);

    dateFormat.formatTo(date, out);

    out.append(' ');

    final String markerName;
    markerName = marker.name();

    pad(out, markerName, 5);

    out.append(' ');

    out.append('[');

    final Thread thread;
    thread = Thread.currentThread();

    final String threadName;
    threadName = thread.getName();

    pad(out, threadName, 15);

    out.append(']');

    out.append(' ');

    pad(out, source, 40);

    out.append(' ');
    out.append(':');
    out.append(' ');

    final int length;
    length = out.length();

    out.append(key);

    return length;
  }

  private void formatInt(StringBuilder out, int value) {
    out.append(' ');

    out.append(value);
  }

  private void formatLong(StringBuilder out, long value) {
    out.append(' ');

    out.append(value);
  }

  private int formatValue(StringBuilder out, int length, Object value) {
    if (out.length() != length) {
      out.append(' ');
    }

    int result;
    result = out.length();

    out.append(value);

    return result;
  }

  private void formatLastValue(StringBuilder out, int length, Object value) {
    if (value instanceof Throwable t) {
      formatThrowable(out, t);
    } else {
      formatValue(out, length, value);
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
    int valueLength;
    valueLength = value.length();

    if (valueLength > length) {
      out.append(value, 0, length);

      valueLength = length;
    } else {
      out.append(value);

      int pad;
      pad = length - valueLength;

      for (int i = 0; i < pad; i++) {
        out.append(' ');
      }
    }
  }

  private void write(StringBuilder out) {
    out.append('\n');

    String s;
    s = out.toString();

    byte[] bytes;
    bytes = s.getBytes(StandardCharsets.UTF_8);

    writeBytes(bytes);
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