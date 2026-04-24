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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

record HttpRequestMatcher7List(List<HttpRequestMatcher> list) implements HttpRequestMatcher {

  HttpRequestMatcher7List {
    final Set<String> distinct = new HashSet<>();

    for (HttpRequestMatcher matcher : list) {
      final String paramName;

      switch (matcher) {
        case HttpRequestMatcher4PathParam p -> paramName = p.paramName();

        case HttpRequestMatcher5PathParamLast p -> paramName = p.paramName();

        default -> {
          continue;
        }
      }

      if (!distinct.add(paramName)) {
        throw new IllegalArgumentException(
            "The '{%s}' path variable was declared more than once".formatted(paramName)
        );
      }
    }
  }

  @Override
  public final boolean match(HttpExchange0 http) {
    for (HttpRequestMatcher matcher : list) {
      if (!matcher.match(http)) {
        return false;
      }
    }

    return true;
  }

}
