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
package objectos.notes.impl;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import objectos.notes.Level;
import objectos.way.IncrementingClock;
import objectos.way.TestingDir;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class FileNoteSinkTest {

  private Path directory;

  @BeforeClass
  public void beforeClass() throws IOException {
    directory = TestingDir.next();
  }

  @Test(description = """
  It should create the parent directory.
  """)
  public void testCase01() throws IOException {
    Path parent;
    parent = directory.resolve("i-do-not-exist");

    Path log;
    log = parent.resolve("test-case-01.log");

    try (FileNoteSink noteSink = new FileNoteSink(log, Level.TRACE)) {
      assertEquals(Files.exists(parent), false);

      noteSink.start();

      assertEquals(Files.exists(parent), true);
    }
  }

  @Test(description = "when configured level is TRACE it should accept all note levels")
  public void send() throws IOException {
    Path file;
    file = directory.resolve("send.log");

    FileNoteSink noteSink;
    noteSink = new FileNoteSink(file, Level.TRACE);

    noteSink.clock(new IncrementingClock(2023, 11, 1));

    noteSink.start();

    try (noteSink) {
      TestingNotes.sendAll0(noteSink);

      String string;
      string = Files.readString(file);

      assertEquals(
          string,

          """
          2023-11-01 10:00:00.000 INFO  --- [main           ] objectos.notes.impl.FileNoteSink         : Started %s TRACE
          2023-11-01 10:01:00.000 TRACE --- [main           ] objectos.notes.impl.TestingNotes         : TRACE0
          2023-11-01 10:02:00.000 DEBUG --- [main           ] objectos.notes.impl.TestingNotes         : DEBUG0
          2023-11-01 10:03:00.000 INFO  --- [main           ] objectos.notes.impl.TestingNotes         : INFO0
          2023-11-01 10:04:00.000 WARN  --- [main           ] objectos.notes.impl.TestingNotes         : WARN0
          2023-11-01 10:05:00.000 ERROR --- [main           ] objectos.notes.impl.TestingNotes         : ERROR0
          """.formatted(file)
      );
    } finally {
      Files.deleteIfExists(file);
    }
  }

}