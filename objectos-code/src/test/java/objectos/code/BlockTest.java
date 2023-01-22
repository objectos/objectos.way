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

import objectos.code.Fixture.Kind;
import org.testng.annotations.Test;

public class BlockTest {

  private final Fixture fix = new Fixture("Blocks", Kind.VOID_METHOD);

  @Test(enabled = false, description = """
  Blocks TC01

  - empty
  """)
  public void testCase01() {
    assertEquals(
      fix.ture(new JavaTemplate() {
        @Override
        protected final void definition() {
          block();
        }
      }),

      """
      class Blocks {
        void method() {
          {}
        }
      }
      """
    );
  }

  @Test(description = """
  Blocks TC02

  - statements only
  """)
  public void testCase02() {
    //    assertEquals(
    //      fix.ture(new JavaTemplate() {
    //        @Override
    //        protected final void definition() {
    //          block(
    //            invoke("a")
    //          );
    //          block(
    //            invoke("a"),
    //            invoke("b")
    //          );
    //        }
    //      }),
    //
    //      """
    //      class Blocks {
    //        void method() {
    //          {
    //            a();
    //          }
    //          {
    //            a();
    //            b();
    //          }
    //        }
    //      }
    //      """
    //    );
  }

}