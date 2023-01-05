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
import objectos.code2.test.Fixture.Kind;
import org.testng.annotations.Test;

public class ExpressionNameTest {

  private final Fixture fix = new Fixture("ExprName", Kind.VOID_METHOD);

  @Test(enabled = false, description = """
  Expression name TC01:

  - identifiers
  """)
  public void testCase01() {
    assertEquals(
      fix.ture(new JavaTemplate() {
        @Override
        protected final void definition() {
          invoke("test", n("a"));
          invoke("test", n("a", "b"));
          invoke("test", n("a", "b", "c"));
        }
      }),

      """
      class ExprName {
        void method() {
          test(a);
          test(a.b);
          test(a.b.c);
        }
      }
      """
    );
  }

  @Test(enabled = false, description = """
  Expression name TC02:

  - base = ClassName
  """)
  public void testCase02() {
    assertEquals(
      fix.ture(new JavaTemplate() {
        @Override
        protected final void definition() {
          invoke("test", n(t("test", "Suit"), "CLUBS"));
          invoke("test", n(t("test", "Suit"), "CLUBS", "field"));
        }
      }),

      """
      class ExprName {
        void method() {
          test(test.Suit.CLUBS);
          test(test.Suit.CLUBS.field);
        }
      }
      """
    );
  }

}