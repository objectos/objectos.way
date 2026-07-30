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
package objectox.html.format;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

public class HtmlWriterTest {

  @Test
  public void writtenNoWrites() {
    char[] buf = new char[10];
    HtmlWriter w = HtmlWriter.wrap(buf, 0, 10);

    assertEquals(w.written(), -1);
  }

  @Test
  public void appendSingleChar() {
    char[] buf = new char[10];
    HtmlWriter w = HtmlWriter.wrap(buf, 0, 10);

    w.append('a');

    assertEquals(buf[0], 'a');
    assertEquals(w.written(), 1);
    assertFalse(w.exhausted());
  }

  @Test
  public void appendSingleCharWithOffset() {
    char[] buf = new char[10];
    HtmlWriter w = HtmlWriter.wrap(buf, 3, 5);

    w.append('x');

    assertEquals(buf[3], 'x');
    assertEquals(w.written(), 1);
    assertFalse(w.exhausted());
  }

  @Test
  public void appendCharSequence() {
    char[] buf = new char[20];
    HtmlWriter w = HtmlWriter.wrap(buf, 0, 20);

    w.append("hello");

    assertEquals(new String(buf, 0, 5), "hello");
    assertEquals(w.written(), 5);
    assertFalse(w.exhausted());
  }

  @Test
  public void appendCharSequenceWithOffset() {
    char[] buf = new char[20];
    HtmlWriter w = HtmlWriter.wrap(buf, 2, 10);

    w.append("abc");

    assertEquals(buf[2], 'a');
    assertEquals(buf[3], 'b');
    assertEquals(buf[4], 'c');
    assertEquals(w.written(), 3);
  }

  @Test
  public void appendCharSequenceSubSequence() {
    char[] buf = new char[20];
    HtmlWriter w = HtmlWriter.wrap(buf, 0, 20);

    w.append("hello world", 6, 11);

    assertEquals(new String(buf, 0, 5), "world");
    assertEquals(w.written(), 5);
  }

  @Test
  public void fillBufferExactly() {
    char[] buf = new char[5];
    HtmlWriter w = HtmlWriter.wrap(buf, 0, 5);

    w.append("abcde");

    assertEquals(new String(buf, 0, 5), "abcde");
    assertEquals(w.written(), 5);
    assertTrue(w.exhausted());
    assertEquals(w.overflow(), "");
  }

  @Test
  public void overflowOneChar() {
    char[] buf = new char[3];
    HtmlWriter w = HtmlWriter.wrap(buf, 0, 3);

    w.append("abcd");

    assertEquals(new String(buf, 0, 3), "abc");
    assertEquals(w.written(), 4);
    assertTrue(w.exhausted());
    assertEquals(w.overflow(), "d");
  }

  @Test
  public void overflowMultipleChars() {
    char[] buf = new char[3];
    HtmlWriter w = HtmlWriter.wrap(buf, 0, 3);

    w.append("abcdef");

    assertEquals(new String(buf, 0, 3), "abc");
    assertEquals(w.written(), 6);
    assertTrue(w.exhausted());
    assertEquals(w.overflow(), "def");
  }

  @Test
  public void overflowWithOffset() {
    char[] buf = new char[10];
    HtmlWriter w = HtmlWriter.wrap(buf, 8, 2);

    w.append("abcde");

    assertEquals(buf[8], 'a');
    assertEquals(buf[9], 'b');
    assertEquals(w.written(), 5);
    assertTrue(w.exhausted());
    assertEquals(w.overflow(), "cde");
  }

  @Test
  public void overflowAppendChar() {
    char[] buf = new char[1];
    HtmlWriter w = HtmlWriter.wrap(buf, 0, 1);

    w.append('a');
    w.append('b');

    assertEquals(buf[0], 'a');
    assertEquals(w.written(), 2);
    assertTrue(w.exhausted());
    assertEquals(w.overflow(), "b");
  }

  @Test
  public void overflowAppendCharSequenceSubSequence() {
    char[] buf = new char[3];
    HtmlWriter w = HtmlWriter.wrap(buf, 0, 3);

    w.append("hello", 0, 5);

    assertEquals(new String(buf, 0, 3), "hel");
    assertEquals(w.written(), 5);
    assertTrue(w.exhausted());
    assertEquals(w.overflow(), "lo");
  }

  @Test
  public void multipleAppends() {
    char[] buf = new char[10];
    HtmlWriter w = HtmlWriter.wrap(buf, 0, 10);

    w.append('a');
    w.append("bc");
    w.append("def");

    assertEquals(new String(buf, 0, 6), "abcdef");
    assertEquals(w.written(), 6);
    assertFalse(w.exhausted());
  }

  @Test
  public void multipleAppendsWithOverflow() {
    char[] buf = new char[4];
    HtmlWriter w = HtmlWriter.wrap(buf, 0, 4);

    w.append("ab");
    w.append("cd");
    w.append("ef");

    assertEquals(new String(buf, 0, 4), "abcd");
    assertEquals(w.written(), 6);
    assertTrue(w.exhausted());
    assertEquals(w.overflow(), "ef");
  }

  @Test
  public void appendNullCharSequence() {
    char[] buf = new char[10];
    HtmlWriter w = HtmlWriter.wrap(buf, 0, 10);

    w.append((CharSequence) null);

    assertEquals(new String(buf, 0, 4), "null");
    assertEquals(w.written(), 4);
  }

  @Test
  public void appendNullCharSequenceSubSequence() {
    char[] buf = new char[10];
    HtmlWriter w = HtmlWriter.wrap(buf, 0, 10);

    w.append(null, 1, 3);

    assertEquals(new String(buf, 0, 2), "ul");
    assertEquals(w.written(), 2);
  }

  @Test
  public void appendEmptyCharSequence() {
    char[] buf = new char[10];
    HtmlWriter w = HtmlWriter.wrap(buf, 0, 10);

    w.append("");

    assertEquals(w.written(), -1);
    assertFalse(w.exhausted());
  }

  @Test
  public void appendEmptyCharSequenceSubSequence() {
    char[] buf = new char[10];
    HtmlWriter w = HtmlWriter.wrap(buf, 0, 10);

    w.append("hello", 2, 2);

    assertEquals(w.written(), -1);
    assertFalse(w.exhausted());
  }

  @Test
  public void overflowThenAppendChar() {
    char[] buf = new char[2];
    HtmlWriter w = HtmlWriter.wrap(buf, 0, 2);

    w.append("ab");
    w.append('c');

    assertEquals(new String(buf, 0, 2), "ab");
    assertEquals(w.written(), 3);
    assertTrue(w.exhausted());
    assertEquals(w.overflow(), "c");
  }

  @Test
  public void overflowThenAppendMore() {
    char[] buf = new char[2];
    HtmlWriter w = HtmlWriter.wrap(buf, 0, 2);

    w.append("ab");
    w.append('c');
    w.append("de");

    assertEquals(new String(buf, 0, 2), "ab");
    assertEquals(w.written(), 5);
    assertTrue(w.exhausted());
    assertEquals(w.overflow(), "cde");
  }

}