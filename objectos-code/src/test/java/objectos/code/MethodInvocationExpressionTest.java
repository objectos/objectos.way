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
          invoke("test");
        }
      }.toString(),

      """
      test();
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
          invoke("test", s("a"));
        }
      }.toString(),

      """
      test("a");
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
          invoke("test", s("a"), s("b"));
        }
      }.toString(),

      """
      test("a", "b");
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
          invoke("m0", s("1"), invoke("m2"), s("3"));
        }
      }.toString(),

      """
      m0("1", m2(), "3");
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
          invoke("m0", nl(), s("1"), nl(), nl(), invoke("m2"), nl(), nl(), s("3"), nl());
        }
      }.toString(),

      """
      m0(
        "1",

        m2(),

        "3"
      );
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
          invoke("test", n("field"));
        }
      }.toString(),

      """
      test(field);
      """
    );
  }

  @Test(description = """
  static methods
  """)
  public void testCase07() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          invoke(t(Thread.class), "currentThread");
        }
      }.toString(),

      """
      java.lang.Thread.currentThread();
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
        @Override
        protected final void definition() {
          invoke(n("a"), "x");
          invoke(n("b"), "y", s("1"));
          invoke(n("c"), "z", s("1"), s("2"));
          invoke(n(ClassName.of(Foo.class), "CTE"), "m");
        }
      }.toString(),

      """
      a.x();

      b.y("1");

      c.z("1", "2");

      objectos.code.Foo.CTE.m();
      """
    );
  }

}