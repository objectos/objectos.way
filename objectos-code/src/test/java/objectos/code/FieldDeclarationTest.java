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

import java.util.Map;
import org.testng.annotations.Test;

public class FieldDeclarationTest {

  @Test(description = """
  typeName + names
  """)
  public void testCase01() {
    assertEquals(
      new JavaTemplate() {
        static final ClassTypeName STRING = classType(String.class);

        @Override
        protected final void definition() {
          _class("Fields");
          body(
            field(STRING, name("a")),

            field(STRING, name("b"), name("c")),

            field(STRING, name("d"), name("e"), name("f"))
          );
        }
      }.toString(),

      """
      class Fields {
        java.lang.String a;

        java.lang.String b, c;

        java.lang.String d, e, f;
      }
      """
    );
  }

  @Test(description = """
  modifiers
  """)
  public void testCase02() {
    assertEquals(
      new JavaTemplate() {
        static final ClassTypeName STRING = classType(String.class);

        @Override
        protected final void definition() {
          _class("Fields");
          body(
            field(PRIVATE, STRING, name("a")),

            field(PRIVATE, FINAL, STRING, name("b")),

            field(PRIVATE, STATIC, FINAL, STRING, name("c"))
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
  }

  @Test(description = """
  init expression
  """)
  public void testCase03() {
    assertEquals(
      new JavaTemplate() {
        static final ClassTypeName STRING = classType(String.class);

        @Override
        protected final void definition() {
          _class("Fields");
          body(
            field(STRING, name("x"), s("init")),

            field(STRING,
              name("a"), s("a"),
              name("b"),
              name("c"), s("c")
            ),

            field(STRING, name("d"), v("d0"), v("d1"))
          );
        }
      }.toString(),

      """
      class Fields {
        java.lang.String x = "init";

        java.lang.String a = "a", b, c = "c";

        java.lang.String d = d0().d1();
      }
      """
    );
  }

  @Test(description = """
  Field declarations TC04

  - array type
  """)
  public void testCase04() {
    assertEquals(
      new JavaTemplate() {
        static final ArrayTypeName OBJECT_ARRAY = arrayType(Object[].class);

        @Override
        protected final void definition() {
          _class("Fields");
          body(
            field(OBJECT_ARRAY, name("a"))
          );
        }
      }.toString(),

      """
      class Fields {
        java.lang.Object[] a;
      }
      """
    );
  }

  @Test(description = """
  Field declarations TC05

  - parameterized type
  """)
  public void testCase05() {
    assertEquals(
      new JavaTemplate() {
        static final ParameterizedTypeName MAP = parameterizedType(
          classType(Map.class), classType(String.class), classType(Integer.class)
        );

        @Override
        protected final void definition() {
          _class("Fields");
          body(
            field(MAP, name("map"))
          );
        }
      }.toString(),

      """
      class Fields {
        java.util.Map<java.lang.String, java.lang.Integer> map;
      }
      """
    );
  }

  @Test(description = """
  Field declarations TC06

  - array initializer
  """)
  public void testCase06() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _class("Fields");
          body(
            t(_int(), dim()), id("a"), ainit(),

            t(_int(), dim()), id("b"), ainit(i(0)),

            t(_int(), dim()), id("c"), ainit(i(0), i(1)),

            t(_int(), dim()), id("d"), ainit(include(this::many))
          );
        }

        private void many() {
          nl();

          for (int i = 0; i < 3; i++) {
            code(i(i), nl());
          }
        }
      }.toString(),

      """
      class Fields {
        int[] a = {};

        int[] b = {0};

        int[] c = {0,1};

        int[] d = {
          0,
          1,
          2
        };
      }
      """
    );
  }

  @Test(description = """
  Field declarations TC07

  - init with static method invocation
  """)
  public void testCase07() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _class("Fields");
          body(
            t(_int(), dim()), id("a"), t("test", "Foo"), invoke("a"),

            t(_int(), dim()), id("b"), t("test", "Foo"), n("b")
          );
        }
      }.toString(),

      """
      class Fields {
        int[] a = test.Foo.a();

        int[] b = test.Foo.b;
      }
      """
    );
  }

}