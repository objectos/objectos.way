/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.css.internal;

import java.util.Arrays;
import org.testng.annotations.Test;

public class Compiler02Test {

  @Test(description = """
  html {}
  """)
  public void testCase01() {
    Compiler02 compiler;
    compiler = new Compiler02();

    compiler.compilationStart();

    // label { \{previous} }
    compiler.styleRuleStart();
    compiler.styleRuleElement(StandardName.html);
    compiler.styleRuleEnd();

    compiler.compilationEnd();

    compiler.optimize();

    CompiledStyleSheet result;
    result = compiler.compile();

    test(
      result,

      ByteCode.SELECTOR,
      Bytes.name0(StandardName.html),
      Bytes.name1(StandardName.html),
      ByteCode.BLOCK_START,
      ByteCode.BLOCK_END
    );
  }

  private void test(CompiledStyleSheet sheet, byte... expected) {
    byte[] result;
    result = sheet.main;

    if (result.length != expected.length) {
      throw new AssertionError(
        """
        Arrays don't have the same size.

        Actual  : %s
        Expected: %s
        """.formatted(Arrays.toString(result), Arrays.toString(expected))
      );
    }

    if (!Arrays.equals(result, expected)) {
      throw new AssertionError(
        """
        Arrays don't have the same content.

        Actual  : %s
        Expected: %s
        """.formatted(Arrays.toString(result), Arrays.toString(expected))
      );
    }
  }

}