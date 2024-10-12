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
 * The <strong>Objectos Notes</strong> main class.
 */
public sealed abstract class Note {

  public static final class Int1 extends Note {
    Int1(Class<?> source, String key, Marker marker) {
      super(source, key, marker);
    }

    public static Int1 create(Class<?> source, String key, Marker marker) {
      return new Int1(source, key, marker);
    }
  }

  public static final class Int2 extends Note {
    Int2(Class<?> source, String key, Marker marker) {
      super(source, key, marker);
    }

    public static Int2 create(Class<?> source, String key, Marker marker) {
      return new Int2(source, key, marker);
    }
  }

  public static final class Int3 extends Note {
    Int3(Class<?> source, String key, Marker marker) {
      super(source, key, marker);
    }

    public static Int3 create(Class<?> source, String key, Marker marker) {
      return new Int3(source, key, marker);
    }
  }

  public static final class Long1 extends Note {
    Long1(Class<?> source, String key, Marker marker) {
      super(source, key, marker);
    }

    public static Long1 create(Class<?> source, String key, Marker marker) {
      return new Long1(source, key, marker);
    }
  }

  public static final class Long2 extends Note {
    Long2(Class<?> source, String key, Marker marker) {
      super(source, key, marker);
    }

    public static Long2 create(Class<?> source, String key, Marker marker) {
      return new Long2(source, key, marker);
    }
  }

  public static final class Ref0 extends Note {
    Ref0(Class<?> source, String key, Marker marker) {
      super(source, key, marker);
    }

    public static Ref0 create(Class<?> source, String key, Marker marker) {
      return new Ref0(source, key, marker);
    }
  }

  public static final class Ref1<T1> extends Note {
    Ref1(Class<?> source, String key, Marker marker) {
      super(source, key, marker);
    }

    public static <T1> Ref1<T1> create(Class<?> source, String key, Marker marker) {
      return new Ref1<>(source, key, marker);
    }
  }

  public static final class Ref2<T1, T2> extends Note {
    Ref2(Class<?> source, String key, Marker marker) {
      super(source, key, marker);
    }

    public static <T1, T2> Ref2<T1, T2> create(Class<?> source, String key, Marker marker) {
      return new Ref2<>(source, key, marker);
    }
  }

  public static final class Ref3<T1, T2, T3> extends Note {
    Ref3(Class<?> source, String key, Marker marker) {
      super(source, key, marker);
    }

    public static <T1, T2, T3> Ref3<T1, T2, T3> create(Class<?> source, String key, Marker marker) {
      return new Ref3<>(source, key, marker);
    }
  }

  public sealed interface Marker {
    String name();
  }

  /**
   * The TRACE level is typically used for events that are for detailed internal
   * workings.
   */
  public static final Marker TRACE = Level.TRACE;

  /**
   * The DEBUG level is typically used for events that are for debugging
   * purposes.
   */
  public static final Marker DEBUG = Level.DEBUG;

  /**
   * The INFO level is typically used for events that are informational.
   */
  public static final Marker INFO = Level.INFO;

  /**
   * The WARN level is typically used for events demanding attention.
   */
  public static final Marker WARN = Level.WARN;

  /**
   * The ERROR level is typically used for events demanding immediate attention.
   */
  public static final Marker ERROR = Level.ERROR;

  /**
   * Represents the severity of a `Note`. They may be used by `NoteSink`
   * instances to limit which notes are actually sent.
   */
  enum Level implements Marker {
    TRACE,

    DEBUG,

    INFO,

    WARN,

    ERROR;
  }

  public static class NoOpSink implements Sink {

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

  public interface Sink {

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

  final String source;

  final String key;

  final Marker marker0;

  Note(Class<?> source, String key, Marker marker) {
    this.source = source.getCanonicalName();

    this.key = Objects.requireNonNull(key, "key == null");

    marker0 = Objects.requireNonNull(marker, "marker == null");
  }

}