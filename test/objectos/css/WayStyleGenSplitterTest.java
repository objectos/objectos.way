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
import objectos.way.Html;
import org.testng.annotations.Test;

public class WayStyleGenSplitterTest {

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
            className("m-0 block leading-3")
        );
      }
    }

    test(Subject.class, "m-0", "block", "leading-3");
  }

  @Test(description = "many class names w/ additional whitespace")
  public void testCase03() {
    class Subject extends Html.Template {
      @Override
      protected final void render() {
        div(
            className("m-0   block  leading-3")
        );
      }
    }

    test(Subject.class, "m-0", "block", "leading-3");
  }

  @Test(description = "leading whitespace")
  public void testCase04() {
    class Subject extends Html.Template {
      @Override
      protected final void render() {
        div(
            className(" block")
        );
      }
    }

    test(Subject.class, "block");
  }

  @Test(description = "trailing whitespace")
  public void testCase05() {
    class Subject extends Html.Template {
      @Override
      protected final void render() {
        div(
            className("block ")
        );
      }
    }

    test(Subject.class, "block");
  }

  @Test
  public void testCase06() {
    class Subject extends Html.Template {
      @Override
      protected final void render() {
        div(
            className("sr-only underline focus:not-sr-only focus:flex focus:h-full focus:items-center focus:border-4 focus:border-focus focus:py-16px focus:outline-none")
        );
      }
    }

    test(Subject.class, "sr-only", "underline", "focus:not-sr-only", "focus:flex", "focus:h-full", "focus:items-center", "focus:border-4", "focus:border-focus", "focus:py-16px", "focus:outline-none");
  }

  private void test(Class<?> type, String... expected) {
    List<String> result;
    result = new ArrayList<>();

    new WayStyleGenSplitter() {
      @Override
      final void onSplit(String s) {
        result.add(s);
      }
    }.scan(type);

    assertEquals(result, List.of(expected));
  }

}