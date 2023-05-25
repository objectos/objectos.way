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

import br.com.objectos.css.select.Selector;
import javax.annotation.Generated;

@Generated("br.com.objectos.css.selfgen.SpecProcessor")
abstract class SelfgenRuleSetBuilder<SELF extends SelfgenRuleSetBuilder<SELF>> extends AbstractRuleSetBuilder<SELF> {
  SelfgenRuleSetBuilder(Selector selector) {
    super(selector);
  }

  public SELF margin(MarginWidthValue value) {
    return addDeclaration("margin", value);
  }

  public SELF margin(MarginWidthValue topBottom, MarginWidthValue rightLeft) {
    return addDeclaration("margin", topBottom, rightLeft);
  }

  public SELF margin(MarginWidthValue top, MarginWidthValue rightLeft, MarginWidthValue bottom) {
    return addDeclaration("margin", top, rightLeft, bottom);
  }

  public SELF margin(MarginWidthValue top, MarginWidthValue right, MarginWidthValue bottom,
      MarginWidthValue left) {
    return addDeclaration("margin", top, right, bottom, left);
  }

  public SELF margin(InheritKeyword value) {
    return addDeclaration("margin", value);
  }
}
