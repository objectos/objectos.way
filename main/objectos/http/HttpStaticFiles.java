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

import java.nio.file.Path;

/// Configures the static files to be served by a `HttpServer` instance.
public sealed interface HttpStaticFiles permits HttpHost0Builder {

  /// Recursively serves the contents of the specified directory as if it was at
  /// the root of the HTTP server.
  ///
  /// @param directory the directory whose contents are to be served
  void addDirectory(Path directory);

  /// Map file extension names to content type (media type) values as defined by
  /// the specified properties string.
  ///
  /// A typical usage is:
  ///
  /// ```java
  /// config.contentTypes("""
  ///     .css: text/stylesheet; charset=utf-8
  ///     .js: text/javascript; charset=utf-8
  ///     .jpg: image/jpeg
  ///     .woff2: font/woff2
  ///     """);
  /// ```
  ///
  /// Which causes:
  ///
  /// - files ending in `.css` to be served with `Content-Type: text/stylesheet;
  ///   charset=utf-8`
  /// - files ending in `.js` to be served with `Content-Type: text/javascript;
  ///   charset=utf-8`
  /// - files ending in `.jpg` to be served with `Content-Type: image/jpeg`
  /// - files ending in `.woff2` to be served with `Content-Type: font/woff2`
  ///
  /// @param propertiesString a string with a file extension / content-type
  ///        mapping in each line
  void contentTypes(String propertiesString);

}