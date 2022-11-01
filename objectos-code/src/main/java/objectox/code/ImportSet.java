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

import java.util.Comparator;
import java.util.List;
import objectos.code.ClassName;
import objectos.code.JavaTemplate.Renderer;
import objectos.code.NoTypeName;
import objectos.code.PackageName;
import objectos.code.TypeName;
import objectos.code.tmpl.ClassNameSet;
import objectos.util.GrowableList;
import objectos.util.GrowableSet;

public final class ImportSet implements ClassNameSet {

  private PackageName packageName;

  private final GrowableSet<ClassName> classNames = new GrowableSet<>();

  private final GrowableSet<String> simpleNames = new GrowableSet<>();

  private final GrowableList<ClassName> sorted = new GrowableList<>();

  boolean enabled;

  boolean skipJavaLang;

  @Override
  public final void addClassName(ClassName value) {
    classNames.add(value);
  }

  public final boolean addSimpleName(String simpleName) {
    return simpleNames.add(simpleName);
  }

  public final boolean canSkipImport(PackageName otherPackageName) {
    if (otherPackageName.is("java.lang")) {
      return skipJavaLang;
    } else {
      return packageName.equals(otherPackageName);
    }
  }

  public final void clear() {
    packageName = PackageName.of();

    classNames.clear();

    simpleNames.clear();

    sorted.clear();

    enabled = false;

    skipJavaLang = false;
  }

  public final void enable() {
    enabled = true;
  }

  public final void execute(Renderer processor, ClassName name) {
    if (!enabled) {
      processor.write(name.toString());
    }

    else if (classNames.contains(name)) {
      processor.write(name.simpleName);
    }

    else if (canSkipImport(name.packageName())) {
      simpleNames.add(name.simpleName);

      processor.write(name.simpleName);
    }

    else {
      throw new UnsupportedOperationException("Implement me");
    }
  }

  public final void execute(Renderer processor, TypeName typeName) {
    if (typeName instanceof NoTypeName) {
      processor.write("void");
    }
  }

  public final void packageName(PackageName packageName) {
    this.packageName = packageName;
  }

  public final void packageName(String s) {
    packageName = PackageName.of(s);
  }

  public final List<ClassName> sort() {
    sorted.addAll(classNames);

    sorted.sort(Comparator.naturalOrder());

    return sorted;
  }

  public final ClassName sorted(int index) {
    return sorted.get(index);
  }

}