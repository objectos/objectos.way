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
package objectox.http.host;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HostNameTest {

  @DataProvider
  public Object[][] testProvider() {
    return new Object[][] {
        {null, 80, "localhost"},
        {"localhost", 80, "localhost"},
        {null, 8080, "localhost:8080"},
        {"example.com", 80, "example.com"},
        {"example.com", 5555, "example.com:5555"}
    };
  }

  @Test(dataProvider = "testProvider")
  public void test(String name, int port, String expected) {
    final HostName hostName;
    hostName = new HostName(name, port);

    final String result;
    result = hostName.get();

    assertEquals(result, expected);
  }

}
