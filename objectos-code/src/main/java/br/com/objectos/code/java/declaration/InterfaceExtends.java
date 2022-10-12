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
import br.com.objectos.code.java.type.NamedClassOrParameterized;
import objectos.util.GrowableList;

public final class InterfaceExtends extends AbstractImmutableCodeElement {

  private static final InterfaceExtends NONE = new InterfaceExtends();

  private InterfaceExtends(CodeElement... elements) {
    super(elements);
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private final GrowableList<NamedClassOrParameterized> interfaces = new GrowableList<>();

    private Builder() {}

    public final Builder addInterface(NamedClassOrParameterized iface) {
      interfaces.addWithNullMessage(iface, "iface == null");
      return this;
    }

    public final InterfaceExtends build() {
      return interfaces.isEmpty() ? NONE : build0();
    }

    private InterfaceExtends build0() {
      return new InterfaceExtends(
          space(),
          Keywords._extends(),
          space(),
          commaSeparated(interfaces)
      );
    }

  }

}
