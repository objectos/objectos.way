/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
package objectos.way;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;

final class HttpRouting extends HttpModuleMatcherParser implements Http.Routing {

  private Predicate<Http.Request> condition = req -> true;

  private Http.Handler[] handlers;

  private int handlersIndex;

  @Override
  public void allow(Http.Method method, Http.Handler handler) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final void handler(Http.Handler handler) {
    Objects.requireNonNull(handler, "handler == null");

    add(handler);
  }

  @Override
  public void install(Consumer<Http.Routing> module) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final void install(Http.Module module) {
    module.configure(this);
  }

  @Override
  public final void path(String path, Consumer<Http.Routing> config) {
    final HttpRouting routing;
    routing = new HttpRouting();

    routing.setPath(path);

    config.accept(routing);

    final Http.Handler handler;
    handler = routing.build();

    add(handler);
  }

  private void setPath(String path) {
    condition = matcher(path);
  }

  @Override
  public void param(String name, Predicate<String> condition) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final void paramDigits(String name) {
    Objects.requireNonNull(name, "name == null");

    final HttpModuleCondition.Digits condition;
    condition = new HttpModuleCondition.Digits(name);

    add(condition);
  }

  @Override
  public final void paramNotEmpty(String name) {
    Objects.requireNonNull(name, "name == null");

    final HttpModuleCondition.NotEmpty condition;
    condition = new HttpModuleCondition.NotEmpty(name);

    add(condition);
  }

  @Override
  public final void paramRegex(String name, String value) {
    Objects.requireNonNull(name, "name == null");

    final Pattern pattern;
    pattern = Pattern.compile(value);

    final HttpModuleCondition.Regex condition;
    condition = new HttpModuleCondition.Regex(name, pattern);

    add(condition);
  }

  private void add(HttpModuleCondition pathCondition) {
    if (!(condition instanceof HttpModuleMatcher matcher)) {
      throw new IllegalStateException("Can't set a path parameter predicate as a path has not been set.");
    }

    condition = matcher.withCondition(pathCondition);
  }

  @Override
  public void when(Predicate<Http.Request> condition, Http.Module module) {
    final HttpRouting builder;
    builder = new HttpRouting();

    builder.condition = Objects.requireNonNull(condition, "condition == null");

    // module implicit null-check
    module.configure(builder);

    final Http.Handler handler;
    handler = builder.build();

    add(handler);
  }

  final Http.Handler build() {
    return switch (handlersIndex) {
      case 0 -> Http.Handler.noop();

      case 1 -> {
        final Http.Handler single;
        single = handlers[0];

        yield handler(condition, single);
      }

      default -> {
        final Http.Handler[] copy;
        copy = Arrays.copyOf(handlers, handlersIndex);

        yield handler(condition, copy);
      }
    };
  }

  private void add(Http.Handler handler) {
    int requiredIndex;
    requiredIndex = handlersIndex++;

    if (handlers == null) {
      handlers = new Http.Handler[10];
    } else {
      handlers = Util.growIfNecessary(handlers, requiredIndex);
    }

    handlers[requiredIndex] = handler;
  }

  private static Http.Handler handler(Predicate<Http.Request> condition, Http.Handler handler) {
    return http -> {
      if (http.processed()) {
        return;
      }

      if (condition.test(http)) {
        handler.handle(http);
      }
    };
  }

  private static Http.Handler handler(Predicate<Http.Request> condition, Http.Handler[] handlers) {
    return http -> {
      if (http.processed()) {
        return;
      }

      if (condition.test(http)) {
        for (Http.Handler handler : handlers) {
          handler.handle(http);

          if (http.processed()) {
            break;
          }
        }
      }
    };
  }

}