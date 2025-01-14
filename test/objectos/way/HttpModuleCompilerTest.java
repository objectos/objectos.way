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

import org.testng.Assert;
import org.testng.annotations.Test;

public class HttpModuleCompilerTest {

  private final HttpModuleCompiler compiler = new HttpModuleCompiler();

  @Test
  public void matcherExact() {
    matcher("/foo", new HttpModuleMatcher.Exact("/foo"));
    matcher("/foo/bar", new HttpModuleMatcher.Exact("/foo/bar"));
    matcher("/", new HttpModuleMatcher.Exact("/"));
  }

  @Test
  public void matcherExactErrors() {
    matcherError("foo", "Path does not start with a '/' character: foo");
  }

  @Test
  public void matcherVariable() {
    matcher(
        "/foo/:a",
        new HttpModuleMatcher.Matcher2(
            new HttpModuleMatcher.StartsWith("/foo/"),
            new HttpModuleMatcher.NamedVariable("a")
        )
    );
    matcher(
        "/foo/:foo",
        new HttpModuleMatcher.Matcher2(
            new HttpModuleMatcher.StartsWith("/foo/"),
            new HttpModuleMatcher.NamedVariable("foo")
        )
    );
    matcher(
        "/foo/:foo/bar/:bar",
        new HttpModuleMatcher.Matcher4(
            new HttpModuleMatcher.StartsWith("/foo/"),
            new HttpModuleMatcher.NamedVariable("foo"),
            new HttpModuleMatcher.Region("/bar/"),
            new HttpModuleMatcher.NamedVariable("bar")
        )
    );
    matcher(
        "/foo/:foo/bar",
        new HttpModuleMatcher.Matcher3(
            new HttpModuleMatcher.StartsWith("/foo/"),
            new HttpModuleMatcher.NamedVariable("foo"),
            new HttpModuleMatcher.Region("/bar")
        )
    );
  }

  @Test
  public void matcherWildcard() {
    matcher("/*", new HttpModuleMatcher.StartsWith("/"));
    matcher("/foo*", new HttpModuleMatcher.StartsWith("/foo"));
    matcher("/foo/*", new HttpModuleMatcher.StartsWith("/foo/"));
  }

  @Test
  public void matcherWildcardErrors() {
    matcherError("/foo/*/", "The '*' wildcard character can only be used once at the end of the path expression: /foo/*/");
    matcherError("/foo**", "The '*' wildcard character can only be used once at the end of the path expression: /foo**");
  }

  private void matcher(String path, HttpModuleMatcher expected) {
    HttpModuleMatcher matcher;
    matcher = compiler.matcher(path);

    assertEquals(matcher, expected);
  }

  private void matcherError(String path, String expectedMessage) {
    try {
      compiler.matcher(path);
      Assert.fail();
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), expectedMessage);
    }
  }

}
