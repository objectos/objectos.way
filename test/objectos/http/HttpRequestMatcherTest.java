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
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import org.testng.annotations.Test;

public class HttpRequestMatcherTest {

  @Test
  public void pathWildcard01() {
    HttpRequestMatcherX matcher;
    matcher = HttpRequestMatcherX.pathSegments(List.of(
        HttpRequestMatcherX.segmentRegion("/foo"),
        HttpRequestMatcherX.segmentWildcard()
    ));

    test(matcher, "/foo", true);
    test(matcher, "/fooo", true);
    test(matcher, "/foo?q=foo", true);
    test(matcher, "/foo/", true);
    test(matcher, "/foo/bar", true);
    test(matcher, "/bar", false);
    test(matcher, "/", false);
  }

  @Test
  public void pathWildcard02() {
    HttpRequestMatcherX matcher;
    matcher = HttpRequestMatcherX.pathSegments(List.of(
        HttpRequestMatcherX.segmentRegion("/prefix/"),
        HttpRequestMatcherX.segmentParam("foo", '/')
    ));

    test(matcher, "/prefix/foo/", true);
    test(matcher, "/prefix/foo/wildcard", true);
    test(matcher, "/prefix/", false);
  }

  private void test(HttpRequestMatcherX matcher, String target, boolean expected) {
    final HttpExchange requestTarget;
    requestTarget = HttpExchange.create(config -> {
      config.path(target);
    });

    assertEquals(matcher.test(requestTarget), expected);
  }

  private void test(HttpRequestMatcherX matcher, String target, Map<String, String> expected) {
    HttpExchange0 requestTarget;
    requestTarget = (HttpExchange0) HttpExchange.create(config -> {
      config.path(target);
    });

    assertTrue(matcher.test(requestTarget));

    assertEquals(requestTarget.pathParams(), expected);
  }

}