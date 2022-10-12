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

public final class FinalModifier extends AbstractModifierImpl
    implements
    ClassModifier,
    FieldModifier,
    MethodModifier,
    ParameterModifier {

  static final FinalModifier INSTANCE = new FinalModifier();

  private FinalModifier() {
    super("final");
  }

  @Override
  public final void acceptClassCodeBuilder(ClassCode.Builder builder) {
    builder.addModifier(this);
  }

  @Override
  public final FieldCode.Builder acceptFieldCodeBuilder(FieldCode.Builder builder) {
    return builder._final();
  }

  @Override
  public final void acceptMethodCodeBuilder(MethodCode.Builder builder) {
    builder.uncheckedAddModifier(this);
  }

}
