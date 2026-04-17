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
import java.io.InputStream;
import java.nio.file.Path;
import objectos.way.Media;

/// Configures the static files to be served by a `HttpServer` instance.
sealed interface HttpStaticFiles permits HttpStaticFiles0 {

  /// Recursively serves the contents of the specified directory as if it was at
  /// the root of the HTTP server.
  ///
  /// @param directory the directory whose contents are to be served
  ///
  /// @throws IOException if an I/O error occurs
  void addDirectory(Path directory) throws IOException;

  /// Serves the bytes from the input stream at the specified path.
  ///
  /// @param pathName the absolute path of the file to be created. It must start
  ///        with a '/' character.
  /// @param in the input stream to read from
  ///
  /// @throws IOException if an I/O error occurs
  void addFile(String pathName, InputStream in) throws IOException;

  /// Serves the contents of the specified media at the specified path.
  /// Additionally, the content type of the media is associated to the path's
  /// file extension, if one is not already associated.
  ///
  /// @param pathName the absolute path of the file to be created. It must start
  ///        with a '/' character.
  /// @param media the media object whose contents is to be served
  ///
  /// @throws IOException if an I/O error occurs
  void addMedia(String pathName, Media media) throws IOException;

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