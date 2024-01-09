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
import objectos.http.Method;
import objectox.http.ObjectoxMethod;

public final class ObjectoxRequestLine {

  private final SocketInput input;

  BadRequestReason badRequest;

  Method method;

  ObjectoxUriPath path;

  ObjectoxUriQuery query;

  int startIndex;

  byte versionMajor;

  byte versionMinor;

  ObjectoxRequestLine(SocketInput input) {
    this.input = input;
  }

  public final void reset() {
    badRequest = null;

    method = null;

    path.reset();

    startIndex = 0;

    query = null;

    versionMajor = versionMinor = 0;
  }

  public final void parse() throws IOException {
    input.parseLine();

    parseMethod();

    if (method == null) {
      // parse method failed -> bad request
      badRequest = BadRequestReason.INVALID_METHOD;

      return;
    }

    parsePathStart();

    if (badRequest != null) {
      // bad request -> fail
      return;
    }

    parsePathRest();

    if (badRequest != null) {
      // bad request -> fail
      return;
    }

    if (query != null) {
      throw new UnsupportedOperationException("Implement me");
    }

    parseVersion();

    if (badRequest != null) {
      // bad request -> fail
      return;
    }

    if (!input.consumeIfEndOfLine()) {
      badRequest = BadRequestReason.INVALID_REQUEST_LINE_TERMINATOR;

      return;
    }
  }

  private void parseMethod() throws IOException {
    if (!input.hasNext()) {
      return;
    }

    byte first;
    first = input.peek();

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
    size = ObjectoxMethod.size();

    byte[][] map;
    map = new byte[size][];

    for (int index = 0; index < size; index++) {
      ObjectoxMethod method;
      method = ObjectoxMethod.get(index);

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

    if (input.matches(candidateBytes)) {
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

  private void parsePathStart() throws IOException {
    // we will check if the request target starts with a '/' char

    int targetStart;
    targetStart = input.index();

    if (!input.hasNext()) {
      // reached EOL -> bad request
      badRequest = BadRequestReason.INVALID_TARGET;

      return;
    }

    byte b;
    b = input.next();

    if (b != Bytes.SOLIDUS) {
      // first char IS NOT '/' => BAD_REQUEST
      badRequest = BadRequestReason.INVALID_TARGET;

      return;
    }

    // mark request path start

    startIndex = targetStart;
  }

  private void parsePathRest() throws IOException {
    // we will look for the first:
    // - ? char
    // - SP char
    int index;
    index = input.indexOf(Bytes.QUESTION_MARK, Bytes.SP);

    if (index < 0) {
      // trailing char was not found
      badRequest = BadRequestReason.URI_TOO_LONG;

      return;
    }

    if (path == null) {
      path = new ObjectoxUriPath();
    }

    String rawValue;
    rawValue = input.getString(startIndex, index);

    path.set(rawValue);

    byte b;
    b = input.setAndNext(index);

    if (b == Bytes.QUESTION_MARK) {
      throw new UnsupportedOperationException("Implement me");
    }
  }

  static final byte[] HTTP_VERSION_PREFIX = {'H', 'T', 'T', 'P', '/'};

  private void parseVersion() {
    // 'H' 'T' 'T' 'P' '/' '1' '.' '1' = 8 bytes

    if (!input.matches(HTTP_VERSION_PREFIX)) {
      // buffer does not start with 'HTTP/'
      badRequest = BadRequestReason.INVALID_PROTOCOL;

      return;
    }

    // check if we  have '1' '.' '1' = 3 bytes

    if (!input.hasNext(3)) {
      badRequest = BadRequestReason.INVALID_PROTOCOL;

      return;
    }

    byte maybeMajor;
    maybeMajor = input.next();

    if (!Bytes.isDigit(maybeMajor)) {
      // major version is not a digit => bad request
      badRequest = BadRequestReason.INVALID_PROTOCOL;

      return;
    }

    byte maybeDot;
    maybeDot = input.next();

    if (maybeDot != '.') {
      // major version not followed by a DOT => bad request
      badRequest = BadRequestReason.INVALID_PROTOCOL;

      return;
    }

    byte maybeMinor;
    maybeMinor = input.next();

    if (!Bytes.isDigit(maybeMinor)) {
      // minor version is not a digit => bad request
      badRequest = BadRequestReason.INVALID_PROTOCOL;

      return;
    }

    versionMajor = (byte) (maybeMajor - 0x30);

    versionMinor = (byte) (maybeMinor - 0x30);
  }

}