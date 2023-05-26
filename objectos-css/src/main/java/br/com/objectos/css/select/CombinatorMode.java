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

abstract class CombinatorMode extends Mode {

  static final CombinatorMode START = new StartMode();

  private static final CombinatorMode COMPOUND = new CompoundMode();

  private static final CombinatorMode SIMPLE = new SimpleMode();

  CombinatorMode() {}

  private static class CompoundMode extends CompoundOrSimpleMode {
    @Override
    final Selector newSelector(ModeDsl dsl) {
      return dsl.newCompoundSelector();
    }
  }

  private static class SimpleMode extends CompoundOrSimpleMode {
    @Override
    final Selector newSelector(ModeDsl dsl) {
      return dsl.newSimpleSelector();
    }
  }

  private abstract static class CompoundOrSimpleMode extends CombinatorMode {

    @Override
    public final Mode addCombinator(ModeDsl dsl, Combinator combinator) {
      Selector selector;
      selector = newSelector(dsl);

      dsl.toCombinatorStartMode(combinator, selector);

      return START;
    }

    @Override
    public final Mode addSimpleSelector(ModeDsl dsl, SimpleSelector s) {
      Selector last;
      last = dsl.getLastCompound();

      s.acceptSimpleSelectorVisitor(SimpleSelectorChecker.INSTANCE, last);

      dsl.toCombinatorCompoundMode(s);

      return COMPOUND;
    }

    @Override
    public final Mode addUniversalSelector(ModeDsl dsl, UniversalSelector s) {
      throw new UnsupportedOperationException("Implement me");
    }

    @Override
    final Selector build(ModeDsl dsl) {
      Selector selector;
      selector = newSelector(dsl);

      return dsl.newCombinatorSelector(selector);
    }

    abstract Selector newSelector(ModeDsl dsl);

  }

  private static class StartMode extends CombinatorMode {

    @Override
    public final Mode addCombinator(ModeDsl dsl, Combinator combinator) {
      throw new IllegalArgumentException("Two consecutives combinators");
    }

    @Override
    public final Mode addSimpleSelector(ModeDsl dsl, SimpleSelector s) {
      dsl.toCombinatorSimpleMode(s);
      return SIMPLE;
    }

    @Override
    public final Mode addUniversalSelector(ModeDsl dsl, UniversalSelector s) {
      dsl.toCombinatorSimpleMode(s);
      return SIMPLE;
    }

  }

}