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
          _class(_final(), id("Subject"));
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
          _class(
            annotation(t(Deprecated.class)),
            id("Subject")
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
          _class(
            id("Subject"),
            method(id("m0"))
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
    var iface1 = ClassName.of(AutoCloseable.class);
    var iface2 = ClassName.of(Serializable.class);

    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _class(id("A"), _implements(iface1));

          _class(id("B"), _implements(iface1, iface2));

          _class(id("C"), _extends(ClassName.of(Foo.class)), _implements(iface1, iface2));
        }
      }.toString(),

      """
      class A implements java.lang.AutoCloseable {}

      class B implements java.lang.AutoCloseable, java.io.Serializable {}

      class C extends objectos.code.Foo implements java.lang.AutoCloseable, java.io.Serializable {}
      """
    );
  }

}