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

public class EnumDeclarationTest {

  @Test(description = """
  constants
  """)
  public void testCase01() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _enum(id("Subject"),
            enumConstant(id("ONE"))
          );
        }
      }.toString(),

      """
      enum Subject {
        ONE;
      }
      """
    );

    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _enum(id("Subject"),
            enumConstant(id("ONE")),
            enumConstant(id("TWO"))
          );
        }
      }.toString(),

      """
      enum Subject {
        ONE,

        TWO;
      }
      """
    );

    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _enum(id("Subject"),
            enumConstant(id("ONE")),
            enumConstant(id("TWO")),
            enumConstant(id("THREE"))
          );
        }
      }.toString(),

      """
      enum Subject {
        ONE,

        TWO,

        THREE;
      }
      """
    );
  }

  @Test(description = """
  constants + args
  """)
  public void testCase02() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _enum(id("Test"),
            enumConstant(id("ONE"), s("abc"))
          );
        }
      }.toString(),

      """
      enum Test {
        ONE("abc");
      }
      """
    );

    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _enum(id("Test"),
            enumConstant(id("ONE"), s("abc"), n("field"))
          );
        }
      }.toString(),

      """
      enum Test {
        ONE("abc", field);
      }
      """
    );
  }

}