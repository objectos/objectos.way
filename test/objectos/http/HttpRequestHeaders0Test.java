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

import java.util.Map;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpRequestHeaders0Test {

  @DataProvider
  public Object[][] closeConnectionProvider() {
    return new Object[][] {
        {Map.of(), false},
        {Map.of(HttpHeaderName.HOST, "close"), false},
        {Map.of(HttpHeaderName.CONNECTION, "keep-alive"), false},
        {Map.of(HttpHeaderName.CONNECTION, "close"), true},
        {Map.of(HttpHeaderName.CONNECTION, "ClOsE"), true}
    };
  }

  @Test(dataProvider = "closeConnectionProvider")
  public void closeConnection(Map<HttpHeaderName, Object> map, boolean expected) {
    final HttpRequestHeaders0 headers;
    headers = new HttpRequestHeaders0(map);

    assertEquals(headers.closeConnection(), expected);
  }

}
