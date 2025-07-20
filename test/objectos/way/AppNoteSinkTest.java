/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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

import java.io.Closeable;
import java.io.IOException;
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
      Note.Ref3<String, LocalDate, String> ref3,

      Note.Ref3<Object, Object, Object> emptyKey,

      Note.Ref1<Throwable> throw1,
      Note.Ref2<Throwable, Throwable> throw2,
      Note.Ref3<Throwable, Throwable, Throwable> throw3
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
          Note.Ref3.create(s, "ref3", Note.WARN),

          Note.Ref3.create(s, "", Note.INFO),

          Note.Ref1.create(s, "throw1", Note.ERROR),
          Note.Ref2.create(s, "throw2", Note.ERROR),
          Note.Ref3.create(s, "throw3", Note.ERROR)
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

    App.NoteSink noteSink;
    noteSink = App.NoteSink.ofAppendable(stream, config -> {
      config.clock(new IncrementingClock(2023, 10, 31));
    });

    sendAll(noteSink);

    assertFalse(stream.closed);

    assertEquals(
        stream.toString(),

        """
        2023-10-31 10:00:00.000 TRACE [main           ] objectos.way.AppNoteSinkTest             : int1 1001
        2023-10-31 10:01:00.000 DEBUG [main           ] objectos.way.AppNoteSinkTest             : int2 2002 3003
        2023-10-31 10:02:00.000 INFO  [main           ] objectos.way.AppNoteSinkTest             : int3 4000 5000 6000
        2023-10-31 10:03:00.000 WARN  [main           ] objectos.way.AppNoteSinkTest             : long1 1000
        2023-10-31 10:04:00.000 ERROR [main           ] objectos.way.AppNoteSinkTest             : long2 2123 3456
        2023-10-31 10:05:00.000 TRACE [main           ] objectos.way.AppNoteSinkTest             : ref0
        2023-10-31 10:06:00.000 DEBUG [main           ] objectos.way.AppNoteSinkTest             : ref1 A
        2023-10-31 10:07:00.000 INFO  [main           ] objectos.way.AppNoteSinkTest             : ref2 id 2024-10-11
        2023-10-31 10:08:00.000 WARN  [main           ] objectos.way.AppNoteSinkTest             : ref3 FOO 2010-05-23 BAR
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

    App.NoteSink noteSink;
    noteSink = App.NoteSink.ofAppendable(stream, config -> {
      config.clock(new IncrementingClock(2023, 10, 31));

      config.filter(note -> note.hasAny(Note.INFO, Note.WARN, Note.ERROR));
    });

    sendAll(noteSink);

    assertFalse(stream.closed);

    assertEquals(
        stream.toString(),

        """
        2023-10-31 10:00:00.000 INFO  [main           ] objectos.way.AppNoteSinkTest             : int3 4000 5000 6000
        2023-10-31 10:01:00.000 WARN  [main           ] objectos.way.AppNoteSinkTest             : long1 1000
        2023-10-31 10:02:00.000 ERROR [main           ] objectos.way.AppNoteSinkTest             : long2 2123 3456
        2023-10-31 10:03:00.000 INFO  [main           ] objectos.way.AppNoteSinkTest             : ref2 id 2024-10-11
        2023-10-31 10:04:00.000 WARN  [main           ] objectos.way.AppNoteSinkTest             : ref3 FOO 2010-05-23 BAR
        """
    );
  }

  @Test(description = """
  - console
  - throwables
  """)
  public void console03() {
    ThisStream stream;
    stream = new ThisStream();

    App.NoteSink noteSink;
    noteSink = App.NoteSink.ofAppendable(stream, config -> {
      config.clock(new IncrementingClock(2023, 10, 31));
    });

    Throwable ignore;
    ignore = TestingStackTraces.ignore();

    noteSink.send(notes.throw1, TestingStackTraces.throwable1());
    noteSink.send(notes.throw2, ignore, TestingStackTraces.throwable2());
    noteSink.send(notes.throw3, ignore, ignore, TestingStackTraces.throwable3());

    String string;
    string = stream.toString();

    System.out.println(string);

    assertEquals(
        string,

        """
        2023-10-31 10:00:00.000 ERROR [main           ] objectos.way.AppNoteSinkTest             : throw1
        java.lang.Throwable
        \tat objectos.way/objectos.way.TestingStackTraces.throwable1(TestingStackTraces.java:27)
        \tat objectos.way/objectos.way.AppNoteSinkTest.console03(AppNoteSinkTest.java:162)
        2023-10-31 10:01:00.000 ERROR [main           ] objectos.way.AppNoteSinkTest             : throw2 java.lang.Throwable
        java.lang.Throwable
        \tat objectos.way/objectos.way.TestingStackTraces.throwable2(TestingStackTraces.java:31)
        \tat objectos.way/objectos.way.AppNoteSinkTest.console03(AppNoteSinkTest.java:163)
        2023-10-31 10:02:00.000 ERROR [main           ] objectos.way.AppNoteSinkTest             : throw3 java.lang.Throwable java.lang.Throwable
        java.lang.Throwable
        \tat objectos.way/objectos.way.TestingStackTraces.throwable3(TestingStackTraces.java:35)
        \tat objectos.way/objectos.way.AppNoteSinkTest.console03(AppNoteSinkTest.java:164)
        """
    );
  }

  @Test
  public void console04() {
    ThisStream stream;
    stream = new ThisStream();

    App.NoteSink noteSink;
    noteSink = App.NoteSink.ofAppendable(stream, config -> {
      config.clock(new IncrementingClock(2023, 10, 31));
    });

    noteSink.send(notes.emptyKey, "Hello", "", "World");

    assertEquals(
        stream.toString(),

        """
        2023-10-31 10:00:00.000 INFO  [main           ] objectos.way.AppNoteSinkTest             : Hello World
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

    App.NoteSink noteSink;
    noteSink = App.NoteSink.ofFile(logFile, config -> {
      config.clock(new IncrementingClock(2023, 10, 31));
    });

    try (noteSink) {
      sendAll(noteSink);
    }

    assertEquals(
        Files.readString(logFile),

        """
        2023-10-31 10:00:00.000 TRACE [main           ] objectos.way.AppNoteSinkTest             : int1 1001
        2023-10-31 10:01:00.000 DEBUG [main           ] objectos.way.AppNoteSinkTest             : int2 2002 3003
        2023-10-31 10:02:00.000 INFO  [main           ] objectos.way.AppNoteSinkTest             : int3 4000 5000 6000
        2023-10-31 10:03:00.000 WARN  [main           ] objectos.way.AppNoteSinkTest             : long1 1000
        2023-10-31 10:04:00.000 ERROR [main           ] objectos.way.AppNoteSinkTest             : long2 2123 3456
        2023-10-31 10:05:00.000 TRACE [main           ] objectos.way.AppNoteSinkTest             : ref0
        2023-10-31 10:06:00.000 DEBUG [main           ] objectos.way.AppNoteSinkTest             : ref1 A
        2023-10-31 10:07:00.000 INFO  [main           ] objectos.way.AppNoteSinkTest             : ref2 id 2024-10-11
        2023-10-31 10:08:00.000 WARN  [main           ] objectos.way.AppNoteSinkTest             : ref3 FOO 2010-05-23 BAR
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

    App.NoteSink noteSink;
    noteSink = App.NoteSink.ofFile(logFile, config -> {
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
        2023-10-31 10:00:00.000 TRACE [main           ] objectos.way.AppNoteSinkTest             : int1 1001
        2023-10-31 10:01:00.000 DEBUG [main           ] objectos.way.AppNoteSinkTest             : int2 2002 3003
        2023-10-31 10:02:00.000 INFO  [main           ] objectos.way.AppNoteSinkTest             : int3 4000 5000 6000
        2023-10-31 10:03:00.000 WARN  [main           ] objectos.way.AppNoteSinkTest             : long1 1000
        2023-10-31 10:04:00.000 ERROR [main           ] objectos.way.AppNoteSinkTest             : long2 2123 3456
        2023-10-31 10:05:00.000 TRACE [main           ] objectos.way.AppNoteSinkTest             : ref0
        2023-10-31 10:06:00.000 DEBUG [main           ] objectos.way.AppNoteSinkTest             : ref1 A
        2023-10-31 10:07:00.000 INFO  [main           ] objectos.way.AppNoteSinkTest             : ref2 id 2024-10-11
        2023-10-31 10:08:00.000 WARN  [main           ] objectos.way.AppNoteSinkTest             : ref3 FOO 2010-05-23 BAR
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

  private static class ThisStream implements Appendable, Closeable {

    boolean closed;

    private final StringBuilder out = new StringBuilder();

    ThisStream() {}

    @Override
    public final void close() throws IOException {
      closed = true;
    }

    @Override
    public final Appendable append(CharSequence csq) throws IOException {
      out.append(csq);

      return this;
    }

    @Override
    public final Appendable append(CharSequence csq, int start, int end) throws IOException {
      throw new UnsupportedOperationException("Implement me");
    }

    @Override
    public final Appendable append(char c) throws IOException {
      out.append(c);

      return this;
    }

    @Override
    public final String toString() {
      return out.toString();
    }

  }

}