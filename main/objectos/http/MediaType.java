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

import objectox.http.media.MediaTypePojo;

/// Represents a media type such as `text/html; charset=utf-8`
/// and `application/json`.
public sealed interface MediaType permits MediaTypePojo {

  /// The `application/octet-stream` media type.
  MediaType APPLICATION_OCTET_STREAM = MediaTypePojo.of0("application/octet-stream");

  /// The `image/jpeg` media type.
  MediaType IMAGE_JPEG = MediaTypePojo.of0("image/jpeg");

  /// The `image/png` media type.
  MediaType IMAGE_PNG = MediaTypePojo.of0("image/png");

  /// The `image/webp` media type.
  MediaType IMAGE_WEBP = MediaTypePojo.of0("image/webp");

  /// The `image/avif` media type.
  MediaType IMAGE_AVIF = MediaTypePojo.of0("image/avif");

  /// The `image/gif` media type.
  MediaType IMAGE_GIF = MediaTypePojo.of0("image/gif");

  /// The `image/svg+xml` media type.
  MediaType IMAGE_SVG = MediaTypePojo.of0("image/svg+xml");

  /// The `text/css; charset=utf-8` media type.
  MediaType TEXT_CSS = MediaTypePojo.of0("text/css; charset=utf-8");

  /// The `text/html; charset=utf-8` media type.
  MediaType TEXT_HTML = MediaTypePojo.of0("text/html; charset=utf-8");

  /// The `text/javascript; charset=utf-8` media type.
  MediaType TEXT_JAVASCRIPT = MediaTypePojo.of0("text/javascript; charset=utf-8");

  /// The `text/plain; charset=utf-8` media type.
  MediaType TEXT_PLAIN = MediaTypePojo.of0("text/plain; charset=utf-8");

  /// The `text/xml` media type.
  MediaType TEXT_XML = MediaTypePojo.of0("text/xml");

  /// Returns the full name of this media type, such as `text/html;
  /// charset=utf-8` or `application/json`.
  ///
  /// @return the full name of this media type
  String fullType();

}
