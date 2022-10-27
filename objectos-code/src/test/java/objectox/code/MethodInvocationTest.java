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
package objectox.code;

import objectos.code.JavaTemplate;
import org.testng.annotations.Test;

public class MethodInvocationTest extends AbstractObjectoxCodeTest {

  @Test(description = """
  - unqualified
  - no args
  """)
  public void testCase01() {
    var tmpl = new JavaTemplate() {
      @Override
      protected final void definition() {
        invoke(name("test"));
      }
    };

    testDefault(
      tmpl,

      """
      test();
      """
    );
  }

  @Test(description = """
  - unqualified
  - single argument
  """)
  public void testCase02() {
    var tmpl = new JavaTemplate() {
      @Override
      protected final void definition() {
        invoke(name("test"), s("a"));
      }
    };

    testDefault(
      tmpl,

      """
      test("a");
      """
    );
  }

  @Test(description = """
    - unqualified
    - two arguments
    """)
  public void testCase03() {
    var tmpl = new JavaTemplate() {
      @Override
      protected final void definition() {
        invoke(name("test"), s("a"), s("b"));
      }
    };

    testDefault(
      tmpl,

      """
      test("a", "b");
      """
    );
  }

}