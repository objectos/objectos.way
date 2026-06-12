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

import java.util.Iterator;
import java.util.List;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpPathMatcher0ExactTest {

  private final RouteMatcher matcher = new RouteMatcherExact("/foo");

  private boolean match(String path) {
    final RoutePath pojo;
    pojo = new RoutePath(path);

    return matcher.matches(pojo);
  }

  @Test
  public void valid() {
    assertTrue(match("/foo"));
  }

  @DataProvider
  public Iterator<String> invalidProvider() {
    return List.of("/fooo", "/foo/", "/foo/bar", "/bar", "/").iterator();
  }

  @Test(dataProvider = "invalidProvider")
  public void invalid(String path) {
    assertFalse(match(path));
  }

}
