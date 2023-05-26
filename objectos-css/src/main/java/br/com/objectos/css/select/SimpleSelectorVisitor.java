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

public interface SimpleSelectorVisitor<R, P> {

  R visitAttributeSelector(AttributeSelector selector, P p);

  R visitAttributeValueSelector(AttributeValueSelector selector, P p);

  R visitClassSelector(ClassSelector selector, P p);

  R visitIdSelector(IdSelector selector, P p);

  R visitPseudoClassSelector(PseudoClassSelector selector, P p);

  R visitPseudoElementSelector(PseudoElementSelector selector, P p);

  R visitTypeSelector(TypeSelector selector, P p);

}
