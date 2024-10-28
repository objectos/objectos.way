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

final class HttpModuleRoute implements HttpModuleAction {

  private final HttpModuleMatcher matcher;

  private final Http.Handler handler;

  public HttpModuleRoute(HttpModuleMatcher matcher, Http.Handler handler) {
    this.matcher = matcher;
    this.handler = handler;
  }

  @Override
  public final boolean execute(Http.Exchange http) {
    boolean result;
    result = false;

    HttpExchange t;
    t = (HttpExchange) http;

    t.matcherReset();

    if (matcher.test(t)) {
      handler.handle(http);

      result = http.processed();
    }

    return result;
  }

}