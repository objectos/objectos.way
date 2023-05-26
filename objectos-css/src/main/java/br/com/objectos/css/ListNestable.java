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

class ListNestable implements Nestable {

  private final Nestable first;
  private final Nestable second;

  ListNestable(Nestable first, Nestable second) {
    this.first = first;
    this.second = second;
  }

  static ListNestable of(Nestable first, Nestable second) {
    return new ListNestable(first, second);
  }

  @Override
  public final Selector acceptSelectorBuilder(SelectorBuilder builder) {
    SelectorBuilder b0 = builder;
    SelectorBuilder b1 = builder.copy();

    Selector s0 = first.acceptSelectorBuilder(b0);
    Selector s1 = second.acceptSelectorBuilder(b1);

    return new ListSelector(s0, s1);
  }

  @Override
  public final boolean equals(Object obj) {
    if (!(obj instanceof ListNestable)) {
      return false;
    }
    ListNestable that = (ListNestable) obj;
    return Objects.equals(first, that.first)
        && Objects.equals(second, that.second);
  }

  @Override
  public final int hashCode() {
    return Objects.hash(first, second);
  }

  @Override
  public final String toString() {
    return first.toString() + ", " + second.toString();
  }

}