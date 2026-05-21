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
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import objectos.lang.Throwables;
import org.testng.annotations.Test;

public class HttpResponse3ChunkedTest {

  private final String row = "|" + "-".repeat(29) + "|\n";

  @Test
  public void testCase01() throws IOException {
    final ByteArrayOutputStream baos;
    baos = new ByteArrayOutputStream();

    try (OutputStream out = of(baos, 256, 0)) {
      w(out, row.repeat(7));
    }

    assertEquals(
        toString(baos),

        """
        E0\r
        |-----------------------------|
        |-----------------------------|
        |-----------------------------|
        |-----------------------------|
        |-----------------------------|
        |-----------------------------|
        |-----------------------------|
        \r
        0\r
        \r
        """
    );
  }

  @Test
  public void testCase02() throws IOException {
    final ByteArrayOutputStream baos;
    baos = new ByteArrayOutputStream();

    try (OutputStream out = of(baos, 256, 0)) {
      w(out, row.repeat(8));
    }

    assertEquals(
        toString(baos),

        """
        FA\r
        |-----------------------------|
        |-----------------------------|
        |-----------------------------|
        |-----------------------------|
        |-----------------------------|
        |-----------------------------|
        |-----------------------------|
        |-------------------------\r
        06\r
        ----|
        \r
        0\r
        \r
        """
    );
  }

  @Test
  public void testCase03() throws IOException {
    final ByteArrayOutputStream baos;
    baos = new ByteArrayOutputStream();

    try (OutputStream out = of(baos, 263, 0)) {
      w(out, row.repeat(8));
    }

    assertEquals(
        toString(baos),

        """
        100\r
        |-----------------------------|
        |-----------------------------|
        |-----------------------------|
        |-----------------------------|
        |-----------------------------|
        |-----------------------------|
        |-----------------------------|
        |-----------------------------|
        \r
        0\r
        \r
        """
    );
  }

  @Test
  public void testCase04() {
    var baos = new OutputStream() {
      @Override
      public final void write(int b) throws IOException {
        // noop
      }

      @Override
      public final void write(byte[] b, int off, int len) throws IOException {
        throw Throwables.trimStackTrace(new IOException(), 1);
      }
    };

    try (OutputStream out = of(baos, 263, 0)) {
      w(out, row.repeat(10));
    } catch (IOException expected) {
      final Throwable[] suppressed;
      suppressed = expected.getSuppressed();

      assertEquals(suppressed.length, 0);
    }
  }

  private OutputStream of(OutputStream output, int size, int offset) {
    final byte[] buffer;
    buffer = new byte[size];

    try {
      return new HttpResponse3Chunked(buffer, 0, output);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private void w(OutputStream out, String s) throws IOException {
    final byte[] bytes;
    bytes = s.getBytes(StandardCharsets.US_ASCII);

    out.write(bytes);
  }

  private String toString(ByteArrayOutputStream baos) {
    final byte[] bytes;
    bytes = baos.toByteArray();

    return new String(bytes, StandardCharsets.US_ASCII);
  }

}
