/*
 * Copyright (C) 2014-2023 Objectos Software LTDA.
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
package objectos.code.internal;

import objectos.code.tmpl.ClassOrInterfaceDeclarationInstruction;
import objectos.code.tmpl.FinalModifier;

/**
 * Represents a modifier of the Java language.
 *
 * @since 0.4.2
 */
public final class ModifierImpl extends External
    implements
    ClassOrInterfaceDeclarationInstruction,
    FinalModifier {
  final int value;

  public ModifierImpl(Keyword keyword) {
    this.value = keyword.ordinal();
  }

  @Override
  public final void execute(InternalApi api) {
    api.extStart();
    api.protoAdd(ByteProto.MODIFIER, value);
  }
}