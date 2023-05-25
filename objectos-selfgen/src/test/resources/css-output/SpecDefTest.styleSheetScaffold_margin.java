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
package css.test;

import br.com.objectos.css.Declaration;
import br.com.objectos.css.Value;
import br.com.objectos.css.selfgen.Selectors;
import br.com.objectos.css.select.PseudoElementSelector;
import br.com.objectos.css.select.TypeSelector;
import javax.annotation.Generated;

@Generated("br.com.objectos.css.selfgen.SpecProcessor")
abstract class GeneratedStyleSheet {
  protected static final TypeSelector a = Selectors.a;

  protected static final TypeSelector div = Selectors.div;

  protected static final DoubleKeyword doubleKeyword = Keywords.doubleKeyword;

  protected final PseudoElementSelector after() {
    return Selectors.after();
  }

  protected final PseudoElementSelector firstLetter() {
    return Selectors.firstLetter();
  }

  protected final Declaration margin(MarginWidthValue value) {
    return declaration("margin", value);
  }

  protected final Declaration margin(MarginWidthValue topBottom, MarginWidthValue rightLeft) {
    return declaration("margin", topBottom, rightLeft);
  }

  protected final Declaration margin(MarginWidthValue top, MarginWidthValue rightLeft,
      MarginWidthValue bottom) {
    return declaration("margin", top, rightLeft, bottom);
  }

  protected final Declaration margin(MarginWidthValue top, MarginWidthValue right,
      MarginWidthValue bottom, MarginWidthValue left) {
    return declaration("margin", top, right, bottom, left);
  }

  protected final Declaration margin(InheritKeyword value) {
    return declaration("margin", value);
  }

  protected abstract Zero zero();

  abstract Declaration declaration(String property, Value... values);

  protected interface Zero extends Value {
  }
}
