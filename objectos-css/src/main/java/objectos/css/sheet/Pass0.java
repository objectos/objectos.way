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
package objectos.css.sheet;

import java.util.Arrays;
import objectos.css.function.StandardFunctionName;
import objectos.css.keyword.StandardKeyword;
import objectos.css.property.StandardPropertyName;
import objectos.css.select.AttributeSelector;
import objectos.css.select.AttributeValueOperator;
import objectos.css.select.AttributeValueSelector;
import objectos.css.select.ClassSelector;
import objectos.css.select.Combinator;
import objectos.css.select.IdSelector;
import objectos.css.select.PseudoClassSelector;
import objectos.css.select.PseudoElementSelector;
import objectos.css.select.TypeSelector;
import objectos.css.select.UniversalSelector;
import objectos.css.type.AngleUnit;
import objectos.css.type.ColorHex;
import objectos.css.type.ColorKind;
import objectos.css.type.ColorName;
import objectos.css.type.LengthUnit;
import objectos.css.type.Value;
import objectos.lang.Check;
import objectos.util.DoubleArrays;
import objectos.util.GrowableList;
import objectos.util.IntArrays;
import objectos.util.UnmodifiableList;

final class Pass0 implements AutoCloseable, StyleEngine {

  private double[] doubles;

  private int doublesLength;

  private int[] objects;

  private int objectsLength;

  private int[] protos;

  private int protosCursor;

  private int protosLength;

  final GrowableList<String> strings = new GrowableList<>();

  private GrowableList<RuleElement> rulePrefix;

  Pass0() {
    doubles = new double[10];

    objects = new int[128];

    protos = new int[128];
  }

  @Override
  public final void addAtMedia(AtMediaElement... elements) {
    Check.notNull(elements, "elements == null");

    addProto(ByteProto.AT_MEDIA_END);

    for (int i = elements.length - 1; i >= 0; i--) {
      AtMediaElement element;
      element = elements[i];

      if (element == null) {
        throw new NullPointerException("elements[" + i + "] == null");
      }

      acceptMediaQueryElement0(element);
    }

    addProto(ByteProto.AT_MEDIA_START);

    addObject(ByteProto.AT_MEDIA_MARK);
  }

  @Override
  public final void addColor(ColorName color) {
    addProto(color.getCode());
    addProto(ByteProto.VALUE_COLOR_NAME);
  }

  @Override
  public final void addDeclaration(StandardPropertyName name, double value) {
    Check.notNull(name, "name == null");

    addDeclarationEnd();
    addValueDouble(value);
    addDeclarationStart(name);
  }

  @Override
  public final void addDeclaration(StandardPropertyName name, int value) {
    Check.notNull(name, "name == null");

    addDeclarationEnd();
    addValueInt(value);
    addDeclarationStart(name);
  }

  @Override
  public final void addDeclaration(StandardPropertyName name, MultiDeclarationElement... elements) {
    Check.notNull(name, "name == null");
    Check.notNull(elements, "elements == null");

    addProto(ByteProto.DECLARATION_MULTI_END);

    // yes, reverse
    for (int i = elements.length - 1; i >= 0; i--) {
      MultiDeclarationElement element = elements[i];

      if (element == null) {
        throw new NullPointerException("elements[" + i + "] == null");
      }

      addProto(ByteProto.DECLARATION_MULTI_ELEMENT_MARK);
    }

    addProto(name.getCode());
    addProto(ByteProto.DECLARATION_MULTI_START);
  }

  @Override
  public final void addDeclaration(StandardPropertyName name, String value) {
    Check.notNull(name, "name == null");
    Check.notNull(value, "value == null");

    addDeclarationEnd();
    addProtoString(ByteProto.VALUE_STRING, value);
    addDeclarationStart(name);
  }

  @Override
  public final void addDeclaration(StandardPropertyName name, Value v1) {
    Check.notNull(name, "name == null");
    Check.notNull(v1, "v1 == null");

    v1.acceptValueCreator(this);

    addDeclarationEnd();
    v1.acceptValueMarker(this);
    addDeclarationStart(name);
  }

  @Override
  public final void addDeclaration(StandardPropertyName name, Value v1, Value v2) {
    Check.notNull(name, "name == null");
    Check.notNull(v1, "v1 == null");
    Check.notNull(v2, "v2 == null");

    v1.acceptValueCreator(this);
    v2.acceptValueCreator(this);

    addDeclarationEnd();
    // yes, reverse
    v2.acceptValueMarker(this);
    v1.acceptValueMarker(this);
    addDeclarationStart(name);
  }

  @Override
  public final void addDeclaration(StandardPropertyName name, Value v1, Value v2, Value v3) {
    Check.notNull(name, "name == null");
    Check.notNull(v1, "v1 == null");
    Check.notNull(v2, "v2 == null");
    Check.notNull(v3, "v3 == null");

    v1.acceptValueCreator(this);
    v2.acceptValueCreator(this);
    v3.acceptValueCreator(this);

    addDeclarationEnd();
    // yes, reverse
    v3.acceptValueMarker(this);
    v2.acceptValueMarker(this);
    v1.acceptValueMarker(this);
    addDeclarationStart(name);
  }

  @Override
  public final void addDeclaration(
      StandardPropertyName name, Value v1, Value v2, Value v3, Value v4) {
    Check.notNull(name, "name == null");
    Check.notNull(v1, "v1 == null");
    Check.notNull(v2, "v2 == null");
    Check.notNull(v3, "v3 == null");
    Check.notNull(v4, "v4 == null");

    v1.acceptValueCreator(this);
    v2.acceptValueCreator(this);
    v3.acceptValueCreator(this);
    v4.acceptValueCreator(this);

    addDeclarationEnd();
    // yes, reverse
    v4.acceptValueMarker(this);
    v3.acceptValueMarker(this);
    v2.acceptValueMarker(this);
    v1.acceptValueMarker(this);
    addDeclarationStart(name);
  }

  @Override
  public final void addDeclaration(
      StandardPropertyName name, Value v1, Value v2, Value v3, Value v4, Value v5) {
    Check.notNull(name, "name == null");
    Check.notNull(v1, "v1 == null");
    Check.notNull(v2, "v2 == null");
    Check.notNull(v3, "v3 == null");
    Check.notNull(v4, "v4 == null");
    Check.notNull(v5, "v5 == null");

    v1.acceptValueCreator(this);
    v2.acceptValueCreator(this);
    v3.acceptValueCreator(this);
    v4.acceptValueCreator(this);
    v5.acceptValueCreator(this);

    addDeclarationEnd();
    // yes, reverse
    v5.acceptValueMarker(this);
    v4.acceptValueMarker(this);
    v3.acceptValueMarker(this);
    v2.acceptValueMarker(this);
    v1.acceptValueMarker(this);
    addDeclarationStart(name);
  }

  @Override
  public final void addDeclaration(
      StandardPropertyName name, Value v1, Value v2, Value v3, Value v4, Value v5, Value v6) {
    Check.notNull(name, "name == null");
    Check.notNull(v1, "v1 == null");
    Check.notNull(v2, "v2 == null");
    Check.notNull(v3, "v3 == null");
    Check.notNull(v4, "v4 == null");
    Check.notNull(v5, "v5 == null");
    Check.notNull(v6, "v6 == null");

    v1.acceptValueCreator(this);
    v2.acceptValueCreator(this);
    v3.acceptValueCreator(this);
    v4.acceptValueCreator(this);
    v5.acceptValueCreator(this);
    v6.acceptValueCreator(this);

    addDeclarationEnd();
    // yes, reverse
    v6.acceptValueMarker(this);
    v5.acceptValueMarker(this);
    v4.acceptValueMarker(this);
    v3.acceptValueMarker(this);
    v2.acceptValueMarker(this);
    v1.acceptValueMarker(this);
    addDeclarationStart(name);
  }

  @Override
  public final void addFunction(StandardFunctionName name, Value v1) {
    Check.notNull(name, "name == null");
    Check.notNull(v1, "v1 == null");

    v1.acceptValueCreator(this);

    addFunctionEnd();

    v1.acceptValueMarker(this);

    addFunctionStart(name);
  }

  @Override
  public final void addKeyword(StandardKeyword keyword) {
    addProto(keyword.getCode());
    addProto(ByteProto.VALUE_KEYWORD);
  }

  @Override
  public void addRule(RuleElement... elements) {
    Check.notNull(elements, "elements == null");

    addProto(ByteProto.RULE_END);

    for (int i = elements.length - 1; i >= 0; i--) {
      RuleElement element;
      element = elements[i];

      if (element == null) {
        throw new NullPointerException("elements[" + i + "] == null");
      }

      acceptRuleElement0(element);
    }

    if (rulePrefix != null) {
      for (int i = rulePrefix.size() - 1; i >= 0; i--) {
        RuleElement element;
        element = rulePrefix.get(i);

        acceptRuleElement0(element);
      }
    }

    addProto(ByteProto.RULE_START);
    addObject(ByteProto.RULE_MARK);
  }

  public void addRule(UnmodifiableList<RuleElement> elements) {
    Check.notNull(elements, "elements == null");

    addProto(ByteProto.RULE_END);

    for (int i = elements.size() - 1; i >= 0; i--) {
      RuleElement element;
      element = elements.get(i);

      if (element == null) {
        throw new NullPointerException("elements[" + i + "] == null");
      }

      acceptRuleElement0(element);
    }

    addProto(ByteProto.RULE_START);
    addObject(ByteProto.RULE_MARK);
  }

  @Override
  public final void addZero() {
    addValueInt(0);
  }

  @Override
  public final void clearRulePrefix() {
    if (rulePrefix != null) {
      rulePrefix.clear();
    }
  }

  @Override
  public final void close() {
    doublesLength = 0;

    objectsLength = 0;

    protosLength = 0;

    strings.clear();

    if (rulePrefix != null) {
      rulePrefix.clear();
    }
  }

  @Override
  public final void createAngle(AngleUnit unit, double value) {
    addDoubleProto(value);
    addProto(unit.getCode());
    addProto(ByteProto.VALUE_ANGLE_DOUBLE);
  }

  @Override
  public final void createAngle(AngleUnit unit, int value) {
    addProto(value);
    addProto(unit.getCode());
    addProto(ByteProto.VALUE_ANGLE_INT);
  }

  @Override
  public final void createAttributeSelector(String name) {
    addProtoString(
      ByteProto.SELECTOR_ATTRIBUTE,
      name
    );
  }

  @Override
  public final void createAttributeValueElement(AttributeValueOperator operator, String value) {
    addProto(strings.size());
    addProto(operator.getCode());
    addProto(ByteProto.SELECTOR_ATTRIBUTE_VALUE_ELEMENT);

    strings.add(value);
  }

  @Override
  public final void createAttributeValueSelector(String name) {
    addProtoString(
      ByteProto.SELECTOR_ATTRIBUTE_VALUE,
      name
    );
  }

  @Override
  public final void createClassSelector(String className) {
    addProtoString(
      ByteProto.SELECTOR_CLASS,
      className
    );
  }

  @Override
  public final void createColor(String hex) {
    ColorHex.checkValidHexNotation(hex);

    addProtoString(ByteProto.VALUE_COLOR_HEX, hex);
  }

  @Override
  public final void createDouble(double value) {
    addDoubleProto(value);
    addProto(ByteProto.VALUE_DOUBLE_DSL);
  }

  @Override
  public final void createIdSelector(String id) {
    addProtoString(
      ByteProto.SELECTOR_ID,
      id
    );
  }

  @Override
  public final void createInt(int value) {
    addProto(value);
    addProto(ByteProto.VALUE_INT_DSL);
  }

  @Override
  public final void createKeyword(String name) {
    Check.notNull(name, "name == null");

    for (char c : name.toCharArray()) {
      if (Character.isLetterOrDigit(c)) {
        continue;
      }
      if (c == '-') {
        continue;
      }
      throw new IllegalArgumentException("Invalid keyword name: " + name);
    }

    addProtoString(ByteProto.VALUE_KEYWORD_CUSTOM, name);
  }

  @Override
  public final void createLength(LengthUnit unit, double value) {
    addDoubleProto(value);
    addProto(unit.getCode());
    addProto(ByteProto.VALUE_LENGTH_DOUBLE);
  }

  @Override
  public final void createLength(LengthUnit unit, int value) {
    addProto(value);
    addProto(unit.getCode());
    addProto(ByteProto.VALUE_LENGTH_INT);
  }

  @Override
  public final void createPercentage(double value) {
    addDoubleProto(value);
    addProto(ByteProto.VALUE_PERCENTAGE_DOUBLE);
  }

  @Override
  public final void createPercentage(int value) {
    addProto(value);
    addProto(ByteProto.VALUE_PERCENTAGE_INT);
  }

  @Override
  public final void createRgb(double r, double g, double b) {
    addProto(doublesLength);

    addDouble(r);
    addDouble(g);
    addDouble(b);

    addProto(ByteProto.VALUE_RGB_DOUBLE);
  }

  @Override
  public final void createRgb(double r, double g, double b, double alpha) {
    addProto(doublesLength);

    addDouble(r);
    addDouble(g);
    addDouble(b);
    addDouble(alpha);

    addProto(ByteProto.VALUE_RGB_DOUBLE_ALPHA);
  }

  @Override
  public final void createRgb(int r, int g, int b) {
    addProto(b);
    addProto(g);
    addProto(r);
    addProto(ByteProto.VALUE_RGB_INT);
  }

  @Override
  public final void createRgb(int r, int g, int b, double alpha) {
    addDoubleProto(alpha);
    addProto(b);
    addProto(g);
    addProto(r);
    addProto(ByteProto.VALUE_RGB_INT_ALPHA);
  }

  @Override
  public final void createRgba(double r, double g, double b, double alpha) {
    addProto(doublesLength);

    addDouble(r);
    addDouble(g);
    addDouble(b);
    addDouble(alpha);

    addProto(ByteProto.VALUE_RGBA_DOUBLE);
  }

  @Override
  public final void createRgba(int r, int g, int b, double alpha) {
    addDoubleProto(alpha);
    addProto(b);
    addProto(g);
    addProto(r);
    addProto(ByteProto.VALUE_RGBA_INT);
  }

  @Override
  public final void createString(String value) {
    addProtoString(ByteProto.VALUE_STRING_DSL, value);
  }

  @Override
  public final void createUri(String value) {
    Check.notNull(value, "value == null");

    addProtoString(
      ByteProto.VALUE_URI,

      value
    );
  }

  public final void evalDone() {
    addProto(ByteProto.ROOT_END);

    for (int i = objectsLength - 1; i >= 0; i--) {
      addProto(objects[i]);
    }

    addProto(ByteProto.ROOT_START);

    protosCursor = protosLength;
  }

  @Override
  public final void markAttributeValueElement() {
    int proto = getLastProto();
    Check.state(
      proto == ByteProto.SELECTOR_ATTRIBUTE_VALUE_ELEMENT,
      "not ", ByteProto.SELECTOR_ATTRIBUTE_VALUE_ELEMENT
    );
  }

  @Override
  public final void markColor(ColorKind kind) {
    Check.notNull(kind, "kind == null");

    switch (kind) {
      case HEX:
        addProto(ByteProto.VALUE_COLOR_HEX_MARK);
        break;
      case RGB_DOUBLE:
        addProto(ByteProto.VALUE_RGB_DOUBLE_MARK);
        break;
      case RGB_DOUBLE_ALPHA:
        addProto(ByteProto.VALUE_RGB_DOUBLE_ALPHA_MARK);
        break;
      case RGB_INT:
        addProto(ByteProto.VALUE_RGB_INT_MARK);
        break;
      case RGB_INT_ALPHA:
        addProto(ByteProto.VALUE_RGB_INT_ALPHA_MARK);
        break;
      case RGBA_DOUBLE:
        addProto(ByteProto.VALUE_RGBA_DOUBLE_MARK);
        break;
      case RGBA_INT:
        addProto(ByteProto.VALUE_RGBA_INT_MARK);
        break;
      default:
        throw new AssertionError("Unexpected kind " + kind);
    }
  }

  @Override
  public final void markDouble() {
    addProto(ByteProto.VALUE_DOUBLE_MARK);
  }

  @Override
  public final void markDoubleAngle() {
    addProto(ByteProto.VALUE_ANGLE_DOUBLE_MARK);
  }

  @Override
  public final void markDoubleLength() {
    addProto(ByteProto.VALUE_LENGTH_DOUBLE_MARK);
  }

  @Override
  public final void markDoublePercentage() {
    addProto(ByteProto.VALUE_PERCENTAGE_DOUBLE_MARK);
  }

  @Override
  public final void markFunction() {
    addProto(ByteProto.VALUE_FUNCTION_MARK);
  }

  public final void markIdSelector() {
    addProto(ByteProto.SELECTOR_ID_MARK);
  }

  @Override
  public final void markInt() {
    addProto(ByteProto.VALUE_INT_MARK);
  }

  @Override
  public final void markIntAngle() {
    addProto(ByteProto.VALUE_ANGLE_INT_MARK);
  }

  @Override
  public final void markIntLength() {
    addProto(ByteProto.VALUE_LENGTH_INT_MARK);
  }

  @Override
  public final void markIntPercentage() {
    addProto(ByteProto.VALUE_PERCENTAGE_INT_MARK);
  }

  @Override
  public final void markKeyword() {
    addProto(ByteProto.VALUE_KEYWORD_CUSTOM_MARK);
  }

  @Override
  public final void markString() {
    addProto(ByteProto.VALUE_STRING_MARK);
  }

  @Override
  public final void markUri() {
    addProto(ByteProto.VALUE_URI_MARK);
  }

  @Override
  public final void setRulePrefix(RuleElement... elements) {
    if (rulePrefix == null) {
      rulePrefix = new GrowableList<>();
    }

    for (int i = 0; i < elements.length; i++) {
      RuleElement element;
      element = elements[i];

      rulePrefix.addWithNullMessage(element, "elements[", i, "] == null");
    }
  }

  void addDouble(double value) {
    doubles = DoubleArrays.growIfNecessary(doubles, doublesLength);

    doubles[doublesLength++] = value;
  }

  final void addProto(int code) {
    protos = IntArrays.growIfNecessary(protos, protosLength);

    protos[protosLength++] = code;
  }

  final double doubleAt(int index) { return doubles[index]; }

  final double[] getDoubles() {
    return Arrays.copyOf(doubles, doublesLength);
  }

  final int getProto(int index) {
    return protos[index];
  }

  final int[] getProtos() {
    return Arrays.copyOf(protos, protosLength);
  }

  final boolean hasProto() { return protosCursor > 0; }

  final int nextProto() { return protos[--protosCursor]; }

  final String stringAt(int index) { return strings.get(index); }

  private void acceptMediaQueryElement0(AtMediaElement element) {
    // TODO use pattern matching when possible
    if (element instanceof AbstractMediaExpressionOrRuleElement) {
      addProto(ByteProto.DECLARATION_MARK);
    } else if (element instanceof MediaType mt) {
      addProto(mt.getCode());
      addProto(ByteProto.MEDIA_TYPE);
    } else if (element instanceof RuleMark) {
      var code = popObject();

      addProto(code);
    } else {
      var type = element.getClass();

      throw new AssertionError("Unexpected AtMediaElement type: " + type);
    }
  }

  private void acceptRuleElement0(RuleElement element) {
    // TODO use pattern matching when possible
    if (element instanceof AbstractMediaExpressionOrRuleElement) {
      addProto(ByteProto.DECLARATION_MARK);
    } else if (element instanceof AttributeSelector sel) {
      createAttributeSelector(sel.name());

      addProto(ByteProto.SELECTOR_ATTRIBUTE_MARK);
    } else if (element instanceof AttributeSelectorMark) {
      addProto(ByteProto.SELECTOR_ATTRIBUTE_MARK);
    } else if (element instanceof AttributeValueSelector sel) {
      createAttributeValueElement(sel.operator(), sel.value());

      markAttributeValueElement();

      var attribute = sel.attribute();

      createAttributeValueSelector(attribute.name());

      addProto(ByteProto.SELECTOR_ATTRIBUTE_VALUE_MARK);
    } else if (element instanceof AttributeValueSelectorMark) {
      addProto(ByteProto.SELECTOR_ATTRIBUTE_VALUE_MARK);
    } else if (element instanceof ClassSelector sel) {
      addProtoString(
        ByteProto.SELECTOR_CLASS_OBJ,
        sel.className()
      );
    } else if (element instanceof ClassSelectorMark) {
      addProto(ByteProto.SELECTOR_CLASS_MARK);
    } else if (element instanceof Combinator c) {
      addProto(c.getCode());
      addProto(ByteProto.SELECTOR_COMBINATOR);
    } else if (element instanceof Declaration) {
      addProto(ByteProto.DECLARATION_MARK);
    } else if (element instanceof IdSelector sel) {
      addProtoString(
        ByteProto.SELECTOR_ID_OBJ,
        sel.id()
      );
    } else if (element instanceof IdSelectorMark) {
      addProto(ByteProto.SELECTOR_ID_MARK);
    } else if (element instanceof PseudoClassSelector sel) {
      addProto(sel.getCode());
      addProto(ByteProto.SELECTOR_PSEUDO_CLASS_OBJ);
    } else if (element instanceof PseudoElementSelector sel) {
      addProto(sel.getCode());
      addProto(ByteProto.SELECTOR_PSEUDO_ELEMENT_OBJ);
    } else if (element instanceof TypeSelector sel) {
      addProto(sel.getCode());
      addProto(ByteProto.SELECTOR_TYPE_OBJ);
    } else if (element instanceof UniversalSelector) {
      addProto(ByteProto.SELECTOR_UNIVERSAL_OBJ);
    } else {
      var type = element.getClass();

      throw new AssertionError("Unexpected RuleElement type: " + type);
    }
  }

  private void addDeclarationEnd() {
    addProto(ByteProto.DECLARATION_END);
  }

  private void addDeclarationStart(StandardPropertyName name) {
    addProto(name.getCode());
    addProto(ByteProto.DECLARATION_START);
  }

  private void addDoubleProto(double value) {
    addProto(doublesLength);
    addDouble(value);
  }

  private void addFunctionEnd() {
    addProto(ByteProto.FUNCTION_END);
  }

  private void addFunctionStart(StandardFunctionName name) {
    addProto(name.getCode());
    addProto(ByteProto.FUNCTION_START);
  }

  private void addObject(int code) {
    objects = IntArrays.growIfNecessary(objects, objectsLength);

    objects[objectsLength++] = code;
  }

  private void addProtoString(int code, String value) {
    addProto(strings.size());
    addProto(code);

    strings.add(value);
  }

  private void addValueDouble(double value) {
    addDoubleProto(value);
    addProto(ByteProto.VALUE_DOUBLE);
  }

  private void addValueInt(int value) {
    addProto(value);
    addProto(ByteProto.VALUE_INT);
  }

  private int getLastProto() {
    return protos[protosLength - 1];
  }

  private int popObject() {
    return objects[--objectsLength];
  }

}
