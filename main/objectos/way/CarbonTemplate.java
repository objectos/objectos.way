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

abstract class CarbonTemplate extends Html.Template implements Http.Handler {

  protected final Carbon carbon;

  protected Http.Exchange http;

  protected CarbonTemplate() {
    carbon = new Carbon(this);
  }

  @Override
  public final void handle(Http.Exchange http) {
    this.http = http;

    handle();
  }

  protected void handle() {
    switch (http.method()) {
      case GET, HEAD -> http.ok(this);

      default -> http.methodNotAllowed();
    }
  }

  /**
   * Renders the standard {@code head} contents.
   */
  protected final void renderStandardHead() {
    meta(charset("utf-8"));
    meta(httpEquiv("content-type"), content("text/html; charset=utf-8"));
    meta(name("viewport"), content("width=device-width, initial-scale=1"));
    link(rel("shortcut icon"), type("image/x-icon"), href("/favicon.png"));
    link(rel("stylesheet"), type("text/css"), href("/ui/carbon.css"));
    script(src("/ui/script.js"));
  }

  protected final boolean currentPage(String href) {
    String path;
    path = http.path();

    return path.equals(href);
  }

  protected final boolean currentPageStartsWith(String href) {
    String path;
    path = http.path();

    return path.startsWith(href);
  }

  protected final Html.Instruction.OfElement icon16(Carbon.Icon icon, Html.Instruction.OfAttribute... attributes) {
    return renderIcon(icon, "16px", attributes);
  }

  protected final Html.Instruction.OfElement icon20(Carbon.Icon icon, Html.Instruction.OfAttribute... attributes) {
    return renderIcon(icon, "20px", attributes);
  }

  protected final Html.Instruction.OfElement icon24(Carbon.Icon icon, Html.Instruction.OfAttribute... attributes) {
    return renderIcon(icon, "24px", attributes);
  }

  protected final Html.Instruction.OfElement icon32(Carbon.Icon icon, Html.Instruction.OfAttribute... attributes) {
    return renderIcon(icon, "32px", attributes);
  }

  private Html.Instruction.OfElement renderIcon(Carbon.Icon icon, String size, Html.Instruction.OfAttribute... attributes) {
    return svg(
        xmlns("http://www.w3.org/2000/svg"),
        fill("currentColor"),
        width(size), height(size), viewBox("0 0 32 32"),

        flatten(attributes),

        raw(icon.raw)
    );
  }

}