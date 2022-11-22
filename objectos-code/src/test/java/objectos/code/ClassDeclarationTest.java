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

public class ClassDeclarationTest {

  @Test(description = """
  final class Subject {}
  """)
  public void testCase01() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _class(_final(), id("Subject"));
        }
      }.toString(),

      """
      final class Subject {}
      """
    );
  }

  @Test(description = """
  single annotation on class

  @java.lang.Deprecated
  class Subject {}
  """)
  public void testCase02() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _class(
            annotation(Deprecated.class),
            id("Subject")
          );
        }
      }.toString(),

      """
      @java.lang.Deprecated
      class Subject {}
      """
    );
  }

  @Test(description = """
  single method

  class Subject {
    void m0() {}
  }
  """)
  public void testCase03() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _class(
            id("Subject"),
            method(id("m0"))
          );
        }
      }.toString(),

      """
      class Subject {
        void m0() {}
      }
      """
    );
  }

  @Test(description = """
  Class declarations TC04

  - allow includes
  """)
  public void testCase04() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _class(
            id("Test"),
            include(this::includeTest)
          );
        }

        private void includeTest() {
          field(_int(), id("a"));
        }
      }.toString(),

      """
      class Test {
        int a;
      }
      """
    );
  }

}