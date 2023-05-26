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
package objectos.css;

import java.util.Objects;

class Ident implements NestablePart {

  private final String value;

  Ident(String value) {
    this.value = value;
  }

  @Override
  public final void acceptSelectorBuilder(SelectorBuilder builder) {
    builder.addIdent(value);
  }

  @Override
  public final boolean equals(Object obj) {
    if (!(obj instanceof Ident)) {
      return false;
    }
    Ident that = (Ident) obj;
    return Objects.equals(value, that.value);
  }

  @Override
  public final int hashCode() {
    return value.hashCode();
  }

  @Override
  public final String toString() {
    return "Ident(" + value + ")";
  }

}