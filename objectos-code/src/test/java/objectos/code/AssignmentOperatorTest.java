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

import objectos.code.type.ClassTypeName;
import org.testng.annotations.Test;

public class AssignmentOperatorTest {

  @Test(description = """
  Assignment Operators TC01

  - implicit operator (default) should be the simple one
  """)
  public void testCase01() {
    assertEquals(
      new JavaTemplate() {
        static final ClassTypeName FOO = ClassTypeName.of("test", "Foo");

        @Override
        protected final void definition() {
          classDeclaration(
            name("Assign"),

            method(
              p(n("a"), IS, n("x")),

              p(n("a"), IS, n("x"), n("y")),

              p(n("a"), IS, v("x"), v("y")),

              p(n("a"), IS, NEW, FOO)
            )
          );
        }
      }.toString(),

      """
      class Assign {
        void unnamed() {
          a = x;
          a = x.y;
          a = x().y();
          a = new test.Foo();
        }
      }
      """
    );
  }

}