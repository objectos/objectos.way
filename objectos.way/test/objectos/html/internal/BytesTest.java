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
package objectos.html.internal;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class BytesTest {

  @Test
  public void varIntR() {
    byte[] buf = new byte[2];
    int off = 0;
    int value = 17482;
    int valbin = 0b0100_0100_0100_1010;

    assertEquals(value, valbin);

    byte l = (byte) 0b1100_1010;
    byte h = (byte) 0b1000_1000;

    assertEquals(l, -54);
    assertEquals(h, -120);

    int off2 = Bytes.encodeVarIntR(buf, off, value);

    assertEquals(off2, 2);
    assertEquals(buf[0], -120);
    assertEquals(buf[1], -54);

    assertEquals(value, (l & 0x7F) | (h & 0xFF) << 7);
    assertEquals(value, Bytes.decodeVarInt(buf[1], buf[0]));
  }

  @Test
  public void varIntMax() {
    assertEquals(Bytes.VARINT_MAX2, 0b1111_1111_111_1111);
  }

}