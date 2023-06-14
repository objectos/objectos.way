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

      // L
      LineStyle,
      LineWidth {}

  private record Keyword(String name) implements GlobalKeyword {
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

  // B
  protected static final BoxSizingValue borderBox = kw("border-box");

  // C
  protected static final BoxSizingValue contentBox = kw("content-box");

  // D
  protected static final LineStyle dashed = kw("dashed");
  protected static final LineStyle dotted = kw("dotted");
  protected static final LineStyle double$ = kw("double");

  // G
  protected static final LineStyle groove = kw("groove");

  // H
  protected static final LineStyle hidden = kw("hidden");

  // I
  protected static final GlobalKeyword inherit = kw("inherit");
  protected static final GlobalKeyword initial = kw("initial");
  protected static final LineStyle inset = kw("inset");

  // M
  protected static final LineWidth medium = kw("medium");

  // N
  protected static final LineStyle none = kw("none");

  // O
  protected static final LineStyle outset = kw("outset");

  // R
  protected static final LineStyle ridge = kw("ridge");

  // S
  protected static final LineStyle solid = kw("solid");

  // T
  protected static final LineWidth thick = kw("thick");
  protected static final LineWidth thin = kw("thin");

  // U
  protected static final GlobalKeyword unset = kw("unset");

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

  protected abstract void definition();

  // selector methods

  protected final IdSelector id(String id) {
    return IdSelector.of(id);
  }

  // property methods

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
    Objects.requireNonNull(value, "value == null");

    return Property.BOX_SIZING.declaration(value);
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