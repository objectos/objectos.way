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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Collectors;
import objectos.notes.Level;
import objectos.notes.Note1;
import objectos.notes.Note2;
import objectos.notes.Note3;
import objectos.way.IncrementingClock;
import org.testng.annotations.Test;

public class ConsoleNoteSinkTest {

  static final Note1<Throwable> THROW1;

  static final Note2<Throwable, Throwable> THROW2;

  static final Note3<Throwable, Throwable, Throwable> THROW3;

  static {
    Class<?> s;
    s = ConsoleNoteSinkTest.class;

    THROW1 = Note1.error(s, "THROW1");

    THROW2 = Note2.error(s, "THROW2");

    THROW3 = Note3.error(s, "THROW3");
  }

  @Test
  public void send() {
    ThisStream stream;
    stream = new ThisStream();

    ConsoleNoteSink noteSink;
    noteSink = new ConsoleNoteSink(Level.TRACE);

    noteSink.clock(new IncrementingClock(2023, 10, 31));

    noteSink.target(stream);

    TestingNotes.sendAll0(noteSink);

    String string;
    string = stream.toString();

    assertEquals(
        string,

        """
        2023-10-31 10:00:00.000 TRACE --- [main           ] objectos.notes.impl.TestingNotes         : TRACE0
        2023-10-31 10:01:00.000 DEBUG --- [main           ] objectos.notes.impl.TestingNotes         : DEBUG0
        2023-10-31 10:02:00.000 INFO  --- [main           ] objectos.notes.impl.TestingNotes         : INFO0
        2023-10-31 10:03:00.000 WARN  --- [main           ] objectos.notes.impl.TestingNotes         : WARN0
        2023-10-31 10:04:00.000 ERROR --- [main           ] objectos.notes.impl.TestingNotes         : ERROR0
        """
    );
  }

  @Test
  public void throwable() {
    ThisStream stream;
    stream = new ThisStream();

    ConsoleNoteSink noteSink;
    noteSink = new ConsoleNoteSink(Level.TRACE);

    noteSink.clock(
        new IncrementingClock(
            LocalDateTime.of(2023, 10, 31, 11, 12, 13).atZone(ZoneId.systemDefault())
        )
    );

    noteSink.target(stream);

    Throwable ignore = TestingStackTraces.ignore();

    noteSink.send(THROW1, TestingStackTraces.throwable1());
    noteSink.send(THROW2, ignore, TestingStackTraces.throwable2());
    noteSink.send(THROW3, ignore, ignore, TestingStackTraces.throwable3());

    String string;
    string = stream.toString();

    string = string.lines()
        .filter(line -> {
          if (!line.startsWith("\tat")) {
            return true;
          }

          return line.startsWith("\tat objectos")
              && !line.contains("RunTests");
        })
        .map(line -> {
          if (!line.startsWith("\tat objectos")) {
            return line;
          }

          return line.replaceFirst("objectos.way(@.*)/", "objectos.way/");
        })
        .collect(Collectors.joining("\n"));

    assertEquals(
        string,

        """
        2023-10-31 11:12:13.000 ERROR --- [main           ] objectos.notes.impl.ConsoleNoteSinkTest  : THROW1
        java.lang.Throwable
        \tat objectos.way/objectos.notes.impl.TestingStackTraces.throwable1(TestingStackTraces.java:27)
        \tat objectos.way/objectos.notes.impl.ConsoleNoteSinkTest.throwable(ConsoleNoteSinkTest.java:99)
        
        2023-10-31 11:13:13.000 ERROR --- [main           ] objectos.notes.impl.ConsoleNoteSinkTest  : THROW2 java.lang.Throwable
        java.lang.Throwable
        \tat objectos.way/objectos.notes.impl.TestingStackTraces.throwable2(TestingStackTraces.java:31)
        \tat objectos.way/objectos.notes.impl.ConsoleNoteSinkTest.throwable(ConsoleNoteSinkTest.java:100)
        
        2023-10-31 11:14:13.000 ERROR --- [main           ] objectos.notes.impl.ConsoleNoteSinkTest  : THROW3 java.lang.Throwable java.lang.Throwable
        java.lang.Throwable
        \tat objectos.way/objectos.notes.impl.TestingStackTraces.throwable3(TestingStackTraces.java:35)
        \tat objectos.way/objectos.notes.impl.ConsoleNoteSinkTest.throwable(ConsoleNoteSinkTest.java:101)
        """
    );
  }

  private static class ThisStream extends PrintStream {

    public ThisStream() {
      super(new ByteArrayOutputStream());
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