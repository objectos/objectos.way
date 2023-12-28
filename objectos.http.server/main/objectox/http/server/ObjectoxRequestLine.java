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

public final class ObjectoxRequestLine {

  private final Input input;

  ObjectoxMethod method;

  HttpRequestPath path;

  ObjectoxRequestLine(Input input) {
    this.input = input;
  }

  public final boolean parse() throws IOException {
    return parseMethod()
        && parseTargetStart();
  }

  private boolean parseMethod() throws IOException {
    if (!input.hasNext()) {
      // input is empty -> bad request
      return false;
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

    return method != null;
  }

  private void parseMethod0(ObjectoxMethod candidate) throws IOException {
    byte[] candidateBytes;
    candidateBytes = candidate.nameAndSpace;

    if (!input.test(candidateBytes)) {
      return;
    }

    int toSkip;
    toSkip = candidateBytes.length;

    input.skip(toSkip);

    method = candidate;
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

  private boolean parseTargetStart() throws IOException {
    // we will check if the request target starts with a '/' char

    int targetStart;
    targetStart = input.index();

    if (!input.hasNext()) {
      // no more input -> bad request
      return false;
    }

    byte b;
    b = input.next();

    if (b != Bytes.SOLIDUS) {
      // first char IS NOT '/' => BAD_REQUEST
      return false;
    }

    // mark request path start

    path = input.createPath();

    path.slash(targetStart);

    return true;
  }

}