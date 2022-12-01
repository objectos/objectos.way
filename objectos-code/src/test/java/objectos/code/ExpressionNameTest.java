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
          invoke("test", n("a"));

          invoke("test", n("a", "b"));

          invoke("test", n("a", "b", "c"));
        }
      }.toString(),

      """
      test(a);

      test(a.b);

      test(a.b.c);
      """
    );
  }

  @Test(description = """
  Expression name TC01:

  - base = ClassName
  """)
  public void testCase02() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          invoke("test", n(t("test", "Suit"), "CLUBS"));

          invoke("test", n(t("test", "Suit"), "CLUBS", "field"));
        }
      }.toString(),

      """
      test(test.Suit.CLUBS);

      test(test.Suit.CLUBS.field);
      """
    );
  }

}