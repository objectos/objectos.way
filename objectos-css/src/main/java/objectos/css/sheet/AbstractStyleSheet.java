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

import objectos.css.Css;
import objectos.css.function.StandardFunctionName;
import objectos.css.property.StandardPropertyName;
import objectos.css.select.AttributeValueOperator;
import objectos.css.select.Combinator;
import objectos.css.select.PseudoElementSelector;
import objectos.css.select.Selector;
import objectos.css.select.SelectorList;
import objectos.css.select.UniversalSelector;
import objectos.css.type.AngleType;
import objectos.css.type.AngleUnit;
import objectos.css.type.ColorKind;
import objectos.css.type.ColorType;
import objectos.css.type.Creator;
import objectos.css.type.DoubleType;
import objectos.css.type.FontFamilyValue;
import objectos.css.type.IntType;
import objectos.css.type.LengthType;
import objectos.css.type.LengthUnit;
import objectos.css.type.Marker;
import objectos.css.type.OutlineValue;
import objectos.css.type.PercentageType;
import objectos.css.type.StringType;
import objectos.css.type.UriType;
import objectos.css.type.Value;
import objectos.css.type.Zero;
import objectos.lang.Check;

public abstract class AbstractStyleSheet extends GeneratedStyleSheet
    implements
    StyleSheet {

  protected final static class CustomKeyword implements FontFamilyValue, OutlineValue {
    static final CustomKeyword INSTANCE = new CustomKeyword();

    private CustomKeyword() {}

    @Override
    public final void acceptValueCreator(Creator creator) {
      // noop
    }

    @Override
    public final void acceptValueMarker(Marker marker) {
      marker.markKeyword();
    }
  }

  private static class ThisAngleTypeDouble extends ThisValue implements AngleType {
    static final AngleType INSTANCE = new ThisAngleTypeDouble();

    @Override
    public final void acceptValueMarker(Marker marker) {
      marker.markDoubleAngle();
    }
  }

  private static class ThisAngleTypeInt extends ThisValue implements AngleType {
    static final AngleType INSTANCE = new ThisAngleTypeInt();

    @Override
    public final void acceptValueMarker(Marker marker) {
      marker.markIntAngle();
    }
  }

  private static class ThisAnyFunction extends ThisValue implements AnyFunction {
    static final AnyFunction INSTANCE = new ThisAnyFunction();

    @Override
    public final void acceptValueMarker(Marker marker) {
      marker.markFunction();
    }
  }

  private enum ThisColorType implements ColorType {
    HEX,

    RGB_DOUBLE,

    RGB_DOUBLE_ALPHA,

    RGB_INT,

    RGB_INT_ALPHA,

    RGBA_DOUBLE,

    RGBA_INT;

    private final ColorKind kind;

    private ThisColorType() {
      this.kind = ColorKind.valueOf(name());
    }

    @Override
    public final void acceptValueCreator(Creator creator) {
      // noop
    }

    @Override
    public final void acceptValueMarker(Marker marker) {
      marker.markColor(kind);
    }
  }

  private static class ThisDoubleType extends ThisValue implements DoubleType {
    static final DoubleType INSTANCE = new ThisDoubleType();

    @Override
    public final void acceptValueMarker(Marker marker) {
      marker.markDouble();
    }
  }

  private static class ThisIntType extends ThisValue implements IntType {
    static final IntType INSTANCE = new ThisIntType();

    @Override
    public final void acceptValueMarker(Marker marker) {
      marker.markInt();
    }
  }

  private static class ThisLengthTypeDouble extends ThisValue implements LengthType {
    static final LengthType INSTANCE = new ThisLengthTypeDouble();

    @Override
    public final void acceptValueMarker(Marker marker) {
      marker.markDoubleLength();
    }
  }

  private static class ThisLengthTypeInt extends ThisValue implements LengthType {
    static final LengthType INSTANCE = new ThisLengthTypeInt();

    @Override
    public final void acceptValueMarker(Marker marker) {
      marker.markIntLength();
    }
  }

  private static class ThisPercentageTypeDouble extends ThisValue implements PercentageType {
    static final PercentageType INSTANCE = new ThisPercentageTypeDouble();

    @Override
    public final void acceptValueMarker(Marker marker) {
      marker.markDoublePercentage();
    }
  }

  private static class ThisPercentageTypeInt extends ThisValue implements PercentageType {
    static final PercentageType INSTANCE = new ThisPercentageTypeInt();

    @Override
    public final void acceptValueMarker(Marker marker) {
      marker.markIntPercentage();
    }
  }

  private static class ThisStringType extends ThisValue implements StringType {
    static final StringType INSTANCE = new ThisStringType();

    @Override
    public final void acceptValueMarker(Marker marker) {
      marker.markString();
    }
  }

  private static class ThisUri extends ThisValue implements UriType {
    static final UriType INSTANCE = new ThisUri();

    @Override
    public final void acceptValueMarker(Marker marker) {
      marker.markUri();
    }
  }

  private abstract static class ThisValue implements Value {
    @Override
    public final void acceptValueCreator(Creator creator) {
      // noop
    }
  }

  protected static final MediaType all = MediaType.ALL;

  protected static final MediaType print = MediaType.PRINT;

  protected static final MediaType screen = MediaType.SCREEN;

  private StyleEngine engine;

  protected AbstractStyleSheet() {}

  @Override
  public final void eval(StyleEngine engine) {
    Check.state(this.engine == null, "Concurrent evaluation by multiple engines is not supported");

    this.engine = Check.notNull(engine, "engine == null");

    try {
      definition();
    } finally {
      this.engine = null;
    }
  }

  @Override
  public final String printMinified() {
    var writer = StyleSheetWriter.ofMinified();

    return writer.toString(this);
  }

  @Override
  public final String toString() {
    var writer = StyleSheetWriter.ofPretty();

    return writer.toString(this);
  }

  protected final RuleElement _webkitTextSizeAdjust(PercentageType pct) {
    // return Property.getStandard("-webkit-text-size-adjust", pct);
    throw new UnsupportedOperationException("Implement me");
  }

  protected final MediaType all() {
    return MediaType.ALL;
  }

  protected final AtMediaElement and(MediaExpression expression) {
    throw new UnsupportedOperationException("Implement me");
  }

  protected final UniversalSelector any() {
    return UniversalSelector.getInstance();
  }

  protected final RuleElement attr(String name) {
    engine.createAttributeSelector(name);

    return AttributeSelectorMark.INSTANCE;
  }

  protected final AttributeValueSelectorMark attr(String name, AttributeValueElementMark element) {
    engine.markAttributeValueElement();

    engine.createAttributeValueSelector(name);

    return AttributeValueSelectorMark.INSTANCE;
  }

  protected final RuleElement cn(String className) {
    engine.createClassSelector(className);

    return ClassSelectorMark.INSTANCE;
  }

  protected abstract void definition();

  protected final AttributeValueElementMark eq(String value) {
    engine.createAttributeValueElement(AttributeValueOperator.EQUALS, value);

    return AttributeValueElementMark.INSTANCE;
  }

  protected final Combinator gt() {
    return Combinator.CHILD;
  }

  protected final ColorType hex(String hex) {
    engine.createColor(hex);

    return ThisColorType.HEX;
  }

  protected final RuleElement id(String id) {
    engine.createIdSelector(id);

    return IdSelectorMark.INSTANCE;
  }

  protected final void install(StyleSheet sheet) {
    Check.notNull(sheet, "sheet == null");

    sheet.eval(engine);
  }

  protected final CustomKeyword keyword(String name) {
    engine.createKeyword(name);

    return CustomKeyword.INSTANCE;
  }

  protected final DoubleType l(double value) {
    engine.createDouble(value);

    return ThisDoubleType.INSTANCE;
  }

  protected final IntType l(int value) {
    engine.createInt(value);

    return ThisIntType.INSTANCE;
  }

  protected final StringType l(String value) {
    engine.createString(value);

    return ThisStringType.INSTANCE;
  }

  protected final SelectorList list(Selector... selectors) {
    return Css.list(selectors);
  }

  @Override
  protected final MaxHeightDeclarationMark maxHeight(LengthType length) {
    engine.addDeclaration(StandardPropertyName.MAX_HEIGHT, length);

    return MaxHeightDeclarationMark.INSTANCE;
  }

  @Override
  protected final MaxWidthDeclarationMark maxWidth(LengthType length) {
    engine.addDeclaration(StandardPropertyName.MAX_WIDTH, length);

    return MaxWidthDeclarationMark.INSTANCE;
  }

  protected final void media(AtMediaElement... elements) {
    engine.addAtMedia(elements);
  }

  @Override
  protected final MinHeightDeclarationMark minHeight(LengthType length) {
    engine.addDeclaration(StandardPropertyName.MIN_HEIGHT, length);

    return MinHeightDeclarationMark.INSTANCE;
  }

  @Override
  protected final MinWidthDeclarationMark minWidth(LengthType length) {
    engine.addDeclaration(StandardPropertyName.MIN_WIDTH, length);

    return MinWidthDeclarationMark.INSTANCE;
  }

  protected final Combinator or() {
    return Combinator.LIST;
  }

  protected final PercentageType pct(double value) {
    engine.createPercentage(value);

    return ThisPercentageTypeDouble.INSTANCE;
  }

  protected final PercentageType pct(int value) {
    engine.createPercentage(value);

    return ThisPercentageTypeInt.INSTANCE;
  }

  protected final Combinator plus() {
    return Combinator.ADJACENT_SIBLING;
  }

  protected final ColorType rgb(double r, double g, double b) {
    engine.createRgb(r, g, b);

    return ThisColorType.RGB_DOUBLE;
  }

  protected final ColorType rgb(double r, double g, double b, double alpha) {
    engine.createRgb(r, g, b, alpha);

    return ThisColorType.RGB_DOUBLE_ALPHA;
  }

  protected final ColorType rgb(int r, int g, int b) {
    engine.createRgb(r, g, b);

    return ThisColorType.RGB_INT;
  }

  protected final ColorType rgb(int r, int g, int b, double alpha) {
    engine.createRgb(r, g, b, alpha);

    return ThisColorType.RGB_INT_ALPHA;
  }

  protected final ColorType rgba(double r, double g, double b, double alpha) {
    engine.createRgba(r, g, b, alpha);

    return ThisColorType.RGBA_DOUBLE;
  }

  protected final ColorType rgba(int r, int g, int b, double alpha) {
    engine.createRgba(r, g, b, alpha);

    return ThisColorType.RGBA_INT;
  }

  protected final Combinator sp() {
    return Combinator.DESCENDANT;
  }

  protected final AttributeValueElementMark startsWith(String value) {
    engine.createAttributeValueElement(AttributeValueOperator.STARTS_WITH, value);

    return AttributeValueElementMark.INSTANCE;
  }

  protected final RuleMark style(RuleElement... elements) {
    addRule(elements);

    return RuleMark.INSTANCE;
  }

  protected final Combinator tilde() {
    return Combinator.GENERAL_SIBLING;
  }

  protected final UriType url(String value) {
    engine.createUri(value);

    return ThisUri.INSTANCE;
  }

  protected final PseudoElementSelector webkitFileUploadButton() {
    return Css._WEBKIT_FILE_UPLOAD_BUTTON;
  }

  protected final Zero zero() {
    return Zero.INSTANCE;
  }

  @Override
  final AnyDeclaration addDeclaration(StandardPropertyName name, double value) {
    engine.addDeclaration(name, value);

    return AnyDeclarationMark.INSTANCE;
  }

  @Override
  final AnyDeclaration addDeclaration(StandardPropertyName name, int value) {
    engine.addDeclaration(name, value);

    return AnyDeclarationMark.INSTANCE;
  }

  @Override
  final AnyDeclaration addDeclaration(
      StandardPropertyName name, MultiDeclarationElement... elements) {
    engine.addDeclaration(name, elements);

    return AnyDeclarationMark.INSTANCE;
  }

  @Override
  final AnyDeclaration addDeclaration(StandardPropertyName name, String value) {
    engine.addDeclaration(name, value);

    return AnyDeclarationMark.INSTANCE;
  }

  @Override
  final AnyDeclaration addDeclaration(StandardPropertyName name, Value v1) {
    engine.addDeclaration(name, v1);

    return AnyDeclarationMark.INSTANCE;
  }

  @Override
  final AnyDeclaration addDeclaration(StandardPropertyName name, Value v1, Value v2) {
    engine.addDeclaration(name, v1, v2);

    return AnyDeclarationMark.INSTANCE;
  }

  @Override
  final AnyDeclaration addDeclaration(StandardPropertyName name, Value v1, Value v2, Value v3) {
    engine.addDeclaration(name, v1, v2, v3);

    return AnyDeclarationMark.INSTANCE;
  }

  @Override
  final AnyDeclaration addDeclaration(
      StandardPropertyName name, Value v1, Value v2, Value v3, Value v4) {
    engine.addDeclaration(name, v1, v2, v3, v4);

    return AnyDeclarationMark.INSTANCE;
  }

  @Override
  final AnyDeclaration addDeclaration(
      StandardPropertyName name, Value v1, Value v2, Value v3, Value v4, Value v5) {
    engine.addDeclaration(name, v1, v2, v3, v4, v5);

    return AnyDeclarationMark.INSTANCE;
  }

  @Override
  final AnyDeclaration addDeclaration(
      StandardPropertyName name, Value v1, Value v2, Value v3, Value v4, Value v5, Value v6) {
    engine.addDeclaration(name, v1, v2, v3, v4, v5, v6);

    return AnyDeclarationMark.INSTANCE;
  }

  @Override
  final AnyFunction addFunction(StandardFunctionName name, Value v1) {
    engine.addFunction(name, v1);

    return ThisAnyFunction.INSTANCE;
  }

  @Override
  final AngleType getAngle(AngleUnit unit, double value) {
    engine.createAngle(unit, value);

    return ThisAngleTypeDouble.INSTANCE;
  }

  @Override
  final AngleType getAngle(AngleUnit unit, int value) {
    engine.createAngle(unit, value);

    return ThisAngleTypeInt.INSTANCE;
  }

  @Override
  final LengthType getLength(LengthUnit unit, double value) {
    engine.createLength(unit, value);

    return ThisLengthTypeDouble.INSTANCE;
  }

  @Override
  final LengthType getLength(LengthUnit unit, int value) {
    engine.createLength(unit, value);

    return ThisLengthTypeInt.INSTANCE;
  }

  private void addRule(RuleElement... elements) {
    engine.addRule(elements);
  }

}