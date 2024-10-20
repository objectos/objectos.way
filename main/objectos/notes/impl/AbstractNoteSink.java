/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.notes.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import objectos.notes.Level;
import objectos.notes.LongNote;
import objectos.notes.Note;
import objectos.notes.Note0;
import objectos.notes.Note1;
import objectos.notes.Note2;
import objectos.notes.Note3;
import objectos.notes.NoteSink;

public abstract class AbstractNoteSink implements NoteSink {

  protected final Level level;

  protected Clock clock = Clock.systemDefaultZone();

  protected final Layout layout = new StandardLayout();

  public AbstractNoteSink(Level level) {
    this.level = level;
  }

  public final Level level() {
    return level;
  }

  public final void clock(Clock clock) {
    this.clock = clock;
  }

  public final boolean isEnabled(Level level) {
    return this.level.compareTo(level) <= 0;
  }

  @Override
  public final boolean isEnabled(Note note) {
    if (note == null) {
      return false;
    }

    return note.isEnabled(level);
  }

  @Override
  public final NoteSink replace(NoteSink sink) {
    return this;
  }

  @Override
  public final void send(Note0 note) {
    if (note == null) {
      return;
    }

    if (!note.isEnabled(level)) {
      return;
    }

    StringBuilder out;
    out = format(note);

    write(out);
  }

  @Override
  public final void send(LongNote note, long value) {
    if (note == null) {
      return;
    }

    if (!note.isEnabled(level)) {
      return;
    }

    StringBuilder out;
    out = format(note);

    formatLong(out, value);

    write(out);
  }

  @Override
  public final <T1> void send(Note1<T1> note, T1 v1) {
    if (note == null) {
      return;
    }

    if (!note.isEnabled(level)) {
      return;
    }

    StringBuilder out;
    out = format(note);

    formatLastValue(out, v1);

    write(out);
  }

  @Override
  public final <T1, T2> void send(Note2<T1, T2> note, T1 v1, T2 v2) {
    if (note == null) {
      return;
    }

    if (!note.isEnabled(level)) {
      return;
    }

    StringBuilder out;
    out = format(note);

    formatValue(out, v1);

    formatLastValue(out, v2);

    write(out);
  }

  @Override
  public final <T1, T2, T3> void send(Note3<T1, T2, T3> note, T1 v1, T2 v2, T3 v3) {
    if (note == null) {
      return;
    }

    if (!note.isEnabled(level)) {
      return;
    }

    StringBuilder out;
    out = format(note);

    formatValue(out, v1);

    formatValue(out, v2);

    formatLastValue(out, v3);

    write(out);
  }

  public final void slf4j(String name, Level level, String message) {
    StringBuilder out;
    out = format0(level, name, message);

    write(out);
  }

  public final void slf4j(String name, Level level, String message, Throwable t) {
    StringBuilder out;
    out = format0(level, name, message);

    if (t != null) {
      formatThrowable(out, t);
    }

    write(out);
  }

  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

  private StringBuilder format(Note note) {
    return format0(
        note.level(),

        note.source(),

        note.key()
    );
  }

  private StringBuilder format0(Level level, String source, Object key) {
    StringBuilder out;
    out = new StringBuilder();

    ZonedDateTime date;
    date = ZonedDateTime.now(clock);

    out.append(DATE_FORMAT.format(date));

    out.append(' ');

    String levelName;
    levelName = level.name();

    Logging.pad(out, levelName, 5);

    out.append(" --- ");

    out.append('[');

    Thread currentThread;
    currentThread = Thread.currentThread();

    String thread;
    thread = currentThread.getName();

    Logging.pad(out, thread, 15);

    out.append(']');

    out.append(' ');

    Logging.abbreviate(out, source, 40);

    out.append(' ');
    out.append(':');
    out.append(' ');

    out.append(key);

    return out;
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

  private void write(StringBuilder out) {
    out.append('\n');

    String s;
    s = out.toString();

    byte[] bytes;
    bytes = s.getBytes(StandardCharsets.UTF_8);

    writeBytes(bytes);
  }

  protected abstract void writeBytes(byte[] bytes);

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