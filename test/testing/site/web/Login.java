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

import java.io.IOException;
import objectos.way.Html;
import objectos.way.Http;
import objectos.way.Web;
import testing.site.auth.User;
import testing.zite.TestingSiteInjector;

final class Login extends WebTemplate implements Http.Handler {

  public Login(TestingSiteInjector injector) {
    title = "Login Page";
  }

  @Override
  public final void handle(Http.Exchange http) {
    http.methodMatrix(
        Http.GET, this::get,
        Http.POST, this::post
    );
  }

  // GET

  private void get(Http.Exchange http) {
    Web.Session session;
    session = http.session();

    User user;
    user = session.get(User.class);

    if (user != null) {
      http.found("/");
    } else {
      http.ok(this);
    }
  }

  private enum FormState {

    LOGIN,

    PASSWORD;

  }

  private static final Html.Id FORM = Html.id("login-form");

  private static final String STEP = "step";

  private static final String LOGIN = "login";

  private static final String PASSWORD = "password";

  private FormState state = FormState.LOGIN;

  private String login;

  @SuppressWarnings("unused")
  private String loginError;

  @Override
  final void bodyImpl() {
    className("bg-gray-100");

    dataFrame("root", "login");

    div(className("mx-4 flex h-screen flex-col items-center sm:mx-auto"),
        form(className("my-auto w-full bg-white sm:w-auto"), FORM, action("/login"), method("post"),
            include(this::renderForm)
        )
    );
  }

  private void renderForm() {
    switch (state) {
      case LOGIN -> renderForm1();

      case PASSWORD -> renderForm2();
    }
  }

  private void renderForm1() {
    dataFrame("form", "1");

    input(name(STEP), type("hidden"), value("one"));

    div(className("mb-1 p-4 pb-0 text-2xl leading-none tracking-tighter"),
        t("Log in")
    );

    div(className("mb-12 px-4 text-gray-700 text-sm leading-none tracking-tighter"),
        t("Don't have an account? "),
        a(className("text-blue-700"), href("#"), t("Create a new one"))
    );

    div(className("relative mb-6"),
        label(className("absolute top-4 left-4 text-xs text-gray-700"),
            forAttr(LOGIN), t("Email address")),

        input(className("w-full border-y border-t-gray-300 border-b-gray-400 px-4 pt-8 pb-3.5 text-sm"),
            id(LOGIN), name(LOGIN), type("text"), placeholder("user@example.com"), required()
        )
    );

    div(className("flex mb-10 pl-4"),
        input(id("remember"), name("remember"), type("checkbox")),

        label(className("ml-3 text-sm text-gray-700"), forAttr("remember"), t("Remember me"))
    );

    div(className("flex"),
        div(className("hidden sm:block sm:w-60"), raw("&nbsp;")),

        button(className("flex justify-between h-16 w-full bg-blue-600 px-4 pt-4 text-left text-sm text-white sm:w-60"),
            type("submit"),

            span("Continue"),

            svg(xmlns("http://www.w3.org/2000/svg"), width("24"), height("24"), viewBox("0 0 32 32"),
                className("fill-current"),
                path(d("M28 16 21 9 19.586 10.414 24.172 15 4 15 4 17 24.172 17 19.586 21.586 21 23 28 16z"))
            )
        )
    );
  }

  private void renderForm2() {
    dataFrame("form", "2");

    input(name(STEP), type("hidden"), value("two"));

    input(name(LOGIN), type("hidden"), value(login));

    div(className("mb-1 p-4 pb-0 text-2xl leading-none tracking-tighter"),
        t("Log In")
    );

    div(className("mb-12 px-4 text-gray-700 text-sm leading-none tracking-tighter"),
        t("Logging in as "), t(login), raw("&nbsp;"),
        a(className("text-blue-700"), href("#"), t("Not you?"))
    );

    div(className("relative mb-6"),
        label(className("absolute top-4 left-4 text-xs text-gray-700"),
            forAttr(PASSWORD), t("Password")),

        input(className("w-full border-y border-t-gray-300 border-b-gray-400 pt-8 px-4 pb-3.5 text-sm tracking-widest"),
            id(PASSWORD), name(PASSWORD), type("password")
        )
    );

    div(className("flex mb-10 pl-4"),
        raw("&nbsp;")
    );

    div(className("flex"),
        div(className("hidden sm:block sm:w-60"), raw("&nbsp;")),

        button(className("flex justify-between h-16 w-full bg-blue-600 px-4 pt-4 text-left text-sm text-white sm:w-60"),
            type("submit"),

            span("Log in"),

            svg(xmlns("http://www.w3.org/2000/svg"), width("24"), height("24"), viewBox("0 0 32 32"),
                className("fill-current"),
                path(d("M28 16 21 9 19.586 10.414 24.172 15 4 15 4 17 24.172 17 19.586 21.586 21 23 28 16z"))
            )
        )
    );
  }

  // POST

  private void post(Http.Exchange http) {
    Web.Session session;
    session = http.session();

    User user;
    user = session.get(User.class);

    if (user != null) {
      http.ok();
    } else {
      post0(http);
    }
  }

  private void post0(Http.Exchange http) {
    try {
      Http.FormUrlEncoded form;
      form = Http.parseFormUrlEncoded(http);

      String step;
      step = form.getOrDefault(STEP, "");

      switch (step) {
        case "one" -> postStep1(http, form);

        case "two" -> postStep2(http, form);

        default -> http.unprocessableContent();
      }
    } catch (IOException e) {
      http.internalServerError(e);
    } catch (Http.UnsupportedMediaTypeException e) {
      http.unsupportedMediaType();
    }
  }

  private void postStep1(Http.Exchange http, Http.FormUrlEncoded form) {
    login = form.getOrDefault(LOGIN, "");

    if (login.isBlank()) {
      loginError = "Login is required";
    }

    state = FormState.PASSWORD;

    http.ok(this);
  }

  private void postStep2(Http.Exchange http, Http.FormUrlEncoded form) {
    String login;
    login = form.getOrDefault(LOGIN, "");

    String password;
    password = form.getOrDefault(PASSWORD, "");

    User user;
    user = authenticate(login, password);

    if (user != null) {
      Web.Session session;
      session = http.session();

      session.put(User.class, user);

      http.found("/");
    } else {
      http.ok(this);
    }
  }

  private User authenticate(String login, String password) {
    if (!"admin".equals(login)) {
      return null;
    }

    if (!"admin".equals(password)) {
      return null;
    }

    return new User(123L, "Someone");
  }

}