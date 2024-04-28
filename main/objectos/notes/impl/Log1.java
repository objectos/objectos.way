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

import java.time.Clock;
import objectos.notes.Level;
import objectos.notes.Note1;

public final class Log1 extends Log {

  final Object value;

  Log1(Clock clock, Note1<?> note, Object value) {
    super(clock, note);

    this.value = value;
  }

  Log1(Clock clock, String name, Level level, String message, Throwable t) {
    super(clock, level, name, message);

    value = t;
  }

}