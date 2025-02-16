/*
 * Copyright (C) 2025 Objectos Software LTDA.
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

class HttpHeader {

  final Http.HeaderName name;

  final HttpExchange input;

  final int start;

  final int end;

  String value;

  public HttpHeader(Http.HeaderName name, HttpExchange input, int start, int end) {
    this.name = name;

    this.input = input;

    this.start = start;

    this.end = end;
  }

  public final HttpHeader add(int startIndex, int endIndex) {
    throw new UnsupportedOperationException("Implement me");
  }

  public final boolean contentEquals(byte[] that) {
    int thisLength;
    thisLength = end - start;

    if (thisLength != that.length) {
      return false;
    }

    for (int offset = 0; offset < thisLength; offset++) {
      byte ch;
      ch = input.get(start + offset);

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
      value = input.bufferToString(start, end);
    }

    return value;
  }

  @Override
  public final String toString() {
    return name.capitalized() + ": " + get();
  }

  public final long unsignedLongValue() {
    int thisLength;
    thisLength = end - start;

    if (thisLength > 19) {
      // larger than max long positive value

      return Long.MIN_VALUE;
    }

    long result;
    result = 0;

    for (int i = start; i < end; i++) {
      byte d;
      d = input.get(i);

      if (!Http.isDigit(d)) {
        return Long.MIN_VALUE;
      }

      result *= 10;

      long l;
      l = (long) d & 0xF;

      result += l;
    }

    if (result < 0) {
      return Long.MIN_VALUE;
    }

    return result;
  }

}