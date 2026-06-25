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

import objectos.http.Content;
import objectos.http.ContentProvider;
import objectos.http.MediaType;

/// Represents the source code of the Objectos Script JS library.
public final class JsLibrary implements ContentProvider {

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

  /// Returns the JS library contents as a new `Content` object having the
  /// `text/javascript; charset=utf-8` type.
  ///
  /// @return a newly created `Content` object
  @Override
  public final Content toContent() {
    return Content.of(MediaType.TEXT_JAVASCRIPT, value);
  }

}