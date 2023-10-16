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
package selfgen.css.util;

import java.util.ArrayList;
import java.util.List;
import objectos.lang.Check;

public sealed abstract class PropertyClass {

  final String simpleName;

  String javadoc;

  PropertyClass(String simpleName) {
    this.simpleName = Check.notNull(simpleName, "simpleName == null");
  }

  public final void javadoc(String value) {
    javadoc = value;
  }

  public static final class Standard extends PropertyClass {

    final String propertyName;

    final List<Constant1> constants = new ArrayList<>();

    public Standard(String simpleName, String propertyName) {
      super(simpleName);

      this.propertyName = propertyName;
    }

    public final void add(String name, String value) {
      Check.notNull(name, "name == null");
      Check.notNull(value, "value == null");

      Constant1 cte;
      cte = new Constant1(name, value);

      constants.add(cte);
    }

  }

  static record Constant1(String name, String value) {}

}