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

import static objectos.http.HttpY.arr;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import objectos.lang.Throwables;
import objectos.way.Note;
import objectos.way.Y;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpPathMatcherParserTest {

  private final String validDelims = Http.pathDelim();

  @DataProvider
  public Object[][] parsePathValidProvider() {
    final List<Object[]> list;
    list = new ArrayList<>();

    list.add(arr("/foo", pathExact("/foo")));
    list.add(arr("/foo/bar", pathExact("/foo/bar")));
    list.add(arr("/", pathExact("/")));

    list.add(arr("/foo/{a}",
        pathList(pathRegion("/foo/"), pathParamLast("a"))));
    list.add(arr("/foo/{foo}",
        pathList(pathRegion("/foo/"), pathParamLast("foo"))));
    list.add(arr("/foo/{foo}/",
        pathList(pathRegion("/foo/"), pathParam("foo", '/'), pathExact(""))));
    list.add(arr("/foo/{foo}/bar/{bar}",
        pathList(pathRegion("/foo/"), pathParam("foo", '/'), pathRegion("bar/"), pathParamLast("bar"))));
    list.add(arr("/foo/{foo}/bar",
        pathList(pathRegion("/foo/"), pathParam("foo", '/'), pathExact("bar"))));

    list.add(arr("/{}", pathList(pathRegion("/"), wildcard())));
    list.add(arr("/foo/{}", pathList(pathRegion("/foo/"), wildcard())));

    list.add(arr("/foo/{x}/{}", pathList(pathRegion("/foo/"), pathParam("x", '/'), wildcard())));
    list.add(arr("/foo/{x}/bar/{}", pathList(pathRegion("/foo/"), pathParam("x", '/'), pathRegion("bar/"), wildcard())));

    // all valid param delimiters
    for (char d : validDelims.toCharArray()) {
      list.add(arr("/foo" + d + "{x}", pathList(pathRegion("/foo" + d), pathParamLast("x"))));

      list.add(arr("/foo/{x}" + d + "bar", pathList(pathRegion("/foo/"), pathParam("x", d), pathExact("bar"))));
    }

    return list.toArray(Object[][]::new);
  }

  @Test(dataProvider = "parsePathValidProvider")
  public void parsePathValid(String path, Object expected) {
    final HttpPathMatcherParser parser;
    parser = new HttpPathMatcherParser(path);

    final HttpPathMatcher result;
    result = parser.parse();

    assertEquals(result, expected);
  }

  @DataProvider
  public Object[][] parsePathInvalidProvider() {
    final List<Object[]> list;
    list = new ArrayList<>();

    list.add(arr("", "Invalid path expression: it must not be empty"));
    list.add(arr("foo", "Invalid path expression: it must begin with the '/' character"));

    list.add(arr("/foo/{error}/bar/{error}", "Invalid path expression: duplicate path parameter name 'error'"));
    list.add(arr("/foo/{bar}{baz}", "Invalid path expression: path parameter can only be either at the end of the expression or immediately followed by one of " + validDelims));
    list.add(arr("/foo/{", "Invalid path expression: unclosed path parameter"));
    list.add(arr("/foo/{bar", "Invalid path expression: unclosed path parameter"));
    list.add(arr("/foo/{f{o}", "Invalid path expression: path parameter name must be a valid Java identifier"));

    list.add(arr("/foo/{}/", "Invalid path expression: the '{}' wildcard path parameter can only be declared at the end of the expression"));
    list.add(arr("/foo{}{}", "Invalid path expression: path parameter can only be immediately preceeded by one of " + validDelims));

    final char[] delims;
    delims = validDelims.toCharArray();

    Arrays.sort(delims);

    for (char d = 0; d < 128; d++) {
      final int idx;
      idx = Arrays.binarySearch(delims, d);

      if (idx >= 0) {
        continue;
      }

      list.add(arr("/foo" + d + "{x}", "Invalid path expression: path parameter can only be immediately preceeded by one of " + validDelims));

      list.add(arr("/foo/{x}" + d + "bar", "Invalid path expression: path parameter can only be either at the end of the expression or immediately followed by one of " + validDelims));
    }

    return list.toArray(Object[][]::new);
  }

  @Test(dataProvider = "parsePathInvalidProvider")
  public void parsePathInvalid(String path, String expectedMessage) {
    try {
      final HttpPathMatcherParser parser;
      parser = new HttpPathMatcherParser(path);

      parser.parse();

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), expectedMessage);

      IllegalArgumentException trimmed;
      trimmed = Throwables.trimStackTrace(expected, 1);

      Note.Ref1<IllegalArgumentException> note;
      note = Note.Ref1.create(getClass(), "IAE", Note.INFO);

      Y.noteSink().send(note, trimmed);
    }
  }

  private HttpPathMatcher pathExact(String value) {
    return new HttpPathMatcher0Exact(value);
  }

  private HttpPathMatcher pathRegion(String value) {
    return new HttpPathMatcher1Region(value);
  }

  private HttpPathMatcher pathParam(String paramName, char terminator) {
    return new HttpPathMatcher2Param(paramName, terminator);
  }

  private HttpPathMatcher pathParamLast(String paramName) {
    return new HttpPathMatcher3ParamLast(paramName);
  }

  private HttpPathMatcher wildcard() {
    return HttpPathMatcher4Wildcard.INSTANCE;
  }

  private HttpPathMatcher pathList(HttpPathMatcher... parts) {
    final List<HttpPathMatcher> list;
    list = List.of(parts);

    return new HttpPathMatcher5List(list);
  }

}
