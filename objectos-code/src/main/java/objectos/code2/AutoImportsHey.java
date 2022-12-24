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
package objectos.code2;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import objectos.util.GrowableMap;
import objectos.util.GrowableSet;

final class AutoImportsHey {

  String packageName;

  String fileName;

  private final StringBuilder canonicalName = new StringBuilder(200);

  private String classTypePackageName;

  private String classTypeSimpleName;

  private final Set<String> importTypes = new TreeSet<>();

  private final GrowableSet<String> simpleNames = new GrowableSet<>();

  private final Map<String, Integer> visitedTypes = new GrowableMap<>();

  private boolean enabled;

  boolean skipJavaLang;

  public final int classTypeInstruction() {
    if (!enabled) {
      return ByteCode.NOP0;
    }

    var cname = canonicalName.toString();

    var existing = visitedTypes.get(cname);

    if (existing != null) {
      return existing.intValue();
    }

    var result = ByteCode.NOP0;

    if (classTypePackageName.equals("java.lang") && skipJavaLang
        || classTypePackageName.equals(packageName)) {

      if (simpleNames.add(classTypeSimpleName)) {
        result = 1;
      }

    } else if (simpleNames.add(classTypeSimpleName)) {
      result = 1;

      importTypes.add(cname);
    }

    visitedTypes.put(cname, result);

    return result;
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

    fileName = null;

    importTypes.clear();

    packageName = "";

    simpleNames.clear();

    skipJavaLang = false;

    visitedTypes.clear();
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

  public final void packageName(String s) {
    packageName = s;
  }

  public final Set<String> types() {
    return importTypes;
  }

}