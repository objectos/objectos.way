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

import java.util.List;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RouteMatcherWildcardTest {

  @DataProvider
  public Object[][] testCase01Provider() {
    return new Object[][] {
        {"/foo", true},
        {"/fooo", true},
        {"/foo?q=foo", true},
        {"/foo/", true},
        {"/foo/bar", true},

        {"/bar", false},
        {"/", false}
    };
  }

  @Test(dataProvider = "testCase01Provider")
  public void testCase01(String path, boolean matched) {
    final RouteMatcher matcher;
    matcher = new RouteMatcherList(List.of(
        new RouteMatcherRegion("/foo"),
        RouteMatcherWildcard.INSTANCE
    ));

    final RoutePath http;
    http = new RoutePath(path);

    assertEquals(matcher.matches(http), matched);
  }

}