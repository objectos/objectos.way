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
package objectos.notes;

import objectos.lang.Level;
import objectos.lang.NoteSink;
import objectos.notes.internal.InternalConsoleNoteSink;
import objectox.lang.Check;

/**
 * A {@link NoteSink} object that writes out notes to the system console.
 */
public sealed interface ConsoleNoteSink extends NoteSink permits InternalConsoleNoteSink {

	/**
	 * Creates a new {@code ConsoleNoteSink} instance with the specified note sink
	 * level.
	 *
	 * <p>
	 * The returned note sink instance will send notes whose level is greater or
	 * equal to the specified level.
	 *
	 * @param level
	 *        the level
	 *
	 * @return a new {@code ConsoleNoteSink} instance
	 */
	static ConsoleNoteSink of(Level level) {
		Check.notNull(level, "level == null");

		return new InternalConsoleNoteSink(level);
	}

}