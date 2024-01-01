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
import java.util.EnumMap;
import java.util.Map;
import objectos.http.HeaderName;
import objectos.http.server.ServerRequestHeaders;
import objectos.lang.object.Check;
import objectox.http.HttpStatus;
import objectox.http.StandardHeaderName;

public final class ObjectoxServerRequestHeaders implements ServerRequestHeaders {

  private final SocketInput input;

  HeaderName headerName;

  Map<StandardHeaderName, ObjectoxHeader> standardHeaders;

  HttpStatus status;

  public ObjectoxServerRequestHeaders(SocketInput input) {
    this.input = input;
  }

  // API

  public final boolean contains(HeaderName name) {
    Check.notNull(name, "name == null");

    int index;
    index = name.index();

    if (index < 0) {
      throw new UnsupportedOperationException("Implement me");
    }

    if (standardHeaders == null) {
      return false;
    }

    return standardHeaders.containsKey(name);
  }

  public final String first(HeaderName name) {
    Check.notNull(name, "name == null");

    int index;
    index = name.index();

    if (index < 0) {
      throw new UnsupportedOperationException("Implement me");
    }

    if (standardHeaders == null) {
      return null;
    }

    ObjectoxHeader maybe;
    maybe = standardHeaders.get(name);

    if (maybe != null) {
      return maybe.get();
    }

    return null;
  }

  public final int size() {
    int size = 0;

    if (standardHeaders != null) {
      size += standardHeaders.size();
    }

    return size;
  }

  // Internal

  public final void parse() throws IOException {
    input.parseLine();

    while (!input.consumeIfEmptyLine()) {
      parseStandardHeaderName();

      if (status != null) {
        break;
      }

      if (headerName == null) {
        throw new UnsupportedOperationException(
            "Implement me :: unknown header name"
        );
      }

      parseHeaderValue();

      input.parseLine();
    }
  }

  private void parseStandardHeaderName() {
    // we reset any previous found header name

    headerName = null;

    // we will use the first char as hash code
    if (!input.hasNext()) {
      status = HttpStatus.BAD_REQUEST;

      return;
    }

    final byte first;
    first = input.peek();

    // ad hoc hash map

    switch (first) {
      case 'A' -> parseHeaderName0(
          StandardHeaderName.ACCEPT_ENCODING
      );

      case 'C' -> parseHeaderName0(
          StandardHeaderName.CONNECTION,
          StandardHeaderName.CONTENT_LENGTH,
          StandardHeaderName.CONTENT_TYPE,
          StandardHeaderName.COOKIE
      );

      case 'D' -> parseHeaderName0(
          StandardHeaderName.DATE
      );

      case 'H' -> parseHeaderName0(
          StandardHeaderName.HOST
      );

      case 'T' -> parseHeaderName0(
          StandardHeaderName.TRANSFER_ENCODING
      );

      case 'U' -> parseHeaderName0(
          StandardHeaderName.USER_AGENT
      );
    }
  }

  static final Map<StandardHeaderName, byte[]> STD_HEADER_NAME_BYTES;

  static {
    Map<StandardHeaderName, byte[]> map;
    map = new EnumMap<>(StandardHeaderName.class);

    for (var headerName : StandardHeaderName.values()) {
      String name;
      name = headerName.name;

      byte[] bytes;
      bytes = name.getBytes(StandardCharsets.UTF_8);

      map.put(headerName, bytes);
    }

    STD_HEADER_NAME_BYTES = map;
  }

  private void parseHeaderName0(StandardHeaderName candidate) {
    final byte[] candidateBytes;
    candidateBytes = STD_HEADER_NAME_BYTES.get(candidate);

    if (!input.matches(candidateBytes)) {
      // does not match -> try next

      return;
    }

    if (!input.hasNext()) {
      // matches but reached end of line -> bad request

      status = HttpStatus.BAD_REQUEST;

      return;
    }

    byte maybeColon;
    maybeColon = input.next();

    if (maybeColon != Bytes.COLON) {
      // matches but is not followed by a colon character

      status = HttpStatus.BAD_REQUEST;

      return;
    }

    headerName = candidate;
  }

  private void parseHeaderName0(StandardHeaderName c0, StandardHeaderName c1, StandardHeaderName c2,
                                StandardHeaderName c3) {
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

  private void parseHeaderValue() {
    if (headerName instanceof StandardHeaderName name) {
      if (standardHeaders == null) {
        standardHeaders = new EnumMap<>(StandardHeaderName.class);
      }

      ObjectoxHeader header;
      header = standardHeaders.get(name);

      if (header == null) {
        header = new ObjectoxHeader(input, name);

        header.parseValue();
      } else {
        header = header.parseAdditionalValue();
      }

      standardHeaders.put(name, header);
    } else {
      throw new UnsupportedOperationException("Implement me");
    }
  }

}
