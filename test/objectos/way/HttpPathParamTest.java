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
import java.util.regex.Pattern;
import org.testng.annotations.Test;

public class HttpPathParamTest {

  @Test
  public void digits() {
    HttpPathParam condition;
    condition = new HttpPathParam.Digits("x");

    test(condition, "/test/1", true);
    test(condition, "/test/0123456789", true);
    test(condition, "/test/1x", false);
    test(condition, "/test/", false);
  }

  @Test
  public void notEmpty() {
    HttpPathParam condition;
    condition = new HttpPathParam.NotEmpty("x");

    test(condition, "/test/a", true);
    test(condition, "/test/abc", true);
    test(condition, "/test/", false);
  }

  @Test
  public void regex() {
    HttpPathParam condition;
    condition = new HttpPathParam.Regex("x", Pattern.compile("[0-9a-z]+"));

    test(condition, "/test/1", true);
    test(condition, "/test/0123456789", true);
    test(condition, "/test/1x", true);
    test(condition, "/test/", false);
  }

  private void test(HttpPathParam condition, String target, boolean expected) {
    HttpExchange requestTarget;
    requestTarget = HttpExchange.parseRequestTarget(target);

    HttpRequestMatcher matcher;
    matcher = HttpRequestMatcher.pathParams(List.of(
        "/test/",
        HttpRequestMatcher.param("x")
    ));

    assertEquals(matcher.test(requestTarget), true);

    assertEquals(condition.test(requestTarget), expected);
  }

}