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
package objectos.css.select;

public abstract class ModeDsl {

  protected ModeDsl() {}

  protected abstract Selector getLastCompound();

  protected abstract Selector newCombinatorSelector(Selector selector);

  protected abstract Selector newCompoundSelector();

  protected abstract Selector newSimpleSelector();

  protected abstract void toCombinatorCompoundMode(Selector selector);

  protected abstract void toCombinatorSimpleMode(Selector selector);

  protected abstract void toCombinatorStartMode(Combinator combinator);

  protected abstract void toCombinatorStartMode(Combinator combinator, Selector selector);

  protected abstract void toCompoundSelectorMode(Selector selector);

  protected abstract void toSimpleSelectorMode(Selector selector);

}