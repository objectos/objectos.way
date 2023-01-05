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
package objectos.code2.test;

import static org.testng.Assert.assertEquals;

import java.util.Map;
import objectos.code2.JavaTemplate;
import org.testng.annotations.Test;

public class FieldDeclarationTest {

  @Test(enabled = false, description = """
  typeName + names
  """)
  public void testCase01() {
    // @formatter:off
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _class("Fields"); body(
            t(String.class), id("name"),

            t(String.class), id("a"), id("b"), id("c")
          );
        }
      }.toString(),

      """
      class Fields {
        java.lang.String name;

        java.lang.String a, b, c;
      }
      """
    );
    // @formatter:on
  }

  @Test(enabled = false, description = """
  modifiers
  """)
  public void testCase02() {
    // @formatter:off
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _class("Fields"); body(
            _private(), t(String.class), id("a"),

            _private(), _final(), t(String.class), id("b"),

            _private(), _static(), _final(), t(String.class), id("c")
          );
        }
      }.toString(),

      """
      class Fields {
        private java.lang.String a;

        private final java.lang.String b;

        private static final java.lang.String c;
      }
      """
    );
    // @formatter:on
  }

  @Test(enabled = false, description = """
  init expression
  """)
  public void testCase03() {
    // @formatter:off
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _class("Fields"); body(
            t(String.class), id("x"), s("init"),

            t(String.class), id("a"), s("a"), id("b"), id("c"), s("c")
          );
        }
      }.toString(),

      """
      class Fields {
        java.lang.String x = "init";

        java.lang.String a = "a", b, c = "c";
      }
      """
    );
    // @formatter:on
  }

  @Test(enabled = false, description = """
  Field declarations TC05

  - array type
  """)
  public void testCase04() {
    // @formatter:off
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _class("Fields"); body(
            t(t(Object.class), dim()), id("a")
          );
        }
      }.toString(),

      """
      class Fields {
        java.lang.Object[] a;
      }
      """
    );
    // @formatter:on
  }

  @Test(enabled = false, description = """
  Field declarations TC05

  - parameterized type
  """)
  public void testCase05() {
    // @formatter:off
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _class("Fields"); body(
            t(t(Map.class), t(String.class), t(Integer.class)), id("map")
          );
        }
      }.toString(),

      """
      class Fields {
        java.util.Map<java.lang.String, java.lang.Integer> map;
      }
      """
    );
    // @formatter:on
  }

  @Test(enabled = false, description = """
  Field declarations TC06

  - array initializer
  """)
  public void testCase06() {
    // @formatter:off
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _class("Fields"); body(
            t(_int(), dim()), id("a"), a(),

            t(_int(), dim()), id("b"), a(i(0)),

            t(_int(), dim()), id("c"), a(i(0), i(1))
          );
        }
      }.toString(),

      """
      class Fields {
        int[] a = {};

        int[] b = {0};

        int[] c = {0, 1};
      }
      """
    );
    // @formatter:on
  }

  /*

  @Test(description = """
  Field declarations TC07

  - includes
  """)
  public void testCase07() {
    // @formatter:off
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          field(_int(), id("a"), include(this::a));
        }

        @Override
        private void a() {
          invoke("a");
        }
      }.toString(),

      """
      class Fields {
        int a = a();
      }
      """
    );
    // @formatter:on
  }

  */

}