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
package objectos.script;

import static org.testng.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import objectos.http.Content;
import org.testng.annotations.Test;

public class JsLibraryTest {

  @Test
  public void of() throws IOException {
    final JsLibrary library;
    library = JsLibrary.of();

    final Content content;
    content = library.toContent();

    final ByteArrayOutputStream out;
    out = new ByteArrayOutputStream();

    content.binaryTo(out);

    final byte[] bytes;
    bytes = out.toByteArray();

    final String result;
    result = new String(bytes, StandardCharsets.UTF_8);

    assertTrue(result.contains("Objectos Software LTDA"));
    assertTrue(result.contains("END: private private"));
  }

}