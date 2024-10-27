/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.way;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import objectos.way.HttpExchangeLoop.ParseStatus;

class HttpRequestBody extends HttpRequestHeaders implements Http.Request.Body {

  private enum Kind {
    EMPTY,

    IN_BUFFER,

    FILE;
  }

  private Kind kind = Kind.EMPTY;

  private java.nio.file.Path requestBodyDirectory;

  private java.nio.file.Path requestBodyFile;

  HttpRequestBody() {}

  public void requestBodyDirectory(java.nio.file.Path directory) {
    requestBodyDirectory = directory;
  }

  public void close() throws IOException {
    if (requestBodyFile != null) {
      Files.delete(requestBodyFile);
    }
  }

  @Override
  public final InputStream openStream() throws IOException {
    return switch (kind) {
      case EMPTY -> InputStream.nullInputStream();

      case IN_BUFFER -> openStreamImpl();

      case FILE -> Files.newInputStream(requestBodyFile);
    };
  }

  final void resetRequestBody() {
    kind = Kind.EMPTY;

    requestBodyFile = null;
  }

  final void parseRequestBody() throws IOException {
    HttpHeader contentLength;
    contentLength = headerUnchecked(Http.HeaderName.CONTENT_LENGTH);

    if (contentLength != null) {
      long value;
      value = contentLength.unsignedLongValue();

      if (value < 0) {
        parseStatus = ParseStatus.INVALID_HEADER;
      }

      else if (canBuffer(value)) {
        int read;
        read = read(value);

        if (read < 0) {
          throw new EOFException();
        }

        kind = Kind.IN_BUFFER;
      }

      else {
        if (requestBodyDirectory == null) {
          requestBodyFile = Files.createTempFile("objectos-way-request-body-", ".tmp");
        } else {
          requestBodyFile = Files.createTempFile(requestBodyDirectory, "objectos-way-request-body-", ".tmp");
        }

        long read;
        read = read(requestBodyFile, value);

        if (read < 0) {
          parseStatus = ParseStatus.EOF;
        } else {
          kind = Kind.FILE;
        }
      }

      return;
    }

    HttpHeader transferEncoding;
    transferEncoding = headerUnchecked(Http.HeaderName.TRANSFER_ENCODING);

    if (transferEncoding != null) {
      throw new UnsupportedOperationException("Implement me");
    }
  }

}