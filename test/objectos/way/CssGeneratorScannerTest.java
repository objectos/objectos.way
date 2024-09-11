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

import java.util.ArrayList;
import java.util.List;
import org.testng.annotations.Test;

public class CssGeneratorScannerTest {

  @Test(description = "single class name")
  public void testCase01() {
    class Subject extends Html.Template {
      @Override
      protected final void render() {
        div(className("m-0"));
      }
    }

    test(Subject.class, "m-0");
  }

  @Test(description = "many class names")
  public void testCase02() {
    class Subject extends Html.Template {
      @Override
      protected final void render() {
        div(
            className("m-0"),
            className("block"),
            className("leading-3")
        );
      }
    }

    test(Subject.class, "m-0", "block", "leading-3");
  }

  @Test(description = "long literal")
  public void testCase03() {
    class Subject extends Html.Template {
      @Override
      protected final void render() {
        div(
            className("m-0"),
            className("block"),
            className("leading-3")
        );
        foo(123L);
      }

      private void foo(long l) {}
    }

    test(Subject.class, "m-0", "block", "leading-3");
  }

  @Test(description = "non-ascii modified utf-8")
  public void testCase04() {
    class Subject extends Html.Template {
      @Override
      protected final void render() {
        div(
            // single byte (last one two bytes to trigger parsing)
            className("\u0001\u0011\u0021\u0031\u0041\u0051\u0061\u007F\u0000"),
            // two bytes
            className("\u0000"),
            className("\u0080\u0081\u07FE\u07FF"),
            // three bytes
            className("\u0800\u0801\uFFFE\uFFFF"),
            // random
            className("Olá"),
            className("こんにちは世界")
        );
      }
    }

    test(
        Subject.class,

        "\u0001\u0011\u0021\u0031\u0041\u0051\u0061\u007F\u0000",
        "\u0000",
        "\u0080\u0081\u07FE\u07FF",
        "\u0800\u0801\uFFFE\uFFFF",
        "Olá",
        "こんにちは世界"
    );
  }

  private void test(Class<?> type, String... expected) {
    List<String> result;
    result = new ArrayList<>();

    CssGeneratorScanner scanner;
    scanner = new CssGeneratorScanner(TestingNoteSink.INSTANCE);

    scanner.scan(type, result::add);

    assertEquals(result, List.of(expected));
  }

}