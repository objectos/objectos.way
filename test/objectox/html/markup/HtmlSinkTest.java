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
package objectox.html.markup;

import static org.testng.Assert.assertEquals;

import objectox.html.IntSize;
import org.testng.annotations.Test;

public class HtmlSinkTest {

  @Test(description = "write a single byte")
  public void addByte() {
    final HtmlSink subject;
    subject = new HtmlSink();

    assertEquals(subject.addByte((byte) 99), 0);

    assertEquals(
        subject,

        new HtmlSink(
            code((byte) 99),
            objects()
        )
    );
  }

  @Test(
      description = "reject int >= 65536",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Invalid 2-byte int value: 65536")
  public void addInt16_01() {
    final HtmlSink subject;
    subject = new HtmlSink();

    subject.addInt16(65536);
  }

  @Test(
      description = "reject int < 0",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Invalid 2-byte int value: -1")
  public void addInt16_02() {
    final HtmlSink subject;
    subject = new HtmlSink();

    subject.addInt16(-1);
  }

  @Test(description = "write 2-byte int, return the index of the first byte")
  public void addInt16_03() {
    final HtmlSink subject;
    subject = new HtmlSink();

    assertEquals(subject.addInt16(0), 0);
    assertEquals(subject.addInt16(256), 2);
    assertEquals(subject.addInt16(HtmlSink.MAX_INT16), 4);

    assertEquals(
        subject,

        new HtmlSink(
            code(
                0x00, 0x00,
                0x01, 0x00,
                0xff, 0xff
            ),

            objects()
        )
    );
  }

  @Test(
      description = "reject int >= 16,777,216",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Invalid 3-byte int value: 16777216")
  public void addInt24_01() {
    final HtmlSink subject;
    subject = new HtmlSink();

    subject.addInt24(16777216);
  }

  @Test(
      description = "reject int < 0",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Invalid 3-byte int value: -1")
  public void addInt24_02() {
    final HtmlSink subject;
    subject = new HtmlSink();

    subject.addInt24(-1);
  }

  @Test(description = "write 3-byte int, return the index of the first byte")
  public void addInt24_03() {
    final HtmlSink subject;
    subject = new HtmlSink();

    assertEquals(subject.addInt24(0), 0);
    assertEquals(subject.addInt24(HtmlSink.MAX_INT24), 3);

    assertEquals(
        subject,

        new HtmlSink(
            code(
                0x00, 0x00, 0x00,
                0xff, 0xff, 0xff
            ),

            objects()
        )
    );
  }

  @Test(
      description = "reject int < 0",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Invalid 1-byte int value: -1")
  public void addInt8_02() {
    final HtmlSink subject;
    subject = new HtmlSink();

    subject.addInt8(-1);
  }

  @Test(description = "write 1-byte int, return the index of the byte")
  public void addInt8_03() {
    final HtmlSink subject;
    subject = new HtmlSink();

    assertEquals(subject.addInt8(0), 0);
    assertEquals(subject.addInt8(1), 1);
    assertEquals(subject.addInt8(126), 2);
    assertEquals(subject.addInt8(127), 3);
    assertEquals(subject.addInt8(128), 4);
    assertEquals(subject.addInt8(255), 5);

    assertEquals(
        subject,

        new HtmlSink(
            code(0, 1, 126, 127, 128, 255),
            objects()
        )
    );
  }

  @Test(description = "write a single object")
  public void addObject01() {
    final HtmlSink subject;
    subject = new HtmlSink();

    assertEquals(subject.addObject("ABC"), 0);

    assertEquals(
        subject,

        new HtmlSink(
            code(),

            objects("ABC")
        )
    );
  }

  @Test(description = "write multiple objects")
  public void addObject02() {
    final HtmlSink subject;
    subject = new HtmlSink();

    assertEquals(subject.addObject("ABC"), 0);
    assertEquals(subject.addObject("XYZ"), 1);

    assertEquals(
        subject,

        new HtmlSink(
            code(),

            objects("ABC", "XYZ")
        )
    );
  }

  @Test(description = "ensure capacity from an empty sink")
  public void ensure01() {
    final HtmlSink subject;
    subject = new HtmlSink();

    final int res;
    res = subject.ensure(0, IntSize.BIT8, 8);

    assertEquals(res, 8);
  }

  @Test(description = "ensure capacity with offset and int16")
  public void ensure02() {
    final HtmlSink subject;
    subject = new HtmlSink();

    final int res;
    res = subject.ensure(2, IntSize.BIT16, 2);

    assertEquals(res, 6);
  }

  @Test(description = "write the current position into a reserved 3-byte int")
  public void jmp2401() {
    final HtmlSink subject;
    subject = new HtmlSink();

    subject.addByte((byte) 5);

    final int idx;
    idx = subject.addInt24(0);

    assertEquals(idx, 1);

    subject.addByte((byte) 9);

    subject.jmp24(idx);

    assertEquals(
        subject,

        new HtmlSink(
            code(5, 0x00, 0x00, 0x05, 9),
            objects()
        )
    );
  }

  @Test(description = "jmp24 records the segment end of an element")
  public void jmp2402() {
    final HtmlSink subject;
    subject = new HtmlSink();

    subject.addByte(HtmlBytes.STARTTAG8);

    subject.addInt8(1);

    subject.addByte(HtmlBytes.JMP24);

    final int idx;
    idx = subject.addInt24(0);

    subject.addByte(HtmlBytes.ENDTAG8);

    subject.addInt8(1);

    subject.jmp24(idx);

    assertEquals(
        subject,

        new HtmlSink(
            code(
                HtmlBytes.STARTTAG8, 0x01,
                HtmlBytes.JMP24, 0x00, 0x00, 0x08,
                HtmlBytes.ENDTAG8, 0x01
            ),
            objects()
        )
    );
  }

  private byte[] code(int... values) {
    final int length;
    length = values.length;

    final byte[] copy;
    copy = new byte[length];

    for (int idx = 0; idx < length; idx++) {
      copy[idx] = (byte) values[idx];
    }

    return copy;
  }

  private Object[] objects(Object... values) {
    return values;
  }

}
