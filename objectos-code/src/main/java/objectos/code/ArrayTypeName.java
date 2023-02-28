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

import objectos.code.internal.ArrayTypeNameImpl;
import objectos.code.tmpl.ArrayTypeComponent;
import objectos.code.tmpl.ReferenceTypeName;

/**
 * TODO
 *
 * @since 0.4.2
 */
public sealed interface ArrayTypeName extends ReferenceTypeName permits ArrayTypeNameImpl {
  /**
   * TODO
   *
   * @param type
   *        the type of the components of this array
   *
   * @return a newly constructed {@code ArrayTypeName} instance
   */
  static ArrayTypeName of(ArrayTypeComponent type) {
    return ArrayTypeNameImpl.of(type, 1);
  }

  /**
   * TODO
   *
   * @param type
   *        the type of the components of this array
   *
   * @return a newly constructed {@code ArrayTypeName} instance
   *
   * @since 0.4.4
   */
  static ArrayTypeName of(ArrayTypeComponent type, int count) {
    return ArrayTypeNameImpl.of(type, count);
  }

  /**
   * TODO
   *
   * @param type
   *        the {@code Class} representing an array type whose name will be used
   *        in a Java program
   *
   * @return a newly constructed {@code ArrayTypeName} instance
   *
   * @throws IllegalArgumentException
   *         if {@code type} does not represent an array.
   */
  static ArrayTypeName of(Class<?> type) {
    return ArrayTypeNameImpl.of(type);
  }
}