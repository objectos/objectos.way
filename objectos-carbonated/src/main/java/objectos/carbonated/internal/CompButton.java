/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.carbonated.internal;

import objectos.carbonated.Button;
import objectos.css.util.ClassSelector;
import objectos.html.HtmlTemplate;
import objectos.html.tmpl.Instruction.ElementContents;
import objectos.lang.Check;

public final class CompButton implements Button {

  static final ClassSelector BTN = ClassSelector.randomClassSelector(5);

  static final ClassSelector SIZE_SM = ClassSelector.randomClassSelector(5);

  static final ClassSelector SIZE_MD = ClassSelector.randomClassSelector(5);

  static final ClassSelector SIZE_LG = ClassSelector.randomClassSelector(5);

  static final ClassSelector SIZE_XL = ClassSelector.randomClassSelector(5);

  static final ClassSelector SIZE_2XL = ClassSelector.randomClassSelector(5);

  static final ClassSelector KIND_PRIMARY = ClassSelector.randomClassSelector(5);

  private Object child;

  private Size size = Size.LARGE;

  private Button.Kind kind = Button.Kind.PRIMARY;

  @SuppressWarnings("unused")
  private final HtmlTemplate html = new HtmlTemplate() {
    @Override
    protected final void definition() {
      button(
        BTN,

        switch (size) {
          case SMALL -> SIZE_SM;

          case MEDIUM -> SIZE_MD;

          case LARGE -> SIZE_LG;

          case X_LARGE -> SIZE_XL;

          case X_LARGE_2 -> SIZE_2XL;
        },

        switch (kind) {
          case PRIMARY -> KIND_PRIMARY;
        },

        child instanceof String s
            ? t(s)
            : noop());
    }
  };

  @Override
  public final Button kind(Kind value) {
    kind = Check.notNull(value, "value == null");

    return this;
  }

  @Override
  public final ElementContents render() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final Button size(Size value) {
    size = Check.notNull(value, "value == null");

    return this;
  }

  @Override
  public final Button text(String value) {
    child = Check.notNull(value, "value == null");

    return this;
  }

}