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
      ByteCode.BLOCK_EMPTY
    );
  }

  @Test(description = """
  * {
    box-sizing: border-box;
  }
  """)
  public void testCase02() {
    Compiler02 compiler;
    compiler = new Compiler02();

    compiler.compilationStart();

    compiler.declarationStart(Property.BOX_SIZING);
    compiler.declarationValue(StandardName.borderBox);
    compiler.declarationEnd();

    compiler.styleRuleStart();
    compiler.styleRuleElement(StandardName.any);
    compiler.styleRuleElement(InternalInstruction.DECLARATION);
    compiler.styleRuleEnd();

    compiler.compilationEnd();

    compiler.optimize();

    CompiledStyleSheet result;
    result = compiler.compile();

    test(
      result,

      ByteCode.SELECTOR,
      Bytes.name0(StandardName.any),
      Bytes.name1(StandardName.any),
      ByteCode.BLOCK_START,
      ByteCode.TAB, (byte) 1,
      ByteCode.PROPERTY_NAME,
      Bytes.prop0(Property.BOX_SIZING),
      Bytes.prop1(Property.BOX_SIZING),
      ByteCode.SPACE_OPTIONAL,
      ByteCode.KEYWORD,
      Bytes.name0(StandardName.borderBox),
      Bytes.name1(StandardName.borderBox),
      ByteCode.SEMICOLON_OPTIONAL,
      ByteCode.BLOCK_END
    );
  }

  @Test(description = """
  h1,h2,h3 {}
  """)
  public void testCase03() {
    Compiler02 compiler;
    compiler = new Compiler02();

    compiler.compilationStart();

    compiler.styleRuleStart();
    compiler.styleRuleElement(StandardName.h1);
    compiler.styleRuleElement(StandardName.h2);
    compiler.styleRuleElement(StandardName.h3);
    compiler.styleRuleEnd();

    compiler.compilationEnd();

    compiler.optimize();

    CompiledStyleSheet result;
    result = compiler.compile();

    test(
      result,

      ByteCode.SELECTOR,
      Bytes.name0(StandardName.h1),
      Bytes.name1(StandardName.h1),
      ByteCode.COMMA,
      ByteCode.SELECTOR,
      Bytes.name0(StandardName.h2),
      Bytes.name1(StandardName.h2),
      ByteCode.COMMA,
      ByteCode.SELECTOR,
      Bytes.name0(StandardName.h3),
      Bytes.name1(StandardName.h3),
      ByteCode.BLOCK_EMPTY
    );
  }

  @Test(description = """
  ul {
    margin: 20px 1.5rem;
  }
  """)
  public void testCase04() {
    Compiler02 compiler;
    compiler = new Compiler02();

    compiler.compilationStart();

    compiler.length(20, LengthUnit.PX);
    compiler.length(1.5, LengthUnit.REM);

    compiler.declarationStart(Property.MARGIN);
    compiler.declarationValue(InternalInstruction.LENGTH_INT);
    compiler.declarationValue(InternalInstruction.LENGTH_DOUBLE);
    compiler.declarationEnd();

    compiler.styleRuleStart();
    compiler.styleRuleElement(StandardName.ul);
    compiler.styleRuleElement(InternalInstruction.DECLARATION);
    compiler.styleRuleEnd();

    compiler.compilationEnd();

    compiler.optimize();

    CompiledStyleSheet result;
    result = compiler.compile();

    long dbl = Double.doubleToLongBits(1.5);

    test(
      result,

      ByteCode.SELECTOR,
      Bytes.name0(StandardName.ul),
      Bytes.name1(StandardName.ul),
      ByteCode.BLOCK_START,
      ByteCode.TAB, (byte) 1,
      ByteCode.PROPERTY_NAME,
      Bytes.prop0(Property.MARGIN),
      Bytes.prop1(Property.MARGIN),
      ByteCode.SPACE_OPTIONAL,
      ByteCode.LENGTH_INT,
      Bytes.int0(20),
      Bytes.int1(20),
      Bytes.int2(20),
      Bytes.int3(20),
      Bytes.unit(LengthUnit.PX),
      ByteCode.SPACE,
      ByteCode.LENGTH_DOUBLE,
      Bytes.lng0(dbl),
      Bytes.lng1(dbl),
      Bytes.lng2(dbl),
      Bytes.lng3(dbl),
      Bytes.lng4(dbl),
      Bytes.lng5(dbl),
      Bytes.lng6(dbl),
      Bytes.lng7(dbl),
      Bytes.unit(LengthUnit.REM),
      ByteCode.SEMICOLON_OPTIONAL,
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