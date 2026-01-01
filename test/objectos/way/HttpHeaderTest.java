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
package objectos.way;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpHeaderTest {

  @Test
  public void create01() {
    byte[] bytes;
    bytes = "ok".getBytes(StandardCharsets.US_ASCII);

    HttpHeader header;
    header = HttpHeader.create(0, bytes.length);

    assertNotNull(header);
  }

  @Test
  public void get01() {
    byte[] bytes;
    bytes = "foo".getBytes(StandardCharsets.US_ASCII);

    HttpHeader header;
    header = HttpHeader.create(0, bytes.length);

    assertEquals(header.get(bytes), "foo");
    assertEquals(header.get(bytes), "foo");
  }

  @Test
  public void get02() {
    byte[] bytes;
    bytes = "foo".getBytes(StandardCharsets.US_ASCII);

    HttpHeader header;
    header = HttpHeader.create(0, 0);

    assertEquals(header.get(bytes), "");
    assertEquals(header.get(bytes), "");
  }

  @Test
  public void get03() {
    byte[] bytes;
    bytes = "foobar".getBytes(StandardCharsets.US_ASCII);

    HttpHeader header;
    header = HttpHeader.create(0, 3);

    header.add(HttpHeader.create(3, 6));

    assertEquals(header.get(bytes), "foo");
    assertEquals(header.get(bytes), "foo");
  }

  @DataProvider
  public Object[][] unsignedLongValueValidProvider() {
    return new Object[][] {
        {"0", 0L},
        {"123", 123L},
        {Long.toString(Long.MAX_VALUE), Long.MAX_VALUE},

        {"922337203685477580X", HttpHeader.LONG_INVALID},
        {"-123", HttpHeader.LONG_INVALID},
        {"+123", HttpHeader.LONG_INVALID},

        {"92233720368547758070", HttpHeader.LONG_OVERFLOW},
        {"9223372036854775808", HttpHeader.LONG_OVERFLOW},
        {BigInteger.valueOf(Long.MAX_VALUE)
            .multiply(BigInteger.TWO)
            .add(BigInteger.ONE)
            .toString(), HttpHeader.LONG_OVERFLOW},
        {BigInteger.valueOf(Long.MAX_VALUE)
            .multiply(BigInteger.TWO)
            .add(BigInteger.TWO)
            .toString(), HttpHeader.LONG_OVERFLOW}

    };
  }

  @Test(dataProvider = "unsignedLongValueValidProvider")
  public void unsignedLongValue(String s, long expected) {
    byte[] bytes;
    bytes = s.getBytes(StandardCharsets.US_ASCII);

    HttpHeader header;
    header = HttpHeader.create(0, bytes.length);

    assertEquals(header.unsignedLongValue(bytes), expected);

    assertEquals(header.get(bytes), s);

    assertEquals(header.unsignedLongValue(bytes), expected);
  }

  @Test(dataProvider = "unsignedLongValueValidProvider")
  public void unsignedLongValueMany(String s, long expected) {
    final int length;
    length = s.length();

    final String x;
    x = s + "x";

    final byte[] bytes;
    bytes = x.getBytes(StandardCharsets.US_ASCII);

    HttpHeader header;
    header = HttpHeader.create(0, length);

    header.add(HttpHeader.create(length, length + 1));

    assertEquals(header.unsignedLongValue(bytes), expected);

    assertEquals(header.get(bytes), s);

    assertEquals(header.unsignedLongValue(bytes), expected);
  }

}
