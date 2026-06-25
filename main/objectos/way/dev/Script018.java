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
package objectos.way.dev;

import objectos.css.CssSource;
import objectos.http.Content;
import objectos.http.MediaType;
import objectos.http.Request;
import objectos.http.Result;
import objectos.script.Js;
import objectos.script.JsAction;
import objectos.script.JsResponse;
import objectos.script.JsString;
import objectos.way.Html;

/*

Test case 018

- event: click
- action: fetch

*/
@CssSource
public final class Script018 extends AbstractDevScript {

  public static final JsAction ACTION = Js.fetch(Js.target().attr(Html.AttributeName.HREF))
      .then(Js.args(0).as(JsResponse.type).text())
      .then(Js.byId("subject").textContent(Js.args(0).as(JsString.type)));

  private String next;

  private String path;

  @Override
  public final Result handle(Request req) {
    next = req.queryParam("next");

    if (next != null) {
      return Content.of(MediaType.TEXT_PLAIN, next);
    } else {
      path = req.path();

      return super.handle(req);
    }
  }

  @Override
  final void renderBody() {
    div(
        css("""
        display:flex
        flex-direction:column
        gap:16rx
        """),

        button(
            id("click-me"),
            onclick(ACTION),
            href(path + "?next=After"),
            text("Click me"),
            type("button")
        ),

        div(id("subject"), text("Before"))
    );
  }

}
