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

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import objectos.way.Media;

final class HttpResponseImpl implements HttpResponse, AutoCloseable {

  private record ResponseHeader(HttpHeaderName name, String value) {}

  private final HttpExchangeImpl outer;

  private HttpStatus status;

  private final List<ResponseHeader> headers = new ArrayList<>();

  private Object body;

  HttpResponseImpl(HttpExchangeImpl outer) {
    this.outer = outer;
  }

  @Override
  public final void close() {

  }

  @Override
  public final void ok(Media.Bytes media) {
    status = HttpStatus.OK;

    media(media);
  }

  @Override
  public final void ok(Media.Stream media) {
    status = HttpStatus.OK;

    media(media);
  }

  @Override
  public final void ok(Media.Text media) {
    status = HttpStatus.OK;

    media(media);
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
    this.httpExchangeImpl.header(name, builder);
  }

  @Override
  public final String now() {
    return outer.now();
  }

  @Override
  public final void body() {
    body = null;
  }

  @Override
  public final void body(byte[] bytes, int offset, int length) {
    final byte[] copy;
    copy = new byte[length];

    System.arraycopy(bytes, offset, copy, 0, length);

    body = copy;
  }

  @Override
  public final void body(Path file) {
    body = file;
  }

  @Override
  public final void media(Media media) {
    switch (media) {
      case Media.Bytes bytes -> media(bytes);

      case Media.Text text -> media(text);

      case Media.Stream stream -> media(stream);
    }
  }

  public final void media(Media.Bytes media) {
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

  public final void media(Media.Text media) {
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

  public final void media(Media.Stream media) {
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
        throw invalidFieldContent(idx, c);
      }

      final byte flag;
      flag = HEADER_VALUE_TABLE[c];

      switch (parser) {
        case START -> {
          if (flag == HEADER_VALUE_VALID) {
            parser = Parser.NORMAL;
          }

          else if (flag == HEADER_VALUE_WS) {
            throw new IllegalArgumentException("Leading SPACE or HTAB characters are not allowed");
          }

          else {
            throw invalidFieldContent(idx, c);
          }
        }

        case NORMAL, WS -> {
          if (flag == HEADER_VALUE_VALID) {
            parser = Parser.NORMAL;
          }

          else if (flag == HEADER_VALUE_WS) {
            parser = Parser.WS;
          }

          else {
            throw invalidFieldContent(idx, c);
          }
        }

        case INVALID -> {
          throw invalidFieldContent(idx, c);
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

  private void header0(HttpHeaderName name, long value) {
    final String $value;
    $value = Long.toString(value);

    header0(name, $value);
  }

  private void header0(HttpHeaderName name, String value) {
    final ResponseHeader header;
    header = new ResponseHeader(name, value);

    headers.add(header);
  }

  private IllegalArgumentException invalidFieldContent(int idx, char c) {
    return new IllegalArgumentException("Invalid character at index " + idx + ": " + c);
  }

}