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
package br.com.objectos.css;

enum Combinator implements NestablePart {

  CHILD('>'),

  ADJACENT_SIBLING('+'),

  GENERAL_SIBLING('~');

  private final char value;

  private Combinator(char value) {
    this.value = value;
  }

  @Override
  public final void acceptSelectorBuilder(SelectorBuilder builder) {
    builder.addCombinator(value);
  }

}