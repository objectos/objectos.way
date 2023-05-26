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

import java.util.Arrays;
import java.util.List;

class SimpleNestable implements Nestable {

  private final Iterable<NestablePart> parts;

  SimpleNestable(Iterable<NestablePart> parts) {
    this.parts = parts;
  }

  static SimpleNestable of(NestablePart... parts) {
    List<NestablePart> iterable = Arrays.asList(parts);
    return new SimpleNestable(iterable);
  }

  @Override
  public final Selector acceptSelectorBuilder(SelectorBuilder builder) {
    for (NestablePart part : parts) {
      part.acceptSelectorBuilder(builder);
    }
    return builder.build();
  }

  @Override
  public final boolean equals(Object obj) {
    if (!(obj instanceof SimpleNestable)) {
      return false;
    }
    SimpleNestable that = (SimpleNestable) obj;

    return Iterables.equals(parts, that.parts);
  }

  @Override
  public final int hashCode() {
    return parts.hashCode();
  }

  @Override
  public final String toString() {
    return parts.toString();
  }

}
