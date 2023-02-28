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
import objectos.lang.Check;

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
public sealed abstract class ClassTypeNameImpl extends External implements ClassTypeName {

  private static final class OfClass extends ClassTypeNameImpl {
    private final Class<?> value;

    public OfClass(Class<?> value) {
      this.value = value;
    }

    @Override
    public final void execute(InternalApi api) {
      api.classType(value);
      api.localToExternal();
    }
  }

  private static final class OfNames extends ClassTypeNameImpl {
    private final String packageName;

    private final String simpleName;

    private final String[] nested;

    OfNames(String packageName, String simpleName, String[] nested) {
      this.packageName = packageName;
      this.simpleName = simpleName;
      this.nested = nested;
    }

    @Override
    public final void execute(InternalApi api) {
      int count = 1; // simple name
      count += nested.length;

      api.extStart();
      api.protoAdd(ByteProto.CLASS_TYPE, api.object(packageName));
      api.protoAdd(count, api.object(simpleName));
      for (var n : nested) {
        api.protoAdd(api.object(n));
      }
    }
  }

  ClassTypeNameImpl() {}

  public static ClassTypeNameImpl of(Class<?> type) {
    Check.argument(!type.isPrimitive(), """
    A `ClassTypeName` cannot be used to represent a primitive type.

    Use a `PrimitiveTypeName` instead.
    """);

    Check.argument(type != void.class, """
    A `ClassTypeName` cannot be used to represent the no-type (void).
    """);

    Check.argument(!type.isArray(), """
    A `ClassTypeName` cannot be used to represent array types.

    Use a `ArrayTypeName` instead.
    """);

    return new OfClass(type);
  }

  public static ClassTypeNameImpl of(String packageName, String simpleName, String... nested) {
    JavaModel.checkPackageName(packageName.toString());
    JavaModel.checkSimpleName(simpleName.toString());

    for (int i = 0; i < nested.length; i++) {
      var nestedName = nested[i];

      if (nestedName == null) {
        throw new NullPointerException("nested[" + i + "] == null");
      }

      JavaModel.checkSimpleName(nestedName);
    }

    return new OfNames(packageName, simpleName, Arrays.copyOf(nested, nested.length));
  }

}