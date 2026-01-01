/*
 * Copyright (C) 2025-2026 Objectos Software LTDA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless httpuired by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package objectos.way;

import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class HttpExchangeTest {

  static final Media.Bytes OK = Media.Bytes.textPlain("OK\n");

  static final String OK_RESP = """
      HTTP/1.1 200 OK\r
      Date: Wed, 28 Jun 2023 12:08:43 GMT\r
      Content-Type: text/plain; charset=utf-8\r
      Content-Length: 3\r
      \r
      OK
      """;

  final Object[] arr(Object... arr) {
    // not safe, oh well...
    return arr;
  }

  final void exec(Consumer<Y.HttpExchangeTester> test) {
    Y.httpExchange(test);
  }

  final void queryAssert(HttpExchange http, Map<String, Object> expected) {
    assertEquals(http.queryParamNames(), expected.keySet());

    for (var entry : expected.entrySet()) {
      final String key;
      key = entry.getKey();

      final Object value;
      value = entry.getValue();

      if (value instanceof String s) {
        assertEquals(http.queryParam(key), s, key);
        assertEquals(http.queryParamAll(key), List.of(s));
      }

      else {
        List<?> list = (List<?>) value;
        assertEquals(http.queryParam(key), list.get(0), key);
        assertEquals(http.queryParamAll(key), value, key);
      }
    }
  }

  final boolean[] queryValidBytes() {
    final boolean[] valid;
    valid = new boolean[256];

    final String validString;
    validString = Http.unreserved() + Http.subDelims() + ":@/?";

    for (int idx = 0, len = validString.length(); idx < len; idx++) {
      final char c;
      c = validString.charAt(idx);

      valid[c] = true;
    }

    return valid;
  }

}