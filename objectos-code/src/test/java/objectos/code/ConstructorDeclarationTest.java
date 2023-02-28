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

public class ConstructorDeclarationTest {

  @Test(description = """
  Constructor declarations TC01:

  - modifiers
  """)
  public void testCase01() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          classDeclaration(
            name("Test0"),
            constructor(
              modifiers(PUBLIC)
            )
          );

          classDeclaration(
            name("Test1"),
            constructor(
              modifiers(PROTECTED)
            )
          );

          classDeclaration(
            name("Test2"),
            constructor(
              modifiers()
            )
          );

          classDeclaration(
            name("Test3"),
            constructor(
              modifiers(PRIVATE)
            )
          );
        }
      }.toString(),

      """
      class Test0 {
        public Test0() {}
      }

      class Test1 {
        protected Test1() {}
      }

      class Test2 {
        Test2() {}
      }

      class Test3 {
        private Test3() {}
      }
      """
    );
  }

  @Test(description = """
  Constructor declarations TC02:

  - parameters
  """)
  public void testCase02() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          classDeclaration(
            name("Test"),
            constructor(
              parameter(INT, "a")
            ),

            constructor(
              parameter(INT, "a"),
              parameter(INT, "b")
            )
          );
        }
      }.toString(),

      """
      class Test {
        Test(int a) {}

        Test(int a, int b) {}
      }
      """
    );
  }

  @Test(description = """
  Constructor declarations TC03:

  - body
  """)
  public void testCase03() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          classDeclaration(
            name("Test"),
            constructor(
              parameter(INT, "a"),
              p(THIS, n("a"), IS, n("a"))
            ),

            constructor(
              parameter(INT, "a"),
              parameter(INT, "b"),
              p(THIS, n("a"), IS, n("a")),
              p(THIS, n("b"), IS, n("b"))
            )
          );
        }
      }.toString(),

      """
      class Test {
        Test(int a) {
          this.a = a;
        }

        Test(int a, int b) {
          this.a = a;
          this.b = b;
        }
      }
      """
    );
  }

  @Test(description = """
  Constructor declarations TC04:

  - super invocations
  """)
  public void testCase04() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          classDeclaration(
            name("Test"),
            constructor(
              p(SUPER)
            ),

            constructor(
              p(SUPER),
              p(v("a"))
            ),

            constructor(
              p(SUPER),
              p(THIS, v("a"))
            ),

            constructor(
              parameter(INT, "a"),
              p(SUPER, arg(n("a")))
            ),

            constructor(
              p(SUPER, arg(s("a")), arg(s("b")))
            )
          );
        }
      }.toString(),

      """
      class Test {
        Test() {
          super();
        }

        Test() {
          super();
          a();
        }

        Test() {
          super();
          this.a();
        }

        Test(int a) {
          super(a);
        }

        Test() {
          super("a", "b");
        }
      }
      """
    );
  }

  @Test(description = """
  Constructor declarations TC05:

  - abstract class + constructor
  """)
  public void testCase05() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          classDeclaration(
            ABSTRACT, name("Test"),
            constructor(
              parameter(INT, "value"),
              p(THIS, n("value"), IS, n("value"))
            )
          );
        }
      }.toString(),

      """
      abstract class Test {
        Test(int value) {
          this.value = value;
        }
      }
      """
    );
  }

}
