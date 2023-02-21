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
          _class("Invoke");
          body(
            method(
              v("test")
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
          _class("Invoke");
          body(
            method(
              v("test", s("a"))
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
          _class("Invoke");
          body(
            method(
              v("test", s("a"), s("b"))
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
          _class("Invoke");
          body(
            method(
              v("m0", s("1"), v("m2"), s("3"))
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
          _class("Invoke");
          body(
            method(
              v("m0", nl(),
                s("1"), nl(), nl(),
                v("m2"), nl(), nl(),
                s("3"), nl()
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
          _class("Invoke");
          body(
            method(
              v("test", n("field"))
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
        static final ClassTypeName COLLECTIONS = classType(Collections.class);

        static final ClassTypeName THREAD = classType(Thread.class);

        @Override
        protected final void definition() {
          _class("Invoke");
          body(
            method(
              t(THREAD).v("currentThread"),

              t(COLLECTIONS).v("sort", n("list"))
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
        static final ClassTypeName FOO = classType(Foo.class);

        @Override
        protected final void definition() {
          _class("Invoke");
          body(
            method(
              n("a").v("x"),

              n("b").v("y", s("1")),

              n("c").v("z", s("1"), s("2")),

              t(FOO).n("CTE").v("m")
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
          _class("Invoke");
          body(
            method(
              v("a").v("b"),

              v("a").v("b").v("c"),

              n("foo").v("a").v("b").v("c"),

              n("list").v("add", s("1")).v("add", s("2")).v("build")
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
        static final ClassTypeName THREAD = classType(Thread.class);

        @Override
        protected final void definition() {
          _class("Invoke");
          body(
            method(
              NEW, t(THREAD).v("start"),

              NEW, t(THREAD, s("foo")).v("start")
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
          _class("Invoke");
          body(
            method(
              v("test", v("a").v("b")),

              v("test", v("a"), v("b"))
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
        static final ClassTypeName A = classType("com.example", "A");

        @Override
        protected final void definition() {
          _class("Invoke");
          body(
            method(
              v("property", nl(),
                t(A).n("B"), nl(),
                s("some text"), nl()
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