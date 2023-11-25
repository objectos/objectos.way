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
import objectos.notes.Note0;

/**
 * An {@link Note0} log instance.
 */
public final class Event0Log extends Log {

  final Note0 event;

  /**
   * Creates a new log instance.
   *
   * @param event
   *        the event instance
   */
  public Event0Log(Note0 event) {
    super(event);

    this.event = event;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void formatToString(StringBuilder sb, int depth) {
    ToString.format(
        sb, depth, this,
        "event", event
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean isEvent0(Note0 event) {
    return Equals.of(this.event, event);
  }

  @Override
  final boolean hasEvent(Note event) {
    return Equals.of(this.event, event);
  }

  @Override
  final void print() {
    print0(event);

    printReturn();
  }

}