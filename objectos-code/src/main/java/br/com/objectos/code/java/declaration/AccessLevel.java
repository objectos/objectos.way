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

import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import objectos.lang.Check;

public enum AccessLevel implements ConstructorCodeElement {

  PUBLIC,

  DEFAULT,

  PROTECTED,

  PRIVATE;

  public static AccessLevel of(Element element) {
    Check.notNull(element, "element == null");
    
    return ofModifiersUnchecked(element.getModifiers());
  }

  private static AccessLevel ofModifiersUnchecked(Set<Modifier> modifiers) {
    if (modifiers.contains(Modifier.PUBLIC)) {
      return PUBLIC;
    }

    if (modifiers.contains(Modifier.PROTECTED)) {
      return PROTECTED;
    }

    if (modifiers.contains(Modifier.PRIVATE)) {
      return PRIVATE;
    }

    return DEFAULT;
  }

  @Override
  public final void acceptConstructorCodeBuilder(ConstructorCode.Builder builder) {
    switch (this) {
      case PUBLIC:
        builder.addModifier(Modifiers.PUBLIC);
        break;
      case PROTECTED:
        builder.addModifier(Modifiers.PROTECTED);
        break;
      case PRIVATE:
        builder.addModifier(Modifiers.PRIVATE);
        break;
      default:
        // noop
        break;
    }
  }

}