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
package objectos.code;

import java.nio.file.Path;
import java.util.Objects;
import objectos.code.internal.Check;
import objectos.code.internal.InternalClassName;
import objectos.code.internal.JavaModel;

/**
 * Represents the fully qualified name of a class or interface type in a Java
 * program.
 *
 * <p>
 * The {@linkplain Code.ImportList import list facility} may collect the
 * processed instances of this class to automatically generate an import
 * declaration list.
 *
 * <p>
 * Additionally, this class provides a method for generating file path names
 * suitable for creating Java files in a standard package directory hierarchy.
 *
 * <p>
 * To create instances of this interface use one of provided factory methods:
 *
 * <ul>
 * <li>{@link ClassName#of(Class)}</li>
 * <li>{@link ClassName#of(String, String, String...)}</li>
 * <li>{@link ClassName#of(ClassName, String)}</li>
 * </ul>
 */
public sealed interface ClassName extends TypeName permits InternalClassName {

  /**
   * Creates a new {@code ClassName} from the provided {@code Class}
   * instance.
   *
   * <p>
   * A {@link ClassName} instance can only represent a class or an interface
   * type. Therefore, this method throws a {@link IllegalArgumentException} when
   * the specified {@code type}:
   *
   * <ul>
   * <li>represents a primitive type;</li>
   * <li>represents an array type; or</li>
   * <li>is the {@code void.class} literal.</li>
   * </ul>
   *
   * @param type
   *        the {@code Class} object whose name will be used in a Java program
   *
   * @return a newly constructed {@code ClassName} instance
   *
   * @throws IllegalArgumentException
   *         if {@code type} represents either a primitive type, an array type,
   *         or void.
   */
  static ClassName of(Class<?> type) {
    Check.argument(!type.isPrimitive(), """
    A `ClassName` cannot be used to represent a primitive type.

    Use a `PrimitiveTypeName` instead.
    """);

    Check.argument(type != void.class, """
    A `ClassName` cannot be used to represent the no-type (void).
    """);

    Check.argument(!type.isArray(), """
    A `ClassName` cannot be used to represent array types.
    """);

    return InternalClassName.of(type);
  }

  /**
   * Creates a new {@code ClassName} from the provided names.
   *
   * <p>
   * The following code illustrates how to create names using the method:
   *
   * <pre>
   * // creates the name for the java.lang.String type
   * static final ClassName STRING =
   *     ClassName.of("java.lang", "String");
   *
   * // creates the name for the java.util.Map.Entry nested type
   * static final ClassName ENTRY =
   *     ClassName.of("java.util", "Map", "Entry");</pre>
   *
   * @param packageName
   *        the name of the package
   * @param simpleName
   *        the name of the top level type
   * @param nested
   *        the additional names that make up the name of this nested type
   *
   * @return a newly constructed {@code ClassName} instance
   *
   * @throws IllegalArgumentException
   *         if any name in {@code packageName} is not a valid identifier, if
   *         {@code simpleName} is not a valid identifier or if any name in
   *         {@code nested} is not a valid identifier
   */
  static ClassName of(
      String packageName, String simpleName, String... nested) {
    JavaModel.checkPackageName(packageName.toString());
    JavaModel.checkSimpleName(simpleName.toString());

    for (int i = 0; i < nested.length; i++) {
      var nestedName = nested[i];

      if (nestedName == null) {
        throw new NullPointerException("nested[" + i + "] == null");
      }

      JavaModel.checkSimpleName(nestedName);
    }

    return InternalClassName.of(packageName, simpleName, nested);
  }

  /**
   * Creates a new {@code ClassName} by nesting the provided
   * {@code simpleName} into the provided {@code enclosing} name.
   *
   * <p>
   * The following code illustrates how to create names using the method:
   *
   * <pre>
   * // creates the name for the java.util.Map type
   * static final ClassName MAP =
   *     ClassName.of("java.util", "Map");
   *
   * // creates the name for the java.util.Map.Entry nested type
   * static final ClassName ENTRY =
   *     ClassName.of(MAP, "Entry");</pre>
   *
   * @param enclosing
   *        the enclosing class name
   * @param simpleName
   *        the name to be immediately nested into {@code enclosing}
   *
   * @return a newly constructed {@code ClassName} instance
   *
   * @throws IllegalArgumentException
   *         if {@code simpleName} is not a valid identifier
   */
  static ClassName of(ClassName enclosing, String simpleName) {
    Objects.requireNonNull(enclosing, "enclosing == null");
    JavaModel.checkSimpleName(simpleName.toString());

    return InternalClassName.of(enclosing, simpleName);
  }

  /**
   * Returns the package name.
   *
   * @return the package name
   */
  String packageName();

  /**
   * Returns the simple name.
   *
   * @return the simple name
   */
  String simpleName();

  /**
   * Returns a Java file name for this class name resolved against the specified
   * path.
   *
   * <p>
   * For example, suppose the path name separator is "/" and:
   *
   * <ul>
   * <li>this class name represents the type {@code com.example.Foo}; and
   * <li>the specified path is "/tmp".
   * </ul>
   *
   * <p>
   * Then this method will return a path for
   * "{@code /tmp/com/example/Foo.java}".
   *
   * @param directory
   *        the directory against which to resolve this class name
   *
   * @return a path representing the Java file name of this class name
   *
   * @throws IllegalStateException
   *         if this class name represents the name of a nested type
   */
  Path toPath(Path directory);

}