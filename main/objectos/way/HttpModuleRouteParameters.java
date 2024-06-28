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

import objectos.way.HttpModule.Condition;

final class HttpModuleRouteParameters extends HttpModule.RouteOption {

  private final HttpModule.Condition[] conditions;

  public HttpModuleRouteParameters(HttpModule.Condition[] conditions) {
    this.conditions = conditions;
  }

  public final HttpModuleMatcher decorate(HttpModuleMatcher matcher) {
    return new ThisMatcher(matcher, conditions);
  }

  private static final class ThisMatcher implements HttpModuleMatcher {

    private final HttpModuleMatcher matcher;

    private final HttpModule.Condition[] conditions;

    public ThisMatcher(HttpModuleMatcher matcher, Condition[] conditions) {
      this.matcher = matcher;

      this.conditions = conditions;
    }

    @Override
    public final boolean test(HttpRequestTargetPath path) {
      return matcher.test(path) && testConditions(path);
    }

    private boolean testConditions(HttpRequestTargetPath path) {
      for (HttpModule.Condition condition : conditions) {
        if (!condition.test(path)) {
          return false;
        }
      }

      return true;
    }

  }

}