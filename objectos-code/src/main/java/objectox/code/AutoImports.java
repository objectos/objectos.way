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
package objectox.code;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import objectos.code.ClassName;
import objectos.code.PackageName;
import objectos.util.GrowableSet;

public final class AutoImports {

  private PackageName packageName;

  private final Map<ClassName, Integer> imports = new TreeMap<>();

  private final GrowableSet<String> simpleNames = new GrowableSet<>();

  private boolean enabled;

  boolean skipJavaLang;

  public final boolean addClassName(ClassName value, int objectIndex) {
    if (!enabled) {
      return false;
    }

    if (imports.containsKey(value)) {
      return true;
    }

    var otherPackageName = value.packageName();

    if (canSkipImport(otherPackageName)) {
      simpleNames.add(value.simpleName);

      return true;
    }

    if (simpleNames.add(value.simpleName)) {
      imports.put(value, objectIndex);

      return true;
    }

    return false;
  }

  public final void clear() {
    packageName = PackageName.of();

    imports.clear();

    simpleNames.clear();

    enabled = false;

    skipJavaLang = false;
  }

  public final void enable() {
    enabled = true;

    skipJavaLang = true;
  }

  public final boolean enabled() {
    return enabled;
  }

  public final Set<Entry<ClassName, Integer>> entrySet() {
    return imports.entrySet();
  }

  public final void packageName(PackageName packageName) {
    this.packageName = packageName;
  }

  public final void packageName(String s) {
    packageName = PackageName.of(s);
  }

  private boolean canSkipImport(PackageName otherPackageName) {
    if (otherPackageName.is("java.lang")) {
      return skipJavaLang;
    } else {
      return packageName.equals(otherPackageName);
    }
  }

}