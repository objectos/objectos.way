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
package objectos.selfgen.css.spec;

import objectos.lang.Check;
import objectos.selfgen.util.JavaNames;

final class ColorName implements Comparable<ColorName> {

  public final String name;

  public final String identifier;

  private ColorName(String name, String identifier) {
    this.name = name;

    this.identifier = identifier;
  }

  public static ColorName of(String name) {
    Check.notNull(name, "name == null");

    return new ColorName(
      name, JavaNames.toIdentifier(name)
    );
  }

  public final void assertIdentifierIsEqualToName() {
    if (!name.equals(identifier)) {
      throw new AssertionError("Not equal: " + name + " != " + identifier);
    }
  }

  @Override
  public final int compareTo(ColorName o) {
    return name.compareTo(o.name);
  }

}
