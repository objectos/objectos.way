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

import objectos.lang.object.Equals;
import objectos.lang.object.ToString;
import objectos.notes.Note;
import objectos.notes.Note1;

/**
 * An {@link Note1} log instance.
 *
 * @param <T1> the type of the log value
 */
public final class Event1Log<T1> extends Log {

  final Note1<T1> event;

  final T1 value;

  /**
   * Creates a new log instance.
   *
   * @param event
   *        the event instance
   * @param value
   *        the value associated with the event
   */
  public Event1Log(Note1<T1> event, T1 value) {
    super(event);

    this.event = event;

    this.value = value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void formatToString(StringBuilder sb, int depth) {
    ToString.format(
        sb, depth, this,
        "event", event,
        "value", value
    );
  }

  /**
   * Returns the event associated with this log instance.
   *
   * @return the event associated with this log instance
   */
  public final Note1<T1> getEvent() {
    return event;
  }

  /**
   * Returns the event associated with this log instance.
   *
   * @return the event associated with this log instance
   */
  public final T1 getValue() {
    return value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final <X1> boolean isEvent1(Note1<X1> event, X1 value) {
    return Equals.of(
        this.event, event,
        this.value, value
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void printStackTrace() {
    printStackTrace(value);
  }

  @Override
  final boolean hasEvent(Note event) {
    return Equals.of(this.event, event);
  }

  @Override
  final void print() {
    print0(event);

    printValue(Logging.format(value));

    printReturn();

    printStackTrace(value);
  }

}