/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.html;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import objectos.html.stream.Event;
import objectox.html.format.BaseFormatter;
import objectox.html.format.Output;
import objectox.html.format.OutputY;
import objectox.html.format.StandardFormatter;
import objectox.html.stream.EmitTextEvent;
import objectox.html.stream.EndDocumentEvent;
import org.testng.annotations.Test;

public class HtmlReaderTest {

  private record Read(
      int bufferSize, int off, int len, int expected, String expectedBuffer) {}

  @Test(
      description = "read(char[], int, int) throws if the reader is closed",
      expectedExceptions = IOException.class,
      expectedExceptionsMessageRegExp = "Stream closed"
  )
  public void read01() throws IOException {
    final Iterator<Event> events;
    events = evts(
        new EmitTextEvent(0, "foo")
    );

    final Output output;
    output = new Output();

    final BaseFormatter formatter;
    formatter = new StandardFormatter();

    final char[] cbuf;
    cbuf = new char[8];

    try (HtmlReader subject = new HtmlReader(events, formatter, output)) {
      subject.close();

      subject.read(cbuf, 0, cbuf.length);
    }
  }

  @Test(
      description = "read(char[], int, int) throws if off is negative",
      expectedExceptions = IndexOutOfBoundsException.class
  )
  public void read02() throws IOException {
    testError(
        evts(
            new EmitTextEvent(0, "foo")
        ),

        8, -1, 1
    );
  }

  @Test(
      description = "read(char[], int, int) throws if len is negative",
      expectedExceptions = IndexOutOfBoundsException.class
  )
  public void read03() throws IOException {
    testError(
        evts(
            new EmitTextEvent(0, "foo")
        ),

        8, 0, -1
    );
  }

  @Test(
      description = "read(char[], int, int) throws if off plus len exceeds the buffer length",
      expectedExceptions = IndexOutOfBoundsException.class
  )
  public void read04() throws IOException {
    testError(
        evts(
            new EmitTextEvent(0, "foo")
        ),

        8, 7, 2
    );
  }

  @Test(
      description = "read(char[], int, int) throws if off exceeds the buffer length",
      expectedExceptions = IndexOutOfBoundsException.class
  )
  public void read05() throws IOException {
    testError(
        evts(
            new EmitTextEvent(0, "foo")
        ),

        8, 9, 0
    );
  }

  @Test(description = "read(char[], int, int) returns 0 if len is 0")
  public void read06() throws IOException {
    test(
        evts(
            new EmitTextEvent(0, "foo")
        ),

        output(_ -> {}),

        read(8, 0, 0, 0, "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000")
    );
  }

  @Test(description = "read(char[], int, int) returns 0 if len is 0 at the end of the buffer")
  public void read07() throws IOException {
    test(
        evts(
            new EmitTextEvent(0, "foo")
        ),

        output(_ -> {}),

        read(8, 8, 0, 0, "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000")
    );
  }

  @Test(description = "read(char[], int, int) returns the number of characters written, including the final newline")
  public void read08() throws IOException {
    test(
        evts(
            new EmitTextEvent(0, "ab"),
            new EmitTextEvent(0, "cd"),
            new EndDocumentEvent(false)
        ),

        output(_ -> {}),

        read(8, 0, 8, 5, "abcd\n\u0000\u0000\u0000"),
        read(8, 0, 8, -1, "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000")
    );
  }

  @Test(description = "read(char[], int, int) returns len when the buffer is exhausted while formatting an event")
  public void read09() throws IOException {
    test(
        evts(
            new EmitTextEvent(0, "abcdefghij"),
            new EndDocumentEvent(false)
        ),

        output(_ -> {}),

        read(4, 0, 4, 4, "abcd"),
        read(4, 0, 4, 4, "efgh"),
        read(4, 0, 4, 3, "ij\n\u0000"),
        read(4, 0, 4, -1, "\u0000\u0000\u0000\u0000")
    );
  }

  @Test(description = "read returns len when the rewritten buffer is already exhausted, then formats the remaining events")
  public void read10() throws IOException {
    test(
        evts(
            new EmitTextEvent(0, "x"),
            new EndDocumentEvent(false)
        ),

        output(y -> y.overflow = "abcdef"),

        read(4, 0, 4, 4, "abcd"),
        read(4, 0, 4, 4, "efx\n"),
        read(4, 0, 4, -1, "\u0000\u0000\u0000\u0000")
    );
  }

  @Test(description = "read returns len while draining the overflow and -1 after it is exhausted")
  public void read11() throws IOException {
    test(
        evts(
            new EndDocumentEvent(false)
        ),

        output(y -> y.overflow = "abcdefgh"),

        read(4, 0, 4, 4, "abcd"),
        read(4, 0, 4, 4, "efgh"),
        read(4, 0, 4, 1, "\n\u0000\u0000\u0000"),
        read(4, 0, 4, -1, "\u0000\u0000\u0000\u0000")
    );
  }

  @Test(description = "read returns the number of characters written at the given offset when the buffer is exhausted after rewrap")
  public void read12() throws IOException {
    test(
        evts(
            new EndDocumentEvent(false)
        ),

        output(y -> y.overflow = "abcdefgh"),

        read(8, 2, 4, 4, "\u0000\u0000abcd\u0000\u0000"),
        read(8, 6, 2, 2, "\u0000\u0000\u0000\u0000\u0000\u0000ef"),
        read(8, 0, 8, 3, "gh\n\u0000\u0000\u0000\u0000\u0000"),
        read(8, 0, 8, -1, "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000")
    );
  }

  @Test(description = "read returns 1 when the rewritten buffer is exhausted and len is 1")
  public void read13() throws IOException {
    test(
        evts(
            new EndDocumentEvent(false)
        ),

        output(y -> y.overflow = "ab"),

        read(4, 0, 1, 1, "a\u0000\u0000\u0000"),
        read(4, 0, 1, 1, "b\u0000\u0000\u0000"),
        read(4, 0, 1, 1, "\n\u0000\u0000\u0000"),
        read(4, 0, 4, -1, "\u0000\u0000\u0000\u0000")
    );
  }

  @Test(description = "read returns len when the pending newline fills the rewritten buffer exactly")
  public void read14() throws IOException {
    test(
        evts(
            new EmitTextEvent(0, "abcdefg"),
            new EndDocumentEvent(false)
        ),

        output(_ -> {}),

        read(4, 0, 4, 4, "abcd"),
        read(4, 0, 4, 4, "efg\n"),
        read(4, 0, 4, -1, "\u0000\u0000\u0000\u0000")
    );
  }

  @Test(description = "read returns -1 for an empty document")
  public void read15() throws IOException {
    test(
        evts(
            new EndDocumentEvent(true)
        ),

        output(_ -> {}),

        read(8, 0, 8, -1, "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000")
    );
  }

  @Test(description = "read returns the pending overflow count for an empty document")
  public void read16() throws IOException {
    test(
        evts(
            new EndDocumentEvent(true)
        ),

        output(y -> y.overflow = "ab"),

        read(8, 0, 8, 2, "ab\u0000\u0000\u0000\u0000\u0000\u0000"),
        read(8, 0, 8, -1, "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000")
    );
  }

  private Iterator<Event> evts(Event... events) {
    final List<Event> list;
    list = List.of(events);

    return list.iterator();
  }

  private Output output(Consumer<? super OutputY> opts) {
    return OutputY.create(opts);
  }

  private Read read(int bufferSize, int off, int len, int expected, String expectedBuffer) {
    return new Read(bufferSize, off, len, expected, expectedBuffer);
  }

  private void test(Iterator<Event> events, Output output, Read... reads) throws IOException {
    final BaseFormatter formatter;
    formatter = new StandardFormatter();

    try (HtmlReader subject = new HtmlReader(events, formatter, output)) {
      for (Read spec : reads) {
        final char[] cbuf;
        cbuf = new char[spec.bufferSize()];

        final int nread;
        nread = subject.read(cbuf, spec.off(), spec.len());

        assertEquals(nread, spec.expected());

        final String actual;
        actual = new String(cbuf);

        assertEquals(actual, spec.expectedBuffer());
      }
    }
  }

  private void testError(Iterator<Event> events, int bufferSize, int off, int len) throws IOException {
    final Output output;
    output = new Output();

    final BaseFormatter formatter;
    formatter = new StandardFormatter();

    final char[] cbuf;
    cbuf = new char[bufferSize];

    try (HtmlReader subject = new HtmlReader(events, formatter, output)) {
      subject.read(cbuf, off, len);

      fail("It should have thrown");
    }
  }

}
