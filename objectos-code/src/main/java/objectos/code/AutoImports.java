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

import java.util.Set;
import java.util.TreeSet;
import objectos.util.GrowableSet;

public final class AutoImports {

  static final int NAME1 = 1 << 0;

  PackageName packageName;

  String fileName;

  private final StringBuilder canonicalName = new StringBuilder(200);

  private String classTypePackageName;

  private String classTypeSimpleName;

  private final GrowableSet<String> simpleNames = new GrowableSet<>();

  private final Set<String> types = new TreeSet<>();

  private boolean enabled;

  boolean skipJavaLang;

  public final int classTypeInstruction() {
    if (!enabled) {
      return ByteCode.NOP;
    }

    var cname = canonicalName.toString();

    if (types.contains(cname)) {
      return NAME1;
    }

    if (skipJavaLang && classTypePackageName.equals("java.lang")) {
      if (simpleNames.add(classTypeSimpleName)) {
        return NAME1;
      } else {
        return ByteCode.NOP;
      }
    }

    if (packageName.name.equals(classTypePackageName)) {
      if (simpleNames.add(classTypeSimpleName)) {
        return NAME1;
      } else {
        return ByteCode.NOP;
      }
    }

    if (simpleNames.add(classTypeSimpleName)) {
      types.add(cname);

      return NAME1;
    }

    return ByteCode.NOP;
  }

  public final void classTypePackageName(String value) {
    canonicalName.setLength(0);

    canonicalName.append(value);

    classTypePackageName = value;
  }

  public final void classTypeSimpleName(String value) {
    if (canonicalName.length() > 0) {
      canonicalName.append('.');
    }

    canonicalName.append(value);

    classTypeSimpleName = value;
  }

  public final void clear() {
    enabled = false;

    packageName = PackageName.of();

    simpleNames.clear();

    skipJavaLang = false;

    types.clear();
  }

  public final void enable() {
    enabled = true;

    skipJavaLang = true;
  }

  public final boolean enabled() {
    return enabled;
  }

  public final void fileName(boolean publicFound, String simpleName) {
    fileName = simpleName + ".java";
  }

  public final void packageName(PackageName packageName) {
    this.packageName = packageName;
  }

  public final void packageName(String s) {
    packageName = PackageName.of(s);
  }

  public final Set<String> types() {
    return types;
  }

}