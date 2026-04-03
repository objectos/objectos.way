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

import objectos.way.Lang.Key;
import objectos.way.Media.Stream;
import objectos.way.Media.Text;

final class HttpExchangeImpl2 implements HttpExchange {

  private final HttpRequest request;

  HttpExchangeImpl2(HttpRequest request) {
    this.request = request;
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

  @Override
  public boolean sessionAbsent() { return false; }

  @Override
  public <T> T sessionAttr(Class<T> key) { return null; }

  @Override
  public <T> T sessionAttr(Key<T> key) { return null; }

  @Override
  public <T> T sessionAttr(Class<T> key, T value) { return null; }

  @Override
  public <T> T sessionAttr(Key<T> key, T value) { return null; }

  @Override
  public void sessionInvalidate() {}

  @Override
  public boolean sessionPresent() { return false; }

  @Override
  public void ok(objectos.way.Media.Bytes media) {}

  @Override
  public void ok(Stream media) {}

  @Override
  public void ok(Text media) {}

  @Override
  public void movedPermanently(String location) {}

  @Override
  public void found(String location) {}

  @Override
  public void seeOther(String location) {}

  @Override
  public void badRequest(Media media) {}

  @Override
  public void forbidden(Media media) {}

  @Override
  public void notFound(Media media) {}

  @Override
  public void allow(HttpMethod... methods) {}

  @Override
  public void internalServerError(Media media, Throwable error) {}

  @Override
  public void respond(Consumer<? super HttpResponse> response) {}

  @Override
  public boolean processed() { return false; }

}