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
package objectox.lang;

import objectos.lang.Level;
import objectos.lang.Note;

public abstract class Log {

  final long timestamp;

  final String thread;

  final Level level;

  final String source;

  final String key;

  Log(Note note) {
    timestamp = System.currentTimeMillis();

    Thread currentThread;
    currentThread = Thread.currentThread();

    thread = currentThread.getName();

    level = note.level();

    source = note.source();

    Object key;
    key = note.key();

    this.key = String.valueOf(key);
  }

  abstract String format(Layout layout);

}