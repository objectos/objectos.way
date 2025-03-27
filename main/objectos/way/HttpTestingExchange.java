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
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SequencedMap;
import java.util.Set;
import java.util.function.Consumer;
import objectos.way.Http.ResponseHeaders;
import objectos.way.Http.Status;
import objectos.way.Lang.MediaObject;
import objectos.way.Lang.MediaWriter;

final class HttpTestingExchange extends HttpSupport implements Http.TestingExchange {

  private Map<Object, Object> objectStore;

  private final Http.Method method;

  private final String path;

  private final Map<String, Object> queryParams;

  private final Map<Http.HeaderName, Object> headers;

  private final byte[] body;

  private Object responseBody;

  private Charset responseCharset;

  private Map<Http.HeaderName, Object> responseHeaders;

  private Http.Status responseStatus;

  HttpTestingExchange(HttpTestingExchangeConfig config) {
    clock = config.clock;

    objectStore = config.objectStore;

    method = config.method;

    path = config.path;

    queryParams = config.queryParams;

    headers = config.headers;

    body = config.body;
  }

  // testing methods

  @Override
  public final Http.Status responseStatus() {
    return responseStatus;
  }

  @Override
  public final String responseHeader(Http.HeaderName name) {
    Objects.requireNonNull(name, "name == null");

    if (responseHeaders == null) {
      return null;
    } else {
      return Http.queryParamsGet(responseHeaders, name);
    }
  }

  @Override
  public final Object responseBody() {
    return responseBody;
  }

  @Override
  public final Charset responseCharset() {
    return responseCharset;
  }

  // request methods

  @SuppressWarnings("unchecked")
  @Override
  public final String header(Http.HeaderName name) {
    Objects.requireNonNull(name, "name == null");

    if (headers == null) {
      return null;
    }

    Object value;
    value = headers.get(name);

    return switch (value) {
      case null -> null;

      case String s -> s;

      case List<?> l -> {
        List<String> list = (List<String>) l;

        yield list.get(0);
      }

      default -> throw new AssertionError(
          "Type should not have been put into the map: " + value.getClass()
      );
    };
  }

  @SuppressWarnings("unchecked")
  @Override
  public final String toRequestHeadersText() {
    final StringBuilder sb;
    sb = new StringBuilder();

    if (headers != null) {

      for (var entry : headers.entrySet()) {

        final Http.HeaderName name;
        name = entry.getKey();

        final Object value;
        value = entry.getValue();

        switch (value) {
          case String s -> {
            sb.append(name.capitalized());
            sb.append(": ");
            sb.append(s);
            sb.append("\n");
          }

          case List<?> l -> {
            List<String> list = (List<String>) l;

            for (var s : list) {
              sb.append(name.capitalized());
              sb.append(": ");
              sb.append(s);
              sb.append("\n");
            }
          }

          default -> throw new AssertionError(
              "Type should not have been put into the map: " + value.getClass()
          );
        }

      }

    }

    return sb.toString();
  }

  @Override
  public final Http.Method method() {
    Check.state(method != null, "method was not set");

    return method;
  }

  @Override
  public final String path() {
    Check.state(path != null, "path was not set");

    return path;
  }

  @Override
  public final String queryParam(String name) {
    if (queryParams == null) {
      return null;
    } else {
      return Http.queryParamsGet(queryParams, name);
    }
  }

  @Override
  public final List<String> queryParamAll(String name) {
    if (queryParams == null) {
      return List.of();
    } else {
      return Http.queryParamsGetAll(queryParams, name);
    }
  }

  @Override
  public final Set<String> queryParamNames() {
    if (queryParams == null) {
      return Set.of();
    } else {
      return queryParams.keySet();
    }
  }

  @Override
  public final String rawPath() {
    StringBuilder builder;
    builder = new StringBuilder();

    String path;
    path = path();

    int slashFromIndex;
    slashFromIndex = 0;

    while (true) {
      if (slashFromIndex == path.length()) {
        break;
      }

      int slash;
      slash = path.indexOf('/', slashFromIndex);

      if (slash == slashFromIndex) {
        builder.append('/');

        slashFromIndex++;

        continue;
      }

      if (slash < 0) {
        int slugStart;
        slugStart = slashFromIndex;

        int slugEnd;
        slugEnd = path.length();

        String slug;
        slug = path.substring(slugStart, slugEnd);

        String encoded;
        encoded = encode(slug);

        builder.append(encoded);

        break;
      }

      int slugStart;
      slugStart = slashFromIndex;

      int slugEnd;
      slugEnd = slash;

      String slug;
      slug = path.substring(slugStart, slugEnd);

      String encoded;
      encoded = encode(slug);

      builder.append(encoded);

      builder.append('/');

      slashFromIndex = slugEnd + 1;
    }

    return builder.toString();
  }

  @Override
  public final String rawQuery() {
    if (queryParams == null) {
      return null;
    } else {
      return Http.queryParamsToString(queryParams, this::encode);
    }
  }

  @Override
  public final String rawQueryWith(String name, String value) {
    if (name.isBlank()) {
      throw new IllegalArgumentException("name must not be blank");
    }

    Objects.requireNonNull(value, "value == null");

    Map<String, Object> params;

    if (queryParams == null) {
      params = Map.of(name, value);
    } else {
      SequencedMap<String, Object> copy;
      copy = Util.createSequencedMap();

      copy.putAll(queryParams);

      copy.put(name, value);

      params = copy;
    }

    return Http.queryParamsToString(params, this::encode);
  }

  @Override
  public final InputStream bodyInputStream() throws IOException {
    Check.state(body != null, "body was not set");

    return new ByteArrayInputStream(body);
  }

  private String encode(String s) {
    return URLEncoder.encode(s, StandardCharsets.UTF_8);
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

  @SuppressWarnings("unchecked")
  @Override
  public final <T> T get(Class<T> key) {
    if (objectStore == null) {
      return null;
    } else {
      return (T) objectStore.get(key);
    }
  }

  // response methods

  @Override
  public final void respond(Http.Status status, Lang.MediaObject object) {
    respond0(status, object);

    responseBody = object;
  }

  @Override
  public final void respond(Status status, MediaObject object, Consumer<ResponseHeaders> headers) {
    respond0(status, object);

    headers.accept(this);

    responseBody = object;
  }

  private void respond0(Http.Status status, Lang.MediaObject object) {
    status0(status);

    String contentType;
    contentType = object.contentType();

    byte[] bytes;
    bytes = object.mediaBytes();

    dateNow();

    header0(Http.HeaderName.CONTENT_TYPE, contentType);

    header0(Http.HeaderName.CONTENT_LENGTH, bytes.length);
  }

  @Override
  public final void respond(Status status, MediaWriter writer) {
    respond0(status, writer);

    responseBody = writer;
  }

  @Override
  public final void respond(Status status, MediaWriter writer, Consumer<ResponseHeaders> headers) {
    respond0(status, writer);

    headers.accept(this);

    responseBody = writer;
  }

  private void respond0(Status status, MediaWriter writer) {
    status0(status);

    String contentType;
    contentType = writer.contentType();

    dateNow();

    header0(Http.HeaderName.CONTENT_TYPE, contentType);

    header0(Http.HeaderName.TRANSFER_ENCODING, "chunked");
  }

  @Override
  final void status0(Http.Status value) {
    responseStatus = value;
  }

  @Override
  final void header0(Http.HeaderName name, String value) {
    if (responseHeaders == null) {
      responseHeaders = Util.createSequencedMap();
    }

    Object previous;
    previous = responseHeaders.put(name, value);

    if (previous instanceof List<?> list) {
      @SuppressWarnings("unchecked")
      List<String> ofString = (List<String>) list;

      ofString.add(value);

      responseHeaders.put(name, ofString);
    } else if (previous instanceof String s) {
      List<String> list = Util.createList();

      list.add(s);

      list.add(value);

      responseHeaders.put(name, list);
    }
  }

  @Override
  final void send0() {
    responseBody = null;
  }

  @Override
  final void send0(byte[] body) {
    responseBody = body;
  }

  @Override
  final void send0(Path file) {
    responseBody = file;
  }

  @Override
  final void endResponse() {}

  //

  @Override
  public final boolean processed() {
    return responseStatus != null;
  }

}