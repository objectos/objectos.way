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

import br.com.objectos.code.java.type.NamedClassOrParameterized;
import java.util.ArrayList;
import objectos.lang.Check;

public class ExtendsMany implements InterfaceCodeElement {

  private final NamedClassOrParameterized[] interfaces;

  private ExtendsMany(NamedClassOrParameterized... interfaces) {
    this.interfaces = interfaces;
  }

  public static ExtendsMany _extends(
      Iterable<? extends NamedClassOrParameterized> interfaces) {
    Check.notNull(interfaces, "interfaces == null");
    ArrayList<NamedClassOrParameterized> list = new ArrayList<>();

    int i = 0;
    for (NamedClassOrParameterized iface : interfaces) {
      list.add(Check.notNull(iface, "interfaces[" + i + "] == null"));
      i++;
    }

    return new ExtendsMany(list.toArray(new NamedClassOrParameterized[] {}));
  }

  public static ExtendsMany _extends(NamedClassOrParameterized... interfaces) {
    Check.notNull(interfaces, "interfaces == null");
    NamedClassOrParameterized[] checked
        = new NamedClassOrParameterized[interfaces.length];

    for (int i = 0; i < interfaces.length; i++) {
      NamedClassOrParameterized iface = interfaces[i];
      checked[i] = Check.notNull(iface, "interfaces[" + i + "] == null");
    }

    return new ExtendsMany(checked);
  }

  public static ExtendsMany _extends(
      NamedClassOrParameterized iface1,
      NamedClassOrParameterized iface2) {
    Check.notNull(iface1, "iface1 == null");
    Check.notNull(iface2, "iface2 == null");
    return new ExtendsMany(iface1, iface2);
  }

  public static ExtendsMany _extends(
      NamedClassOrParameterized iface1,
      NamedClassOrParameterized iface2,
      NamedClassOrParameterized iface3) {
    Check.notNull(iface1, "iface1 == null");
    Check.notNull(iface2, "iface2 == null");
    Check.notNull(iface3, "iface3 == null");
    return new ExtendsMany(iface1, iface2, iface3);
  }

  public static ExtendsMany _extends(
      NamedClassOrParameterized iface1,
      NamedClassOrParameterized iface2,
      NamedClassOrParameterized iface3,
      NamedClassOrParameterized iface4) {
    Check.notNull(iface1, "iface1 == null");
    Check.notNull(iface2, "iface2 == null");
    Check.notNull(iface3, "iface3 == null");
    Check.notNull(iface4, "iface4 == null");
    return new ExtendsMany(iface1, iface2, iface3, iface4);
  }

  @Override
  public final void acceptInterfaceCodeBuilder(InterfaceCode.Builder builder) {
    for (NamedClassOrParameterized iface : interfaces) {
      builder.addExtends(iface);
    }
  }

}
