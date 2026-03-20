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
import objectos.http.HttpRequestParser.InvalidRequestLine;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SuppressWarnings("exports")
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

  @DataProvider
  public Object[][] pathInvalidProvider() {
    List<Object[]> l = new ArrayList<>();

    l.add(invalid("", InvalidRequestLine.PATH_FIRST_CHAR, "empty path"));
    l.add(invalid("index.html", InvalidRequestLine.PATH_FIRST_CHAR, "path does not begin with '/'"));
    l.add(invalid("//index.html", InvalidRequestLine.PATH_SEGMENT_NZ, "path begins with empty segment"));
    l.add(invalid("/%2Findex.html", InvalidRequestLine.PATH_SEGMENT_NZ, "path begins with empty segment"));
    l.add(invalid("%2F/index.html", InvalidRequestLine.PATH_SEGMENT_NZ, "path begins with empty segment"));
    l.add(invalid("%2F%2Findex.html", InvalidRequestLine.PATH_SEGMENT_NZ, "path begins with empty segment"));

    for (int value = 0; value < VALID_BYTES.length; value++) {
      switch (value) {
        case ' ', '\n', '\r' -> {/* will trigger 505 not 400 */}

        case '?' -> {/* valid, but tested on parse query */}

        case '%' -> {/* tested on percent-encoded */}

        default -> {
          if (!VALID_BYTES[value]) {
            l.add(invalid(value));
          }
        }
      }
    }

    return l.toArray(Object[][]::new);
  }

  private Object[] invalid(int value) {
    final String path;
    path = "/pa" + (char) value + "th";

    final String description;
    description = "path contains the " + Integer.toHexString(value) + " invalid byte";

    return invalid(path, InvalidRequestLine.PATH_NEXT_CHAR, description);
  }

  private Object[] invalid(
      String path,
      HttpClientException.Kind kind,
      String description) {
    if (!path.isEmpty()) {
      path = " " + path;
    }

    String req = """
    GET%s HTTP/1.1\r
    \r
    """.formatted(path);

    return new Object[] {req, kind, description};
  }

  @Test(dataProvider = "pathInvalidProvider")
  public void pathInvalid(
      String request,
      HttpClientException.Kind kind,
      String description) throws IOException {
    try {
      HttpRequestTester.parse(
          test -> test.bufferSize(256, 512),

          iso8859(request)
      );

      Assert.fail("It should have thrown");
    } catch (HttpClientException expected) {
      assertEquals(expected.kind, kind);
    }
  }

  @Test
  public void pathInvalid0() throws IOException {
    Object[] invalid = invalid("/%2Findex.html", InvalidRequestLine.PATH_SEGMENT_NZ, "path begins with empty segment");

    pathInvalid((String) invalid[0], (HttpClientException.Kind) invalid[1], (String) invalid[2]);
  }

  private byte[] iso8859(String s) {
    return s.getBytes(StandardCharsets.ISO_8859_1);
  }

}
