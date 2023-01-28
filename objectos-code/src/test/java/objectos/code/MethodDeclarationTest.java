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
          _class("Methods");
          body(
            _void(), method("test"), block()
          );
        }
      }.toString(),

      """
      class Methods {
        void test() {}
      }
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
          _class("Methods");
          body(
            _void(), method("test"), block(
              invoke("foo")
            )
          );
        }
      }.toString(),

      """
      class Methods {
        void test() {
          foo();
        }
      }
      """
    );
  }
  //
  //  @Test(description = """
  //  - single annotation
  //  - void
  //  - empty body
  //  """)
  //  public void testCase03() {
  //    assertEquals(
  //      new JavaTemplate() {
  //        @Override
  //        protected final void definition() {
  //          _class("Methods");
  //          body(
  //            at(t(Override.class)),
  //            _void(), method("test"), block()
  //          );
  //        }
  //      }.toString(),
  //
  //      """
  //      class Methods {
  //        @java.lang.Override
  //        void test() {}
  //      }
  //      """
  //    );
  //  }
  //
  //  @Test(description = """
  //  Method declarations TC04
  //
  //  - modifiers
  //  """)
  //  public void testCase04() {
  //    assertEquals(
  //      new JavaTemplate() {
  //        @Override
  //        protected final void definition() {
  //          _class("Methods");
  //          body(
  //            _final(), _void(), method("a"), block(),
  //            _public(), _final(), _void(), method("b"), block(),
  //            _protected(), _static(), _final(), _void(), method("c"), block()
  //          );
  //        }
  //      }.toString(),
  //
  //      """
  //      class Methods {
  //        final void a() {}
  //
  //        public final void b() {}
  //
  //        protected static final void c() {}
  //      }
  //      """
  //    );
  //  }
  //
  //  @Test(description = """
  //  Add include support:
  //
  //  - single include
  //  - single statement
  //  """)
  //  public void testCase05() {
  //    assertEquals(
  //      new JavaTemplate() {
  //        @Override
  //        protected final void definition() {
  //          _class("Methods");
  //          body(
  //            _void(), method("test"), block(
  //              include(this::bodyImpl)
  //            )
  //          );
  //        }
  //
  //        private void bodyImpl() {
  //          invoke("foo");
  //        }
  //      }.toString(),
  //
  //      """
  //      class Methods {
  //        void test() {
  //          foo();
  //        }
  //      }
  //      """
  //    );
  //  }
  //
  //  @Test(description = """
  //  annotation + modifier
  //  """)
  //  public void testCase06() {
  //    assertEquals(
  //      new JavaTemplate() {
  //        @Override
  //        protected final void definition() {
  //          _class("Methods");
  //          body(
  //            at(t(Override.class)),
  //            _final(), _void(), method("test"), block()
  //          );
  //        }
  //      }.toString(),
  //
  //      """
  //      class Methods {
  //        @java.lang.Override
  //        final void test() {}
  //      }
  //      """
  //    );
  //  }
  //
  //  @Test(description = """
  //  Method declarations TC07
  //
  //  - parameters
  //  """)
  //  public void testCase07() {
  //    assertEquals(
  //      new JavaTemplate() {
  //        @Override
  //        protected final void definition() {
  //          _class("Methods");
  //          body(
  //            _void(), method("test0", t(String.class), id("a")), block(),
  //
  //            _void(), method("test1", t(String.class), id("a"), t(String.class), id("b")), block(),
  //
  //            _void(), method(
  //              "test2",
  //              _int(), id("a"),
  //              _double(), id("b"),
  //              _boolean(), id("c")
  //            ),
  //            block(),
  //
  //            _void(), method("test3", _int(), ellipsis(), id("a")), block(),
  //
  //            _void(), method("test4", t(t(String.class), dim()), id("args")), block(),
  //
  //            _void(), method("test5", tvar("N"), id("n")), block(),
  //
  //            _void(), method("test6", t(t(List.class), tvar("N")), id("list")), block(),
  //
  //            t(String.class), method("test7", t(String.class), id("a")), block(
  //              _return(), n("a")
  //            )
  //          );
  //        }
  //      }.toString(),
  //
  //      """
  //      class Methods {
  //        void test0(java.lang.String a) {}
  //
  //        void test1(java.lang.String a, java.lang.String b) {}
  //
  //        void test2(int a, double b, boolean c) {}
  //
  //        void test3(int... a) {}
  //
  //        void test4(java.lang.String[] args) {}
  //
  //        void test5(N n) {}
  //
  //        void test6(java.util.List<N> list) {}
  //
  //        java.lang.String test7(java.lang.String a) {
  //          return a;
  //        }
  //      }
  //      """
  //    );
  //  }
  //
  //  @Test(description = """
  //  Method declarations TC08
  //  - multiple statements
  //  """)
  //  public void testCase08() {
  //    assertEquals(
  //      new JavaTemplate() {
  //        @Override
  //        protected final void definition() {
  //          _class("Methods");
  //          body(
  //            _void(), method("test0"), block(
  //              invoke("a")
  //            ),
  //
  //            _void(), method("test1"), block(
  //              invoke("a"),
  //              invoke("b")
  //            ),
  //
  //            _void(), method("test2"), block(
  //              invoke("a"),
  //              invoke("b"),
  //              invoke("c")
  //            )
  //          );
  //        }
  //      }.toString(),
  //
  //      """
  //      class Methods {
  //        void test0() {
  //          a();
  //        }
  //
  //        void test1() {
  //          a();
  //          b();
  //        }
  //
  //        void test2() {
  //          a();
  //          b();
  //          c();
  //        }
  //      }
  //      """
  //    );
  //  }
  //
  //  @Test(description = """
  //  Method declarations TC09
  //
  //  - method return type
  //  """)
  //  public void testCase09() {
  //    assertEquals(
  //      new JavaTemplate() {
  //        @Override
  //        protected final void definition() {
  //          _class("Methods");
  //          body(
  //            _void(), method("test0"), block(),
  //
  //            t(t(String.class), dim()), method("test1"), block(),
  //
  //            _int(), method("test2"), block(),
  //
  //            t(Integer.class), method("test3"), block(),
  //
  //            tvar("E"), method("test4"), block(),
  //
  //            t(t(Map.class), tvar("K"), tvar("V")), method("test5"), block()
  //          );
  //        }
  //      }.toString(),
  //
  //      """
  //      class Methods {
  //        void test0() {}
  //
  //        java.lang.String[] test1() {}
  //
  //        int test2() {}
  //
  //        java.lang.Integer test3() {}
  //
  //        E test4() {}
  //
  //        java.util.Map<K, V> test5() {}
  //      }
  //      """
  //    );
  //  }
  //
  //  @Test(description = """
  //  Method declarations TC10
  //
  //  - abstract method vs method w/ empty body
  //  """)
  //  public void testCase10() {
  //    assertEquals(
  //      new JavaTemplate() {
  //        @Override
  //        protected final void definition() {
  //          _class("Methods");
  //          body(
  //            _abstract(), _void(), method("test0"),
  //
  //            _void(), method("test1"), block()
  //          );
  //        }
  //      }.toString(),
  //
  //      """
  //      class Methods {
  //        abstract void test0();
  //
  //        void test1() {}
  //      }
  //      """
  //    );
  //  }
  //
  //  @Test(description = """
  //  Method declarations TC11
  //
  //  - type parameters
  //  """)
  //  public void testCase11() {
  //    assertEquals(
  //      new JavaTemplate() {
  //        @Override
  //        protected final void definition() {
  //          _class("Methods");
  //          body(
  //            tparam("T"),
  //            _void(), method("test0"), block(),
  //
  //            _public(),
  //            tparam("T"),
  //            _void(), method("test1"), block(),
  //
  //            tparam("E1"),
  //            tparam("E2"),
  //            _void(), method("test2"), block(),
  //
  //            tparam("T", t(Object.class)),
  //            _void(), method("test3"), block(),
  //
  //            tparam("T", t(Object.class), t(Serializable.class)),
  //            t(String.class), method("test4"), block()
  //          );
  //        }
  //      }.toString(),
  //
  //      """
  //      class Methods {
  //        <T> void test0() {}
  //
  //        public <T> void test1() {}
  //
  //        <E1, E2> void test2() {}
  //
  //        <T extends java.lang.Object> void test3() {}
  //
  //        <T extends java.lang.Object & java.io.Serializable> java.lang.String test4() {}
  //      }
  //      """
  //    );
  //  }

}