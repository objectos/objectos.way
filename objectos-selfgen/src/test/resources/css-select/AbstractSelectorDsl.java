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

@Generated("br.com.objectos.css.boot.select.SelectorImplSpecProcessor")
abstract class AbstractSelectorDsl implements SelectorAlt, PseudoClassSelectorDsl, PseudoElementSelectorDsl {
  @Override
  public final PseudoClassSelectorAlt active() {
    return pseudoClassSelectorAlt("active");
  }

  @Override
  public final PseudoClassSelectorAlt defaultPseudoClass() {
    return pseudoClassSelectorAlt("default");
  }

  @Override
  public final PseudoClassSelectorAlt firstOfType() {
    return pseudoClassSelectorAlt("first-of-type");
  }

  abstract PseudoClassSelectorAlt pseudoClassSelectorAlt(String value);

  @Override
  public final PseudoElementSelectorAlt after() {
    return pseudoElementSelectorAlt("after");
  }

  @Override
  public final PseudoElementSelectorAlt firstLetter() {
    return pseudoElementSelectorAlt("first-letter");
  }

  abstract PseudoElementSelectorAlt pseudoElementSelectorAlt(String value);
}
