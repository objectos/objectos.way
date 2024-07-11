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

  public static final Theme WHITE = CarbonTheme.WHITE;

  public static final Theme G10 = CarbonTheme.G10;

  public static final Theme G90 = CarbonTheme.G90;

  public static final Theme G100 = CarbonTheme.G100;

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
   * A Carbon UI theme.
   */
  public sealed interface Theme extends Header.Component permits CarbonTheme {}

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

    CarbonTheme theme;

    Header(Component[] components) {
      for (Component c : components) { // implicit null check
        switch (c) {
          case HeaderName o -> headerName = o;

          case CarbonTheme o -> theme = o;
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

    HeaderNameText text;

    HeaderName(Component[] components) {
      for (Component c : components) { // implicit null check
        switch (c) {
          case Href o -> href = o.value();

          case HeaderNameText o -> text = o;
        }
      }
    }
  }

  public static final class HeaderNameText implements HeaderName.Component {

    final String prefix;

    final String text;

    HeaderNameText(String prefix, String text) {
      this.prefix = prefix;
      this.text = text;
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

    // elements

    public final Html.ElementInstruction header(Header.Component... components) {
      Header pojo;
      pojo = new Header(components);

      return tmpl.header(
          tmpl.className("fixed inset-0px flex h-48px"),
          tmpl.className("border-b border-subtle"),
          tmpl.className("bg-background"),

          pojo.theme != null ? tmpl.className(pojo.theme.className) : tmpl.noop(),

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
          pojo.text != null
              ? tmpl.flatten(
                  tmpl.span(
                      tmpl.className("font-normal"),

                      tmpl.t(pojo.text.prefix)
                  ),
                  tmpl.raw("&nbsp;"),
                  tmpl.t(pojo.text.text)
              )
              : tmpl.noop()
      );
    }

    public final HeaderNameText headerNameText(String prefix, String text) {
      Check.notNull(prefix, "prefix == null");
      Check.notNull(text, "text == null");

      return new HeaderNameText(prefix, text);
    }

  }

  /**
   * The UI shell is the top level UI component of an web application.
   */
  public static abstract class Shell extends CarbonShell {

    /**
     * Sole constructor.
     *
     * @param http
     *        the HTTP exchange
     */
    protected Shell(Http.Exchange http) {
      super(http);
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

  final Ui ui(Html.Template tmpl) {
    return new Ui(tmpl);
  }

}