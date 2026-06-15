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
package objectox.http.resp;

import static org.testng.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.testng.annotations.Test;

public class ResponseChunkedTest {

  private String write(int len, String prefix, String s) {
    final byte[] prefixBytes;
    prefixBytes = prefix.getBytes(StandardCharsets.US_ASCII);

    final byte[] buffer;
    buffer = Arrays.copyOf(prefixBytes, len);

    final int bufferIndex;
    bufferIndex = prefix.length();

    final byte[] bytes;
    bytes = s.getBytes(StandardCharsets.US_ASCII);

    final ByteArrayOutputStream baos;
    baos = new ByteArrayOutputStream();

    try (var out = ResponseChunked.of(buffer, bufferIndex, baos)) {
      out.write(bytes);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    final byte[] written;
    written = baos.toByteArray();

    return new String(written, StandardCharsets.US_ASCII);
  }

  @Test
  public void write01() {
    assertEquals(
        write(32, "", "0123456789abcdef".repeat(1)),

        """
        10\r
        0123456789abcdef\r
        0\r
        \r
        """
    );
  }

  @Test
  public void write02() {
    assertEquals(
        write(32, "", "0123456789abcdef".repeat(2)),

        """
        1A\r
        0123456789abcdef0123456789\r
        06\r
        abcdef\r
        0\r
        \r
        """
    );
  }

  @Test
  public void write03() {
    assertEquals(
        write(32, "....\r\n", "0123456789abcdef".repeat(1)),

        """
        ....\r
        10\r
        0123456789abcdef\r
        0\r
        \r
        """
    );
  }

}
