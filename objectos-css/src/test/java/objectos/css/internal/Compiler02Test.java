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

    compiler.compilationBegin();

    compiler.styleRuleBegin();
    compiler.styleRuleElement(StandardTypeSelector.html);
    compiler.styleRuleEnd();

    compiler.compilationEnd();

    compiler.optimize();

    CompiledStyleSheet result;
    result = compiler.compile();

    test(
      result,

      ByteCode.SELECTOR_TYPE,
      (byte) StandardTypeSelector.html.ordinal(),
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

    compiler.compilationBegin();

    compiler.declarationBegin(Property.BOX_SIZING);
    compiler.propertyValue(StandardName.borderBox);
    compiler.declarationEnd();

    compiler.styleRuleBegin();
    compiler.styleRuleElement(StandardName.any);
    compiler.styleRuleElement(InternalInstruction.INSTANCE);
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

    compiler.compilationBegin();

    compiler.styleRuleBegin();
    compiler.styleRuleElement(StandardTypeSelector.h1);
    compiler.styleRuleElement(StandardTypeSelector.h2);
    compiler.styleRuleElement(StandardTypeSelector.h3);
    compiler.styleRuleEnd();

    compiler.compilationEnd();

    compiler.optimize();

    CompiledStyleSheet result;
    result = compiler.compile();

    test(
      result,

      ByteCode.SELECTOR_TYPE,
      (byte) StandardTypeSelector.h1.ordinal(),
      ByteCode.COMMA,
      ByteCode.SELECTOR_TYPE,
      (byte) StandardTypeSelector.h2.ordinal(),
      ByteCode.COMMA,
      ByteCode.SELECTOR_TYPE,
      (byte) StandardTypeSelector.h3.ordinal(),
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

    compiler.compilationBegin();

    compiler.length(20, LengthUnit.PX);
    compiler.length(1.5, LengthUnit.REM);

    compiler.declarationBegin(Property.MARGIN);
    compiler.propertyValue(InternalInstruction.LENGTH_INT);
    compiler.propertyValue(InternalInstruction.LENGTH_DOUBLE);
    compiler.declarationEnd();

    compiler.styleRuleBegin();
    compiler.styleRuleElement(StandardTypeSelector.ul);
    compiler.styleRuleElement(InternalInstruction.INSTANCE);
    compiler.styleRuleEnd();

    compiler.compilationEnd();

    compiler.optimize();

    CompiledStyleSheet result;
    result = compiler.compile();

    long dbl = Double.doubleToLongBits(1.5);

    test(
      result,

      ByteCode.SELECTOR_TYPE,
      (byte) StandardTypeSelector.ul.ordinal(),
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
      Bytes.long0(dbl),
      Bytes.long1(dbl),
      Bytes.long2(dbl),
      Bytes.long3(dbl),
      Bytes.long4(dbl),
      Bytes.long5(dbl),
      Bytes.long6(dbl),
      Bytes.long7(dbl),
      Bytes.unit(LengthUnit.REM),
      ByteCode.SEMICOLON_OPTIONAL,
      ByteCode.BLOCK_END
    );
  }

  @Test(description = """
  TC 05: double value

  [hidden] {}
  """)
  public void testCase06() {
    Compiler02 compiler;
    compiler = new Compiler02();

    compiler.compilationBegin();

    compiler.selectorAttribute("hidden");

    compiler.styleRuleBegin();
    compiler.styleRuleElement(InternalInstruction.INSTANCE);
    compiler.styleRuleEnd();

    compiler.compilationEnd();

    compiler.optimize();

    CompiledStyleSheet result;
    result = compiler.compile();

    test(
      result,

      ByteCode.SELECTOR_ATTR,
      Bytes.two0(0),
      Bytes.two1(0),
      ByteCode.BLOCK_EMPTY
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