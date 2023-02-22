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

public class EqualityOperatorTest {

  @Test(description = """
  Equality operators TC01

  - equalTo
  """)
  public void testCase01() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _class("EqualityOperator");
          body(
            method(
              p(v("foo"), arg(n("x"), EQ, n("y")))
            )
          );
        }
      }.toString(),

      """
      class EqualityOperator {
        void unnamed() {
          foo(x == y);
        }
      }
      """
    );
  }

  @Test(description = """
  Equality operators TC02

  - notEqualTo
  """)
  public void testCase02() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _class("EqualityOperator");
          body(
            method(
              p(v("foo"), arg(n("x"), NE, n("y")))
            )
          );
        }
      }.toString(),

      """
      class EqualityOperator {
        void unnamed() {
          foo(x != y);
        }
      }
      """
    );
  }

}
