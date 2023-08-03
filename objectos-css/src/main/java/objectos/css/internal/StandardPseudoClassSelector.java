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
package objectos.css.internal;

import objectos.css.tmpl.Api.SelectorInstruction;
import objectos.lang.Generated;

@Generated("objectos.selfgen.CssSpec")
public enum StandardPseudoClassSelector implements SelectorInstruction {
  _disabled(":disabled"),

  _firstChild(":first-child"),

  _firstOfType(":first-of-type"),

  _focus(":focus"),

  _hover(":hover"),

  _mozFocusring(":-moz-focusring"),

  _mozUiInvalid(":-moz-ui-invalid"),

  _root(":root");

  private static final StandardPseudoClassSelector[] VALUES = values();

  public final String cssName;

  private StandardPseudoClassSelector(String cssName) {
    this.cssName = cssName;
  }

  public static StandardPseudoClassSelector ofOrdinal(int ordinal) {
    return VALUES[ordinal];
  }

  @Override
  public final String toString() {
    return cssName;
  }
}
