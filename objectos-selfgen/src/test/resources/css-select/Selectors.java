/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package br.com.objectos.css.select;

import javax.annotation.Generated;

@Generated("br.com.objectos.css.boot.select.StaticTypeProcessor")
public final class Selectors {
  public static final TypeSelectorAlt a = new TypeSelectorImpl("a");

  public static final TypeSelectorAlt div = new TypeSelectorImpl("div");

  private static final PseudoElementSelectorAlt $attribute_after = new SimplePseudoElementSelectorImpl("after");

  private static final PseudoElementSelectorAlt $attribute_firstLetter = new SimplePseudoElementSelectorImpl("first-letter");

  private Selectors() {
  }

  public static TypeSelectorAlt tag(String name) {
    return new TypeSelectorImpl(name);
  }

  public static PseudoElementSelectorAlt after() {
    return $attribute_after;
  }

  public static PseudoElementSelectorAlt firstLetter() {
    return $attribute_firstLetter;
  }
}
