/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.html.internal;

import java.util.Arrays;
import objectos.html.tmpl.StandardElementName;
import org.testng.annotations.Test;

public class HtmlCompiler01Test {

  @Test
  public void testCase00() {
    HtmlCompiler01 compiler;
    compiler = new HtmlCompiler01();

    compiler.compilationBegin();

    compiler.elementBegin(StandardElementName.HTML);
    compiler.elementEnd();

    compiler.compilationEnd();

    test(
      compiler,

      ByteProto2.ELEMENT,
      Bytes.encodeInt0(5),
      Bytes.encodeInt1(5),
      ByteProto2.ELEMENT_STANDARD,
      (byte) StandardElementName.HTML.ordinal(),
      ByteProto2.END,
      Bytes.encodeInt0(5),
      ByteProto2.INTERNAL
    );
  }

  private void test(HtmlCompiler01 compiler, byte... expected) {
    byte[] result;
    result = Arrays.copyOf(compiler.main, compiler.mainIndex);

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