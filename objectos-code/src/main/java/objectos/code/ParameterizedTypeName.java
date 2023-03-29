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

import objectos.code.internal.ParameterizedTypeNameImpl;
import objectos.code.tmpl.ClassOrParameterizedTypeName;
import objectos.code.tmpl.ExpressionPart;
import objectos.code.tmpl.ExtendsClauseInstruction;
import objectos.code.tmpl.ImplementsClauseInstruction;
import objectos.code.tmpl.ReferenceTypeName;

/**
 * Represents the name of a parameterized type in a Java program.
 *
 * @since 0.4.2
 */
public sealed interface ParameterizedTypeName
    extends
    ClassOrParameterizedTypeName,
    ExpressionPart,
    ExtendsClauseInstruction,
    ImplementsClauseInstruction
    permits ParameterizedTypeNameImpl {
  /**
   * Creates a new {@code ParameterizedTypeName} of the generic type {@code raw}
   * whose type arguments are given by {@code first} and {@code rest} in order.
   *
   * <p>
   * Typical usage:
   *
   * <pre>
   * // List&lt;Path&gt;
   * ParameterizedTypeName.of(
   *   ClassTypeName.of(List.class),
   *   ClassTypeName.of(Path.class)
   * )
   *
   * // Map&lt;String, E&gt;
   * ParameterizedTypeName.of(
   *   ClassTypeName.of(Map.class),
   *   ClassTypeName.of(String.class),
   *   TypeVariableName.of("E")
   * )
   *
   * // Future&lt;List&lt;LocalDate&gt;&gt;
   * ParameterizedTypeName.of(
   *   ClassTypeName.of(Future.class),
   *   ParameterizedTypeName.of(
   *     ClassTypeName.of(List.class),
   *     ClassTypeName.of(LocalDate.class)
   *   )
   * )</pre>
   *
   * @param raw
   *        the generic type
   * @param first
   *        the first type argument
   * @param rest
   *        the remaining type arguments
   *
   * @return a newly constructed {@code ParameterizedTypeName} instance
   */
  static ParameterizedTypeName of(
      ClassTypeName raw, ReferenceTypeName first, ReferenceTypeName... rest) {
    return ParameterizedTypeNameImpl.of(raw, first, rest);
  }
}