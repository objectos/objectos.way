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

import objectos.css.select.ClassSelector;
import objectos.html.HtmlTemplate;

class BootstrapExamplesSignInForm extends HtmlTemplate {

  final ClassSelector className = Css.cn("form-signin");

  @Override
  protected final void definition() {
    form(className,
      img(className("mb-4"),
        src("https://getbootstrap.com/docs/4.3/assets/brand/bootstrap-solid.svg"),
        alt(""),
        width("72"), height("72")),
      h1(className("h3 mb-3 font-weight-normal"),
        t("Please sign in")
      ),
      label(forAttr("inputEmail"), className("sr-only"),
        t("Email address")
      ),
      input(
        type("email"),
        id("inputEmail"),
        className("form-control"),
        placeholder("Email address"),
        required(),
        autofocus()
      ),
      label(
        forAttr("inputPassword"),
        className("sr-only"),
        t("Password")
      ),
      input(
        type("password"),
        id("inputPassword"),
        className("form-control"),
        placeholder("Password"),
        required()
      ),
      div(className("checkbox mb-3"),
        label(
          input(type("checkbox"), value("remember-me")),
          t(" Remember me")
        )
      ),
      button(className("btn btn-lg btn-primary btn-block"), type("submit"),
        t("Sign in")
      ),
      p(className("mt-5 mb-3 text-muted"),
        t("&copy; 2017-2019")
      )
    );
  }

}