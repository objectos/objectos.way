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
import objectos.css.internal.StyleSheetBuilder;
import objectos.css.om.Selector;
import objectos.css.om.StyleDeclaration;
import objectos.css.om.StyleSheet;

public abstract class CssTemplate2 extends GeneratedCssTemplate {

  private StyleSheetBuilder builder;

  protected CssTemplate2() {}

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

  @Override
  public String toString() {
    return toStyleSheet().toString();
  }

  protected final void style(Selector selector,
      StyleDeclaration... declarations) {
    Objects.requireNonNull(selector, "selector == null");
    Objects.requireNonNull(declarations, "declarations == null");

    builder.addStyleRule(selector, declarations);
  }

  protected final void style(Selector selector1, Selector selector2,
      StyleDeclaration... declarations) {
    Objects.requireNonNull(selector1, "selector1 == null");
    Objects.requireNonNull(selector2, "selector2 == null");
    Objects.requireNonNull(declarations, "declarations == null");

    builder.addStyleRule(selector1, selector2, declarations);
  }

  protected final void style(
      Selector selector1, Selector selector2, Selector selector3,
      StyleDeclaration... declarations) {
    Objects.requireNonNull(selector1, "selector1 == null");
    Objects.requireNonNull(selector2, "selector2 == null");
    Objects.requireNonNull(selector3, "selector3 == null");
    Objects.requireNonNull(declarations, "declarations == null");

    builder.addStyleRule(selector1, selector2, selector3, declarations);
  }

  protected final void style(
      Selector selector1, Selector selector2, Selector selector3,
      Selector selector4,
      StyleDeclaration... declarations) {
    Objects.requireNonNull(selector1, "selector1 == null");
    Objects.requireNonNull(selector2, "selector2 == null");
    Objects.requireNonNull(selector3, "selector3 == null");
    Objects.requireNonNull(selector4, "selector4 == null");
    Objects.requireNonNull(declarations, "declarations == null");

    builder.addStyleRule(
      selector1, selector2, selector3,
      selector4,
      declarations
    );
  }

  protected final void style(
      Selector selector1, Selector selector2, Selector selector3,
      Selector selector4, Selector selector5, Selector selector6,
      StyleDeclaration... declarations) {
    Objects.requireNonNull(selector1, "selector1 == null");
    Objects.requireNonNull(selector2, "selector2 == null");
    Objects.requireNonNull(selector3, "selector3 == null");
    Objects.requireNonNull(selector4, "selector4 == null");
    Objects.requireNonNull(selector5, "selector5 == null");
    Objects.requireNonNull(selector6, "selector6 == null");
    Objects.requireNonNull(declarations, "declarations == null");

    builder.addStyleRule(
      selector1, selector2, selector3,
      selector4, selector5, selector6,
      declarations
    );
  }

}