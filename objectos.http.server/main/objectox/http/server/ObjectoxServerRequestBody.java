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
package objectox.http.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import objectos.http.HeaderName;
import objectos.http.server.Body;

class ObjectoxServerRequestBody extends ObjectoxServerRequestHeaders implements Body {

  private enum Kind {
    EMPTY,

    IN_BUFFER;
  }

  private Kind kind = Kind.EMPTY;

  ObjectoxServerRequestBody() {}

  @Override
  public final InputStream openStream() {
    return switch (kind) {
      case EMPTY -> InputStream.nullInputStream();

      case IN_BUFFER -> openStreamImpl();
    };
  }

  final void resetRequestBody() {
    kind = Kind.EMPTY;
  }

  final void parseRequestBody() throws IOException {
    ObjectoxHeader contentLength;
    contentLength = headerUnchecked(HeaderName.CONTENT_LENGTH);

    if (contentLength != null) {
      long value;
      value = contentLength.unsignedLongValue();

      if (value < 0) {
        badRequest = BadRequestReason.INVALID_HEADER;

        return;
      }

      if (!canBuffer(value)) {
        throw new UnsupportedOperationException(
            "Implement me :: persist request body to file"
        );
      }

      int read;
      read = read(value);

      if (read < 0) {
        throw new EOFException();
      }

      kind = Kind.IN_BUFFER;

      return;
    }

    ObjectoxHeader transferEncoding;
    transferEncoding = headerUnchecked(HeaderName.TRANSFER_ENCODING);

    if (transferEncoding != null) {
      throw new UnsupportedOperationException("Implement me");
    }
  }

}