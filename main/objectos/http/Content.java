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
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/// Represents content of an specific format to be transmitted over the
/// HTTP protocol.
public non-sealed interface Content extends Result {

  /// Returns a new `Content` object with the specified content type and the
  /// `UTF-8` bytes of the specified string.
  ///
  /// @param contentType the type of the content
  /// @param contents the textual content which will be `UTF-8` encoded
  static Content of(MediaType contentType, String contents) {
    record ContentPojo(MediaType type, byte[] value) implements Content {
      @Override
      public final void sendContent(ContentSender sender) throws IOException {
        sender.send(type, value);
      }
    }

    final MediaType type;
    type = Objects.requireNonNull(contentType, "contentType == null");

    final byte[] value;
    value = contents.getBytes(StandardCharsets.UTF_8);

    return new ContentPojo(type, value);
  }

  /// Sends the contents of this entity using the specified sender.
  ///
  /// @param sender the object responsible for sending the contents of this
  ///        entity
  ///
  /// @throws IOException if an I/O error occurs
  void sendContent(ContentSender sender) throws IOException;

}