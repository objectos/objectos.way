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
package testing.site.web;

import objectos.way.Html;

abstract class WebTemplate extends Html.Template {
  
  protected String title = "";

  @Override
  protected void definition() {
    doctype();
    html(
        head(f(this::$head)),
        body(f(this::bodyImpl))
    );
  }

  private void $head() {
    /*
    meta(charset("utf-8"));
    meta(httpEquiv("x-ua-compatible"), content("ie=edge"));
    meta(name("viewport"), content("width=device-width, initial-scale=1"));
    script(src("/common/way.js"));
    link(rel("stylesheet"), type("text/css"), href("/common/preflight.css"));
    link(rel("stylesheet"), type("text/css"), href("/styles.css"));
    */
    
    if (title != null) {
      title(title);
    }
  }
  
  abstract void bodyImpl();

}