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
package br.com.objectos.css.sheet;

import br.com.objectos.css.function.StandardFunctionName;
import br.com.objectos.css.keyword.Keywords;
import br.com.objectos.css.property.StandardPropertyName;
import br.com.objectos.css.select.AttributeValueOperator;
import br.com.objectos.css.select.Combinator;
import br.com.objectos.css.select.PseudoClassSelectors;
import br.com.objectos.css.select.PseudoElementSelectors;
import br.com.objectos.css.select.TypeSelectors;
import br.com.objectos.css.select.UniversalSelector;
import br.com.objectos.css.sheet.StyleSheet.Processor;
import br.com.objectos.css.type.AngleUnit;
import br.com.objectos.css.type.Color;
import br.com.objectos.css.type.LengthUnit;
import java.util.ArrayDeque;
import java.util.Deque;
import objectos.util.IntArrays;

final class Pass2 implements AutoCloseable {

  @FunctionalInterface
  private interface Action {
    void execute();
  }

  private static final int _START = 1;

  private static final int _STOP = 2;

  static final int _AT_MEDIA_BODY = 3;

  private static final int _AT_MEDIA_START = 4;

  private static final int _DECLARATION_START = 5;

  private static final int _DECLARATION_VALUES = 6;

  private static final int _FUNCTION_START = 7;

  private static final int _FUNCTION_VALUES = 8;

  private static final int _MEDIA_QUERY = 12;

  private static final int _MEDIA_QUERY_DECLARATION = 13;

  private static final int _SELECTOR = 14;

  static final int _SHEET_BODY = 15;

  private final Deque<Body> bodyStack = new ArrayDeque<>(3);

  private int[] call = new int[64];

  private int callIndex = -1;

  private int state;

  private int cursor;

  private PassSource source;

  private Processor processor;

  protected Pass2() {}

  @Override
  public final void close() {
    bodyStack.clear();

    callIndex = -1;

    cursor = 0;

    state = 0;
  }

  public final void execute(PassSource source, Processor processor) {
    this.source = source;
    this.processor = processor;

    cursor = 0;

    state = _START;

    while (state != _STOP) {
      execute0();
    }
  }

  final Body peekBody() {
    return bodyStack.peek();
  }

  final Body popBody() {
    return bodyStack.pop();
  }

  final void pushBody(Body body) {
    bodyStack.push(body);
  }

  private void doDeclarationStart() {
    var code = nextCode();

    var name = StandardPropertyName.getByCode(code);

    state = switch (state) {
      //      case _AT_MEDIA_BODY -> {
      //        processor.visitMediaEnd();
      //
      //        popBody();
      //
      //        var body = peekBody();
      //
      //        yield body.state;
      //      }

      case _DECLARATION_VALUES -> {
        processor.visitBeforeNextDeclaration();

        processor.visitDeclarationStart(name);

        yield _DECLARATION_START;
      }

      case _MEDIA_QUERY -> {
        processor.visitLogicalExpressionStart(LogicalOperator.AND);

        processor.visitDeclarationStart(name);

        yield _MEDIA_QUERY_DECLARATION;
      }

      case _SELECTOR -> {
        processor.visitBlockStart();

        processor.visitDeclarationStart(name);

        yield _DECLARATION_START;
      }

      default -> throw new IllegalArgumentException("Unexpected state: " + state);
    };
  }

  private void doFlowJmp() {
    int jumpTo = nextCode();

    pushCall();

    cursor = jumpTo;
  }

  private void doFlowReturn() {
    if (callIndex >= 0) {
      cursor = popCall();
    } else {
      state = _STOP;
    }
  }

  private void doFunctionEnd() {
    state = switch (state) {
      case _FUNCTION_VALUES -> {
        processor.visitFunctionEnd();

        yield _DECLARATION_VALUES;
      }

      default -> throw new IllegalArgumentException("Unexpected state: " + state);
    };
  }

  private void doFunctionStart() {
    var code = nextCode();

    var name = StandardFunctionName.getByCode(code);

    state = switch (state) {
      case _DECLARATION_START -> {
        processor.visitFunctionStart(name);

        yield _FUNCTION_START;
      }

      case _DECLARATION_VALUES -> {
        processor.visitBeforeNextValue();

        processor.visitFunctionStart(name);

        yield _FUNCTION_START;
      }

      default -> throw new IllegalArgumentException("Unexpected state: " + state);
    };
  }

  private void doMediaEnd() {
    state = switch (state) {
      case _AT_MEDIA_BODY -> {
        processor.visitMediaEnd();

        popBody();

        var body = peekBody();

        yield body.state;
      }

      default -> throw new IllegalArgumentException("Unexpected state: " + state);
    };
  }

  private void doMediaStart() {
    state = switch (state) {
      case _SHEET_BODY -> {
        processor.visitMediaStart();

        yield _AT_MEDIA_START;
      }

      case _START -> {
        pushBody(Body.SHEET);

        processor.visitMediaStart();

        yield _AT_MEDIA_START;
      }

      default -> throw new IllegalArgumentException("Unexpected state: " + state);
    };
  }

  private void doMediaType() {
    state = switch (state) {
      case _AT_MEDIA_START -> {
        var code = nextCode();

        var type = MediaType.getByCode(code);

        processor.visitMediaType(type);

        yield _MEDIA_QUERY;
      }

      default -> throw new IllegalArgumentException("Unexpected state: " + state);
    };
  }

  private void doMultiDeclarationSeparator() {
    state = switch (state) {
      case _DECLARATION_START -> state;

      case _DECLARATION_VALUES -> {
        processor.visitMultiDeclarationSeparator();

        yield _DECLARATION_START;
      }

      default -> throw new IllegalArgumentException("Unexpected state: " + state);
    };
  }

  private void doRuleEnd() {
    state = switch (state) {
      case _DECLARATION_VALUES -> {
        processor.visitAfterLastDeclaration();

        processor.visitRuleEnd();

        var body = peekBody();

        yield body.state;
      }

      case _SELECTOR -> {
        processor.visitEmptyBlock();

        var body = peekBody();

        yield body.state;
      }

      default -> throw new IllegalArgumentException("Unexpected state: " + state);
    };
  }

  private void doRuleStart() {
    state = switch (state) {
      case _AT_MEDIA_BODY, _SHEET_BODY -> state;

      case _MEDIA_QUERY -> {
        pushBody(Body.MEDIA);

        processor.visitBlockStart();

        yield state;
      }

      case _START -> {
        pushBody(Body.SHEET);

        yield state;
      }

      default -> throw new IllegalArgumentException("Unexpected state: " + state);
    };
  }

  private void doSelector0() {
    switch (state) {
      case _AT_MEDIA_BODY -> processor.visitRuleStart();

      case _MEDIA_QUERY -> processor.visitRuleStart();

      case _SHEET_BODY -> processor.visitRuleStart();

      case _SELECTOR -> {}

      case _START -> processor.visitRuleStart();

      default -> throw new IllegalArgumentException("Unexpected state: " + state);
    }

    state = _SELECTOR;
  }

  private void doSelectorAttribute() {
    doSelector0();

    var attributeName = getString();

    processor.visitAttributeSelector(attributeName);
  }

  private void doSelectorAttributeValue() {
    doSelector0();

    var attributeName = getString();

    var opCode = nextCode();

    var operator = AttributeValueOperator.getByCode(opCode);

    var value = getString();

    processor.visitAttributeValueSelector(attributeName, operator, value);
  }

  private void doSelectorClass() {
    var className = getString();

    doSelector0();

    processor.visitClassSelector(className);
  }

  private void doSelectorCombinator() {
    doSelector0();

    var code = nextCode();

    var combinator = Combinator.getByCode(code);

    processor.visitCombinator(combinator);
  }

  private void doSelectorId() {
    doSelector0();

    var id = getString();

    processor.visitIdSelector(id);
  }

  private void doSelectorPseudoClass() {
    doSelector0();

    var code = nextCode();

    var selector = PseudoClassSelectors.getByCode(code);

    processor.visitSimpleSelector(selector);
  }

  private void doSelectorPseudoElement() {
    doSelector0();

    var code = nextCode();

    var selector = PseudoElementSelectors.getByCode(code);

    processor.visitSimpleSelector(selector);
  }

  private void doSelectorType() {
    doSelector0();

    var code = nextCode();

    var selector = TypeSelectors.getByCode(code);

    processor.visitSimpleSelector(selector);
  }

  private void doSelectorUniversal() {
    doSelector0();

    var selector = UniversalSelector.getInstance();

    processor.visitUniversalSelector(selector);
  }

  private <T> void doValue0(Action action) {
    state = switch (state) {
      case _DECLARATION_START -> {
        action.execute();

        yield _DECLARATION_VALUES;
      }

      case _DECLARATION_VALUES -> {
        processor.visitBeforeNextValue();

        action.execute();

        yield _DECLARATION_VALUES;
      }

      case _FUNCTION_START -> {
        action.execute();

        yield _FUNCTION_VALUES;
      }

      case _FUNCTION_VALUES -> {
        processor.visitBeforeNextValue();

        action.execute();

        yield _FUNCTION_VALUES;
      }

      case _MEDIA_QUERY_DECLARATION -> {
        action.execute();

        processor.visitLogicalExpressionEnd();

        yield _MEDIA_QUERY;
      }

      default -> throw new IllegalArgumentException("Unexpected state: " + state);
    };
  }

  private void doValueAngleDouble() {
    var unit = getAngleUnit();

    var value = getDouble();

    doValue0(() -> processor.visitAngle(unit, value));
  }

  private void doValueAngleInt() {
    var unit = getAngleUnit();

    var value = nextCode();

    doValue0(() -> processor.visitAngle(unit, value));
  }

  private void doValueColorHex() {
    var hex = getString();

    doValue0(() -> processor.visitColor(hex));
  }

  private void doValueColorName() {
    var code = nextCode();

    var color = Color.getByCode(code);

    doValue0(() -> processor.visitColor(color));
  }

  private void doValueDouble() {
    var value = getDouble();

    doValue0(() -> processor.visitDouble(value));
  }

  private void doValueInt() {
    var value = nextCode();

    doValue0(() -> processor.visitInt(value));
  }

  private void doValueKeyword() {
    var code = nextCode();

    var keyword = Keywords.getByCode(code);

    doValue0(() -> processor.visitKeyword(keyword));
  }

  private void doValueKeywordCustom() {
    var keyword = getString();

    doValue0(() -> processor.visitKeyword(keyword));
  }

  private void doValueLengthDouble() {
    var unit = getLengthUnit();

    var value = getDouble();

    doValue0(() -> processor.visitLength(unit, value));
  }

  private void doValueLengthInt() {
    var unit = getLengthUnit();

    var value = nextCode();

    doValue0(() -> processor.visitLength(unit, value));
  }

  private void doValuePercentageDouble() {
    var value = getDouble();

    doValue0(() -> processor.visitPercentage(value));
  }

  private void doValuePercentageInt() {
    var value = nextCode();

    doValue0(() -> processor.visitPercentage(value));
  }

  private void doValueRgbaDouble() {
    var doubleStartIndex = nextCode();

    var r = getDouble(doubleStartIndex++);
    var g = getDouble(doubleStartIndex++);
    var b = getDouble(doubleStartIndex++);
    var alpha = getDouble(doubleStartIndex);

    doValue0(() -> processor.visitRgba(r, g, b, alpha));
  }

  private void doValueRgbaInt() {
    var r = nextCode();
    var g = nextCode();
    var b = nextCode();
    var alpha = getDouble();

    doValue0(() -> processor.visitRgba(r, g, b, alpha));
  }

  private void doValueRgbDouble() {
    var doubleStartIndex = nextCode();

    var r = getDouble(doubleStartIndex++);
    var g = getDouble(doubleStartIndex++);
    var b = getDouble(doubleStartIndex++);

    doValue0(() -> processor.visitRgb(r, g, b));
  }

  private void doValueRgbDoubleAlpha() {
    var doubleStartIndex = nextCode();

    var r = getDouble(doubleStartIndex++);
    var g = getDouble(doubleStartIndex++);
    var b = getDouble(doubleStartIndex++);
    var alpha = getDouble(doubleStartIndex);

    doValue0(() -> processor.visitRgb(r, g, b, alpha));
  }

  private void doValueRgbInt() {
    var r = nextCode();
    var g = nextCode();
    var b = nextCode();

    doValue0(() -> processor.visitRgb(r, g, b));
  }

  private void doValueRgbIntAlpha() {
    var r = nextCode();
    var g = nextCode();
    var b = nextCode();
    var alpha = getDouble();

    doValue0(() -> processor.visitRgb(r, g, b, alpha));
  }

  private void doValueString() {
    var value = getString();

    doValue0(() -> processor.visitString(value));
  }

  private void doValueUri() {
    var value = getString();

    doValue0(() -> processor.visitUri(value));
  }

  private void execute0() {
    var code = nextCode();

    switch (code) {
      case ByteCode.DECLARATION_START -> doDeclarationStart();

      case ByteCode.FLOW_JMP -> doFlowJmp();

      case ByteCode.FLOW_RETURN -> doFlowReturn();

      case ByteCode.FUNCTION_END -> doFunctionEnd();

      case ByteCode.FUNCTION_START -> doFunctionStart();

      case ByteCode.MEDIA_END -> doMediaEnd();

      case ByteCode.MEDIA_START -> doMediaStart();

      case ByteCode.MEDIA_TYPE -> doMediaType();

      case ByteCode.MULTI_DECLARATION_SEPARATOR -> doMultiDeclarationSeparator();

      case ByteCode.SELECTOR_ATTRIBUTE -> doSelectorAttribute();

      case ByteCode.SELECTOR_ATTRIBUTE_VALUE -> doSelectorAttributeValue();

      case ByteCode.SELECTOR_CLASS -> doSelectorClass();

      case ByteCode.SELECTOR_COMBINATOR -> doSelectorCombinator();

      case ByteCode.SELECTOR_ID -> doSelectorId();

      case ByteCode.SELECTOR_PSEUDO_CLASS -> doSelectorPseudoClass();

      case ByteCode.SELECTOR_PSEUDO_ELEMENT -> doSelectorPseudoElement();

      case ByteCode.SELECTOR_TYPE -> doSelectorType();

      case ByteCode.SELECTOR_UNIVERSAL -> doSelectorUniversal();

      case ByteCode.ROOT -> { /*noop*/ }

      case ByteCode.RULE_END -> doRuleEnd();

      case ByteCode.RULE_START -> doRuleStart();

      case ByteCode.VALUE_ANGLE_DOUBLE -> doValueAngleDouble();

      case ByteCode.VALUE_ANGLE_INT -> doValueAngleInt();

      case ByteCode.VALUE_COLOR_HEX -> doValueColorHex();

      case ByteCode.VALUE_COLOR_NAME -> doValueColorName();

      case ByteCode.VALUE_DOUBLE -> doValueDouble();

      case ByteCode.VALUE_INT -> doValueInt();

      case ByteCode.VALUE_LENGTH_DOUBLE -> doValueLengthDouble();

      case ByteCode.VALUE_LENGTH_INT -> doValueLengthInt();

      case ByteCode.VALUE_KEYWORD -> doValueKeyword();

      case ByteCode.VALUE_KEYWORD_CUSTOM -> doValueKeywordCustom();

      case ByteCode.VALUE_PERCENTAGE_DOUBLE -> doValuePercentageDouble();

      case ByteCode.VALUE_PERCENTAGE_INT -> doValuePercentageInt();

      case ByteCode.VALUE_RGB_DOUBLE -> doValueRgbDouble();

      case ByteCode.VALUE_RGB_DOUBLE_ALPHA -> doValueRgbDoubleAlpha();

      case ByteCode.VALUE_RGB_INT -> doValueRgbInt();

      case ByteCode.VALUE_RGB_INT_ALPHA -> doValueRgbIntAlpha();

      case ByteCode.VALUE_RGBA_DOUBLE -> doValueRgbaDouble();

      case ByteCode.VALUE_RGBA_INT -> doValueRgbaInt();

      case ByteCode.VALUE_STRING -> doValueString();

      case ByteCode.VALUE_URI -> doValueUri();

      default -> throw new UnsupportedOperationException("Implement me: " + code);
    }
  }

  private AngleUnit getAngleUnit() {
    int unitCode;
    unitCode = nextCode();

    return AngleUnit.getByCode(unitCode);
  }

  private double getDouble() {
    int index;
    index = nextCode();

    return getDouble(index);
  }

  private double getDouble(int index) {
    return source.doubleAt(index);
  }

  private LengthUnit getLengthUnit() {
    int unitCode;
    unitCode = nextCode();

    return LengthUnit.getByCode(unitCode);
  }

  private String getString() {
    return getString(nextCode());
  }

  private String getString(int index) {
    return source.stringAt(index);
  }

  private int nextCode() {
    return source.codeAt(cursor++);
  }

  private int popCall() {
    return call[callIndex--];
  }

  private void pushCall() {
    callIndex++;

    call = IntArrays.growIfNecessary(call, callIndex);

    call[callIndex] = cursor;
  }

}
