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

/**
 * The <strong>Objectos Carbon UI</strong> main class.
 *
 * <p>
 * Objectos Carbon UI is an implementation of the Carbon Design System in pure
 * Java.
 */
public final class Carbon {

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

  /**
   * The {@code prefix} attribute.
   */
  public static final class Prefix extends Attribute implements HeaderName.Component { Prefix(String value) { super(value); } }

  // elements

  /**
   * The UI shell header.
   */
  public static final class Header {
    /**
     * An UI shell header component.
     */
    public sealed interface Component {}

    HeaderName headerName;

    Header(Component[] components) {
      for (Component c : components) { // implicit null check
        switch (c) {
          case HeaderName o -> headerName = o;
        }
      }
    }
  }

  /**
   * The UI shell header name.
   */
  public static final class HeaderName implements Header.Component {
    /**
     * An UI shell header name component.
     */
    public sealed interface Component {}

    String href;

    String prefix;

    HeaderName(Component[] components) {
      for (Component c : components) { // implicit null check
        switch (c) {
          case Href o -> href = o.value();

          case Prefix o -> prefix = o.value();
        }
      }
    }
  }

  // ui builder

  /**
   * The UI builder.
   */
  public static final class Ui {

    private final Html.Template tmpl;

    Ui(Html.Template tmpl) {
      this.tmpl = tmpl;
    }

    // attributes

    /**
     * Creates a new {@code href} attribute with the specified value.
     *
     * @param value the string value of the attribute
     *
     * @return a new attribute
     */
    public final Href href(String value) {
      return new Href(value);
    }

    /**
     * Creates a new {@code prefix} attribute with the specified value.
     *
     * @param value the string value of the attribute
     *
     * @return a new attribute
     */
    public final Prefix prefix(String value) {
      return new Prefix(value);
    }

    // elements

    public final Html.ElementInstruction header(Header.Component... components) {
      Header pojo;
      pojo = new Header(components);

      return tmpl.header(
          tmpl.className("fixed inset-0px flex h-48px"),
          tmpl.className("border-b border-subtle"),
          tmpl.className("bg-background"),

          pojo.headerName != null ? headerName(pojo.headerName) : tmpl.noop()
      );
    }

    public final HeaderName headerName(HeaderName.Component... components) {
      return new HeaderName(components);
    }

    private Html.ElementInstruction headerName(HeaderName pojo) {
      return tmpl.a(
          tmpl.className("flex h-full select-none items-center"),
          tmpl.className("border-2 border-transparent"),
          tmpl.className("pr-32px pl-16px"),
          tmpl.className("text-body-compact-01 font-semibold"),
          tmpl.className("outline-none"),
          tmpl.className("transition-colors duration-100"),
          tmpl.className("focus:border-focus"),

          pojo.href != null ? tmpl.href(pojo.href) : tmpl.noop(),
          pojo.prefix != null ? tmpl.span(pojo.prefix) : tmpl.noop()
      );
    }

  }

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

  public final Ui ui(Shell shell) {
    Check.notNull(shell, "shell == null");

    return new Ui(shell);
  }

}