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
import objectos.css.tmpl.PropertyValue.ColorValue;
import objectos.css.util.ClassSelector;
import objectos.css.util.CustomProperty;
import objectos.css.util.Length;
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
      Bytes.len0(9),
      Bytes.len1(9),
      ByteProto.PROPERTY_STANDARD,
      Bytes.prop0(Property.BOX_SIZING),
      Bytes.prop1(Property.BOX_SIZING),
      ByteProto.STANDARD_NAME,
      Bytes.name0(StandardName.borderBox),
      Bytes.name1(StandardName.borderBox),
      ByteProto.DECLARATION_END,
      Bytes.int0(9),
      ByteProto.DECLARATION,

      ByteProto.STYLE_RULE,
      Bytes.len0(8),
      Bytes.len1(8),
      ByteProto.STANDARD_NAME,
      Bytes.name0(StandardName.any),
      Bytes.name1(StandardName.any),
      ByteProto.DECLARATION,
      Bytes.int0(19),
      ByteProto.STYLE_RULE_END,
      Bytes.int0(20),
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
      Bytes.len0(10),
      Bytes.len1(10),
      ByteProto.PROPERTY_STANDARD,
      Bytes.prop0(Property.MARGIN),
      Bytes.prop1(Property.MARGIN),
      ByteProto.LENGTH_INT,
      Bytes.int0(23),
      ByteProto.LENGTH_DOUBLE,
      Bytes.int0(19),
      ByteProto.DECLARATION_END,
      Bytes.int0(26),
      ByteProto.DECLARATION,

      ByteProto.STYLE_RULE,
      Bytes.len0(7),
      Bytes.len1(7),
      ByteProto.SELECTOR_TYPE,
      (byte) StandardTypeSelector.ul.ordinal(),
      ByteProto.DECLARATION,
      Bytes.int0(19),
      ByteProto.STYLE_RULE_END,
      Bytes.int0(36),
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
      Bytes.len0(15),
      Bytes.len1(15),
      ByteProto.PROPERTY_STANDARD,
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
      Bytes.int0(15),
      ByteProto.DECLARATION,

      ByteProto.STYLE_RULE,
      Bytes.len0(7),
      Bytes.len1(7),
      ByteProto.SELECTOR_TYPE,
      (byte) StandardTypeSelector.p.ordinal(),
      ByteProto.DECLARATION,
      Bytes.int0(24),
      ByteProto.STYLE_RULE_END,
      Bytes.int0(25),
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

    compiler.styleRuleBegin();
    compiler.styleRuleElement(StandardTypeSelector.input);
    compiler.styleRuleElement(StandardPseudoElementSelector.__placeholder);
    compiler.styleRuleEnd();

    compiler.compilationEnd();

    test(
      compiler,

      ByteProto.STYLE_RULE,
      Bytes.len0(7),
      Bytes.len1(7),
      ByteProto.SELECTOR_TYPE,
      (byte) StandardTypeSelector.input.ordinal(),
      ByteProto.SELECTOR_PSEUDO_ELEMENT,
      (byte) StandardPseudoElementSelector.__placeholder.ordinal(),
      ByteProto.STYLE_RULE_END,
      Bytes.int0(7),
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
      Bytes.len0(8),
      Bytes.len1(8),
      ByteProto.PROPERTY_STANDARD,
      Bytes.prop0(Property.LIST_STYLE_IMAGE),
      Bytes.prop1(Property.LIST_STYLE_IMAGE),
      ByteProto.URL,
      Bytes.int0(10),
      ByteProto.DECLARATION_END,
      Bytes.int0(11),
      ByteProto.DECLARATION,

      ByteProto.STYLE_RULE,
      Bytes.len0(7),
      Bytes.len1(7),
      ByteProto.SELECTOR_TYPE,
      (byte) StandardTypeSelector.ul.ordinal(),
      ByteProto.DECLARATION,
      Bytes.int0(17),
      ByteProto.STYLE_RULE_END,
      Bytes.int0(21),
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
      Bytes.len0(8),
      Bytes.len1(8),
      ByteProto.PROPERTY_STANDARD,
      Bytes.prop0(Property.FONT_FAMILY),
      Bytes.prop1(Property.FONT_FAMILY),
      ByteProto.STRING_LITERAL,
      Bytes.int0(10),
      ByteProto.DECLARATION_END,
      Bytes.int0(11),
      ByteProto.DECLARATION,

      ByteProto.STYLE_RULE,
      Bytes.len0(7),
      Bytes.len1(7),
      ByteProto.SELECTOR_TYPE,
      (byte) StandardTypeSelector.p.ordinal(),
      ByteProto.DECLARATION,
      Bytes.int0(17),
      ByteProto.STYLE_RULE_END,
      Bytes.int0(21),
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
      Bytes.len0(12),
      Bytes.len1(12),
      ByteProto.PROPERTY_STANDARD,
      Bytes.prop0(Property.FONT_FAMILY),
      Bytes.prop1(Property.FONT_FAMILY),
      ByteProto.STRING_LITERAL,
      Bytes.int0(10),
      ByteProto.COMMA,
      ByteProto.STANDARD_NAME,
      Bytes.name0(StandardName.sansSerif),
      Bytes.name1(StandardName.sansSerif),
      ByteProto.DECLARATION_END,
      Bytes.int0(15),
      ByteProto.DECLARATION,

      ByteProto.STYLE_RULE,
      Bytes.len0(7),
      Bytes.len1(7),
      ByteProto.SELECTOR_TYPE,
      (byte) StandardTypeSelector.p.ordinal(),
      ByteProto.DECLARATION,
      Bytes.int0(21),
      ByteProto.STYLE_RULE_END,
      Bytes.int0(25),
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
      Bytes.len0(8),
      Bytes.len1(8),
      ByteProto.PROPERTY_STANDARD,
      Bytes.prop0(Property.COLOR),
      Bytes.prop1(Property.COLOR),
      ByteProto.COLOR_HEX,
      Bytes.int0(10),
      ByteProto.DECLARATION_END,
      Bytes.int0(11),
      ByteProto.DECLARATION,

      ByteProto.STYLE_RULE,
      Bytes.len0(7),
      Bytes.len1(7),
      ByteProto.SELECTOR_TYPE,
      (byte) StandardTypeSelector.input.ordinal(),
      ByteProto.DECLARATION,
      Bytes.int0(17),
      ByteProto.STYLE_RULE_END,
      Bytes.int0(21),
      ByteProto.STYLE_RULE
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
    Compiler01 compiler;
    compiler = new Compiler01();

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

    test(
      compiler,

      ByteProto.MARKED,
      Bytes.len0(9),
      Bytes.len1(9),
      ByteProto.PROPERTY_STANDARD,
      Bytes.prop0(Property.DISPLAY),
      Bytes.prop1(Property.DISPLAY),
      ByteProto.STANDARD_NAME,
      Bytes.name0(StandardName.flex),
      Bytes.name1(StandardName.flex),
      ByteProto.DECLARATION_END,
      Bytes.int0(9),
      ByteProto.DECLARATION,

      ByteProto.MARKED,
      Bytes.len0(7),
      Bytes.len1(7),
      ByteProto.SELECTOR_TYPE,
      (byte) StandardTypeSelector.p.ordinal(),
      ByteProto.DECLARATION,
      Bytes.int0(18),
      ByteProto.STYLE_RULE_END,
      Bytes.int0(19),
      ByteProto.STYLE_RULE,

      ByteProto.MEDIA_RULE,
      Bytes.len0(7),
      Bytes.len1(7),
      ByteProto.MEDIA_TYPE,
      (byte) MediaType.SCREEN.ordinal(),
      ByteProto.STYLE_RULE,
      Bytes.int0(16),
      ByteProto.MEDIA_RULE_END,
      Bytes.int0(29),
      ByteProto.MEDIA_RULE
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
    Compiler01 compiler;
    compiler = new Compiler01();

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

    test(
      compiler,

      ByteProto.MARKED6,
      Bytes.int0(640),
      Bytes.int1(640),
      Bytes.int2(640),
      Bytes.int3(640),
      Bytes.unit(LengthUnit.PX),

      ByteProto.MARKED,
      Bytes.len0(8),
      Bytes.len1(8),
      ByteProto.PROPERTY_STANDARD,
      Bytes.prop0(Property.MIN_WIDTH),
      Bytes.prop1(Property.MIN_WIDTH),
      ByteProto.LENGTH_INT,
      Bytes.int0(13),
      ByteProto.DECLARATION_END,
      Bytes.int0(14),
      ByteProto.DECLARATION,

      ByteProto.MARKED,
      Bytes.len0(9),
      Bytes.len1(9),
      ByteProto.PROPERTY_STANDARD,
      Bytes.prop0(Property.DISPLAY),
      Bytes.prop1(Property.DISPLAY),
      ByteProto.STANDARD_NAME,
      Bytes.name0(StandardName.flex),
      Bytes.name1(StandardName.flex),
      ByteProto.DECLARATION_END,
      Bytes.int0(9),
      ByteProto.DECLARATION,

      ByteProto.MARKED,
      Bytes.len0(7),
      Bytes.len1(7),
      ByteProto.SELECTOR_TYPE,
      (byte) StandardTypeSelector.p.ordinal(),
      ByteProto.DECLARATION,
      Bytes.int0(18),
      ByteProto.STYLE_RULE_END,
      Bytes.int0(19),
      ByteProto.STYLE_RULE,

      ByteProto.MEDIA_RULE,
      Bytes.len0(7),
      Bytes.len1(7),
      ByteProto.DECLARATION,
      Bytes.int0(37),
      ByteProto.STYLE_RULE,
      Bytes.int0(16),
      ByteProto.MEDIA_RULE_END,
      Bytes.int0(46),
      ByteProto.MEDIA_RULE
    );
  }

  @Test(description = """
  .foo {}
  """)
  public void testCase15() {
    ClassSelector foo;
    foo = ClassSelector.of("foo");

    Compiler01 compiler;
    compiler = new Compiler01();

    compiler.compilationBegin();

    compiler.styleRuleBegin();
    compiler.styleRuleElement(foo);
    compiler.styleRuleEnd();

    compiler.compilationEnd();

    test(
      compiler,

      ByteProto.STYLE_RULE,
      Bytes.len0(6),
      Bytes.len1(6),
      ByteProto.SELECTOR_CLASS,
      Bytes.two0(0),
      Bytes.two1(0),
      ByteProto.STYLE_RULE_END,
      Bytes.int0(6),
      ByteProto.STYLE_RULE
    );
  }

  @Test(description = """
  .foo > * + * {}
  """)
  public void testCase16() {
    ClassSelector foo;
    foo = ClassSelector.of("foo");

    Compiler01 compiler;
    compiler = new Compiler01();

    compiler.compilationBegin();

    compiler.styleRuleBegin();
    compiler.styleRuleElement(foo);
    compiler.styleRuleElement(Combinator.CHILD);
    compiler.styleRuleElement(GeneratedCssTemplate.any);
    compiler.styleRuleElement(Combinator.ADJACENT_SIBLING);
    compiler.styleRuleElement(GeneratedCssTemplate.any);
    compiler.styleRuleEnd();

    compiler.compilationEnd();

    test(
      compiler,

      ByteProto.STYLE_RULE,
      Bytes.len0(16),
      Bytes.len1(16),
      ByteProto.SELECTOR_CLASS,
      Bytes.two0(0),
      Bytes.two1(0),
      ByteProto.SELECTOR_COMBINATOR,
      (byte) Combinator.CHILD.ordinal(),
      ByteProto.STANDARD_NAME,
      Bytes.name0(StandardName.any),
      Bytes.name1(StandardName.any),
      ByteProto.SELECTOR_COMBINATOR,
      (byte) Combinator.ADJACENT_SIBLING.ordinal(),
      ByteProto.STANDARD_NAME,
      Bytes.name0(StandardName.any),
      Bytes.name1(StandardName.any),
      ByteProto.STYLE_RULE_END,
      Bytes.int0(16),
      ByteProto.STYLE_RULE
    );
  }

  @Test(description = """
  :-moz-ui-invalid {
    box-shadow: 60px -16px teal, 10px 5px black;
  }
  """)
  public void testCase17() {
    Compiler01 compiler;
    compiler = new Compiler01();

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

    test(
      compiler,

      ByteProto.MARKED6,
      Bytes.int0(60),
      Bytes.int1(60),
      Bytes.int2(60),
      Bytes.int3(60),
      Bytes.unit(LengthUnit.PX),

      ByteProto.MARKED6,
      Bytes.int0(-16),
      Bytes.int1(-16),
      Bytes.int2(-16),
      Bytes.int3(-16),
      Bytes.unit(LengthUnit.PX),

      ByteProto.MARKED,
      Bytes.len0(13),
      Bytes.len1(13),
      ByteProto.PROPERTY_STANDARD,
      Bytes.prop0(Property.BOX_SHADOW),
      Bytes.prop1(Property.BOX_SHADOW),
      ByteProto.LENGTH_INT,
      Bytes.int0(19),
      ByteProto.LENGTH_INT,
      Bytes.int0(15),
      ByteProto.STANDARD_NAME,
      Bytes.name0(StandardName.teal),
      Bytes.name1(StandardName.teal),
      ByteProto.DECLARATION_END,
      Bytes.int0(25),
      ByteProto.DECLARATION,

      ByteProto.MARKED6,
      Bytes.int0(10),
      Bytes.int1(10),
      Bytes.int2(10),
      Bytes.int3(10),
      Bytes.unit(LengthUnit.PX),

      ByteProto.MARKED6,
      Bytes.int0(5),
      Bytes.int1(5),
      Bytes.int2(5),
      Bytes.int3(5),
      Bytes.unit(LengthUnit.PX),

      ByteProto.MARKED,
      Bytes.len0(13),
      Bytes.len1(13),
      ByteProto.PROPERTY_STANDARD,
      Bytes.prop0(Property.BOX_SHADOW),
      Bytes.prop1(Property.BOX_SHADOW),
      ByteProto.LENGTH_INT,
      Bytes.int0(19),
      ByteProto.LENGTH_INT,
      Bytes.int0(15),
      ByteProto.STANDARD_NAME,
      Bytes.name0(StandardName.black),
      Bytes.name1(StandardName.black),
      ByteProto.DECLARATION_END,
      Bytes.int0(25),
      ByteProto.DECLARATION,

      ByteProto.MARKED,
      Bytes.len0(11),
      Bytes.len1(11),
      ByteProto.PROPERTY_STANDARD,
      Bytes.prop0(Property.BOX_SHADOW),
      Bytes.prop1(Property.BOX_SHADOW),
      ByteProto.DECLARATION,
      Bytes.int0(51),
      ByteProto.COMMA,
      ByteProto.DECLARATION,
      Bytes.int0(26),
      ByteProto.DECLARATION_END,
      Bytes.int0(67),
      ByteProto.DECLARATION,

      ByteProto.STYLE_RULE,
      Bytes.len0(7),
      Bytes.len1(7),
      ByteProto.SELECTOR_PSEUDO_CLASS,
      (byte) StandardPseudoClassSelector._mozUiInvalid.ordinal(),
      ByteProto.DECLARATION,
      Bytes.int0(20),
      ByteProto.STYLE_RULE_END,
      Bytes.int0(77),
      ByteProto.STYLE_RULE
    );
  }

  @Test(description = """
  [#487] p {
    --text-color: black;
  }
  """)
  public void testCase18() {
    CustomProperty<ColorValue> textColor;
    textColor = CustomProperty.named("--text-color");

    Compiler01 compiler;
    compiler = new Compiler01();

    compiler.compilationBegin();

    compiler.customPropertyBegin(textColor);
    compiler.propertyValue(StandardName.black);
    compiler.customPropertyEnd();

    compiler.styleRuleBegin();
    compiler.styleRuleElement(StandardTypeSelector.p);
    compiler.styleRuleElement(InternalInstruction.INSTANCE);
    compiler.styleRuleEnd();

    compiler.compilationEnd();

    test(
      compiler,

      ByteProto.MARKED,
      Bytes.len0(9),
      Bytes.len1(9),
      ByteProto.PROPERTY_CUSTOM,
      Bytes.two0(0),
      Bytes.two1(0),
      ByteProto.STANDARD_NAME,
      Bytes.name0(StandardName.black),
      Bytes.name1(StandardName.black),
      ByteProto.DECLARATION_END,
      Bytes.int0(9),
      ByteProto.DECLARATION,

      ByteProto.STYLE_RULE,
      Bytes.len0(7),
      Bytes.len1(7),
      ByteProto.SELECTOR_TYPE,
      (byte) StandardTypeSelector.p.ordinal(),
      ByteProto.DECLARATION,
      Bytes.int0(18),
      ByteProto.STYLE_RULE_END,
      Bytes.int0(19),
      ByteProto.STYLE_RULE
    );
  }

  @Test(description = """
  [#487] p {
    color: var(--text-color);
  }
  """)
  public void testCase19() {
    CustomProperty<ColorValue> textColor;
    textColor = CustomProperty.named("--text-color");

    Compiler01 compiler;
    compiler = new Compiler01();

    compiler.compilationBegin();

    compiler.varFunction(textColor);

    compiler.declarationBegin(Property.COLOR);
    compiler.propertyValue(InternalInstruction.VAR_FUNCTION);
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
      Bytes.len0(8),
      Bytes.len1(8),
      ByteProto.PROPERTY_STANDARD,
      Bytes.prop0(Property.COLOR),
      Bytes.prop1(Property.COLOR),
      ByteProto.VAR0,
      Bytes.int0(10),
      ByteProto.DECLARATION_END,
      Bytes.int0(11),
      ByteProto.DECLARATION,

      ByteProto.STYLE_RULE,
      Bytes.len0(7),
      Bytes.len1(7),
      ByteProto.SELECTOR_TYPE,
      (byte) StandardTypeSelector.p.ordinal(),
      ByteProto.DECLARATION,
      Bytes.int0(17),
      ByteProto.STYLE_RULE_END,
      Bytes.int0(21),
      ByteProto.STYLE_RULE
    );
  }

  @Test(description = """
  ul {
    margin: 20px 1.5rem;
  }
  """)
  public void testCase20() {
    Compiler01 compiler;
    compiler = new Compiler01();

    compiler.compilationBegin();

    compiler.declarationBegin(Property.MARGIN);
    compiler.propertyValue(Length.px(20));
    compiler.propertyValue(Length.rem(1.5));
    compiler.declarationEnd();

    compiler.styleRuleBegin();
    compiler.styleRuleElement(StandardTypeSelector.ul);
    compiler.styleRuleElement(InternalInstruction.INSTANCE);
    compiler.styleRuleEnd();

    compiler.compilationEnd();

    test(
      compiler,

      ByteProto.MARKED,
      Bytes.len0(12),
      Bytes.len1(12),
      ByteProto.PROPERTY_STANDARD,
      Bytes.prop0(Property.MARGIN),
      Bytes.prop1(Property.MARGIN),
      ByteProto.RAW,
      Bytes.two0(0),
      Bytes.two1(0),
      ByteProto.RAW,
      Bytes.two0(1),
      Bytes.two1(1),
      ByteProto.DECLARATION_END,
      Bytes.int0(12),
      ByteProto.DECLARATION,

      ByteProto.STYLE_RULE,
      Bytes.len0(7),
      Bytes.len1(7),
      ByteProto.SELECTOR_TYPE,
      (byte) StandardTypeSelector.ul.ordinal(),
      ByteProto.DECLARATION,
      Bytes.int0(21),
      ByteProto.STYLE_RULE_END,
      Bytes.int0(22),
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
