/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.css.internal;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class BytesTest {

  @Test
  public void standardName() {
    for (StandardName name : StandardName.values()) {
      byte name0 = Bytes.name0(name);
      byte name1 = Bytes.name1(name);

      assertEquals(
        Bytes.standardNameValue(name0, name1),
        name.cssName
      );
    }
  }

  @Test
  public void varInt() {
    // max 1 byte
    assertEquals(Bytes.VARINT_MAX1, 127);

    // max 2 bytes
    assertEquals(Bytes.VARINT_MAX2, 32767);

    byte[] buf;
    buf = new byte[2];

    assertEquals(Bytes.encodeVariableLengthR(buf, 0, 1), 1);
    assertEquals(buf[0], 0x01);
    assertEquals(buf[1], 0x00);

    assertEquals(Bytes.encodeVariableLengthR(buf, 0, 127), 1);
    assertEquals(buf[0], 0x7F);
    assertEquals(buf[1], 0x00);

    assertEquals(Bytes.encodeVariableLengthR(buf, 0, 128), 2);
    assertEquals(buf[0], 0x01);
    assertEquals(buf[1] & 0xFF, 0x80);

    assertEquals(Bytes.decodeVariableLength(buf[1], buf[0]), 128);

    assertEquals(Bytes.var0(128) & 0xFF, 0x80);
    assertEquals(Bytes.var1(128) & 0xFF, 0x01);

  }

}