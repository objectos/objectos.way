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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import objectos.http.HeaderName;
import objectos.http.server.ServerRequestHeaders;
import objectos.lang.object.Check;
import objectox.http.ObjectoxHeaderName;

class ObjectoxServerRequestHeaders extends ObjectoxRequestLine implements ServerRequestHeaders {

  HeaderName headerName;

  ObjectoxHeader[] standardHeaders;

  int standardHeadersCount;

  Map<HeaderName, ObjectoxHeader> unknownHeaders;

  ObjectoxServerRequestHeaders() {}

  // public API

  @Override
  public final String first(HeaderName name) {
    Check.notNull(name, "name == null");

    int index;
    index = name.index();

    if (index >= 0) {

      if (standardHeaders == null) {
        return null;
      }

      ObjectoxHeader maybe;
      maybe = standardHeaders[index];

      if (maybe != null) {
        return maybe.get();
      } else {
        return null;
      }

    } else {

      if (unknownHeaders == null) {
        return null;
      }

      ObjectoxHeader maybe;
      maybe = unknownHeaders.get(name);

      if (maybe != null) {
        return maybe.get();
      } else {
        return null;
      }

    }
  }

  @Override
  public final int size() {
    int size = 0;

    if (standardHeaders != null) {
      size += standardHeadersCount;
    }

    if (unknownHeaders != null) {
      size += unknownHeaders.size();
    }

    return size;
  }

  // unchecked API

  final ObjectoxHeader headerUnchecked(HeaderName name) {
    if (standardHeaders == null) {
      return null;
    } else {
      int index;
      index = name.index();

      return standardHeaders[index];
    }
  }

  // Internal

  final void resetHeaders() {
    headerName = null;

    if (standardHeaders != null) {
      Arrays.fill(standardHeaders, null);
    }

    standardHeadersCount = 0;

    if (unknownHeaders != null) {
      unknownHeaders.clear();
    }
  }

  final void parseHeaders() throws IOException {
    parseLine();

    while (!consumeIfEmptyLine()) {
      parseStandardHeaderName();

      if (badRequest != null) {
        break;
      }

      if (headerName == null) {
        parseUnknownHeaderName();

        if (badRequest != null) {
          break;
        }
      }

      parseHeaderValue();

      parseLine();
    }

    // clear last header name just in case
    headerName = null;
  }

  private void parseStandardHeaderName() {
    // we reset any previous found header name

    headerName = null;

    // we will use the first char as hash code
    if (bufferIndex >= lineLimit) {
      badRequest = BadRequestReason.INVALID_HEADER;

      return;
    }

    final byte first;
    first = buffer[bufferIndex];

    // ad hoc hash map

    switch (first) {
      case 'A' -> parseHeaderName0(
          HeaderName.ACCEPT_ENCODING
      );

      case 'C' -> parseHeaderName0(
          HeaderName.CONNECTION,
          HeaderName.CONTENT_LENGTH,
          HeaderName.CONTENT_TYPE,
          HeaderName.COOKIE
      );

      case 'D' -> parseHeaderName0(
          HeaderName.DATE
      );

      case 'H' -> parseHeaderName0(
          HeaderName.HOST
      );

      case 'T' -> parseHeaderName0(
          HeaderName.TRANSFER_ENCODING
      );

      case 'U' -> parseHeaderName0(
          HeaderName.USER_AGENT
      );
    }
  }

  static final byte[][] STD_HEADER_NAME_BYTES;

  static {
    int size;
    size = ObjectoxHeaderName.standardNamesSize();

    byte[][] map;
    map = new byte[size][];

    for (int i = 0; i < size; i++) {
      ObjectoxHeaderName headerName;
      headerName = ObjectoxHeaderName.standardName(i);

      String name;
      name = headerName.capitalized();

      map[i] = name.getBytes(StandardCharsets.UTF_8);
    }

    STD_HEADER_NAME_BYTES = map;
  }

  private void parseHeaderName0(HeaderName candidate) {
    int index;
    index = candidate.index();

    final byte[] candidateBytes;
    candidateBytes = STD_HEADER_NAME_BYTES[index];

    if (!matches(candidateBytes)) {
      // does not match -> try next

      return;
    }

    if (bufferIndex >= lineLimit) {
      // matches but reached end of line -> bad request

      badRequest = BadRequestReason.INVALID_HEADER;

      return;
    }

    byte maybeColon;
    maybeColon = buffer[bufferIndex++];

    if (maybeColon != Bytes.COLON) {
      // matches but is not followed by a colon character

      badRequest = BadRequestReason.INVALID_HEADER;

      return;
    }

    headerName = candidate;
  }

  private void parseHeaderName0(HeaderName c0, HeaderName c1, HeaderName c2,
                                HeaderName c3) {
    parseHeaderName0(c0);

    if (headerName != null) {
      return;
    }

    parseHeaderName0(c1);

    if (headerName != null) {
      return;
    }

    parseHeaderName0(c2);

    if (headerName != null) {
      return;
    }

    parseHeaderName0(c3);
  }

  private void parseUnknownHeaderName() {
    int startIndex;
    startIndex = bufferIndex;

    int colonIndex;
    colonIndex = indexOf(Bytes.COLON);

    if (colonIndex < 0) {
      // no colon found
      badRequest = BadRequestReason.INVALID_HEADER;

      return;
    }

    if (startIndex == colonIndex) {
      // empty header name
      badRequest = BadRequestReason.INVALID_HEADER;

      return;
    }

    String name;
    name = bufferToString(startIndex, colonIndex);

    headerName = HeaderName.create(name);

    // resume immediately after the colon
    bufferIndex = colonIndex + 1;
  }

  private void parseHeaderValue() {
    int startIndex;
    startIndex = parseHeaderValueStart();

    int endIndex;
    endIndex = parseHeaderValueEnd();

    if (startIndex > endIndex) {
      // value has negative length... is it possible?
      throw new UnsupportedOperationException("Implement me");
    }

    int index;
    index = headerName.index();

    if (index >= 0) {
      if (standardHeaders == null) {
        int size;
        size = ObjectoxHeaderName.standardNamesSize();

        standardHeaders = new ObjectoxHeader[size];
      }

      ObjectoxHeader header;
      header = standardHeaders[index];

      if (header == null) {
        header = new ObjectoxHeader(headerName, this, startIndex, endIndex);

        standardHeadersCount++;
      } else {
        header = header.add(startIndex, endIndex);
      }

      standardHeaders[index] = header;
    } else {
      if (unknownHeaders == null) {
        unknownHeaders = new HashMap<>();
      }

      HeaderName name;
      name = headerName;

      ObjectoxHeader header;
      header = unknownHeaders.get(name);

      if (header == null) {
        header = new ObjectoxHeader(headerName, this, startIndex, endIndex);
      } else {
        header = header.add(startIndex, endIndex);
      }

      unknownHeaders.put(name, header);
    }
  }

  private int parseHeaderValueStart() {
    // consumes and discard a single leading OWS if present
    byte maybeOws;
    maybeOws = buffer[bufferIndex];

    if (Bytes.isOptionalWhitespace(maybeOws)) {
      // consume and discard leading OWS
      bufferIndex++;
    }

    return bufferIndex;
  }

  private int parseHeaderValueEnd() {
    int end;
    end = lineLimit;

    byte maybeCR;
    maybeCR = buffer[end - 1];

    if (maybeCR == Bytes.CR) {
      // value ends at the CR of the line end CRLF
      end = end - 1;
    }

    byte maybeOWS;
    maybeOWS = buffer[end - 1];

    if (Bytes.isOptionalWhitespace(maybeOWS)) {
      // value ends at the trailing OWS
      end = end - 1;
    }

    // resume immediately after lineLimite
    bufferIndex = lineLimit + 1;

    return end;
  }

}
