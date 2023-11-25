/*
 * Copyright (C) 2021-2023 Objectos Software LTDA.
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
package objectos.concurrent.log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import objectos.lang.object.ToString;
import objectos.notes.Level;
import objectos.notes.Note;
import objectos.notes.Note0;
import objectos.notes.Note1;
import objectos.notes.Note2;
import objectos.notes.Note3;

/**
 * A logged event that provides methods for its inspection.
 */
public abstract class Log implements ToString.Formattable {

  private final Note _event;

  private final String thread;

  private final long timestamp;

  Log(Note event) {
    _event = event;

    Thread currentThread;
    currentThread = Thread.currentThread();

    thread = currentThread.getName();

    timestamp = System.currentTimeMillis();
  }

  /**
   * Formats and appends to the {@code toString} builder at the specified
   * indentation {@code level} a string representation of this log instance.
   *
   * @param toString
   *        the builder of a {@code toString} method
   * @param level
   *        the indentation level.
   */
  @Override
  public abstract void formatToString(StringBuilder toString, int level);

  /**
   * Returns {@code true} if this log instance resulted from the specified
   * event.
   *
   * @param event
   *        the event to check this log against
   *
   * @return {@code true} if this log instance resulted from the specified
   *         event and {@code false} otherwise
   */
  public boolean isEvent0(Note0 event) {
    return false;
  }

  /**
   * Returns {@code true} if this log instance resulted from the specified
   * event and contains the specified value.
   *
   * @param <X1> the type of the log value
   *
   * @param event
   *        the event to check this log against
   * @param value
   *        the value to check this log against
   *
   * @return {@code true} if this log instance resulted from the specified
   *         event and value; {@code false} otherwise
   */
  public <X1> boolean isEvent1(Note1<X1> event, X1 value) {
    return false;
  }

  /**
   * Returns {@code true} if this log instance resulted from the specified
   * event and contains the specified values.
   *
   * @param <X1> the type of the first log value
   * @param <X2> the type of the second log value
   *
   * @param event
   *        the event to check this log against
   * @param value1
   *        the first value to check this log against
   * @param value2
   *        the second value to check this log against
   *
   * @return {@code true} if this log instance resulted from the specified
   *         event and values; {@code false} otherwise
   */
  public <X1, X2> boolean isEvent2(Note2<X1, X2> event, X1 value1, X2 value2) {
    return false;
  }

  /**
   * Returns {@code true} if this log instance resulted from the specified
   * event and contains the specified values.
   *
   * @param <X1> the type of the first log value
   * @param <X2> the type of the second log value
   * @param <X3> the type of the third log value
   *
   * @param event
   *        the event to check this log against
   * @param value1
   *        the first value to check this log against
   * @param value2
   *        the second value to check this log against
   * @param value3
   *        the third value to check this log against
   *
   * @return {@code true} if this log instance resulted from the specified
   *         event and values; {@code false} otherwise
   */
  public <X1, X2, X3> boolean isEvent3(Note3<X1, X2, X3> event, X1 value1, X2 value2, X3 value3) {
    return false;
  }

  /**
   * Returns {@code true} if the event associated with this log instance has the
   * specified level.
   *
   * @param level
   *        the event level to check
   *
   * @return {@code true} if the event associated with this log instance has the
   *         specified level and {@code false} otherwise
   */
  public final boolean isLevel(Level level) {
    Level actual;
    actual = _event.level();

    return actual.equals(level);
  }

  /**
   * Prints the stack trace of any {@link Throwable} this log may contain.
   */
  public void printStackTrace() {}

  /**
   * Returns a string representation of this log instance.
   *
   * @return a string representation of this log instance
   */
  @Override
  public final String toString() {
    return ToString.of(this);
  }

  abstract boolean hasEvent(Note event);

  abstract void print();

  final void print0(Note event) {
    StringBuilder out;
    out = new StringBuilder();

    Date date;
    date = new Date(timestamp);

    DateFormat dateFormat;
    dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    out.append(dateFormat.format(date));

    out.append(' ');

    Level level;
    level = event.level();

    String levelName;
    levelName = level.name();

    Logging.pad(out, levelName, 5);

    out.append(" --- ");

    out.append('[');

    Logging.pad(out, thread, 15);

    out.append(']');

    out.append(' ');

    String source;
    source = event.source();

    Logging.abbreviate(out, source, 40);

    out.append(' ');
    out.append(':');
    out.append(' ');

    out.append(event.key());

    System.out.print(out.toString());
  }

  final void printReturn() {
    System.out.append(System.lineSeparator());
  }

  final void printStackTrace(Object o) {
    if (o instanceof Throwable) {
      Throwable t;
      t = (Throwable) o;

      t.printStackTrace(System.out);
    }
  }

  final void printValue(String value) {
    System.out.append(' ');

    System.out.append(value);
  }

}