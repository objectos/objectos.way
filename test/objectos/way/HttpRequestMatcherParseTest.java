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

import static objectos.way.HttpRequestMatcher.pathExact;
import static objectos.way.HttpRequestMatcher.pathSegments;
import static objectos.way.HttpRequestMatcher.pathWildcard;
import static objectos.way.HttpRequestMatcher.segmentExact;
import static objectos.way.HttpRequestMatcher.segmentParam;
import static objectos.way.HttpRequestMatcher.segmentParamLast;
import static objectos.way.HttpRequestMatcher.segmentRegion;
import static objectos.way.HttpRequestMatcher.subpathExact;
import static objectos.way.HttpRequestMatcher.subpathSegments;
import static objectos.way.HttpRequestMatcher.subpathWildcard;
import static org.testng.Assert.assertEquals;

import java.util.List;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpRequestMatcherParseTest {

  @DataProvider
  public Object[][] parsePathValidProvider() {
    return new Object[][] {
        {"/foo", pathExact("/foo")},
        {"/foo/bar", pathExact("/foo/bar")},
        {"/", pathExact("/")},

        {"/foo/:a",
            pathSegments(List.of(segmentRegion("/foo/"), segmentParamLast("a")))},
        {"/foo/:foo",
            pathSegments(List.of(segmentRegion("/foo/"), segmentParamLast("foo")))},
        {"/foo/:foo/",
            pathSegments(List.of(segmentRegion("/foo/"), segmentParam("foo", '/'), segmentExact("")))},
        {"/foo/:foo/bar/:bar",
            pathSegments(List.of(segmentRegion("/foo/"), segmentParam("foo", '/'), segmentRegion("bar/"), segmentParamLast("bar")))},
        {"/foo/:foo/bar",
            pathSegments(List.of(segmentRegion("/foo/"), segmentParam("foo", '/'), segmentExact("bar")))},

        {"/*", pathWildcard("/")},
        {"/foo*", pathWildcard("/foo")},
        {"/foo/*", pathWildcard("/foo/")}
    };
  }

  @Test(dataProvider = "parsePathValidProvider")
  public void parsePathValid(String path, Object expected) {
    final HttpRequestMatcher result;
    result = HttpRequestMatcher.parsePath(path);

    assertEquals(result, expected);
  }

  @DataProvider
  public Object[][] parsePathInvalidProvider() {
    return new Object[][] {
        {"foo", "Path must start with a '/' character: foo"},

        {"/foo/:error/bar/:error", "The ':error' path variable was declared more than once"},
        {"/foo/:bar:baz", "Cannot begin a path parameter immediately after the end of another parameter: /foo/:bar:baz"},
        {"/foo/:bar/*", "The '*' wildcard character cannot be used when path parameters are declared: /foo/:bar/*"},

        {"/foo/*/", "The '*' wildcard character can only be used once at the end of the path expression: /foo/*/"},
        {"/foo**", "The '*' wildcard character can only be used once at the end of the path expression: /foo**"}
    };
  }

  @Test(dataProvider = "parsePathInvalidProvider")
  public void parsePathInvalid(String path, String expectedMessage) {
    try {
      HttpRequestMatcher.parsePath(path);

      Assert.fail();
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), expectedMessage);
    }
  }

  @DataProvider
  public Object[][] parseSubpathValidProvider() {
    return new Object[][] {
        {"foo", subpathExact("foo")},
        {"foo/bar", subpathExact("foo/bar")},
        {"/", subpathExact("/")},

        {"foo/:a",
            subpathSegments(List.of(segmentRegion("foo/"), segmentParamLast("a")))},
        {"foo/:foo",
            subpathSegments(List.of(segmentRegion("foo/"), segmentParamLast("foo")))},
        {"foo/:foo/",
            subpathSegments(List.of(segmentRegion("foo/"), segmentParam("foo", '/'), segmentExact("")))},
        {"foo/:foo/bar/:bar",
            subpathSegments(List.of(segmentRegion("foo/"), segmentParam("foo", '/'), segmentRegion("bar/"), segmentParamLast("bar")))},
        {"foo/:foo/bar",
            subpathSegments(List.of(segmentRegion("foo/"), segmentParam("foo", '/'), segmentExact("bar")))},

        {"*", subpathWildcard("")},
        {"foo/*", subpathWildcard("foo/")}
    };
  }

  @Test(dataProvider = "parseSubpathValidProvider")
  public void parseSubpathValid(String path, Object expected) {
    HttpRequestMatcher matcher;
    matcher = HttpRequestMatcher.parseSubpath(path);

    assertEquals(matcher, expected);
  }

}
