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
  parameters
  """)
  public void testCase07() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          method(
            _void(), id("test"),
            param(t(String.class), id("a"))
          );
        }
      }.toString(),

      """
      void test(java.lang.String a) {}
      """
    );
  }

}