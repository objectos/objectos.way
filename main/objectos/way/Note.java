/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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

import java.util.Objects;

/**
 * The <strong>Objectos Notes</strong> main interface.
 */
public sealed interface Note {

  sealed interface Int1 extends Note {
    static Int1 create(Class<?> source, String key, Marker marker) {
      return new SharedNote(source, key, marker);
    }
  }

  sealed interface Int2 extends Note {
    static Int2 create(Class<?> source, String key, Marker marker) {
      return new SharedNote(source, key, marker);
    }
  }

  sealed interface Int3 extends Note {
    static Int3 create(Class<?> source, String key, Marker marker) {
      return new SharedNote(source, key, marker);
    }
  }

  sealed interface Long1 extends Note {
    static Long1 create(Class<?> source, String key, Marker marker) {
      return new SharedNote(source, key, marker);
    }
  }

  sealed interface Long2 extends Note {
    static Long2 create(Class<?> source, String key, Marker marker) {
      return new SharedNote(source, key, marker);
    }
  }

  sealed interface Ref0 extends Note {
    static Ref0 create(Class<?> source, String key, Marker marker) {
      return new SharedNote(source, key, marker);
    }
  }

  sealed interface Ref1<T1> extends Note {
    @SuppressWarnings("unchecked")
    static <T1> Ref1<T1> create(Class<?> source, String key, Marker marker) {
      return (Ref1<T1>) new SharedNote(source, key, marker);
    }
  }

  sealed interface Ref2<T1, T2> extends Note {
    @SuppressWarnings("unchecked")
    static <T1, T2> Ref2<T1, T2> create(Class<?> source, String key, Marker marker) {
      return (Ref2<T1, T2>) new SharedNote(source, key, marker);
    }
  }

  sealed interface Ref3<T1, T2, T3> extends Note {
    @SuppressWarnings("unchecked")
    static <T1, T2, T3> Ref3<T1, T2, T3> create(Class<?> source, String key, Marker marker) {
      return (Ref3<T1, T2, T3>) new SharedNote(source, key, marker);
    }
  }

  sealed interface Marker {
    String name();
  }

  /**
   * The TRACE level is typically used for events that are for detailed internal
   * workings.
   */
  Marker TRACE = NoteLevel.TRACE;

  /**
   * The DEBUG level is typically used for events that are for debugging
   * purposes.
   */
  Marker DEBUG = NoteLevel.DEBUG;

  /**
   * The INFO level is typically used for events that are informational.
   */
  Marker INFO = NoteLevel.INFO;

  /**
   * The WARN level is typically used for events demanding attention.
   */
  Marker WARN = NoteLevel.WARN;

  /**
   * The ERROR level is typically used for events demanding immediate attention.
   */
  Marker ERROR = NoteLevel.ERROR;

  /**
   * Represents the severity of a `Note`. They may be used by `NoteSink`
   * instances to limit which notes are actually sent.
   */
  enum NoteLevel implements Marker {
    TRACE,

    DEBUG,

    INFO,

    WARN,

    ERROR;
  }

  static class NoOpSink implements Sink {

    @Override
    public boolean isEnabled(Note note) {
      return false;
    }

    @Override
    public void send(Int1 note, int value) {}

    @Override
    public void send(Int2 note, int value1, int value2) {}

    @Override
    public void send(Int3 note, int value1, int value2, int value3) {}

    @Override
    public void send(Long1 note, long value) {}

    @Override
    public void send(Long2 note, long value1, long value2) {}

    @Override
    public void send(Ref0 note) {}

    @Override
    public <T1> void send(Ref1<T1> note, T1 value) {}

    @Override
    public <T1, T2> void send(Ref2<T1, T2> note, T1 value1, T2 value2) {}

    @Override
    public <T1, T2, T3> void send(Ref3<T1, T2, T3> note, T1 value1, T2 value2, T3 value3) {}

  }

  interface Sink {

    boolean isEnabled(Note note);

    void send(Int1 note, int value);

    void send(Int2 note, int value1, int value2);

    void send(Int3 note, int value1, int value2, int value3);

    void send(Long1 note, long value);

    void send(Long2 note, long value1, long value2);

    void send(Ref0 note);

    <T1> void send(Ref1<T1> note, T1 value);

    <T1, T2> void send(Ref2<T1, T2> note, T1 value1, T2 value2);

    <T1, T2, T3> void send(Ref3<T1, T2, T3> note, T1 value1, T2 value2, T3 value3);

  }

  String source();

  String key();

  Marker marker();

}

record SharedNote(String source, String key, Marker marker)
    implements
    Note.Int1,
    Note.Int2,
    Note.Int3,

    Note.Long1,
    Note.Long2,

    Note.Ref0,
    Note.Ref1<Object>,
    Note.Ref2<Object, Object>,
    Note.Ref3<Object, Object, Object> {

  SharedNote(Class<?> source, String key, Marker marker) {
    this(
        source.getCanonicalName(),

        Objects.requireNonNull(key, "key == null"),

        Objects.requireNonNull(marker, "marker == null")
    );
  }

}