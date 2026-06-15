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
package objectox.http.handler;

import static org.testng.Assert.assertEquals;

import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RouteParserRightTest {

  @Test(description = "reject unclosed parameter")
  public void execute01() {
    final RouteParser ctx;
    ctx = new RouteParser("/{", 2);

    final RouteParserRight subject;
    subject = new RouteParserRight(ctx);

    try {
      subject.execute();

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "Invalid path expression: unclosed path parameter");
    }
  }

  @Test(description = "wildcard")
  public void execute02() {
    final RouteParser ctx;
    ctx = new RouteParser("/{}", 2);

    final RouteParserRight subject;
    subject = new RouteParserRight(ctx);

    subject.execute();

    assertEquals(ctx.index(), 3);
    assertEquals(ctx.segments(), List.of(RouteMatcherWildcard.INSTANCE));
    assertEquals(ctx.stop(), true);
  }

  @Test(description = "reject wildcard if not at the end")
  public void execute03() {
    final RouteParser ctx;
    ctx = new RouteParser("/{}.", 2);

    final RouteParserRight subject;
    subject = new RouteParserRight(ctx);

    try {
      subject.execute();

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "Invalid path expression: the '{}' wildcard path parameter can only be declared at the end of the expression");
    }
  }

  @Test(description = "reject param name: first char must be java identifier first")
  public void execute04() {
    final RouteParser ctx;
    ctx = new RouteParser("/{2fa}.", 2);

    final RouteParserRight subject;
    subject = new RouteParserRight(ctx);

    try {
      subject.execute();

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "Invalid path expression: path parameter name must be a valid Java identifier");
    }
  }

  @Test(description = "reject param name: remaining chars must be java identifier chars")
  public void execute05() {
    final RouteParser ctx;
    ctx = new RouteParser("/{c#}.", 2);

    final RouteParserRight subject;
    subject = new RouteParserRight(ctx);

    try {
      subject.execute();

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "Invalid path expression: path parameter name must be a valid Java identifier");
    }
  }

  @Test(description = "reject duplicate param name")
  public void execute06() {
    final RouteParser ctx;
    ctx = new RouteParser("/{x}.", 2);

    ctx.add("x");

    final RouteParserRight subject;
    subject = new RouteParserRight(ctx);

    try {
      subject.execute();

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "Invalid path expression: duplicate path parameter name 'x'");
    }
  }

  @Test(description = "param last")
  public void execute07() {
    final RouteParser ctx;
    ctx = new RouteParser("/{x}", 2);

    final RouteParserRight subject;
    subject = new RouteParserRight(ctx);

    subject.execute();

    assertEquals(ctx.index(), 4);
    assertEquals(ctx.segments(), List.of(new RouteMatcherParamLast("x")));
    assertEquals(ctx.stop(), true);
  }

  @Test(description = "reject param with an invalid trailing delimiter")
  public void execute08() {
    final RouteParser ctx;
    ctx = new RouteParser("/{x}invalid", 2);

    final RouteParserRight subject;
    subject = new RouteParserRight(ctx);

    try {
      subject.execute();

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "Invalid path expression: path parameter can only be either at the end of the expression or immediately followed by one of /-.");
    }
  }

  @Test(description = "param")
  public void execute09() {
    final RouteParser ctx;
    ctx = new RouteParser("/{x}/more", 2);

    final RouteParserRight subject;
    subject = new RouteParserRight(ctx);

    subject.execute();

    assertEquals(ctx.index(), 5);
    assertEquals(ctx.segments(), List.of(new RouteMatcherParam("x", '/')));
    assertEquals(ctx.stop(), false);
  }

}
