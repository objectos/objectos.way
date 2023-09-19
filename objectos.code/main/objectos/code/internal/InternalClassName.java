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

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import objectos.code.ClassName;

public final class InternalClassName implements ClassName {

  final String packageName;

  private final String[] names;

  private String toString;

  InternalClassName(String packageName, String[] names) {
    this.packageName = packageName;
    this.names = names;
  }

  public static InternalClassName of(Class<?> type) {
    List<String> reversed;
    reversed = new ArrayList<>();

    int size = 0;

    while (true) {
      String simpleName; // implicit null-check
      simpleName = type.getSimpleName();

      reversed.add(simpleName);

      size++;

      Class<?> outer;
      outer = type.getEnclosingClass();

      if (outer == null) {
        break;
      } else {
        type = outer;
      }
    }

    String[] names;
    names = new String[size];

    for (int i = 0; i < size; i++) {
      names[i] = reversed.get(size - 1 - i);
    }

    String packageName;
    packageName = type.getPackageName();

    return new InternalClassName(packageName, names);
  }

  public static ClassName of(ClassName enclosing, String simpleName) {
    // cast is safe: ClassTypeName is sealed
    InternalClassName impl;
    impl = (InternalClassName) enclosing;

    return impl.nestedClass(simpleName);
  }

  public static InternalClassName of(String packageName, String simpleName, String... nested) {
    String[] names;
    names = new String[nested.length + 1];

    names[0] = simpleName;

    System.arraycopy(nested, 0, names, 1, nested.length);

    return new InternalClassName(packageName, names);
  }

  @Override
  public final boolean equals(Object obj) {
    return obj == this || obj instanceof InternalClassName that
        && Objects.equals(toString(), that.toString());
  }

  @Override
  public final int hashCode() {
    return toString().hashCode();
  }

  @Override
  public final String packageName() {
    return packageName;
  }

  @Override
  public final String simpleName() {
    return names[names.length - 1];
  }

  @Override
  public final Path toPath(Path directory) {
    Check.state(names.length == 1, "Cannot convert the name of a nested type to a Path");
    Check.notNull(directory, "directory == null");

    Path packageDirectory;
    packageDirectory = toPackageDirectory(directory);

    String fileName;
    fileName = names[0];

    fileName += ".java";

    return packageDirectory.resolve(fileName);
  }

  @Override
  public final String toString() {
    if (toString == null) {
      StringBuilder sb;
      sb = new StringBuilder(packageName);

      for (int i = 0, len = names.length; i < len; i++) {
        sb.append('.');

        sb.append(names[i]);
      }

      toString = sb.toString();
    }

    return toString;
  }

  private ClassName nestedClass(String simpleName) {
    String[] copy;
    copy = Arrays.copyOf(names, names.length + 1);

    copy[copy.length - 1] = simpleName;

    return new InternalClassName(packageName, copy);
  }

  private Path toPackageDirectory(Path directory) {
    Path result;
    result = directory;

    int beginIndex;
    beginIndex = 0;

    int endIndex;
    endIndex = packageName.indexOf('.', beginIndex);

    while (endIndex > 0) {
      String part;
      part = packageName.substring(beginIndex, endIndex);

      result = result.resolve(part);

      beginIndex = endIndex + 1;

      endIndex = packageName.indexOf('.', beginIndex);
    }

    endIndex = packageName.length();

    if (beginIndex < endIndex) {
      String part;
      part = packageName.substring(beginIndex, endIndex);

      result = result.resolve(part);
    }

    return result;
  }

}