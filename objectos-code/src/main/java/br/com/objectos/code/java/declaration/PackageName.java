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
/*
. * Copyright (C) 2014-2020 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode project.
 *
 * ObjectosCode is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * ObjectosCode is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with ObjectosCode.  If not, see <https://www.gnu.org/licenses/>.
 */
package br.com.objectos.code.java.declaration;

import br.com.objectos.code.annotations.Ignore;
import br.com.objectos.code.java.io.JavaFile;
import br.com.objectos.code.java.io.JavaFileCodeElement;
import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.java.type.NamedClassOrPackage;
import br.com.objectos.code.model.element.ProcessingPackage;
import java.nio.file.Path;
import javax.lang.model.SourceVersion;
import objectos.lang.Check;

public class PackageName implements NamedClassOrPackage, JavaFileCodeElement {

  private static final PackageName UNNAMED = new PackageName("");

  private final String canonicalName;

  private PackageName(String canonicalName) {
    this.canonicalName = canonicalName;
  }

  public static PackageName _package(PackageName parent, String child) {
    return parent.nestedPackage(child);
  }

  public static PackageName _package(String packageName) {
    return named(packageName);
  }

  @Ignore("AggregatorGenProcessor")
  public static PackageName named(String packageName) {
    Check.notNull(packageName, "packageName == null");
    Check.argument(
      SourceVersion.isName(packageName),
      packageName, " is not a valid package name"
    );
    return new PackageName(packageName);
  }

  @Ignore("AggregatorGenProcessor")
  public static PackageName of(Class<?> type) {
    Check.notNull(type, "type == null");
    return of(type.getPackage());
  }

  @Ignore("AggregatorGenProcessor")
  public static PackageName of(Package pkg) {
    Check.notNull(pkg, "pkg == null");
    return ofCanonicalName(pkg.getName());
  }

  @Ignore("AggregatorGenProcessor")
  public static PackageName of(ProcessingPackage pkg) {
    Check.notNull(pkg, "pkg == null");
    return ofCanonicalName(pkg.getCanonicalName());
  }

  public static PackageName pn(PackageName parent, String child) {
    return _package(parent, child);
  }

  public static PackageName pn(String packageName) {
    return _package(packageName);
  }

  @Ignore("AggregatorGenProcessor")
  public static PackageName unnamed() {
    return UNNAMED;
  }

  private static PackageName ofCanonicalName(String name) {
    if (name.equals("")) {
      return UNNAMED;
    }

    return new PackageName(name);
  }

  @Override
  public final void acceptJavaFileBuilder(JavaFile.Builder builder) {
    builder.setPackage(this);
  }

  @Override
  public final boolean equals(Object obj) {
    if (!(obj instanceof PackageName)) {
      return false;
    }
    PackageName that = (PackageName) obj;
    return canonicalName.equals(that.canonicalName);
  }

  public final String getCanonicalName() {
    return canonicalName;
  }

  @Override
  public final PackageName getPackage() {
    return this;
  }

  public final String getSimpleName() {
    int lastIndex = canonicalName.lastIndexOf('.');
    return lastIndex < 0 ? canonicalName : canonicalName.substring(lastIndex + 1);
  }

  @Override
  public final int hashCode() {
    return canonicalName.hashCode();
  }

  public final boolean is(String name) {
    return canonicalName.equals(name);
  }

  public final boolean isUnnamed() {
    return is("");
  }

  @Override
  public final NamedClass nestedClass(String simpleName) {
    return NamedClass.of(this, simpleName);
  }

  public final PackageName nestedPackage(String child) {
    Check.notNull(child, "child == null");
    String newPackage = canonicalName + "." + child;
    Check.argument(
      SourceVersion.isName(newPackage),
      newPackage, " is not a valid package name"
    );
    return new PackageName(newPackage);
  }

  public final Path resolve(Path path) {
    var result = path;

    var beginIndex = 0;

    var endIndex = canonicalName.indexOf('.', beginIndex);

    while (endIndex > 0) {
      var part = canonicalName.substring(beginIndex, endIndex);

      result = result.resolve(part);

      beginIndex = endIndex + 1;

      endIndex = canonicalName.indexOf('.', beginIndex);
    }

    endIndex = canonicalName.length();

    if (beginIndex < endIndex) {
      var part = canonicalName.substring(beginIndex, endIndex);

      result = result.resolve(part);
    }

    return result;
  }

  @Override
  public final String toString() {
    return canonicalName;
  }

}