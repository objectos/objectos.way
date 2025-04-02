/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import org.testng.annotations.Test;

public class HttpRequestMatcherTest {

  @Test
  public void exact01() {
    HttpRequestMatcher matcher;
    matcher = HttpRequestMatcher.pathExact("/foo");

    test(matcher, "/foo", true);
    test(matcher, "/fooo", false);
    test(matcher, "/foo?q=foo", true);
    test(matcher, "/foo/", false);
    test(matcher, "/foo/bar", false);
    test(matcher, "/bar", false);
    test(matcher, "/", false);
  }

  @Test
  public void namedVariable02() {
    HttpRequestMatcher matcher;
    matcher = HttpRequestMatcher.pathParams(List.of(
        "/foo/",
        HttpRequestMatcher.param("foo")
    ));

    test(matcher, "/foo", false);
    test(matcher, "/fooo", false);
    test(matcher, "/foo?q=foo", false);
    test(matcher, "/foo/", Map.of("foo", ""));
    test(matcher, "/foo/bar", Map.of("foo", "bar"));
    test(matcher, "/foo/bar/x", false);
    test(matcher, "/bar", false);
    test(matcher, "/", false);
  }

  @Test
  public void namedVariable03() {
    HttpRequestMatcher matcher;
    matcher = HttpRequestMatcher.pathParams(List.of(
        "/foo/",
        HttpRequestMatcher.param("foo"),
        "/pdf"
    ));

    test(matcher, "/foo/bar/pdf", Map.of("foo", "bar"));
    test(matcher, "/foo//pdf", Map.of("foo", ""));

    test(matcher, "/foo", false);
    test(matcher, "/fooo", false);
    test(matcher, "/foo?q=foo", false);
    test(matcher, "/foo/", false);
    test(matcher, "/foo/bar/pdf/more", false);
    test(matcher, "/foo/bar/x", false);
    test(matcher, "/bar", false);
    test(matcher, "/", false);
  }

  @Test
  public void namedVariable04() {
    HttpRequestMatcher matcher;
    matcher = HttpRequestMatcher.pathParams(List.of(
        "/foo/",
        HttpRequestMatcher.param("foo"),
        "/bar/",
        HttpRequestMatcher.param("bar")
    ));

    test(matcher, "/foo/bar/bar/bar", Map.of("foo", "bar", "bar", "bar"));
    test(matcher, "/foo/x/bar/y", Map.of("foo", "x", "bar", "y"));

    test(matcher, "/foo", false);
    test(matcher, "/foo/", false);
    test(matcher, "/foo//", false);
    test(matcher, "/foo/foo/bar", false);
    test(matcher, "/foo/foo/bar/bar/x", false);
  }

  @Test
  public void namedVariable05() {
    HttpRequestMatcher matcher;
    matcher = HttpRequestMatcher.pathParams(List.of(
        "/foo/",
        HttpRequestMatcher.param("foo"),
        "/bar/",
        HttpRequestMatcher.param("bar"),
        "/pdf"
    ));

    test(matcher, "/foo/x/bar/123/pdf", Map.of("foo", "x", "bar", "123"));
    test(matcher, "/foo//bar//pdf", Map.of("foo", "", "bar", ""));

    test(matcher, "/foo/x/bar/y/pdf/more", false);
  }

  @Test
  public void namedVariableN() {
    HttpRequestMatcher matcher;
    matcher = HttpRequestMatcher.pathParams(List.of(
        "/foo/",
        HttpRequestMatcher.param("foo"),
        "/bar/",
        HttpRequestMatcher.param("bar"),
        "/pdf"
    ));

    test(matcher, "/foo/x/bar/123/pdf", Map.of("foo", "x", "bar", "123"));
    test(matcher, "/foo//bar//pdf", Map.of("foo", "", "bar", ""));

    test(matcher, "/foo/x/bar/y/pdf/more", false);
  }

  @Test
  public void startsWith01() {
    HttpRequestMatcher matcher;
    matcher = HttpRequestMatcher.pathStartsWith("/foo");

    test(matcher, "/foo", true);
    test(matcher, "/fooo", true);
    test(matcher, "/foo?q=foo", true);
    test(matcher, "/foo/", true);
    test(matcher, "/foo/bar", true);
    test(matcher, "/bar", false);
    test(matcher, "/", false);
  }

  private void test(HttpRequestMatcher matcher, String target, boolean expected) {
    HttpExchange requestTarget;
    requestTarget = HttpExchange.parseRequestTarget(target);

    assertEquals(matcher.test(requestTarget), expected);
  }

  private void test(HttpRequestMatcher matcher, String target, Map<String, String> expected) {
    HttpExchange requestTarget;
    requestTarget = HttpExchange.parseRequestTarget(target);

    assertTrue(matcher.test(requestTarget));

    assertEquals(requestTarget.pathParams, expected);
  }

}