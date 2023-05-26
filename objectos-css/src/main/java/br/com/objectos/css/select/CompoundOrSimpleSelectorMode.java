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

abstract class CompoundOrSimpleSelectorMode extends Mode {

  CompoundOrSimpleSelectorMode() {}

  @Override
  public final Mode addCombinator(ModeDsl dsl, Combinator combinator) {
    dsl.toCombinatorStartMode(combinator);
    return CombinatorMode.START;
  }

  @Override
  public final Mode addSimpleSelector(ModeDsl dsl, SimpleSelector s) {
    Selector last;
    last = dsl.getLastCompound();

    s.acceptSimpleSelectorVisitor(SimpleSelectorChecker.INSTANCE, last);

    return toCompoundSelectorMode(dsl, s);
  }

  @Override
  public final Mode addUniversalSelector(ModeDsl dsl, UniversalSelector s) {
    throw newCannotAppendUniversalSelector(dsl);
  }

  private Mode toCompoundSelectorMode(ModeDsl dsl, Selector selector) {
    dsl.toCompoundSelectorMode(selector);
    return CompoundSelectorMode.INSTANCE;
  }

}