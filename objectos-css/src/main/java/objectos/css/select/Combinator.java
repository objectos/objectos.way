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
package objectos.css.select;

import objectos.css.parser.IsTerminal;
import objectos.util.UnmodifiableList;

public enum Combinator implements SelectorElement, IsTerminal {

  ADJACENT_SIBLING('+', "plus") {
    @Override
    final Selector combine(UnmodifiableList<Selector> selectors) {
      return new AdjacentSiblingSelector(selectors);
    }
  },

  CHILD('>', "gt") {
    @Override
    final Selector combine(UnmodifiableList<Selector> selectors) {
      return new ChildSelector(selectors);
    }
  },

  DESCENDANT(' ', "sp") {
    @Override
    final Selector combine(UnmodifiableList<Selector> selectors) {
      return new DescendantSelector(selectors);
    }
  },

  GENERAL_SIBLING('~', "tilde") {
    @Override
    final Selector combine(UnmodifiableList<Selector> selectors) {
      return new GeneralSiblingSelector(selectors);
    }
  },

  LIST(',', "or") {
    @Override
    final Selector combine(UnmodifiableList<Selector> selectors) {
      return new SelectorList(selectors);
    }
  };

  private static final Combinator[] ARRAY = values();

  private final String javaName;

  public final char symbol;

  private Combinator(char symbol, String javaName) {
    this.symbol = symbol;
    this.javaName = javaName;
  }

  public static Combinator getByCode(int code) {
    return ARRAY[code];
  }

  @Override
  public final void acceptSelectorBuilderDsl(Selector.BuilderDsl builder) {
    builder.addCombinator(this);
  }

  public final int getCode() {
    return ordinal();
  }

  public final String getJavaName() {
    return javaName;
  }

  abstract Selector combine(UnmodifiableList<Selector> selectors);

}