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
          _class(id("Test0"),
            constructor(_public())
          );
          _class(id("Test1"),
            constructor(_protected())
          );
          _class(id("Test2"),
            constructor()
          );
          constructor(_private());
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

      private Constructor() {}
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
          constructor(
            param(_int(), id("a"))
          );
          constructor(
            param(_int(), id("a")),
            param(_int(), id("b"))
          );
        }
      }.toString(),

      """
      Constructor(int a) {}

      Constructor(int a, int b) {}
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
          constructor(
            param(_int(), id("a")),
            assign(n(_this(), "a"), n("a"))
          );
          constructor(
            param(_int(), id("a")),
            param(_int(), id("b")),
            assign(n(_this(), "a"), n("a")),
            assign(n(_this(), "b"), n("b"))
          );
        }
      }.toString(),

      """
      Constructor(int a) {
        this.a = a;
      }

      Constructor(int a, int b) {
        this.a = a;
        this.b = b;
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
          constructor(_super());
          constructor(_super(s("a")));
          constructor(_super(s("a"), s("b")));
        }
      }.toString(),

      """
      Constructor() {
        super();
      }

      Constructor() {
        super("a");
      }

      Constructor() {
        super("a", "b");
      }
      """
    );
  }

}
