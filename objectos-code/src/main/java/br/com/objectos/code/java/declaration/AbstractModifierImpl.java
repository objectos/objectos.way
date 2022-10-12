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

import br.com.objectos.code.java.io.CodeWriter;

abstract class AbstractModifierImpl implements Modifier {

  private final String value;

  AbstractModifierImpl(String value) {
    this.value = value;
  }

  @Override
  public final CodeWriter acceptCodeWriter(CodeWriter w) {
    w.writePreIndentation();
    w.write(value);
    return w;
  }

  @Override
  public final boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof AbstractModifierImpl)) {
      return false;
    }
    AbstractModifierImpl that = (AbstractModifierImpl) obj;
    return value.equals(that.value);
  }

  @Override
  public final int hashCode() {
    return value.hashCode();
  }

  @Override
  public final String toString() {
    return value;
  }

}
