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
import java.util.List;
import java.util.Map;
import org.testng.annotations.Test;

public class MethodDeclarationTest {

  @Test(description = """
  Method declaration TC00

  method() invoked with no args
  """)
  public void testCase00() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _class("Methods");
          body(
            method()
          );
        }
      }.toString(),

      """
      class Methods {
        void unnamed() {}
      }
      """
    );
  }

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
            method(
              name("test")
            )
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
          _class("Methods");
          body(
            method(
              annotation(Override.class),
              name("test")
            )
          );
        }
      }.toString(),

      """
      class Methods {
        @java.lang.Override
        void test() {}
      }
      """
    );
  }

  @Test(description = """
  Method declarations TC04

  - modifiers
  """)
  public void testCase04() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _class("Methods");
          body(
            method(
              modifiers(FINAL),
              name("a")
            ),

            method(
              modifiers(PUBLIC, FINAL),
              name("b")
            ),

            method(
              modifiers(PROTECTED, STATIC, FINAL),
              name("c")
            )
          );
        }
      }.toString(),

      """
      class Methods {
        final void a() {}

        public final void b() {}

        protected static final void c() {}
      }
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
          _class("Methods");
          body(
            _void(), method("test"), block(
              include(this::bodyImpl)
            )
          );
        }

        private void bodyImpl() {
          invoke("foo");
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

  @Test(description = """
  annotation + modifier
  """)
  public void testCase06() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _class("Methods");
          body(
            method(
              annotation(Override.class),
              modifiers(FINAL),
              name("test")
            )
          );
        }
      }.toString(),

      """
      class Methods {
        @java.lang.Override
        final void test() {}
      }
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
        static final ClassTypeName String$ = classType(String.class);

        static final TypeVariableName N$ = typeVariable("N");

        static final ParameterizedTypeName ListN = parameterizedType(
          classType(List.class), N$
        );

        @Override
        protected final void definition() {
          _class("Methods");
          body(
            method(
              name("test0"),
              parameter(String.class, "a")
            ),

            method(
              name("test1"),
              parameter(String$, "a"),
              parameter(String$, "b")
            ),

            method(
              name("test2"),
              parameter(INT, "a"),
              parameter(DOUBLE, "b"),
              parameter(BOOLEAN, "c")
            ),

            _void(), method("test3", _int(), ellipsis(), id("a")), block(),

            method(
              name("test4"),
              parameter(String[].class, "args")
            ),

            method(
              name("test5"),
              parameter(N$, "n")
            ),

            method(
              name("test6"),
              parameter(ListN, "list")
            ),

            method(
              returnType(String$),
              name("test7"),
              parameter(String$, "a")
            )
          );
        }
      }.toString(),

      """
      class Methods {
        void test0(java.lang.String a) {}

        void test1(java.lang.String a, java.lang.String b) {}

        void test2(int a, double b, boolean c) {}

        void test3(int... a) {}

        void test4(java.lang.String[] args) {}

        void test5(N n) {}

        void test6(java.util.List<N> list) {}

        java.lang.String test7(java.lang.String a) {}
      }
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
          _class("Methods");
          body(
            _void(), method("test0"), block(
              invoke("a")
            ),

            _void(), method("test1"), block(
              invoke("a"), end(),
              invoke("b")
            ),

            _void(), method("test2"), block(
              invoke("a"), end(),
              invoke("b"), end(),
              invoke("c")
            )
          );
        }
      }.toString(),

      """
      class Methods {
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
        static final TypeVariableName E$ = typeVariable("E");

        static final ParameterizedTypeName MapKV = parameterizedType(
          classType(Map.class), typeVariable("K"), typeVariable("V")
        );

        @Override
        protected final void definition() {
          _class("Methods");
          body(
            method(
              name("test0")
            ),

            method(
              returnType(String[].class),
              name("test1")
            ),

            method(
              returnType(INT),
              name("test2")
            ),

            method(
              returnType(Integer.class),
              name("test3")
            ),

            method(
              returnType(E$),
              name("test4")
            ),

            method(
              returnType(MapKV),
              name("test5")
            )
          );
        }
      }.toString(),

      """
      class Methods {
        void test0() {}

        java.lang.String[] test1() {}

        int test2() {}

        java.lang.Integer test3() {}

        E test4() {}

        java.util.Map<K, V> test5() {}
      }
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
          _class("Methods");
          body(
            _abstract(), _void(), method("test0"),

            _void(), method("test1"), block()
          );
        }
      }.toString(),

      """
      class Methods {
        abstract void test0();

        void test1() {}
      }
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
          _class("Methods");
          body(
            tparam("T"),
            _void(), method("test0"), block(),

            _public(),
            tparam("T"),
            _void(), method("test1"), block(),

            tparam("E1"),
            tparam("E2"),
            _void(), method("test2"), block(),

            tparam("T", t(Object.class)),
            _void(), method("test3"), block(),

            tparam("T", t(Object.class), t(Serializable.class)),
            t(String.class), method("test4"), block()
          );
        }
      }.toString(),

      """
      class Methods {
        <T> void test0() {}

        public <T> void test1() {}

        <E1, E2> void test2() {}

        <T extends java.lang.Object> void test3() {}

        <T extends java.lang.Object & java.io.Serializable> java.lang.String test4() {}
      }
      """
    );
  }

  @Test(description = """
  Method declarations TC12

  - include parameters
  """)
  public void testCase12() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _class("Methods");
          body(
            _abstract(), _void(), method("test0", include(this::params))
          );
        }

        private void params() {
          code(_int(), id("a"));
          code(t(String.class), id("b"));
        }
      }.toString(),

      """
      class Methods {
        abstract void test0(int a, java.lang.String b);
      }
      """
    );
  }

  @Test(description = """
  Method declarations TC13

  - shorthand variant
  """)
  public void testCase13() {
    assertEquals(
      new JavaTemplate() {
        static final ClassTypeName String$ = classType(String.class);

        static final ClassTypeName Foo$ = classType("com.example", "Foo");

        static final ParameterizedTypeName Map$ = parameterizedType(
          classType(Map.class),
          classType(Integer.class),
          classType(String.class)
        );

        static final TypeVariableName E$ = typeVariable("E");

        @Override
        protected final void definition() {
          _class("Methods");
          body(
            method(
              PUBLIC, FINAL, String$, name("a")
            ),

            method(
              PRIVATE, Foo$, name("foo")
            ),

            method(
              annotation(Override.class),
              PUBLIC, INT, name("hashCode")
            ),

            method(
              PUBLIC, STATIC, Map$, name("b")
            ),

            method(
              E$, name("e")
            )
          );
        }
      }.toString(),

      """
      class Methods {
        public final java.lang.String a() {}

        private com.example.Foo foo() {}

        @java.lang.Override
        public int hashCode() {}

        public static java.util.Map<java.lang.Integer, java.lang.String> b() {}

        E e() {}
      }
      """
    );
  }

}