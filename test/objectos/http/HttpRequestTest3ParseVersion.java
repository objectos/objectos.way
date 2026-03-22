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
import objectos.http.HttpRequestParser.InvalidRequestLine;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpRequestTest3ParseVersion {

  @DataProvider
  public Object[][] versionValidProvider() {
    return new Object[][] {
        {"GET / HTTP/1.1", HttpVersion.HTTP_1_1, "1.1 + path"},
        {"GET /%C3%A1 HTTP/1.1", HttpVersion.HTTP_1_1, "1.1 + path + percent"},
        {"GET /url?key=value HTTP/1.1", HttpVersion.HTTP_1_1, "1.1 + query key + value"},
        {"GET /url?key HTTP/1.1", HttpVersion.HTTP_1_1, "1.1 + query key only"},
        {"GET /url?key=val%C3%A1 HTTP/1.1", HttpVersion.HTTP_1_1, "1.1 + query key + value percent-encoded"},
        {"GET /url?key%C3%A1 HTTP/1.1", HttpVersion.HTTP_1_1, "1.1 + query key only percent-encoded"},
        {"GET /url?key= HTTP/1.1", HttpVersion.HTTP_1_1, "1.1 + query key only (equals)"},
        {"GET /url?key%C3%A1= HTTP/1.1", HttpVersion.HTTP_1_1, "1.1 + query key only percent-encoded (equals)"}
    };
  }

  @Test(dataProvider = "versionValidProvider")
  public void versionValid(String line, HttpVersion expected, String description) throws IOException {
    final HttpRequest req;
    req = HttpRequestTester.parse(
        test -> test.bufferSize(256, 512),

        """
        %s\r
        Host: test\r
        \r
        """.formatted(line)
    );

    assertEquals(req.version(), expected);
  }

  @DataProvider
  public Object[][] versionNotSupportedProvider() {
    return new Object[][] {
        {"GET / HTTP/1.0", "1.0 is not supported"},
        {"GET / HTTP/2", "2 is not supported (yet)"},
        {"GET / HTTP/9.9", "9.9 is not supported (yet)"},
        {"GET / HTTP/123456789012345678901234567890.123456789012345678901234567890", "Not supported"}
    };
  }

  @Test(dataProvider = "versionNotSupportedProvider")
  public void versionNotSupported(String line, String description) throws IOException {
    try {
      HttpRequestTester.parse(
          test -> test.bufferSize(256, 512),

          """
          %s\r
          Host: test\r
          \r
          """.formatted(line)
      );

      Assert.fail("It should have thrown");
    } catch (HttpClientException expected) {
      assertEquals(expected.kind, InvalidRequestLine.HTTP_VERSION_NOT_SUPPORTED);
    }
  }

}
