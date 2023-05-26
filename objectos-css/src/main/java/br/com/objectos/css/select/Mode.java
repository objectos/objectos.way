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

public abstract class Mode {

  private static final Mode START = new StartMode();

  Mode() {}

  public static Mode start() {
    return START;
  }

  @Override
  public final String toString() {
    Class<? extends Mode> modeClass;
    modeClass = getClass();

    return modeClass.getSimpleName();
  }

  public Mode addCombinator(ModeDsl dsl, Combinator combinator) {
    throw new UnsupportedOperationException("Implement me @ " + getClass());
  }

  public abstract Mode addSimpleSelector(ModeDsl dsl, SimpleSelector s);

  public abstract Mode addUniversalSelector(ModeDsl dsl, UniversalSelector s);

  Selector build(ModeDsl dsl) {
    throw new UnsupportedOperationException("Implement me @ " + getClass());
  }

  final InvalidSelectorException
      newCannotAppendUniversalSelector(ModeDsl dsl) {
    return InvalidSelectorException.get("Cannot append universal selector to '%s'", build(dsl));
  }

  final IllegalStateException newCannotCombineToEmptySelectorException() {
    return new IllegalStateException("Cannot combine to an empty Selector.");
  }

  private static class StartMode extends Mode {

    private StartMode() {}

    @Override
    public final Mode addSimpleSelector(ModeDsl dsl, SimpleSelector s) {
      return toSimpleSelectorMode(dsl, s);
    }

    @Override
    public final Mode addUniversalSelector(ModeDsl dsl, UniversalSelector s) {
      return toSimpleSelectorMode(dsl, s);
    }

    @Override
    final Selector build(ModeDsl dsl) {
      return UniversalSelector.getInstance();
    }

    private Mode toSimpleSelectorMode(ModeDsl dsl, Selector selector) {
      dsl.toSimpleSelectorMode(selector);
      return SimpleSelectorMode.INSTANCE;
    }

  }

}