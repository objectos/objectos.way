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

/**
 * A note that takes two arguments.
 *
 * @param <T1> the type of the first note argument
 * @param <T2> the type of the second note argument
 *
 * @since 0.2
 */
public final class Note2<T1, T2> extends Note {

  /**
   * Creates a new note instance.
   *
   * @param source
   *        a name to identify the source class this note is bound to
   * @param key
   *        a key that uniquely identifies this note within the given
   *        {@code source}
   * @param level
   *        the severity of this note
   */
  public Note2(String source, Object key, Level level) {
    super(source, key, level);
  }

  /**
   * Creates a new DEBUG note.
   *
   * <p>
   * The {@code source} and the {@code key} values are inferred from the method
   * immediately calling this method.
   *
   * <p>
   * The <strong>{@code source}</strong> value is the binary name of class
   * enclosing the method calling this method.
   *
   * <p>
   * The <strong>{@code key}</strong> value is a string containing the file
   * name, a colon ':' and the line number where this method has been invoked.
   *
   * @param <T1> the type of the note argument
   * @param <T2> the type of the second note argument
   *
   * @return a new DEBUG note
   */
  public static <T1, T2> Note2<T1, T2> debug() {
    return create(Level.DEBUG, Note2::new);
  }

  /**
   * Creates a new ERROR note.
   *
   * <p>
   * The {@code source} and the {@code key} values are inferred from the method
   * immediately calling this method.
   *
   * <p>
   * The <strong>{@code source}</strong> value is the binary name of class
   * enclosing the method calling this method.
   *
   * <p>
   * The <strong>{@code key}</strong> value is a string containing the file
   * name, a colon ':' and the line number where this method has been invoked.
   *
   * @param <T1> the type of the note argument
   * @param <T2> the type of the second note argument
   *
   * @return a new ERROR note
   */
  public static <T1, T2> Note2<T1, T2> error() {
    return create(Level.ERROR, Note2::new);
  }

  /**
   * Creates a new INFO note.
   *
   * <p>
   * The {@code source} and the {@code key} values are inferred from the method
   * immediately calling this method.
   *
   * <p>
   * The <strong>{@code source}</strong> value is the binary name of class
   * enclosing the method calling this method.
   *
   * <p>
   * The <strong>{@code key}</strong> value is a string containing the file
   * name, a colon ':' and the line number where this method has been invoked.
   *
   * @param <T1> the type of the note argument
   * @param <T2> the type of the second note argument
   *
   * @return a new INFO note
   */
  public static <T1, T2> Note2<T1, T2> info() {
    return create(Level.INFO, Note2::new);
  }

  /**
   * Creates a new TRACE note.
   *
   * <p>
   * The {@code source} and the {@code key} values are inferred from the method
   * immediately calling this method.
   *
   * <p>
   * The <strong>{@code source}</strong> value is the binary name of class
   * enclosing the method calling this method.
   *
   * <p>
   * The <strong>{@code key}</strong> value is a string containing the file
   * name, a colon ':' and the line number where this method has been invoked.
   *
   * @param <T1> the type of the note argument
   * @param <T2> the type of the second note argument
   *
   * @return a new TRACE note
   */
  public static <T1, T2> Note2<T1, T2> trace() {
    return create(Level.TRACE, Note2::new);
  }

  /**
   * Creates a new WARN note.
   *
   * <p>
   * The {@code source} and the {@code key} values are inferred from the method
   * immediately calling this method.
   *
   * <p>
   * The <strong>{@code source}</strong> value is the binary name of class
   * enclosing the method calling this method.
   *
   * <p>
   * The <strong>{@code key}</strong> value is a string containing the file
   * name, a colon ':' and the line number where this method has been invoked.
   *
   * @param <T1> the type of the note argument
   * @param <T2> the type of the second note argument
   *
   * @return a new WARN note
   */
  public static <T1, T2> Note2<T1, T2> warn() {
    return create(Level.WARN, Note2::new);
  }

}