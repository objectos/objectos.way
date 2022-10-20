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
import objectos.code.JavaTemplate.Renderer;
import objectos.lang.Check;
import objectos.lang.Equals;
import objectos.lang.HashCode;

public class ClassName implements Comparable<ClassName> {

  private final PackageName packageName;

  private final String simpleName;

  private ClassName(PackageName packageName, String simpleName) {
    this.packageName = packageName;
    this.simpleName = simpleName;
  }

  public static ClassName of(PackageName packageName, String simpleName) {
    Check.notNull(packageName, "packageName == null");
    Check.argument(
      SourceVersion.isName(simpleName),
      simpleName, " is not a valid package name"
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
          packageName, that.packageName,
          simpleName, that.simpleName
        );
  }

  @Override
  public final int hashCode() {
    return HashCode.of(packageName, simpleName);
  }

  @Override
  public final String toString() {
    return packageName.toString(simpleName);
  }

  final void execute(Renderer processor, ImportSet set) {
    if (set.contains(this)) {
      processor.identifier(simpleName);
    }

    else if (set.canSkipImport(packageName)) {
      set.addSimpleName(simpleName);

      processor.identifier(simpleName);
    }

    else if (set.addSimpleName(simpleName)) {
      processor.identifier(simpleName);
    }

    else {
      processor.name(toString());
    }
  }

}