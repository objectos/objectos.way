/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.way;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import org.testng.annotations.Test;

public class AppNoteSinkTest {

  record Notes(
      Note.Int1 int1,
      Note.Int2 int2,
      Note.Int3 int3,

      Note.Long1 long1,
      Note.Long2 long2,

      Note.Ref0 ref0,
      Note.Ref1<String> ref1,
      Note.Ref2<String, LocalDate> ref2,
      Note.Ref3<String, LocalDate, String> ref3
  ) {
    static Notes create() {
      Class<?> s;
      s = AppNoteSinkTest.class;

      return new Notes(
          Note.Int1.create(s, "int1", Note.TRACE),
          Note.Int2.create(s, "int2", Note.DEBUG),
          Note.Int3.create(s, "int3", Note.INFO),

          Note.Long1.create(s, "long1", Note.WARN),
          Note.Long2.create(s, "long2", Note.ERROR),

          Note.Ref0.create(s, "ref0", Note.TRACE),
          Note.Ref1.create(s, "ref1", Note.DEBUG),
          Note.Ref2.create(s, "ref2", Note.INFO),
          Note.Ref3.create(s, "ref3", Note.WARN)
      );
    }
  }

  private final Path directory = TestingDir.next();

  private final Notes notes = Notes.create();

  @Test(description = """
  - console
  - no filtering
  """)
  public void console01() {
    ThisStream stream;
    stream = new ThisStream();

    App.NoteSink2.OfConsole noteSink;
    noteSink = App.NoteSink2.OfConsole.create(config -> {
      config.clock(new IncrementingClock(2023, 10, 31));

      config.target(stream);
    });

    sendAll(noteSink);

    assertFalse(stream.closed);

    assertEquals(
        stream.toString(),

        """
        2023-10-31 10:00:00.000 TRACE --- [main           ] objectos.way.AppNoteSinkTest             : int1 1001
        2023-10-31 10:01:00.000 DEBUG --- [main           ] objectos.way.AppNoteSinkTest             : int2 2002 3003
        2023-10-31 10:02:00.000 INFO  --- [main           ] objectos.way.AppNoteSinkTest             : int3 4000 5000 6000
        2023-10-31 10:03:00.000 WARN  --- [main           ] objectos.way.AppNoteSinkTest             : long1 1000
        2023-10-31 10:04:00.000 ERROR --- [main           ] objectos.way.AppNoteSinkTest             : long2 2123 3456
        2023-10-31 10:05:00.000 TRACE --- [main           ] objectos.way.AppNoteSinkTest             : ref0
        2023-10-31 10:06:00.000 DEBUG --- [main           ] objectos.way.AppNoteSinkTest             : ref1 A
        2023-10-31 10:07:00.000 INFO  --- [main           ] objectos.way.AppNoteSinkTest             : ref2 id 2024-10-11
        2023-10-31 10:08:00.000 WARN  --- [main           ] objectos.way.AppNoteSinkTest             : ref3 FOO 2010-05-23 BAR
        """
    );
  }

  @Test(description = """
  - console
  - with filtering
  """)
  public void console02() {
    ThisStream stream;
    stream = new ThisStream();

    App.NoteSink2.OfConsole noteSink;
    noteSink = App.NoteSink2.OfConsole.create(config -> {
      config.clock(new IncrementingClock(2023, 10, 31));

      config.filter(note -> {
        Note.Marker marker;
        marker = note.marker();

        return marker == Note.INFO
            || marker == Note.WARN
            || marker == Note.ERROR;
      });

      config.target(stream);
    });

    sendAll(noteSink);

    assertFalse(stream.closed);

    assertEquals(
        stream.toString(),

        """
        2023-10-31 10:00:00.000 INFO  --- [main           ] objectos.way.AppNoteSinkTest             : int3 4000 5000 6000
        2023-10-31 10:01:00.000 WARN  --- [main           ] objectos.way.AppNoteSinkTest             : long1 1000
        2023-10-31 10:02:00.000 ERROR --- [main           ] objectos.way.AppNoteSinkTest             : long2 2123 3456
        2023-10-31 10:03:00.000 INFO  --- [main           ] objectos.way.AppNoteSinkTest             : ref2 id 2024-10-11
        2023-10-31 10:04:00.000 WARN  --- [main           ] objectos.way.AppNoteSinkTest             : ref3 FOO 2010-05-23 BAR
        """
    );
  }

  @Test(description = """
  - no filtering
  - parent directories exists
  - log file does not exist
  """)
  public void file01() throws IOException {
    Path parent;
    parent = directory.resolve("file01");

    Files.createDirectory(parent);

    Path logFile;
    logFile = parent.resolve("file.log");

    App.NoteSink2.OfFile noteSink;
    noteSink = App.NoteSink2.OfFile.create(logFile, config -> {
      config.clock(new IncrementingClock(2023, 10, 31));
    });

    try (noteSink) {
      sendAll(noteSink);
    }

    assertEquals(
        Files.readString(logFile),

        """
        2023-10-31 10:00:00.000 TRACE --- [main           ] objectos.way.AppNoteSinkTest             : int1 1001
        2023-10-31 10:01:00.000 DEBUG --- [main           ] objectos.way.AppNoteSinkTest             : int2 2002 3003
        2023-10-31 10:02:00.000 INFO  --- [main           ] objectos.way.AppNoteSinkTest             : int3 4000 5000 6000
        2023-10-31 10:03:00.000 WARN  --- [main           ] objectos.way.AppNoteSinkTest             : long1 1000
        2023-10-31 10:04:00.000 ERROR --- [main           ] objectos.way.AppNoteSinkTest             : long2 2123 3456
        2023-10-31 10:05:00.000 TRACE --- [main           ] objectos.way.AppNoteSinkTest             : ref0
        2023-10-31 10:06:00.000 DEBUG --- [main           ] objectos.way.AppNoteSinkTest             : ref1 A
        2023-10-31 10:07:00.000 INFO  --- [main           ] objectos.way.AppNoteSinkTest             : ref2 id 2024-10-11
        2023-10-31 10:08:00.000 WARN  --- [main           ] objectos.way.AppNoteSinkTest             : ref3 FOO 2010-05-23 BAR
        """
    );
  }

  @Test(description = """
  - no filtering
  - parent directory exists
  - log file exists and should be rotated
  """)
  public void file02() throws IOException {
    Path parent;
    parent = directory.resolve("file02");

    Files.createDirectory(parent);

    Path logFile;
    logFile = parent.resolve("file.log");

    Files.writeString(logFile, "Existing content", StandardOpenOption.CREATE);

    IncrementingClock clock;
    clock = new IncrementingClock(2023, 10, 31);

    App.NoteSink2.OfFile noteSink;
    noteSink = App.NoteSink2.OfFile.create(logFile, config -> {
      config.clock(clock);
    });

    clock.reset();

    try (noteSink) {
      sendAll(noteSink);
    }

    assertEquals(
        Files.readString(parent.resolve("file.log.2023-10-31T10:00:00")),

        "Existing content"
    );

    assertEquals(
        Files.readString(logFile),

        """
        2023-10-31 10:00:00.000 TRACE --- [main           ] objectos.way.AppNoteSinkTest             : int1 1001
        2023-10-31 10:01:00.000 DEBUG --- [main           ] objectos.way.AppNoteSinkTest             : int2 2002 3003
        2023-10-31 10:02:00.000 INFO  --- [main           ] objectos.way.AppNoteSinkTest             : int3 4000 5000 6000
        2023-10-31 10:03:00.000 WARN  --- [main           ] objectos.way.AppNoteSinkTest             : long1 1000
        2023-10-31 10:04:00.000 ERROR --- [main           ] objectos.way.AppNoteSinkTest             : long2 2123 3456
        2023-10-31 10:05:00.000 TRACE --- [main           ] objectos.way.AppNoteSinkTest             : ref0
        2023-10-31 10:06:00.000 DEBUG --- [main           ] objectos.way.AppNoteSinkTest             : ref1 A
        2023-10-31 10:07:00.000 INFO  --- [main           ] objectos.way.AppNoteSinkTest             : ref2 id 2024-10-11
        2023-10-31 10:08:00.000 WARN  --- [main           ] objectos.way.AppNoteSinkTest             : ref3 FOO 2010-05-23 BAR
        """
    );
  }

  private void sendAll(Note.Sink sink) {
    sink.send(notes.int1, 1001);
    sink.send(notes.int2, 2002, 3003);
    sink.send(notes.int3, 4000, 5000, 6000);

    sink.send(notes.long1, 1000L);
    sink.send(notes.long2, 2123L, 3456L);

    sink.send(notes.ref0);
    sink.send(notes.ref1, "A");
    sink.send(notes.ref2, "id", LocalDate.of(2024, 10, 11));
    sink.send(notes.ref3, "FOO", LocalDate.of(2010, 5, 23), "BAR");
  }

  private static class ThisStream extends PrintStream {

    boolean closed;

    public ThisStream() {
      super(new ByteArrayOutputStream());
    }

    @Override
    public final void close() {
      closed = true;
    }

    @Override
    public final String toString() {
      ByteArrayOutputStream byteStream;
      byteStream = (ByteArrayOutputStream) out;

      byte[] bytes;
      bytes = byteStream.toByteArray();

      return new String(bytes);
    }

  }

}