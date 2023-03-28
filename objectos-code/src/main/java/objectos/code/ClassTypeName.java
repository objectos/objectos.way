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

import java.util.Objects;
import objectos.code.internal.ClassTypeNameImpl;
import objectos.code.internal.JavaModel;
import objectos.code.tmpl.ArrayTypeComponent;
import objectos.code.tmpl.ClassOrParameterizedTypeName;
import objectos.code.tmpl.ExpressionPart;
import objectos.code.tmpl.ImplementsClauseInstruction;
import objectos.lang.Check;

/**
 * Represents the fully qualified name of a class or interface type in a Java
 * program.
 *
 * <p>
 * To create instances of this class use one of provided factory methods:
 *
 * <ul>
 * <li>{@link ClassTypeName#of(Class)}</li>
 * <li>{@link ClassTypeName#of(String, String, String...)}</li>
 * <li>{@link ClassTypeName#of(ClassTypeName, String)}</li>
 * </ul>
 *
 * @since 0.4.2
 */
public sealed interface ClassTypeName
    extends
    ClassOrParameterizedTypeName,
    ArrayTypeComponent,
    ExpressionPart,
    ImplementsClauseInstruction
    permits ClassTypeNameImpl {

  /**
   * Creates a new {@code ClassTypeName} from the provided {@code Class}
   * instance.
   *
   * <p>
   * A {@link ClassTypeName} instance can only represent a class or an interface
   * type. Therefore, this method throws a {@link IllegalArgumentException} when
   * the specified {@code type}:
   *
   * <ul>
   * <li>represents a primitive type;</li>
   * <li>represents an array type; or</li>
   * <li>is the {@code void.class} literal.</li>
   * </ul>
   *
   * <p>
   * This method is typically used to initialize a static field:
   *
   * <pre>
   * static final ClassTypeName STRING = ClassTypeName.of(String.class);</pre>
   *
   * <p>
   * Which then can be used:
   *
   * <pre>
   * method(
   *   PUBLIC, STRING, name("toString"),
   *   p(RETURN, s("Objectos Code"))
   * )</pre>
   *
   * <p>
   * And, in turn, it generates:
   *
   * <pre>
   * public String toString() {
   *   return "Objectos Code";
   * }</pre>
   *
   * @param type
   *        the {@code Class} object whose name will be used in a Java program
   *
   * @return a newly constructed {@code ClassTypeName} instance
   *
   * @throws IllegalArgumentException
   *         if {@code type} represents either a primitive type, an array type,
   *         or void.
   */
  static ClassTypeName of(Class<?> type) {
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

    return ClassTypeNameImpl.of(type);
  }

  /**
   * Creates a new {@code ClassTypeName} from the provided names.
   *
   * <p>
   * The following code illustrates how to create names using the method:
   *
   * <pre>
   * // creates the name for the java.lang.String type
   * static final ClassTypeName STRING =
   *     ClassTypeName.of("java.lang", "String");
   *
   * // creates the name for the java.util.Map.Entry nested type
   * static final ClassTypeName ENTRY =
   *     ClassTypeName.of("java.util", "Map", "Entry");</pre>
   *
   * @param packageName
   *        the name of the package
   * @param simpleName
   *        the name of the top level type
   * @param nested
   *        the additional names that make up the name of this nested type
   *
   * @return a newly constructed {@code ClassTypeName} instance
   *
   * @throws IllegalArgumentException
   *         if any name in {@code packageName} is not a valid identifier, if
   *         {@code simpleName} is not a valid identifier or if any name in
   *         {@code nested} is not a valid identifier
   */
  static ClassTypeName of(
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

    return ClassTypeNameImpl.of(packageName, simpleName, nested);
  }

  /**
   * Creates a new {@code ClassTypeName} by nesting the provided
   * {@code simpleName} into the provided {@code enclosing} name.
   *
   * <p>
   * The following code illustrates how to create names using the method:
   *
   * <pre>
   * // creates the name for the java.util.Map type
   * static final ClassTypeName MAP =
   *     ClassTypeName.of("java.util", "Map");
   *
   * // creates the name for the java.util.Map.Entry nested type
   * static final ClassTypeName ENTRY =
   *     ClassTypeName.of(MAP, "Entry");</pre>
   *
   * @param enclosing
   *        the enclosing class name
   * @param simpleName
   *        the name to be immediately nested into {@code enclosing}
   *
   * @return a newly constructed {@code ClassTypeName} instance
   *
   * @throws IllegalArgumentException
   *         if {@code simpleName} is not a valid identifier
   */
  static ClassTypeName of(ClassTypeName enclosing, String simpleName) {
    Objects.requireNonNull(enclosing, "enclosing == null");
    JavaModel.checkSimpleName(simpleName.toString());

    return ClassTypeNameImpl.of(enclosing, simpleName);
  }

  /**
   * Returns the simple name.
   *
   * @return the simple name
   *
   * @since 0.4.4
   */
  String simpleName();

}