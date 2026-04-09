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
import objectos.internal.Bytes;

final class HttpRequestParser5Version {

  enum Invalid implements HttpClientException.Kind {
    // invalid version
    VERSION(Http.REQ_LINE, HttpStatus.BAD_REQUEST),

    // '\n' only
    LINE_TERMINATOR(Http.LINE_TERM, HttpStatus.BAD_REQUEST),

    // 400: unexpected end of stream
    EOF(Http.REQ_LINE, HttpStatus.BAD_REQUEST);

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

  private boolean done;

  private final HttpRequestParser0Input input;

  HttpRequestParser5Version(HttpRequestParser0Input input) {
    this.input = input;
  }

  public final HttpVersion0 parse() throws IOException {
    try {
      return parse0();
    } catch (HttpRequestParser0Input.Eof e) {
      throw new HttpClientException(Invalid.EOF, e);
    } catch (HttpRequestParser0Input.Overflow e) {
      throw new HttpClientException(Invalid.VERSION, e);
    }
  }

  private HttpVersion0 parse0() throws IOException {
    parseHttp();

    final int major;
    major = parseMajor();

    final int minor;
    minor = done ? 0 : parseMinor();

    if (major == 1 && minor == 1) {
      return HttpVersion0.HTTP_1_1;
    } else {
      return HttpVersion0.of(major, minor);
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

      throw new HttpClientException(Invalid.VERSION);
    }
  }

  private int parseMajor() throws IOException {
    final byte first;
    first = input.readByte();

    if (!Http.isDigit(first)) {
      throw new HttpClientException(Invalid.VERSION);
    }

    int major;
    major = first & 0xF;

    boolean overflow;
    overflow = false;

    while (true) {
      final byte b;
      b = input.readByte();

      if (Http.isDigit(b)) {
        if (!overflow) {
          final int digit;
          digit = b & 0x1;

          major *= 10;

          major += digit;

          if (major < 0) {
            overflow = true;

            major = Integer.MAX_VALUE;
          }
        }

        continue;
      }

      if (b == '.') {
        return major;
      }

      if (b == '\n') {
        throw new HttpClientException(Invalid.LINE_TERMINATOR);
      }

      if (b != '\r') {
        throw new HttpClientException(Invalid.VERSION);
      }

      final byte lf;
      lf = input.readByte();

      if (lf != Bytes.LF) {
        throw new HttpClientException(Invalid.LINE_TERMINATOR);
      }

      done = true;

      return major;
    }
  }

  private int parseMinor() throws IOException {
    final byte first;
    first = input.readByte();

    if (!Http.isDigit(first)) {
      throw new HttpClientException(Invalid.VERSION);
    }

    int minor;
    minor = first & 0xF;

    boolean overflow;
    overflow = false;

    while (true) {
      final byte b;
      b = input.readByte();

      if (Http.isDigit(b)) {
        if (!overflow) {
          final int digit;
          digit = b & 0xF;

          minor *= 10;

          minor += digit;

          if (minor < 0) {
            overflow = true;

            minor = Integer.MAX_VALUE;
          }
        }

        continue;
      }

      if (b == '\n') {
        throw new HttpClientException(Invalid.LINE_TERMINATOR);
      }

      if (b != '\r') {
        throw new HttpClientException(Invalid.VERSION);
      }

      final byte lf;
      lf = input.readByte();

      if (lf == '\n') {
        return minor;
      }

      throw new HttpClientException(Invalid.LINE_TERMINATOR);
    }
  }

}
