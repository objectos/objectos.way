/*
 * Copyright (C) 2016-2026 Objectos Software LTDA.
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
package objectos.http;

import module java.base;
import module objectos.way;
import objectos.lang.Key;
import objectox.http.Version;
import objectox.http.req.RequestPojo;

final class HttpExchange0 implements HttpExchange {

  static final Key<Map<String, String>> PATH_PARAMS = Key.of("objectos.http.PathParams");

  private final RequestPojo request;

  private final HttpResponse0 response;

  private final HttpSession session;

  private final HttpStaticFilesWriter staticFilesWriter;

  HttpExchange0(RequestPojo request, HttpResponse0 response, HttpSession session, HttpStaticFilesWriter staticFilesWriter) {
    this.request = request;

    this.response = response;

    this.session = session;

    this.staticFilesWriter = staticFilesWriter;
  }

  @Override
  public final String toString() {
    if (!response.processed()) {
      return "HttpExchange[id=" + response.id() + ",method=" + request.method() + ",path=" + request.path() + "]";
    } else {
      return response.toString();
    }
  }

  // ##################################################################
  // # BEGIN: path parameters
  // ##################################################################

  @Override
  public final String pathParam(String name) {
    final Map<String, String> pathParams;
    pathParams = req(PATH_PARAMS);

    if (pathParams != null) {
      return pathParams.get(name);
    } else {
      return null;
    }
  }

  // ##################################################################
  // # END: path parameters
  // ##################################################################

  // ##################################################################
  // # BEGIN: HttpRequest
  // ##################################################################

  public final RequestPojo request() {
    return request;
  }

  @Override
  public final RequestMethod method() {
    return request.method();
  }

  @Override
  public final String header(HeaderName name) {
    return request.header(name);
  }

  @Override
  public final InputStream bodyInputStream() throws IOException {
    return request.bodyInputStream();
  }

  @Override
  public final String path() {
    return request.path();
  }

  public final Version version() {
    return request.version();
  }

  @Override
  public final Set<String> formParamNames() {
    return request.formParamNames();
  }

  @Override
  public final String formParam(String name) {
    return request.formParam(name);
  }

  @Override
  public final int formParamAsInt(String name, int defaultValue) {
    return request.formParamAsInt(name, defaultValue);
  }

  @Override
  public final String queryParam(String name) {
    return request.queryParam(name);
  }

  @Override
  public final long formParamAsLong(String name, long defaultValue) {
    return request.formParamAsLong(name, defaultValue);
  }

  @Override
  public final List<String> queryParamAll(String name) {
    return request.queryParamAll(name);
  }

  @Override
  public final int queryParamAsInt(String name, int defaultValue) {
    return request.queryParamAsInt(name, defaultValue);
  }

  @Override
  public final List<String> formParamAll(String name) {
    return request.formParamAll(name);
  }

  @Override
  public final IntStream formParamAllAsInt(String name, int defaultValue) {
    return request.formParamAllAsInt(name, defaultValue);
  }

  @Override
  public final int queryParamAsInt(String name, IntSupplier defaultSupplier) {
    return request.queryParamAsInt(name, defaultSupplier);
  }

  @Override
  public final LongStream formParamAllAsLong(String name, long defaultValue) {
    return request.formParamAllAsLong(name, defaultValue);
  }

  @Override
  public final long queryParamAsLong(String name, long defaultValue) {
    return request.queryParamAsLong(name, defaultValue);
  }

  @Override
  public final long queryParamAsLong(String name, LongSupplier defaultSupplier) {
    return request.queryParamAsLong(name, defaultSupplier);
  }

  @Override
  public final Set<String> queryParamNames() {
    return request.queryParamNames();
  }

  @Override
  public final String rawPath() {
    return request.rawPath();
  }

  @Override
  public final String rawQuery() {
    return request.rawQuery();
  }

  @Override
  public final String rawQueryWith(String name, String value) {
    return request.rawQueryWith(name, value);
  }

  // ##################################################################
  // # END: HttpRequest
  // ##################################################################

  @SuppressWarnings("unchecked")
  @Override
  public final <T> T req(Class<T> key) {
    return request.attr(key);
  }

  @SuppressWarnings("unchecked")
  @Override
  public final <T> T req(Key<T> key) {
    return request.attr(key);
  }

  @Override
  public final <T> void req(Class<T> key, T value) {
    request.attr(key, value);
  }

  @Override
  public final <T> void req(Key<T> key, T value) {
    request.attr(key, value);
  }

  // ##################################################################
  // # BEGIN: HttpSession
  // ##################################################################

  @Override
  public final <T> T session(Class<T> key) {
    return session.sessionAttr(key);
  }

  @Override
  public final <T> T session(Key<T> key) {
    return session.sessionAttr(key);
  }

  @Override
  public final <T> T session(Class<T> key, T value) {
    return session.sessionAttr(key, value);
  }

  @Override
  public final <T> T session(Key<T> key, T value) {
    return session.sessionAttr(key, value);
  }

  @Override
  public final boolean sessionAbsent() {
    return session.sessionAbsent();
  }

  @Override
  public final boolean sessionPresent() {
    return session.sessionPresent();
  }

  @Override
  public final void sessionInvalidate() {
    session.sessionInvalidate();
  }

  // ##################################################################
  // # END: HttpSession
  // ##################################################################

  // ##################################################################
  // # BEGIN: HttpResponse
  // ##################################################################

  @Override
  public final void ok(Media media) {
    response.ok(media);
  }

  @Override
  public final void movedPermanently(String location) {
    response.movedPermanently(location);
  }

  @Override
  public final void found(String location) {
    response.found(location);
  }

  @Override
  public final void seeOther(String location) {
    response.seeOther(location);
  }

  @Override
  public final void error(Status status) {
    response.error(status);
  }

  @Override
  public final void error(Status status, String message) {
    response.error(status, message);
  }

  @Override
  public final void error(Status status, Throwable cause) {
    response.error(status, cause);
  }

  @Override
  public final void status(Status value) {
    response.status(value);
  }

  @Override
  public final void header(HeaderName name, long value) {
    response.header(name, value);
  }

  @Override
  public final void header(HeaderName name, String value) {
    response.header(name, value);
  }

  @Override
  public final void header(HeaderName name, Consumer<? super HttpHeaderValueBuilder> builder) {
    response.header(name, builder);
  }

  @Override
  public final String now() {
    return response.now();
  }

  @Override
  public final void send() {
    response.send();
  }

  @Override
  public final void send(byte[] bytes) {
    response.send(bytes);
  }

  @Override
  public final void send(byte[] bytes, int offset, int length) {
    response.send(bytes, offset, length);
  }

  @Override
  public final void send(Path file) {
    response.send(file);
  }

  @Override
  public final void send(Media media) {
    response.send(media);
  }

  @Override
  public final boolean processed() {
    return response.processed();
  }

  // ##################################################################
  // # END: HttpResponse
  // ##################################################################

  // ##################################################################
  // # BEGIN: HttpStaticFiles
  // ##################################################################

  @Override
  public final void staticFile(Media media) {
    try {
      staticFilesWriter.writeMedia(this, media);
    } catch (HttpTraversalException e) {
      error(Status.BAD_REQUEST, e);
    } catch (IOException e) {
      error(Status.INTERNAL_SERVER_ERROR, e);
    }
  }

  // ##################################################################
  // # END: HttpStaticFiles
  // ##################################################################

}