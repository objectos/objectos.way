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
import java.util.List;
import java.util.Map;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpRequestTest6ParseAppForm {

  @DataProvider
  public Object[][] appFormValidProvider() {
    return HttpY.queryValidProvider();
  }

  @Test(dataProvider = "appFormValidProvider", enabled = false)
  public void appFormValid(String payload, Map<String, Object> expected, String description) throws IOException {
    final HttpRequest req;
    req = HttpRequestParserY.parse(
        test -> test.bufferSize(128, 256),

        """
        POST / HTTP/1.1\r
        Host: Host\r
        Content-Type: application/x-www-form-urlencoded\r
        Content-Length: %d\r
        \r
        %s\
        """.formatted(payload.length(), payload)
    );

    formAssert(req, expected);
  }

  private void formAssert(HttpRequest http, Map<String, Object> expected) {
    assertEquals(http.formParamNames(), expected.keySet());

    for (var entry : expected.entrySet()) {
      final String key;
      key = entry.getKey();

      final Object value;
      value = entry.getValue();

      if (value instanceof String s) {
        assertEquals(http.formParam(key), s, key);
        assertEquals(http.formParamAll(key), List.of(s));
      }

      else {
        List<?> list = (List<?>) value;
        assertEquals(http.formParam(key), list.get(0), key);
        assertEquals(http.formParamAll(key), value, key);
      }
    }
  }

}
