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

public enum SiblingOperator {

  ADJACENT('+') {
    @Override
    boolean matches(Selector selector, Selectable element) {
      // return selector.secondMatches(element)
      // && element.siblings().previous()
      // .filter(el -> selector.firstMatches(el))
      // .isPresent();
      throw new UnsupportedOperationException("Implement me");
    }
  },

  GENERAL('~') {
    @Override
    boolean matches(Selector selector, Selectable element) {
      // return selector.firstMatches(element)
      // && siblingStream(element)
      // .filter(el -> selector.secondMatches(el))
      // .findFirst()
      // .isPresent();
      throw new UnsupportedOperationException("Implement me");
    }
  };

  private final char symbol;
  private final String separator;

  private SiblingOperator(char symbol) {
    this.symbol = symbol;
    separator = " " + symbol + " ";
  }

  public char symbol() {
    return symbol;
  }

  abstract boolean matches(Selector selectors, Selectable element);

  String separator() {
    return separator;
  }

}