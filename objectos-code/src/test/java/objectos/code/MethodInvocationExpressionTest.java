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

import java.util.Collections;
import objectos.code.Fixture.Kind;
import org.testng.annotations.Test;

public class MethodInvocationExpressionTest {

  private final Fixture fix = new Fixture("Invoke", Kind.VOID_METHOD);

  @Test(description = """
  - unqualified
  - no args
  """)
  public void testCase01() {
    assertEquals(
      fix.ture(new JavaTemplate() {
        @Override
        protected final void definition() {
          invoke("test");
        }
      }),

      """
      class Invoke {
        void method() {
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
      fix.ture(new JavaTemplate() {
        @Override
        protected final void definition() {
          invoke("test", s("a"));
        }
      }),

      """
      class Invoke {
        void method() {
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
      fix.ture(new JavaTemplate() {
        @Override
        protected final void definition() {
          invoke("test", s("a"), s("b"));
        }
      }),

      """
      class Invoke {
        void method() {
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
      fix.ture(new JavaTemplate() {
        @Override
        protected final void definition() {
          invoke("m0", s("1"), end(), invoke("m2"), s("3"));
        }
      }),

      """
      class Invoke {
        void method() {
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
      fix.ture(new JavaTemplate() {
        @Override
        protected final void definition() {
          invoke("m0", nl(),
            s("1"), end(), nl(), nl(),
            invoke("m2"), nl(), nl(),
            s("3"), nl()
          );
        }
      }),

      """
      class Invoke {
        void method() {
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
      fix.ture(new JavaTemplate() {
        @Override
        protected final void definition() {
          invoke("test", n("field"));
        }
      }),

      """
      class Invoke {
        void method() {
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
    // @formatter:off
    assertEquals(
      fix.ture(new JavaTemplate() {
        @Override
        protected final void definition() {
          t(Thread.class); invoke("currentThread");

          t(Collections.class); invoke("sort", n("list"));
        }
      }),

      """
      class Invoke {
        void method() {
          java.lang.Thread.currentThread();
          java.util.Collections.sort(list);
        }
      }
      """
    );
    // @formatter:on
  }

  @Test(description = """
  Method Invocation Expresions TC08

  - expression name
  """)
  public void testCase08() {
    // @formatter:off
    assertEquals(
      fix.ture(new JavaTemplate() {
        @Override
        protected final void definition() {
          n("a"); invoke("x"); end();
          n("b"); invoke("y", s("1")); end();
          n("c"); invoke("z", s("1"), s("2"));
          t(Foo.class); n("CTE"); invoke("m");
        }
      }),

      """
      class Invoke {
        void method() {
          a.x();
          b.y("1");
          c.z("1", "2");
          objectos.code.Foo.CTE.m();
        }
      }
      """
    );
    // @formatter:on
  }

  @Test(description = """
  Method Invocation Expresions TC09

  - chain support
  """)
  public void testCase09() {
    // @formatter:off
    assertEquals(
      fix.ture(new JavaTemplate() {
        @Override
        protected final void definition() {
          invoke("a"); invoke("b"); end();

          invoke("a"); invoke("b"); invoke("c"); end();

          n("foo"); invoke("a"); invoke("b"); invoke("c"); end();

          n("list");
          invoke("add", s("1")); nl();
          invoke("add", s("2")); nl();
          invoke("build");
        }
      }),

      """
      class Invoke {
        void method() {
          a().b();
          a().b().c();
          foo.a().b().c();
          list.add("1")
              .add("2")
              .build();
        }
      }
      """
    );
    // @formatter:on
  }

  @Test(description = """
  Method Invocation Expresions TC10

  - primary expressions
  """)
  public void testCase10() {
    // @formatter:off
    assertEquals(
      fix.ture(new JavaTemplate() {
        @Override
        protected final void definition() {
          _new(t(Thread.class)); invoke("start");
        }
      }),

      """
      class Invoke {
        void method() {
          new java.lang.Thread().start();
        }
      }
      """
    );
    // @formatter:on
  }

  @Test(description = """
  Method Invocation Expresions TC11

  - end() at argument list
  """)
  public void testCase11() {
    // @formatter:off
    assertEquals(
      fix.ture(new JavaTemplate() {
        @Override
        protected final void definition() {
          invoke("test", invoke("a"), invoke("b")); end();
          invoke("test", invoke("a"), end(), invoke("b")); end();
        }
      }),

      """
      class Invoke {
        void method() {
          test(a().b());
          test(a(), b());
        }
      }
      """
    );
    // @formatter:on
  }

}