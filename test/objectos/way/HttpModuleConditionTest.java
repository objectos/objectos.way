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

import java.util.regex.Pattern;
import org.testng.annotations.Test;

public class HttpModuleConditionTest {

  @Test
  public void digits() {
    HttpModuleCondition condition;
    condition = new HttpModuleCondition.Digits("x");

    test(condition, "/test/1", true);
    test(condition, "/test/0123456789", true);
    test(condition, "/test/1x", false);
    test(condition, "/test/", false);
  }

  @Test
  public void notEmpty() {
    HttpModuleCondition condition;
    condition = new HttpModuleCondition.NotEmpty("x");

    test(condition, "/test/a", true);
    test(condition, "/test/abc", true);
    test(condition, "/test/", false);
  }

  @Test
  public void regex() {
    HttpModuleCondition condition;
    condition = new HttpModuleCondition.Regex("x", Pattern.compile("[0-9a-z]+"));

    test(condition, "/test/1", true);
    test(condition, "/test/0123456789", true);
    test(condition, "/test/1x", true);
    test(condition, "/test/", false);
  }

  private void test(HttpModuleCondition condition, String target, boolean expected) {
    HttpModuleSupport requestTarget;
    requestTarget = HttpExchange.parseRequestTarget(target);

    HttpModuleMatcher matcher;
    matcher = new HttpModuleMatcher.Matcher2(
        new HttpModuleMatcher.StartsWith("/test/"),
        new HttpModuleMatcher.NamedVariable("x")
    );

    assertEquals(matcher.test(requestTarget), true);

    assertEquals(condition.test(requestTarget), expected);
  }

}