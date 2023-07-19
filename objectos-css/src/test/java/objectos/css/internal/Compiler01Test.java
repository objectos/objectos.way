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
    compiler.styleRuleElement(StandardTypeSelector.html);
    compiler.styleRuleEnd();

    compiler.compilationEnd();

    test(
      compiler,

      ByteProto.STYLE_RULE,
      Bytes.len0(5),
      Bytes.len1(5),
      ByteProto.SELECTOR_TYPE,
      (byte) StandardTypeSelector.html.ordinal(),
      ByteProto.STYLE_RULE_END,
      Bytes.int0(5),
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
    compiler.styleRuleElement(StandardTypeSelector.ul);
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
      Bytes.len0(9),
      Bytes.len1(9),
      Bytes.prop0(Property.MARGIN),
      Bytes.prop1(Property.MARGIN),
      ByteProto.LENGTH_INT,
      Bytes.int0(22),
      ByteProto.LENGTH_DOUBLE,
      Bytes.int0(18),
      ByteProto.DECLARATION_END,
      Bytes.int0(25),
      ByteProto.DECLARATION,

      ByteProto.STYLE_RULE,
      Bytes.len0(7),
      Bytes.len1(7),
      ByteProto.SELECTOR_TYPE,
      (byte) StandardTypeSelector.ul.ordinal(),
      ByteProto.DECLARATION,
      Bytes.int0(18),
      ByteProto.STYLE_RULE_END,
      Bytes.int0(35),
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
    compiler.styleRuleElement(StandardTypeSelector.p);
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
      Bytes.len0(7),
      Bytes.len1(7),
      ByteProto.SELECTOR_TYPE,
      (byte) StandardTypeSelector.p.ordinal(),
      ByteProto.DECLARATION,
      Bytes.int0(23),
      ByteProto.STYLE_RULE_END,
      Bytes.int0(24),
      ByteProto.STYLE_RULE
    );
  }

  @Test(description = """
  TC 06: selector attribute

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

  @Test(description = """
  TC 07: selector pseudo class/element
  """)
  public void testCase07() {
    Compiler01 compiler;
    compiler = new Compiler01();

    compiler.compilationBegin();

    compiler.styleRuleBegin();
    compiler.styleRuleElement(StandardPseudoClassSelector._disabled);
    compiler.styleRuleEnd();

    compiler.styleRuleBegin();
    compiler.styleRuleElement(StandardPseudoElementSelector.__after);
    compiler.styleRuleEnd();

    compiler.compilationEnd();

    test(
      compiler,

      ByteProto.STYLE_RULE,
      Bytes.len0(5),
      Bytes.len1(5),
      ByteProto.SELECTOR_PSEUDO_CLASS,
      (byte) StandardPseudoClassSelector._disabled.ordinal(),
      ByteProto.STYLE_RULE_END,
      Bytes.int0(5),
      ByteProto.STYLE_RULE,

      ByteProto.STYLE_RULE,
      Bytes.len0(5),
      Bytes.len1(5),
      ByteProto.SELECTOR_PSEUDO_ELEMENT,
      (byte) StandardPseudoElementSelector.__after.ordinal(),
      ByteProto.STYLE_RULE_END,
      Bytes.int0(5),
      ByteProto.STYLE_RULE
    );
  }

  @Test(description = """
  TC 08: selector joined
  """)
  public void testCase08() {
    Compiler01 compiler;
    compiler = new Compiler01();

    compiler.compilationBegin();

    compiler.selectorBegin();
    compiler.selectorElement(StandardTypeSelector.input);
    compiler.selectorElement(StandardPseudoElementSelector.__placeholder);
    compiler.selectorEnd();

    compiler.styleRuleBegin();
    compiler.styleRuleElement(InternalInstruction.INSTANCE);
    compiler.styleRuleEnd();

    compiler.compilationEnd();

    test(
      compiler,

      ByteProto.MARKED,
      Bytes.len0(7),
      Bytes.len1(7),
      ByteProto.SELECTOR_TYPE,
      (byte) StandardTypeSelector.input.ordinal(),
      ByteProto.SELECTOR_PSEUDO_ELEMENT,
      (byte) StandardPseudoElementSelector.__placeholder.ordinal(),
      ByteProto.SELECTOR_SEL_END,
      Bytes.int0(7),
      ByteProto.SELECTOR_SEL,

      ByteProto.STYLE_RULE,
      Bytes.len0(5),
      Bytes.len1(5),
      ByteProto.SELECTOR_SEL,
      Bytes.int0(14),
      ByteProto.STYLE_RULE_END,
      Bytes.int0(15),
      ByteProto.STYLE_RULE
    );
  }

  @Test(description = """
  ul {
    list-style-image: url("foo");
  }
  """)
  public void testCase09() {
    Compiler01 compiler;
    compiler = new Compiler01();

    compiler.compilationBegin();

    compiler.url("foo");

    compiler.declarationBegin(Property.LIST_STYLE_IMAGE);
    compiler.propertyValue(InternalInstruction.URL);
    compiler.declarationEnd();

    compiler.styleRuleBegin();
    compiler.styleRuleElement(StandardTypeSelector.ul);
    compiler.styleRuleElement(InternalInstruction.INSTANCE);
    compiler.styleRuleEnd();

    compiler.compilationEnd();

    test(
      compiler,

      ByteProto.MARKED3,
      Bytes.two0(0),
      Bytes.two1(0),

      ByteProto.MARKED,
      Bytes.len0(7),
      Bytes.len1(7),
      Bytes.prop0(Property.LIST_STYLE_IMAGE),
      Bytes.prop1(Property.LIST_STYLE_IMAGE),
      ByteProto.URL,
      Bytes.int0(9),
      ByteProto.DECLARATION_END,
      Bytes.int0(10),
      ByteProto.DECLARATION,

      ByteProto.STYLE_RULE,
      Bytes.len0(7),
      Bytes.len1(7),
      ByteProto.SELECTOR_TYPE,
      (byte) StandardTypeSelector.ul.ordinal(),
      ByteProto.DECLARATION,
      Bytes.int0(16),
      ByteProto.STYLE_RULE_END,
      Bytes.int0(20),
      ByteProto.STYLE_RULE
    );
  }

  @Test(description = """
  p {
    font-family: "Foo Bar";
  }
  """)
  public void testCase10() {
    Compiler01 compiler;
    compiler = new Compiler01();

    compiler.compilationBegin();

    compiler.stringLiteral("Foo Bar");

    compiler.declarationBegin(Property.FONT_FAMILY);
    compiler.propertyValue(InternalInstruction.STRING_LITERAL);
    compiler.declarationEnd();

    compiler.styleRuleBegin();
    compiler.styleRuleElement(StandardTypeSelector.p);
    compiler.styleRuleElement(InternalInstruction.INSTANCE);
    compiler.styleRuleEnd();

    compiler.compilationEnd();

    test(
      compiler,

      ByteProto.MARKED3,
      Bytes.two0(0),
      Bytes.two1(0),

      ByteProto.MARKED,
      Bytes.len0(7),
      Bytes.len1(7),
      Bytes.prop0(Property.FONT_FAMILY),
      Bytes.prop1(Property.FONT_FAMILY),
      ByteProto.STRING_LITERAL,
      Bytes.int0(9),
      ByteProto.DECLARATION_END,
      Bytes.int0(10),
      ByteProto.DECLARATION,

      ByteProto.STYLE_RULE,
      Bytes.len0(7),
      Bytes.len1(7),
      ByteProto.SELECTOR_TYPE,
      (byte) StandardTypeSelector.p.ordinal(),
      ByteProto.DECLARATION,
      Bytes.int0(16),
      ByteProto.STYLE_RULE_END,
      Bytes.int0(20),
      ByteProto.STYLE_RULE
    );
  }

  @Test(description = """
  p {
    font-family: "Foo Bar", sans-serif;
  }
  """)
  public void testCase11() {
    Compiler01 compiler;
    compiler = new Compiler01();

    compiler.compilationBegin();

    compiler.stringLiteral("Foo Bar");

    compiler.declarationBegin(Property.FONT_FAMILY);
    compiler.propertyValue(InternalInstruction.STRING_LITERAL);
    compiler.propertyValueComma();
    compiler.propertyValue(StandardName.sansSerif);
    compiler.declarationEnd();

    compiler.styleRuleBegin();
    compiler.styleRuleElement(StandardTypeSelector.p);
    compiler.styleRuleElement(InternalInstruction.INSTANCE);
    compiler.styleRuleEnd();

    compiler.compilationEnd();

    test(
      compiler,

      ByteProto.MARKED3,
      Bytes.two0(0),
      Bytes.two1(0),

      ByteProto.MARKED,
      Bytes.len0(11),
      Bytes.len1(11),
      Bytes.prop0(Property.FONT_FAMILY),
      Bytes.prop1(Property.FONT_FAMILY),
      ByteProto.STRING_LITERAL,
      Bytes.int0(9),
      ByteProto.COMMA,
      ByteProto.STANDARD_NAME,
      Bytes.name0(StandardName.sansSerif),
      Bytes.name1(StandardName.sansSerif),
      ByteProto.DECLARATION_END,
      Bytes.int0(14),
      ByteProto.DECLARATION,

      ByteProto.STYLE_RULE,
      Bytes.len0(7),
      Bytes.len1(7),
      ByteProto.SELECTOR_TYPE,
      (byte) StandardTypeSelector.p.ordinal(),
      ByteProto.DECLARATION,
      Bytes.int0(20),
      ByteProto.STYLE_RULE_END,
      Bytes.int0(24),
      ByteProto.STYLE_RULE
    );
  }

  @Test(description = """
  input {
    color: #9ca3af
  }
  """)
  public void testCase12() {
    Compiler01 compiler;
    compiler = new Compiler01();

    compiler.compilationBegin();

    compiler.colorHex("#9ca3af");

    compiler.declarationBegin(Property.COLOR);
    compiler.propertyValue(InternalInstruction.COLOR_HEX);
    compiler.declarationEnd();

    compiler.styleRuleBegin();
    compiler.styleRuleElement(StandardTypeSelector.input);
    compiler.styleRuleElement(InternalInstruction.INSTANCE);
    compiler.styleRuleEnd();

    compiler.compilationEnd();

    test(
      compiler,

      ByteProto.MARKED3,
      Bytes.two0(0),
      Bytes.two1(0),

      ByteProto.MARKED,
      Bytes.len0(7),
      Bytes.len1(7),
      Bytes.prop0(Property.COLOR),
      Bytes.prop1(Property.COLOR),
      ByteProto.COLOR_HEX,
      Bytes.int0(9),
      ByteProto.DECLARATION_END,
      Bytes.int0(10),
      ByteProto.DECLARATION,

      ByteProto.STYLE_RULE,
      Bytes.len0(7),
      Bytes.len1(7),
      ByteProto.SELECTOR_TYPE,
      (byte) StandardTypeSelector.input.ordinal(),
      ByteProto.DECLARATION,
      Bytes.int0(16),
      ByteProto.STYLE_RULE_END,
      Bytes.int0(20),
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
