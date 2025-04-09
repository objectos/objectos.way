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

import java.nio.charset.StandardCharsets;
import org.testng.annotations.Test;

public class HttpHeaderTest {

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

  @Test
  public void unsignedLongValue01() {
    byte[] bytes;
    bytes = "123".getBytes(StandardCharsets.US_ASCII);

    HttpHeader header;
    header = HttpHeader.create(0, bytes.length);

    assertEquals(header.unsignedLongValue(bytes), 123L);
  }

  @Test
  public void unsignedLongValue02() {
    long max;
    max = Long.MAX_VALUE;

    byte[] bytes;
    bytes = Long.toString(max).getBytes(StandardCharsets.US_ASCII);

    HttpHeader header;
    header = HttpHeader.create(0, bytes.length);

    assertEquals(header.unsignedLongValue(bytes), max);
  }

  @Test(description = "String length exceeds 19 chars")
  public void unsignedLongValue03() {
    final byte[] bytes;
    bytes = "92233720368547758070".getBytes(StandardCharsets.US_ASCII);

    HttpHeader header;
    header = HttpHeader.create(0, bytes.length);

    assertEquals(header.unsignedLongValue(bytes), Long.MIN_VALUE);
  }

  @Test(description = "Value will cause long overflow")
  public void unsignedLongValue04() {
    final byte[] bytes;
    bytes = "9223372036854775808".getBytes(StandardCharsets.US_ASCII);

    HttpHeader header;
    header = HttpHeader.create(0, bytes.length);

    assertEquals(header.unsignedLongValue(bytes), Long.MIN_VALUE);
  }

  @Test(description = "Invalid character")
  public void unsignedLongValue05() {
    final byte[] bytes;
    bytes = "922337203685477580X".getBytes(StandardCharsets.US_ASCII);

    HttpHeader header;
    header = HttpHeader.create(0, bytes.length);

    assertEquals(header.unsignedLongValue(bytes), Long.MIN_VALUE);
  }

  @Test(description = "Kind.MANY")
  public void unsignedLongValue06() {
    final byte[] bytes;
    bytes = "123987".getBytes(StandardCharsets.US_ASCII);

    HttpHeader header;
    header = HttpHeader.create(0, 3);

    header.add(HttpHeader.create(3, 6));

    assertEquals(header.unsignedLongValue(bytes), 123L);
  }

}
