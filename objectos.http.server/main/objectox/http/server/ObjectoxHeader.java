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

import objectos.http.HeaderName;

class ObjectoxHeader {

  final SocketInput input;

  final HeaderName name;

  String value;

  int valueStart;

  int valueEnd;

  public ObjectoxHeader(SocketInput input, HeaderName name) {
    this.input = input;

    this.name = name;
  }

  // API

  public final boolean contentEquals(byte[] that) {
    int thisLength;
    thisLength = valueEnd - valueStart;

    if (thisLength != that.length) {
      return false;
    }

    for (int offset = 0; offset < thisLength; offset++) {
      byte ch;
      ch = input.get(valueStart + offset);

      byte thisLow;
      thisLow = Bytes.toLowerCase(ch);

      byte thatLow;
      thatLow = that[offset];

      if (thisLow != thatLow) {
        return false;
      }
    }

    return true;
  }

  public final String get() {
    if (value == null) {
      value = input.getString(valueStart, valueEnd);
    }

    return value;
  }

  public final void parseValue() {
    parseStart();

    parseEnd();
  }

  public final ObjectoxHeader parseAdditionalValue() {
    throw new UnsupportedOperationException("Implement me");
  }

  final void parseStart() {
    // assume value starts @ current index
    int start;
    start = input.index();

    if (!input.hasNext()) {
      throw new UnsupportedOperationException(
          "Implement me :: empty value?"
      );
    }

    // consumes and discard a single leading OWS if present
    byte maybeOws;
    maybeOws = input.peek();

    if (Bytes.isOptionalWhitespace(maybeOws)) {
      // consume and discard leading OWS
      start += 1;

      input.set(start);
    }

    valueStart = start;
  }

  final void parseEnd() {
    int lineLimit;
    lineLimit = input.lineLimit();

    int end;
    end = lineLimit;

    byte maybeCR;
    maybeCR = input.get(end - 1);

    if (maybeCR == Bytes.CR) {
      // value ends at the CR of the line end CRLF
      end = end - 1;
    }

    byte maybeOWS;
    maybeOWS = input.get(end - 1);

    if (Bytes.isOptionalWhitespace(maybeOWS)) {
      // value ends at the trailing OWS
      end = end - 1;
    }

    if (valueStart > end) {
      // value has negative length... is it possible?
      throw new UnsupportedOperationException("Implement me");
    }

    valueEnd = end;

    input.set(lineLimit + 1);
  }

}