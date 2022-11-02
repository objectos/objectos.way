/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
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
package objectos.code;

import org.testng.annotations.Test;

public class IncludeTest extends AbstractObjectoxCodeTest {

  @Test(description = """
  - single include
  - single statement
  """)
  public void testCase01() {
    var tmpl = new JavaTemplate() {
      @Override
      protected final void definition() {
        invoke("test", include(this::body));
      }

      private void body() {
        invoke("foo");
      }
    };

    testDefault(
      tmpl,

      """
      test(foo());
      """
    );
  }

  @Test(description = """
  - single include
  - many statements
  """)
  public void testCase02() {
    var tmpl = new JavaTemplate() {
      @Override
      protected final void definition() {
        invoke("test", include(this::body));
      }

      private void body() {
        invoke("a");
        invoke("b");
        invoke("c");
      }
    };

    testDefault(
      tmpl,

      """
      test(a(), b(), c());
      """
    );
  }

  @Test(description = """
  - many includes
  - same level
  """)
  public void testCase03() {
    var tmpl = new JavaTemplate() {
      @Override
      protected final void definition() {
        invoke(
          "test",
          include(this::body1),
          invoke("d"),
          include(this::body2)
        );
      }

      private void body1() {
        invoke("a");
        invoke("b");
        invoke("c");
      }

      private void body2() {
        invoke("e");
        invoke("f");
      }
    };

    testDefault(
      tmpl,

      """
      test(a(), b(), c(), d(), e(), f());
      """
    );
  }

}