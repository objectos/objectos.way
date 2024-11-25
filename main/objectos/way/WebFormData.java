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
package objectos.way;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

final class WebFormData implements Web.FormData {

  private final Map<String, Object> map;

  private WebFormData(Map<String, Object> map) {
    this.map = map;
  }

  static WebFormData parse(Http.RequestBody body) throws UncheckedIOException {
    try (InputStream in = body.bodyInputStream()) {
      return parse0(in);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  static WebFormData parse(Http.Exchange http) throws Http.UnsupportedMediaTypeException, UncheckedIOException {
    String contentType;
    contentType = http.header(Http.HeaderName.CONTENT_TYPE);

    if (!contentType.equals("application/x-www-form-urlencoded")) {
      throw new Http.UnsupportedMediaTypeException(contentType, "application/x-www-form-urlencoded");
    }

    Http.RequestBody body;
    body = http;

    return parse(body);
  }

  private static WebFormData parse0(InputStream in) throws IOException {
    Map<String, Object> map;
    map = Util.createSequencedMap();

    ByteArrayOutputStream bytes;
    bytes = new ByteArrayOutputStream();

    String key;
    key = null;

    int c;

    while ((c = in.read()) != -1) {
      switch (c) {
        case '=' -> {
          if (key != null) {
            Http.queryParamsAdd(map, Function.identity(), key, "");
          }

          key = bytes.toString();

          bytes.reset();
        }

        case '+' -> bytes.write(' ');

        case '&' -> {
          if (key == null) {
            throw new UnsupportedOperationException("Implement me");
          }

          String value;
          value = bytes.toString();

          bytes.reset();

          Http.queryParamsAdd(map, Function.identity(), key, value);

          key = null;
        }

        case '%' -> {
          c = in.read();

          if (c == -1) {
            throw new IllegalArgumentException(
                "Invalid % escape sequence: no data"
            );
          }

          int high;
          high = Http.parseHexDigit(c);

          c = in.read();

          if (c == -1) {
            throw new IllegalArgumentException(
                "Invalid % escape sequence: not enough data"
            );
          }

          int low;
          low = Http.parseHexDigit(c);

          int value;
          value = high << 4 | low;

          bytes.write(value);
        }

        default -> bytes.write(c);
      }
    }

    String value;
    value = bytes.toString();

    if (key != null) {
      Http.queryParamsAdd(map, Function.identity(), key, value);
    }

    return new WebFormData(
        Util.toUnmodifiableMap(map)
    );
  }

  @Override
  public final Set<String> names() {
    return map.keySet();
  }

  @Override
  public final String get(String key) {
    return Http.queryParamsGet(map, key);
  }

  @Override
  public final List<String> getAll(String name) {
    return Http.queryParamsGetAll(map, name);
  }

  @Override
  public final String getOrDefault(String key, String defaultValue) {
    String maybe;
    maybe = get(key);

    return maybe != null ? maybe : defaultValue;
  }

  @Override
  public final int size() {
    return map.size();
  }

}