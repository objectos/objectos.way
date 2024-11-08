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

/**
 * The <strong>Objectos Notes</strong> main interface.
 */
public sealed interface Note {

  /**
   * A note that takes one {@code int} argument.
   */
  sealed interface Int1 extends Note permits NoteImpl {
    /**
     * Creates a new {@code Int1} instance with the specified source, key and
     * marker.
     *
     * @param source
     *        a class instance whose canonical name will be used as the source
     *        of the note
     * @param key
     *        the key of the note
     * @param marker
     *        the marker to associate to the note
     *
     * @return a newly create note instance
     */
    static Int1 create(Class<?> source, String key, Marker marker) {
      return new NoteImpl(source, key, marker);
    }

    /**
     * Creates a new {@code Int1} instance with the specified source, key and
     * marker.
     *
     * @param source
     *        the source of the note
     * @param key
     *        the key of the note
     * @param marker
     *        the marker to associate to the note
     *
     * @return a newly create note instance
     */
    static Int1 create(String source, String key, Marker marker) {
      return new NoteImpl(source, key, marker);
    }
  }

  /**
   * A note that takes two {@code int} arguments.
   */
  sealed interface Int2 extends Note permits NoteImpl {
    /**
     * Creates a new {@code Int2} instance with the specified source, key and
     * marker.
     *
     * @param source
     *        a class instance whose canonical name will be used as the source
     *        of the note
     * @param key
     *        the key of the note
     * @param marker
     *        the marker to associate to the note
     *
     * @return a newly create note instance
     */
    static Int2 create(Class<?> source, String key, Marker marker) {
      return new NoteImpl(source, key, marker);
    }

    /**
     * Creates a new {@code Int2} instance with the specified source, key and
     * marker.
     *
     * @param source
     *        the source of the note
     * @param key
     *        the key of the note
     * @param marker
     *        the marker to associate to the note
     *
     * @return a newly create note instance
     */
    static Int2 create(String source, String key, Marker marker) {
      return new NoteImpl(source, key, marker);
    }
  }

  /**
   * A note that takes three {@code int} arguments.
   */
  sealed interface Int3 extends Note permits NoteImpl {
    /**
     * Creates a new {@code Int3} instance with the specified source, key and
     * marker.
     *
     * @param source
     *        a class instance whose canonical name will be used as the source
     *        of the note
     * @param key
     *        the key of the note
     * @param marker
     *        the marker to associate to the note
     *
     * @return a newly create note instance
     */
    static Int3 create(Class<?> source, String key, Marker marker) {
      return new NoteImpl(source, key, marker);
    }

    /**
     * Creates a new {@code Int3} instance with the specified source, key and
     * marker.
     *
     * @param source
     *        the source of the note
     * @param key
     *        the key of the note
     * @param marker
     *        the marker to associate to the note
     *
     * @return a newly create note instance
     */
    static Int3 create(String source, String key, Marker marker) {
      return new NoteImpl(source, key, marker);
    }
  }

  /**
   * A note that takes one {@code long} argument.
   */
  sealed interface Long1 extends Note permits NoteImpl {
    /**
     * Creates a new {@code Long1} instance with the specified source, key and
     * marker.
     *
     * @param source
     *        a class instance whose canonical name will be used as the source
     *        of the note
     * @param key
     *        the key of the note
     * @param marker
     *        the marker to associate to the note
     *
     * @return a newly create note instance
     */
    static Long1 create(Class<?> source, String key, Marker marker) {
      return new NoteImpl(source, key, marker);
    }

    /**
     * Creates a new {@code Long1} instance with the specified source, key and
     * marker.
     *
     * @param source
     *        the source of the note
     * @param key
     *        the key of the note
     * @param marker
     *        the marker to associate to the note
     *
     * @return a newly create note instance
     */
    static Long1 create(String source, String key, Marker marker) {
      return new NoteImpl(source, key, marker);
    }
  }

  /**
   * A note that takes two {@code long} arguments.
   */
  sealed interface Long2 extends Note permits NoteImpl {
    /**
     * Creates a new {@code Long2} instance with the specified source, key and
     * marker.
     *
     * @param source
     *        a class instance whose canonical name will be used as the source
     *        of the note
     * @param key
     *        the key of the note
     * @param marker
     *        the marker to associate to the note
     *
     * @return a newly create note instance
     */
    static Long2 create(Class<?> source, String key, Marker marker) {
      return new NoteImpl(source, key, marker);
    }

    /**
     * Creates a new {@code Long2} instance with the specified source, key and
     * marker.
     *
     * @param source
     *        the source of the note
     * @param key
     *        the key of the note
     * @param marker
     *        the marker to associate to the note
     *
     * @return a newly create note instance
     */
    static Long2 create(String source, String key, Marker marker) {
      return new NoteImpl(source, key, marker);
    }
  }

  /**
   * A note that takes no arguments.
   */
  sealed interface Ref0 extends Note permits NoteImpl {
    /**
     * Creates a new {@code Ref0} instance with the specified source, key and
     * marker.
     *
     * @param source
     *        a class instance whose canonical name will be used as the source
     *        of the note
     * @param key
     *        the key of the note
     * @param marker
     *        the marker to associate to the note
     *
     * @return a newly create note instance
     */
    static Ref0 create(Class<?> source, String key, Marker marker) {
      return new NoteImpl(source, key, marker);
    }

    /**
     * Creates a new {@code Ref0} instance with the specified source, key and
     * marker.
     *
     * @param source
     *        the source of the note
     * @param key
     *        the key of the note
     * @param marker
     *        the marker to associate to the note
     *
     * @return a newly create note instance
     */
    static Ref0 create(String source, String key, Marker marker) {
      return new NoteImpl(source, key, marker);
    }
  }

  /**
   * A note that takes one object reference argument.
   *
   * @param <T1> the type of the note argument
   */
  sealed interface Ref1<T1> extends Note permits NoteImpl {
    /**
     * Creates a new {@code Ref1} instance with the specified source, key and
     * marker.
     *
     * @param <T1> the type of the note argument
     * @param source
     *        a class instance whose canonical name will be used as the source
     *        of the note
     * @param key
     *        the key of the note
     * @param marker
     *        the marker to associate to the note
     *
     * @return a newly create note instance
     */
    @SuppressWarnings("unchecked")
    static <T1> Ref1<T1> create(Class<?> source, String key, Marker marker) {
      return (Ref1<T1>) new NoteImpl(source, key, marker);
    }

    /**
     * Creates a new {@code Ref1} instance with the specified source, key and
     * marker.
     *
     * @param <T1> the type of the note argument
     * @param source
     *        the source of the note
     * @param key
     *        the key of the note
     * @param marker
     *        the marker to associate to the note
     *
     * @return a newly create note instance
     */
    @SuppressWarnings("unchecked")
    static <T1> Ref1<T1> create(String source, String key, Marker marker) {
      return (Ref1<T1>) new NoteImpl(source, key, marker);
    }
  }

  /**
   * A note that takes two object reference arguments.
   *
   * @param <T1> the type of the first note argument
   * @param <T2> the type of the second note argument
   */
  sealed interface Ref2<T1, T2> extends Note permits NoteImpl {
    /**
     * Creates a new {@code Ref2} instance with the specified source, key and
     * marker.
     *
     * @param <T1> the type of the first note argument
     * @param <T2> the type of the second note argument
     * @param source
     *        a class instance whose canonical name will be used as the source
     *        of the note
     * @param key
     *        the key of the note
     * @param marker
     *        the marker to associate to the note
     *
     * @return a newly create note instance
     */
    @SuppressWarnings("unchecked")
    static <T1, T2> Ref2<T1, T2> create(Class<?> source, String key, Marker marker) {
      return (Ref2<T1, T2>) new NoteImpl(source, key, marker);
    }

    /**
     * Creates a new {@code Ref2} instance with the specified source, key and
     * marker.
     *
     * @param <T1> the type of the first note argument
     * @param <T2> the type of the second note argument
     * @param source
     *        the source of the note
     * @param key
     *        the key of the note
     * @param marker
     *        the marker to associate to the note
     *
     * @return a newly create note instance
     */
    @SuppressWarnings("unchecked")
    static <T1, T2> Ref2<T1, T2> create(String source, String key, Marker marker) {
      return (Ref2<T1, T2>) new NoteImpl(source, key, marker);
    }
  }

  /**
   * A note that takes three object reference arguments.
   *
   * @param <T1> the type of the first note argument
   * @param <T2> the type of the second note argument
   * @param <T3> the type of the third note argument
   */
  sealed interface Ref3<T1, T2, T3> extends Note permits NoteImpl {
    /**
     * Creates a new {@code Ref3} instance with the specified source, key and
     * marker.
     *
     * @param <T1> the type of the first note argument
     * @param <T2> the type of the second note argument
     * @param <T3> the type of the third note argument
     * @param source
     *        a class instance whose canonical name will be used as the source
     *        of the note
     * @param key
     *        the key of the note
     * @param marker
     *        the marker to associate to the note
     *
     * @return a newly create note instance
     */
    @SuppressWarnings("unchecked")
    static <T1, T2, T3> Ref3<T1, T2, T3> create(Class<?> source, String key, Marker marker) {
      return (Ref3<T1, T2, T3>) new NoteImpl(source, key, marker);
    }

    /**
     * Creates a new {@code Ref3} instance with the specified source, key and
     * marker.
     *
     * @param <T1> the type of the first note argument
     * @param <T2> the type of the second note argument
     * @param <T3> the type of the third note argument
     * @param source
     *        the source of the note
     * @param key
     *        the key of the note
     * @param marker
     *        the marker to associate to the note
     *
     * @return a newly create note instance
     */
    @SuppressWarnings("unchecked")
    static <T1, T2, T3> Ref3<T1, T2, T3> create(String source, String key, Marker marker) {
      return (Ref3<T1, T2, T3>) new NoteImpl(source, key, marker);
    }
  }

  /**
   * Markers help to filter out note instances.
   */
  sealed interface Marker permits NoteLevel {

    /**
     * Returns the name of this marker.
     *
     * @return the name of this marker
     */
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
   * A no-op {@code Sink} implementation.
   */
  static class NoOpSink implements Sink {

    static final NoOpSink INSTANCE = new NoOpSink();

    /**
     * Sole constructor.
     */
    protected NoOpSink() {}

    public static NoOpSink create() {
      return new NoOpSink();
    }

    /**
     * Returns {@code false}.
     *
     * @param note
     *        a note instance (ignored)
     *
     * @return {@code false}
     */
    @Override
    public boolean isEnabled(Note note) {
      return false;
    }

    /**
     * Does nothing, this is a no-op sink.
     *
     * @param note
     *        a note instance (ignored)
     * @param value
     *        a value (ignored)
     */
    @Override
    public void send(Int1 note, int value) {}

    /**
     * Does nothing, this is a no-op sink.
     *
     * @param note
     *        a note instance (ignored)
     * @param value1
     *        a first value (ignored)
     * @param value2
     *        a second value (ignored)
     */
    @Override
    public void send(Int2 note, int value1, int value2) {}

    /**
     * Does nothing, this is a no-op sink.
     *
     * @param note
     *        a note instance (ignored)
     * @param value1
     *        a first value (ignored)
     * @param value2
     *        a second value (ignored)
     * @param value3
     *        a third value (ignored)
     */
    @Override
    public void send(Int3 note, int value1, int value2, int value3) {}

    /**
     * Does nothing, this is a no-op sink.
     *
     * @param note
     *        a note instance (ignored)
     * @param value
     *        a value (ignored)
     */
    @Override
    public void send(Long1 note, long value) {}

    /**
     * Does nothing, this is a no-op sink.
     *
     * @param note
     *        a note instance (ignored)
     * @param value1
     *        a first value (ignored)
     * @param value2
     *        a second value (ignored)
     */
    @Override
    public void send(Long2 note, long value1, long value2) {}

    /**
     * Does nothing, this is a no-op sink.
     *
     * @param note
     *        a note instance (ignored)
     */
    @Override
    public void send(Ref0 note) {}

    /**
     * Does nothing, this is a no-op sink.
     *
     * @param note
     *        a note instance (ignored)
     * @param value
     *        a value (ignored)
     */
    @Override
    public <T1> void send(Ref1<T1> note, T1 value) {}

    /**
     * Does nothing, this is a no-op sink.
     *
     * @param note
     *        a note instance (ignored)
     * @param value1
     *        a first value (ignored)
     * @param value2
     *        a second value (ignored)
     */
    @Override
    public <T1, T2> void send(Ref2<T1, T2> note, T1 value1, T2 value2) {}

    /**
     * Does nothing, this is a no-op sink.
     *
     * @param note
     *        a note instance (ignored)
     * @param value1
     *        a first value (ignored)
     * @param value2
     *        a second value (ignored)
     * @param value3
     *        a third value (ignored)
     */
    @Override
    public <T1, T2, T3> void send(Ref3<T1, T2, T3> note, T1 value1, T2 value2, T3 value3) {}

  }

  /**
   * An object responsible for sending the notes of a program; it behaves as an
   * event listener.
   */
  interface Sink {

    /**
     * Returns {@code true} if the given {@code note} would be sent by this
     * {@code NoteSink} instance.
     *
     * @param note
     *        the note to test
     *
     * @return {@code true} if the given {@code note} would be sent
     */
    boolean isEnabled(Note note);

    /**
     * Sends the given note that takes a {@code int} argument.
     *
     * @param note
     *        an note instance
     * @param value
     *        argument of the consumed note
     */
    void send(Int1 note, int value);

    /**
     * Sends the given note that takes two {@code int} arguments.
     *
     * @param note
     *        an note instance
     * @param value1
     *        first argument of the consumed note
     * @param value2
     *        second argument of the consumed note
     */
    void send(Int2 note, int value1, int value2);

    /**
     * Sends the given note that takes three {@code int} arguments.
     *
     * @param note
     *        an note instance
     * @param value1
     *        first argument of the consumed note
     * @param value2
     *        second argument of the consumed note
     * @param value3
     *        third argument of the consumed note
     */
    void send(Int3 note, int value1, int value2, int value3);

    /**
     * Sends the given note that takes a {@code long} argument.
     *
     * @param note
     *        an note instance
     * @param value
     *        argument of the consumed note
     */
    void send(Long1 note, long value);

    /**
     * Sends the given note that takes two {@code long} arguments.
     *
     * @param note
     *        an note instance
     * @param value1
     *        first argument of the consumed note
     * @param value2
     *        second argument of the consumed note
     */
    void send(Long2 note, long value1, long value2);

    /**
     * Sends the given note that takes no arguments.
     *
     * @param note
     *        an note instance
     */
    void send(Ref0 note);

    /**
     * Sends the given note that takes one object reference argument.
     *
     * @param <T1> type of the note argument
     *
     * @param note
     *        an note instance
     *
     * @param value
     *        argument of the consumed note (can be null)
     */
    <T1> void send(Ref1<T1> note, T1 value);

    /**
     * Sends the given note that takes two object reference arguments.
     *
     * @param <T1> type of the first note argument
     * @param <T2> type of the second note argument
     *
     * @param note
     *        an note instance
     * @param value1
     *        first argument of the consumed note (can be null)
     * @param value2
     *        second argument of the consumed note (can be null)
     */
    <T1, T2> void send(Ref2<T1, T2> note, T1 value1, T2 value2);

    /**
     * Sends the given note that takes three object reference arguments.
     *
     * @param <T1> type of the first note argument
     * @param <T2> type of the second note argument
     * @param <T3> type of the third note argument
     * @param note
     *        an note instance
     * @param value1
     *        first argument of the consumed note (can be null)
     * @param value2
     *        second argument of the consumed note (can be null)
     * @param value3
     *        third argument of the consumed note (can be null)
     */
    <T1, T2, T3> void send(Ref3<T1, T2, T3> note, T1 value1, T2 value2, T3 value3);

  }

  /**
   * Returns the source of this note. The source of an note is a name
   * that indicates the origin of a note. The source is typically
   * the fully qualified name of the class where the note is declared.
   *
   * @return the source of this note
   */
  String source();

  /**
   * Returns the key of this note. The key of an note is a name that uniquely
   * identifies an note within a given source.
   *
   * @return the key of this note
   */
  String key();

  /**
   * Returns the first marker of this note.
   *
   * @return the first marker of this note
   */
  Marker marker();

  /**
   * Returns {@code true} if this note has the specified marker.
   *
   * @param marker
   *        the marker
   *
   * @return {@code true} if this note has the specified marker
   */
  default boolean has(Marker marker) {
    return marker().equals(marker);
  }

  /**
   * Returns {@code true} if this note has at least one of the specified
   * markers.
   *
   * @param marker1
   *        the first marker
   * @param marker2
   *        the second marker
   *
   * @return {@code true} if this note has at least one of the specified
   *         markers
   */
  default boolean hasAny(Marker marker1, Marker marker2) {
    return marker().equals(marker1)
        || marker().equals(marker2);
  }

  /**
   * Returns {@code true} if this note has at least one of the specified
   * markers.
   *
   * @param marker1
   *        the first marker
   * @param marker2
   *        the second marker
   * @param marker3
   *        the third marker
   *
   * @return {@code true} if this note has at least one of the specified
   *         markers
   */
  default boolean hasAny(Marker marker1, Marker marker2, Marker marker3) {
    return marker().equals(marker1)
        || marker().equals(marker2)
        || marker().equals(marker3);
  }

}
