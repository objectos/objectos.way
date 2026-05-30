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
package objectos.http;

import java.io.IOException;
import java.io.OutputStream;

/// Represents content to be transmitted over the HTTP protocol.
public non-sealed interface Content extends Result {

  /// Writes the contents to the specified output stream.
  ///
  /// @param out where to write bytes into.
  ///
  /// @throws IOException if an I/O error occurs
  void contentBytes(OutputStream out) throws IOException;

  /// Returns the type of the transmitted content, e.g., `text/html;
  /// charset=utf-8` or `application/json`.
  ///
  /// @return the content type of this media
  String contentType();

}