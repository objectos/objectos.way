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

import org.testng.annotations.Test;

public class FieldAccessExpressionTest {

  @Test(enabled = false, description = """
  Field Access Expressions TC01

  - Primary . Identifier
  """)
  public void testCase01() {
    assertEquals(
      new JavaTemplate() {
        static final ClassTypeName FOO = classType("test", "Foo");

        @Override
        protected final void definition() {
          _class("FieldAccess");
          body(
            method(
              _this().n("x"), gets(), n("y"),

              v("x").v("y").n("z"), gets(), n("foo"),

              n("comparator"), gets(), s("abc").n("CASE_INSENSITIVE_ORDER"),

              n("a"), gets(), NEW, t(FOO).n("field"),

              n("b"), gets(), n("array").dim(i(0)).n("field")
            )
          );
        }
      }.toString(),

      """
      class FieldAccess {
        void method() {
          this.x = y;
          x().y().z = foo;
          comparator = "abc".CASE_INSENSITIVE_ORDER;
          a = new test.Foo().field;
          b = array[0].field;
        }
      }
      """
    );
  }

}