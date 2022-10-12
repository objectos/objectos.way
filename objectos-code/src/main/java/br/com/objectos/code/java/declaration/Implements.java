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

import br.com.objectos.code.annotations.Ignore;
import br.com.objectos.code.java.element.AbstractImmutableCodeElement;
import br.com.objectos.code.java.element.CodeElement;
import br.com.objectos.code.java.element.Keywords;
import br.com.objectos.code.java.type.NamedClassOrParameterized;
import objectos.lang.Check;
import objectos.util.GrowableList;

public class Implements extends AbstractImmutableCodeElement
    implements
    ClassCodeElement,
    EnumCodeElement {

  private static final Implements NONE = new Implements();

  private Implements(CodeElement... elements) {
    super(elements);
  }

  public static Implements _implements(
      Iterable<? extends NamedClassOrParameterized> interfaces) {
    Check.notNull(interfaces, "interfaces == null");
    Builder b = builder();

    int i = 0;
    for (NamedClassOrParameterized iface : interfaces) {
      b.addWithNullMessage(iface, "interfaces[" + i + "] == null");
      i++;
    }

    return b.build();
  }

  public static Implements _implements(
      NamedClassOrParameterized iface1) {
    Builder b = builder();
    b.addWithNullMessage(iface1, "iface1 == null");
    return b.build();
  }

  public static Implements _implements(NamedClassOrParameterized... interfaces) {
    Check.notNull(interfaces, "interfaces == null");
    Builder b = builder();

    for (int i = 0; i < interfaces.length; i++) {
      b.addWithNullMessage(interfaces[i], "interfaces[" + i + "] == null");
    }

    return b.build();
  }

  public static Implements _implements(
      NamedClassOrParameterized iface1,
      NamedClassOrParameterized iface2) {
    Builder b = builder();
    b.addWithNullMessage(iface1, "iface1 == null");
    b.addWithNullMessage(iface2, "iface2 == null");
    return b.build();
  }

  public static Implements _implements(
      NamedClassOrParameterized iface1,
      NamedClassOrParameterized iface2,
      NamedClassOrParameterized iface3) {
    Builder b = builder();
    b.addWithNullMessage(iface1, "iface1 == null");
    b.addWithNullMessage(iface2, "iface2 == null");
    b.addWithNullMessage(iface3, "iface3 == null");
    return b.build();
  }

  public static Implements _implements(
      NamedClassOrParameterized iface1,
      NamedClassOrParameterized iface2,
      NamedClassOrParameterized iface3,
      NamedClassOrParameterized iface4) {
    Builder b = builder();
    b.addWithNullMessage(iface1, "iface1 == null");
    b.addWithNullMessage(iface2, "iface2 == null");
    b.addWithNullMessage(iface3, "iface3 == null");
    b.addWithNullMessage(iface4, "iface4 == null");
    return b.build();
  }

  public static Implements _implements(
      NamedClassOrParameterized iface1,
      NamedClassOrParameterized iface2,
      NamedClassOrParameterized iface3,
      NamedClassOrParameterized iface4,
      NamedClassOrParameterized iface5) {
    Builder b = builder();
    b.addWithNullMessage(iface1, "iface1 == null");
    b.addWithNullMessage(iface2, "iface2 == null");
    b.addWithNullMessage(iface3, "iface3 == null");
    b.addWithNullMessage(iface4, "iface4 == null");
    b.addWithNullMessage(iface5, "iface5 == null");
    return b.build();
  }

  public static Implements _implements(
      NamedClassOrParameterized iface1,
      NamedClassOrParameterized iface2,
      NamedClassOrParameterized iface3,
      NamedClassOrParameterized iface4,
      NamedClassOrParameterized iface5,
      NamedClassOrParameterized iface6) {
    Builder b = builder();
    b.addWithNullMessage(iface1, "iface1 == null");
    b.addWithNullMessage(iface2, "iface2 == null");
    b.addWithNullMessage(iface3, "iface3 == null");
    b.addWithNullMessage(iface4, "iface4 == null");
    b.addWithNullMessage(iface5, "iface5 == null");
    b.addWithNullMessage(iface6, "iface6 == null");
    return b.build();
  }

  public static Implements _implements(
      NamedClassOrParameterized iface1,
      NamedClassOrParameterized iface2,
      NamedClassOrParameterized iface3,
      NamedClassOrParameterized iface4,
      NamedClassOrParameterized iface5,
      NamedClassOrParameterized iface6,
      NamedClassOrParameterized iface7) {
    Builder b = builder();
    b.addWithNullMessage(iface1, "iface1 == null");
    b.addWithNullMessage(iface2, "iface2 == null");
    b.addWithNullMessage(iface3, "iface3 == null");
    b.addWithNullMessage(iface4, "iface4 == null");
    b.addWithNullMessage(iface5, "iface5 == null");
    b.addWithNullMessage(iface6, "iface6 == null");
    b.addWithNullMessage(iface7, "iface7 == null");
    return b.build();
  }

  public static Implements _implements(
      NamedClassOrParameterized iface1,
      NamedClassOrParameterized iface2,
      NamedClassOrParameterized iface3,
      NamedClassOrParameterized iface4,
      NamedClassOrParameterized iface5,
      NamedClassOrParameterized iface6,
      NamedClassOrParameterized iface7,
      NamedClassOrParameterized iface8) {
    Builder b = builder();
    b.addWithNullMessage(iface1, "iface1 == null");
    b.addWithNullMessage(iface2, "iface2 == null");
    b.addWithNullMessage(iface3, "iface3 == null");
    b.addWithNullMessage(iface4, "iface4 == null");
    b.addWithNullMessage(iface5, "iface5 == null");
    b.addWithNullMessage(iface6, "iface6 == null");
    b.addWithNullMessage(iface7, "iface7 == null");
    b.addWithNullMessage(iface8, "iface8 == null");
    return b.build();
  }

  public static Implements _implements(
      NamedClassOrParameterized iface1,
      NamedClassOrParameterized iface2,
      NamedClassOrParameterized iface3,
      NamedClassOrParameterized iface4,
      NamedClassOrParameterized iface5,
      NamedClassOrParameterized iface6,
      NamedClassOrParameterized iface7,
      NamedClassOrParameterized iface8,
      NamedClassOrParameterized iface9) {
    Builder b = builder();
    b.addWithNullMessage(iface1, "iface1 == null");
    b.addWithNullMessage(iface2, "iface2 == null");
    b.addWithNullMessage(iface3, "iface3 == null");
    b.addWithNullMessage(iface4, "iface4 == null");
    b.addWithNullMessage(iface5, "iface5 == null");
    b.addWithNullMessage(iface6, "iface6 == null");
    b.addWithNullMessage(iface7, "iface7 == null");
    b.addWithNullMessage(iface8, "iface8 == null");
    b.addWithNullMessage(iface9, "iface9 == null");
    return b.build();
  }

  @Ignore("AggregatorGenProcessor")
  public static Builder builder() {
    return new Builder();
  }

  @Override
  public final void acceptClassCodeBuilder(ClassCode.Builder builder) {
    builder.setImplementsUnchecked(this);
  }

  @Override
  public final void acceptEnumCodeBuilder(EnumCode.Builder builder) {
    builder.setImplementsUnchecked(this);
  }

  public static class Builder {

    private final GrowableList<NamedClassOrParameterized> interfaces
        = new GrowableList<>();

    private Builder() {}

    public final Builder add(NamedClassOrParameterized iface) {
      interfaces.addWithNullMessage(iface, "iface == null");
      return this;
    }

    public final Builder addAll(Iterable<? extends NamedClassOrParameterized> ifaces) {
      interfaces.addAllIterable(ifaces);
      return this;
    }

    public final Implements build() {
      return interfaces.isEmpty()
          ? NONE
          : new Implements(
              space(),
              Keywords._implements(),
              space(),
              commaSeparated(interfaces)
          );
    }

    final void addWithNullMessage(NamedClassOrParameterized iface, String string) {
      interfaces.addWithNullMessage(iface, string);
    }

  }

}
