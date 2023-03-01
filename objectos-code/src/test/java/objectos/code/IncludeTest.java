/*
 * Copyright (C) 2014-2023 Objectos Software LTDA.
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

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class IncludeTest {

  @Test(description = """
  - single include
  - single statement
  """)
  public void testCase01() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          classDeclaration(
            name("TestCase01"),

            include(this::tc01)
          );
        }

        private void tc01() {
          field(INT, name("a"));
        }
      }.toString(),

      """
      class TestCase01 {
        int a;
      }
      """
    );
  }

  @Test(description = """
  - single include
  - many statements
  """)
  public void testCase02() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          classDeclaration(
            name("Test"),

            include(this::tc01)
          );
        }

        private void tc01() {
          method(
            name("foo"),
            p(
              v("test"),
              argument(v("a")),
              argument(v("b")),
              argument(v("c"))
            )
          );
        }
      }.toString(),

      """
      class Test {
        void foo() {
          test(a(), b(), c());
        }
      }
      """
    );
  }

  @Test(description = """
  - many includes
  - same level
  """)
  public void testCase03() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          classDeclaration(
            name("Test01"),

            include(this::tc01)
          );

          classDeclaration(name("Foo"));

          interfaceDeclaration(
            name("Test02"),

            include(this::tc02)
          );
        }

        private void tc01() {
          field(INT, name("a"));
          field(INT, name("b"));
          field(INT, name("c"));
        }

        private void tc02() {

          field(INT, name("d"));
          field(INT, name("e"));
          field(INT, name("f"));
        }
      }.toString(),
      """
      class Test01 {
        int a;

        int b;

        int c;
      }

      class Foo {}

      interface Test02 {
        int d;

        int e;

        int f;
      }
      """
    );
  }

  @Test(description = """
  Include TC04

  ArrayIndexOutOfBounds when included template adds no instructions
  """)
  public void testCase04() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          classDeclaration(
            name("L1A"),

            include(this::level1A)
          );

          classDeclaration(
            name("L1B"),

            include(this::level1B)
          );
        }

        private void level1A() {
          field(INT, name("field"));
        }

        private void level1B() {
          method(
            include(this::level2A)
          );

          method(
            name("l2B"),
            include(this::level2B)
          );
        }

        private void level2A() {
          // empty
        }

        private void level2B() {
          p(v("foo"), argument(s("level 2B")));
        }
      }.toString(),
      """
      class L1A {
        int field;
      }

      class L1B {
        void unnamed() {}

        void l2B() {
          foo("level 2B");
        }
      }
      """
    );
  }

}