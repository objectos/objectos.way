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
import java.nio.charset.StandardCharsets;
import objectos.way.Media;

/// Represents the source code of the Objectos Script JS library.
public final class JsLibrary implements Media.Text {

  private static final JsLibrary INSTANCE = new JsLibrary(
      JsLibraryGenerated.SOURCE
  );

  private final String value;

  private JsLibrary(String value) {
    this.value = value;
  }

  /// Returns the sole instance of this class.
  ///
  /// @return the sole instance of this class
  public static JsLibrary of() {
    return INSTANCE;
  }

  /// Returns `text/javascript; charset=utf-8`.
  ///
  /// @return always `text/css; charset=utf-8`
  @Override
  public final String contentType() {
    return "text/javascript; charset=utf-8";
  }

  /// Returns `StandardCharsets.UTF_8`.
  ///
  /// @return always `StandardCharsets.UTF_8`
  @Override
  public final Charset charset() {
    return StandardCharsets.UTF_8;
  }

  /// Writes the JS library source code to the specified `Appendable`.
  ///
  /// @param out where to append the source code
  ///
  /// @throws IOException if an I/O error occurs
  @Override
  public final void writeTo(Appendable out) throws IOException {
    out.append(value);
  }

}