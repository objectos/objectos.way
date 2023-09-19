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

import java.util.Locale;

/**
 * Represents the name of a primitive type in a Java program.
 */
public enum PrimitiveTypeName implements TypeName {

  /**
   * The {@code boolean} primitive type.
   */
  BOOLEAN,

  /**
   * The {@code byte} primitive type.
   */
  BYTE,

  /**
   * The {@code short} primitive type.
   */
  SHORT,

  /**
   * The {@code int} primitive type.
   */
  INT,

  /**
   * The {@code long} primitive type.
   */
  LONG,

  /**
   * The {@code char} primitive type.
   */
  CHAR,

  /**
   * The {@code float} primitive type.
   */
  FLOAT,

  /**
   * The {@code double} primitive type.
   */
  DOUBLE;

  private final String value = name().toLowerCase(Locale.US);

  /**
   * Returns the name of this primitive type so it can be used in a Java
   * program.
   *
   * @return the primitive type's name
   */
  @Override
  public final String toString() {
    return value;
  }

}