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

public class ConstructorDeclarationTest {

  //  @Test(description = """
  //  Constructor declarations TC01:
  //
  //  - modifiers
  //  """)
  //  public void testCase01() {
  //    assertEquals(
  //      new JavaTemplate() {
  //        @Override
  //        protected final void definition() {
  //          _class("Test0");
  //          body(
  //            _public(), constructor(), block()
  //          );
  //
  //          _class("Test1");
  //          body(
  //            _protected(), constructor(), block()
  //          );
  //
  //          _class("Test2");
  //          body(
  //            constructor(), block()
  //          );
  //
  //          _class("Test3");
  //          body(
  //            _private(), constructor(), block()
  //          );
  //        }
  //      }.toString(),
  //
  //      """
  //      class Test0 {
  //        public Test0() {}
  //      }
  //
  //      class Test1 {
  //        protected Test1() {}
  //      }
  //
  //      class Test2 {
  //        Test2() {}
  //      }
  //
  //      class Test3 {
  //        private Test3() {}
  //      }
  //      """
  //    );
  //  }
  //
  //  @Test(description = """
  //  Constructor declarations TC02:
  //
  //  - parameters
  //  """)
  //  public void testCase02() {
  //    assertEquals(
  //      new JavaTemplate() {
  //        @Override
  //        protected final void definition() {
  //          _class("Test");
  //          body(
  //            constructor(_int(), id("a")),
  //            block(),
  //
  //            constructor(_int(), id("a"), _int(), id("b")),
  //            block()
  //          );
  //        }
  //      }.toString(),
  //
  //      """
  //      class Test {
  //        Test(int a) {}
  //
  //        Test(int a, int b) {}
  //      }
  //      """
  //    );
  //  }
  //
  //  @Test(description = """
  //  Constructor declarations TC03:
  //
  //  - body
  //  """)
  //  public void testCase03() {
  //    assertEquals(
  //      new JavaTemplate() {
  //        @Override
  //        protected final void definition() {
  //          _class("Test");
  //          body(
  //            constructor(
  //              _int(), id("a")
  //            ),
  //            block(
  //              assign(_this().n("a"), n("a"))
  //            ),
  //            constructor(
  //              _int(), id("a"),
  //              _int(), id("b")
  //            ),
  //            block(
  //              assign(_this().n("a"), n("a")),
  //              assign(_this().n("b"), n("b"))
  //            )
  //          );
  //        }
  //      }.toString(),
  //
  //      """
  //          class Test {
  //            Test(int a) {
  //              this.a = a;
  //            }
  //
  //            Test(int a, int b) {
  //              this.a = a;
  //              this.b = b;
  //            }
  //          }
  //          """
  //    );
  //  }
  //
  //  @Test(description = """
  //  Constructor declarations TC04:
  //
  //  - super invocations
  //  """)
  //  public void testCase04() {
  //    assertEquals(
  //      new JavaTemplate() {
  //        @Override
  //        protected final void definition() {
  //          _class("Test");
  //          body(
  //            constructor(),
  //            block(
  //              _super()
  //            ),
  //
  //            constructor(),
  //            block(
  //              _super(),
  //              invoke("a")
  //            ),
  //
  //            constructor(),
  //            block(
  //              _super(),
  //              _this().invoke("a")
  //            ),
  //
  //            constructor(_int(), id("a")),
  //            block(
  //              _super(n("a"))
  //            ),
  //
  //            constructor(),
  //            block(
  //              _super(s("a"), s("b"))
  //            )
  //          );
  //        }
  //      }.toString(),
  //
  //      """
  //      class Test {
  //        Test() {
  //          super();
  //        }
  //
  //        Test() {
  //          super();
  //          a();
  //        }
  //
  //        Test() {
  //          super();
  //          this.a();
  //        }
  //
  //        Test(int a) {
  //          super(a);
  //        }
  //
  //        Test() {
  //          super("a", "b");
  //        }
  //      }
  //      """
  //    );
  //  }

}
