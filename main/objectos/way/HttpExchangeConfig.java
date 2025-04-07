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

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

final class HttpExchangeConfig implements Http.Exchange.Config {

  Clock clock;

  Map<Object, Object> objectStore;

  Http.Method method = Http.Method.GET;

  String path = "/";

  Map<String, Object> queryParams;

  Http.Version version = Http.Version.HTTP_1_1;

  Map<Http.HeaderName, Object> headers;

  Map<String, Object> formParams;

  byte[] body;

  public final Http.Exchange build() {
    if (formParams == null) {
      body = Util.EMPTY_BYTE_ARRAY;
    } else {

      if (headers == null) {
        headers = Util.createSequencedMap();
      }

      if (!headers.containsKey(Http.HeaderName.CONTENT_TYPE)) {
        headers.put(Http.HeaderName.CONTENT_TYPE, "application/x-www-form-urlencoded");
      }

      String body;
      body = Http.queryParamsToString(formParams, this::encode);

      this.body = body.getBytes(StandardCharsets.UTF_8);

    }

    //return new HttpExchange(this);
    throw new UnsupportedOperationException("Implement me");
  }

  private String encode(String s) {
    return URLEncoder.encode(s, StandardCharsets.UTF_8);
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
    Objects.requireNonNull(key, "key == null");
    Objects.requireNonNull(value, "value == null");

    if (objectStore == null) {
      objectStore = Util.createMap();
    }

    objectStore.put(key, value);
  }

}