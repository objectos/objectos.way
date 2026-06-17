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

public record RouteMatcherParam(String paramName, char terminator) implements RouteMatcher {

  @Override
  public final boolean matches(RoutePath path) {
    final int terminatorIndex;
    terminatorIndex = path.indexOf(terminator);

    if (terminatorIndex < 0) {
      return false;
    } else {
      path.param(paramName, terminatorIndex);

      return true;
    }
  }

}
