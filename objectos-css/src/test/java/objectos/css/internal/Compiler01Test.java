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

import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import org.testng.annotations.Test;

public class Compiler01Test {

  @Test(description = """
  html {}
  """)
  public void testCase01() {
    Compiler01 compiler;
    compiler = new Compiler01();

    compiler.compilationStart();

    compiler.styleRuleStart();
    compiler.styleRuleElement(StandardName.html);
    compiler.styleRuleEnd();

    compiler.compilationEnd();

    test(
      compiler,

      ByteProto.STYLE_RULE,
      Bytes.idx0(15), Bytes.idx1(15), Bytes.idx2(15),
      ByteProto.STANDARD_NAME,
      Bytes.name0(StandardName.html),
      Bytes.name1(StandardName.html),
      ByteProto.STYLE_RULE_END,
      Bytes.idx0(0), Bytes.idx1(0), Bytes.idx2(0),
      Bytes.idx0(0), Bytes.idx1(0), Bytes.idx2(0),
      ByteProto.STYLE_RULE,
      ByteProto.ROOT,
      ByteProto.STYLE_RULE, Bytes.idx0(0), Bytes.idx1(0), Bytes.idx2(0),
      ByteProto.ROOT_END, Bytes.idx0(15), Bytes.idx1(15), Bytes.idx2(15)
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

    compiler.compilationStart();

    compiler.declarationStart(Property.BOX_SIZING);
    compiler.declarationValue(StandardName.borderBox);
    compiler.declarationEnd();

    compiler.styleRuleStart();
    compiler.styleRuleElement(StandardName.any);
    compiler.styleRuleElement(InternalInstruction.DECLARATION);
    compiler.styleRuleEnd();

    compiler.compilationEnd();

    test(
      compiler,

      ByteProto.MARKED,
      Bytes.idx0(17), Bytes.idx1(17), Bytes.idx2(17),
      Bytes.prop0(Property.BOX_SIZING),
      Bytes.prop1(Property.BOX_SIZING),
      ByteProto.STANDARD_NAME,
      Bytes.name0(StandardName.borderBox),
      Bytes.name1(StandardName.borderBox),
      ByteProto.DECLARATION_END,
      Bytes.idx0(0), Bytes.idx1(0), Bytes.idx2(0),
      Bytes.idx0(0), Bytes.idx1(0), Bytes.idx2(0),
      ByteProto.DECLARATION,

      ByteProto.STYLE_RULE,
      Bytes.idx0(36), Bytes.idx1(36), Bytes.idx2(36),
      ByteProto.STANDARD_NAME,
      Bytes.name0(StandardName.any),
      Bytes.name1(StandardName.any),
      ByteProto.DECLARATION,
      Bytes.idx0(0), Bytes.idx1(0), Bytes.idx2(0),
      ByteProto.STYLE_RULE_END,
      Bytes.idx0(0), Bytes.idx1(0), Bytes.idx2(0),
      Bytes.idx0(17), Bytes.idx1(17), Bytes.idx2(17),
      ByteProto.STYLE_RULE,

      ByteProto.ROOT,
      ByteProto.STYLE_RULE, Bytes.idx0(17), Bytes.idx1(17), Bytes.idx2(17),
      ByteProto.ROOT_END, Bytes.idx0(36), Bytes.idx1(36), Bytes.idx2(36)
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

    compiler.compilationStart();

    assertEquals(compiler.mainIndex, 0);

    compiler.length(20, LengthUnit.PX);

    assertEquals(compiler.mainIndex, 6);

    compiler.length(1.5, LengthUnit.REM);

    assertEquals(compiler.mainIndex, 6 + 10);

    compiler.declarationStart(Property.MARGIN);

    assertEquals(compiler.mainContents, 6 + 10);

    compiler.declarationValue(InternalInstruction.LENGTH_INT);

    assertEquals(compiler.mainContents, 10);

    compiler.declarationValue(InternalInstruction.LENGTH_DOUBLE);

    assertEquals(compiler.mainContents, 0);

    compiler.declarationEnd();

    compiler.styleRuleStart();
    compiler.styleRuleElement(StandardName.ul);
    compiler.styleRuleElement(InternalInstruction.DECLARATION);
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
      Bytes.idx0(38), Bytes.idx1(38), Bytes.idx2(38),
      Bytes.prop0(Property.MARGIN),
      Bytes.prop1(Property.MARGIN),
      ByteProto.LENGTH_INT,
      Bytes.idx0(0), Bytes.idx1(0), Bytes.idx2(0),
      ByteProto.LENGTH_DOUBLE,
      Bytes.idx0(6), Bytes.idx1(6), Bytes.idx2(6),
      ByteProto.DECLARATION_END,
      Bytes.idx0(0), Bytes.idx1(0), Bytes.idx2(0),
      Bytes.idx0(16), Bytes.idx1(16), Bytes.idx2(16),
      ByteProto.DECLARATION,

      ByteProto.STYLE_RULE,
      Bytes.idx0(57), Bytes.idx1(57), Bytes.idx2(57),
      ByteProto.STANDARD_NAME,
      Bytes.name0(StandardName.ul),
      Bytes.name1(StandardName.ul),
      ByteProto.DECLARATION,
      Bytes.idx0(16), Bytes.idx1(16), Bytes.idx2(16),
      ByteProto.STYLE_RULE_END,
      Bytes.idx0(0), Bytes.idx1(0), Bytes.idx2(0),
      Bytes.idx0(38), Bytes.idx1(38), Bytes.idx2(38),
      ByteProto.STYLE_RULE,

      ByteProto.ROOT,
      ByteProto.STYLE_RULE, Bytes.idx0(38), Bytes.idx1(38), Bytes.idx2(38),
      ByteProto.ROOT_END, Bytes.idx0(57), Bytes.idx1(57), Bytes.idx2(57)
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

    compiler.compilationStart();

    compiler.declarationStart(Property.LINE_HEIGHT);
    compiler.javaDouble(1.5);
    compiler.declarationEnd();

    compiler.styleRuleStart();
    compiler.styleRuleElement(StandardName.p);
    compiler.styleRuleElement(InternalInstruction.DECLARATION);
    compiler.styleRuleEnd();

    compiler.compilationEnd();

    long dbl;
    dbl = Double.doubleToLongBits(1.5);

    test(
      compiler,

      ByteProto.MARKED,
      Bytes.idx0(23), Bytes.idx1(23), Bytes.idx2(23),
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
      Bytes.idx0(0), Bytes.idx1(0), Bytes.idx2(0),
      Bytes.idx0(0), Bytes.idx1(0), Bytes.idx2(0),
      ByteProto.DECLARATION,

      ByteProto.STYLE_RULE,
      Bytes.idx0(42), Bytes.idx1(42), Bytes.idx2(42),
      ByteProto.STANDARD_NAME,
      Bytes.name0(StandardName.p),
      Bytes.name1(StandardName.p),
      ByteProto.DECLARATION,
      Bytes.idx0(0), Bytes.idx1(0), Bytes.idx2(0),
      ByteProto.STYLE_RULE_END,
      Bytes.idx0(0), Bytes.idx1(0), Bytes.idx2(0),
      Bytes.idx0(23), Bytes.idx1(23), Bytes.idx2(23),
      ByteProto.STYLE_RULE,

      ByteProto.ROOT,
      ByteProto.STYLE_RULE, Bytes.idx0(23), Bytes.idx1(23), Bytes.idx2(23),
      ByteProto.ROOT_END, Bytes.idx0(42), Bytes.idx1(42), Bytes.idx2(42)
    );
  }

  @Test(description = """
  TC 05: double value

  [hidden] {}
  """)
  public void testCase06() {
    Compiler01 compiler;
    compiler = new Compiler01();

    compiler.compilationStart();

    compiler.selectorAttribute("hidden");

    compiler.styleRuleStart();
    compiler.styleRuleElement(InternalInstruction.SELECTOR_ATTR);
    compiler.styleRuleEnd();

    compiler.compilationEnd();

    test(
      compiler,

      ByteProto.MARKED,
      Bytes.two0(0),
      Bytes.two1(0),

      ByteProto.STYLE_RULE,
      Bytes.idx0(18), Bytes.idx1(18), Bytes.idx2(18),
      ByteProto.SELECTOR_ATTR,
      Bytes.two0(0),
      Bytes.two1(0),
      ByteProto.STYLE_RULE_END,
      Bytes.idx0(0), Bytes.idx1(0), Bytes.idx2(0),
      Bytes.idx0(3), Bytes.idx1(3), Bytes.idx2(3),
      ByteProto.STYLE_RULE,

      ByteProto.ROOT,
      ByteProto.STYLE_RULE, Bytes.idx0(3), Bytes.idx1(3), Bytes.idx2(3),
      ByteProto.ROOT_END, Bytes.idx0(18), Bytes.idx1(18), Bytes.idx2(18)
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
