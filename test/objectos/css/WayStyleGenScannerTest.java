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
package objectos.css;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import objectos.html.HtmlTemplate;
import org.testng.annotations.Test;

public class WayStyleGenScannerTest {

  @Test(description = "single class name")
  public void testCase01() {
    class Subject extends HtmlTemplate {
      @Override
      protected final void definition() {
        div(className("m-0"));
      }
    }

    test(Subject.class, "m-0");
  }

  @Test(description = "many class names")
  public void testCase02() {
    class Subject extends HtmlTemplate {
      @Override
      protected final void definition() {
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
    class Subject extends HtmlTemplate {
      @Override
      protected final void definition() {
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

  private void test(Class<?> type, String... expected) {
    List<String> result;
    result = new ArrayList<>();

    new WayStyleGenScanner() {
      @Override
      final void onScan(String s) {
        result.add(s);
      }
    }.scan(type);

    assertEquals(result, List.of(expected));
  }

}