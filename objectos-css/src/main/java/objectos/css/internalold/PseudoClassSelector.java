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
package objectos.css.internalold;

import objectos.css.pseudom.PSelectorElement.PPseudoClassSelector;
import objectos.css.tmpl.StyleRuleElement;

// generate me
public enum PseudoClassSelector implements StyleRuleElement, PPseudoClassSelector {

  ACTIVE(":active"),

  VISITED(":visited");

  private static final PseudoClassSelector[] VALUES = PseudoClassSelector.values();

  private final String toString;

  private PseudoClassSelector(String toString) {
    this.toString = toString;
  }

  public static PseudoClassSelector ofOrdinal(int value) {
    return VALUES[value];
  }

  @Override
  public final String toString() {
    return toString;
  }

}
