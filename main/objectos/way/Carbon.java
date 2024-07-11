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

  /**
   * The White theme.
   */
  public static final Theme WHITE = CarbonTheme.WHITE;

  /**
   * The Gray 10 theme.
   */
  public static final Theme G10 = CarbonTheme.G10;

  /**
   * The Gray 90 theme.
   */
  public static final Theme G90 = CarbonTheme.G90;

  /**
   * The Gray 100 theme.
   */
  public static final Theme G100 = CarbonTheme.G100;

  // attributes

  sealed static abstract class BooleanAttribute {
    private final boolean value;

    BooleanAttribute(boolean value) {
      this.value = value;
    }

    public final boolean value() {
      return value;
    }
  }

  sealed static abstract class StringAttribute {
    private final String value;

    StringAttribute(String value) {
      this.value = Check.notNull(value, "value == null");
    }

    public final String value() {
      return value;
    }
  }

  /**
   * Carbon {@code href} attribute.
   */
  public static final class Href extends StringAttribute implements HeaderMenuItem.Component, HeaderName.Component {
    Href(String value) { super(value); }
  }

  public static final class IsActive extends BooleanAttribute implements HeaderMenuItem.Component {
    IsActive(boolean value) { super(value); }
  }

  /**
   * Carbon {@code name} attribute.
   */
  public static final class Name extends StringAttribute implements HeaderMenuItem.Component {
    Name(String value) { super(value); }
  }

  /**
   * Carbon UI theme.
   */
  public sealed interface Theme extends Header.Component permits CarbonTheme {}

  // elements

  /**
   * Carbon UI shell header.
   */
  public sealed interface Header permits CarbonUi.CarbonHeader {
    /**
     * An UI shell header component.
     */
    public sealed interface Component {}
  }

  /**
   * An UI shell header menu item.
   */
  public sealed interface HeaderMenuItem extends HeaderNavigation.Component permits CarbonUi.CarbonHeaderMenuItem {
    /**
     * An UI shell header menu item component.
     */
    public sealed interface Component {}
  }

  /**
   * An UI shell header name.
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

  /**
   * A navigation section of a UI shell header.
   */
  public sealed interface HeaderNavigation extends Header.Component permits CarbonUi.CarbonHeaderNavigation {
    /**
     * Attributes and elements that can be nested in a header navigation
     * component.
     */
    public sealed interface Component {}
  }

  // ui builder

  /**
   * The UI builder.
   */
  public sealed interface Ui permits CarbonUi {

    /**
     * Creates a new {@code href} attribute with the specified value.
     *
     * @param value the string value of the attribute
     *
     * @return a new attribute
     */
    Href href(String value);

    IsActive isActive(boolean value);

    Name name(String value);

    // elements

    /**
     * Renders the UI Shell Header component.
     *
     * @param components
     *        the components to be nested in this header
     *
     * @return an HTML instruction
     */
    Html.ElementInstruction header(Header.Component... components);

    /**
     * Declares an UI shell header menu item.
     *
     * @param components
     *        the components to be nested in this menu item
     *
     * @return instructions to render a header menu item
     */
    HeaderMenuItem headerMenuItem(HeaderMenuItem.Component... components);

    HeaderName headerName(HeaderName.Component... components);

    HeaderNameText headerNameText(String prefix, String text);

    /**
     * Declares an UI shell header navigation section.
     *
     * @param components
     *        the components to be nested in this navigation section
     *
     * @return instructions to render a header navigation section
     */
    HeaderNavigation headerNavigation(HeaderNavigation.Component... components);

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

    return new CarbonUi(shell);
  }

  final Ui ui(Html.Template tmpl) {
    return new CarbonUi(tmpl);
  }

}