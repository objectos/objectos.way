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

import java.util.Arrays;
import objectos.code.ClassTypeName;
import objectos.code.JavaTemplate;
import objectos.util.ObjectArrays;

/**
 * Represents the fully qualified name of a class or interface type in a Java
 * program.
 *
 * <p>
 * To create instances of this class use one of provided factory methods:
 *
 * <ul>
 * <li>{@link JavaTemplate#classType(Class)}</li>
 * <li>{@link JavaTemplate#classType(String, String, String...)}</li>
 * </ul>
 *
 * @since 0.4.2
 */
public final class ClassTypeNameImpl extends External implements ClassTypeName {

  private final String packageName;

  private final String[] names;

  ClassTypeNameImpl(String packageName, String[] names) {
    this.packageName = packageName;
    this.names = names;
  }

  public static ClassTypeNameImpl of(Class<?> type) {
    String[] reversed = new String[4];

    int size = 0;

    while (true) {
      var simpleName = type.getSimpleName(); // implicit null-check

      reversed = ObjectArrays.growIfNecessary(reversed, size);

      reversed[size++] = simpleName;

      var outer = type.getEnclosingClass();

      if (outer == null) {
        break;
      } else {
        type = outer;
      }
    }

    String[] names = new String[size];

    for (int i = 0; i < size; i++) {
      names[i] = reversed[size - 1 - i];
    }

    var packageName = type.getPackageName();

    return new ClassTypeNameImpl(packageName, names);
  }

  public static ClassTypeName of(ClassTypeName enclosing, String simpleName) {
    // cast is safe: ClassTypeName is sealed
    var impl = (ClassTypeNameImpl) enclosing;

    return impl.nestedClass(simpleName);
  }

  public static ClassTypeNameImpl of(String packageName, String simpleName, String... nested) {
    var names = new String[nested.length + 1];

    names[0] = simpleName;

    System.arraycopy(nested, 0, names, 1, nested.length);

    return new ClassTypeNameImpl(packageName, names);
  }

  @Override
  public final void execute(InternalApi api) {
    api.extStart();
    api.protoAdd(ByteProto.CLASS_TYPE, api.object(packageName));
    api.protoAdd(names.length);
    for (var name : names) {
      api.protoAdd(api.object(name));
    }
  }

  /**
   * Returns the simple name.
   *
   * @return the simple name
   *
   * @since 0.4.4
   */
  public final String simpleName() {
    return names[names.length - 1];
  }

  private ClassTypeName nestedClass(String simpleName) {
    String[] copy = Arrays.copyOf(names, names.length + 1);

    copy[copy.length - 1] = simpleName;

    return new ClassTypeNameImpl(packageName, copy);
  }

}