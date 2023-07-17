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

public class Compiler01Test {

  @Test(description = """
  html {}
  """)
  public void testCase01() {
    Compiler01 compiler;
    compiler = new Compiler01();

    compiler.compilationBegin();

    compiler.styleRuleBegin();
    compiler.styleRuleElement(StandardName.html);
    compiler.styleRuleEnd();

    compiler.compilationEnd();

    test(
      compiler,

      ByteProto.STYLE_RULE,
      Bytes.len0(6),
      Bytes.len1(6),
      ByteProto.STANDARD_NAME,
      Bytes.name0(StandardName.html),
      Bytes.name1(StandardName.html),
      ByteProto.STYLE_RULE_END,
      Bytes.int0(6),
      ByteProto.STYLE_RULE
    );
  }

  @Test(description = """
  * {
    box-sizing: border-box;
  }
  """)
  public void testCase02() {
    Compiler01 compiler;
    compiler = new Compiler01();

    compiler.compilationBegin();

    compiler.declarationBegin(Property.BOX_SIZING);
    compiler.propertyValue(StandardName.borderBox);
    compiler.declarationEnd();

    compiler.styleRuleBegin();
    compiler.styleRuleElement(StandardName.any);
    compiler.styleRuleElement(InternalInstruction.INSTANCE);
    compiler.styleRuleEnd();

    compiler.compilationEnd();

    test(
      compiler,

      ByteProto.MARKED,
      Bytes.len0(8),
      Bytes.len1(8),
      Bytes.prop0(Property.BOX_SIZING),
      Bytes.prop1(Property.BOX_SIZING),
      ByteProto.STANDARD_NAME,
      Bytes.name0(StandardName.borderBox),
      Bytes.name1(StandardName.borderBox),
      ByteProto.DECLARATION_END,
      Bytes.int0(8),
      ByteProto.DECLARATION,

      ByteProto.STYLE_RULE,
      Bytes.len0(8),
      Bytes.len1(8),
      ByteProto.STANDARD_NAME,
      Bytes.name0(StandardName.any),
      Bytes.name1(StandardName.any),
      ByteProto.DECLARATION,
      Bytes.int0(18),
      ByteProto.STYLE_RULE_END,
      Bytes.int0(19),
      ByteProto.STYLE_RULE
    );
  }

  @Test(description = """
  ul {
    margin: 20px 1.5rem;
  }
  """)
  public void testCase04() {
    Compiler01 compiler;
    compiler = new Compiler01();

    compiler.compilationBegin();

    compiler.length(20, LengthUnit.PX);
    compiler.length(1.5, LengthUnit.REM);

    compiler.declarationBegin(Property.MARGIN);
    compiler.propertyValue(InternalInstruction.LENGTH_INT);
    compiler.propertyValue(InternalInstruction.LENGTH_DOUBLE);
    compiler.declarationEnd();

    compiler.styleRuleBegin();
    compiler.styleRuleElement(StandardName.ul);
    compiler.styleRuleElement(InternalInstruction.INSTANCE);
    compiler.styleRuleEnd();

    compiler.compilationEnd();

    long dbl = Double.doubleToLongBits(1.5);

    test(
      compiler,

      ByteProto.MARKED6,
      Bytes.int0(20),
      Bytes.int1(20),
      Bytes.int2(20),
      Bytes.int3(20),
      Bytes.unit(LengthUnit.PX),

      ByteProto.MARKED10,
      Bytes.long0(dbl),
      Bytes.long1(dbl),
      Bytes.long2(dbl),
      Bytes.long3(dbl),
      Bytes.long4(dbl),
      Bytes.long5(dbl),
      Bytes.long6(dbl),
      Bytes.long7(dbl),
      Bytes.unit(LengthUnit.REM),

      ByteProto.MARKED,
      Bytes.len0(13),
      Bytes.len1(13),
      Bytes.prop0(Property.MARGIN),
      Bytes.prop1(Property.MARGIN),
      ByteProto.LENGTH_INT,
      Bytes.idx0(0), Bytes.idx1(0), Bytes.idx2(0),
      ByteProto.LENGTH_DOUBLE,
      Bytes.idx0(6), Bytes.idx1(6), Bytes.idx2(6),
      ByteProto.DECLARATION_END,
      Bytes.int0(29),
      ByteProto.DECLARATION,

      ByteProto.STYLE_RULE,
      Bytes.len0(8),
      Bytes.len1(8),
      ByteProto.STANDARD_NAME,
      Bytes.name0(StandardName.ul),
      Bytes.name1(StandardName.ul),
      ByteProto.DECLARATION,
      Bytes.int0(23),
      ByteProto.STYLE_RULE_END,
      Bytes.int0(40),
      ByteProto.STYLE_RULE
    );
  }

  @Test(description = """
  TC 05: double value

  p {
    line-height: 1.5;
  }
  """)
  public void testCase05() {
    Compiler01 compiler;
    compiler = new Compiler01();

    compiler.compilationBegin();

    compiler.declarationBegin(Property.LINE_HEIGHT);
    compiler.javaDouble(1.5);
    compiler.declarationEnd();

    compiler.styleRuleBegin();
    compiler.styleRuleElement(StandardName.p);
    compiler.styleRuleElement(InternalInstruction.INSTANCE);
    compiler.styleRuleEnd();

    compiler.compilationEnd();

    long dbl;
    dbl = Double.doubleToLongBits(1.5);

    test(
      compiler,

      ByteProto.MARKED,
      Bytes.len0(14),
      Bytes.len1(14),
      Bytes.prop0(Property.LINE_HEIGHT),
      Bytes.prop1(Property.LINE_HEIGHT),
      ByteProto.JAVA_DOUBLE,
      Bytes.long0(dbl),
      Bytes.long1(dbl),
      Bytes.long2(dbl),
      Bytes.long3(dbl),
      Bytes.long4(dbl),
      Bytes.long5(dbl),
      Bytes.long6(dbl),
      Bytes.long7(dbl),
      ByteProto.DECLARATION_END,
      Bytes.int0(14),
      ByteProto.DECLARATION,

      ByteProto.STYLE_RULE,
      Bytes.len0(8),
      Bytes.len1(8),
      ByteProto.STANDARD_NAME,
      Bytes.name0(StandardName.p),
      Bytes.name1(StandardName.p),
      ByteProto.DECLARATION,
      Bytes.int0(24),
      ByteProto.STYLE_RULE_END,
      Bytes.int0(25),
      ByteProto.STYLE_RULE
    );
  }

  @Test(description = """
  TC 05: double value

  [hidden] {}
  """)
  public void testCase06() {
    Compiler01 compiler;
    compiler = new Compiler01();

    compiler.compilationBegin();

    compiler.selectorAttribute("hidden");

    compiler.styleRuleBegin();
    compiler.styleRuleElement(InternalInstruction.INSTANCE);
    compiler.styleRuleEnd();

    compiler.compilationEnd();

    test(
      compiler,

      ByteProto.MARKED4,
      Bytes.two0(0),
      Bytes.two1(0),
      ByteProto.INTERNAL4,

      ByteProto.STYLE_RULE,
      Bytes.len0(6),
      Bytes.len1(6),
      ByteProto.SELECTOR_ATTR,
      Bytes.two0(0),
      Bytes.two1(0),
      ByteProto.STYLE_RULE_END,
      Bytes.int0(10),
      ByteProto.STYLE_RULE
    );
  }

  private void test(Compiler01 compiler, byte... expected) {
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
