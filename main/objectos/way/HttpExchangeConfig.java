/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import objectos.way.HttpExchange.ParseStatus;

final class HttpExchangeConfig implements Http.Exchange.Config {

  Map<String, Object> attributes;

  final int bufferSizeInitial = 1024;

  final int bufferSizeMax = 4096;

  Clock clock = Clock.systemUTC();

  private Http.Method method = Http.Method.GET;

  private String path = "/";

  private Map<String, Object> queryParams;

  private final Http.Version version = Http.Version.HTTP_1_1;

  private Map<Http.HeaderName, Object> headers;

  private Map<String, Object> formParams;

  final Note.Sink noteSink = Note.NoOpSink.INSTANCE;

  public final Http.Exchange build() {
    try {
      final HttpExchange http;
      http = new HttpExchange(this);

      final ParseStatus status;
      status = http.parse();

      if (status != ParseStatus.NORMAL) {
        throw new IllegalArgumentException("Invalid request");
      }

      return http;
    } catch (IOException e) {
      throw new AssertionError("ByteArrayInputStream does not throw IOException", e);
    }
  }

  @Override
  public final void clock(Clock value) {
    clock = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void method(Http.Method value) {
    method = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void formParam(String name, int value) {
    Objects.requireNonNull(name, "name == null");

    formParam0(name, Integer.toString(value));
  }

  @Override
  public final void formParam(String name, long value) {
    Objects.requireNonNull(name, "name == null");

    formParam0(name, Long.toString(value));
  }

  @Override
  public final void formParam(String name, String value) {
    Objects.requireNonNull(name, "name == null");
    Objects.requireNonNull(value, "value == null");

    formParam0(name, value);
  }

  private void formParam0(String name, String value) {
    if (formParams == null) {
      formParams = Util.createSequencedMap();
    }

    Http.queryParamsAdd(formParams, Function.identity(), name, value);
  }

  @SuppressWarnings("unchecked")
  @Override
  public final void header(Http.HeaderName name, String value) {
    Objects.requireNonNull(name, "name == null");
    Objects.requireNonNull(value, "value == null");

    if (headers == null) {
      headers = Util.createSequencedMap();
    }

    Object previous;
    previous = headers.put(name, value);

    switch (previous) {
      case null -> {}

      case String s -> {
        List<String> list = Util.createList();

        list.add(s);

        list.add(value);

        headers.put(name, list);
      }

      case List<?> l -> {
        List<String> list = (List<String>) l;

        list.add(value);

        headers.put(name, list);
      }

      default -> throw new AssertionError(
          "Type should not have been put into the map: " + previous.getClass()
      );
    }
  }

  @Override
  public final void path(String value) {
    int length;
    length = value.length();

    if (length == 0) {
      throw new IllegalArgumentException("path must not be empty");
    }

    char first;
    first = value.charAt(0);

    if (first != '/') {
      throw new IllegalArgumentException("path must start with a '/' character");
    }

    path = value;
  }

  @Override
  public final void queryParam(String name, String value) {
    Objects.requireNonNull(name, "name == null");
    Objects.requireNonNull(value, "value == null");

    queryParam0(name, value);
  }

  @Override
  public final void queryParam(String name, int value) {
    Objects.requireNonNull(name, "name == null");

    queryParam0(name, Integer.toString(value));
  }

  @Override
  public final void queryParam(String name, long value) {
    Objects.requireNonNull(name, "name == null");

    queryParam0(name, Long.toString(value));
  }

  private void queryParam0(String name, String value) {
    if (queryParams == null) {
      queryParams = Util.createSequencedMap();
    }

    Http.queryParamsAdd(queryParams, Function.identity(), name, value);
  }

  @Override
  public final <T> void set(Class<T> key, T value) {
    final String name;
    name = key.getName();

    Objects.requireNonNull(value, "value == null");

    if (attributes == null) {
      attributes = Util.createMap();
    }

    attributes.put(name, value);
  }

  final InputStream inputStream() {
    final byte[] body;
    body = body();

    final ByteArrayOutputStream out;
    out = new ByteArrayOutputStream();

    try {
      final OutputStreamWriter w;
      w = new OutputStreamWriter(out, StandardCharsets.ISO_8859_1);

      // status line

      w.write(method.name());
      w.write(' ');
      w.write(path);

      if (queryParams != null) {
        w.write('?');

        final String query;
        query = Http.queryParamsToString(queryParams, this::encode);

        w.write(query);
      }

      w.write(' ');

      switch (version) {
        case HTTP_1_0 -> w.write("HTTP/1.0");

        case HTTP_1_1 -> w.write("HTTP/1.1");
      }

      w.write('\r');
      w.write('\n');

      // fields

      if (headers != null) {
        for (var entry : headers.entrySet()) {

          final Http.HeaderName name;
          name = entry.getKey();

          final Object value;
          value = entry.getValue();

          switch (value) {
            case String s -> {
              w.write(name.headerCase());
              w.write(": ");
              w.write(s);
              w.write('\r');
              w.write('\n');
            }

            case List<?> l -> {
              @SuppressWarnings("unchecked")
              final List<String> list = (List<String>) l;

              for (var s : list) {
                w.write(name.headerCase());
                w.write(": ");
                w.write(s);
                w.write('\r');
                w.write('\n');
              }
            }

            default -> throw new AssertionError(
                "Type should not have been put into the map: " + value.getClass()
            );
          }
        }
      }

      w.write('\r');
      w.write('\n');

      w.flush();

      out.write(body);
    } catch (IOException e) {
      throw new AssertionError("ByteArrayOutputStream should not throw", e);
    }

    final byte[] bytes;
    bytes = out.toByteArray();

    return new ByteArrayInputStream(bytes);
  }

  private byte[] body() {
    if (formParams == null) {
      return Util.EMPTY_BYTE_ARRAY;
    }

    if (headers == null) {
      headers = Util.createSequencedMap();
    }

    if (!headers.containsKey(Http.HeaderName.CONTENT_TYPE)) {
      headers.put(Http.HeaderName.CONTENT_TYPE, "application/x-www-form-urlencoded");
    }

    final String form;
    form = Http.queryParamsToString(formParams, this::encode);

    final byte[] body;
    body = form.getBytes(StandardCharsets.UTF_8);

    if (!headers.containsKey(Http.HeaderName.CONTENT_LENGTH)) {
      final String length;
      length = Integer.toString(body.length);

      headers.put(Http.HeaderName.CONTENT_LENGTH, length);
    }

    return body;
  }

  private String encode(String s) {
    return URLEncoder.encode(s, StandardCharsets.UTF_8);
  }

}