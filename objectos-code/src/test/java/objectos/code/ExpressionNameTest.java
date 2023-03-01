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

public class ExpressionNameTest {

  @Test(description = """
  Expression name TC01:

  - identifiers
  """)
  public void testCase01() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          classDeclaration(
            name("ExpressionName"),
            method(
              p(v("test"), argument(n("a"))),

              p(v("test"), argument(n("a"), n("b"))),

              p(v("test"), argument(n("a"), n("b"), n("c")))
            )
          );
        }
      }.toString(),

      """
      class ExpressionName {
        void unnamed() {
          test(a);
          test(a.b);
          test(a.b.c);
        }
      }
      """
    );
  }

  @Test(description = """
  Expression name TC02:

  - base = ClassName
  """)
  public void testCase02() {
    assertEquals(
      new JavaTemplate() {
        static final ClassTypeName SUIT = ClassTypeName.of("test", "Suit");

        @Override
        protected final void definition() {
          classDeclaration(
            name("ExpressionName"),
            method(
              p(v("test"), argument(SUIT, n("CLUBS"))),

              p(v("test"), argument(SUIT, n("CLUBS"), n("field")))
            )
          );
        }
      }.toString(),

      """
      class ExpressionName {
        void unnamed() {
          test(test.Suit.CLUBS);
          test(test.Suit.CLUBS.field);
        }
      }
      """
    );
  }

}