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

import static objectos.http.HttpY.arr;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import objectox.http.Rfc;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RouteParserTest {

  private final String validDelims = Rfc.pathDelim();

  @Test(description = "path expresions must not be empty")
  public void parse01() {
    final RouteParser subject;
    subject = new RouteParser("");

    try {
      subject.parse();

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "Invalid path expression: it must not be empty");
    }
  }

  @Test(description = "path expressions must start with '/'")
  public void parse02() {
    final RouteParser subject;
    subject = new RouteParser("index.html");

    try {
      subject.parse();

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "Invalid path expression: it must begin with the '/' character");
    }
  }

  @DataProvider
  public Iterator<String> parse03Provider() {
    return List.of("/", "/index.html", "/one", "/one/two").iterator();
  }

  @Test(dataProvider = "parse03Provider", description = "1 matcher = EXACT")
  public void parse03(String pathExpression) {
    final RouteParser subject;
    subject = new RouteParser(pathExpression);

    final RouteMatcher res;
    res = subject.parse();

    assertEquals(res, new RouteMatcherExact(pathExpression));
  }

  @DataProvider
  public Object[][] parse04Provider() {
    return new Object[][] {
        {"/{a}", "/", "a"},
        {"/{foo}", "/", "foo"},
        {"/foo/{foo}", "/foo/", "foo"},
        {"/foo/bar/{x}", "/foo/bar/", "x"}
    };
  }

  @Test(dataProvider = "parse04Provider", description = "REGION + PARAM_LAST")
  public void parse04(String pathExpression, String region, String param) {
    final RouteParser subject;
    subject = new RouteParser(pathExpression);

    final RouteMatcher res;
    res = subject.parse();

    assertEquals(
        res,

        new RouteMatcherList(List.of(
            new RouteMatcherRegion(region),
            new RouteMatcherParamLast(param)
        ))
    );
  }

  @DataProvider
  public Object[][] parse05Provider() {
    return new Object[][] {
        {"/{a}/pdf", "/", "a", '/', "pdf"},
        {"/{foo}.pdf", "/", "foo", '.', "pdf"},
        {"/foo/{foo}-bar", "/foo/", "foo", '-', "bar"},
        {"/foo/bar/{x}/baz", "/foo/bar/", "x", '/', "baz"},
        {"/prefix{suffix}.pdf", "/prefix", "suffix", '.', "pdf"},
    };
  }

  @Test(dataProvider = "parse05Provider", description = "REGION + PARAM + EXACT")
  public void parse05(String pathExpression, String region, String param, char delim, String exact) {
    final RouteParser subject;
    subject = new RouteParser(pathExpression);

    final RouteMatcher res;
    res = subject.parse();

    assertEquals(
        res,

        new RouteMatcherList(List.of(
            new RouteMatcherRegion(region),
            new RouteMatcherParam(param, delim),
            new RouteMatcherExact(exact)
        ))
    );
  }

  @DataProvider
  public Object[][] parse06Provider() {
    final List<Object[]> list;
    list = new ArrayList<>();

    final char[] delims;
    delims = validDelims.toCharArray();

    Arrays.sort(delims);

    for (char d = 0; d < 128; d++) {
      final int idx;
      idx = Arrays.binarySearch(delims, d);

      if (idx >= 0) {
        continue;
      }

      list.add(arr("/foo/{x}" + d + "bar", "Invalid path expression: path parameter can only be either at the end of the expression or immediately followed by one of " + validDelims));
    }

    return list.toArray(Object[][]::new);
  }

  @Test(dataProvider = "parse06Provider", description = "Invalid param trailing delimiter")
  public void parse06(String pathExpression, String expectedMessage) {
    try {
      final RouteParser parser;
      parser = new RouteParser(pathExpression);

      parser.parse();

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), expectedMessage);
    }
  }

}
