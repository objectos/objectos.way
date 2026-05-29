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
import objectos.lang.OutputStreamConsumer;
import objectos.way.Media;

/// An HTTP response message.
public sealed interface Response extends Result permits ResponsePojo {

  /// Configures the creation of a `Response` object.
  sealed interface Options permits ResponseBuilder {

    /// Sets the status of the response message.
    ///
    /// @param value the response status
    void status(HttpStatus value);

    /// Adds the specified header field to the response message.
    ///
    /// @param name the header name
    /// @param value the header value
    void header(HttpHeaderName name, long value);

    /// Adds the specified header field to the response message.
    ///
    /// @param name the header name
    /// @param value the header value
    void header(HttpHeaderName name, String value);

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
    void header(HttpHeaderName name, Consumer<? super HttpHeaderValueBuilder> builder);

    /// Adds the `Date` header field with the server's current time.
    void date();

    /// Sets the response body to the contents provided by the specified entity.
    ///
    /// @param entity the object providing the body contents
    void body(OutputStreamConsumer entity);

    /// Sets the response body to the contents of the specified file.
    ///
    /// @param file the path to a regular file containing the body contents
    void body(Path file);

  }

  /// Creates a new response with the specified options.
  ///
  /// @param opts allows for setting the options
  ///
  /// @return a newly created response message
  static Response create(Consumer<? super Options> opts) {
    final ResponseBuilder builder;
    builder = new ResponseBuilder();

    opts.accept(builder);

    return builder.build();
  }

  /// Returns a `200 OK` response with the specified media entity.
  ///
  /// @param media the media entity
  ///
  /// @return a newly created response message
  static Response ok(Media media) {
    return create(opts -> {
      opts.status(HttpStatus.OK);

      opts.date();

      final String contentType;
      contentType = media.contentType();

      if (contentType == null) {
        throw new IllegalArgumentException("The specified Media provided a null content-type");
      }

      opts.header(HttpHeaderName.CONTENT_TYPE, contentType);

      opts.body(media::writeTo);
    });
  }

}
