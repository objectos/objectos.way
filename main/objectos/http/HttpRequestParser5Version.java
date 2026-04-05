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

final class HttpRequestParser5Version {

  enum Invalid implements HttpClientException.Kind {
    // invalid version
    VERSION(Http.REQ_LINE, HttpStatus.BAD_REQUEST),

    // '\n' only
    LINE_TERMINATOR(Http.LINE_TERM, HttpStatus.BAD_REQUEST),

    // 400: unexpected end of stream
    EOF(Http.REQ_LINE, HttpStatus.BAD_REQUEST),

    // 505 HTTP Version Not Supported
    HTTP_VERSION_NOT_SUPPORTED(Http.REQ_LINE, HttpStatus.HTTP_VERSION_NOT_SUPPORTED);

    private final String message;

    private final HttpStatus status;

    private Invalid(String message, HttpStatus status) {
      this.message = message;

      this.status = status;
    }

    @Override
    public final String message() {
      return message;
    }

    @Override
    public final HttpStatus status() {
      return status;
    }
  }

  private final HttpRequestParser0Input input;

  HttpRequestParser5Version(HttpRequestParser0Input input) {
    this.input = input;
  }

  public final HttpVersion parse() throws IOException {
    try {
      return parse0();
    } catch (HttpRequestParser0Input.Eof e) {
      throw HttpClientException.of(Invalid.EOF, e);
    } catch (HttpRequestParser0Input.Overflow e) {
      throw HttpClientException.of(Invalid.VERSION, e);
    }
  }

  private HttpVersion parse0() throws IOException {
    parseHttp();

    final int major;
    major = parseMajor();

    final int minor;
    minor = parseMinor();

    if (major == 1 && minor == 1) {
      return HttpVersion.HTTP_1_1;
    } else {
      throw HttpClientException.of(Invalid.HTTP_VERSION_NOT_SUPPORTED);
    }
  }

  private static final byte[] HTTP = "HTTP/".getBytes(StandardCharsets.US_ASCII);

  private void parseHttp() throws IOException {
    for (int idx = 0; idx < HTTP.length; idx++) {
      final byte b;
      b = input.readByte();

      if (HTTP[idx] == b) {
        continue;
      }

      throw HttpClientException.of(Invalid.VERSION);
    }
  }

  private int parseMajor() throws IOException {
    final byte first;
    first = input.readByte();

    if (!Http.isDigit(first)) {
      throw HttpClientException.of(Invalid.VERSION);
    }

    int major;
    major = first & 0xF;

    while (true) {
      final byte b;
      b = input.readByte();

      if (Http.isDigit(b)) {
        // absolute value is not important as it is already invalid
        major = Integer.MAX_VALUE;

        continue;
      }

      if (b == '.') {
        break;
      }

      if (b == '\n') {
        throw HttpClientException.of(Invalid.LINE_TERMINATOR);
      }

      if (b != '\r') {
        throw HttpClientException.of(Invalid.VERSION);
      }

      final byte lf;
      lf = input.readByte();

      final Invalid invalid;
      invalid = lf == '\n' ? Invalid.HTTP_VERSION_NOT_SUPPORTED : Invalid.LINE_TERMINATOR;

      throw HttpClientException.of(invalid);
    }

    return major;
  }

  private int parseMinor() throws IOException {
    final byte first;
    first = input.readByte();

    if (!Http.isDigit(first)) {
      throw HttpClientException.of(Invalid.VERSION);
    }

    int minor;
    minor = first & 0xF;

    while (true) {
      final byte b;
      b = input.readByte();

      if (Http.isDigit(b)) {
        continue;
      }

      if (b == '\n') {
        throw HttpClientException.of(Invalid.LINE_TERMINATOR);
      }

      if (b != '\r') {
        throw HttpClientException.of(Invalid.VERSION);
      }

      final byte lf;
      lf = input.readByte();

      if (lf == '\n') {
        break;
      }

      throw HttpClientException.of(Invalid.LINE_TERMINATOR);
    }

    return minor;
  }

}
