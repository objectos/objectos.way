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

import java.util.Comparator;
import java.util.List;
import objectos.util.GrowableList;
import objectos.util.GrowableSet;

final class ImportSet {

  private PackageName packageName;

  private final GrowableSet<ClassName> classNames = new GrowableSet<>();

  private final GrowableSet<String> simpleNames = new GrowableSet<>();

  private final GrowableList<ClassName> sorted = new GrowableList<>();

  boolean enabled;

  public final void addClassName(ClassName value) {
    classNames.add(value);
  }

  public final boolean addSimpleName(String simpleName) {
    return simpleNames.add(simpleName);
  }

  public final boolean canSkipImport(PackageName otherPackageName) {
    return otherPackageName.is("java.lang") || packageName.equals(otherPackageName);
  }

  public final void clear() {
    packageName = PackageName.UNNAMED;

    classNames.clear();

    sorted.clear();

    enabled = false;
  }

  public final boolean contains(ClassName className) {
    return classNames.contains(className);
  }

  public final void enable() {
    enabled = true;
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