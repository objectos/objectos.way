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

public abstract class ImportSet {

  private static final ImportSet FOR_TO_STRING = new ForToStringImportSet();

  ImportSet() {}

  public static ImportSet forPackage(PackageName packageName) {
    return new JavaFileImportSet(packageName);
  }

  static ImportSet forToString() {
    return FOR_TO_STRING;
  }

  public abstract boolean contains(NamedClass qualifiedName);

  public abstract String get(CanGenerateImportDeclaration element);

  public abstract boolean isEmpty();

  public abstract int size();

  private static class ForToStringImportSet extends ImportSet {
    @Override
    public final boolean contains(NamedClass qualifiedName) {
      return false;
    }

    @Override
    public final String get(CanGenerateImportDeclaration element) {
      return element.toString();
    }

    @Override
    public final boolean isEmpty() {
      return true;
    }

    @Override
    public final int size() {
      return 0;
    }
  }

}