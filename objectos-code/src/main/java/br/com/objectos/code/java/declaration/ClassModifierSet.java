/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
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
package br.com.objectos.code.java.declaration;

import objectos.util.UnmodifiableList;

public final class ClassModifierSet extends ElementModifierSet {

  private static final ClassModifierSet EMPTY = new ClassModifierSet(
      UnmodifiableList.<Modifier> of()
  );

  private ClassModifierSet(Builder builder) {
    super(builder);
  }

  private ClassModifierSet(UnmodifiableList<Modifier> values) {
    super(values);
  }

  public static Builder _abstract() {
    return builder()._abstract();
  }

  public static Builder _final() {
    return builder()._final();
  }

  public static Builder _private() {
    return builder()._private();
  }

  public static Builder _protected() {
    return builder()._protected();
  }

  public static Builder _public() {
    return builder()._public();
  }

  public static Builder _static() {
    return builder()._static();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static ClassModifierSet empty() {
    return EMPTY;
  }

  public static final class Builder extends ElementModifierSet.Builder<ClassModifierSet> {

    private Builder() {}

    public final Builder _abstract() {
      return with(Modifiers._abstract());
    }

    public final Builder _final() {
      return with(Modifiers._final());
    }

    public final Builder _private() {
      return with(Modifiers._private());
    }

    public final Builder _protected() {
      return with(Modifiers._protected());
    }

    public final Builder _public() {
      return with(Modifiers._public());
    }

    public final Builder _static() {
      return with(Modifiers._static());
    }

    public final Builder _strictfp() {
      return with(Modifiers._strictfp());
    }

    public final Builder add(ClassModifier... modifiers) {
      addModifier(modifiers);
      return this;
    }

    public final Builder addAll(Iterable<? extends ClassModifier> modifiers) {
      addModifiers(modifiers);
      return this;
    }

    @Override
    public final ClassModifierSet build() {
      return new ClassModifierSet(this);
    }

    private Builder with(Modifier modifier) {
      addModifier(modifier);
      return this;
    }

  }

}