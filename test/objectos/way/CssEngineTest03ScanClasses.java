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

import java.io.IOException;
import java.util.Set;
import org.testng.annotations.Test;

public class CssEngineTest03ScanClasses {

  @Test(description = "single class name")
  public void testCase01() {
    class Subject extends Html.Template {
      @Override
      protected final void render() {
        div(className("margin:0"));
      }
    }

    test(Subject.class, "margin:0");
  }

  @Test(description = "many class names")
  public void testCase02() {
    class Subject extends Html.Template {
      @Override
      protected final void render() {
        div(
            className("margin:0"),
            className("display:block"),
            className("line-height:1")
        );
      }
    }

    test(Subject.class, "margin:0", "display:block", "line-height:1");
  }

  @Test(description = "long literal")
  public void testCase03() {
    class Subject extends Html.Template {
      @Override
      protected final void render() {
        div(
            className("margin:0"),
            className("display:block"),
            className("line-height:1")
        );
        foo(123L);
      }

      private void foo(long l) {}
    }

    test(Subject.class, "margin:0", "display:block", "line-height:1");
  }

  @Test(description = "non-ascii modified utf-8")
  public void testCase04() {
    class Subject extends Html.Template {
      @Override
      protected final void render() {
        div(
            // single byte (last one two bytes to trigger parsing)
            className("x:\u0001\u0011\u0021\u0031\u0041\u0051\u0061\u007F\u0000"),
            // two bytes
            className("x:\u0000"),
            className("x:\u0080\u0081\u07FE\u07FF"),
            // three bytes
            className("x:\u0800\u0801\uFFFE\uFFFF"),
            // random
            className("x:Olá"),
            className("x:こんにちは世界")
        );
      }
    }

    test(
        Subject.class,

        "x:\u0001\u0011\u0021\u0031\u0041\u0051\u0061\u007F\u0000",
        "x:\u0000",
        "x:\u0080\u0081\u07FE\u07FF",
        "x:\u0800\u0801\uFFFE\uFFFF",
        "x:Olá",
        "x:こんにちは世界"
    );
  }

  @Test(description = "whitespace")
  public void testCase05() {
    class Subject extends Html.Template {
      @Override
      protected final void render() {
        div(css("margin: \t display:\t\f"));
      }
    }

    test(
        Subject.class,

        "margin:",
        "display:"
    );
  }

  private void test(Class<?> type, String... expected) {
    try {
      CssEngine engine;
      engine = CssEngine.create(config -> {
        config.noteSink(Y.noteSink());

        config.scanClass(type);
      });

      engine.state(CssEngine.$SCAN);

      while (engine.shouldExecute(CssEngine.$SCAN_DIRECTORY)) {
        engine.executeOne();
      }

      assertEquals(engine.tokens(), Set.of(expected));
    } catch (IOException e) {
      throw new AssertionError("Should not have thrown", e);
    }
  }

}