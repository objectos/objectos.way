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
package objectos.way;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

final class AppNoteSink implements App.NoteSink {

  private class ThisPrintWriter extends PrintWriter {

    ThisPrintWriter() {
      super(new Writer() {
        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {/* noop */}

        @Override
        public void flush() throws IOException {/* noop */}

        @Override
        public void close() throws IOException {/* noop */}
      });
    }

    @Override
    public final void println(Object x) {
      // bypass PrintWriter lock as AppNoteSink is already synchronized
      final String s;
      s = String.valueOf(x);

      println(s);
    }

    @Override
    public final void println(String x) {
      // bypass PrintWriter lock as AppNoteSink is already synchronized
      try {
        AppNoteSink.this.out.append(x);
        AppNoteSink.this.out.append('\n');
      } catch (IOException e) {
        error(e);
      }
    }

  }

  private final Clock clock;

  private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

  private IOException error;

  private final Predicate<? super Note> filter;

  private final Flushable flushable;

  private final Lock lock = new ReentrantLock();

  private final Appendable out;

  private final PrintWriter printWriter = new ThisPrintWriter();

  AppNoteSink(AppNoteSinkBuilder builder, Appendable out) {
    clock = builder.clock;

    filter = builder.filter;

    if (out instanceof Flushable f) {
      flushable = f;
    } else {
      flushable = null;
    }

    this.out = out;
  }

  private void error(IOException e) {
    // not much we can do here

    e.printStackTrace();

    error = e;
  }

  @Override
  public final void close() throws IOException {
    if (out instanceof Closeable c) {
      c.close();
    }
  }

  @Override
  public final boolean isEnabled(Note note) {
    if (note == null) {
      return false;
    }

    lock.lock();
    try {
      return test(note);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public final void send(Note.Int1 note, int value) {
    if (note == null) {
      return;
    }

    lock.lock();
    try {
      if (!test(note)) {
        return;
      }

      int length;
      length = format(note);

      formatLastValue(length, value);

      flush();
    } catch (IOException e) {
      error(e);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public final void send(Note.Int2 note, int value1, int value2) {
    if (note == null) {
      return;
    }

    lock.lock();
    try {
      if (!test(note)) {
        return;
      }

      int length;
      length = format(note);

      length = formatValue(length, value1);

      formatLastValue(length, value2);

      flush();
    } catch (IOException e) {
      error(e);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public final void send(Note.Int3 note, int value1, int value2, int value3) {
    if (note == null) {
      return;
    }

    lock.lock();
    try {
      if (!test(note)) {
        return;
      }

      int length;
      length = format(note);

      length = formatValue(length, value1);

      length = formatValue(length, value2);

      formatLastValue(length, value3);

      flush();
    } catch (IOException e) {
      error(e);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public final void send(Note.Long1 note, long value) {
    if (note == null) {
      return;
    }

    lock.lock();
    try {
      if (!test(note)) {
        return;
      }

      int length;
      length = format(note);

      formatLastValue(length, value);

      flush();
    } catch (IOException e) {
      error(e);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public final <T1> void send(Note.Long1Ref1<T1> note, long value1, T1 value2) {
    if (note == null) {
      return;
    }

    lock.lock();
    try {
      if (!test(note)) {
        return;
      }

      int length;
      length = format(note);

      length = formatValue(length, value1);

      formatLastValue(length, value2);

      flush();
    } catch (IOException e) {
      error(e);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public final <T1, T2> void send(Note.Long1Ref2<T1, T2> note, long value1, T1 value2, T2 value3) {
    if (note == null) {
      return;
    }

    lock.lock();
    try {
      if (!test(note)) {
        return;
      }

      int length;
      length = format(note);

      length = formatValue(length, value1);

      length = formatValue(length, value2);

      formatLastValue(length, value3);

      flush();
    } catch (IOException e) {
      error(e);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public final void send(Note.Long2 note, long value1, long value2) {
    if (note == null) {
      return;
    }

    lock.lock();
    try {
      if (!test(note)) {
        return;
      }

      int length;
      length = format(note);

      length = formatValue(length, value1);

      formatLastValue(length, value2);

      flush();
    } catch (IOException e) {
      error(e);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public final void send(Note.Ref0 note) {
    if (note == null) {
      return;
    }

    lock.lock();
    try {
      if (!test(note)) {
        return;
      }

      format(note);

      out.append('\n');

      flush();
    } catch (IOException e) {
      error(e);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public final <T1> void send(Note.Ref1<T1> note, T1 value) {
    if (note == null) {
      return;
    }

    lock.lock();
    try {
      if (!test(note)) {
        return;
      }

      int length;
      length = format(note);

      formatLastValue(length, value);

      flush();
    } catch (IOException e) {
      error(e);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public final <T1, T2> void send(Note.Ref2<T1, T2> note, T1 value1, T2 value2) {
    if (note == null) {
      return;
    }

    lock.lock();
    try {
      if (!test(note)) {
        return;
      }

      int length;
      length = format(note);

      length = formatValue(length, value1);

      formatLastValue(length, value2);

      flush();
    } catch (IOException e) {
      error(e);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public final <T1, T2, T3> void send(Note.Ref3<T1, T2, T3> note, T1 value1, T2 value2, T3 value3) {
    if (note == null) {
      return;
    }

    lock.lock();
    try {
      if (!test(note)) {
        return;
      }

      int length;
      length = format(note);

      length = formatValue(length, value1);

      length = formatValue(length, value2);

      formatLastValue(length, value3);

      flush();
    } catch (IOException e) {
      error(e);
    } finally {
      lock.unlock();
    }
  }

  public final void log(String name, Note.Marker level, String message) {
    if (level == null) {
      return;
    }

    lock.lock();
    try {
      format(
          level,

          String.valueOf(name),

          String.valueOf(message)
      );

      out.append('\n');

      flush();
    } catch (IOException e) {
      error(e);
    } finally {
      lock.unlock();
    }
  }

  public final void log(String name, Note.Marker level, String message, Throwable t) {
    if (level == null) {
      return;
    }

    lock.lock();
    try {
      format(
          level,

          String.valueOf(name),

          String.valueOf(message)
      );

      if (t != null) {
        formatThrowable(t);
      }

      flush();
    } catch (IOException e) {
      error(e);
    } finally {
      lock.unlock();
    }
  }

  private boolean test(Note note) {
    return error == null && filter.test(note);
  }

  private void flush() throws IOException {
    if (flushable != null) {
      flushable.flush();
    }
  }

  private int format(Note note) throws IOException {
    return format(
        note.marker(),

        note.source(),

        note.key()
    );
  }

  private int format(Note.Marker marker, String source, String key) throws IOException {
    final LocalDateTime date;
    date = LocalDateTime.now(clock);

    dateFormat.formatTo(date, out);

    out.append(' ');

    final String markerName;
    markerName = marker.name();

    pad(markerName, 5);

    out.append(' ');

    out.append('[');

    final Thread thread;
    thread = Thread.currentThread();

    final String threadName;
    threadName = thread.getName();

    pad(threadName, 15);

    out.append(']');

    out.append(' ');

    pad(source, 40);

    out.append(' ');
    out.append(':');
    out.append(' ');

    final int length;
    length = key.length();

    out.append(key);

    return length;
  }

  private int formatValue(int length, int value) throws IOException {
    return formatValue0(length, Integer.toString(value));
  }

  private int formatValue(int length, long value) throws IOException {
    return formatValue0(length, Long.toString(value));
  }

  private int formatValue(int length, Object value) throws IOException {
    return formatValue0(length, String.valueOf(value));
  }

  private int formatValue0(int length, String value) throws IOException {
    if (length > 0) {
      out.append(' ');
    }

    final int result;
    result = value.length();

    out.append(value);

    return result;
  }

  private void formatLastValue(int length, int value) throws IOException {
    formatValue(length, value);

    out.append('\n');
  }

  private void formatLastValue(int length, long value) throws IOException {
    formatValue(length, value);

    out.append('\n');
  }

  private void formatLastValue(int length, Object value) throws IOException {
    if (value instanceof Throwable t) {
      formatThrowable(t);
    } else {
      formatValue(length, value);

      out.append('\n');
    }
  }

  private void formatThrowable(Throwable t) throws IOException {
    out.append('\n');

    t.printStackTrace(printWriter);
  }

  private void pad(String value, int length) throws IOException {
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

}