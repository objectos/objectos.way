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

import objectos.css.internal.NamedSelector;
import objectos.css.internal.StyleSheetBuilder;
import objectos.css.om.Selector;
import objectos.css.om.StyleRule;
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

  private static final NamedSelector named(String name) {
    return new NamedSelector(name);
  }

  private StyleSheetBuilder builder;

  protected CssTemplate() {}

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
    builder.addRule(
      StyleRule.of(selector)
    );
  }

}