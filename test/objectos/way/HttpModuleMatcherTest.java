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
import static org.testng.Assert.assertTrue;

import java.util.Map;
import org.testng.annotations.Test;

public class HttpModuleMatcherTest {

  @Test
  public void exact01() {
    HttpModuleMatcher matcher;
    matcher = new HttpModuleMatcher.Exact("/foo");

    test(matcher, "/foo", true);
    test(matcher, "/fooo", false);
    test(matcher, "/foo?q=foo", true);
    test(matcher, "/foo/", false);
    test(matcher, "/foo/bar", false);
    test(matcher, "/bar", false);
    test(matcher, "/", false);
  }

  @Test
  public void namedVariable01() {
    HttpModuleMatcher matcher;
    matcher = new HttpModuleMatcher.Matcher2(
        new HttpModuleMatcher.StartsWith("/foo/"),
        new HttpModuleMatcher.NamedVariable("foo")
    );

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
  public void namedVariable02() {
    HttpModuleMatcher matcher;
    matcher = new HttpModuleMatcher.Matcher4(
        new HttpModuleMatcher.StartsWith("/foo/"),
        new HttpModuleMatcher.NamedVariable("foo"),
        new HttpModuleMatcher.Region("/bar/"),
        new HttpModuleMatcher.NamedVariable("bar")
    );

    test(matcher, "/foo/bar/bar/bar", Map.of("foo", "bar", "bar", "bar"));
    test(matcher, "/foo/x/bar/y", Map.of("foo", "x", "bar", "y"));

    test(matcher, "/foo", false);
    test(matcher, "/foo/", false);
    test(matcher, "/foo//", false);
    test(matcher, "/foo/foo/bar", false);
    test(matcher, "/foo/foo/bar/bar/x", false);
  }

  @Test
  public void startsWith01() {
    HttpModuleMatcher matcher;
    matcher = new HttpModuleMatcher.StartsWith("/foo");

    test(matcher, "/foo", true);
    test(matcher, "/fooo", true);
    test(matcher, "/foo?q=foo", true);
    test(matcher, "/foo/", true);
    test(matcher, "/foo/bar", true);
    test(matcher, "/bar", false);
    test(matcher, "/", false);
  }

  private void test(HttpModuleMatcher matcher, String target, boolean expected) {
    Http.Request.Target requestTarget;
    requestTarget = Http.parseRequestTarget(target);

    Http.Request.Target.Path p;
    p = requestTarget.path();

    HttpRequestTargetPath path;
    path = (HttpRequestTargetPath) p;

    assertEquals(matcher.test(path), expected);
  }

  private void test(HttpModuleMatcher matcher, String target, Map<String, String> expected) {
    Http.Request.Target requestTarget;
    requestTarget = Http.parseRequestTarget(target);

    Http.Request.Target.Path p;
    p = requestTarget.path();

    HttpRequestTargetPath path;
    path = (HttpRequestTargetPath) p;

    assertTrue(matcher.test(path));

    assertEquals(path.variables, expected);
  }

}