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

import java.util.Map;
import org.testng.annotations.Test;

public class FieldDeclarationTest {

  @Test(description = """
  typeName + names
  """)
  public void testCase01() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          field(t(String.class), id("name"));
        }
      }.toString(),

      """
      java.lang.String name;
      """
    );

    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          field(t(String.class), id("a"), id("b"), id("c"));
        }
      }.toString(),

      """
      java.lang.String a, b, c;
      """
    );
  }

  @Test(description = """
  modifiers
  """)
  public void testCase02() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          field(_private(), t(String.class), id("a"));
        }
      }.toString(),

      """
      private java.lang.String a;
      """
    );

    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          field(_private(), _final(), t(String.class), id("a"));
        }
      }.toString(),

      """
      private final java.lang.String a;
      """
    );

    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          field(_private(), _static(), _final(), t(String.class), id("a"));
        }
      }.toString(),

      """
      private static final java.lang.String a;
      """
    );
  }

  @Test(description = """
  init expression
  """)
  public void testCase03() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          field(t(String.class), id("a"), s("init"));
        }
      }.toString(),

      """
      java.lang.String a = "init";
      """
    );

    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          field(t(String.class), id("a"), s("a"), id("b"), id("c"), s("c"));
        }
      }.toString(),

      """
      java.lang.String a = "a", b, c = "c";
      """
    );
  }

  @Test(description = """
  Field declarations TC05

  - array type
  """)
  public void testCase04() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          field(t(t(Object.class), dim()), id("a"));
        }
      }.toString(),

      """
      java.lang.Object[] a;
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
        @Override
        protected final void definition() {
          field(t(t(Map.class), t(String.class), t(Integer.class)), id("map"));
        }
      }.toString(),

      """
      java.util.Map<java.lang.String, java.lang.Integer> map;
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
          field(t(_int(), dim()), id("a"), a());
          field(t(_int(), dim()), id("b"), a(i(0)));
          field(t(_int(), dim()), id("c"), a(i(0), i(1)));
        }
      }.toString(),

      """
      int[] a = {};

      int[] b = {0};

      int[] c = {0, 1};
      """
    );
  }

}