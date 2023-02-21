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
            method(
              p(v("foo"))
            )
          );
        }
      }.toString(),

      """
      class Methods {
        void unnamed() {
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
            method(this::bodyImpl)
          );
        }

        private void bodyImpl() {
          p(v("foo"));
        }
      }.toString(),

      """
      class Methods {
        void unnamed() {
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
        static final ClassTypeName STRING = classType(String.class);

        static final TypeVariableName N = typeVariable("N");

        static final ParameterizedTypeName LIST_N = parameterizedType(
          classType(List.class), N
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
              parameter(STRING, "a"),
              parameter(STRING, "b")
            ),

            method(
              name("test2"),
              parameter(INT, "a"),
              parameter(DOUBLE, "b"),
              parameter(BOOLEAN, "c")
            ),

            method(
              name("test3"),
              parameter(INT, ELLIPSIS, "a")
            ),

            method(
              name("test4"),
              parameter(String[].class, "args")
            ),

            method(
              name("test5"),
              parameter(N, "n")
            ),

            method(
              name("test6"),
              parameter(LIST_N, "list")
            ),

            method(
              returnType(STRING),
              name("test7"),
              parameter(STRING, "a")
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
            method(
              VOID, name("test0"),
              p(v("a"))
            ),

            method(
              VOID, name("test1"),
              p(v("a")),
              p(v("b"))
            ),

            method(
              VOID, name("test2"),
              p(v("a")),
              p(v("b")),
              p(v("c"))
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
        static final TypeVariableName E = typeVariable("E");

        static final ParameterizedTypeName MAP_KV = parameterizedType(
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
              returnType(E),
              name("test4")
            ),

            method(
              returnType(MAP_KV),
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
            method(
              ABSTRACT, VOID, name("test0")
            ),

            method(
              VOID, name("test1")
            )
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
        static final ClassTypeName OBJECT = classType(Object.class);

        static final ClassTypeName SERIALIZABLE = classType(Serializable.class);

        static final ClassTypeName STRING = classType(String.class);

        @Override
        protected final void definition() {
          _class("Methods");
          body(
            method(
              typeParameter("T"), name("test0")
            ),

            method(
              PUBLIC, typeParameter("T"), name("test1")
            ),

            method(
              typeParameter("E1"),
              typeParameter("E2"),
              name("test2")
            ),

            method(
              typeParameter("T", OBJECT),
              name("test3")
            ),

            method(
              typeParameter("T", OBJECT, SERIALIZABLE),
              returnType(STRING),
              name("test4")
            )
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
        static final ClassTypeName STRING = classType(String.class);

        @Override
        protected final void definition() {
          _class("Methods");
          body(
            method(this::params)
          );
        }

        private void params() {
          modifiers(ABSTRACT);
          name("test0");
          parameter(INT, "a");
          parameter(STRING, "b");
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
        static final ClassTypeName STRING = classType(String.class);

        static final ClassTypeName FOO = classType("com.example", "Foo");

        static final ParameterizedTypeName MAP_I_S = parameterizedType(
          classType(Map.class),
          classType(Integer.class),
          classType(String.class)
        );

        static final TypeVariableName E = typeVariable("E");

        @Override
        protected final void definition() {
          _class("Methods");
          body(
            method(
              PUBLIC, FINAL, STRING, name("a")
            ),

            method(
              PRIVATE, FOO, name("foo")
            ),

            method(
              annotation(Override.class),
              PUBLIC, INT, name("hashCode")
            ),

            method(
              PUBLIC, STATIC, MAP_I_S, name("b")
            ),

            method(
              E, name("e")
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