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

import java.util.List;
import objectos.way.Note;
import objectos.way.Y;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpRequestMatcherParserTest {

  @DataProvider
  public Object[][] parsePathValidProvider() {
    return new Object[][] {
        {"/foo", pathExact("/foo")},
        {"/foo/bar", pathExact("/foo/bar")},
        {"/", pathExact("/")},

        {"/foo/{a}",
            pathSegments(pathRegion("/foo/"), pathParamLast("a"))},
        {"/foo/{foo}",
            pathSegments(pathRegion("/foo/"), pathParamLast("foo"))},
        {"/foo/{foo}/",
            pathSegments(pathRegion("/foo/"), pathParam("foo", '/'), pathExact(""))},
        {"/foo/{foo}/bar/{bar}",
            pathSegments(pathRegion("/foo/"), pathParam("foo", '/'), pathRegion("bar/"), pathParamLast("bar"))},
        {"/foo/{foo}/bar",
            pathSegments(pathRegion("/foo/"), pathParam("foo", '/'), pathExact("bar"))},

        {"/{}", pathSegments(pathRegion("/"), segmentWildcard())},
        {"/foo{}", pathSegments(pathRegion("/foo"), segmentWildcard())},
        {"/foo/{}", pathSegments(pathRegion("/foo/"), segmentWildcard())},

        {"/foo/{x}/{}", pathSegments(pathRegion("/foo/"), pathParam("x", '/'), segmentWildcard())},
        {"/foo/{x}/bar/{}", pathSegments(pathRegion("/foo/"), pathParam("x", '/'), pathRegion("bar/"), segmentWildcard())}
    };
  }

  @Test(dataProvider = "parsePathValidProvider")
  public void parsePathValid(String path, Object expected) {
    final HttpRequestMatcherParser parser;
    parser = new HttpRequestMatcherParser(path);

    final HttpRequestMatcher result;
    result = parser.parse();

    assertEquals(result, expected);
  }

  @DataProvider
  public Object[][] parsePathInvalidProvider() {
    return new Object[][] {
        {"", "Route path must start with a '/' character: "},
        {"foo", "Route path must start with a '/' character: foo"},

        {"/foo/{error}/bar/{error}", "The '{error}' path variable was declared more than once"},
        {"/foo/{bar}{baz}", "Route path parameter must not begin immediately after the end of another parameter: /foo/{bar}{baz}"},
        {"/foo/{", "Route path with an unclosed path parameter definition: /foo/{"},
        {"/foo/{bar", "Route path with an unclosed path parameter definition: /foo/{bar"},
        {"/foo/{f{o}", "Route path parameter names must not contain the '{' character: /foo/{f{o}"},

        {"/foo/{}/", "Route path can only declare the '{}' wildcard path parameter once and at the end of the path: /foo/{}/"},
        {"/foo{}{}", "Route path can only declare the '{}' wildcard path parameter once and at the end of the path: /foo{}{}"}
    };
  }

  @Test(dataProvider = "parsePathInvalidProvider")
  public void parsePathInvalid(String path, String expectedMessage) {
    try {
      final HttpRequestMatcherParser parser;
      parser = new HttpRequestMatcherParser(path);

      parser.parse();

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), expectedMessage);

      IllegalArgumentException trimmed;
      trimmed = Y.trimStackTrace(expected, 1);

      Note.Ref1<IllegalArgumentException> note;
      note = Note.Ref1.create(getClass(), "IAE", Note.INFO);

      Y.noteSink().send(note, trimmed);
    }
  }

  private HttpRequestMatcher pathExact(String value) {
    return new HttpRequestMatcher2PathExact(value);
  }

  private HttpRequestMatcher pathRegion(String value) {
    return new HttpRequestMatcher3PathRegion(value);
  }

  private HttpRequestMatcher pathParam(String paramName, char terminator) {
    return new HttpRequestMatcher4PathParam(paramName, terminator);
  }

  private HttpRequestMatcher pathParamLast(String paramName) {
    return new HttpRequestMatcher5PathParamLast(paramName);
  }

  private HttpRequestMatcher segmentWildcard() {
    return HttpRequestMatcher6Wildcard.INSTANCE;
  }

  private HttpRequestMatcher pathSegments(HttpRequestMatcher... parts) {
    final List<HttpRequestMatcher> list;
    list = List.of(parts);

    return new HttpRequestMatcher7List(list);
  }

}
