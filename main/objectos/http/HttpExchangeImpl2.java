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
import objectos.way.Media.Bytes;
import objectos.way.Media.Stream;
import objectos.way.Media.Text;

final class HttpExchangeImpl2 implements HttpExchange {

  private final HttpRequest request;

  private final HttpResponse0 response;

  private final HttpSession session;

  HttpExchangeImpl2(HttpRequest request, HttpResponse0 response, HttpSession session) {
    this.request = request;

    this.response = response;

    this.session = session;
  }

  // ##################################################################
  // # BEGIN: HttpRequest
  // ##################################################################

  @Override
  public final HttpMethod method() {
    return request.method();
  }

  @Override
  public final String header(HttpHeaderName name) {
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

  @Override
  public final HttpVersion version() {
    return request.version();
  }

  @Override
  public final String pathParam(String name) {
    return request.pathParam(name);
  }

  @Override
  public final Set<String> formParamNames() {
    return request.formParamNames();
  }

  @Override
  public final int pathParamAsInt(String name, int defaultValue) {
    return request.pathParamAsInt(name, defaultValue);
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

  @Override
  public <T> void set(Class<T> key, T value) {}

  @Override
  public <T> T get(Class<T> key) { return null; }

  // ##################################################################
  // # BEGIN: HttpSession
  // ##################################################################

  @Override
  public final boolean sessionAbsent() {
    return session == null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public final <T> T sessionAttr(Class<T> key) {
    checkSession();

    final String name;
    name = key.getName();

    return (T) session.get0(name);
  }

  @SuppressWarnings("unchecked")
  @Override
  public final <T> T sessionAttr(Lang.Key<T> key) {
    checkSession();

    Objects.requireNonNull(key, "key == null");

    return (T) session.get0(key);
  }

  @SuppressWarnings("unchecked")
  @Override
  public final <T> T sessionAttr(Class<T> key, T value) {
    checkSession();

    final String name;
    name = key.getName();

    return (T) session.set0(name, value);
  }

  @SuppressWarnings("unchecked")
  @Override
  public final <T> T sessionAttr(Lang.Key<T> key, T value) {
    checkSession();

    Objects.requireNonNull(key, "key == null");

    return (T) session.set0(key, value);
  }

  @Override
  public final boolean sessionPresent() {
    return session != null;
  }

  @Override
  public final void sessionInvalidate() {
    checkSession();

    session.invalidate();
  }

  private void checkSession() {
    if (session == null) {
      throw new IllegalStateException("No session associated to this exchange");
    }
  }

  // ##################################################################
  // # END: HttpSession
  // ##################################################################

  // ##################################################################
  // # BEGIN: HttpResponse
  // ##################################################################

  @Override
  public final void ok(Bytes media) {
    response.ok(media);
  }

  @Override
  public final void ok(Stream media) {
    response.ok(media);
  }

  @Override
  public final void ok(Text media) {
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
  public final void badRequest(Media media) {
    response.badRequest(media);
  }

  @Override
  public final void forbidden(Media media) {
    response.forbidden(media);
  }

  @Override
  public final void notFound(Media media) {
    response.notFound(media);
  }

  @Override
  public final void allow(HttpMethod... methods) {
    response.allow(methods);
  }

  @Override
  public final void internalServerError(Media media, Throwable error) {
    response.internalServerError(media, error);
  }

  public final void status(HttpStatus value) {
    response.status(value);
  }

  public final void header(HttpHeaderName name, long value) {
    response.header(name, value);
  }

  public final void header(HttpHeaderName name, String value) {
    response.header(name, value);
  }

  public final void header(HttpHeaderName name, Consumer<? super HttpHeaderValueBuilder> builder) {
    response.header(name, builder);
  }

  public final String now() {
    return response.now();
  }

  public final void send() {
    response.send();
  }

  public final void send(byte[] bytes, int offset, int length) {
    response.send(bytes, offset, length);
  }

  public final void send(Path file) {
    response.send(file);
  }

  public final void send(Media media) {
    response.send(media);
  }

  public final void media(Media.Bytes media) {
    response.media(media);
  }

  public final void media(Media.Text media) {
    response.media(media);
  }

  public final void media(Media.Stream media) {
    response.media(media);
  }

  // ##################################################################
  // # END: HttpResponse
  // ##################################################################

  @Override
  public final boolean processed() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public void respond(Consumer<? super HttpResponse> response) {
    throw new UnsupportedOperationException("Implement me");
  }

}