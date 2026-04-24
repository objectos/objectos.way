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

record HttpRequestMatcher5PathParamLast(String paramName) implements HttpRequestMatcher {

  @Override
  public final boolean match(HttpExchange0 http) {
    final int pathIndex;
    pathIndex = http.pathIndex();

    final String path;
    path = http.path();

    final int solidus;
    solidus = path.indexOf('/', pathIndex);

    if (solidus < 0) {

      final String varValue;
      varValue = path.substring(pathIndex);

      http.pathIndexAdd(varValue.length());

      http.pathParamsPut(paramName, varValue);

      return true;

    } else {

      return false;

    }
  }

}
