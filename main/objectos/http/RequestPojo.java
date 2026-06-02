/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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

/// Provides methods for inspecting the request message of an HTTP exchange.
record RequestPojo(

    HttpMethod method,

    String path,

    Map<String, Object> queryParams,

    Version0 version,

    RequestHeaders headers,

    RequestBodyData bodyData,

    RequestBodyForm bodyForm

) implements Request {

  public final boolean closeConnection() {
    return headers.closeConnection();
  }

  @Override
  public final String queryParam(String name) {
    Objects.requireNonNull(name, "name == null");

    return Http.queryParamsGet(queryParams, name);
  }

  @Override
  public final List<String> queryParamAll(String name) {
    Objects.requireNonNull(name, "name == null");

    return Http.queryParamsGetAll(queryParams, name);
  }

  @Override
  public final Set<String> queryParamNames() {
    return queryParams.keySet();
  }

  @Override
  public final String rawPath() {
    // TODO remove?
    throw new UnsupportedOperationException();
  }

  @Override
  public final String rawQuery() {
    // TODO remove?
    throw new UnsupportedOperationException();
  }

  @Override
  public final String rawQueryWith(String name, String value) {
    // TODO remove?
    throw new UnsupportedOperationException();
  }

  @Override
  public final String header(HttpHeaderName name) {
    return headers.header(name);
  }

  @Override
  public final InputStream bodyInputStream() throws IOException {
    return bodyData.open();
  }

  @Override
  public final Set<String> formParamNames() {
    return bodyForm.formParamNames();
  }

  @Override
  public final String formParam(String name) {
    return bodyForm.formParam(name);
  }

  @Override
  public final int formParamAsInt(String name, int defaultValue) {
    return bodyForm.formParamAsInt(name, defaultValue);
  }

  @Override
  public final long formParamAsLong(String name, long defaultValue) {
    return bodyForm.formParamAsLong(name, defaultValue);
  }

  @Override
  public final List<String> formParamAll(String name) {
    return bodyForm.formParamAll(name);
  }

  @Override
  public final IntStream formParamAllAsInt(String name, int defaultValue) {
    return bodyForm.formParamAllAsInt(name, defaultValue);
  }

  @Override
  public final LongStream formParamAllAsLong(String name, long defaultValue) {
    return bodyForm.formParamAllAsLong(name, defaultValue);
  }

}
