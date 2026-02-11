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

import java.util.List;
import module objectos.way;

/*

- event: submit
- method: GET

*/
@Css.Source
public final class ScriptSubmit01 extends AbstractDevScript {

  public static final JsAction ACTION = Js.submit();

  private final List<String> items = List.of(
      "AAA",
      "BBB",
      "CCC"
  );

  private String search;

  @Override
  public final void handle(Http.Exchange http) {
    search = http.queryParam("search");

    super.handle(http);
  }

  @Override
  final void renderBody() {
    form(
        css("""
        display:flex
        flex-direction:column
        gap:16rx
        """),

        action("/script/submit/01"),

        method("get"),

        onsubmit(ACTION),

        label(forId("search"), text("search")),

        input(
            id("search"),
            name("search"),
            type("text")
        ),

        button(
            id("click-me"),
            text("Click me"),
            type("submit")
        )
    );

    ul(
        id("subject"),

        f(this::renderList)
    );
  }

  private void renderList() {
    items.stream().filter(s -> {
      if (search == null) {
        return true;
      }

      return s.contains(search);
    }).forEach(s -> {
      li(s);
    });
  }

}
