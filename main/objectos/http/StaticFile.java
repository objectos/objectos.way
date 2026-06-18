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

import java.util.Objects;
import objectox.http.media.StaticFileContent;

/// A result that writes the associated content as a static file and responds
/// with a `200 OK` message. Subsequent requests to the same path are handled as
/// a static file.
///
/// If static files support is not enabled, then this result produces as a `200
/// OK` response with the specified content.
public sealed interface StaticFile
    extends Result, RoutingOption
    permits StaticFileContent {

  /// Returns a new static file result of the specified content.
  ///
  /// @param content the content to be saved and served as a static file
  ///
  /// @return a newly created static file result
  static StaticFile of(Content content) {
    return new StaticFileContent(
        Objects.requireNonNull(content, "content == null")
    );
  }

  /// Returns a new static file result of the content from the specified
  /// provider.
  ///
  /// @param provider the object providing the content to be saved and served as
  ///        a static file
  ///
  /// @return a newly created static file result
  static StaticFile of(ContentProvider provider) {
    final Content content;
    content = provider.toContent();

    if (content == null) {
      throw new IllegalArgumentException("%s provided a null `Content` instance".formatted(provider));
    }

    return new StaticFileContent(content);
  }

}
