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
package objectox.http.req;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.Map;
import objectos.http.HeaderName;
import objectox.http.HttpClientException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RequestBodyMetaParserTest {

  private RequestBodyMeta parse(Map<HeaderName, Object> map) throws IOException {
    final RequestHeaders headers;
    headers = new RequestHeaders(map);

    final RequestBodyMetaParser parser;
    parser = new RequestBodyMetaParser(headers);

    return parser.parse();
  }

  @DataProvider
  public Object[][] validProvider() {
    return new Object[][] {
        {
            Map.of(),

            RequestBodyMeta.ofEmpty(),
            "empty: no content-length"
        },
        {
            Map.of(
                HeaderName.HOST, "www.example.com"
            ),

            RequestBodyMeta.ofEmpty(),
            "empty: no content-length"
        },
        {
            Map.of(
                HeaderName.HOST, "www.example.com",
                HeaderName.CONTENT_LENGTH, "0"
            ),

            RequestBodyMeta.ofEmpty(),
            "empty: content-length=0"
        },
        {
            Map.of(
                HeaderName.HOST, "www.example.com",
                HeaderName.CONTENT_LENGTH, "123"
            ),

            RequestBodyMeta.of(123, RequestBodyMeta.TypeKind.NONE),
            "fixed: no type"
        },
        {
            Map.of(
                HeaderName.HOST, "www.example.com",
                HeaderName.CONTENT_LENGTH, "9223372036854775807"
            ),

            RequestBodyMeta.of(Long.MAX_VALUE, RequestBodyMeta.TypeKind.NONE),
            "fixed: long max value"
        },
        {
            Map.of(
                HeaderName.HOST, "www.example.com",
                HeaderName.CONTENT_LENGTH, "47890",
                HeaderName.CONTENT_TYPE, "text/plain"
            ),

            RequestBodyMeta.of(47890, RequestBodyMeta.TypeKind.NONE),
            "fixed: with content-type but no parsing"
        },
        {
            Map.of(
                HeaderName.HOST, "www.example.com",
                HeaderName.CONTENT_LENGTH, "1209830",
                HeaderName.CONTENT_TYPE, "application/x-www-form-urlencoded"
            ),

            RequestBodyMeta.of(1209830, RequestBodyMeta.TypeKind.APPLICATION_FORM_URLENCODED),
            "fixed: form"
        },
        {
            Map.of(
                HeaderName.HOST, "www.example.com",
                HeaderName.CONTENT_LENGTH, "1209830",
                HeaderName.CONTENT_TYPE, "application/x-WWW-form-urlencoded"
            ),

            RequestBodyMeta.of(1209830, RequestBodyMeta.TypeKind.APPLICATION_FORM_URLENCODED),
            "fixed: form no standard but ok..."
        }
    };
  }

  @SuppressWarnings("exports")
  @Test(dataProvider = "validProvider")
  public void valid(Map<HeaderName, Object> map, RequestBodyMeta expected, String description) throws IOException {
    assertEquals(
        parse(map),

        expected
    );
  }

  @DataProvider
  public Object[][] invalidProvider() {
    return new Object[][] {
        {
            Map.of(
                HeaderName.CONTENT_LENGTH, "two hundred bytes",
                HeaderName.CONTENT_TYPE, "application/x-www-form-urlencoded"
            ),

            HttpClientException.Kind.INVALID_REQUEST_HEADERS,
            "Invalid Content-Length: char 't' is not a digit"
        },
        {
            Map.of(
                HeaderName.CONTENT_LENGTH, "234",
                HeaderName.CONTENT_TYPE, "application/x-www-form-urlencoded",
                HeaderName.TRANSFER_ENCODING, "chunked"
            ),

            HttpClientException.Kind.INVALID_REQUEST_HEADERS,
            "Content-Length and Transfer-Encoding in the same request message"
        },
        {
            Map.of(
                HeaderName.CONTENT_LENGTH, "9223372036854775808"
            ),

            HttpClientException.Kind.CONTENT_TOO_LARGE,
            "Invalid Content-Length: value is larger than Long.MAX_VALUE"
        },
        {
            Map.of(
                HeaderName.CONTENT_TYPE, "application/json"
            ),

            HttpClientException.Kind.LENGTH_REQUIRED,
            "Invalid request headers: expected Content-Length"
        }
    };
  }

  @SuppressWarnings("exports")
  @Test(dataProvider = "invalidProvider")
  public void invalid(Map<HeaderName, Object> map, HttpClientException.Kind kind, String msg) throws IOException {
    try {
      parse(map);

      Assert.fail("It should have thrown");
    } catch (HttpClientException expected) {
      assertEquals(expected.getMessage(), msg);

      assertEquals(expected.kind, kind);
    }
  }

}
