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

public class AbstractSelectorVisitor<R, P> implements SelectorVisitor<R, P> {

  @Override
  public R visitAdjacentSiblingSelector(AdjacentSiblingSelector selector, P p) {
    return defaultAction(selector, p);
  }

  @Override
  public R visitAttributeSelector(AttributeSelector selector, P p) {
    return defaultAction(selector, p);
  }

  @Override
  public R visitAttributeValueSelector(AttributeValueSelector selector, P p) {
    return defaultAction(selector, p);
  }

  @Override
  public R visitChildSelector(ChildSelector selector, P p) {
    return defaultAction(selector, p);
  }

  @Override
  public R visitClassSelector(ClassSelector selector, P p) {
    return defaultAction(selector, p);
  }

  @Override
  public R visitCompoundSelector(CompoundSelector selector, P p) {
    return defaultAction(selector, p);
  }

  @Override
  public R visitDescendantSelector(DescendantSelector selector, P p) {
    return defaultAction(selector, p);
  }

  @Override
  public R visitIdSelector(IdSelector selector, P p) {
    return defaultAction(selector, p);
  }

  @Override
  public R visitPseudoClassSelector(PseudoClassSelector selector, P p) {
    return defaultAction(selector, p);
  }

  @Override
  public R visitPseudoElementSelector(PseudoElementSelector selector, P p) {
    return defaultAction(selector, p);
  }

  @Override
  public R visitSelectorList(SelectorList list, P p) {
    return defaultAction(list, p);
  }

  @Override
  public R visitTypeSelector(TypeSelector selector, P p) {
    return defaultAction(selector, p);
  }

  @Override
  public R visitUniversalSelector(UniversalSelector selector, P p) {
    return defaultAction(selector, p);
  }

  protected R defaultAction(Selector selector, P p) {
    return null;
  }

}
