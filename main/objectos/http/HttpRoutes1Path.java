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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import objectos.http.HttpRoutes.Option;

final class HttpRoutes1Path {

  private HttpRoutes2Method currentMethod;

  private final List<HttpHandler> handlers = new ArrayList<>();

  private final HttpPathMatcher matcher;

  private Map<HttpMethod, HttpRoutes2Method> methods = Map.of();

  private final Set<String> paramNames;

  private Map<String, Predicate<String>> predicates = Map.of();

  HttpRoutes1Path(HttpPathMatcher matcher, Set<String> paramNames) {
    this.matcher = matcher;

    this.paramNames = paramNames;
  }

  public final HttpHandler build() {
    for (HttpRoutes2Method m : methods.values()) {
      final HttpHandler h;
      h = m.build();

      handlers.add(h);
    }

    if (!methods.isEmpty()) {
      final Set<HttpMethod> original;
      original = methods.keySet();

      final Set<HttpMethod> allowed;
      allowed = Set.copyOf(original);

      final HttpHandler3MethodNotAllowed notAllowed;
      notAllowed = new HttpHandler3MethodNotAllowed(allowed);

      handlers.add(notAllowed);
    }

    final Map<String, Predicate<String>> predicatesCopy;
    predicatesCopy = Map.copyOf(predicates);

    final List<HttpHandler> handlersCopy;
    handlersCopy = List.copyOf(handlers);

    return new HttpHandler1Path(matcher, predicatesCopy, handlersCopy);
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

      case HttpMethod method -> addMethod(method);

      case PathParam param -> addPathParam(param);
    }
  }

  private void addHandler(HttpHandler handler) {
    if (currentMethod != null) {
      currentMethod.add(handler);
    } else {
      handlers.add(handler);
    }
  }

  private void addMethod(HttpMethod method) {
    if (methods.isEmpty()) {
      // we use LinkedHashMap instead of EnumMap
      // as declaration order is important
      methods = new LinkedHashMap<>();
    }

    HttpRoutes2Method builder;
    builder = methods.get(method);

    if (builder == null) {
      builder = new HttpRoutes2Method(method);

      methods.put(method, builder);
    }

    currentMethod = builder;
  }

  private void addPathParam(PathParam param) {
    final String name;
    name = param.name();

    if (!paramNames.contains(name)) {
      final String msg;
      msg = "Path expression does not declare a '%s' path parameter".formatted(name);

      throw new IllegalArgumentException(msg);
    }

    if (predicates.isEmpty()) {
      predicates = new HashMap<>();
    }

    final Predicate<String> predicate;
    predicate = param.predicate();

    final Predicate<String> existing;
    existing = predicates.put(name, predicate);

    if (existing != null) {
      final Predicate<String> combined;
      combined = existing.and(predicate);

      predicates.put(name, combined);
    }
  }

}
