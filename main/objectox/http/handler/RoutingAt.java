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
package objectox.http.handler;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import objectos.http.Handler;
import objectos.http.Redirection;
import objectos.http.RequestMethod;
import objectos.http.Response;
import objectos.http.RoutingOption;

final class RoutingAt {

  private RoutingAtCommon current;

  private Map<RequestMethod, RoutingAtMethod> methods = Map.of();

  private final RoutingAtPath parent;

  RoutingAt(RouteMatcher pathMatcher) {
    parent = new RoutingAtPath(pathMatcher);

    current = parent;
  }

  public final Handler build() {
    final Collection<RoutingAtMethod> atMethods;
    atMethods = methods.values();

    for (RoutingAtMethod m : atMethods) {
      final Handler handler;
      handler = m.build();

      parent.addHandler(handler);
    }

    return parent.build();
  }

  public final void option(RoutingOption option) {
    switch (option) {
      case Redirection redir -> current.result(redir);

      case RequestMethod method -> method(method);

      case Response response -> current.result(response);
    }
  }

  private void method(RequestMethod method) {
    if (methods.isEmpty()) {
      methods = new LinkedHashMap<>();
    }

    final RoutingAtMethod routing;
    routing = new RoutingAtMethod(method);

    final RoutingAtMethod existing;
    existing = methods.put(method, routing);

    if (existing != null) {
      final String msg;
      msg = "Method %s has already been configured".formatted(method.name());

      throw new IllegalArgumentException(msg);
    }

    current = routing;
  }

}
