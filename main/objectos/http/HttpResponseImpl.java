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

import module java.base;
import module objectos.way;

final class HttpResponseImpl implements HttpResponse, AutoCloseable {

  private final HttpExchangeImpl outer;

  private HttpStatus status;

  private final List<HttpResponse1Header> headers = new ArrayList<>();

  private Object body;

  private Throwable error;

  HttpResponseImpl(HttpExchangeImpl outer) {
    this.outer = outer;
  }

  @Override
  public final void close() {
    if (status == null) {
      throw new IllegalStateException("A response status was not set");
    }

    outer.respond(status, headers, body);

    if (error != null) {
      outer.note(error);
    }
  }

  // 2xx responses

  @Override
  public final void ok(Media media) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final void ok(Media.Bytes media) {
    status = HttpStatus.OK;

    send(media);
  }

  @Override
  public final void ok(Media.Stream media) {
    status = HttpStatus.OK;

    send(media);
  }

  @Override
  public final void ok(Media.Text media) {
    status = HttpStatus.OK;

    send(media);
  }

  // 3xx responses

  @Override
  public final void movedPermanently(String location) {
    location0(HttpStatus.MOVED_PERMANENTLY, location);
  }

  @Override
  public final void found(String location) {
    location0(HttpStatus.FOUND, location);
  }

  @Override
  public final void seeOther(String location) {
    location0(HttpStatus.SEE_OTHER, location);
  }

  private void location0(HttpStatus status, String location) {
    this.status = status;

    Objects.requireNonNull(location, "location == null");

    final String raw;
    raw = Http.raw(location);

    header0(HttpHeaderName.DATE, now());

    header0(HttpHeaderName.CONTENT_LENGTH, 0L);

    header0(HttpHeaderName.LOCATION, raw);

    body = null;
  }

  // 4xx responses

  @Override
  public final void badRequest(Media media) {
    status = HttpStatus.BAD_REQUEST;

    send(media);
  }

  @Override
  public final void forbidden(Media media) {
    status = HttpStatus.FORBIDDEN;

    send(media);
  }

  @Override
  public final void notFound(Media media) {
    status = HttpStatus.NOT_FOUND;

    send(media);
  }

  @Override
  public final void allow(HttpMethod... methods) {
    Objects.requireNonNull(methods, "methods == null");

    final String allow;
    allow = Arrays.stream(methods).map(HttpMethod::name).collect(Collectors.joining(", "));

    status = HttpStatus.METHOD_NOT_ALLOWED;

    header0(HttpHeaderName.DATE, now());

    header0(HttpHeaderName.ALLOW, allow);

    header0(HttpHeaderName.CONTENT_LENGTH, 0L);

    send();
  }

  // 5xx responses

  @Override
  public final void internalServerError(Media media, Throwable error) {
    status = HttpStatus.INTERNAL_SERVER_ERROR;

    send(media);

    this.error = error;
  }

  @Override
  public final void status(HttpStatus value) {
    status = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void header(HttpHeaderName name, long value) {
    Objects.requireNonNull(name, "name == null");

    header0(name, value);
  }

  @Override
  public final void header(HttpHeaderName name, String value) {
    Objects.requireNonNull(name, "name == null");
    Objects.requireNonNull(value, "value == null");

    header0(name, value);
  }

  @Override
  public final void header(HttpHeaderName name, Consumer<? super HttpHeaderValueBuilder> builder) {
    Objects.requireNonNull(name, "name == null");

    final HttpHeaderValueBuilderImpl valueBuilder;
    valueBuilder = new HttpHeaderValueBuilderImpl();

    builder.accept(valueBuilder);

    final String value;
    value = valueBuilder.build();

    header0(name, value);
  }

  @Override
  public final String now() {
    return outer.now();
  }

  @Override
  public final void send() {
    body = null;
  }

  @Override
  public final void send(byte[] bytes) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final void send(byte[] bytes, int offset, int length) {
    final byte[] copy;
    copy = new byte[length];

    System.arraycopy(bytes, offset, copy, 0, length);

    body = copy;
  }

  @Override
  public final void send(Path file) {
    body = file;
  }

  @Override
  public final void send(Media media) {
    switch (media) {
      case Media.Bytes bytes -> send(bytes);

      case Media.Text text -> send(text);

      case Media.Stream stream -> send(stream);
    }
  }

  @Override
  public final void send(Media.Bytes media) {
    // early media validation
    final String contentType;
    contentType = media.contentType();

    if (contentType == null) {
      throw new IllegalArgumentException("The specified Media.Bytes provided a null content-type");
    }

    final byte[] bytes;
    bytes = media.toByteArray();

    if (bytes == null) {
      throw new IllegalArgumentException("The specified Media.Bytes provided a null byte array");
    }

    header0(HttpHeaderName.DATE, now());

    header0(HttpHeaderName.CONTENT_TYPE, contentType);

    header0(HttpHeaderName.CONTENT_LENGTH, bytes.length);

    body = bytes;
  }

  @Override
  public final void send(Media.Text media) {
    // early media validation
    final String contentType;
    contentType = media.contentType();

    if (contentType == null) {
      throw new IllegalArgumentException("The specified Media.Text provided a null content-type");
    }

    final Charset charset;
    charset = media.charset();

    if (charset == null) {
      throw new IllegalArgumentException("The specified Media.Text provided a null charset");
    }

    header0(HttpHeaderName.DATE, now());

    header0(HttpHeaderName.CONTENT_TYPE, contentType);

    header0(HttpHeaderName.TRANSFER_ENCODING, "chunked");

    body = media;
  }

  @Override
  public final void send(Media.Stream media) {
    // early media validation
    final String contentType;
    contentType = media.contentType();

    if (contentType == null) {
      throw new IllegalArgumentException("The specified Media.Stream provided a null content-type");
    }

    header0(HttpHeaderName.DATE, now());

    header0(HttpHeaderName.CONTENT_TYPE, contentType);

    header0(HttpHeaderName.TRANSFER_ENCODING, "chunked");

    body = media;
  }

  private void header0(HttpHeaderName name, long value) {
    final String $value;
    $value = Long.toString(value);

    final HttpResponse1Header header;
    header = new HttpResponse1Header(name, $value);

    headers.add(header);
  }

  private void header0(HttpHeaderName name, String value) {
    checkHeaderValue(value);

    final HttpResponse1Header header;
    header = new HttpResponse1Header(name, value);

    headers.add(header);
  }

  private void checkHeaderValue(String value) {
    enum Parser {

      START,

      NORMAL,

      WS,

      INVALID;

    }

    final int len;
    len = value.length(); // early implicit null-check

    Parser parser;
    parser = Parser.START;

    for (int idx = 0; idx < len; idx++) {
      final char c;
      c = value.charAt(idx);

      if (c >= 128) {
        throw Http.invalidFieldContent(idx, c);
      }

      final byte flag;
      flag = Http.HEADER_VALUE_TABLE[c];

      switch (parser) {
        case START -> {
          if (flag == Http.HEADER_VALUE_VALID) {
            parser = Parser.NORMAL;
          }

          else if (flag == Http.HEADER_VALUE_WS) {
            throw new IllegalArgumentException("Leading SPACE or HTAB characters are not allowed");
          }

          else {
            throw Http.invalidFieldContent(idx, c);
          }
        }

        case NORMAL, WS -> {
          if (flag == Http.HEADER_VALUE_VALID) {
            parser = Parser.NORMAL;
          }

          else if (flag == Http.HEADER_VALUE_WS) {
            parser = Parser.WS;
          }

          else {
            throw Http.invalidFieldContent(idx, c);
          }
        }

        case INVALID -> {
          throw Http.invalidFieldContent(idx, c);
        }
      }
    }

    switch (parser) {
      case START, NORMAL -> {
        // valid - noop
      }

      case WS -> {
        throw new IllegalArgumentException("Trailing SPACE or HTAB characters are not allowed");
      }

      case INVALID -> {
        throw new IllegalStateException("Unexpected INVALID state");
      }
    }
  }

}