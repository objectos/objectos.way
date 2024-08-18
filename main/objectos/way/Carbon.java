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
import java.util.function.Predicate;
import objectos.lang.object.Check;
import objectos.way.Carbon.Size.ExtraSmall;
import objectos.way.Carbon.Size.Max;

/**
 * The <strong>Objectos Carbon UI</strong> main class.
 *
 * <p>
 * Objectos Carbon UI is an implementation of the Carbon Design System in pure
 * Java.
 */
public final class Carbon extends CarbonComponents {

  //
  // Components and attributes
  //

  /**
   * A screen width breakpoint for responsive design.
   */
  public sealed interface Breakpoint {}

  /**
   * A button variant.
   */
  public sealed interface ButtonVariant {}

  /**
   * A button size.
   */
  public sealed interface ButtonSize {}

  record CarbonButtonVariant(int index) implements ButtonVariant {}

  /**
   * A data table size.
   */
  public sealed interface DataTableSize {}

  /**
   * An icon.
   */
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
   * A Carbon link style.
   */
  public sealed interface LinkStyle {}

  /**
   * The standard link style: no text decoration and the visited links are
   * rendered the same as the not-visited ones.
   */
  public static final LinkStyle LINK_STANDARD = CarbonLinkStyle.STANDARD;

  /**
   * The visited link style: no text decoration and the visited links are
   * rendered with a color that is distinct of the not-visited ones.
   */
  public static final LinkStyle LINK_VISITED = CarbonLinkStyle.VISITED;

  /**
   * The inline link style: has text decoration and the visited links are
   * rendered the same as the not-visited ones.
   */
  public static final LinkStyle LINK_INLINE = CarbonLinkStyle.INLINE;

  /**
   * The inline-visited link style: has text decoration and the visited links
   * are rendered with a color that is distinct of the not-visited ones.
   */
  public static final LinkStyle LINK_INLINE_VISITED = CarbonLinkStyle.INLINE_VISITED;

  enum CarbonLinkStyle implements LinkStyle {
    STANDARD,

    VISITED,

    INLINE,

    INLINE_VISITED;
  }

  public sealed interface MenuElement {}

  public sealed interface MenuLink extends MenuElement {}

  record CarbonMenuLink(String text, String href, boolean active, Script.Action onClick) implements MenuLink {}

  public static MenuLink menuLink(String text, String href, boolean active) {
    Check.notNull(text, "text == null");
    Check.notNull(href, "href == null");

    return new CarbonMenuLink(text, href, active, null);
  }

  public static MenuLink menuLink(String text, String href, boolean active, Script.Action onClick) {
    Check.notNull(text, "text == null");
    Check.notNull(href, "href == null");
    Check.notNull(onClick, "onClick == null");

    return new CarbonMenuLink(text, href, active, onClick);
  }

  public static MenuLink menuLink(String text, String href, Predicate<String> activePredicate) {
    Check.notNull(text, "text == null");
    Check.notNull(href, "href == null");

    boolean active;
    active = activePredicate.test(href);

    return new CarbonMenuLink(text, href, active, null);
  }

  public static MenuLink menuLink(String text, String href, Predicate<String> activePredicate, Script.Action onClick) {
    Check.notNull(text, "text == null");
    Check.notNull(href, "href == null");
    Check.notNull(onClick, "onClick == null");

    boolean active;
    active = activePredicate.test(href);

    return new CarbonMenuLink(text, href, active, onClick);
  }

  /**
   * A Carbon configuration option.
   */
  public sealed interface Option {}

  /**
   * A Carbon size variant.
   */
  public sealed interface Size extends Breakpoint, ButtonSize, DataTableSize {

    /**
     * The Extra Small size variant.
     */
    public sealed interface ExtraSmall extends DataTableSize {}

    /**
     * The Max size variant.
     */
    public sealed interface Max extends Breakpoint, ButtonSize {}

  }

  record CarbonSize(int index) implements ExtraSmall, Size, Max {}

  static final Size NONE = new CarbonSize(0);

  /**
   * The Extra Small size.
   */
  public static final Size.ExtraSmall XS = new CarbonSize(0);

  /**
   * The Small size.
   */
  public static final Size SM = new CarbonSize(1);

  /**
   * The Medium size.
   */
  public static final Size MD = new CarbonSize(2);

  /**
   * The Large size.
   */
  public static final Size LG = new CarbonSize(3);

  /**
   * The Extra Large size.
   */
  public static final Size XL = new CarbonSize(4);

  /**
   * The Max size.
   */
  public static final Size.Max MAX = new CarbonSize(5);

  /**
   * A Carbon spacing token.
   */
  public sealed interface Spacing {}

  record CarbonSpacing(int value) implements Spacing {}

  /**
   * The Carbon {@code spacing-01} token.
   */
  public static final Spacing SPACING_01 = new CarbonSpacing(1);

  /**
   * The Carbon {@code spacing-02} token.
   */
  public static final Spacing SPACING_02 = new CarbonSpacing(2);

  /**
   * The Carbon {@code spacing-03} token.
   */
  public static final Spacing SPACING_03 = new CarbonSpacing(3);

  /**
   * The Carbon {@code spacing-04} token.
   */
  public static final Spacing SPACING_04 = new CarbonSpacing(4);

  /**
   * The Carbon {@code spacing-05} token.
   */
  public static final Spacing SPACING_05 = new CarbonSpacing(5);

  /**
   * The Carbon {@code spacing-06} token.
   */
  public static final Spacing SPACING_06 = new CarbonSpacing(6);

  /**
   * The Carbon {@code spacing-07} token.
   */
  public static final Spacing SPACING_07 = new CarbonSpacing(7);

  /**
   * The Carbon {@code spacing-08} token.
   */
  public static final Spacing SPACING_08 = new CarbonSpacing(8);

  /**
   * The Carbon {@code spacing-09} token.
   */
  public static final Spacing SPACING_09 = new CarbonSpacing(9);

  /**
   * The Carbon {@code spacing-10} token.
   */
  public static final Spacing SPACING_10 = new CarbonSpacing(10);

  /**
   * The Carbon {@code spacing-11} token.
   */
  public static final Spacing SPACING_11 = new CarbonSpacing(11);

  /**
   * The Carbon {@code spacing-12} token.
   */
  public static final Spacing SPACING_12 = new CarbonSpacing(12);

  /**
   * The Carbon {@code spacing-13} token.
   */
  public static final Spacing SPACING_13 = new CarbonSpacing(13);

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
   * The {@code white} Carbon theme.
   */
  public static final Html.ClassName THEME_WHITE = Html.className("theme-white");

  /**
   * The {@code g10} Carbon theme.
   */
  public static final Html.ClassName THEME_G10 = Html.className("theme-g10");

  /**
   * The {@code g90} Carbon theme.
   */
  public static final Html.ClassName THEME_G90 = Html.className("theme-g90");

  /**
   * The {@code g100} Carbon theme.
   */
  public static final Html.ClassName THEME_G100 = Html.className("theme-g100");

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

  Carbon(Html.TemplateBase tmpl) {
    super(tmpl);
  }

  /**
   * Creates a new carbon HTTP module with the specified configuration options.
   *
   * @return a newly created HTTP module
   */
  public static Http.Module createHttpModule(Option... options) {
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

}