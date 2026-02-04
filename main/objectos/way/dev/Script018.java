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

import module objectos.way;
import objectos.script.Js;

/*

Test case 018

- event: click
- action: fetch

*/
@Css.Source
public final class Script018 extends AbstractDevScript {

  public static final JsAction ACTION = Js.fetch(Js.target().attr(Html.AttributeName.HREF))
      .then(Js.args(0).as(JsResponse.type).text())
      .then(Js.byId("subject").textContent(Js.args(0).as(JsString.type)));

  private String next;

  private String path;

  @Override
  public final void handle(Http.Exchange http) {
    next = http.queryParam("next");

    if (next != null) {
      final Media.Bytes text;
      text = Media.Bytes.textPlain(next);

      http.ok(text);
    } else {
      path = http.path();

      super.handle(http);
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
