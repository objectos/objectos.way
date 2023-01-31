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

import objectos.code.Fixture.Kind;
import org.testng.annotations.Test;

public class ArrayAccessExpressionTest {

  private final Fixture fix = new Fixture("ArrayAccess", Kind.VOID_METHOD);

  @Test(description = """
  variable arity test
  """)
  public void testCase01() {
    assertEquals(
      fix.ture(new JavaTemplate() {
        @Override
        protected final void definition() {
          invoke("foo",
            n("a"), dim(n("x")),
            n("a"), dim(n("x")), dim(n("y")),
            n("a"), dim(n("x")), dim(n("y")), dim(n("z"))
          );
        }
      }),

      """
      class ArrayAccess {
        void method() {
          foo(a[x], a[x][y], a[x][y][z]);
        }
      }
      """
    );
  }

}