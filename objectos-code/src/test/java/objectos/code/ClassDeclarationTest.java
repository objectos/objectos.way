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

import java.io.Serializable;
import org.testng.annotations.Test;

public class ClassDeclarationTest {

  @Test(description = """
  final class Subject {}
  """)
  public void testCase01() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          code(
            _final(), _class("Subject"), body()
          );
        }
      }.toString(),

      """
      final class Subject {}
      """
    );
  }

  @Test(description = """
  single annotation on class

  @java.lang.Deprecated
  class Subject {}
  """)
  public void testCase02() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          code(
            at(t(Deprecated.class)),
            _class("Subject"), body()
          );
        }
      }.toString(),

      """
      @java.lang.Deprecated
      class Subject {}
      """
    );
  }

  @Test(description = """
  single method

  class Subject {
    void m0() {}
  }
  """)
  public void testCase03() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _class("Subject");
          body(
            _void(), method("m0"), block()
          );
        }
      }.toString(),

      """
      class Subject {
        void m0() {}
      }
      """
    );
  }

  @Test(description = """
  Class declarations TC04

  - allow includes
  """)
  public void testCase04() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _class("Test");
          body(
            include(this::includeTest)
          );
        }

        private void includeTest() {
          _int();
          id("a");
        }
      }.toString(),

      """
      class Test {
        int a;
      }
      """
    );
  }

  @Test(description = """
  Class declarations TC05

  - nested class declaration
  """)
  public void testCase05() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          code(
            _class("Level1"), body(
              _class("A"), body()
            ),

            _class("Level2"), body(
              _class("A"), body(
                _class("B"), body()
              )
            ),

            _class("Level3"), body(
              _class("A"), body(
                _class("B"), body(
                  _class("C"), body()
                )
              )
            )
          );
        }
      }.toString(),
      """
      class Level1 {
        class A {}
      }

      class Level2 {
        class A {
          class B {}
        }
      }

      class Level3 {
        class A {
          class B {
            class C {}
          }
        }
      }
      """
    );
  }

  @Test(description = """
  Class declarations TC06

  - class modifiers
  """)
  public void testCase06() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          code(
            _public(), _class("A"), body(),

            _protected(), _class("B"), body(),

            _private(), _class("C"), body(),

            _static(), _class("D"), body(),

            _abstract(), _class("E"), body(),

            _final(), _class("F"), body()
          );
        }
      }.toString(),

      """
      public class A {}

      protected class B {}

      private class C {}

      static class D {}

      abstract class E {}

      final class F {}
      """
    );
  }

  @Test(description = """
  Class declarations TC07

  - implements clause
  """)
  public void testCase07() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          code(
            _class("A"), _implements(), t(AutoCloseable.class), body(),

            _class("B"), _implements(), t(AutoCloseable.class), t(Serializable.class), body(),

            _class("C"), _extends(), t("objectos.code", "Foo"),
            _implements(), t(AutoCloseable.class), t(Serializable.class), body()
          );
        }
      }.toString(),

      """
      class A implements java.lang.AutoCloseable {}

      class B implements java.lang.AutoCloseable, java.io.Serializable {}

      class C extends objectos.code.Foo implements java.lang.AutoCloseable, java.io.Serializable {}
      """
    );
  }

  @Test(description = """
  Class declarations TC08

  - annotated method
  """)
  public void testCase08() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          code(
            _class("A"), _extends(), t(Thread.class), body(
              at(t(Override.class)),
              _void(), method("foo"), block()
            ),

            _class("B"), _implements(), t(Serializable.class), body(
              at(t(Override.class)),
              _void(), method("foo"), block()
            )
          );
        }
      }.toString(),

      """
      class A extends java.lang.Thread {
        @java.lang.Override
        void foo() {}
      }

      class B implements java.io.Serializable {
        @java.lang.Override
        void foo() {}
      }
      """
    );
  }

  //  @Test(enabled = false, description = """
  //  Class declarations TC09
  //
  //  - more than one member
  //  """)
  //  public void testCase09() {
//    // @formatter:off
//    assertEquals(
//      new JavaTemplate() {
//        @Override
//        protected final void definition() {
//          _class("A"); body(
//            _int(), field("value"),
//
//            _int(), field("value"), block(
//              _return(), n("value")
//            )
//          );
//        }
//      }.toString(),
//
//      """
//      class A {
//        int value;
//
//        int value() {
//          return value;
//        }
//      }
//      """
//    );
//    // @formatter:on
  //  }

}