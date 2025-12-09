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
package objectos.way.dev;

import module objectos.way;

abstract class AbstractDevScript extends Html.Template implements Http.Handler {

  @Override
  public final void handle(Http.Exchange http) {
    final String thatPath;
    thatPath = http.path();

    final String thisPath;
    thisPath = path();

    if (!thatPath.equals(thisPath)) {
      return;
    }

    final Http.Method method;
    method = http.method();

    if (method != Http.Method.GET) {
      http.allow(Http.Method.GET);

      return;
    }

    http.ok(this);
  }

  @Override
  protected final void render() {
    doctype();

    html(
        head(
            meta(charset("utf-8")),
            meta(httpEquiv("content-type"), content("text/html; charset=utf-8")),
            meta(name("viewport"), content("width=device-width, initial-scale=1"))
        ),

        body(f(this::renderBody))
    );
  }

  abstract String path();

  abstract void renderBody();

}
