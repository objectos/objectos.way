/*
 * Copyright (C) 2014-2023 Objectos Software LTDA.
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
package objectos.code.internal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import objectos.code.Code;

public final class CodeImportList implements Code.ImportList {

  /**
   * The package name which all import declarations will be relative to.
   */
  private String packageName = "";

  final Set<String> importTypes = new TreeSet<>();

  private final Set<String> simpleNames = new HashSet<>();

  private final Map<InternalClassName, String> visitedTypes = new HashMap<>();

  public CodeImportList() {}

  /*
   * Visible for testing
   */
  CodeImportList(String packageName) {
    this.packageName = packageName;
  }

  public final String process(InternalClassName className) {
    // do we have a result computed already?
    String existing;
    existing = visitedTypes.get(className);

    if (existing != null) {
      // we have already computed a result for this name so we just return it

      return existing;
    }

    // we don't have a result yet, let's compute it

    String simpleName;
    simpleName = className.simpleName();

    String fullName;
    fullName = className.toString();

    String result;

    if (packageName.equals(className.packageName) || "java.lang".equals(className.packageName)) {
      // type does not need an import declaration

      if (simpleNames.add(simpleName)) {
        // this simple name has not been emitted yet
        result = simpleName;
      } else {
        // the simple name of this type will conflict with the simple name of another type
        result = fullName;
      }
    } else if (simpleNames.add(simpleName)) {
      result = simpleName;

      importTypes.add(fullName);
    } else {
      // we will have to emit the full name of the type
      result = fullName;
    }

    // let's cache our result
    visitedTypes.put(className, result);

    return result;
  }

  @Override
  public final String toString() {
    if (importTypes.isEmpty()) {
      return "";
    } else {
      return importTypes.stream()
          .map(s -> "import " + s + ";")
          .collect(Collectors.joining("\n", "\n", "\n"));
    }
  }

  final void set(String packageName) {
    this.packageName = packageName;

    importTypes.clear();

    simpleNames.clear();

    visitedTypes.clear();
  }

}