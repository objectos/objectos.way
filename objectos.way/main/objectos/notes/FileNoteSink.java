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

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import objectos.lang.Level;
import objectos.lang.Note2;
import objectos.lang.NoteSink;
import objectos.notes.internal.StandardFileNoteSink;
import objectox.lang.Check;

/**
 * A {@link NoteSink} that must be closed in order to release the resources it
 * is holding.
 */
public sealed interface FileNoteSink extends Closeable, NoteSink permits StandardFileNoteSink {

	/**
	 * A note indicating that the instance has started successfully.
	 */
	Note2<Path, Level> STARTED = Note2.info(FileNoteSink.class, "Started");

	static FileNoteSink create(Path file, Level level) throws IOException {
		Check.notNull(file, "file == null");
		Check.notNull(level, "level == null");

		return null;
	}

}