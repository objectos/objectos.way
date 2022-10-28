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

public class MethodDeclarationTest extends AbstractObjectoxCodeTest {

  @Test(description = """
  - void
  - empty body
  """)
  public void testCase01() {
    var tmpl = new JavaTemplate() {
      @Override
      protected final void definition() {
        method(
          _void(), id("test")
        );
      }
    };

    testDefault(
      tmpl,

      """
      void test() {}
      """
    );
  }

  @Test(description = """
  - void
  - single statement
  """)
  public void testCase02() {
    var tmpl = new JavaTemplate() {
      @Override
      protected final void definition() {
        method(
          _void(), id("test"),
          invoke("foo")
        );
      }
    };

    testDefault(
      tmpl,

      """
      void test() {
        foo();
      }
      """
    );
  }

  @Test(description = """
  - single annotation
  - void
  - empty body
  """)
  public void testCase03() {
    var tmpl = new JavaTemplate() {
      @Override
      protected final void definition() {
        method(
          annotation(Override.class),
          _void(), id("test")
        );
      }
    };

    testDefault(
      tmpl,

      """
      @java.lang.Override
      void test() {}
      """
    );
  }

  @Test(description = """
  - single modifier
  - void
  - empty body
  """)
  public void testCase04() {
    var tmpl = new JavaTemplate() {
      @Override
      protected final void definition() {
        method(
          _final(), _void(), id("test")
        );
      }
    };

    testDefault(
      tmpl,

      """
      final void test() {}
      """
    );
  }

}