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

public final class PropertyClass {

  final String simpleName;

  final List<String> constants = new ArrayList<>();

  String javadoc;

  public PropertyClass(String simpleName) {
    this.simpleName = Check.notNull(simpleName, "simpleName == null");
  }

  public final void add(String name) {
    Check.notNull(name, "name == null");

    constants.add(name);
  }

  public final void javadoc(String value) {
    javadoc = value;
  }

}