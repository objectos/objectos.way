/*
 * Copyright (C) 2025-2026 Objectos Software LTDA.
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

import module java.base;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpRequestTest0ParseMethod {

  @DataProvider
  public Iterator<HttpMethod> methodProvider() {
    return Stream.of(HttpMethod.VALUES).iterator();
  }

  @Test(dataProvider = "methodProvider", description = "method: valid")
  public void method01(HttpMethod method) {
    if (!method.implemented) {
      return;
    }

    final HttpRequest req;
    req = HttpRequestTester.parse(
        test -> test.bufferSize(256, 512),

        """
        %s /index.html HTTP/1.1\r
        Host: www.example.com\r
        \r
        """.formatted(method.name())
    );

    assertEquals(req.method(), method);
  }

}