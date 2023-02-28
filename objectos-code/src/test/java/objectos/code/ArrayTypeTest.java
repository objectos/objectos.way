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

import objectos.code.type.ArrayTypeName;
import objectos.code.type.ClassTypeName;
import org.testng.annotations.Test;

public class ArrayTypeTest {

  @Test(description = """
  Array types TC01

  - reference types
  """)
  public void testCase01() {
    assertEquals(
      new JavaTemplate() {
        static final ClassTypeName OBJECT = ClassTypeName.of(Object.class);

        static final ArrayTypeName OBJECT1 = ArrayTypeName.of(OBJECT, 1);
        static final ArrayTypeName OBJECT2 = ArrayTypeName.of(OBJECT, 2);
        static final ArrayTypeName OBJECT3 = ArrayTypeName.of(OBJECT, 3);

        @Override
        protected final void definition() {
          classDeclaration(
            name("Arrays"),

            field(OBJECT1, name("a")),
            field(OBJECT2, name("b")),
            field(OBJECT3, name("c"))
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
        static final ArrayTypeName INT1 = ArrayTypeName.of(INT, 1);
        static final ArrayTypeName DOUBLE2 = ArrayTypeName.of(DOUBLE, 2);
        static final ArrayTypeName BOOL3 = ArrayTypeName.of(BOOLEAN, 3);

        @Override
        protected final void definition() {
          classDeclaration(
            name("Arrays"),

            field(INT1, name("a")),
            field(DOUBLE2, name("b")),
            field(BOOL3, name("c"))
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