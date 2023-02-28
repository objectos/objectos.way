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

import java.util.Collections;
import org.testng.annotations.Test;

public class MethodInvocationExpressionTest {

  @Test(description = """
  - unqualified
  - no args
  """)
  public void testCase01() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          classDeclaration(
            name("Invoke"),

            method(
              p(v("test"))
            )
          );
        }
      }.toString(),

      """
      class Invoke {
        void unnamed() {
          test();
        }
      }
      """
    );
  }

  @Test(description = """
  - unqualified
  - single argument
  """)
  public void testCase02() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          classDeclaration(
            name("Invoke"),

            method(
              p(v("test"), arg(s("a")))
            )
          );
        }
      }.toString(),

      """
      class Invoke {
        void unnamed() {
          test("a");
        }
      }
      """
    );
  }

  @Test(description = """
  - unqualified
  - two arguments
  """)
  public void testCase03() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          classDeclaration(
            name("Invoke"),

            method(
              p(v("test"), arg(s("a")), arg(s("b")))
            )
          );
        }
      }.toString(),

      """
      class Invoke {
        void unnamed() {
          test("a", "b");
        }
      }
      """
    );
  }

  @Test(description = """
  - unqualified
  - three args
  - one arg is a nested invocation
  """)
  public void testCase04() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          classDeclaration(
            name("Invoke"),

            method(
              p(v("m0"), arg(s("1")), arg(v("m2")), arg(s("3")))
            )
          );
        }
      }.toString(),

      """
      class Invoke {
        void unnamed() {
          m0("1", m2(), "3");
        }
      }
      """
    );
  }

  @Test(description = """
  - unqualified
  - three args
  - explicit new lines
  """)
  public void testCase05() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          classDeclaration(
            name("Invoke"),

            method(
              p(
                v("m0"), NL,
                arg(s("1")), NL, NL,
                arg(v("m2")), NL, NL,
                arg(s("3"), NL)
              )
            )
          );
        }
      }.toString(),

      """
      class Invoke {
        void unnamed() {
          m0(
            "1",

            m2(),

            "3"
          );
        }
      }
      """
    );
  }

  @Test(description = """
  - allow expression names
  """)
  public void testCase06() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          classDeclaration(
            name("Invoke"),

            method(
              p(v("test"), arg(n("field")))
            )
          );
        }
      }.toString(),

      """
      class Invoke {
        void unnamed() {
          test(field);
        }
      }
      """
    );
  }

  @Test(description = """
  static methods
  """)
  public void testCase07() {
    assertEquals(
      new JavaTemplate() {
        static final ClassTypeName COLLECTIONS = ClassTypeName.of(Collections.class);

        static final ClassTypeName THREAD = ClassTypeName.of(Thread.class);

        @Override
        protected final void definition() {
          classDeclaration(
            name("Invoke"),

            method(
              p(THREAD, v("currentThread")),

              p(COLLECTIONS, v("sort"), arg(n("list")))
            )
          );
        }
      }.toString(),

      """
      class Invoke {
        void unnamed() {
          java.lang.Thread.currentThread();
          java.util.Collections.sort(list);
        }
      }
      """
    );
  }

  @Test(description = """
  Method Invocation Expresions TC08

  - expression name
  """)
  public void testCase08() {
    assertEquals(
      new JavaTemplate() {
        static final ClassTypeName FOO = ClassTypeName.of(Foo.class);

        @Override
        protected final void definition() {
          classDeclaration(
            name("Invoke"),

            method(
              p(n("a"), v("x")),

              p(n("b"), v("y"), arg(s("1"))),

              p(n("c"), v("z"), arg(s("1")), arg(s("2"))),

              p(FOO, n("CTE"), v("m"))
            )
          );
        }
      }.toString(),

      """
      class Invoke {
        void unnamed() {
          a.x();
          b.y("1");
          c.z("1", "2");
          objectos.code.Foo.CTE.m();
        }
      }
      """
    );
  }

  @Test(description = """
  Method Invocation Expresions TC09

  - chain support
  """)
  public void testCase09() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          classDeclaration(
            name("Invoke"),

            method(
              p(v("a"), v("b")),

              p(v("a"), v("b"), v("c")),

              p(n("foo"), v("a"), v("b"), v("c")),

              p(n("list"), v("add"), arg(s("1")), v("add"), arg(s("2")), v("build"))
            )
          );
        }
      }.toString(),

      """
      class Invoke {
        void unnamed() {
          a().b();
          a().b().c();
          foo.a().b().c();
          list.add("1").add("2").build();
        }
      }
      """
    );
  }

  @Test(description = """
  Method Invocation Expresions TC10

  - primary expressions
  """)
  public void testCase10() {
    assertEquals(
      new JavaTemplate() {
        static final ClassTypeName THREAD = ClassTypeName.of(Thread.class);

        @Override
        protected final void definition() {
          classDeclaration(
            name("Invoke"),

            method(
              p(NEW, THREAD, v("start")),

              p(NEW, THREAD, arg(s("foo")), v("start"))
            )
          );
        }
      }.toString(),

      """
      class Invoke {
        void unnamed() {
          new java.lang.Thread().start();
          new java.lang.Thread("foo").start();
        }
      }
      """
    );
  }

  @Test(description = """
  Method Invocation Expresions TC11

  - end() at argument list
  """)
  public void testCase11() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          classDeclaration(
            name("Invoke"),

            method(
              p(v("test"), arg(v("a"), v("b"))),

              p(v("test"), arg(v("a")), arg(v("b")))
            )
          );
        }
      }.toString(),

      """
      class Invoke {
        void unnamed() {
          test(a().b());
          test(a(), b());
        }
      }
      """
    );
  }

  @Test(description = """
  Method Invocation Expresions TC12

  - comma location after expression name
  """)
  public void testCase12() {
    assertEquals(
      new JavaTemplate() {
        static final ClassTypeName A = ClassTypeName.of("com.example", "A");

        @Override
        protected final void definition() {
          classDeclaration(
            name("Invoke"),

            method(
              p(
                v("property"), NL,
                arg(A, n("B")), NL,
                arg(s("some text"), NL)
              )
            )
          );
        }
      }.toString(),

      """
      class Invoke {
        void unnamed() {
          property(
            com.example.A.B,
            "some text"
          );
        }
      }
      """
    );
  }

}