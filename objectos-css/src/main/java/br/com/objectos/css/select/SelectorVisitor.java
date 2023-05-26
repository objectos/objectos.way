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

public interface SelectorVisitor<R, P> extends SimpleSelectorVisitor<R, P> {

  R visitAdjacentSiblingSelector(AdjacentSiblingSelector selector, P p);

  R visitChildSelector(ChildSelector selector, P p);

  R visitCompoundSelector(CompoundSelector selector, P p);

  R visitDescendantSelector(DescendantSelector selector, P p);

  R visitSelectorList(SelectorList list, P p);

  R visitUniversalSelector(UniversalSelector selector, P p);

}
