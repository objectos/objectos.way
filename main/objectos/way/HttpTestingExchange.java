/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import objectos.way.Http.HeaderName;

final class HttpTestingExchange implements Http.TestingExchange {

  private final Clock clock;

  private Map<Object, Object> objectStore;

  private final Http.Method method;

  private final String path;

  private final Map<String, String> queryParams;

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
  }

  // testing methods

  @Override
  public final Http.Status responseStatus() {
    return responseStatus;
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
  public final String pathParam(String name) {
    if (objectStore == null) {
      return null;
    } else {
      return (String) objectStore.get(name);
    }
  }

  @Override
  public final String queryParam(String name) {
    if (queryParams == null) {
      return null;
    } else {
      return queryParams.get(name);
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
    return URLEncoder.encode(path(), StandardCharsets.UTF_8);
  }

  @Override
  public final String rawQuery() {
    throw new IllegalStateException("query was not set");
  }

  @Override
  public final String header(HeaderName name) {
    throw new UnsupportedOperationException();
  }

  @Override
  public final InputStream bodyInputStream() throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public final <T> void set(Class<T> key, T value) {
    Check.notNull(key, "key == null");
    Check.notNull(value, "value == null");

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
  public final void header(Http.HeaderName name, long value) {
    header(name, Long.toString(value));
  }

  @Override
  public final void header(Http.HeaderName name, String value) {
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
  public final void dateNow() {
    Clock theClock;
    theClock = clock;

    if (theClock == null) {
      theClock = Clock.systemUTC();
    }

    ZonedDateTime now;
    now = ZonedDateTime.now(theClock);

    String value;
    value = Http.formatDate(now);

    header(Http.HeaderName.DATE, value);
  }

  @Override
  public final void send() {
    responseBody = null;
  }

  @Override
  public final void send(byte[] body) {
    responseBody = Objects.requireNonNull(body, "body == null");
  }

  @Override
  public final void send(Lang.CharWritable body, Charset charset) {
    responseBody = Objects.requireNonNull(body, "body == null");

    responseCharset = Objects.requireNonNull(charset, "charset == null");
  }

  @Override
  public final void send(Path file) {
    responseBody = Objects.requireNonNull(file, "file == null");
  }

  @Override
  public final void notFound() {
    status(Http.Status.NOT_FOUND);

    dateNow();

    header(Http.HeaderName.CONNECTION, "close");

    send();
  }

  @Override
  public final void methodNotAllowed() {
    status(Http.Status.METHOD_NOT_ALLOWED);

    dateNow();

    header(Http.HeaderName.CONNECTION, "close");

    send();
  }

  @Override
  public final void internalServerError(Throwable t) {
    StringWriter sw;
    sw = new StringWriter();

    PrintWriter pw;
    pw = new PrintWriter(sw);

    t.printStackTrace(pw);

    String msg;
    msg = sw.toString();

    byte[] bytes;
    bytes = msg.getBytes();

    status(Http.Status.INTERNAL_SERVER_ERROR);

    dateNow();

    header(Http.HeaderName.CONTENT_LENGTH, bytes.length);

    header(Http.HeaderName.CONTENT_TYPE, "text/plain");

    header(Http.HeaderName.CONNECTION, "close");

    send(bytes);
  }

  @Override
  public final boolean processed() {
    return responseStatus != null;
  }

  @Override
  public final void status(Http.Status value) {
    responseStatus = Objects.requireNonNull(value, "value == null");
  }

}