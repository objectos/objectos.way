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

public class HttpPathMatcherParseTest {

  @Test
  public void exact01() {
    matcher("/foo", HttpPathMatcher.exact("/foo"));
    matcher("/foo/bar", HttpPathMatcher.exact("/foo/bar"));
    matcher("/", HttpPathMatcher.exact("/"));
  }

  @Test
  public void exact02() {
    matcherError("foo", "Path must start with a '/' character: foo");
  }

  @Test
  public void exact03() {
    matcher("/prefix", "/foo", HttpPathMatcher.exact("/prefix/foo"));
    matcher("/prefix", "/foo/bar", HttpPathMatcher.exact("/prefix/foo/bar"));
    matcher("/prefix", "/", HttpPathMatcher.exact("/prefix/"));
  }

  @Test
  public void params01() {
    matcher(
        "/foo/:a",
        HttpPathMatcher.params(List.of(
            "/foo/",
            HttpPathMatcher.param("a")
        ))
    );
    matcher(
        "/foo/:foo",
        HttpPathMatcher.params(List.of(
            "/foo/",
            HttpPathMatcher.param("foo")
        ))
    );
    matcher(
        "/foo/:foo/bar/:bar",
        HttpPathMatcher.params(List.of(
            "/foo/",
            HttpPathMatcher.param("foo"),
            "/bar/",
            HttpPathMatcher.param("bar")
        ))
    );
    matcher(
        "/foo/:foo/bar",
        HttpPathMatcher.params(List.of(
            "/foo/",
            HttpPathMatcher.param("foo"),
            "/bar"
        ))
    );
  }

  @Test(description = """
  it should disallow duplicate path variable names
  """)
  public void params02() {
    matcherError("/foo/:error/bar/:error", "The ':error' path variable was declared more than once");
  }

  @Test
  public void params03() {
    matcherError("/foo/:bar:baz", "Cannot begin a path parameter immediately after the end of another parameter: /foo/:bar:baz");
  }

  @Test
  public void params04() {
    matcherError("/foo/:bar/*", "The '*' wildcard character cannot be used when path parameters are declared: /foo/:bar/*");
  }

  @Test
  public void params05() {
    matcher(
        "/prefix",
        "/:a",
        HttpPathMatcher.params(List.of(
            "/prefix/",
            HttpPathMatcher.param("a")
        ))
    );
    matcher(
        "/prefix",
        "/foo/:a",
        HttpPathMatcher.params(List.of(
            "/prefix/foo/",
            HttpPathMatcher.param("a")
        ))
    );
    matcher(
        "/prefix",
        "/foo/:foo",
        HttpPathMatcher.params(List.of(
            "/prefix/foo/",
            HttpPathMatcher.param("foo")
        ))
    );
    matcher(
        "/prefix",
        "/foo/:foo/bar/:bar",
        HttpPathMatcher.params(List.of(
            "/prefix/foo/",
            HttpPathMatcher.param("foo"),
            "/bar/",
            HttpPathMatcher.param("bar")
        ))
    );
    matcher(
        "/prefix",
        "/foo/:foo/bar",
        HttpPathMatcher.params(List.of(
            "/prefix/foo/",
            HttpPathMatcher.param("foo"),
            "/bar"
        ))
    );
  }

  @Test
  public void startsWith01() {
    matcher("/*", HttpPathMatcher.startsWith("/"));
    matcher("/foo*", HttpPathMatcher.startsWith("/foo"));
    matcher("/foo/*", HttpPathMatcher.startsWith("/foo/"));
  }

  @Test
  public void startsWith02() {
    matcherError("/foo/*/", "The '*' wildcard character can only be used once at the end of the path expression: /foo/*/");
    matcherError("/foo**", "The '*' wildcard character can only be used once at the end of the path expression: /foo**");
  }

  @Test
  public void startsWith03() {
    matcher("/prefix", "/*", HttpPathMatcher.startsWith("/prefix/"));
    matcher("/prefix", "/foo*", HttpPathMatcher.startsWith("/prefix/foo"));
    matcher("/prefix", "/foo/*", HttpPathMatcher.startsWith("/prefix/foo/"));
  }

  private void matcher(String path, HttpPathMatcher expected) {
    matcher(null, path, expected);
  }

  private void matcher(String prefix, String path, HttpPathMatcher expected) {
    HttpPathMatcher matcher;
    matcher = HttpPathMatcher.parse(prefix, path);

    assertEquals(matcher, expected);
  }

  private void matcherError(String path, String expectedMessage) {
    try {
      HttpPathMatcher.parse(null, path);

      Assert.fail();
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), expectedMessage);
    }
  }

}
