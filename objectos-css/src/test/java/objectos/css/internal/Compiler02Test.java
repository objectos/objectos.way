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
import objectos.css.util.ClassSelector;
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

  @Test(description = """
  TC 07: selector pseudo class/element
  """)
  public void testCase07() {
    Compiler02 compiler;
    compiler = new Compiler02();

    compiler.compilationBegin();

    compiler.styleRuleBegin();
    compiler.styleRuleElement(StandardPseudoClassSelector._disabled);
    compiler.styleRuleEnd();

    compiler.styleRuleBegin();
    compiler.styleRuleElement(StandardPseudoElementSelector.__after);
    compiler.styleRuleEnd();

    compiler.compilationEnd();

    compiler.optimize();

    CompiledStyleSheet result;
    result = compiler.compile();

    test(
      result,

      ByteCode.SELECTOR_PSEUDO_CLASS,
      (byte) StandardPseudoClassSelector._disabled.ordinal(),
      ByteCode.BLOCK_EMPTY,

      ByteCode.NEXT_RULE,

      ByteCode.SELECTOR_PSEUDO_ELEMENT,
      (byte) StandardPseudoElementSelector.__after.ordinal(),
      ByteCode.BLOCK_EMPTY
    );
  }

  @Test(description = """
  TC 08: selector joined
  """)
  public void testCase08() {
    Compiler02 compiler;
    compiler = new Compiler02();

    compiler.compilationBegin();

    compiler.selectorBegin();
    compiler.selectorElement(StandardTypeSelector.input);
    compiler.selectorElement(StandardPseudoElementSelector.__placeholder);
    compiler.selectorEnd();

    compiler.styleRuleBegin();
    compiler.styleRuleElement(InternalInstruction.INSTANCE);
    compiler.styleRuleEnd();

    compiler.compilationEnd();

    compiler.optimize();

    CompiledStyleSheet result;
    result = compiler.compile();

    test(
      result,

      ByteCode.SELECTOR_TYPE,
      (byte) StandardTypeSelector.input.ordinal(),
      ByteCode.SELECTOR_PSEUDO_ELEMENT,
      (byte) StandardPseudoElementSelector.__placeholder.ordinal(),
      ByteCode.BLOCK_EMPTY
    );
  }

  @Test(description = """
  ul {
    list-style-image: url("foo");
  }
  """)
  public void testCase09() {
    Compiler02 compiler;
    compiler = new Compiler02();

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

    compiler.optimize();

    CompiledStyleSheet result;
    result = compiler.compile();

    test(
      result,

      ByteCode.SELECTOR_TYPE,
      (byte) StandardTypeSelector.ul.ordinal(),
      ByteCode.BLOCK_START,
      ByteCode.TAB, (byte) 1,
      ByteCode.PROPERTY_NAME,
      Bytes.prop0(Property.LIST_STYLE_IMAGE),
      Bytes.prop1(Property.LIST_STYLE_IMAGE),
      ByteCode.SPACE_OPTIONAL,
      ByteCode.URL,
      Bytes.two0(0),
      Bytes.two1(0),
      ByteCode.SEMICOLON_OPTIONAL,
      ByteCode.BLOCK_END
    );
  }

  @Test(description = """
  p {
    font-family: "Foo Bar";
  }
  """)
  public void testCase10() {
    Compiler02 compiler;
    compiler = new Compiler02();

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

    compiler.optimize();

    CompiledStyleSheet result;
    result = compiler.compile();

    test(
      result,

      ByteCode.SELECTOR_TYPE,
      (byte) StandardTypeSelector.p.ordinal(),
      ByteCode.BLOCK_START,
      ByteCode.TAB, (byte) 1,
      ByteCode.PROPERTY_NAME,
      Bytes.prop0(Property.FONT_FAMILY),
      Bytes.prop1(Property.FONT_FAMILY),
      ByteCode.SPACE_OPTIONAL,
      ByteCode.STRING_LITERAL,
      Bytes.two0(0),
      Bytes.two1(0),
      ByteCode.SEMICOLON_OPTIONAL,
      ByteCode.BLOCK_END
    );
  }

  @Test(description = """
  p {
    font-family: "Foo Bar", sans-serif;
  }
  """)
  public void testCase11() {
    Compiler02 compiler;
    compiler = new Compiler02();

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

    compiler.optimize();

    CompiledStyleSheet result;
    result = compiler.compile();

    test(
      result,

      ByteCode.SELECTOR_TYPE,
      (byte) StandardTypeSelector.p.ordinal(),
      ByteCode.BLOCK_START,
      ByteCode.TAB, (byte) 1,
      ByteCode.PROPERTY_NAME,
      Bytes.prop0(Property.FONT_FAMILY),
      Bytes.prop1(Property.FONT_FAMILY),
      ByteCode.SPACE_OPTIONAL,
      ByteCode.STRING_LITERAL,
      Bytes.two0(0),
      Bytes.two1(0),
      ByteCode.COMMA,
      ByteCode.KEYWORD,
      Bytes.name0(StandardName.sansSerif),
      Bytes.name1(StandardName.sansSerif),
      ByteCode.SEMICOLON_OPTIONAL,
      ByteCode.BLOCK_END
    );
  }

  @Test(description = """
  input {
    color: #9ca3af
  }
  """)
  public void testCase12() {
    Compiler02 compiler;
    compiler = new Compiler02();

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

    compiler.optimize();

    CompiledStyleSheet result;
    result = compiler.compile();

    test(
      result,

      ByteCode.SELECTOR_TYPE,
      (byte) StandardTypeSelector.input.ordinal(),
      ByteCode.BLOCK_START,
      ByteCode.TAB, (byte) 1,
      ByteCode.PROPERTY_NAME,
      Bytes.prop0(Property.COLOR),
      Bytes.prop1(Property.COLOR),
      ByteCode.SPACE_OPTIONAL,
      ByteCode.COLOR_HEX,
      Bytes.two0(0),
      Bytes.two1(0),
      ByteCode.SEMICOLON_OPTIONAL,
      ByteCode.BLOCK_END
    );
  }

  @Test(description = """
  @media screen {
    p {
      display: flex;
    }
  }
  """)
  public void testCase13() {
    Compiler02 compiler;
    compiler = new Compiler02();

    compiler.compilationBegin();

    compiler.declarationBegin(Property.DISPLAY);
    compiler.propertyValue(StandardName.flex);
    compiler.declarationEnd();

    compiler.styleRuleBegin();
    compiler.styleRuleElement(StandardTypeSelector.p);
    compiler.styleRuleElement(InternalInstruction.INSTANCE);
    compiler.styleRuleEnd();

    compiler.mediaRuleBegin();
    compiler.mediaRuleElement(MediaType.SCREEN);
    compiler.mediaRuleElement(InternalInstruction.INSTANCE);
    compiler.mediaRuleEnd();

    compiler.compilationEnd();

    compiler.optimize();

    CompiledStyleSheet result;
    result = compiler.compile();

    test(
      result,

      ByteCode.AT_MEDIA,
      ByteCode.SPACE,
      ByteCode.MEDIA_QUERY,
      (byte) MediaType.SCREEN.ordinal(),
      ByteCode.BLOCK_START,
      ByteCode.TAB, (byte) 1,
      ByteCode.SELECTOR_TYPE,
      (byte) StandardTypeSelector.p.ordinal(),
      ByteCode.BLOCK_START,
      ByteCode.TAB, (byte) 2,
      ByteCode.PROPERTY_NAME,
      Bytes.prop0(Property.DISPLAY),
      Bytes.prop1(Property.DISPLAY),
      ByteCode.SPACE_OPTIONAL,
      ByteCode.KEYWORD,
      Bytes.name0(StandardName.flex),
      Bytes.name1(StandardName.flex),
      ByteCode.SEMICOLON_OPTIONAL,
      ByteCode.TAB, (byte) 1,
      ByteCode.BLOCK_END,
      ByteCode.BLOCK_END
    );
  }

  @Test(description = """
  @media (min-width: 640px) {
    p {
      display: flex;
    }
  }
  """)
  public void testCase14() {
    Compiler02 compiler;
    compiler = new Compiler02();

    compiler.compilationBegin();

    compiler.length(640, LengthUnit.PX);

    compiler.declarationBegin(Property.MIN_WIDTH);
    compiler.propertyValue(InternalInstruction.LENGTH_INT);
    compiler.declarationEnd();

    compiler.declarationBegin(Property.DISPLAY);
    compiler.propertyValue(StandardName.flex);
    compiler.declarationEnd();

    compiler.styleRuleBegin();
    compiler.styleRuleElement(StandardTypeSelector.p);
    compiler.styleRuleElement(InternalInstruction.INSTANCE);
    compiler.styleRuleEnd();

    compiler.mediaRuleBegin();
    compiler.mediaRuleElement(InternalInstruction.INSTANCE);
    compiler.mediaRuleElement(InternalInstruction.INSTANCE);
    compiler.mediaRuleEnd();

    compiler.compilationEnd();

    compiler.optimize();

    CompiledStyleSheet result;
    result = compiler.compile();

    test(
      result,

      ByteCode.AT_MEDIA,
      ByteCode.SPACE,
      ByteCode.PARENS_OPEN,
      ByteCode.PROPERTY_NAME,
      Bytes.prop0(Property.MIN_WIDTH),
      Bytes.prop1(Property.MIN_WIDTH),
      ByteCode.SPACE_OPTIONAL,
      ByteCode.LENGTH_INT,
      Bytes.int0(640),
      Bytes.int1(640),
      Bytes.int2(640),
      Bytes.int3(640),
      Bytes.unit(LengthUnit.PX),
      ByteCode.PARENS_CLOSE,
      ByteCode.BLOCK_START,
      ByteCode.TAB, (byte) 1,
      ByteCode.SELECTOR_TYPE,
      (byte) StandardTypeSelector.p.ordinal(),
      ByteCode.BLOCK_START,
      ByteCode.TAB, (byte) 2,
      ByteCode.PROPERTY_NAME,
      Bytes.prop0(Property.DISPLAY),
      Bytes.prop1(Property.DISPLAY),
      ByteCode.SPACE_OPTIONAL,
      ByteCode.KEYWORD,
      Bytes.name0(StandardName.flex),
      Bytes.name1(StandardName.flex),
      ByteCode.SEMICOLON_OPTIONAL,
      ByteCode.TAB, (byte) 1,
      ByteCode.BLOCK_END,
      ByteCode.BLOCK_END
    );
  }

  @Test(description = """
  .foo {}
  """)
  public void testCase15() {
    ClassSelector foo;
    foo = ClassSelector.of("foo");

    Compiler02 compiler;
    compiler = new Compiler02();

    compiler.compilationBegin();

    compiler.styleRuleBegin();
    compiler.styleRuleElement(foo);
    compiler.styleRuleEnd();

    compiler.compilationEnd();

    compiler.optimize();

    CompiledStyleSheet result;
    result = compiler.compile();

    test(
      result,

      ByteCode.SELECTOR_CLASS,
      Bytes.two0(0),
      Bytes.two1(0),
      ByteCode.BLOCK_EMPTY
    );
  }

  @Test(description = """
  :-moz-ui-invalid {
    box-shadow: 60px -16px teal, 10px 5px black;
  }
  """)
  public void testCase17() {
    Compiler02 compiler;
    compiler = new Compiler02();

    compiler.compilationBegin();

    compiler.length(60, LengthUnit.PX);
    compiler.length(-16, LengthUnit.PX);

    compiler.declarationBegin(Property.BOX_SHADOW);
    compiler.propertyValue(InternalInstruction.LENGTH_INT);
    compiler.propertyValue(InternalInstruction.LENGTH_INT);
    compiler.propertyValue(StandardName.teal);
    compiler.declarationEnd();

    compiler.length(10, LengthUnit.PX);
    compiler.length(5, LengthUnit.PX);

    compiler.declarationBegin(Property.BOX_SHADOW);
    compiler.propertyValue(InternalInstruction.LENGTH_INT);
    compiler.propertyValue(InternalInstruction.LENGTH_INT);
    compiler.propertyValue(StandardName.black);
    compiler.declarationEnd();

    compiler.declarationBegin(Property.BOX_SHADOW);
    compiler.propertyHash(InternalInstruction.INSTANCE);
    compiler.propertyValueComma();
    compiler.propertyHash(InternalInstruction.INSTANCE);
    compiler.declarationEnd();

    compiler.styleRuleBegin();
    compiler.styleRuleElement(StandardPseudoClassSelector._mozUiInvalid);
    compiler.styleRuleElement(InternalInstruction.INSTANCE);
    compiler.styleRuleEnd();

    compiler.compilationEnd();

    compiler.optimize();

    CompiledStyleSheet result;
    result = compiler.compile();

    test(
      result,

      ByteCode.SELECTOR_PSEUDO_CLASS,
      (byte) StandardPseudoClassSelector._mozUiInvalid.ordinal(),
      ByteCode.BLOCK_START,
      ByteCode.TAB, (byte) 1,
      ByteCode.PROPERTY_NAME,
      Bytes.prop0(Property.BOX_SHADOW),
      Bytes.prop1(Property.BOX_SHADOW),
      ByteCode.SPACE_OPTIONAL,
      ByteCode.LENGTH_INT,
      Bytes.int0(60),
      Bytes.int1(60),
      Bytes.int2(60),
      Bytes.int3(60),
      Bytes.unit(LengthUnit.PX),
      ByteCode.SPACE,
      ByteCode.LENGTH_INT,
      Bytes.int0(-16),
      Bytes.int1(-16),
      Bytes.int2(-16),
      Bytes.int3(-16),
      Bytes.unit(LengthUnit.PX),
      ByteCode.SPACE,
      ByteCode.KEYWORD,
      Bytes.name0(StandardName.teal),
      Bytes.name1(StandardName.teal),
      ByteCode.COMMA,
      ByteCode.LENGTH_INT,
      Bytes.int0(10),
      Bytes.int1(10),
      Bytes.int2(10),
      Bytes.int3(10),
      Bytes.unit(LengthUnit.PX),
      ByteCode.SPACE,
      ByteCode.LENGTH_INT,
      Bytes.int0(5),
      Bytes.int1(5),
      Bytes.int2(5),
      Bytes.int3(5),
      Bytes.unit(LengthUnit.PX),
      ByteCode.SPACE,
      ByteCode.KEYWORD,
      Bytes.name0(StandardName.black),
      Bytes.name1(StandardName.black),
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