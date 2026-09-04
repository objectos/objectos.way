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

public class OutputTest {

  @Test(description = "no writes returns -1")
  public void written01() {
    final char[] buf;
    buf = new char[10];

    final Output subject;
    subject = Output.wrap(buf, 0, 10);

    assertEquals(subject.written(), -1);
  }

  @Test(description = "written does not count chars diverted to the overflow")
  public void written02() {
    final char[] buf;
    buf = new char[0];

    final Output subject;
    subject = Output.wrap(buf, 0, 0);

    subject.append("world");

    assertEquals(subject.overflow(), "world");
    assertEquals(subject.written(), -1);
  }

  @Test(description = "written counts only the chars stored in the buffer")
  public void written03() {
    final char[] buf;
    buf = new char[5];

    final Output subject;
    subject = Output.wrap(buf, 0, 5);

    subject.append("hello world");

    assertEquals(new String(buf, 0, 5), "hello");
    assertEquals(subject.overflow(), " world");
    assertEquals(subject.written(), 5);
  }

  @Test(description = "append single char")
  public void appendChar01() {
    final char[] buf;
    buf = new char[10];

    final Output subject;
    subject = Output.wrap(buf, 0, 10);

    subject.append('a');

    assertEquals(buf[0], 'a');
    assertEquals(subject.written(), 1);
    assertFalse(subject.exhausted());
  }

  @Test(description = "append single char with offset")
  public void appendChar02() {
    final char[] buf;
    buf = new char[10];

    final Output subject;
    subject = Output.wrap(buf, 3, 5);

    subject.append('x');

    assertEquals(buf[3], 'x');
    assertEquals(subject.written(), 1);
    assertFalse(subject.exhausted());
  }

  @Test(description = "append char sequence")
  public void appendCharSequence01() {
    final char[] buf;
    buf = new char[20];

    final Output subject;
    subject = Output.wrap(buf, 0, 20);

    subject.append("hello");

    assertEquals(new String(buf, 0, 5), "hello");
    assertEquals(subject.written(), 5);
    assertFalse(subject.exhausted());
  }

  @Test(description = "append char sequence with offset")
  public void appendCharSequence02() {
    final char[] buf;
    buf = new char[20];

    final Output subject;
    subject = Output.wrap(buf, 2, 10);

    subject.append("abc");

    assertEquals(buf[2], 'a');
    assertEquals(buf[3], 'b');
    assertEquals(buf[4], 'c');
    assertEquals(subject.written(), 3);
  }

  @Test(description = "fill buffer exactly")
  public void exhausted01() {
    final char[] buf;
    buf = new char[5];

    final Output subject;
    subject = Output.wrap(buf, 0, 5);

    subject.append("abcde");

    assertEquals(new String(buf, 0, 5), "abcde");
    assertEquals(subject.written(), 5);
    assertTrue(subject.exhausted());
    assertEquals(subject.overflow(), "");
  }

  @Test(description = "overflow one char")
  public void overflow01() {
    final char[] buf;
    buf = new char[3];

    final Output subject;
    subject = Output.wrap(buf, 0, 3);

    subject.append("abcd");

    assertEquals(new String(buf, 0, 3), "abc");
    assertEquals(subject.written(), 3);
    assertTrue(subject.exhausted());
    assertEquals(subject.overflow(), "d");
  }

  @Test(description = "overflow multiple chars")
  public void overflow02() {
    final char[] buf;
    buf = new char[3];

    final Output subject;
    subject = Output.wrap(buf, 0, 3);

    subject.append("abcdef");

    assertEquals(new String(buf, 0, 3), "abc");
    assertEquals(subject.written(), 3);
    assertTrue(subject.exhausted());
    assertEquals(subject.overflow(), "def");
  }

  @Test(description = "overflow with offset")
  public void overflow03() {
    final char[] buf;
    buf = new char[10];

    final Output subject;
    subject = Output.wrap(buf, 8, 2);

    subject.append("abcde");

    assertEquals(buf[8], 'a');
    assertEquals(buf[9], 'b');
    assertEquals(subject.written(), 2);
    assertTrue(subject.exhausted());
    assertEquals(subject.overflow(), "cde");
  }

  @Test(description = "overflow append char")
  public void overflow04() {
    final char[] buf;
    buf = new char[1];

    final Output subject;
    subject = Output.wrap(buf, 0, 1);

    subject.append('a');
    subject.append('b');

    assertEquals(buf[0], 'a');
    assertEquals(subject.written(), 1);
    assertTrue(subject.exhausted());
    assertEquals(subject.overflow(), "b");
  }

  @Test(description = "multiple appends")
  public void append01() {
    final char[] buf;
    buf = new char[10];

    final Output subject;
    subject = Output.wrap(buf, 0, 10);

    subject.append('a');
    subject.append("bc");
    subject.append("def");

    assertEquals(new String(buf, 0, 6), "abcdef");
    assertEquals(subject.written(), 6);
    assertFalse(subject.exhausted());
  }

  @Test(description = "multiple appends with overflow")
  public void append02() {
    final char[] buf;
    buf = new char[4];

    final Output subject;
    subject = Output.wrap(buf, 0, 4);

    subject.append("ab");
    subject.append("cd");
    subject.append("ef");

    assertEquals(new String(buf, 0, 4), "abcd");
    assertEquals(subject.written(), 4);
    assertTrue(subject.exhausted());
    assertEquals(subject.overflow(), "ef");
  }

  @Test(description = "append null char sequence")
  public void appendNull01() {
    final char[] buf;
    buf = new char[10];

    final Output subject;
    subject = Output.wrap(buf, 0, 10);

    subject.append((CharSequence) null);

    assertEquals(new String(buf, 0, 4), "null");
    assertEquals(subject.written(), 4);
  }

  @Test(description = "append empty char sequence")
  public void appendEmpty01() {
    final char[] buf;
    buf = new char[10];

    final Output subject;
    subject = Output.wrap(buf, 0, 10);

    subject.append("");

    assertEquals(subject.written(), -1);
    assertFalse(subject.exhausted());
  }

  @Test(description = "overflow then append char")
  public void overflowAppend01() {
    final char[] buf;
    buf = new char[2];

    final Output subject;
    subject = Output.wrap(buf, 0, 2);

    subject.append("ab");
    subject.append('c');

    assertEquals(new String(buf, 0, 2), "ab");
    assertEquals(subject.written(), 2);
    assertTrue(subject.exhausted());
    assertEquals(subject.overflow(), "c");
  }

  @Test(description = "overflow then append more")
  public void overflowAppend02() {
    final char[] buf;
    buf = new char[2];

    final Output subject;
    subject = Output.wrap(buf, 0, 2);

    subject.append("ab");
    subject.append('c');
    subject.append("de");

    assertEquals(new String(buf, 0, 2), "ab");
    assertEquals(subject.written(), 2);
    assertTrue(subject.exhausted());
    assertEquals(subject.overflow(), "cde");
  }

  @Test(description = "toString with no writes returns empty string")
  public void toString01() {
    final char[] buf;
    buf = new char[10];

    final Output subject;
    subject = Output.wrap(buf, 0, 10);

    assertEquals(subject.toString(), "");
  }

  @Test(description = "toString with buffer only")
  public void toString02() {
    final char[] buf;
    buf = new char[10];

    final Output subject;
    subject = Output.wrap(buf, 0, 10);

    subject.append("hello");

    assertEquals(subject.toString(), "hello");
  }

  @Test(description = "toString with overflow only")
  public void toString03() {
    final char[] buf;
    buf = new char[0];

    final Output subject;
    subject = Output.wrap(buf, 0, 0);

    subject.append("world");

    assertEquals(subject.toString(), "world");
  }

  @Test(description = "toString with buffer and overflow")
  public void toString04() {
    final char[] buf;
    buf = new char[5];

    final Output subject;
    subject = Output.wrap(buf, 0, 5);

    subject.append("hello world");

    assertEquals(subject.toString(), "hello world");
  }

  @Test(description = "toString with offset")
  public void toString05() {
    final char[] buf;
    buf = new char[10];

    final Output subject;
    subject = Output.wrap(buf, 3, 5);

    subject.append("abc");
    subject.append("defgh");

    assertEquals(subject.toString(), "abcdefgh");
  }

  @Test(description = "toString with mixed appends")
  public void toString06() {
    final char[] buf;
    buf = new char[4];

    final Output subject;
    subject = Output.wrap(buf, 0, 4);

    subject.append('a');
    subject.append("bc");
    subject.append("def");

    assertEquals(subject.toString(), "abcdef");
  }

  @Test(description = "startOfLine returns true when empty")
  public void startOfLine01() {
    final char[] buf;
    buf = new char[10];

    final Output subject;
    subject = Output.wrap(buf, 0, 10);

    assertEquals(subject.startOfLine(), true);
  }

  @Test(description = "startOfLine returns false after appending regular char")
  public void startOfLine02() {
    final char[] buf;
    buf = new char[10];

    final Output subject;
    subject = Output.wrap(buf, 0, 10);

    subject.append('a');

    assertEquals(subject.startOfLine(), false);
  }

  @Test(description = "startOfLine returns true after appending newline")
  public void startOfLine03() {
    final char[] buf;
    buf = new char[10];

    final Output subject;
    subject = Output.wrap(buf, 0, 10);

    subject.append('\n');

    assertEquals(subject.startOfLine(), true);
  }

  @Test(description = "startOfLine returns true after appending carriage return")
  public void startOfLine04() {
    final char[] buf;
    buf = new char[10];

    final Output subject;
    subject = Output.wrap(buf, 0, 10);

    subject.append('\r');

    assertEquals(subject.startOfLine(), true);
  }

  @Test(description = "startOfLine returns true after appending string ending with newline")
  public void startOfLine05() {
    final char[] buf;
    buf = new char[10];

    final Output subject;
    subject = Output.wrap(buf, 0, 10);

    subject.append("hello\n");

    assertEquals(subject.startOfLine(), true);
  }

  @Test(description = "startOfLine returns false after appending string not ending with newline")
  public void startOfLine06() {
    final char[] buf;
    buf = new char[10];

    final Output subject;
    subject = Output.wrap(buf, 0, 10);

    subject.append("hello");

    assertEquals(subject.startOfLine(), false);
  }

  @Test(description = "startOfLine returns true after appending string ending with carriage return")
  public void startOfLine07() {
    final char[] buf;
    buf = new char[10];

    final Output subject;
    subject = Output.wrap(buf, 0, 10);

    subject.append("hello\r");

    assertEquals(subject.startOfLine(), true);
  }

  @Test(description = "startOfLine returns true when the overflow ends with a newline")
  public void startOfLine08() {
    final char[] buf;
    buf = new char[2];

    final Output subject;
    subject = Output.wrap(buf, 0, 2);

    subject.append("ab");
    subject.append("c\n");

    assertEquals(subject.startOfLine(), true);
  }

  @Test(description = "startOfLine returns false when the overflow ends with a regular char")
  public void startOfLine09() {
    final char[] buf;
    buf = new char[2];

    final Output subject;
    subject = Output.wrap(buf, 0, 2);

    subject.append("ab");
    subject.append("cd");

    assertEquals(subject.startOfLine(), false);
  }

  @Test(description = "startOfLine checks the overflow after the buffer is exhausted")
  public void startOfLine10() {
    final char[] buf;
    buf = new char[4];

    final Output subject;
    subject = Output.wrap(buf, 0, 4);

    subject.append("a\nb\n");
    subject.append("xy");

    assertEquals(subject.startOfLine(), false);
  }

}