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

import java.util.ArrayList;
import java.util.List;
import objectos.http.HttpRoutes.Option;

final class HttpRoutes1Path {

  private final List<HttpHandler> handlers = new ArrayList<>();

  private final HttpPathMatcher matcher;

  private HttpMethod method;

  HttpRoutes1Path(HttpPathMatcher matcher) {
    this.matcher = matcher;
  }

  public final HttpHandler build() {
    final List<HttpHandler> handlersCopy;
    handlersCopy = List.copyOf(handlers);

    return new HttpHandler3Path2(matcher, handlersCopy);
  }

  public final void add(Option o, String name) {
    if (o == null) {
      throw new NullPointerException(name + " == null");
    }

    add(o);
  }

  public final void add(Option o, String name, int idx) {
    if (o == null) {
      throw new NullPointerException(name + "[" + idx + "] == null");
    }

    add(o);
  }

  private void add(Option o) {
    switch (o) {
      case HttpHandler handler -> addHandler(handler);
    }
  }

  private void addHandler(HttpHandler handler) {
    if (method != null) {
      throw new UnsupportedOperationException("Implement me");
    } else {
      handlers.add(handler);
    }
  }

}
