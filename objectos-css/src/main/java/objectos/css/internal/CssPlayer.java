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

import objectos.css.pseudom.StyleSheetProcessor;

public class CssPlayer extends CssRecorder {

  @FunctionalInterface
  private interface PseudoFactory<T> {
    T create(CssPlayer player);
  }

  public CssPlayer() {
  }

  protected final void executePlayer(StyleSheetProcessor processor) {
    var sheet = pseudoStyleSheet();

    processor.process(sheet.init());
  }

  final int cas(int state, int expected, int newState) {
    if (state != expected) {
      throw new IllegalStateException(
        """
        Found state '%d' but expected state '%d'
        """.formatted(state, expected)
      );
    }

    return newState;
  }

  final Object objectGet(int index) {
    return objectArray[index];
  }

  final int protoGet(int index) {
    return protoArray[index];
  }

  final int protoLast() {
    return protoArray[protoIndex - 1];
  }

  final boolean protoMore(int index) {
    return index < protoIndex;
  }

  final PAttributeSelectorImpl pseudoAttributeNameSelector() {
    return pseudoFactory(PATTRIBUTE_NAME_SELECTOR, PAttributeSelectorImpl::new);
  }

  final PAttributeValueSelectorImpl pseudoAttributeValueSelector() {
    return pseudoFactory(PATTRIBUTE_VALUE_SELECTOR, PAttributeValueSelectorImpl::new);
  }

  final PClassSelectorImpl pseudoClassSelector() {
    return pseudoFactory(PCLASS_SELECTOR, PClassSelectorImpl::new);
  }

  final PseudoSelector pseudoSelector() {
    return pseudoFactory(PSELECTOR, PseudoSelector::new);
  }

  final PseudoStyleRule pseudoStyleRule() {
    return pseudoFactory(PSTYLE_RULE, PseudoStyleRule::new);
  }

  final PseudoStyleSheet pseudoStyleSheet() {
    return pseudoFactory(PSTYLE_SHEET, PseudoStyleSheet::new);
  }

  @SuppressWarnings("unchecked")
  private <T> T pseudoFactory(int index, PseudoFactory<T> factory) {
    if (objectArray[index] == null) {
      objectArray[index] = factory.create(this);
    }

    return (T) objectArray[index];
  }

}