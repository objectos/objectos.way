/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
import java.io.OutputStream;

/// Represents an object which can write a binary representation of itself.
@FunctionalInterface
public interface BinaryObject {

  /// Returns an empty binary object, i.e., one whose binary representation is a
  /// zero-length array of bytes.
  ///
  /// @return an empty binary object
  static BinaryObject empty() {
    return _ -> {};
  }

  /// Writes the binary representation of this object to the specified output
  /// stream.
  ///
  /// @param out where bytes will be written to
  ///
  /// @throws IOException if an I/O error occurs
  void binaryTo(OutputStream out) throws IOException;

}
