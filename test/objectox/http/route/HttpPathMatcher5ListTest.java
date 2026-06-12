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
import java.util.Map;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpPathMatcher5ListTest {

  @DataProvider
  public Object[][] testCase01Provider() {
    return new Object[][] {
        {"/foo/", true, Map.of("foo", "")},
        {"/foo/bar", true, Map.of("foo", "bar")},
        {"/foo/bar/x", true, Map.of("foo", "bar/x")},

        {"/foo", false, Map.of()},
        {"/fooo", false, Map.of()},
        {"/foo?q=foo", false, Map.of()},
        {"/bar", false, Map.of()},
        {"/", false, Map.of()}
    };
  }

  @Test(dataProvider = "testCase01Provider")
  public void testCase01(String path, boolean matched, Map<String, String> params) {
    final RouteMatcher matcher;
    matcher = new RouteMatcherList(List.of(
        new RouteMatcherRegion("/foo/"),
        new RouteMatcherParamLast("foo")
    ));

    final RoutePath http;
    http = new RoutePath(path);

    assertEquals(matcher.matches(http), matched);

    assertEquals(http.params, params);
  }

  @DataProvider
  public Object[][] testCase02Provider() {
    return new Object[][] {
        {"/foo/bar/bar/bar", true, Map.of("foo", "bar", "bar", "bar")},
        {"/foo/x/bar/y", true, Map.of("foo", "x", "bar", "y")},
        {"/foo/foo/bar/bar/x", true, Map.of("foo", "foo", "bar", "bar/x")},

        {"/foo", false, Map.of()},
        {"/foo/", false, Map.of()},
        {"/foo//", false, Map.of()},
        {"/foo/foo/bar", false, Map.of()}
    };
  }

  @Test(dataProvider = "testCase02Provider")
  public void testCase02(String path, boolean matched, Map<String, String> params) {
    final RouteMatcher matcher;
    matcher = new RouteMatcherList(List.of(
        new RouteMatcherRegion("/foo/"),
        new RouteMatcherParam("foo", '/'),
        new RouteMatcherRegion("bar/"),
        new RouteMatcherParamLast("bar")
    ));

    final RoutePath http;
    http = new RoutePath(path);

    assertEquals(matcher.matches(http), matched);

    assertEquals(http.params, params);
  }

  @DataProvider
  public Object[][] testCase03Provider() {
    return new Object[][] {
        {"/foo/x/bar/123/pdf", true, Map.of("foo", "x", "bar", "123")},
        {"/foo//bar//pdf", true, Map.of("foo", "", "bar", "")},

        {"/foo/x/bar/y/pdf/more", false, Map.of()}
    };
  }

  @Test(dataProvider = "testCase03Provider")
  public void testCase03(String path, boolean matched, Map<String, String> params) {
    final RouteMatcher matcher;
    matcher = new RouteMatcherList(List.of(
        new RouteMatcherRegion("/foo/"),
        new RouteMatcherParam("foo", '/'),
        new RouteMatcherRegion("bar/"),
        new RouteMatcherParam("bar", '/'),
        new RouteMatcherExact("pdf")
    ));

    final RoutePath http;
    http = new RoutePath(path);

    assertEquals(matcher.matches(http), matched);

    assertEquals(http.params, params);
  }

}
