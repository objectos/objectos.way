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
package objectox.http.req;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import objectos.http.HeaderName;
import objectos.http.RequestMethod;
import objectos.http.RequestOptions;
import objectox.http.RequestMethodEnum;
import objectox.http.Version0;
import objectox.http.session.Session;
import objectox.http.session.SessionPojo;

public final class RequestBuilder implements RequestOptions {

  private final RequestAttributes attributes = new RequestAttributes();

  private RequestBodyData bodyData;

  private RequestBodyForm bodyForm;

  private Map<HeaderName, Object> headers = Map.of();

  private RequestMethod method = RequestMethodEnum.GET;

  private String path = "/";

  private final Map<String, Object> queryParams = Map.of();

  private SessionPojo session;

  private final Version0 version = Version0.HTTP_1_1;

  public final RequestPojo build() {
    return new RequestPojo(
        attributes,

        method,

        path,

        queryParams,

        version,

        new RequestHeaders(headers),

        bodyData,

        bodyForm
    );
  }

  @Override
  public final void header(HeaderName name, String value) {
    Objects.requireNonNull(name, "name == null");
    Objects.requireNonNull(value, "value == null");

    if (headers.isEmpty()) {
      headers = new LinkedHashMap<>();
    }

    final Object previous;
    previous = headers.put(name, value);

    switch (previous) {
      case null -> {}

      case String s -> {
        List<String> list = new ArrayList<>();

        list.add(s);

        list.add(value);

        headers.put(name, list);
      }

      case List<?> l -> {
        @SuppressWarnings("unchecked")
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
  public final void method(RequestMethod value) {
    method = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void path(String value) {
    path = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final <T> T sessionAttr(Class<T> key, T value) {
    if (session == null) {
      final Map<Object, Object> s;
      s = new HashMap<>();

      session = new SessionPojo(s);

      attributes.set(Session.KEY, session);
    }

    return session.attr(key, value);
  }

}
