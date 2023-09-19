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

@Generated("br.com.objectos.css.boot.SpecProcessor")
abstract class SelfgenRuleSetBuilder<SELF extends SelfgenRuleSetBuilder<SELF>> extends AbstractRuleSetBuilder<SELF> {
  SelfgenRuleSetBuilder(Selector selector) {
    super(selector);
  }

  public SELF backgroundAttachment(ScrollKeyword value) {
    return addDeclaration("background-attachment", value);
  }

  public SELF backgroundAttachment(FixedKeyword value) {
    return addDeclaration("background-attachment", value);
  }

  public SELF backgroundAttachment(InheritKeyword value) {
    return addDeclaration("background-attachment", value);
  }

  public SELF border(BorderWidthValue value) {
    return addDeclaration("border", value);
  }

  public SELF border(BorderWidthValue topBottom, BorderWidthValue rightLeft) {
    return addDeclaration("border", topBottom, rightLeft);
  }

  public SELF border(BorderWidthValue top, BorderWidthValue rightLeft, BorderWidthValue bottom) {
    return addDeclaration("border", top, rightLeft, bottom);
  }

  public SELF border(BorderWidthValue top, BorderWidthValue right, BorderWidthValue bottom,
      BorderWidthValue left) {
    return addDeclaration("border", top, right, bottom, left);
  }

  public SELF border(InheritKeyword value) {
    return addDeclaration("border", value);
  }

  public SELF borderTopWidth(BorderWidthValue value) {
    return addDeclaration("border-top-width", value);
  }

  public SELF borderTopWidth(InheritKeyword value) {
    return addDeclaration("border-top-width", value);
  }
}
