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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

record HttpRequestImpl(

    HttpMethod method,

    String path,

    Map<String, Object> queryParams,

    HttpVersion version,

    Map<HttpHeaderName, Object> headers

) implements HttpRequest {

  @Override
  public final String header(HttpHeaderName name) {
    Objects.requireNonNull(name, "name == null");

    if (headers == null) {
      return null;
    } else {
      return Http.queryParamsGet(headers, name);
    }
  }

  @Override
  public String pathParam(String name) { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public final String queryParam(String name) {
    Objects.requireNonNull(name, "name == null");

    if (queryParams == null) {
      return null;
    } else {
      return Http.queryParamsGet(queryParams, name);
    }
  }

  @Override
  public final List<String> queryParamAll(String name) {
    Objects.requireNonNull(name, "name == null");

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
    // TODO remove?
    throw new UnsupportedOperationException();
  }

  @Override
  public String rawQuery() { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public String rawQueryWith(String name, String value) { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public InputStream bodyInputStream() throws IOException { throw new UnsupportedOperationException("Implement me"); }

}
