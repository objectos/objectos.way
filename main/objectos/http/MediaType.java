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

/// Represents a media type such as `application/json` or
/// `text/html; charset=utf-8`.
public sealed interface MediaType permits MediaTypePojo {

  /// The `application/octet-stream` media type.
  MediaType APPLICATION_OCTET_STREAM = MediaTypePojo.of0("application/octet-stream");

  /// The `text/css` media type.
  MediaType TEXT_CSS = MediaTypePojo.of0("text/css");

  /// The `text/html` media type.
  MediaType TEXT_HTML = MediaTypePojo.of0("text/html");

  /// The `text/javascript` media type.
  MediaType TEXT_JAVASCRIPT = MediaTypePojo.of0("text/javascript");

  /// The `text/plain` media type.
  MediaType TEXT_PLAIN = MediaTypePojo.of0("text/plain");

  /// Returns the full name of this media type, such as `text/html`.
  ///
  /// @return the full name of this media type
  String fullType();

}
