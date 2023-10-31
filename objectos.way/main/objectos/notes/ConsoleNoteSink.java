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

import java.io.PrintStream;
import objectos.lang.Level;
import objectos.lang.NoteSink;
import objectos.notes.internal.InternalConsoleNoteSink;
import objectos.notes.internal.InternalConsoleNoteSink.OptionValue;
import objectox.lang.Check;

/**
 * A {@link NoteSink} object that writes out notes to the system console.
 */
public sealed interface ConsoleNoteSink extends NoteSink permits InternalConsoleNoteSink {

	sealed interface Option permits OptionValue {

		static Option target(PrintStream stream) {
			Check.notNull(stream, "stream == null");

			return new OptionValue() {
				@Override
				public final void accept(InternalConsoleNoteSink builder) {
					builder.target(stream);
				}
			};
		}

	}

	/**
	 * Creates a new {@code ConsoleNoteSink} instance with the specified note sink
	 * level and options.
	 *
	 * <p>
	 * The returned note sink instance will send notes whose level is greater or
	 * equal to the specified level.
	 *
	 * @param level
	 *        the level
	 * @param options
	 *        configure the returned instance with these options
	 *
	 * @return a new {@code ConsoleNoteSink} instance
	 */
	static ConsoleNoteSink of(Level level, Option... options) {
		Check.notNull(level, "level == null");
		Check.notNull(options, "options == null");

		InternalConsoleNoteSink instance;
		instance = new InternalConsoleNoteSink(level);

		for (int i = 0; i < options.length; i++) {
			Option o;
			o = options[i];

			Check.notNull(o, "options[", i, "] == null");

			((OptionValue) o).accept(instance);
		}

		return instance;
	}

}