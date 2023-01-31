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

public class LocalVariableDeclarationTest {

  private final Fixture fix = new Fixture("LocalVar", Kind.VOID_METHOD);

  @Test(description = """
  var s = "java";
  """)
  public void testCase01() {
    assertEquals(
      fix.ture(new JavaTemplate() {
        @Override
        protected final void definition() {
          code(
            _var(), id("a"), s("java"),
            _var(), id("b"), invoke("m", s("java")),
            _int(), id("c"), i(0),
            t(String.class), id("d"), s("d")
          );
        }
      }),

      """
      class LocalVar {
        void method() {
          var a = "java";
          var b = m("java");
          int c = 0;
          java.lang.String d = "d";
        }
      }
      """
    );
  }

}