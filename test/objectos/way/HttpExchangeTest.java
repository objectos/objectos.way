/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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

import org.testng.annotations.Test;

public class HttpExchangeTest {

  @Test(description = """
  Sets:
  - request target
  - request attribute
  """)
  public void testCase01() {
    Http.Exchange http = Http.createExchange(
        Http.requestTarget("/foo?page=1"),

        Http.set(String.class, "Hello")
    );

    assertEquals(http.path().value(), "/foo");
    assertEquals(http.query().get("page"), "1");
    assertEquals(http.get(String.class), "Hello");
  }

}