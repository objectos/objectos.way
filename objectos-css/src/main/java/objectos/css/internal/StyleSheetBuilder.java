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

import java.util.Objects;
import objectos.css.om.Selector;
import objectos.css.om.StyleDeclaration;
import objectos.css.om.StyleSheet;
import objectos.lang.Check;
import objectos.util.GrowableList;

public class StyleSheetBuilder {

  private final GrowableList<Object> rules = new GrowableList<>();

  public final void addStyleRule(Selector selector,
      StyleDeclaration[] declarations) {
    int length = declarations.length;

    var rule = switch (length) {
      case 0 -> new StyleRule0(selector);

      case 1 -> new StyleRule1(
        selector,
        Objects.requireNonNull(declarations[0], "declarations[0] == null")
      );

      default -> {
        var copy = new StyleDeclaration[length];

        for (int i = 0; i < length; i++) {
          copy[i] = Check.notNull(declarations[i], "declarations[", i, "] == null");
        }

        yield new StyleRuleN(selector, copy);
      }
    };

    rules.add(rule);
  }

  public final void addStyleRule(Selector selector1, Selector selector2, Selector selector3,
      StyleDeclaration[] declarations) {

    addStyleRule(
      Combinator.LIST.combine(
        selector1,
        Combinator.LIST.combine(selector2, selector3)
      ),
      declarations
    );
  }

  public final void addStyleRule(
      Selector selector1, Selector selector2, Selector selector3,
      Selector selector4, Selector selector5, Selector selector6,
      StyleDeclaration[] declarations) {

    addStyleRule(
      Combinator.LIST.combine(
        selector1,
        Combinator.LIST.combine(
          selector2,
          Combinator.LIST.combine(
            selector3,
            Combinator.LIST.combine(
              selector4,
              Combinator.LIST.combine(
                selector5,
                selector6
              )
            )
          )
        )
      ),
      declarations
    );
  }

  public final StyleSheet build() {
    return new StyleSheetImpl(
      rules.toUnmodifiableList()
    );
  }

}