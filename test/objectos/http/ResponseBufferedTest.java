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
package objectos.http;

import static org.testng.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import org.testng.annotations.Test;

public class ResponseBufferedTest {

  private void write(int len, String s) {
    final byte[] buffer;
    buffer = new byte[len];

    final byte[] bytes;
    bytes = s.getBytes(StandardCharsets.US_ASCII);

    final ByteArrayOutputStream baos;
    baos = new ByteArrayOutputStream();

    try (var out = new ResponseBuffered(buffer, baos)) {
      out.write(bytes);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    final byte[] written;
    written = baos.toByteArray();

    final String result;
    result = new String(written, StandardCharsets.US_ASCII);

    assertEquals(s, result);
  }

  @Test(description = "bytes < buffer.length")
  public void write01() {
    write(64, "1".repeat(32));
  }

  @Test(description = "bytes == buffer.length")
  public void write02() {
    write(64, "1".repeat(64));
  }

  @Test(description = "bytes > buffer.length")
  public void write03() {
    write(64, "1".repeat(64 + 32));
  }

  @Test(description = "bytes > 3 x buffer.length")
  public void write04() {
    write(64, "1".repeat(64 * 3 + 1));
  }

}
