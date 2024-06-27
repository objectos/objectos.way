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

import org.testng.annotations.Test;

public class HttpModuleCompilerTest {

  private final HttpModuleCompiler compiler = new HttpModuleCompiler();

  @Test
  public void matcherExact01() {
    matcher("/foo", new HttpModuleMatcher.Exact("/foo"));
  }

  @Test
  public void matcherExact02() {
    matcher("/foo/bar", new HttpModuleMatcher.Exact("/foo/bar"));
  }

  @Test
  public void matcherExact03() {
    matcher("/", new HttpModuleMatcher.Exact("/"));
  }

  @Test(
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Path does not start with a '/' character: .*"
  )
  public void matcherExact04() {
    matcher("foo ", null);
  }

  @Test
  public void wildcard01() {
    matcher("/*", new HttpModuleMatcher.StartsWith("/"));
  }

  @Test
  public void wildcard02() {
    matcher("/foo*", new HttpModuleMatcher.StartsWith("/foo"));
  }

  @Test
  public void wildcard03() {
    matcher("/foo/*", new HttpModuleMatcher.StartsWith("/foo/"));
  }

  @Test(
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "The '\\*' wildcard character can only be used once at the end of the path expression: /foo/\\*/"
  )
  public void wildcard04() {
    matcher("/foo/*/", null);
  }

  @Test(
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "The '\\*' wildcard character can only be used once at the end of the path expression: /foo\\*\\*"
  )
  public void wildcard05() {
    matcher("/foo**", new HttpModuleMatcher.Exact("/foo*bar"));
  }

  private void matcher(String path, HttpModuleMatcher expected) {
    HttpModuleMatcher matcher;
    matcher = compiler.matcher(path);

    assertEquals(matcher, expected);
  }

}
