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
package objectos.core.notes;

/**
 * An object responsible for sending the notes of a program; it behaves as an
 * event listener.
 *
 * <p>
 * <em>Note on {@code null} handling:</em> unlike other methods in this package,
 * the parameterized arguments of the various {@code note} methods of this
 * interface can be {@code null}.
 *
 * @see Note
 */
public interface NoteSink {

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
	 * Replaces this instance with the given {@code sink} instance if it is
	 * possible to do so.
	 *
	 * @param sink
	 *        a {@code NoteSink} instance that may replace this instance
	 *
	 * @return the given {@code sink} instance or this instance
	 */
	NoteSink replace(NoteSink sink);

	/**
	 * Sends the given note that takes no argument.
	 *
	 * @param note
	 *        an note instance
	 */
	void send(Note0 note);

	/**
	 * Sends the given note that takes a {@code long} argument.
	 *
	 * @param note
	 *        an note instance
	 * @param value
	 *        argument of the consumed note
	 */
	void send(LongNote note, long value);

	/**
	 * Sends the given note that takes one argument.
	 *
	 * @param <T1> type of the note argument
	 * @param note
	 *        an note instance
	 * @param v1
	 *        argument of the consumed note (can be null)
	 */
	<T1> void send(Note1<T1> note, T1 v1);

	/**
	 * Sends the given note that takes two arguments.
	 *
	 * @param <T1> type of the first note argument
	 * @param <T2> type of the second note argument
	 * @param note
	 *        an note instance
	 * @param v1
	 *        first argument of the consumed note (can be null)
	 * @param v2
	 *        second argument of the consumed note (can be null)
	 */
	<T1, T2> void send(Note2<T1, T2> note, T1 v1, T2 v2);

	/**
	 * Sends the given note that takes three arguments.
	 *
	 * @param <T1> type of the first note argument
	 * @param <T2> type of the second note argument
	 * @param <T3> type of the third note argument
	 * @param note
	 *        an note instance
	 * @param v1
	 *        first argument of the consumed note (can be null)
	 * @param v2
	 *        second argument of the consumed note (can be null)
	 * @param v3
	 *        third argument of the consumed note (can be null)
	 */
	<T1, T2, T3> void send(Note3<T1, T2, T3> note, T1 v1, T2 v2, T3 v3);

}
