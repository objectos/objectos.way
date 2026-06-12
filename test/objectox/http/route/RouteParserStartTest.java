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

import static org.testng.Assert.assertEquals;

import org.testng.Assert;
import org.testng.annotations.Test;

public class RouteParserStartTest {

  private RouteParserStart create(String pathExpression) {
    final RouteParser ctx;
    ctx = new RouteParser(pathExpression);

    return new RouteParserStart(ctx);
  }

  @Test(description = "path expresions must not be empty")
  public void testCase01() {
    final RouteParserStart subject;
    subject = create("");

    try {
      subject.parse();

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      final RouteParser ctx;
      ctx = subject.unwrap();

      assertEquals(ctx.state(), RouteParser.State.START);

      assertEquals(expected.getMessage(), "Invalid path expression: it must not be empty");
    }
  }

  @Test(description = "path expressions must start with '/'")
  public void testCase02() {
    final RouteParserStart subject;
    subject = create("index.html");

    try {
      subject.parse();

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      final RouteParser ctx;
      ctx = subject.unwrap();

      assertEquals(ctx.state(), RouteParser.State.START);

      assertEquals(expected.getMessage(), "Invalid path expression: it must begin with the '/' character");
    }
  }

  @Test(description = "Next state is EXACT")
  public void testCase03() {
    final RouteParserStart subject;
    subject = create("/index.html");

    subject.parse();

    final RouteParser ctx;
    ctx = subject.unwrap();

    assertEquals(ctx.state(), RouteParser.State.EXACT);
    assertEquals(ctx.startIndex(), 0);
    assertEquals(ctx.pathIndex(), 1);
  }

}
