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
package objectos.code2.test;

import static org.testng.Assert.assertEquals;

import objectos.code2.JavaTemplate;
import org.testng.annotations.Test;

public class ClassDeclarationTest {

  @Test(description = """
  final class Subject {}
  """)
  public void testCase01() {
    // @formatter:off
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _final(); _class("A"); body();
        }
      }.toString(),

      """
      final class A {}
      """
    );
    // @formatter:on
  }

  /*

  @Test(description = """
  single annotation on class

  @java.lang.Deprecated
  class Subject {}
  """)
  public void testCase02() {
    // @formatter:off
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          at(t(Deprecated.class));
          _class("Subject"); body();
        }
      }.toString(),

      """
      @java.lang.Deprecated
      class Subject {}
      """
    );
    // @formatter:on
  }

  @Test(description = """
  single method

  class Subject {
    void m0() {}
  }
  """)
  public void testCase03() {
    // @formatter:off
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _class("Subject"); body(
            _void(), id("m0"), block()
          );
        }
      }.toString(),

      """
      class Subject {
        void m0() {}
      }
      """
    );
    // @formatter:on
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
          _class(
            id("Test"),
            include(this::includeTest)
          );
        }

        private void includeTest() {
          field(_int(), id("a"));
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
          _class(
            id("Level1"),
            _class(id("A"))
          );

          _class(
            id("Level2"),
            _class(id("A"),
              _class(id("B"))
            )
          );

          _class(
            id("Level3"),
            _class(id("A"),
              _class(id("B"),
                _class(id("C"))
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
          _class(_public(), id("A"));
          _class(_protected(), id("B"));
          _class(_private(), id("C"));
          _class(_static(), id("D"));
          _class(_abstract(), id("E"));
          _class(_final(), id("F"));
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
          _class(id("A"), _implements(t(AutoCloseable.class)));

          _class(id("B"), _implements(t(AutoCloseable.class), t(Serializable.class)));

          _class(
            id("C"), _extends(t("objectos.code", "Foo")),
            _implements(t(AutoCloseable.class), t(Serializable.class))
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
          _class(
            id("A"), _extends(t(Thread.class)),

            method(
              annotation(t(Override.class)),
              id("foo")
            )
          );

          _class(
            id("B"), _implements(t(Serializable.class)),

            method(
              annotation(t(Override.class)),
              id("foo")
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

  @Test(description = """
  Class declarations TC09

  - more than one member
  """)
  public void testCase09() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _class(
            id("A"),

            field(_int(), id("value")),

            method(
              _int(), id("value"),
              _return(n("value"))
            )
          );
        }
      }.toString(),

      """
      class A {
        int value;

        int value() {
          return value;
        }
      }
      """
    );
  }

  */

}