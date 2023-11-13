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
package objectos.notes.file;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Clock;
import objectos.core.object.Check;
import objectos.notes.Level;
import objectos.notes.Note2;
import objectos.notes.NoteSink;

/**
 * A {@link objectos.notes.NoteSink} implementation that writes out notes to a
 * regular file.
 */
public sealed interface FileNoteSink extends Closeable, NoteSink permits StandardFileNoteSink {

	/**
	 * Configures the creation of a {@link FileNoteSink} instance.
	 */
	sealed interface Option permits OptionValue {

		/**
		 * Defines the clock instance from which timestamps will be obtained.
		 *
		 * @param clock
		 *        the clock instance
		 *
		 * @return an option instance
		 */
		static Option clock(Clock clock) {
			Check.notNull(clock, "clock == null");

			return new OptionValue() {
				@Override
				public final void accept(StandardFileNoteSink builder) {
					builder.clock(clock);
				}
			};
		}

	}

	/**
	 * A note indicating that the instance has started successfully.
	 */
	Note2<Path, Level> STARTED = Note2.info(FileNoteSink.class, "Started");

	static FileNoteSink create(Path file, Level level, Option... options) throws IOException {
		Check.notNull(file, "file == null");
		Check.notNull(level, "level == null");

		StandardFileNoteSink instance;
		instance = new StandardFileNoteSink(file, level);

		for (int i = 0; i < options.length; i++) {
			Option o;
			o = options[i];

			Check.notNull(o, "options[", i, "] == null");

			((OptionValue) o).accept(instance);
		}

		instance.start();

		return instance;
	}

}