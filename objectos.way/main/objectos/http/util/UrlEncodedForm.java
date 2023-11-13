/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.http.util;

import objectos.http.server.Body;
import objectos.util.map.GrowableMap;
import objectos.util.map.UnmodifiableMap;
import objectox.http.Bytes;
import objectox.http.InBufferRequestBody;

public final class UrlEncodedForm {

  private static final String EMPTY = "";

  private final UnmodifiableMap<String, String> map;

  private UrlEncodedForm(UnmodifiableMap<String, String> map) {
    this.map = map;
  }

  public static UrlEncodedForm parse(Body body) {
    return switch (body) {
      case InBufferRequestBody buffer -> parse0(buffer);

      default -> throw new IllegalArgumentException("Unexpected value: " + body.getClass());
    };
  }

  private static UrlEncodedForm parse0(InBufferRequestBody buffer) {
    GrowableMap<String, String> map;
    map = new GrowableMap<>();

    StringBuilder sb;
    sb = new StringBuilder();

    String key;
    key = null;

    int index;
    index = buffer.start;

    while (index < buffer.end) {
      byte b;
      b = buffer.get(index++);

      int c;
      c = Bytes.toInt(b);

      switch (c) {
        case '=' -> {
          key = sb.toString();

          sb.setLength(0);

          String oldValue;
          oldValue = map.put(key, EMPTY);

          if (oldValue != null) {
            throw new UnsupportedOperationException("Implement me");
          }
        }

        case '&' -> {
          if (key == null) {
            throw new UnsupportedOperationException("Implement me");
          }

          String value;
          value = sb.toString();

          sb.setLength(0);

          String oldValue;
          oldValue = map.put(key, value);

          if (oldValue != EMPTY) {
            throw new UnsupportedOperationException("Implement me");
          }

          key = null;
        }

        case '%' -> {
          if (index >= buffer.end) {
            throw new IllegalArgumentException(
              "Invalid % escape sequence: no data"
            );
          }

          byte b0;
          b0 = buffer.get(index++);

          int high;
          high = Bytes.parseHexDigit(b0);

          if (index >= buffer.end) {
            throw new IllegalArgumentException(
              "Invalid % escape sequence: not enough data"
            );
          }

          byte b1;
          b1 = buffer.get(index++);

          int low;
          low = Bytes.parseHexDigit(b1);

          int value;
          value = high << 4 | low;

          sb.append((char) value);
        }

        default -> sb.append((char) c);
      }
    }

    String value;
    value = sb.toString();

    if (key != null) {
      map.put(key, value);
    }

    return new UrlEncodedForm(
      map.toUnmodifiableMap()
    );
  }

  public final String get(String key) {
    return map.get(key);
  }

  public final int size() {
    return map.size();
  }

}