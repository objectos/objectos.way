/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectox.http;

import java.util.Map;
import objectos.http.Http.Header.Name;
import objectos.http.Http.Header.Value;
import objectos.http.Http.Method;
import objectos.http.server.Request;

final class HttpRequest implements Request {

  private final HttpExchange outer;

  HttpRequest(HttpExchange outer) {
    this.outer = outer;
  }

  @Override
  public final Value header(Name name) {
    Map<HeaderName, HeaderValue> map;
    map = outer.requestHeaders;

    Value value;
    value = map.get(name);

    if (value == null) {
      value = Value.NULL;
    }

    return value;
  }

  @Override
  public final Method method() {
    return outer.method;
  }

  @Override
  public final String path() {
    return outer.requestTarget.toString();
  }

  @Override
  public final Body body() {
    return outer.requestBody;
  }

}