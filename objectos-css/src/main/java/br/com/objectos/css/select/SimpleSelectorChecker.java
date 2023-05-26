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
package br.com.objectos.css.select;

final class SimpleSelectorChecker implements SimpleSelectorVisitor<Void, Selector> {

  static final SimpleSelectorChecker INSTANCE = new SimpleSelectorChecker();

  private SimpleSelectorChecker() {}

  @Override
  public final Void visitAttributeSelector(AttributeSelector selector, Selector p) {
    p.uncheckedAddAttributeSelector(selector);
    return null;
  }

  @Override
  public final Void visitAttributeValueSelector(AttributeValueSelector selector, Selector p) {
    p.uncheckedAddAttributeValueSelector(selector);
    return null;
  }

  @Override
  public final Void visitClassSelector(ClassSelector selector, Selector p) {
    p.uncheckedAddClassSelector(selector);
    return null;
  }

  @Override
  public final Void visitIdSelector(IdSelector selector, Selector p) {
    p.uncheckedAddIdSelector(selector);
    return null;
  }

  @Override
  public final Void visitPseudoClassSelector(PseudoClassSelector selector, Selector p) {
    p.uncheckedAddPseudoClassSelector(selector);
    return null;
  }

  @Override
  public final Void visitPseudoElementSelector(PseudoElementSelector selector, Selector p) {
    p.uncheckedAddPseudoElementSelector(selector);
    return null;
  }

  @Override
  public final Void visitTypeSelector(TypeSelector selector, Selector p) {
    p.uncheckedAddTypeSelector(selector);
    return null;
  }

}
