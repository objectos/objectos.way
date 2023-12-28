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
package objectos.notes.base;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import objectos.notes.Level;

public class StandardLayout implements Layout {

  private final DateTimeFormatter dateFormat
      = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

  public StandardLayout() {}

  @Override
  public final String formatLog0(Log0 log) {
    StringBuilder out;
    out = format0(log);

    out.append('\n');

    return out.toString();
  }

  @Override
  public final String formatLog1(Log1 log) {
    StringBuilder out;
    out = format0(log);

    formatLastValue(out, log.value);

    out.append('\n');

    return out.toString();
  }

  @Override
  public final String formatLog2(Log2 log) {
    StringBuilder out;
    out = format0(log);

    formatValue(out, log.value1);

    formatLastValue(out, log.value2);

    out.append('\n');

    return out.toString();
  }

  @Override
  public final String formatLog3(Log3 log) {
    StringBuilder out;
    out = format0(log);

    formatValue(out, log.value1);

    formatValue(out, log.value2);

    formatLastValue(out, log.value3);

    out.append('\n');

    return out.toString();
  }

  @Override
  public final String formatLongLog(LongLog log) {
    StringBuilder out;
    out = format0(log);

    formatValue(out, Long.toString(log.value));

    out.append('\n');

    return out.toString();
  }

  private StringBuilder format0(Log log) {
    StringBuilder out;
    out = new StringBuilder();

    ZonedDateTime date;
    date = log.timestamp;

    out.append(dateFormat.format(date));

    out.append(' ');

    Level level;
    level = log.level;

    String levelName;
    levelName = level.name();

    Logging.pad(out, levelName, 5);

    out.append(" --- ");

    out.append('[');

    Logging.pad(out, log.thread, 15);

    out.append(']');

    out.append(' ');

    Logging.abbreviate(out, log.source, 40);

    out.append(' ');
    out.append(':');
    out.append(' ');

    out.append(log.key);

    return out;
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