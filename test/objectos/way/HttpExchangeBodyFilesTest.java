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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import org.testng.annotations.Test;

public class HttpExchangeBodyFilesTest {

  @Test
  public void standard01() throws IOException {
    final HttpExchangeBodyFiles files;
    files = HttpExchangeBodyFiles.standard();

    final Path file1;
    file1 = files.file(1);

    assertEquals(file1, files.directory().resolve("0000000000000000001"));

    try (OutputStream out = files.newOutputStream(file1)) {
      out.write("Hello!".getBytes(StandardCharsets.US_ASCII));
    }

    try (InputStream in = files.newInputStream(file1)) {
      final byte[] bytes;
      bytes = in.readAllBytes();

      final String result;
      result = new String(bytes, StandardCharsets.US_ASCII);

      assertEquals(result, "Hello!");
    }
  }

}