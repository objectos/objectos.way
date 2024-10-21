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
import objectos.way.Note.Int1;
import objectos.way.Note.Int2;
import objectos.way.Note.Int3;
import objectos.way.Note.Long1;
import objectos.way.Note.Long2;
import objectos.way.Note.Ref0;
import objectos.way.Note.Ref1;
import objectos.way.Note.Ref2;
import objectos.way.Note.Ref3;

public class TestingNoteSink implements objectos.way.Note.Sink, objectos.notes.NoteSink {

  public static final App.NoteSink INSTANCE;

  static {
    INSTANCE = App.NoteSink.OfConsole.create(config -> {});
  }

  @Override
  public boolean isEnabled(objectos.way.Note note) {
    return INSTANCE.isEnabled(note);
  }

  @Override
  public void send(Int1 note, int value) {
    INSTANCE.send(note, value);
  }

  @Override
  public void send(Int2 note, int value1, int value2) {
    INSTANCE.send(note, value1, value2);
  }

  @Override
  public void send(Int3 note, int value1, int value2, int value3) {
    INSTANCE.send(note, value1, value2, value3);
  }

  @Override
  public void send(Long1 note, long value) {
    INSTANCE.send(note, value);
  }

  @Override
  public void send(Long2 note, long value1, long value2) {
    INSTANCE.send(note, value1, value2);
  }

  @Override
  public void send(Ref0 note) {
    INSTANCE.send(note);
  }

  @Override
  public <T1> void send(Ref1<T1> note, T1 value) {
    INSTANCE.send(note, value);
  }

  @Override
  public <T1, T2> void send(Ref2<T1, T2> note, T1 value1, T2 value2) {
    INSTANCE.send(note, value1, value2);
  }

  @Override
  public <T1, T2, T3> void send(Ref3<T1, T2, T3> note, T1 value1, T2 value2, T3 value3) {
    INSTANCE.send(note, value1, value2, value3);
  }

  // legacy methods

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