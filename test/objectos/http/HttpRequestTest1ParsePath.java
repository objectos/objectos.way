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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpRequestTest1ParsePath {

  private static final boolean[] VALID_BYTES;

  static {
    final boolean[] valid;
    valid = new boolean[256];

    final String validString;
    validString = Http.unreserved() + Http.subDelims() + ":@";

    for (int idx = 0, len = validString.length(); idx < len; idx++) {
      final char c;
      c = validString.charAt(idx);

      valid[c] = true;
    }

    valid['/'] = true;

    VALID_BYTES = valid;
  }

  @DataProvider
  public Object[][] pathValidProvider() {
    final List<Object[]> l;
    l = new ArrayList<>();

    l.add(valid("/", "/", "root"));
    l.add(valid("/index.html", "/index.html", "with segment"));

    for (int value = 0; value < VALID_BYTES.length; value++) {
      switch (value) {
        case '\n', '\r' -> {/* will trigger 505 not 400 */}

        case '?' -> {/* valid, but tested on parse query */}

        case '%' -> {/* tested on percent-encoded */}

        default -> {
          if (VALID_BYTES[value]) {
            l.add(valid(value));
          }
        }
      }
    }

    return l.toArray(Object[][]::new);
  }

  private Object[] valid(int value) {
    final String raw;
    raw = "/path" + (char) value;

    final String path;
    path = raw;

    final String description;
    description = "path contains the " + Integer.toHexString(value) + " valid byte";

    return valid(raw, path, description);
  }

  private Object[] valid(String raw, String path, String description) {
    return new Object[] {raw, path, description};
  }

  @Test(dataProvider = "pathValidProvider")
  public void pathValid(String raw, String path, String description) throws IOException {
    final HttpRequest req;
    req = HttpRequestTester.parse(
        test -> test.bufferSize(256, 512),

        iso8859("""
        GET %s HTTP/1.1\r
        Host: test\r
        \r
        """.formatted(raw))
    );

    assertEquals(req.path(), path);
  }

  private byte[] iso8859(String s) {
    return s.getBytes(StandardCharsets.ISO_8859_1);
  }

}
