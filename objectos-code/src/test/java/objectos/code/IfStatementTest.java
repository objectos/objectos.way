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

public class IfStatementTest {

  @Test(description = """
  The if statement TC01

  - IfThenStatement production
  """)
  public void testCase01() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          classDeclaration(
            name("IfStatement"),

            method(
              p(IF, arg(v("condition")), block(
                p(v("foo"))
              ))
            )
          );
        }
      }.toString(),

      """
      class IfStatement {
        void unnamed() {
          if (condition()) {
            foo();
          }
        }
      }
      """
    );
  }

  @Test(description = """
  The if statement TC02

  - The _else() instruction
  """)
  public void testCase02() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          classDeclaration(
            name("IfStatement"),

            method(
              p(IF, arg(v("condition")), block(
                p(v("ifTrue"))
              ), ELSE, block(
                p(v("ifFalse"))
              ))
            )
          );
        }
      }.toString(),

      """
      class IfStatement {
        void unnamed() {
          if (condition()) {
            ifTrue();
          } else {
            ifFalse();
          }
        }
      }
      """
    );
  }

  @Test(description = """
  The if statement TC03

  - statement after if block
  """)
  public void testCase03() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          classDeclaration(
            name("IfStatement"),

            method(
              p(IF, arg(v("condition")), block(
                p(v("ifTrue"))
              )),
              p(RETURN, n("foo"))
            )
          );
        }
      }.toString(),

      """
      class IfStatement {
        void unnamed() {
          if (condition()) {
            ifTrue();
          }
          return foo;
        }
      }
      """
    );
  }

}