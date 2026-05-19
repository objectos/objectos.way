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

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.function.Consumer;
import objectos.way.Media;

/// An HTTP response message.
public sealed interface Response permits Response0 {

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

    /// Sets the response body to the empty value.
    void body();

    /// Sets the response body to the specified bytes.
    ///
    /// @param bytes the array of bytes with the body contents
    void body(byte[] bytes);

    /// Sets the response body to the specified bytes.
    ///
    /// @param bytes the array of bytes with the body contents
    /// @param offset index where the actual body begins
    /// @param length the body length in bytes
    void body(byte[] bytes, int offset, int length);

    /// Sets the response body to the contents of specified file.
    ///
    /// @param file the path to a regular file containing the body contents
    void body(Path file);

    /// Sets the response body to the contents of the specified source of bytes.
    ///
    /// @param source an object providing the body contents
    void body(Media.Stream source);

    /// Sets the response body to the contents of the specified source of
    /// characters.
    ///
    /// @param source an object providing the body contents
    void body(Media.Text source);

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

  /// Returns a `200 OK` response message with the specified media entity.
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

      switch (media) {
        case Media.Bytes b -> {
          final byte[] bytes;
          bytes = b.toByteArray();

          if (bytes == null) {
            throw new IllegalArgumentException("The specified Media.Bytes provided a null byte array");
          }

          final int length;
          length = bytes.length;

          opts.header(HttpHeaderName.CONTENT_LENGTH, length);

          opts.body(bytes);
        }

        case Media.Stream s -> {
          opts.body((OutputStream out) -> {
            s.writeTo(out);

            return 0;
          });
        }

        case Media.Text t -> {
          final Charset charset;
          charset = t.charset();

          if (charset == null) {
            throw new IllegalArgumentException("The specified Media.Text provided a null charset");
          }

          opts.body((Appendable out) -> {
            t.writeTo(out);

            return 0;
          });
        }
      }
    });
  }

  /// Returns a `301 Moved Permanently` response message with the specified
  /// `Location` header.
  ///
  /// @param location the value of the `Location` header
  ///
  /// @return a newly created response message
  static Response movedPermanently(String location) {
    throw new UnsupportedOperationException("Implement me");
  }

}
