/*
 * Copyright (C) 2021-2023 Objectos Software LTDA.
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
package objectos.asciidoc;

import static org.testng.Assert.assertEquals;

import java.util.Map;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class Pass2Test extends AsciiDocTest {

  private Pass2 pass2;

  @BeforeClass
  @Override
  public void _beforeClass() {
    if (pass2 == null) {
      pass2 = new Pass2();
    }
  }

  @Test(enabled = false)
  public void _enableCodeMinings() {
  }

  @Override
  final void test(
      String source,
      int[] p0,
      int[] p1, Map<String, String> docAttr,
      int[][] p2,
      String expectedHtml) {
    if (p2 == null) {
      return;
    }

    var s = new ArrayPass2Source(p0);

    var index = 0;

    for (int i = 0; i < p1.length; i++) {
      var code = p1[i];

      if (code != Code.TOKENS) {
        continue;
      }

      var first = p1[i + 1];
      var last = p1[i + 2];

      pass2.execute(s, first, last);

      pass2.reset();

      var result = pass2.toText();

      testArrays(result, p2[index], "Pass (2) assertion failed at index=" + index);

      index++;
    }

    assertEquals(p2.length, index);
  }

}