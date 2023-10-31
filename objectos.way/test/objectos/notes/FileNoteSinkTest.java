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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import objectos.lang.Level;
import objectos.way.Rmdir;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class FileNoteSinkTest {

	private Path directory;

	@BeforeClass
	public void beforeClass() throws IOException {
		directory = Files.createTempDirectory("file-note-sink-test-");
	}

	@Test(enabled = false, description = "when configured level is TRACE it should accept all note levels")
	public void send() throws IOException {
		Path file;
		file = directory.resolve("send.log");

		try (FileNoteSink noteSink = FileNoteSink.create(file, Level.TRACE)) {
			TestingNotes.sendAll0(noteSink);
		} finally {
			Files.deleteIfExists(file);
		}
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() throws IOException {
		Rmdir.rmdir(directory);
	}

}