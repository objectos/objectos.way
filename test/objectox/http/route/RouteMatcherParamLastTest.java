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
package objectox.http.route;

import static org.testng.Assert.assertEquals;

import java.util.Map;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RouteMatcherParamLastTest {

  private final RouteMatcher matcher = new RouteMatcherParamLast("test");

  @DataProvider
  public Object[][] validProvider() {
    return new Object[][] {
        {"/foo/", 1, "foo/"},
        {"/bar/foo/", 5, "foo/"},
        {"/bar/foo/more", 5, "foo/more"},
        {"/bar//", 5, "/"}
    };
  }

  @Test(dataProvider = "validProvider")
  public void valid(String path, int index, String value) {
    final RoutePath http;
    http = new RoutePath(path, index);

    assertEquals(matcher.matches(http), true);

    assertEquals(http.params, Map.of("test", value));
  }

}
