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
import objectos.way.Carbon.Icon;

abstract class CarbonShell extends Html.Template implements Web.Action {

  private final Http.Exchange http;

  private Html.ClassName theme = Carbon.WHITE;

  private String title;

  protected CarbonShell(Http.Exchange http) {
    this.http = http;
  }

  @Override
  public void execute() {
    http.ok(this);
  }

  @Override
  protected final void render() throws Exception {
    doctype();

    html(
        theme,

        head(
            meta(charset("utf-8")),
            meta(httpEquiv("content-type"), content("text/html; charset=utf-8")),
            meta(name("viewport"), content("width=device-width, initial-scale=1")),
            script(src("/ui/script.js")),
            link(rel("shortcut icon"), type("image/x-icon"), href("/favicon.png")),
            link(rel("stylesheet"), type("text/css"), href("/ui/carbon.css")),
            title != null ? title(title) : noop()
        ),

        body(
            f(this::renderUi)
        )
    );
  }

  protected abstract void renderUi() throws Exception;

  protected final void shellTheme(Html.ClassName theme) {
    this.theme = Check.notNull(theme, "theme == null");
  }

  protected final void shellTitle(String title) {
    this.title = Check.notNull(title, "title == null");
  }

  protected final Html.ElementInstruction icon16(Icon icon, Html.AttributeInstruction... attributes) {
    return renderIcon(icon, "16px", attributes);
  }

  protected final Html.ElementInstruction icon20(Icon icon, Html.AttributeInstruction... attributes) {
    return renderIcon(icon, "20px", attributes);
  }

  protected final Html.ElementInstruction icon24(Icon icon, Html.AttributeInstruction... attributes) {
    return renderIcon(icon, "24px", attributes);
  }

  protected final Html.ElementInstruction icon32(Icon icon, Html.AttributeInstruction... attributes) {
    return renderIcon(icon, "32px", attributes);
  }

  private Html.ElementInstruction renderIcon(Carbon.Icon icon, String size, Html.AttributeInstruction... attributes) {
    return svg(
        xmlns("http://www.w3.org/2000/svg"),
        fill("currentColor"),
        width(size), height(size), viewBox("0 0 32 32"),

        flatten(attributes),

        raw(
            renderIconRaw(icon)
        )
    );
  }

  private String renderIconRaw(Carbon.Icon icon) {
    return switch (icon) {
      case CLOSE -> """
      <polygon points="17.4141 16 24 9.4141 22.5859 8 16 14.5859 9.4143 8 8 9.4141 14.5859 16 8 22.5859 9.4143 24 16 17.4141 22.5859 24 24 22.5859 17.4141 16"/>""";

      case MENU -> """
      <rect x="4" y="6" width="24" height="2"/><rect x="4" y="24" width="24" height="2"/><rect x="4" y="12" width="24" height="2"/><rect x="4" y="18" width="24" height="2"/>""";
    };
  }

}