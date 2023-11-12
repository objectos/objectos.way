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
package objectos.lang;

import java.util.List;
import objectos.core.notes.Note1;
import objectos.core.notes.Note2;
import objectos.util.GrowableList;

final class ShutdownHookNoteSink extends NoOpNoteSink {

  final List<Throwable> exceptions = new GrowableList<>();

  final List<Object> hooks = new GrowableList<>();

  @Override
  public <T1> void send(Note1<T1> note, T1 v1) {
    if (note == ShutdownHook.REGISTRATION) {
      hooks.add(v1);
    }
  }

  @Override
  public <T1, T2> void send(Note2<T1, T2> note, T1 v1, T2 v2) {
    if (v2 instanceof Throwable t) {
      exceptions.add(t);
    }
  }

}