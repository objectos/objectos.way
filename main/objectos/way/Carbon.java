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

import java.io.IOException;
import java.io.UncheckedIOException;
import objectos.lang.object.Check;
import objectos.way.Carbon.Ui.HeaderData;
import objectos.way.Carbon.Ui.HeaderNameData;
import objectos.way.Html.FragmentLambda1;

/**
 * The <strong>Objectos Carbon UI</strong> main class.
 *
 * <p>
 * Objectos Carbon UI is an implementation of the Carbon Design System in pure
 * Java.
 */
public final class Carbon {

  /**
   * The UI shell is the top level UI component of an web application.
   */
  public static abstract class Shell extends Html.Template implements Web.Action {

    private final Http.Exchange http;

    protected final Ui ui;

    protected String title;

    protected Shell(Http.Exchange http) {
      this.http = http;

      Carbon carbon;
      carbon = http.get(Carbon.class);

      ui = carbon.ui(this);
    }

    @Override
    public void execute() {
      http.ok(this);
    }

    @Override
    protected final void render() throws Exception {
      doctype();

      head(
          meta(charset("utf-8")),
          meta(httpEquiv("content-type"), content("text/html; charset=utf-8")),
          meta(name("viewport"), content("width=device-width, initial-scale=1")),
          script(src("/ui/script.js")),
          link(rel("shortcut icon"), type("image/x-icon"), href("/favicon.png")),
          link(rel("stylesheet"), type("text/css"), href("/ui/carbon.css")),
          title != null ? title(title) : noop()
      );

      body(
          f(this::renderUi)
      );
    }

    protected abstract void renderUi() throws Exception;

    protected final void shellTitle(String title) {
      this.title = Check.notNull(title, "title == null");
    }

  }

  // attributes

  sealed static abstract class Attribute {
    private final String value;

    Attribute(String value) {
      this.value = Check.notNull(value, "value == null");
    }

    public final String value() {
      return value;
    }
  }

  /**
   * The {@code href} attribute.
   */
  public static final class Href extends Attribute implements HeaderName.Component { Href(String value) { super(value); } }

  public static final class Prefix extends Attribute implements HeaderName.Component { Prefix(String value) { super(value); } }

  // elements

  sealed static abstract class Element1<T1> implements Html.FragmentLambda {
    final Html.FragmentLambda1<T1> fragment;
    final T1 data;

    Element1(FragmentLambda1<T1> fragment, T1 data) {
      this.fragment = fragment;
      this.data = data;
    }

    @Override
    public final void invoke() throws Exception {
      fragment.invoke(data);
    }
  }

  /**
   * The UI shell header.
   */
  public static final class Header extends Element1<Ui.HeaderData> {
    Header(FragmentLambda1<HeaderData> fragment, HeaderData data) { super(fragment, data); }

    /**
     * An UI shell header component.
     */
    public sealed interface Component extends Carbon.Component {}
  }

  /**
   * The UI shell header name.
   */
  public static final class HeaderName extends Element1<HeaderNameData> implements Header.Component {
    HeaderName(FragmentLambda1<HeaderNameData> fragment, HeaderNameData data) { super(fragment, data); }

    /**
     * An UI shell header name component.
     */
    public sealed interface Component extends Carbon.Component {}
  }

  sealed interface Component {}

  // ui builder

  public static final class Ui {

    private final Html.Template tmpl;

    private Ui(Html.Template tmpl) {
      this.tmpl = tmpl;
    }

    // attributes

    public final Href href(String value) {
      return new Href(value);
    }

    public final Prefix prefix(String value) {
      return new Prefix(value);
    }

    // elements

    // Header

    public final Header header(Header.Component... components) {
      HeaderData data;
      data = new HeaderData();

      for (Header.Component c : components) {
        switch (c) {
          case HeaderName headerName -> data.headerName = headerName;
        }
      }

      return new Header(this::header, data);
    }

    static final class HeaderData {
      HeaderName headerName;
    }

    private void header(HeaderData data) {
      tmpl.header(
          tmpl.className("fixed inset-0px flex h-48px"),
          tmpl.className("border-b border-subtle"),
          tmpl.className("bg-background"),

          data.headerName != null ? tmpl.include(data.headerName) : tmpl.noop()
      );
    }

    // HeaderName

    public final HeaderName headerName(HeaderName.Component... components) {
      HeaderNameData data;
      data = new HeaderNameData();

      for (HeaderName.Component c : components) {
        switch (c) {
          case Href href -> data.href = href.value();
          case Prefix prefix -> data.prefix = prefix.value();
        }
      }

      return new HeaderName(this::headerName, data);
    }

    static final class HeaderNameData {
      String href;
      String prefix;
    }

    private void headerName(HeaderNameData data) {
      tmpl.a(
          tmpl.className("flex h-full select-none items-center"),
          tmpl.className("border-2 border-transparent"),
          tmpl.className("pr-32px pl-16px"),
          tmpl.className("text-body-compact-01 font-semibold"),
          tmpl.className("outline-none"),
          tmpl.className("transition-colors duration-100"),
          tmpl.className("focus:border-focus"),

          data.href != null ? tmpl.href(data.href) : tmpl.noop(),
          data.prefix != null ? tmpl.span(data.prefix) : tmpl.noop()
      );
    }
  }

  private Carbon() {}

  public static Carbon create() {
    return new Carbon();
  }

  public final Http.Module createHttpModule() {
    final byte[] script;

    try {
      script = Script.getBytes();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    return new Web.Module() {
      @Override
      protected final void configure() {
        route("/ui/script.js", GET(this::script));
        route("/ui/carbon.css", GET(new CarbonStyles()));

        filter(this::injectCarbon);
      }

      private void script(Http.Exchange http) {
        http.status(Http.OK);

        http.dateNow();

        http.header(Http.CONTENT_TYPE, "text/javascript; charset=utf-8");

        http.header(Http.CONTENT_LENGTH, script.length);

        http.send(script);
      }

      private void injectCarbon(Http.Exchange http) {
        http.set(Carbon.class, Carbon.this);
      }
    };
  }

  public final Ui ui(Html.Template parent) {
    return new Ui(parent);
  }

}