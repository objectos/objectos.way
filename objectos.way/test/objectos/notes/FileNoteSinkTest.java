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

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import objectos.core.notes.Level;
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

	@Test(description = "when configured level is TRACE it should accept all note levels")
	public void send() throws IOException {
		Path file;
		file = directory.resolve("send.log");

		FileNoteSink noteSink = FileNoteSink.create(
				file,

				Level.TRACE,

				FileNoteSink.Option.clock(
						new TestingClock(2023, 11, 1)
				)
		);

		try (noteSink) {
			TestingNotes.sendAll0(noteSink);

			String string;
			string = Files.readString(file);

			assertEquals(
					string,

					"""
					2023-11-01 10:00:00.000 INFO  --- [main           ] objectos.notes.FileNoteSink              : Started %s TRACE
					2023-11-01 10:01:00.000 TRACE --- [main           ] objectos.notes.TestingNotes              : TRACE0
					2023-11-01 10:02:00.000 DEBUG --- [main           ] objectos.notes.TestingNotes              : DEBUG0
					2023-11-01 10:03:00.000 INFO  --- [main           ] objectos.notes.TestingNotes              : INFO0
					2023-11-01 10:04:00.000 WARN  --- [main           ] objectos.notes.TestingNotes              : WARN0
					2023-11-01 10:05:00.000 ERROR --- [main           ] objectos.notes.TestingNotes              : ERROR0
					""".formatted(file)
			);
		} finally {
			Files.deleteIfExists(file);
		}
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() throws IOException {
		Rmdir.rmdir(directory);
	}

}