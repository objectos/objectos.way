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
package br.com.objectos.code.java.io;

import br.com.objectos.code.java.declaration.PackageName;
import br.com.objectos.code.java.type.NamedClass;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import objectos.util.GrowableSet;

public class JavaFileImportSet extends ImportSet {

  private final PackageName packageName;

  private final Set<NamedClass> qualifiedNameSet = new TreeSet<>();

  private final Set<String> simpleNameSet = new GrowableSet<>();

  JavaFileImportSet(PackageName packageName) {
    this.packageName = packageName;
  }

  public final boolean addQualifiedName(NamedClass qualifiedName) {
    return qualifiedNameSet.add(qualifiedName);
  }

  public final boolean addSimpleName(String simpleName) {
    return simpleNameSet.add(simpleName);
  }

  public final boolean canSkipImport(PackageName otherPackageName) {
    return otherPackageName.is("java.lang") || packageName.equals(otherPackageName);
  }

  @Override
  public final boolean contains(NamedClass qualifiedName) {
    return qualifiedNameSet.contains(qualifiedName);
  }

  @Override
  public final String get(CanGenerateImportDeclaration element) {
    return element.acceptJavaFileImportSet(this);
  }

  @Override
  public final boolean isEmpty() {
    return size() == 0 && packageName.isUnnamed();
  }

  @Override
  public final int size() {
    return qualifiedNameSet.size();
  }

  @Override
  public final String toString() {
    return packageName.isUnnamed() ? toStringUnnamed() : toStringRegular();
  }

  private String toStringRegular() {
    CodeWriter w = CodeWriter.forToString();

    w.write("package");
    w.write(' ');
    w.write(packageName.toString());
    w.write(';');

    if (!qualifiedNameSet.isEmpty()) {
      w.nextLine();
    }

    for (NamedClass className : qualifiedNameSet) {
      w.nextLine();
      writeImport(w, className);
    }

    return w.toString();
  }

  private String toStringUnnamed() {
    CodeWriter w;
    w = CodeWriter.forToString();

    Iterator<NamedClass> iterator;
    iterator = qualifiedNameSet.iterator();

    if (iterator.hasNext()) {
      writeImport(w, iterator.next());

      while (iterator.hasNext()) {
        w.nextLine();

        writeImport(w, iterator.next());
      }
    }

    return w.toString();
  }

  private void writeImport(CodeWriter w, NamedClass className) {
    w.write("import");
    w.write(' ');
    w.write(className.toString());
    w.write(';');
  }

}