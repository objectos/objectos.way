/*
 * Copyright (C) 2022-2023 Objectos Software LTDA.
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
package objectos.lang;

import java.io.IOException;

/**
 * An object that can write out its string representation.
 */
public interface CharWritable {

  /**
   * Writes this object's textual representation to the appendable.
   *
   * @param dest the appendable where to write characters into.
   *
   * @throws IOException if an I/O error occurs
   */
  default void writeTo(Appendable dest) throws IOException {
    dest.append(toString());
  }

}