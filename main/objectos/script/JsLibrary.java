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
package objectos.script;

import java.io.IOException;
import java.nio.charset.Charset;
import objectos.way.Media;

/// Represents the source code of the Objectos Way JS library.
public sealed interface JsLibrary extends Media.Text permits ScriptJsLibrary {

  /// Returns the sole instance of this interface.
  ///
  /// @return the sole instance of this interface
  static JsLibrary of() {
    return ScriptJsLibrary.INSTANCE;
  }

  /// Returns `text/javascript; charset=utf-8`.
  ///
  /// @return always `text/css; charset=utf-8`
  @Override
  String contentType();

  /// Returns `StandardCharsets.UTF_8`.
  ///
  /// @return always `StandardCharsets.UTF_8`
  @Override
  Charset charset();

  /// Writes the Objectos Way JS library source code to the specified
  /// `Appendable`.
  ///
  /// @param out where to append the source code
  ///
  /// @throws IOException if an I/O error occurs
  @Override
  void writeTo(Appendable out) throws IOException;

}