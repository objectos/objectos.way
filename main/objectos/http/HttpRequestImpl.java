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

record HttpRequestImpl(

    HttpMethod method,

    String path,

    Map<String, Object> queryParams,

    HttpVersion version,

    Map<HttpHeaderName, Object> headers,

    InputStream bodyInputStream,

    Map<String, Object> formParams

) implements HttpRequest {

  @Override
  public final Set<String> formParamNames() {
    if (formParams == null) {
      return Set.of();
    } else {
      return formParams.keySet();
    }
  }

  @Override
  public final String formParam(String name) {
    Objects.requireNonNull(name, "name == null");

    if (formParams == null) {
      return null;
    } else {
      return Http.queryParamsGet(formParams, name);
    }
  }

  @Override
  public final int formParamAsInt(String name, int defaultValue) {
    String maybe;
    maybe = formParam(name);

    if (maybe == null) {
      return defaultValue;
    }

    try {
      return Integer.parseInt(maybe);
    } catch (NumberFormatException expected) {
      return defaultValue;
    }
  }

  @Override
  public final long formParamAsLong(String name, long defaultValue) {
    String maybe;
    maybe = formParam(name);

    if (maybe == null) {
      return defaultValue;
    }

    try {
      return Long.parseLong(maybe);
    } catch (NumberFormatException expected) {
      return defaultValue;
    }
  }

  @Override
  public final List<String> formParamAll(String name) {
    Objects.requireNonNull(name, "name == null");

    if (formParams == null) {
      return List.of();
    } else {
      return Http.queryParamsGetAll(formParams, name);
    }
  }

  @Override
  public final IntStream formParamAllAsInt(String name, int defaultValue) {
    return formParamAll(name).stream().mapToInt(s -> {
      try {
        return Integer.parseInt(s);
      } catch (NumberFormatException expected) {
        return defaultValue;
      }
    });
  }

  @Override
  public final LongStream formParamAllAsLong(String name, long defaultValue) {
    return formParamAll(name).stream().mapToLong(s -> {
      try {
        return Long.parseLong(s);
      } catch (NumberFormatException expected) {
        return defaultValue;
      }
    });
  }

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
  public final String rawQuery() {
    // TODO remove?
    throw new UnsupportedOperationException();
  }

  @Override
  public final String rawQueryWith(String name, String value) {
    // TODO remove?
    throw new UnsupportedOperationException();
  }

}
