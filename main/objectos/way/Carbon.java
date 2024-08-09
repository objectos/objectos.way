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
import java.util.Set;
import objectos.lang.object.Check;
import objectos.way.Carbon.Component.ProgressIndicator;

/**
 * The <strong>Objectos Carbon UI</strong> main class.
 *
 * <p>
 * Objectos Carbon UI is an implementation of the Carbon Design System in pure
 * Java.
 */
public final class Carbon extends CarbonClasses {

  //
  // Components
  //

  /**
   * An UI component.
   */
  public sealed interface Component {

    public sealed interface ProgressIndicator extends Component permits CarbonProgressIndicator {

      public sealed interface Step extends Component permits CarbonProgressIndicator {

        Step step(String title, String description);

        Step step(String title, String description, String optionalLabel);

        Step enabled(boolean value);

        Step valid(boolean value);

      }

      ProgressIndicator currentIndex(int value);

      ProgressIndicator vertical();

      Step step(String title, String description);

      Step step(String title, String description, String optionalLabel);

    }

    Html.ElementInstruction render();

  }

  /**
   * UI component: UI shell header.
   */
  public sealed interface Header extends Component permits CarbonHeader {

    /**
     * UI component: mobile header navigation close button.
     */
    public sealed interface CloseButton extends Component permits CarbonHeaderCloseButton {

      /**
       * Sets the auxiliary description to the specified value.
       *
       * @param value
       *        the new value
       *
       * @return a reference to this object
       */
      CloseButton description(String value);

    }

    /**
     * UI component: mobile header navigation menu button.
     */
    public sealed interface MenuButton extends Component permits CarbonHeaderMenuButton {

      /**
       * Sets the auxiliary description to the specified value.
       *
       * @param value
       *        the new value
       *
       * @return a reference to this object
       */
      MenuButton description(String value);

    }

    /**
     * A header navigation menu item.
     */
    public sealed interface MenuItem {}

    /**
     * UI component: the header name.
     */
    public sealed interface Name extends Component permits CarbonHeaderName {

      Name href(String value);

      Name prefix(String value);

      Name text(String value);

    }

    /**
     * UI component: the header top-level navigation.
     */
    public sealed interface Navigation extends Component permits CarbonHeaderNavigation {

      /**
       * Adds all of the specified items to this header navigation.
       *
       * @param values
       *        the items to add
       *
       * @return a reference to this object
       */
      Navigation addItems(Iterable<MenuItem> values);

      /**
       * Sets the {@code data-frame} attribute to the specified name:value pair.
       *
       * @param name
       *        the name of the frame
       * @param value
       *        the value of the frame
       *
       * @return a reference to this object
       */
      Navigation dataFrame(String name, String value);

      /**
       * Sets the auxiliary description to the specified value.
       *
       * @param value
       *        the new value
       *
       * @return a reference to this object
       */
      Navigation description(String value);

      /**
       * Use expressive type sets.
       *
       * @return a reference to this object
       */
      Navigation expressive();

    }

    /**
     * Sets the auxiliary description to the specified value.
     *
     * @param value
     *        the new value
     *
     * @return a reference to this object
     */
    Header description(String value);

    /**
     * Sets the close button to the specified value.
     *
     * @param value
     *        the new value
     *
     * @return a reference to this object
     */
    Header closeButton(CloseButton value);

    /**
     * Sets the menu button to the specified value.
     *
     * @param value
     *        the new value
     *
     * @return a reference to this object
     */
    Header menuButton(MenuButton value);

    /**
     * Sets the header name to the specified value.
     *
     * @param value
     *        the new value
     *
     * @return a reference to this object
     */
    Header name(Name value);

    /**
     * Sets the header navigation to the specified value.
     *
     * @param value
     *        the new value
     *
     * @return a reference to this object
     */
    Header navigation(Navigation value);

    /**
     * Sets the theme to the specified value.
     *
     * @param value
     *        the new value
     *
     * @return a reference to this object
     */
    Header theme(Theme value);

  }

  /**
   * Creates a new header component.
   *
   * @return a newly created component instance
   */
  public final Header header() {
    return new CarbonHeader(tmpl);
  }

  /**
   * Creates a new header close button component.
   *
   * @return a newly created component instance
   */
  public final Header.CloseButton headerCloseButton() {
    return new CarbonHeaderCloseButton(tmpl);
  }

  /**
   * Creates a new header menu button component.
   *
   * @return a newly created component instance
   */
  public final Header.MenuButton headerMenuButton() {
    return new CarbonHeaderMenuButton(tmpl);
  }

  record CarbonHeaderMenuItem(String text, String href, boolean active) implements Header.MenuItem {}

  /**
   * Creates a new header navigation menu item with the specified values.
   *
   * @param text
   *        the text contents of the menu item
   * @param href
   *        the {@code href} value of the menu item
   * @param active
   *        whether the menu item is active
   *
   * @return a newly created header menu item instance
   */
  public final Header.MenuItem headerMenuItem(String text, String href, boolean active) {
    Check.notNull(text, "text == null");
    Check.notNull(href, "href == null");

    return new CarbonHeaderMenuItem(text, href, active);
  }

  public final Header.Name headerName() {
    return new CarbonHeaderName(tmpl);
  }

  /**
   * Creates a new header navigation component.
   *
   * @return a newly created component instance
   */
  public final Header.Navigation headerNavigation() {
    return new CarbonHeaderNavigation(tmpl);
  }

  public enum Icon {

    // @formatter:off

    ADD("""
    <polygon points="17,15 17,8 15,8 15,15 8,15 8,17 15,17 15,24 17,24 17,17 24,17 24,15 "/>"""),

    ADD_LARGE("""
    <polygon points="17 15 17 5 15 5 15 15 5 15 5 17 15 17 15 27 17 27 17 17 27 17 27 15 17 15"/>"""),

    CLOSE("""
    <polygon points="17.4141 16 24 9.4141 22.5859 8 16 14.5859 9.4143 8 8 9.4141 14.5859 16 8 22.5859 9.4143 24 16 17.4141 22.5859 24 24 22.5859 17.4141 16"/>"""),

    LOGO_GITHUB("""
    <path d="M16,2a14,14,0,0,0-4.43,27.28c.7.13,1-.3,1-.67s0-1.21,0-2.38c-3.89.84-4.71-1.88-4.71-1.88A3.71,3.71,0,0,0,6.24,22.3c-1.27-.86.1-.85.1-.85A2.94,2.94,0,0,1,8.48,22.9a3,3,0,0,0,4.08,1.16,2.93,2.93,0,0,1,.88-1.87c-3.1-.36-6.37-1.56-6.37-6.92a5.4,5.4,0,0,1,1.44-3.76,5,5,0,0,1,.14-3.7s1.17-.38,3.85,1.43a13.3,13.3,0,0,1,7,0c2.67-1.81,3.84-1.43,3.84-1.43a5,5,0,0,1,.14,3.7,5.4,5.4,0,0,1,1.44,3.76c0,5.38-3.27,6.56-6.39,6.91a3.33,3.33,0,0,1,.95,2.59c0,1.87,0,3.38,0,3.84s.25.81,1,.67A14,14,0,0,0,16,2Z"/>"""),

    LOGO_LINKEDIN("""
    <path d="M26.2,4H5.8C4.8,4,4,4.8,4,5.7v20.5c0,0.9,0.8,1.7,1.8,1.7h20.4c1,0,1.8-0.8,1.8-1.7V5.7C28,4.8,27.2,4,26.2,4z M11.1,24.4 H7.6V13h3.5V24.4z M9.4,11.4c-1.1,0-2.1-0.9-2.1-2.1c0-1.2,0.9-2.1,2.1-2.1c1.1,0,2.1,0.9,2.1,2.1S10.5,11.4,9.4,11.4z M24.5,24.3 H21v-5.6c0-1.3,0-3.1-1.9-3.1c-1.9,0-2.1,1.5-2.1,2.9v5.7h-3.5V13h3.3v1.5h0.1c0.5-0.9,1.7-1.9,3.4-1.9c3.6,0,4.3,2.4,4.3,5.5V24.3z"/>"""),

    MENU("""
    <rect x="4" y="6" width="24" height="2"/><rect x="4" y="24" width="24" height="2"/><rect x="4" y="12" width="24" height="2"/><rect x="4" y="18" width="24" height="2"/>"""),

    TRASH_CAN("""
    <rect x="12" y="12" width="2" height="12"/><rect x="18" y="12" width="2" height="12"/><path d="M4,6V8H6V28a2,2,0,0,0,2,2H24a2,2,0,0,0,2-2V8h2V6ZM8,28V8H24V28Z"/><rect x="12" y="2" width="8" height="2"/>""");

    // @formatter:on

    final String raw;

    private Icon(String raw) {
      this.raw = raw;
    }

  }

  /**
   * A Carbon configuration option.
   */
  public sealed interface Option {}

  /**
   * The UI shell is the top-level UI component of an web application.
   */
  public static abstract class Template extends CarbonTemplate {

    /**
     * Sole constructor.
     *
     * @param http
     *        the HTTP exchange
     */
    protected Template(Http.Exchange http) {
      super(http);
    }

  }

  /**
   * A Carbon UI theme.
   */
  public sealed interface Theme extends Html.ClassName {}

  private record CarbonTheme(String value) implements Theme {}

  /**
   * The {@code white} Carbon theme.
   */
  public static final Theme THEME_WHITE = new CarbonTheme("theme-white");

  /**
   * The {@code g10} Carbon theme.
   */
  public static final Theme THEME_G10 = new CarbonTheme("theme-g10");

  /**
   * The {@code g90} Carbon theme.
   */
  public static final Theme THEME_G90 = new CarbonTheme("theme-g90");

  /**
   * The {@code g100} Carbon theme.
   */
  public static final Theme THEME_G100 = new CarbonTheme("theme-g100");

  //
  // non-public types
  //

  private static final class Builder extends Web.Module {

    Set<Class<?>> classes = Set.of();

    private byte[] script;

    public final Http.Module build() {
      try {
        script = Script.getBytes();
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }

      return this;
    }

    final void classes(Set<Class<?>> value) {
      classes = value;
    }

    @Override
    protected final void configure() {
      route("/ui/script.js", GET(this::script));

      CarbonStyles styles;
      styles = new CarbonStyles(classes);

      route("/ui/carbon.css", GET(styles));
    }

    private void script(Http.Exchange http) {
      http.status(Http.OK);

      http.dateNow();

      http.header(Http.CONTENT_TYPE, "text/javascript; charset=utf-8");

      http.header(Http.CONTENT_LENGTH, script.length);

      http.send(script);
    }

  }

  private non-sealed static abstract class CarbonOption implements Option {

    abstract void acceptBuilder(Builder builder);

  }

  private final Html.TemplateBase tmpl;

  Carbon(Html.TemplateBase tmpl) {
    this.tmpl = tmpl;
  }

  /**
   * Creates a new carbon HTTP module with the specified configuration options.
   *
   * @return a newly created HTTP module
   */
  public static Http.Module create(Option... options) {
    Builder builder;
    builder = new Builder();

    for (int i = 0; i < options.length; i++) {
      Option o;
      o = Check.notNull(options[i], "options[", i, "] == null");

      CarbonOption option;
      option = (CarbonOption) o;

      option.acceptBuilder(builder);
    }

    return builder.build();
  }

  public static Option classes(Class<?>... classesToScan) {
    Set<Class<?>> set;
    set = Set.of(classesToScan);

    return new CarbonOption() {
      @Override
      final void acceptBuilder(Builder builder) {
        builder.classes(set);
      }
    };
  }

  public static Script.Action openTearsheet(Html.Id id) {
    return Script.actions(
        Script.replaceClass(id, "tearsheet-hidden", "tearsheet"),
        Script.setProperty(id, "aria-hidden", "false"),
        Script.delay(50, Script.addClass(id, "tearsheet-transition"))
    );
  }

  public final ProgressIndicator progressIndicator() {
    return new CarbonProgressIndicator(tmpl);
  }

  static Script.Action joinIf(Script.Action existing, Script.Action value) {
    Script.Action a;
    a = Check.notNull(value, "value == null");

    if (existing == null) {
      return a;
    } else {
      return Script.join(existing, a);
    }
  }

  static Html.ElementInstruction icon16(Html.TemplateBase tmpl, Icon icon, Html.AttributeInstruction... attributes) {
    return icon(tmpl, icon, "1rem", attributes);
  }

  static Html.ElementInstruction icon20(Html.TemplateBase tmpl, Icon icon, Html.AttributeInstruction... attributes) {
    return icon(tmpl, icon, "1.25rem", attributes);
  }

  static Html.ElementInstruction icon24(Html.TemplateBase tmpl, Icon icon, Html.AttributeInstruction... attributes) {
    return icon(tmpl, icon, "1.5rem", attributes);
  }

  static Html.ElementInstruction icon32(Html.TemplateBase tmpl, Icon icon, Html.AttributeInstruction... attributes) {
    return icon(tmpl, icon, "2rem", attributes);
  }

  private static Html.ElementInstruction icon(Html.TemplateBase tmpl, Icon icon, String size, Html.AttributeInstruction... attributes) {
    return tmpl.svg(
        tmpl.xmlns("http://www.w3.org/2000/svg"),
        tmpl.fill("currentColor"),
        tmpl.width(size), tmpl.height(size), tmpl.viewBox("0 0 32 32"),

        tmpl.flatten(attributes),

        tmpl.raw(icon.raw)
    );
  }

}