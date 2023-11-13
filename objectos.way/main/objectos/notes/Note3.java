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
package objectos.notes;

import objectos.core.object.Check;

/**
 * A note that takes three arguments.
 *
 * @param <T1> the type of the first note argument
 * @param <T2> the type of the second note argument
 * @param <T3> the type of the third note argument
 */
public final class Note3<T1, T2, T3> extends Note {

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
	Note3(Level level, String source, Object key) {
		super(level, source, key);
	}

	/**
	 * Creates a new DEBUG note with the specified source and key.
	 *
	 * <p>
	 * The actual <strong>{@code source}</strong> value will be the canonical name
	 * of the specified class instance.
	 *
	 * @param <T1> the type of the note argument
	 * @param <T2> the type of the second note argument
	 * @param <T3> the type of the third note argument
	 * @param source
	 *        a class instance whose canonical name will be used as the source of
	 *        this note
	 * @param key
	 *        the object instance to be used as key of this note
	 *
	 * @return a new DEBUG note
	 */
	public static <T1, T2, T3> Note3<T1, T2, T3> debug(Class<?> source, Object key) {
		Check.notNull(source, "source == null");
		Check.notNull(key, "key == null");

		return new Note3<>(Level.DEBUG, source(source), key);
	}

	/**
	 * Creates a new ERROR note with the specified source and key.
	 *
	 * <p>
	 * The actual <strong>{@code source}</strong> value will be the canonical name
	 * of the specified class instance.
	 *
	 * @param <T1> the type of the note argument
	 * @param <T2> the type of the second note argument
	 * @param <T3> the type of the third note argument
	 * @param source
	 *        a class instance whose canonical name will be used as the source of
	 *        this note
	 * @param key
	 *        the object instance to be used as key of this note
	 *
	 * @return a new ERROR note
	 */
	public static <T1, T2, T3> Note3<T1, T2, T3> error(Class<?> source, Object key) {
		Check.notNull(source, "source == null");
		Check.notNull(key, "key == null");

		return new Note3<>(Level.ERROR, source(source), key);
	}

	/**
	 * Creates a new INFO note with the specified source and key.
	 *
	 * <p>
	 * The actual <strong>{@code source}</strong> value will be the canonical name
	 * of the specified class instance.
	 *
	 * @param <T1> the type of the note argument
	 * @param <T2> the type of the second note argument
	 * @param <T3> the type of the third note argument
	 * @param source
	 *        a class instance whose canonical name will be used as the source of
	 *        this note
	 * @param key
	 *        the object instance to be used as key of this note
	 *
	 * @return a new INFO note
	 */
	public static <T1, T2, T3> Note3<T1, T2, T3> info(Class<?> source, Object key) {
		Check.notNull(source, "source == null");
		Check.notNull(key, "key == null");

		return new Note3<>(Level.INFO, source(source), key);
	}

	/**
	 * Creates a new TRACE note with the specified source and key.
	 *
	 * <p>
	 * The actual <strong>{@code source}</strong> value will be the canonical name
	 * of the specified class instance.
	 *
	 * @param <T1> the type of the note argument
	 * @param <T2> the type of the second note argument
	 * @param <T3> the type of the third note argument
	 * @param source
	 *        a class instance whose canonical name will be used as the source of
	 *        this note
	 * @param key
	 *        the object instance to be used as key of this note
	 *
	 * @return a new TRACE note
	 */
	public static <T1, T2, T3> Note3<T1, T2, T3> trace(Class<?> source, Object key) {
		Check.notNull(source, "source == null");
		Check.notNull(key, "key == null");

		return new Note3<>(Level.TRACE, source(source), key);
	}

	/**
	 * Creates a new WARN note with the specified source and key.
	 *
	 * <p>
	 * The actual <strong>{@code source}</strong> value will be the canonical name
	 * of the specified class instance.
	 *
	 * @param <T1> the type of the note argument
	 * @param <T2> the type of the second note argument
	 * @param <T3> the type of the third note argument
	 * @param source
	 *        a class instance whose canonical name will be used as the source of
	 *        this note
	 * @param key
	 *        the object instance to be used as key of this note
	 *
	 * @return a new WARN note
	 */
	public static <T1, T2, T3> Note3<T1, T2, T3> warn(Class<?> source, Object key) {
		Check.notNull(source, "source == null");
		Check.notNull(key, "key == null");

		return new Note3<>(Level.WARN, source(source), key);
	}

}