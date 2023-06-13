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

  protected interface GlobalKeyword extends BoxSizingValue {}

  private record Keyword(String name) implements GlobalKeyword {
    @Override
    public final String toString() {
      return name;
    }
  }

  // B
  protected static final BoxSizingValue borderBox = kw("border-box");

  // C
  protected static final BoxSizingValue contentBox = kw("content-box");

  // I
  protected static final GlobalKeyword inherit = kw("inherit");
  protected static final GlobalKeyword initial = kw("initial");

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

  // property methods: box-sizing

  protected interface BoxSizingValue extends PropertyValue {}

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