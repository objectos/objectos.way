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
import objectox.http.HttpStatus;

public final class ObjectoxRequestLine {

  private final SocketInput input;

  ObjectoxMethod method;

  HttpRequestPath path;

  ObjectoxUriQuery query;

  HttpStatus status;

  byte versionMajor;

  byte versionMinor;

  ObjectoxRequestLine(SocketInput input) {
    this.input = input;
  }

  public final void parse() throws IOException {
    input.parseLine();

    parseMethod();

    if (method == null) {
      // parse method failed -> bad request
      status = HttpStatus.BAD_REQUEST;

      return;
    }

    parsePathStart();

    if (path == null) {
      // parse path start failed -> bad request
      status = HttpStatus.BAD_REQUEST;

      return;
    }

    parsePathRest();

    if (status != null) {
      // bad request -> fail
      return;
    }

    if (query != null) {
      throw new UnsupportedOperationException("Implement me");
    }

    parseVersion();

    if (status != null) {
      // bad request -> fail
      return;
    }

    if (!input.endOfLine()) {
      status = HttpStatus.BAD_REQUEST;

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
      case 'C' -> parseMethod0(ObjectoxMethod.CONNECT);

      case 'D' -> parseMethod0(ObjectoxMethod.DELETE);

      case 'G' -> parseMethod0(ObjectoxMethod.GET);

      case 'H' -> parseMethod0(ObjectoxMethod.HEAD);

      case 'O' -> parseMethod0(ObjectoxMethod.OPTIONS);

      case 'P' -> parseMethodP();

      case 'T' -> parseMethod0(ObjectoxMethod.TRACE);
    }
  }

  private void parseMethod0(ObjectoxMethod candidate) throws IOException {
    byte[] candidateBytes;
    candidateBytes = candidate.nameAndSpace;

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

    parseMethod0(ObjectoxMethod.POST);

    if (method != null) {
      return;
    }

    parseMethod0(ObjectoxMethod.PUT);

    if (method != null) {
      return;
    }

    parseMethod0(ObjectoxMethod.PATCH);

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
      return;
    }

    byte b;
    b = input.next();

    if (b != Bytes.SOLIDUS) {
      // first char IS NOT '/' => BAD_REQUEST
      return;
    }

    // mark request path start

    path = input.createPath(targetStart);

    path.slash(targetStart);
  }

  private void parsePathRest() throws IOException {
    // we will look for the first:
    // - ? char
    // - SP char
    int index;
    index = input.indexOf(Bytes.QUESTION_MARK, Bytes.SP);

    if (index < 0) {
      // trailing char was not found
      status = HttpStatus.BAD_REQUEST;

      return;
    }

    path.end(index);

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
      status = HttpStatus.BAD_REQUEST;

      return;
    }

    // check if we  have '1' '.' '1' = 3 bytes

    if (!input.hasNext(3)) {
      status = HttpStatus.BAD_REQUEST;

      return;
    }

    byte maybeMajor;
    maybeMajor = input.next();

    if (!Bytes.isDigit(maybeMajor)) {
      // major version is not a digit => bad request
      status = HttpStatus.BAD_REQUEST;

      return;
    }

    byte maybeDot;
    maybeDot = input.next();

    if (maybeDot != '.') {
      // major version not followed by a DOT => bad request
      status = HttpStatus.BAD_REQUEST;

      return;
    }

    byte maybeMinor;
    maybeMinor = input.next();

    if (!Bytes.isDigit(maybeMinor)) {
      // minor version is not a digit => bad request
      status = HttpStatus.BAD_REQUEST;

      return;
    }

    versionMajor = (byte) (maybeMajor - 0x30);

    versionMinor = (byte) (maybeMinor - 0x30);
  }

}