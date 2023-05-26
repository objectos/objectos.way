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

import java.util.Objects;

class ListSelector extends AbstractSelector {

  private final Selector first;
  private final Selector second;

  ListSelector(Selector first, Selector second) {
    this.first = first;
    this.second = second;
  }

  @Override
  public final boolean equals(Object obj) {
    if (!(obj instanceof ListSelector)) {
      return false;
    }
    ListSelector that = (ListSelector) obj;
    return Objects.equals(first, that.first)
        && Objects.equals(second, that.second);
  }

  @Override
  public final int hashCode() {
    return Objects.hash(first, second);
  }

  @Override
  public final SelectorBuilder selectorBuilder() {
    return new ThisBuilder();
  }

  @Override
  public final String toString() {
    return first.toString() + ", " + second.toString();
  }

  private class ThisBuilder implements SelectorBuilder {

    private final SelectorBuilder b0;
    private final SelectorBuilder b1;

    ThisBuilder() {
      this(first.selectorBuilder(), second.selectorBuilder());
    }

    ThisBuilder(SelectorBuilder b0, SelectorBuilder b1) {
      this.b0 = b0;
      this.b1 = b1;
    }

    @Override
    public final void addCombinator(char value) {
      b0.addCombinator(value);
      b1.addCombinator(value);
    }

    @Override
    public final void addDescendant() {
      b0.addDescendant();
      b1.addDescendant();
    }

    @Override
    public final void addIdent(String value) {
      b0.addIdent(value);
      b1.addIdent(value);
    }

    @Override
    public final void addParent() {
      b0.addParent();
      b1.addParent();
    }

    @Override
    public final Selector build() {
      return new ListSelector(b0.build(), b1.build());
    }

    @Override
    public final SelectorBuilder copy() {
      return new ThisBuilder(b0.copy(), b1.copy());
    }

  }

}