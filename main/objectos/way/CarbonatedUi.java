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

final class CarbonatedUi extends Ui {

  private final Html.Template parent;

  public CarbonatedUi(Html.Template parent) {
    this.parent = parent;
  }

  @Override
  public final void page(PageComponent... components) {
    $index(components);

    String title;
    title = (String) $attr(AttributeKey.TITLE);

    Html.FragmentLambda body;
    body = (Html.FragmentLambda) $elem(ElementKey.BODY);

    parent.plugin(html -> {
      html.doctype();

      html.head(
          html.meta(html.charset("utf-8")),
          html.meta(html.httpEquiv("content-type"), html.content("text/html; charset=utf-8")),
          html.meta(html.name("viewport"), html.content("width=device-width, initial-scale=1")),
          html.script(html.src("/carbonated/script.js")),
          html.link(html.rel("shortcut icon"), html.type("image/x-icon"), html.href("/favicon.png")),
          html.link(html.rel("stylesheet"), html.type("text/css"), html.href("/carbonated/styles.css")),
          title != null ? html.title(title) : html.noop()
      );

      html.body(
          body != null ? html.include(body) : html.noop()
      );
    });
  }

}