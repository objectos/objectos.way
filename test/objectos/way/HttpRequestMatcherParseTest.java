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

import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HttpRequestMatcherParseTest {

  @Test
  public void pathExact01() {
    path("/foo", HttpRequestMatcher.pathExact("/foo"));
    path("/foo/bar", HttpRequestMatcher.pathExact("/foo/bar"));
    path("/", HttpRequestMatcher.pathExact("/"));
  }

  @Test
  public void pathExact02() {
    pathError("foo", "Path must start with a '/' character: foo");
  }

  @Test
  public void pathParams01() {
    path(
        "/foo/:a",
        HttpRequestMatcher.pathParams(List.of(
            "/foo/",
            HttpRequestMatcher.param("a")
        ))
    );
    path(
        "/foo/:foo",
        HttpRequestMatcher.pathParams(List.of(
            "/foo/",
            HttpRequestMatcher.param("foo")
        ))
    );
    path(
        "/foo/:foo/bar/:bar",
        HttpRequestMatcher.pathParams(List.of(
            "/foo/",
            HttpRequestMatcher.param("foo"),
            "/bar/",
            HttpRequestMatcher.param("bar")
        ))
    );
    path(
        "/foo/:foo/bar",
        HttpRequestMatcher.pathParams(List.of(
            "/foo/",
            HttpRequestMatcher.param("foo"),
            "/bar"
        ))
    );
  }

  @Test(description = """
  it should disallow duplicate path variable names
  """)
  public void pathParams02() {
    pathError("/foo/:error/bar/:error", "The ':error' path variable was declared more than once");
  }

  @Test
  public void pathParams03() {
    pathError("/foo/:bar:baz", "Cannot begin a path parameter immediately after the end of another parameter: /foo/:bar:baz");
  }

  @Test
  public void pathParams04() {
    pathError("/foo/:bar/*", "The '*' wildcard character cannot be used when path parameters are declared: /foo/:bar/*");
  }

  @Test
  public void pathStartsWith01() {
    path("/*", HttpRequestMatcher.pathStartsWith("/"));
    path("/foo*", HttpRequestMatcher.pathStartsWith("/foo"));
    path("/foo/*", HttpRequestMatcher.pathStartsWith("/foo/"));
  }

  @Test
  public void pathStartsWith02() {
    pathError("/foo/*/", "The '*' wildcard character can only be used once at the end of the path expression: /foo/*/");
    pathError("/foo**", "The '*' wildcard character can only be used once at the end of the path expression: /foo**");
  }

  private void path(String path, HttpRequestMatcher expected) {
    HttpRequestMatcher matcher;
    matcher = HttpRequestMatcher.parsePath(path);

    assertEquals(matcher, expected);
  }

  private void pathError(String path, String expectedMessage) {
    try {
      HttpRequestMatcher.parsePath(path);

      Assert.fail();
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), expectedMessage);
    }
  }

}
