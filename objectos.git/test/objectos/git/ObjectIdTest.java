/*
 * Copyright (C) 2020-2023 Objectos Software LTDA.
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
package objectos.git;

import static org.testng.Assert.assertEquals;

import java.security.SecureRandom;
import java.util.Random;
import objectos.util.array.ByteArrays;
import org.testng.annotations.Test;

public class ObjectIdTest {

  private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

  private final Random random = new SecureRandom();

  @Test
  public void getFanOutIndex() throws InvalidObjectIdFormatException {
    byte[] trailingBytes;
    trailingBytes = new byte[19];

    for (int expectedIndex = 0; expectedIndex < 256; expectedIndex++) {
      StringBuilder sb;
      sb = new StringBuilder();

      char highChar;
      highChar = toHexChar(expectedIndex >>> 4);

      sb.append(highChar);

      char lowChar;
      lowChar = toHexChar(expectedIndex & 0xF);

      sb.append(lowChar);

      random.nextBytes(trailingBytes);

      String randomTrailer;
      randomTrailer = ByteArrays.toHexString(trailingBytes);

      sb.append(randomTrailer);

      String id;
      id = sb.toString();

      ObjectId oid;
      oid = ObjectId.parse(id);

      int result;
      result = oid.getFanOutIndex();

      assertEquals(result, expectedIndex);
    }
  }

  @Test
  public void getHexString() throws InvalidObjectIdFormatException {
    ObjectId id;
    id = ObjectId.parse("d670460b4b4aece5915caf5c68d12f560a9fe3e4");

    assertEquals(id.getHexString(), "d670460b4b4aece5915caf5c68d12f560a9fe3e4");
  }

  @Test
  public void toStringTest() throws InvalidObjectIdFormatException {
    ObjectId id;
    id = ObjectId.parse("d670460b4b4aece5915caf5c68d12f560a9fe3e4");

    assertEquals(id.toString(), "d670460b4b4aece5915caf5c68d12f560a9fe3e4");
  }

  private char toHexChar(int i) {
    return HEX_CHARS[i];
  }

}
