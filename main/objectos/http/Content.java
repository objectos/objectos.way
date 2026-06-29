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

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import objectos.lang.BinaryObject;
import objectox.http.media.ContentBinaryObject;
import objectox.http.media.ContentBytes;

/// Represents content associated to a `MediaType` to be transmitted over the
/// HTTP protocol.
public sealed interface Content
    extends
    BinaryObject,
    Result,
    RoutingOption
    permits
    ContentBinaryObject,
    ContentBytes {

  /// Returns a new `Content` object whose contents are provided by the
  /// specified `BinaryObject`.
  ///
  /// @param contentType the type of the content
  /// @param contents the string providing the `UTF-8` bytes
  ///
  /// @return a newly created content object
  static Content of(MediaType contentType, BinaryObject contents) {
    return new ContentBinaryObject(
        Objects.requireNonNull(contentType, "contentType == null"),

        Objects.requireNonNull(contents, "contents == null")
    );
  }

  /// Returns a new `Content` object whose contents are the specified bytes.
  ///
  /// @param contentType the type of the content
  /// @param bytes the contents
  ///
  /// @return a newly created content object
  static Content of(MediaType contentType, byte[] bytes) {
    final MediaType type;
    type = Objects.requireNonNull(contentType, "contentType == null");

    final byte[] value;
    value = bytes.clone();

    return new ContentBytes(type, value);
  }

  /// Returns a new `Content` object whose contents are the `UTF-8` bytes of the
  /// specified string.
  ///
  /// @param contentType the type of the content
  /// @param contents the string providing the `UTF-8` bytes
  ///
  /// @return a newly created content object
  static Content of(MediaType contentType, String contents) {
    final MediaType type;
    type = Objects.requireNonNull(contentType, "contentType == null");

    final byte[] value;
    value = contents.getBytes(StandardCharsets.UTF_8);

    return new ContentBytes(type, value);
  }

}