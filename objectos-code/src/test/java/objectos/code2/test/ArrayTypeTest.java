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

import objectos.code2.JavaTemplate;
import org.testng.annotations.Test;

public class ArrayTypeTest {

  @Test(enabled = false, description = """
  Array types TC01

  - reference types
  """)
  public void testCase01() {
    // @formatter:off
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _class("Arrays"); body(
            t(t(Object.class), dim()), id("a"),

            t(t(Object.class), dim(), dim()), id("b"),

            t(t(Object.class), dim(), dim(), dim()), id("c")
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
    // @formatter:on
  }

  @Test(enabled = false, description = """
  Array types TC02

  - primitive types
  """)
  public void testCase02() {
    // @formatter:off
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _class("Arrays"); body(
            t(_int(), dim()), id("a"),

            t(_double(), dim(), dim()), id("b"),

            t(_boolean(), dim(), dim(), dim()), id("c")
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
    // @formatter:on
  }

}