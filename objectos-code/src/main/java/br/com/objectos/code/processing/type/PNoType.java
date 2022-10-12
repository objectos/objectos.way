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
package br.com.objectos.code.processing.type;

import br.com.objectos.code.java.type.NamedType;
import br.com.objectos.code.java.type.NamedVoid;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.NoType;

public final class PNoType extends PTypeMirror {

  private final NoType type;

  PNoType(ProcessingEnvironment processingEnv, NoType type) {
    super(processingEnv);
    this.type = type;
  }

  @Override
  public final NamedType getName() {
    return NamedVoid._void();
  }

  @Override
  public final boolean isNoType() {
    return true;
  }

  @Override
  public final PNoType toNoType() {
    return this;
  }

  @Override
  final NoType getType() {
    return type;
  }

}
