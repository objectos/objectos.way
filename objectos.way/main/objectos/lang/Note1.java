/*
 * Copyright (C) 2022-2023 Objectos Software LTDA.
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

import objectox.lang.Check;
import objectox.lang.Notes;

/**
 * A note that takes one argument.
 *
 * @param <T1> the type of the note argument
 */
public final class Note1<T1> extends Note {

  /**
   * Creates a new note instance.
   *
   * @param level
   *        the severity of this note
   * @param source
   *        a name to identify the source class this note is bound to
   * @param key
   *        a key that uniquely identifies this note within the given
   *        {@code source}
   */
  Note1(Level level, String source, Object key) {
    super(level, source, key);
  }

  /**
   * Creates a new DEBUG note with the specified source and key.
   *
   * <p>
   * The actual <strong>{@code source}</strong> value will be the canonical name
   * of the specified class instance.
   *
   * @param <T1> the type of the log argument
   * @param source
   *        a class instance whose canonical name will be used as the source of
   *        this note
   * @param key
   *        the object instance to be used as key of this note
   *
   * @return a new DEBUG note
   */
  public static <T1> Note1<T1> debug(Class<?> source, Object key) {
    Check.notNull(source, "source == null");
    Check.notNull(key, "key == null");

    return new Note1<>(Level.DEBUG, Notes.source(source), key);
  }

  /**
   * Creates a new ERROR note with the specified source and key.
   *
   * <p>
   * The actual <strong>{@code source}</strong> value will be the canonical name
   * of the specified class instance.
   *
   * @param <T1> the type of the log argument
   * @param source
   *        a class instance whose canonical name will be used as the source of
   *        this note
   * @param key
   *        the object instance to be used as key of this note
   *
   * @return a new ERROR note
   */
  public static <T1> Note1<T1> error(Class<?> source, Object key) {
    Check.notNull(source, "source == null");
    Check.notNull(key, "key == null");

    return new Note1<>(Level.ERROR, Notes.source(source), key);
  }

  /**
   * Creates a new INFO note with the specified source and key.
   *
   * <p>
   * The actual <strong>{@code source}</strong> value will be the canonical name
   * of the specified class instance.
   *
   * @param <T1> the type of the log argument
   * @param source
   *        a class instance whose canonical name will be used as the source of
   *        this note
   * @param key
   *        the object instance to be used as key of this note
   *
   * @return a new INFO note
   */
  public static <T1> Note1<T1> info(Class<?> source, Object key) {
    Check.notNull(source, "source == null");
    Check.notNull(key, "key == null");

    return new Note1<>(Level.INFO, Notes.source(source), key);
  }

  /**
   * Creates a new TRACE note with the specified source and key.
   *
   * <p>
   * The actual <strong>{@code source}</strong> value will be the canonical name
   * of the specified class instance.
   *
   * @param <T1> the type of the log argument
   * @param source
   *        a class instance whose canonical name will be used as the source of
   *        this note
   * @param key
   *        the object instance to be used as key of this note
   *
   * @return a new TRACE note
   */
  public static <T1> Note1<T1> trace(Class<?> source, Object key) {
    Check.notNull(source, "source == null");
    Check.notNull(key, "key == null");

    return new Note1<>(Level.TRACE, Notes.source(source), key);
  }

  /**
   * Creates a new WARN note with the specified source and key.
   *
   * <p>
   * The actual <strong>{@code source}</strong> value will be the canonical name
   * of the specified class instance.
   *
   * @param <T1> the type of the log argument
   * @param source
   *        a class instance whose canonical name will be used as the source of
   *        this note
   * @param key
   *        the object instance to be used as key of this note
   *
   * @return a new WARN note
   */
  public static <T1> Note1<T1> warn(Class<?> source, Object key) {
    Check.notNull(source, "source == null");
    Check.notNull(key, "key == null");

    return new Note1<>(Level.WARN, Notes.source(source), key);
  }

}