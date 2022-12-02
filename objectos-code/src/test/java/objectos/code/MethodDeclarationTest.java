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
import java.util.List;
import java.util.Map;
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
            annotation(t(Override.class)),
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
            annotation(t(Override.class)),
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

          method(
            _void(), id("test5"),
            param(tvar("N"), id("n"))
          );

          method(
            _void(), id("test6"),
            param(t(t(List.class), tvar("N")), id("list"))
          );
        }
      }.toString(),

      """
      void test0(java.lang.String a) {}

      void test1(java.lang.String a, java.lang.String b) {}

      void test2(int a, double b, boolean c) {}

      void test3(int... a) {}

      void test4(java.lang.String[] args) {}

      void test5(N n) {}

      void test6(java.util.List<N> list) {}
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
          method(
            tvar("E"), id("test4")
          );
          method(
            t(t(Map.class), tvar("K"), tvar("V")), id("test5")
          );
        }
      }.toString(),

      """
      void test0() {}

      java.lang.String[] test1() {}

      int test2() {}

      java.lang.Integer test3() {}

      E test4() {}

      java.util.Map<K, V> test5() {}
      """
    );
  }

  @Test(description = """
  Method declarations TC10

  - abstract method vs method w/ empty body
  """)
  public void testCase10() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          method(
            _abstract(), _void(), id("test0")
          );
          method(
            _void(), id("test1")
          );
        }
      }.toString(),

      """
      abstract void test0();

      void test1() {}
      """
    );
  }

  @Test(description = """
  Method declarations TC11

  - type parameters
  """)
  public void testCase11() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          method(
            tparam("T"), _void(), id("test0")
          );
          method(
            _public(), tparam("T"), _void(), id("test1")
          );
          method(
            tparam("E1"), tparam("E2"), _void(), id("test2")
          );
          method(
            tparam("T", t(Object.class)), _void(), id("test3")
          );
          method(
            tparam("T", t(Object.class), t(Serializable.class)), _void(), id("test4")
          );
        }
      }.toString(),

      """
      <T> void test0() {}

      public <T> void test1() {}

      <E1, E2> void test2() {}

      <T extends java.lang.Object> void test3() {}

      <T extends java.lang.Object & java.io.Serializable> void test4() {}
      """
    );
  }

}