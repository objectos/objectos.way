/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
package objectos.way;

import static org.testng.Assert.assertEquals;

import java.util.Base64;
import java.util.HexFormat;
import java.util.Random;
import java.util.random.RandomGenerator;
import org.testng.annotations.Test;

public class WebTokenTest {

  private final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

  private final HexFormat hexFormat = HexFormat.of();

  private final RandomGenerator random = new Random(123456789L);

  @Test
  public void testCase01() {
    final WebToken token;
    token = WebToken.of32(random);

    final byte[] bytes;
    bytes = token.toByteArray();

    assertEquals(hex(token), hexFormat.formatHex(bytes));
  }

  @Test
  public void testCase02() {
    final WebToken token;
    token = WebToken.of32(random);

    final byte[] bytes;
    bytes = token.toByteArray();

    assertEquals(token.toString(), base64Encoder.encodeToString(bytes));
  }

  @Test
  public void testCase03() {
    for (int i = 0; i < 100; i++) {
      final WebToken token;
      token = WebToken.of32(random);

      final byte[] bytes;
      bytes = token.toByteArray();

      assertEquals(token.toString(), base64Encoder.encodeToString(bytes));
    }
  }

  @Test
  public void testCase04() throws WebToken.ParseException {
    final WebToken token;
    token = WebToken.of32(random);

    final String s;
    s = token.toString();

    final WebToken result;
    result = WebToken.parse(s, 32);

    assertEquals(result, token);
  }

  @Test
  public void testCase05() throws WebToken.ParseException {
    for (int i = 0; i < 100; i++) {
      final WebToken token;
      token = WebToken.of32(random);

      final String s;
      s = token.toString();

      final WebToken result;
      result = WebToken.parse(s, 32);

      assertEquals(result, token);
    }
  }

  @Test
  public void testCase06() {
    final WebToken token;
    token = WebToken.of32(1L, 2L, 3L, 4L);

    assertEquals(token.toString(), "AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=");
  }

  private String hex(WebToken token) {
    return hexFormat.toHexDigits(token.l0)
        + hexFormat.toHexDigits(token.l1)
        + hexFormat.toHexDigits(token.l2)
        + hexFormat.toHexDigits(token.l3);
  }

}