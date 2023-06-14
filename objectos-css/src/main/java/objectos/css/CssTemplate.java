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
package objectos.css;

import java.util.Objects;
import objectos.css.internal.NamedSelector;
import objectos.css.internal.Property;
import objectos.css.internal.StyleSheetBuilder;
import objectos.css.om.PropertyValue;
import objectos.css.om.Selector;
import objectos.css.om.StyleDeclaration;
import objectos.css.om.StyleSheet;

public abstract class CssTemplate {

  // type selectors

  protected static final Selector a = named("a");

  protected static final Selector body = named("body");

  protected static final Selector html = named("html");

  protected static final Selector li = named("li");

  protected static final Selector ul = named("ul");

  // pseudo element selectors

  protected static final Selector __after = named("::after");

  protected static final Selector __before = named("::before");

  // universal selector

  protected static final Selector any = named("*");

  // keywords

  protected sealed interface GlobalKeyword
      extends
      // B
      BoxSizingValue,

      // C
      Color,

      // L
      LineHeightValue,
      LineStyle,
      LineWidth,

      // T
      TextSizeAdjustValue {}

  private record Keyword(String name) implements GlobalKeyword, NoneKeyword {
    @Override
    public final String toString() {
      return name;
    }
  }

  // zero

  protected static final class Zero implements LineWidth {
    static final Zero INSTANCE = new Zero();

    private Zero() {}

    @Override
    public final String toString() { return "0"; }
  }

  protected static final Zero $0 = Zero.INSTANCE;

  // A
  protected static final Color aqua = kw("aqua");
  protected static final TextSizeAdjustValue auto = kw("auto");

  // B
  protected static final BoxSizingValue borderBox = kw("border-box");
  protected static final Color black = kw("black");
  protected static final Color blue = kw("blue");

  // C
  protected static final BoxSizingValue contentBox = kw("content-box");
  protected static final Color currentcolor = kw("currentcolor");

  // D
  protected static final LineStyle dashed = kw("dashed");
  protected static final LineStyle dotted = kw("dotted");
  protected static final LineStyle double$ = kw("double");

  // F
  protected static final Color fuchsia = kw("fuchsia");

  // G
  protected static final Color gray = kw("gray");
  protected static final Color green = kw("green");
  protected static final LineStyle groove = kw("groove");

  // H
  protected static final LineStyle hidden = kw("hidden");

  // I
  protected static final GlobalKeyword inherit = kw("inherit");
  protected static final GlobalKeyword initial = kw("initial");
  protected static final LineStyle inset = kw("inset");

  // L
  protected static final Color lime = kw("lime");

  // M
  protected static final Color maroon = kw("maroon");
  protected static final LineWidth medium = kw("medium");

  // N
  protected sealed interface NoneKeyword extends LineStyle, TextSizeAdjustValue {}

  protected static final Color navy = kw("navy");
  protected static final NoneKeyword none = kw("none");
  protected static final LineHeightValue normal = kw("normal");

  // O
  protected static final Color olive = kw("olive");
  protected static final LineStyle outset = kw("outset");

  // P
  protected static final Color purple = kw("purple");

  // R
  protected static final Color red = kw("red");
  protected static final LineStyle ridge = kw("ridge");

  // S
  protected static final Color silver = kw("silver");
  protected static final LineStyle solid = kw("solid");

  // T
  protected static final Color teal = kw("teal");
  protected static final LineWidth thick = kw("thick");
  protected static final LineWidth thin = kw("thin");
  protected static final Color transparent = kw("transparent");

  // U
  protected static final GlobalKeyword unset = kw("unset");

  // W
  protected static final Color white = kw("white");

  // Y
  protected static final Color yellow = kw("yellow");

  private StyleSheetBuilder builder;

  protected CssTemplate() {}

  private static final NamedSelector named(String name) {
    return new NamedSelector(name);
  }

  private static final Keyword kw(String name) {
    return new Keyword(name);
  }

  public final StyleSheet toStyleSheet() {
    try {
      builder = new StyleSheetBuilder();

      definition();

      return builder.build();
    } finally {
      builder = null;
    }
  }

  @Override
  public String toString() {
    return toStyleSheet().toString();
  }

  protected abstract void definition();

  // selector methods

  protected final IdSelector id(String id) {
    return IdSelector.of(id);
  }

  // length methods

  protected static final class Length implements LineHeightValue {
    static final Length ZERO = new Length("0");

    final String value;

    public Length(String value) { this.value = value; }

    @Override
    public final String toString() { return value; }
  }

  private enum LengthUnit {
    ch,
    cm,
    em,
    ex,
    in,
    mm,
    pc,
    pt,
    px,
    q,
    rem,
    vh,
    vmax,
    vmin,
    vw;

    final Length of(int value) {
      if (value == 0) {
        return Length.ZERO;
      } else {
        var s = Integer.toString(value);

        return new Length(s + name());
      }
    }
  }

  protected final Length em(int value) {
    return LengthUnit.em.of(value);
  }

  protected final Length px(int value) {
    return LengthUnit.px.of(value);
  }

  // percentage method

  protected static final class Percentage implements LineHeightValue, TextSizeAdjustValue {
    static final Percentage ZERO = new Percentage("0");

    final String value;

    public Percentage(String value) { this.value = value; }

    @Override
    public final String toString() { return value; }
  }

  protected final Percentage pct(double value) {
    if (value == 0) {
      return Percentage.ZERO;
    } else {
      var s = Double.toString(value);

      return new Percentage(s + "%");
    }
  }

  protected final Percentage pct(int value) {
    if (value == 0) {
      return Percentage.ZERO;
    } else {
      var s = Integer.toString(value);

      return new Percentage(s + "%");
    }
  }

  // property methods

  // property methods: border-color

  protected sealed interface Color extends PropertyValue {}

  protected final StyleDeclaration borderColor(Color all) {
    return Property.BORDER_COLOR.sides1(all);
  }

  protected final StyleDeclaration borderColor(Color vertical, Color horizontal) {
    return Property.BORDER_COLOR.sides2(vertical, horizontal);
  }

  protected final StyleDeclaration borderColor(Color top, Color horizontal,
      Color bottom) {
    return Property.BORDER_COLOR.sides3(top, horizontal, bottom);
  }

  protected final StyleDeclaration borderColor(Color top, Color right, Color bottom,
      Color left) {
    return Property.BORDER_COLOR.sides4(top, right, bottom, left);
  }

  // property methods: border-style

  protected sealed interface LineStyle extends PropertyValue {}

  protected final StyleDeclaration borderStyle(LineStyle all) {
    return Property.BORDER_STYLE.sides1(all);
  }

  protected final StyleDeclaration borderStyle(LineStyle vertical, LineStyle horizontal) {
    return Property.BORDER_STYLE.sides2(vertical, horizontal);
  }

  protected final StyleDeclaration borderStyle(LineStyle top, LineStyle horizontal,
      LineStyle bottom) {
    return Property.BORDER_STYLE.sides3(top, horizontal, bottom);
  }

  protected final StyleDeclaration borderStyle(LineStyle top, LineStyle right, LineStyle bottom,
      LineStyle left) {
    return Property.BORDER_STYLE.sides4(top, right, bottom, left);
  }

  // property methods: border-width

  protected sealed interface LineWidth extends PropertyValue {}

  protected final StyleDeclaration borderWidth(LineWidth all) {
    return Property.BORDER_WIDTH.sides1(all);
  }

  protected final StyleDeclaration borderWidth(LineWidth vertical, LineWidth horizontal) {
    return Property.BORDER_WIDTH.sides2(vertical, horizontal);
  }

  protected final StyleDeclaration borderWidth(LineWidth top, LineWidth horizontal,
      LineWidth bottom) {
    return Property.BORDER_WIDTH.sides3(top, horizontal, bottom);
  }

  protected final StyleDeclaration borderWidth(LineWidth top, LineWidth right, LineWidth bottom,
      LineWidth left) {
    return Property.BORDER_WIDTH.sides4(top, right, bottom, left);
  }

  // property methods: box-sizing

  protected sealed interface BoxSizingValue extends PropertyValue {}

  protected final StyleDeclaration boxSizing(BoxSizingValue value) {
    return Property.BOX_SIZING.value(value);
  }

  // property methods: line-height

  protected sealed interface LineHeightValue extends PropertyValue {}

  protected final StyleDeclaration lineHeight(int value) {
    return Property.LINE_HEIGHT.value(value);
  }

  protected final StyleDeclaration lineHeight(double value) {
    return Property.LINE_HEIGHT.value(value);
  }

  protected final StyleDeclaration lineHeight(LineHeightValue value) {
    return Property.LINE_HEIGHT.value(value);
  }

  // property methods: text-size-adjust

  protected sealed interface TextSizeAdjustValue extends PropertyValue {}

  protected final StyleDeclaration webkitTextSizeAdjust(TextSizeAdjustValue value) {
    return Property._WEBKIT_TEXT_SIZE_ADJUST.value(value);
  }

  protected final void style(Selector selector,
      StyleDeclaration... declarations) {
    Objects.requireNonNull(selector, "selector == null");
    Objects.requireNonNull(declarations, "declarations == null");

    builder.addStyleRule(selector, declarations);
  }

  protected final void style(Selector selector1, Selector selector2, Selector selector3,
      StyleDeclaration... declarations) {
    Objects.requireNonNull(selector1, "selector1 == null");
    Objects.requireNonNull(selector2, "selector2 == null");
    Objects.requireNonNull(selector3, "selector3 == null");
    Objects.requireNonNull(declarations, "declarations == null");

    builder.addStyleRule(selector1, selector2, selector3, declarations);
  }

}