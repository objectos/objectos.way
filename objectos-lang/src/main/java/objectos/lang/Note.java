/*
 * Copyright (C) 2022 Objectos Software LTDA.
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

/**
 * A {@code Note} is an event of a program execution that can be consumed by a
 * {@code NoteSink}. Typical uses for notes are:
 *
 * <ul>
 * <li>logging;</li>
 * <li>debugging; and</li>
 * <li>monitoring.</li>
 * </ul>
 *
 * Examples of notes (events) that can be modeled by instances of this class are
 *
 * <ul>
 * <li>the application start;</li>
 * <li>the operation to be performed by a class completes abruptly because of
 * exception {@code e}; and</li>
 * <li>the service implemented by a class receives a message with values
 * {@code x} and {@code y}.</li>
 * </ul>
 *
 * <p>
 * This class represents the note specification, not the note instance; a
 * note message is an invocation of instances of this class. From this, a
 * note message is (logically) composed of a note event (instances of this
 * class) and zero or more arguments.
 *
 * <p>
 * As an example, suppose that you want to notify the successful GET responses
 * of a HTTP server. The note message should contain the client's IP address,
 * the path name of the resource and the total number of bytes sents. The
 * following could be the note specification
 *
 * <pre>
 * {@code Note3<InetAddress, String, Integer> GET_OK = Note3.info();}</pre>
 *
 * <p>
 * While the invocation of this note, i.e., the actual note message would be
 * in the form
 *
 * <pre>
 * sink.log(GET_OK, clientAddress, pathName, bytes.length);</pre>
 *
 * Therefore, a {@code Note} instance indicates:
 *
 * <ul>
 * <li>the {@code source} of a note message. The {@code source} is a name
 * indicating the location a note message originated. It is typically the
 * fully qualified name of the class where the event is declared;</li>
 * <li>the {@code level} of a note message;</li>
 * <li>the {@code key} of the note. The note key is an object that uniquely
 * identifies the note within a {@code source}. It also serves as a description
 * of the note itself; and</li>
 * <li>the number and the types of the arguments that should be supplied to a
 * {@link NoteSink} instance when invoking this note.</li>
 * </ul>
 *
 * <h2>Note on the name of this class</h2>
 *
 * This class represents an event in an event listener context. However it is
 * not named {@code Event} as the name is an overloaded term in programming. In
 * order to avoid name clashes this class is named {@code Note} instead.
 *
 * @since 0.2
 */
public abstract class Note {

  private final String source;

  private final Object key;

  private final Level level;

  Note(String source, Object key, Level level) {
    this.source = Check.notNull(source, "source == null");

    this.key = Check.notNull(key, "key == null");

    this.level = Check.notNull(level, "level == null");
  }

  static <E extends Note> E create(Level l, Constructor<E> c) {
    StackWalker w;
    w = StackWalker.getInstance();

    return w.walk(s -> {
      var skip = s.skip(2);

      var maybe = skip.findFirst();

      var f = maybe.orElseThrow();

      var source = f.getClassName();

      var key = f.getFileName();

      key = key + ":" + f.getLineNumber();

      return c.create(source, key, l);
    });
  }

  /**
   * <p>
   * Compares the specified object with this note for equality. Returns
   * {@code true} if and only if
   *
   * <ul>
   * <li>the specified object is also a {@code Note};</li>
   * <li>the specified object is of the same subclass of {@code Note};</li>
   * <li>both notes have source classes that are equal to each other; and</li>
   * <li>both notes have keys that are equal to each other.</li>
   * </ul>
   *
   * @param obj
   *        the object to be compared for equality with this note
   *
   * @return {@code true} if the specified object is equal to this note
   */
  @Override
  public final boolean equals(Object obj) {
    return obj instanceof Note that
        && Equals.of(
          getClass(), that.getClass(),
          source, that.source,
          key, that.key
        );
  }

  /**
   * Returns the hash code value of this note.
   *
   * @return the hash code value of this note
   */
  @Override
  public final int hashCode() {
    return HashCode.of(key, source);
  }

  /**
   * Returns {@code true} if this note would be logged at the specified
   * {@code level}.
   *
   * @param level
   *        the level to check against
   *
   * @return {@code true} if this note would be logged at the specified
   *         {@code level}
   */
  public final boolean isEnabled(Level level) {
    return this.level.compareTo(level) >= 0;
  }

  /**
   * Returns the key of this note. The key of an note is an object that
   * uniquely identifies an note within a given source.
   *
   * @return the key of this note
   */
  public final Object key() {
    return key;
  }

  /**
   * Returns the logging level of this note.
   *
   * @return the logging level of this note
   */
  public final Level level() {
    return level;
  }

  /**
   * Returns the source of this note. The source of an note is a name
   * that indicates the origin of a log message. The source is typically
   * the canonical name of the class where the note is declared.
   *
   * @return the source of this note
   */
  public final String source() {
    return source;
  }

  /**
   * Returns a string representation of this note.
   *
   * @return a string representation of this note
   */
  @Override
  public final String toString() {
    StringBuilder sb;
    sb = new StringBuilder();

    Class<? extends Note> type;
    type = getClass();

    sb.append(type.getSimpleName());

    sb.append('[');

    sb.append(source);

    sb.append(',');

    sb.append(level.name());

    sb.append(',');

    sb.append(key);

    sb.append(']');

    return sb.toString();
  }

  @FunctionalInterface
  interface Constructor<E extends Note> {

    E create(String source, String key, Level level);

  }

}