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

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RouteMatcherRegionTest {

  private final RouteMatcher matcher = new RouteMatcherRegion("/foo");

  private boolean matches(String path, int index) {
    final RoutePath http;
    http = new RoutePath(path, index);

    return matcher.matches(http);
  }

  @DataProvider
  public Object[][] validProvider() {
    return new Object[][] {
        {"/foo", 0},
        {"/fooo", 0},
        {"/bar/foo", 4},
        {"/bar/foo/", 4}
    };
  }

  @Test(dataProvider = "validProvider")
  public void valid(String path, int index) {
    assertTrue(matches(path, index));
  }

  @DataProvider
  public Object[][] invalidProvider() {
    return new Object[][] {
        {"/foo", 1},
        {"/bar/foo", 3},
        {"/bar/foo/", 5}
    };
  }

  @Test(dataProvider = "invalidProvider")
  public void invalid(String path, int index) {
    assertFalse(matches(path, index));
  }

}
