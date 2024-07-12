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
import objectos.way.CarbonUi.HeaderMenuButtonPojo;
import objectos.way.CarbonUi.HeaderMenuItemPojo;
import objectos.way.CarbonUi.HeaderNamePojo;
import objectos.way.CarbonUi.HeaderNameTextPojo;
import objectos.way.CarbonUi.HeaderNavigationPojo;

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

  public enum Icon {

    MENU;

  }

  public enum IconSize {

    PX16,

    PX20,

    PX24,

    PX32;

  }

  // attributes

  /**
   * The nested types of this interface represent the Carbon UI attributes.
   */
  public sealed interface Attribute {

    /**
     * Carbon {@code aria-label} attribute.
     */
    public sealed interface AriaLabel
        extends
        ChildOf.HeaderMenuButton
        permits CarbonUi.AriaLabelAttribute {}

    /**
     * Carbon {@code href} attribute.
     */
    public sealed interface Href
        extends
        ChildOf.HeaderMenuItem,
        ChildOf.HeaderName
        permits CarbonUi.HrefAttribute {}

    /**
     * Carbon {@code isActive} attribute.
     */
    public sealed interface IsActive
        extends
        ChildOf.HeaderMenuItem
        permits CarbonUi.IsActiveAttribute {}

    /**
     * Carbon {@code name} attribute.
     */
    public sealed interface Name
        extends
        ChildOf.HeaderMenuItem
        permits CarbonUi.NameAttribute {}

  }

  /**
   * A nested type of this interface can be used as a child of the corresponding
   * component.
   */
  public sealed interface ChildOf {

    /**
     * Accepted as a child of the UI shell header component.
     */
    public sealed interface Header
        permits
        HeaderMenuButtonPojo,
        HeaderNamePojo,
        HeaderNavigationPojo,
        Theme {}

    /**
     * Accepted as a child of the UI shell header menu button component.
     */
    public sealed interface HeaderMenuButton {}

    /**
     * Accepted as a child of the UI shell header menu item component.
     */
    public sealed interface HeaderMenuItem {}

    /**
     * Accepted as a child of the UI shell header name component.
     */
    public sealed interface HeaderName
        permits
        HeaderNameTextPojo,
        Attribute.Href {}

    /**
     * Accepted as a child of the UI shell header navigation component.
     */
    public sealed interface HeaderNavigation
        permits
        HeaderMenuItemPojo {}

  }

  public sealed interface Component {}

  @SuppressWarnings("unused")
  private static final class NoImpl implements Attribute, ChildOf, Component {}

  /**
   * The UI builder.
   */
  public sealed interface Ui permits CarbonUi {

    /**
     * Creates a new {@code aria-label} attribute with the specified value.
     *
     * @param value the string value of the attribute
     *
     * @return a new attribute
     */
    Attribute.AriaLabel ariaLabel(String value);

    /**
     * Creates a new {@code href} attribute with the specified value.
     *
     * @param value the string value of the attribute
     *
     * @return a new attribute
     */
    Attribute.Href href(String value);

    Attribute.IsActive isActive(boolean value);

    Attribute.Name name(String value);

    // elements

    /**
     * Renders the UI shell content container.
     *
     * @param fragment
     *        the fragment to render
     *
     * @return an HTML instruction
     */
    Html.ElementInstruction content(Html.FragmentLambda fragment);

    /**
     * Renders the UI shell Header component.
     *
     * @param components
     *        the nested components
     *
     * @return an HTML instruction
     */
    Html.ElementInstruction header(ChildOf.Header... components);

    /**
     * Declares an UI shell header menu button.
     *
     * @param components
     *        the nested components
     *
     * @return instructions to render a header menu button
     */
    ChildOf.Header headerMenuButton(ChildOf.HeaderMenuButton... components);

    /**
     * Declares an UI shell header menu item.
     *
     * @param components
     *        the nested components
     *
     * @return instructions to render a header menu item
     */
    ChildOf.HeaderNavigation headerMenuItem(ChildOf.HeaderMenuItem... components);

    ChildOf.Header headerName(ChildOf.HeaderName... components);

    ChildOf.HeaderName headerNameText(String prefix, String text);

    /**
     * Declares an UI shell header navigation section.
     *
     * @param components
     *        the nested components
     *
     * @return instructions to render a header navigation section
     */
    ChildOf.Header headerNavigation(ChildOf.HeaderNavigation... components);

  }

  /**
   * Carbon UI theme.
   */
  public sealed interface Theme extends ChildOf.Header permits CarbonTheme {}

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