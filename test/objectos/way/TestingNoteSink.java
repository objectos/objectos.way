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
package objectos.way;

import objectos.notes.LongNote;
import objectos.notes.Note;
import objectos.notes.Note0;
import objectos.notes.Note1;
import objectos.notes.Note2;
import objectos.notes.Note3;
import objectos.notes.NoteSink;

public class TestingNoteSink implements NoteSink {

  public static final App.NoteSink INSTANCE;

  static {
    INSTANCE = App.NoteSink.OfConsole.create(config -> {});
  }

  @Override
  public NoteSink replace(NoteSink sink) { return null; }

  @Override
  public boolean isEnabled(Note note) {
    return INSTANCE.isEnabled(note);
  }

  @Override
  public void send(Note0 note) {
    visitNote(note);

    INSTANCE.send(note);
  }

  @Override
  public void send(LongNote note, long value) {
    visitNote(note);

    INSTANCE.send(note, value);
  }

  @Override
  public <T1> void send(Note1<T1> note, T1 v1) {
    visitNote(note);

    INSTANCE.send(note, v1);
  }

  @Override
  public <T1, T2> void send(Note2<T1, T2> note, T1 v1, T2 v2) {
    visitNote(note);

    INSTANCE.send(note, v1, v2);
  }

  @Override
  public <T1, T2, T3> void send(Note3<T1, T2, T3> note, T1 v1, T2 v2, T3 v3) {
    visitNote(note);

    INSTANCE.send(note, v1, v2, v3);
  }

  protected void visitNote(Note note) {

  }

}