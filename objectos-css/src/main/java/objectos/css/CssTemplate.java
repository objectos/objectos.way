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
import objectos.css.internal.StyleSheetBuilder;
import objectos.css.om.Selector;
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

  private StyleSheetBuilder builder;

  protected CssTemplate() {}

  private static final NamedSelector named(String name) {
    return new NamedSelector(name);
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

  protected final IdSelector id(String id) {
    return IdSelector.of(id);
  }

  protected final void style(Selector selector) {
    Objects.requireNonNull(selector, "selector == null");

    builder.addStyleRule(selector);
  }

  protected final void style(Selector selector1, Selector selector2, Selector selector3) {
    Objects.requireNonNull(selector1, "selector1 == null");
    Objects.requireNonNull(selector2, "selector2 == null");
    Objects.requireNonNull(selector3, "selector3 == null");

    builder.addStyleRule(selector1, selector2, selector3);
  }

}