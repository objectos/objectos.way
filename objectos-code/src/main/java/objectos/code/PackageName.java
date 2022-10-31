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
package objectos.code;

import javax.lang.model.SourceVersion;
import objectos.lang.Check;

public final class PackageName extends PackageOrClassName {

  private static final PackageName UNNAMED = new PackageName("");

  public final String name;

  private PackageName(String name) {
    this.name = name;
  }

  public static PackageName of() {
    return UNNAMED;
  }

  public static PackageName of(Package packageInstance) {
    var name = packageInstance.getName(); // implicit null-check

    return new PackageName(name);
  }

  public static PackageName of(PackageName parent, String name) {
    return parent.nested(name);
  }

  public static PackageName of(String name) {
    Check.notNull(name, "name == null");
    Check.argument(
      SourceVersion.isName(name),
      name, " is not a valid package name"
    );

    return new PackageName(name);
  }

  @Override
  public final boolean equals(Object obj) {
    return obj == this || obj instanceof PackageName that
        && name.equals(that.name);
  }

  @Override
  public final int hashCode() { return name.hashCode(); }

  public final boolean is(String name) {
    return this.name.equals(name);
  }

  @Override
  public final String toString() { return name; }

  @Override
  final PackageName packageName() { return this; }

  private PackageName nested(String name) {
    Check.argument(
      SourceVersion.isName(name),
      name, " is not a valid part of a package name"
    );

    return new PackageName(toString(name));
  }

}