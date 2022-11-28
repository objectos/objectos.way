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
import objectos.lang.Equals;
import objectos.lang.HashCode;

public final class ClassName extends PackageOrClassName implements Comparable<ClassName>, TypeName {

  public final PackageOrClassName enclosingName;

  public final String simpleName;

  private ClassName(PackageOrClassName enclosingName, String simpleName) {
    this.enclosingName = enclosingName;

    this.simpleName = simpleName;
  }

  public static ClassName of(Class<?> type) {
    var enclosingType = type.getEnclosingClass(); // implicit null-check

    PackageOrClassName enclosingName;

    if (enclosingType == null) {
      var package_ = type.getPackage();

      enclosingName = PackageName.of(package_);
    } else {
      enclosingName = of(enclosingType);
    }

    var simpleName = type.getSimpleName();

    return new ClassName(enclosingName, simpleName);
  }

  public static ClassName of(ClassName outerClass, String simpleName) {
    Check.notNull(outerClass, "outerClass == null");
    Check.argument(
      SourceVersion.isName(simpleName),
      simpleName, " is not a valid type name"
    );

    return new ClassName(outerClass, simpleName);
  }

  public static ClassName of(PackageName packageName, String simpleName) {
    Check.notNull(packageName, "packageName == null");
    Check.argument(
      SourceVersion.isName(simpleName),
      simpleName, " is not a valid type name"
    );

    return new ClassName(packageName, simpleName);
  }

  @Override
  public final int compareTo(ClassName o) {
    return toString().compareTo(o.toString());
  }

  @Override
  public final boolean equals(Object obj) {
    return obj == this || obj instanceof ClassName that
        && Equals.of(
          enclosingName, that.enclosingName,
          simpleName, that.simpleName
        );
  }

  @Override
  public final int hashCode() {
    return HashCode.of(enclosingName, simpleName);
  }

  @Override
  public final PackageName packageName() {
    return enclosingName.packageName();
  }

  @Override
  public final String toString() {
    return enclosingName.toString(simpleName);
  }

}