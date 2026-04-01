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
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpRequestParser7BodyMetaTest {

  private HttpRequestBodyMeta parse(Map<HttpHeaderName, Object> map) throws IOException {
    final HttpRequestHeadersImpl headers;
    headers = new HttpRequestHeadersImpl(map);

    final HttpRequestParser7BodyMeta parser;
    parser = new HttpRequestParser7BodyMeta(headers);

    return parser.parse();
  }

  @DataProvider
  public Object[][] validProvider() {
    return new Object[][] {
        {
            Map.of(),

            HttpRequestBodyMeta.ofEmpty(),
            "empty: no content-length"
        },
        {
            Map.of(
                HttpHeaderName.HOST, "www.example.com"
            ),

            HttpRequestBodyMeta.ofEmpty(),
            "empty: no content-length"
        },
        {
            Map.of(
                HttpHeaderName.HOST, "www.example.com",
                HttpHeaderName.CONTENT_LENGTH, "0"
            ),

            HttpRequestBodyMeta.ofEmpty(),
            "empty: content-length=0"
        },
        {
            Map.of(
                HttpHeaderName.HOST, "www.example.com",
                HttpHeaderName.CONTENT_LENGTH, "123"
            ),

            HttpRequestBodyMeta.of(123, HttpRequestBodyMeta.TypeKind.NONE),
            "fixed: no type"
        },
        {
            Map.of(
                HttpHeaderName.HOST, "www.example.com",
                HttpHeaderName.CONTENT_LENGTH, "9223372036854775807"
            ),

            HttpRequestBodyMeta.of(Long.MAX_VALUE, HttpRequestBodyMeta.TypeKind.NONE),
            "fixed: long max value"
        },
        {
            Map.of(
                HttpHeaderName.HOST, "www.example.com",
                HttpHeaderName.CONTENT_LENGTH, "47890",
                HttpHeaderName.CONTENT_TYPE, "text/plain"
            ),

            HttpRequestBodyMeta.of(47890, HttpRequestBodyMeta.TypeKind.NONE),
            "fixed: with content-type but no parsing"
        },
        {
            Map.of(
                HttpHeaderName.HOST, "www.example.com",
                HttpHeaderName.CONTENT_LENGTH, "1209830",
                HttpHeaderName.CONTENT_TYPE, "application/x-www-form-urlencoded"
            ),

            HttpRequestBodyMeta.of(1209830, HttpRequestBodyMeta.TypeKind.APPLICATION_FORM_URLENCODED),
            "fixed: form"
        },
        {
            Map.of(
                HttpHeaderName.HOST, "www.example.com",
                HttpHeaderName.CONTENT_LENGTH, "1209830",
                HttpHeaderName.CONTENT_TYPE, "application/x-WWW-form-urlencoded"
            ),

            HttpRequestBodyMeta.of(1209830, HttpRequestBodyMeta.TypeKind.APPLICATION_FORM_URLENCODED),
            "fixed: form no standard but ok..."
        }
    };
  }

  @SuppressWarnings("exports")
  @Test(dataProvider = "validProvider")
  public void valid(Map<HttpHeaderName, Object> map, HttpRequestBodyMeta expected, String description) throws IOException {
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
                HttpHeaderName.CONTENT_LENGTH, "two hundred bytes",
                HttpHeaderName.CONTENT_TYPE, "application/x-www-form-urlencoded"
            ),

            HttpRequestParser7BodyMeta.Invalid.INVALID_CONTENT_LENGTH,
            "non-numeric content-length"
        },
        {
            Map.of(
                HttpHeaderName.CONTENT_LENGTH, "234",
                HttpHeaderName.CONTENT_TYPE, "application/x-www-form-urlencoded",
                HttpHeaderName.TRANSFER_ENCODING, "chunked"
            ),

            HttpRequestParser7BodyMeta.Invalid.BOTH_CL_TE,
            "content-length + transfer-encoding"
        },
        {
            Map.of(
                HttpHeaderName.CONTENT_LENGTH, "9223372036854775808"
            ),

            HttpRequestParser7BodyMeta.Invalid.CONTENT_TOO_LARGE,
            "content-length = Long.MAX_VALUE + 1"
        },
        {
            Map.of(
                HttpHeaderName.CONTENT_TYPE, "application/json"
            ),

            HttpRequestParser7BodyMeta.Invalid.LENGTH_REQUIRED,
            "content-type with no content-length"
        }
    };
  }

  @SuppressWarnings("exports")
  @Test(dataProvider = "invalidProvider")
  public void invalid(Map<HttpHeaderName, Object> map, HttpClientException.Kind kind, String description) throws IOException {
    try {
      parse(map);

      Assert.fail("It should have thrown");
    } catch (HttpClientException expected) {
      assertEquals(expected.kind, kind);
    }
  }

}
