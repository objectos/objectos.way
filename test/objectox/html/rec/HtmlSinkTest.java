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
package objectox.html.rec;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class HtmlSinkTest {

  @Test
  public void addByte() {
    final HtmlSink subject;
    subject = new HtmlSink();

    subject.addByte((byte) 99);

    assertEquals(
        subject,

        new HtmlSink(
            code((byte) 99),
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

  @Test
  public void addInt8_03() {
    final HtmlSink subject;
    subject = new HtmlSink();

    subject.addInt8(0);
    subject.addInt8(1);
    subject.addInt8(126);
    subject.addInt8(127);
    subject.addInt8(128);
    subject.addInt8(255);

    assertEquals(
        subject,

        new HtmlSink(
            code(0, 1, 126, 127, 128, 255),
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

  @Test
  public void addInt16_03() {
    final HtmlSink subject;
    subject = new HtmlSink();

    subject.addInt16(0);
    subject.addInt16(256);
    subject.addInt16(HtmlSink.MAX_INT16);

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
