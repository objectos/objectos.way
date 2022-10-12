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

import br.com.objectos.code.java.element.AbstractImmutableCodeElement;
import br.com.objectos.code.java.element.CodeElement;
import br.com.objectos.code.java.element.Keywords;
import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.java.type.NamedClassOrParameterized;
import objectos.lang.Check;

public final class ClassExtends extends AbstractImmutableCodeElement {

  private static final ClassExtends NONE = new ClassExtends();

  private ClassExtends(CodeElement... elements) {
    super(elements);
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private NamedClassOrParameterized type;

    private Builder() {}

    public final ClassExtends build() {
      return type != null ? buildSingleIfPossible() : NONE;
    }

    public final Builder setSuperclass(Class<?> superclass) {
      Check.notNull(superclass, "superclass == null");
      type = NamedClass.of(superclass);
      return this;
    }
    
    public final Builder setSuperclass(NamedClassOrParameterized superclass) {
      type = Check.notNull(superclass, "superclass == null");
      return this;
    }

    private ClassExtends buildSingleIfPossible() {
      return type.isVoid() ? NONE : buildSingle();
    }

    private ClassExtends buildSingle() {
      return new ClassExtends(
          space(),
          Keywords._extends(),
          space(),
          type
      );
    }


  }

}