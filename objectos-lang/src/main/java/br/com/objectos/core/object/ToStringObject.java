/*
 * Copyright (C) 2022-2022 Objectos Software LTDA.
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
package br.com.objectos.core.object;

/**
 * An object that can correctly participate in nested {@code toString}
 * implementations.
 */
public interface ToStringObject {

  /**
   * Formats and appends this object's string representation to the
   * {@code toString} builder at the specified indentation level.
   *
   * @param toString
   *        the builder of a {@code toString} method
   * @param level
   *        the indentation level
   */
  void formatToString(StringBuilder toString, int level);

}