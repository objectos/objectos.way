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

public class HttpModuleConditionTest {

  @Test
  public void digits() {
    HttpModuleCondition digits;
    digits = new HttpModuleCondition.Digits("x");

    test(digits, "/test/1", true);
    test(digits, "/test/012345678", true);
    test(digits, "/test/1x", false);
    test(digits, "/test/", false);
  }

  @Test
  public void notEmpty() {
    HttpModuleCondition notEmpty;
    notEmpty = new HttpModuleCondition.NotEmpty("x");

    test(notEmpty, "/test/a", true);
    test(notEmpty, "/test/abc", true);
    test(notEmpty, "/test/", false);
  }

  private void test(HttpModule.Condition condition, String target, boolean expected) {
    Http.Request.Target requestTarget;
    requestTarget = Http.parseRequestTarget(target);

    Http.Request.Target.Path p;
    p = requestTarget.path();

    HttpRequestTargetPath path;
    path = (HttpRequestTargetPath) p;

    HttpModuleMatcher matcher;
    matcher = new HttpModuleMatcher.Matcher2(
        new HttpModuleMatcher.StartsWith("/test/"),
        new HttpModuleMatcher.NamedVariable("x")
    );

    assertEquals(matcher.test(path), true);

    assertEquals(condition.test(path), expected);
  }

}