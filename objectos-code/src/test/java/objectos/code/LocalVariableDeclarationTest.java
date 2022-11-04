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

import org.testng.annotations.Test;

public class LocalVariableDeclarationTest extends AbstractObjectosCodeTest {

  @Test(description = """
  var s = "java";
  """)
  public void testCase01() {
    var tmpl = new JavaTemplate() {
      @Override
      protected final void definition() {
        var("s", s("java"));
      }
    };

    testDefault(
      tmpl,

      """
      var s = "java";
      """
    );
  }

  @Test(description = """
  var s = m("java");
  """)
  public void testCase02() {
    var tmpl = new JavaTemplate() {
      @Override
      protected final void definition() {
        var("s", invoke("m", s("java")));
      }
    };

    testDefault(
      tmpl,

      """
      var s = m("java");
      """
    );
  }

}