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
import objectos.html.stream.Event;
import objectox.html.format.StandardFormatter;
import objectox.html.stream.RenderTextEvent;
import org.testng.annotations.Test;

public class HtmlReaderTest {

  @Test(
      description = "read(char[], int, int) throws if the reader is closed",
      expectedExceptions = IOException.class,
      expectedExceptionsMessageRegExp = "Stream closed"
  )
  public void read01() throws IOException {
    final Iterator<Event> events;
    events = evts(
        new RenderTextEvent(0, "foo")
    );

    final StandardFormatter formatter;
    formatter = new StandardFormatter();

    final HtmlReader subject;
    subject = new HtmlReader(events, formatter);

    subject.close();

    final char[] cbuf;
    cbuf = new char[8];

    subject.read(cbuf, 0, cbuf.length);
  }

  @Test(
      description = "read(char[], int, int) throws if off is negative",
      expectedExceptions = IndexOutOfBoundsException.class
  )
  public void read02() throws IOException {
    testError(
        evts(
            new RenderTextEvent(0, "foo")
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
            new RenderTextEvent(0, "foo")
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
            new RenderTextEvent(0, "foo")
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
            new RenderTextEvent(0, "foo")
        ),

        8, 9, 0
    );
  }

  @Test(description = "read(char[], int, int) returns 0 if len is 0")
  public void read06() throws IOException {
    test(
        evts(
            new RenderTextEvent(0, "foo")
        ),

        8, 0, 0,

        0
    );
  }

  @Test(description = "read(char[], int, int) returns 0 if len is 0 at the end of the buffer")
  public void read07() throws IOException {
    test(
        evts(
            new RenderTextEvent(0, "foo")
        ),

        8, 8, 0,

        0
    );
  }

  private Iterator<Event> evts(Event... events) {
    final List<Event> list;
    list = List.of(events);

    return list.iterator();
  }

  private void test(Iterator<Event> events, int bufferSize, int off, int len, int expected) throws IOException {
    final StandardFormatter formatter;
    formatter = new StandardFormatter();

    final char[] cbuf;
    cbuf = new char[bufferSize];

    try (HtmlReader subject = new HtmlReader(events, formatter)) {
      final int nread;
      nread = subject.read(cbuf, off, len);

      assertEquals(nread, expected);
    }
  }

  private void testError(Iterator<Event> events, int bufferSize, int off, int len) throws IOException {
    final StandardFormatter formatter;
    formatter = new StandardFormatter();

    final char[] cbuf;
    cbuf = new char[bufferSize];

    try (HtmlReader subject = new HtmlReader(events, formatter)) {
      subject.read(cbuf, off, len);

      fail("It should have thrown");
    }
  }

}
