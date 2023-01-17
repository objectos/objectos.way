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

public class ArrayTypeTest {

  @Test(description = """
  Array types TC01

  - reference types
  """)
  public void testCase01() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _class("Arrays");
          body(
            t(t(Object.class), dim()), field("a"),

            t(t(Object.class), dim(), dim()), field("b"),

            t(t(Object.class), dim(), dim(), dim()), field("c")
          );
        }
      }.toString(),

      """
      class Arrays {
        java.lang.Object[] a;

        java.lang.Object[][] b;

        java.lang.Object[][][] c;
      }
      """
    );
  }

  @Test(description = """
  Array types TC02

  - primitive types
  """)
  public void testCase02() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _class("Arrays");
          body(
            t(_int(), dim()), field("a"),

            t(_double(), dim(), dim()), field("b"),

            t(_boolean(), dim(), dim(), dim()), field("c")
          );
        }
      }.toString(),

      """
      class Arrays {
        int[] a;

        double[][] b;

        boolean[][][] c;
      }
      """
    );
  }

}