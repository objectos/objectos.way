/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.css;

import objectos.css.select.SelectorFactory;
import objectos.css.sheet.AbstractStyleSheet;
import objectos.css.type.Color;
import objectos.html.HtmlTemplate;

class BootstrapExamplesSignIn extends HtmlTemplate {

  private final BootstrapExamplesSignInForm _form;

  BootstrapExamplesSignIn(BootstrapExamplesSignInForm form) {
    this._form = form;
  }

  @Override
  protected final void definition() {
    html(lang("en"),
      f(this::headFrag),
      f(this::bodyFrag)
    );
  }

  private void bodyFrag() {
    body(
      className("text-center"),
      add(_form)
    );
  }

  private void headFrag() {
    head(
      meta(charset("utf-8")),
      meta(name("viewport"), content("width=device-width, initial-scale=1, shrink-to-fit=no")),
      title(
        t("Signin Template · Bootstrap")
      ),
      link(rel("canonical"), href("https://getbootstrap.com/docs/4.3/examples/sign-in/")),
      /*<!-- Bootstrap core CSS -->*/
      link(
        href("https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"),
        rel("stylesheet"),
        /*integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"*/
        crossorigin("anonymous")
      ),
      style(
        t("@media (min-width: 768px) {\n" +
            "  .bd-placeholder-img-lg {\n" +
            "    font-size: 3.5rem;\n" +
            "  }\n" +
            "}")
      ),
      /*<!-- Custom styles for this template -->*/
      // link(href("signin.css"), rel("stylesheet"))
      style(
        t(new ThisStyleSheet().toString())
      )
    );
  }

  private class ThisStyleSheet extends AbstractStyleSheet {
    @Override
    protected final void definition() {
      style(
        html, or(),
        body,

        height(pct(100))
      );

      style(
        body,
        // display(flex)
        // alignItems()
        paddingTop(px(40)),
        paddingBottom(px(40)),
        backgroundColor(Color.hex("#f5f5f5"))
      );

      style(
        _form.className,

        width(pct(100)),
        maxWidth(px(330)),
        padding(px(15)),
        margin(auto)
      );

      style(
        _form.className, sp(), cn("checkbox"),

        fontWeight(400)
      );

      style(
        _form.className, sp(), cn("form-control"),

        position(relative),
        boxSizing(borderBox),
        height(auto),
        padding(px(10)),
        fontSize(px(16))
      );

      style(
        _form.className, sp(), cn("form-control"), FOCUS,

        zIndex(2)
      );

      style(
        _form.className, sp(),
        input, SelectorFactory.attr("type", SelectorFactory.eq("email")),

        marginBottom(px(-1))// ,
      // borderBottomRightRadius(zero()),
      // borderBottomLeftRadius(zero()),
      );

      style(
        _form.className, sp(),
        input, SelectorFactory.attr("type", SelectorFactory.eq("password")),

        marginBottom(px(10))// ,
      // borderTopLeftRadius(zero()),
      // borderTopRightRadius(zero()),
      );
    }
  }

}
