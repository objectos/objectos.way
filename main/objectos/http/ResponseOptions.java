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
import java.util.function.Consumer;
import objectox.http.resp.ResponseBuilder;

/// Configures the creation of a `Response` object.
public sealed interface ResponseOptions permits ResponseBuilder {

  /// Sets the status of the response message.
  ///
  /// @param value the response status
  void status(Status value);

  /// Adds the specified header field to the response message.
  ///
  /// @param name the header name
  /// @param value the header value
  void header(HeaderName name, long value);

  /// Adds the specified header field to the response message.
  ///
  /// @param name the header name
  /// @param value the header value
  void header(HeaderName name, String value);

  /// Adds the specified header field to the response message.
  ///
  /// Example usage:
  ///
  /// ```java
  /// response.header(Http.HeaderName.CONTENT_DISPOSITION, builder -> {
  ///   builder.value("attachment");
  ///   builder.param("filename", "document.pdf");
  ///   builder.param("filename*", StandardCharsets.UTF_8, "document.pdf");
  /// });
  /// ```
  ///
  /// Which would result in the following header field written out to the
  /// response:
  ///
  /// ```
  /// Content-Disposition: attachment; filename=document.pdf; filename*=UTF-8''document.pdf
  /// ```
  ///
  /// @param name the header name
  /// @param builder a handle for creating the header field value
  void header(HeaderName name, Consumer<? super HttpHeaderValueBuilder> builder);

  /// Adds the `Date` header field with the server's current time.
  void date();

  /// Sends the specified file as part of this response message.
  ///
  /// @param file the path to a regular file to be sent as part of this
  ///        response message
  void send(Path file);

  /// Sends the specified `Content` object as part of this response message. As
  /// a minimum, the `Content-Type` header field will be appended to the
  /// response, and the response body will be provided by the specified content
  /// object.
  ///
  /// @param value the object to be sent as part of this response message
  void send(Content value);

  /// Sends the `Content` from the specified provider as part of this response
  /// message. As a minimum, the `Content-Type` header field will be appended to
  /// the response, and the response body will be provided by the specified
  /// content object.
  ///
  /// @param value the object providing the `Content` object
  void send(ContentProvider value);

}