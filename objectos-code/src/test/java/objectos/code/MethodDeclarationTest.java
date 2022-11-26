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

public class MethodDeclarationTest {

  @Test(description = """
  - void
  - empty body
  """)
  public void testCase01() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          method(
            _void(), id("test")
          );
        }
      }.toString(),

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
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          method(
            _void(), id("test"),
            invoke("foo")
          );
        }
      }.toString(),

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
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          method(
            annotation(Override.class),
            _void(), id("test")
          );
        }
      }.toString(),

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
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          method(
            _final(), _void(), id("test")
          );
        }
      }.toString(),

      """
      final void test() {}
      """
    );
  }

  @Test(description = """
  Add include support:

  - single include
  - single statement
  """)
  public void testCase05() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          method(
            _void(), id("test"),
            include(this::body)
          );
        }

        private void body() {
          invoke("foo");
        }
      }.toString(),

      """
      void test() {
        foo();
      }
      """
    );
  }

  @Test(description = """
  annotation + modifier
  """)
  public void testCase06() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          method(
            annotation(Override.class),
            _final(), _void(), id("test")
          );
        }
      }.toString(),

      """
      @java.lang.Override
      final void test() {}
      """
    );
  }

  @Test(description = """
  Method declarations TC07

  - parameters
  """)
  public void testCase07() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          method(
            _void(), id("test0"),
            param(t(String.class), id("a"))
          );

          method(
            _void(), id("test1"),
            param(t(String.class), id("a")),
            param(t(String.class), id("b"))
          );

          method(
            _void(), id("test2"),
            param(_int(), id("a")),
            param(_double(), id("b")),
            param(_boolean(), id("c"))
          );

          method(
            _void(), id("test3"),
            param(_int(), ellipsis(), id("a"))
          );

          method(
            _void(), id("test4"),
            param(t(t(String.class), dim()), id("args"))
          );
        }
      }.toString(),

      """
      void test0(java.lang.String a) {}

      void test1(java.lang.String a, java.lang.String b) {}

      void test2(int a, double b, boolean c) {}

      void test3(int... a) {}

      void test4(java.lang.String[] args) {}
      """
    );
  }

  @Test(description = """
  Method declarations TC08
  - multiple statements
  """)
  public void testCase08() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          method(
            _void(), id("test0"),
            invoke("a")
          );
          method(
            _void(), id("test1"),
            invoke("a"),
            invoke("b")
          );
          method(
            _void(), id("test2"),
            invoke("a"),
            invoke("b"),
            invoke("c")
          );
        }
      }.toString(),

      """
      void test0() {
        a();
      }

      void test1() {
        a();
        b();
      }

      void test2() {
        a();
        b();
        c();
      }
      """
    );
  }

  @Test(description = """
  Method declarations TC09

  - method return type
  """)
  public void testCase09() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          method(
            _void(), id("test0")
          );
          method(
            t(t(String.class), dim()), id("test1")
          );
          method(
            _int(), id("test2")
          );
          method(
            t(Integer.class), id("test3")
          );
        }
      }.toString(),

      """
      void test0() {}

      java.lang.String[] test1() {}

      int test2() {}

      java.lang.Integer test3() {}
      """
    );
  }

}