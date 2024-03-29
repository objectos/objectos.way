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
package objectos.http;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import objectos.http.WayServerLoop.ParseStatus;

class WayRequestLine extends SocketInput {

  Method method;

  WayUriPath path;

  UriQuery query = EmptyUriQuery.INSTANCE;

  byte versionMajor;

  byte versionMinor;

  WayRequestLine() {}

  final void resetRequestLine() {
    method = null;

    path.reset();

    query = EmptyUriQuery.INSTANCE;

    versionMajor = versionMinor = 0;
  }

  final void parseRequestLine() throws IOException {
    parseLine();

    if (parseStatus == ParseStatus.UNEXPECTED_EOF) {
      if (bufferLimit == 0) {
        // buffer is empty, this is an expected EOF
        parseStatus = ParseStatus.EOF;
      }

      return;
    }

    parseMethod();

    if (method == null) {
      // parse method failed -> bad request
      parseStatus = ParseStatus.INVALID_METHOD;

      return;
    }

    int startIndex;
    startIndex = parsePathStart();

    if (parseStatus.isError()) {
      // bad request -> fail
      return;
    }

    parsePathRest(startIndex);

    if (parseStatus.isError()) {
      // bad request -> fail
      return;
    }

    parseVersion();

    if (parseStatus.isError()) {
      // bad request -> fail
      return;
    }

    if (!consumeIfEndOfLine()) {
      parseStatus = ParseStatus.INVALID_REQUEST_LINE_TERMINATOR;

      return;
    }
  }

  private void parseMethod() throws IOException {
    if (bufferIndex >= lineLimit) {
      // empty line... nothing to do
      return;
    }

    byte first;
    first = buffer[bufferIndex];

    // based on the first char, we select out method candidate

    switch (first) {
      case 'C' -> parseMethod0(Method.CONNECT);

      case 'D' -> parseMethod0(Method.DELETE);

      case 'G' -> parseMethod0(Method.GET);

      case 'H' -> parseMethod0(Method.HEAD);

      case 'O' -> parseMethod0(Method.OPTIONS);

      case 'P' -> parseMethodP();

      case 'T' -> parseMethod0(Method.TRACE);
    }
  }

  static final byte[][] STD_METHOD_BYTES;

  static {
    int size;
    size = WayMethod.size();

    byte[][] map;
    map = new byte[size][];

    for (int index = 0; index < size; index++) {
      WayMethod method;
      method = WayMethod.get(index);

      String nameAndSpace;
      nameAndSpace = method.text() + " ";

      map[index] = nameAndSpace.getBytes(StandardCharsets.UTF_8);
    }

    STD_METHOD_BYTES = map;
  }

  private void parseMethod0(Method candidate) throws IOException {
    int index;
    index = candidate.index();

    byte[] candidateBytes;
    candidateBytes = STD_METHOD_BYTES[index];

    if (matches(candidateBytes)) {
      method = candidate;
    }
  }

  private void parseMethodP() throws IOException {
    // method starts with a P. It might be:
    // - POST
    // - PUT
    // - PATCH

    // we'll try them in sequence

    parseMethod0(Method.POST);

    if (method != null) {
      return;
    }

    parseMethod0(Method.PUT);

    if (method != null) {
      return;
    }

    parseMethod0(Method.PATCH);

    if (method != null) {
      return;
    }
  }

  private int parsePathStart() throws IOException {
    // we will check if the request target starts with a '/' char

    int targetStart;
    targetStart = bufferIndex;

    if (bufferIndex >= lineLimit) {
      // reached EOL -> bad request
      parseStatus = ParseStatus.INVALID_TARGET;

      return 0;
    }

    byte b;
    b = buffer[bufferIndex++];

    if (b != Bytes.SOLIDUS) {
      // first char IS NOT '/' => BAD_REQUEST
      parseStatus = ParseStatus.INVALID_TARGET;

      return 0;
    }

    // mark request path start

    return targetStart;
  }

  private void parsePathRest(int startIndex) throws IOException {
    // we will look for the first:
    // - ? char
    // - SP char
    int index;
    index = indexOf(Bytes.QUESTION_MARK, Bytes.SP);

    if (index < 0) {
      // trailing char was not found
      parseStatus = ParseStatus.URI_TOO_LONG;

      return;
    }

    if (path == null) {
      path = new WayUriPath();
    }

    String rawValue;
    rawValue = bufferToString(startIndex, index);

    path.set(rawValue);

    // we'll continue at the '?' or SP char
    bufferIndex = index;

    byte b;
    b = buffer[bufferIndex++];

    if (b == Bytes.QUESTION_MARK) {
      parseQuery();
    }
  }

  private void parseQuery() {
    int startIndex;
    startIndex = bufferIndex;

    int end;
    end = indexOf(Bytes.SP);

    if (end < 0) {
      // trailing char was not found
      parseStatus = ParseStatus.URI_TOO_LONG;

      return;
    }

    String rawValue;
    rawValue = bufferToString(startIndex, end);

    WayUriQuery q;

    if (query == EmptyUriQuery.INSTANCE) {
      query = q = new WayUriQuery();
    } else {
      q = (WayUriQuery) query;
    }

    q.set(rawValue);

    // we'll continue immediately after the SP
    bufferIndex = end + 1;
  }

  static final byte[] HTTP_VERSION_PREFIX = {'H', 'T', 'T', 'P', '/'};

  private void parseVersion() {
    // 'H' 'T' 'T' 'P' '/' '1' '.' '1' = 8 bytes

    if (!matches(HTTP_VERSION_PREFIX)) {
      // buffer does not start with 'HTTP/'
      parseStatus = ParseStatus.INVALID_PROTOCOL;

      return;
    }

    // check if we  have '1' '.' '1' = 3 bytes

    int requiredIndex;
    requiredIndex = bufferIndex + 3 - 1;

    if (requiredIndex >= lineLimit) {
      parseStatus = ParseStatus.INVALID_PROTOCOL;

      return;
    }

    byte maybeMajor;
    maybeMajor = buffer[bufferIndex++];

    if (!Bytes.isDigit(maybeMajor)) {
      // major version is not a digit => bad request
      parseStatus = ParseStatus.INVALID_PROTOCOL;

      return;
    }

    byte maybeDot;
    maybeDot = buffer[bufferIndex++];

    if (maybeDot != '.') {
      // major version not followed by a DOT => bad request
      parseStatus = ParseStatus.INVALID_PROTOCOL;

      return;
    }

    byte maybeMinor;
    maybeMinor = buffer[bufferIndex++];

    if (!Bytes.isDigit(maybeMinor)) {
      // minor version is not a digit => bad request
      parseStatus = ParseStatus.INVALID_PROTOCOL;

      return;
    }

    versionMajor = (byte) (maybeMajor - 0x30);

    versionMinor = (byte) (maybeMinor - 0x30);
  }

}