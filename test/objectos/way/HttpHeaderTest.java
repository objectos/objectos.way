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
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.nio.charset.StandardCharsets;
import org.testng.annotations.Test;

public class HttpHeaderTest {

  @Test
  public void createIfValid01() {
    byte[] bytes;
    bytes = "ok".getBytes(StandardCharsets.US_ASCII);

    HttpHeader header;
    header = HttpHeader.createIfValid(bytes, 0, bytes.length);

    assertNotNull(header);
  }

  @Test(description = "Reject non-visible characters")
  public void createIfValid02() {
    final byte[] bytes;
    bytes = new byte[1];

    for (byte b = 0; b < Bytes.SP; b++) {
      bytes[0] = b;

      HttpHeader header;
      header = HttpHeader.createIfValid(bytes, 0, bytes.length);

      assertNull(header);
    }
  }

  @Test(description = "WS")
  public void createIfValid03() {
    assertNull(ascii(" foo"));
    assertNull(ascii("foo "));
    assertNull(ascii("\tfoo"));
    assertNull(ascii("foo\t"));

    assertNotNull(ascii("foo bar"));
    assertNotNull(ascii("foo \tbar"));
    assertNotNull(ascii("foo \t bar"));
  }

  private HttpHeader ascii(String s) {
    final byte[] bytes;
    bytes = s.getBytes(StandardCharsets.US_ASCII);

    return HttpHeader.createIfValid(bytes, 0, bytes.length);
  }

  @Test(description = "Reject non-ascii visible characters")
  public void createIfValid04() {
    assertNull(iso("café"));
  }

  private HttpHeader iso(String s) {
    final byte[] bytes;
    bytes = s.getBytes(StandardCharsets.ISO_8859_1);

    return HttpHeader.createIfValid(bytes, 0, bytes.length);
  }

  @Test
  public void get01() {
    byte[] bytes;
    bytes = "foo".getBytes(StandardCharsets.US_ASCII);

    HttpHeader header;
    header = HttpHeader.createIfValid(bytes, 0, bytes.length);

    assertEquals(header.get(bytes), "foo");
    assertEquals(header.get(bytes), "foo");
  }

  @Test
  public void get02() {
    byte[] bytes;
    bytes = "foo".getBytes(StandardCharsets.US_ASCII);

    HttpHeader header;
    header = HttpHeader.createIfValid(bytes, 0, 0);

    assertEquals(header.get(bytes), "");
    assertEquals(header.get(bytes), "");
  }

  @Test
  public void get03() {
    byte[] bytes;
    bytes = "foobar".getBytes(StandardCharsets.US_ASCII);

    HttpHeader header;
    header = HttpHeader.createIfValid(bytes, 0, 3);

    header.add(HttpHeader.createIfValid(bytes, 3, 6));

    assertEquals(header.get(bytes), "foo");
    assertEquals(header.get(bytes), "foo");
  }

  @Test
  public void unsignedLongValue01() {
    byte[] bytes;
    bytes = "123".getBytes(StandardCharsets.US_ASCII);

    HttpHeader header;
    header = HttpHeader.createIfValid(bytes, 0, bytes.length);

    assertEquals(header.unsignedLongValue(bytes), 123L);
  }

  @Test
  public void unsignedLongValue02() {
    long max;
    max = Long.MAX_VALUE;

    byte[] bytes;
    bytes = Long.toString(max).getBytes(StandardCharsets.US_ASCII);

    HttpHeader header;
    header = HttpHeader.createIfValid(bytes, 0, bytes.length);

    assertEquals(header.unsignedLongValue(bytes), max);
  }

  @Test(description = "String length exceeds 19 chars")
  public void unsignedLongValue03() {
    final byte[] bytes;
    bytes = "92233720368547758070".getBytes(StandardCharsets.US_ASCII);

    HttpHeader header;
    header = HttpHeader.createIfValid(bytes, 0, bytes.length);

    assertEquals(header.unsignedLongValue(bytes), Long.MIN_VALUE);
  }

  @Test(description = "Value will cause long overflow")
  public void unsignedLongValue04() {
    final byte[] bytes;
    bytes = "9223372036854775808".getBytes(StandardCharsets.US_ASCII);

    HttpHeader header;
    header = HttpHeader.createIfValid(bytes, 0, bytes.length);

    assertEquals(header.unsignedLongValue(bytes), Long.MIN_VALUE);
  }

  @Test(description = "Invalid character")
  public void unsignedLongValue05() {
    final byte[] bytes;
    bytes = "922337203685477580X".getBytes(StandardCharsets.US_ASCII);

    HttpHeader header;
    header = HttpHeader.createIfValid(bytes, 0, bytes.length);

    assertEquals(header.unsignedLongValue(bytes), Long.MIN_VALUE);
  }

  @Test(description = "Kind.MANY")
  public void unsignedLongValue06() {
    final byte[] bytes;
    bytes = "123987".getBytes(StandardCharsets.US_ASCII);

    HttpHeader header;
    header = HttpHeader.createIfValid(bytes, 0, 3);

    header.add(HttpHeader.createIfValid(bytes, 3, 6));

    assertEquals(header.unsignedLongValue(bytes), 123L);
  }

}
