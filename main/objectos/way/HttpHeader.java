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

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

final class HttpHeader {

  private enum Kind {

    SINGLE,

    MANY,

    STRING_SINGLE,

    STRING_MANY;

  }

  private record Range(int startIndex, int endIndex) {
    final int length() {
      return endIndex - startIndex;
    }

    final String toString(byte[] data) {
      return new String(data, startIndex, length(), StandardCharsets.US_ASCII);
    }
  }

  private Kind kind;

  private Object value;

  private HttpHeader(Kind kind, Object value) {
    this.kind = kind;
    this.value = value;
  }

  private static final byte[] TABLE;

  static final byte INVALID = 0;

  static final byte VALID = 1;

  static final byte WS = 2;

  static final byte CR = 3;

  static final byte LF = 4;

  static {
    final byte[] table;
    table = new byte[128];

    for (int b = 0x21; b < 0x7F; b++) {
      // VCHAR are valid
      table[b] = VALID;
    }

    // valid under certain circustances
    table[' '] = WS;

    table['\t'] = WS;

    table['\r'] = CR;

    table['\n'] = LF;

    TABLE = table;
  }

  public static HttpHeader create(int startIndex, int endIndex) {
    final Range range;
    range = new Range(startIndex, endIndex);

    return new HttpHeader(Kind.SINGLE, range);
  }

  public static HttpHeader createIfValid(byte[] buffer, int startIndex, int endIndex) {
    if (startIndex > endIndex) {
      return null;
    }

    boolean ws = false;

    for (int idx = startIndex; idx < endIndex; idx++) {
      final byte b;
      b = buffer[idx];

      if (b < 0) {
        // reject non-ascii
        return null;
      }

      if (b > 0x7F) {
        // reject non-ascii
        return null;
      }

      final byte flag;
      flag = TABLE[b];

      switch (flag) {
        case 0 -> {
          if (idx == startIndex) {
            // reject WS at the start
            return null;
          }

          ws = true;
        }

        case 1 -> ws = false;

        default -> {
          return null;
        }
      }
    }

    if (ws) {
      // reject WS at the end
      return null;
    }

    final Range range;
    range = new Range(startIndex, endIndex);

    return new HttpHeader(Kind.SINGLE, range);
  }

  public final void add(HttpHeader header) {
    assert header.kind == Kind.SINGLE;

    final Range other;
    other = (Range) header.value;

    switch (kind) {
      case SINGLE -> {
        final List<Range> list;
        list = Util.createList();

        final Range existing;
        existing = (Range) value;

        list.add(existing);

        list.add(other);

        kind = Kind.MANY;

        value = list;
      }

      case MANY -> {
        @SuppressWarnings("unchecked")
        final List<Range> list = (List<Range>) value;

        list.add(other);
      }

      default -> throw new IllegalStateException("Cannot invoke add(HttpHeader) after the value has been consumed");
    }
  }

  public final String get(byte[] data) {
    return switch (kind) {
      case SINGLE -> {
        final Range range;
        range = (Range) value;

        final String s;
        s = range.toString(data);

        kind = Kind.STRING_SINGLE;

        value = s;

        yield s;
      }

      case MANY -> {
        final List<?> source;
        source = (List<?>) value;

        final List<String> result;
        result = new ArrayList<>(source.size());

        for (Object o : source) {
          final Range range;
          range = (Range) o;

          final String s;
          s = range.toString(data);

          result.add(s);
        }

        kind = Kind.STRING_MANY;

        value = result;

        yield result.get(0);
      }

      case STRING_SINGLE -> (String) value;

      case STRING_MANY -> {
        final List<?> list;
        list = (List<?>) value;

        final Object first;
        first = list.get(0);

        yield (String) first;
      }
    };
  }

  public final long unsignedLongValue(byte[] data) {
    return switch (kind) {
      case SINGLE -> {
        final Range range;
        range = (Range) value;

        yield unsignedLongValue(data, range);
      }

      case MANY -> {
        final List<?> list;
        list = (List<?>) value;

        final Object first;
        first = list.get(0);

        final Range range;
        range = (Range) first;

        yield unsignedLongValue(data, range);
      }

      case STRING_SINGLE -> {
        final String s;
        s = (String) value;

        yield unsignedLongValue(s);
      }

      case STRING_MANY -> {
        final List<?> list;
        list = (List<?>) value;

        final Object first;
        first = list.get(0);

        final String s;
        s = (String) first;

        yield unsignedLongValue(s);
      }
    };
  }

  private long unsignedLongValue(byte[] data, Range range) {
    final int length;
    length = range.length();

    if (length > 19) {
      // larger than max long positive value

      return Long.MIN_VALUE;
    }

    final int start;
    start = range.startIndex;

    final int end;
    end = range.endIndex;

    long result;
    result = 0;

    for (int i = start; i < end; i++) {
      byte d;
      d = data[i];

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

  private long unsignedLongValue(String s) {
    try {
      return Long.parseLong(s);
    } catch (NumberFormatException e) {
      return Long.MIN_VALUE;
    }
  }

}