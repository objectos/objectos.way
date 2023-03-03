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

import objectos.code.internal.TypeVariableNameImpl;
import objectos.code.tmpl.ArrayTypeComponent;
import objectos.code.tmpl.ReferenceTypeName;

/**
 * Represents the name of a type variable in a Java program.
 *
 * <p>
 * The following Objectos Code class declaration:
 *
 * <pre>
 * static final TypeVariableName E
 *     = TypeVariableName.of("E");
 *
 * classDeclaration(
 *   PUBLIC, name("Box"), typeParameter("E"),
 *
 *   field(PRIVATE, FINAL, E, name("value")),
 *
 *   constructor(
 *     PUBLIC,
 *     parameter(E, name("value"),
 *     p(THIS, n("value"), IS, n("value"))
 *   ),
 *
 *   method(
 *     PUBLIC, E, name("get"),
 *     p(RETURN, n("value"))
 *   )
 * );</pre>
 *
 * <p>
 * Generates:
 *
 * <pre>
 * public class Box&lt;E&gt; {
 *   private final E value;
 *
 *   public Box(E value) {
 *     this.value = value;
 *   }
 *
 *   public E get() {
 *     return value;
 *   }
 * }</pre>
 *
 * @since 0.4.2
 */
public sealed interface TypeVariableName
    extends ReferenceTypeName, ArrayTypeComponent permits TypeVariableNameImpl {
  /**
   * Creates a new {@code TypeVariableName} instance with the specified
   * {@code name}.
   *
   * @param name
   *        the name of this type variable
   *
   * @return a newly constructed {@code TypeVariableName} instance
   *
   * @throws IllegalArgumentException
   *         if {@code name} contains an invalid character for a Java identifier
   *
   * @since 0.4.2
   */
  static TypeVariableName of(String name) {
    return TypeVariableNameImpl.of(name);
  }
}