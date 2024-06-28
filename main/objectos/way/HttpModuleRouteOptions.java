/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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

import objectos.lang.object.Check;

record HttpModuleRouteOptions(HttpModuleRouteParameters parameters) {

  static class Builder {

    HttpModuleRouteParameters params;

    public final HttpModuleRouteOptions build() {
      return new HttpModuleRouteOptions(params);
    }

  }

  private static final HttpModuleRouteOptions EMPTY = new HttpModuleRouteOptions(null);

  static HttpModuleRouteOptions of(HttpModule.RouteOption[] options) {
    if (options.length == 0) {
      return EMPTY;
    }

    Builder builder = new Builder();

    for (int i = 0; i < options.length; i++) {
      HttpModule.RouteOption o;
      o = Check.notNull(options[i], "options[", i, "] == null");

      switch (o) {
        case HttpModuleRouteParameters params -> builder.params = params;
      }
    }

    return builder.build();
  }

  public final HttpModuleMatcher decorate(HttpModuleMatcher matcher) {
    return parameters != null ? parameters.decorate(matcher) : matcher;
  }

}