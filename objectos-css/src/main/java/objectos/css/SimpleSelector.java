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

class SimpleSelector extends AbstractSelector {

  private final String value;

  SimpleSelector(String value) {
    this.value = value;
  }

  @Override
  public final boolean equals(Object obj) {
    if (!(obj instanceof SimpleSelector)) {
      return false;
    }
    SimpleSelector that = (SimpleSelector) obj;
    return value.equals(that.value);
  }

  @Override
  public final int hashCode() {
    return value.hashCode();
  }

  @Override
  public final SelectorBuilder selectorBuilder() {
    return new ThisBuilder();
  }

  @Override
  public final String toString() {
    return value;
  }

  private class ThisBuilder implements SelectorBuilder {

    private final StringBuilder sb;

    ThisBuilder() {
      this(new StringBuilder());
    }

    ThisBuilder(StringBuilder sb) {
      this.sb = sb;
    }

    @Override
    public final void addCombinator(char value) {
      sb.append(' ').append(value).append(' ');
    }

    @Override
    public final void addDescendant() {
      sb.append(' ');
    }

    @Override
    public final void addIdent(String value) {
      sb.append(value);
    }

    @Override
    public final void addParent() {
      sb.append(value);
    }

    @Override
    public final Selector build() {
      return new SimpleSelector(sb.toString());
    }

    @Override
    public final SelectorBuilder copy() {
      StringBuilder copy = new StringBuilder(sb.toString());
      return new ThisBuilder(copy);
    }

  }

}