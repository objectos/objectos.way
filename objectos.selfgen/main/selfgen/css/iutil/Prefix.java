/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package selfgen.css.iutil;

import java.util.ArrayList;
import java.util.List;
import objectos.code.ClassName;
import objectos.lang.Check;

public sealed abstract class Prefix {

  public static final class Breakpoint extends Prefix {
    public final int length;

    private Breakpoint(ClassName className, int length) {
      super(className);
      this.length = length;
    }
  }

  public static final class Simple extends Prefix {
    private Simple(ClassName className) {
      super(className);
    }
  }

  final ClassName className;

  final List<PropertyClass> propertyClassList = new ArrayList<>();

  Prefix(ClassName className) {
    this.className = className;
  }

  public static Breakpoint ofBreakpoint(String simpleName, int length) {
    Check.notNull(simpleName, "simpleName == null");
    Check.argument(length >= 0, "Length must not be negative");

    ClassName className;
    className = ClassName.of(ThisTemplate.CSS_UTIL, simpleName);

    return new Breakpoint(className, length);
  }

  public static Simple ofSimple(String simpleName) {
    Check.notNull(simpleName, "simpleName == null");

    ClassName className;
    className = ClassName.of(ThisTemplate.CSS_UTIL, simpleName);

    return new Simple(className);
  }

  public final PropertyClass propertyClass(SimpleName simpleName) {
    String value;
    value = simpleName.name();

    return propertyClass(value);
  }

  public final PropertyClass propertyClass(String simpleName) {
    ClassName propertyClassName;
    propertyClassName = ClassName.of(className, simpleName);

    PropertyClass propertyClass;
    propertyClass = new PropertyClass(propertyClassName);

    propertyClassList.add(propertyClass);

    return propertyClass;
  }

}