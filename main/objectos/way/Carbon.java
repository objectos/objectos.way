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
import objectos.way.Html.ElementName;

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

    ARROW_RIGHT("""
    <polygon points="18 6 16.57 7.393 24.15 15 4 15 4 17 24.15 17 16.57 24.573 18 26 28 16 18 6"/>"""),

    CHECKMARK_OUTLINE("""
    <path d="M14 21.414L9 16.413 10.413 15 14 18.586 21.585 11 23 12.415 14 21.414z"/><path d="M16,2A14,14,0,1,0,30,16,14,14,0,0,0,16,2Zm0,26A12,12,0,1,1,28,16,12,12,0,0,1,16,28Z"/>"""),

    CIRCLE_DASH("""
    <path d="M7.7 4.7a14.7 14.7 0 00-3 3.1L6.3 9A13.26 13.26 0 018.9 6.3zM4.6 12.3l-1.9-.6A12.51 12.51 0 002 16H4A11.48 11.48 0 014.6 12.3zM2.7 20.4a14.4 14.4 0 002 3.9l1.6-1.2a12.89 12.89 0 01-1.7-3.3zM7.8 27.3a14.4 14.4 0 003.9 2l.6-1.9A12.89 12.89 0 019 25.7zM11.7 2.7l.6 1.9A11.48 11.48 0 0116 4V2A12.51 12.51 0 0011.7 2.7zM24.2 27.3a15.18 15.18 0 003.1-3.1L25.7 23A11.53 11.53 0 0123 25.7zM27.4 19.7l1.9.6A15.47 15.47 0 0030 16H28A11.48 11.48 0 0127.4 19.7zM29.2 11.6a14.4 14.4 0 00-2-3.9L25.6 8.9a12.89 12.89 0 011.7 3.3zM24.1 4.6a14.4 14.4 0 00-3.9-2l-.6 1.9a12.89 12.89 0 013.3 1.7zM20.3 29.3l-.6-1.9A11.48 11.48 0 0116 28v2A21.42 21.42 0 0020.3 29.3z"/>"""),

    CLOSE("""
    <polygon points="17.4141 16 24 9.4141 22.5859 8 16 14.5859 9.4143 8 8 9.4141 14.5859 16 8 22.5859 9.4143 24 16 17.4141 22.5859 24 24 22.5859 17.4141 16"/>"""),

    INCOMPLETE("""
    <path d="M23.7642 6.8593l1.2851-1.5315A13.976 13.976 0 0020.8672 2.887l-.6836 1.8776A11.9729 11.9729 0 0123.7642 6.8593zM27.81 14l1.9677-.4128A13.8888 13.8888 0 0028.14 9.0457L26.4087 10A12.52 12.52 0 0127.81 14zM20.1836 27.2354l.6836 1.8776a13.976 13.976 0 004.1821-2.4408l-1.2851-1.5315A11.9729 11.9729 0 0120.1836 27.2354zM26.4087 22L28.14 23a14.14 14.14 0 001.6382-4.5872L27.81 18.0659A12.1519 12.1519 0 0126.4087 22zM16 30V2a14 14 0 000 28z"/>"""),

    LOGO_GITHUB("""
    <path d="M16,2a14,14,0,0,0-4.43,27.28c.7.13,1-.3,1-.67s0-1.21,0-2.38c-3.89.84-4.71-1.88-4.71-1.88A3.71,3.71,0,0,0,6.24,22.3c-1.27-.86.1-.85.1-.85A2.94,2.94,0,0,1,8.48,22.9a3,3,0,0,0,4.08,1.16,2.93,2.93,0,0,1,.88-1.87c-3.1-.36-6.37-1.56-6.37-6.92a5.4,5.4,0,0,1,1.44-3.76,5,5,0,0,1,.14-3.7s1.17-.38,3.85,1.43a13.3,13.3,0,0,1,7,0c2.67-1.81,3.84-1.43,3.84-1.43a5,5,0,0,1,.14,3.7,5.4,5.4,0,0,1,1.44,3.76c0,5.38-3.27,6.56-6.39,6.91a3.33,3.33,0,0,1,.95,2.59c0,1.87,0,3.38,0,3.84s.25.81,1,.67A14,14,0,0,0,16,2Z"/>"""),

    LOGO_LINKEDIN("""
    <path d="M26.2,4H5.8C4.8,4,4,4.8,4,5.7v20.5c0,0.9,0.8,1.7,1.8,1.7h20.4c1,0,1.8-0.8,1.8-1.7V5.7C28,4.8,27.2,4,26.2,4z M11.1,24.4 H7.6V13h3.5V24.4z M9.4,11.4c-1.1,0-2.1-0.9-2.1-2.1c0-1.2,0.9-2.1,2.1-2.1c1.1,0,2.1,0.9,2.1,2.1S10.5,11.4,9.4,11.4z M24.5,24.3 H21v-5.6c0-1.3,0-3.1-1.9-3.1c-1.9,0-2.1,1.5-2.1,2.9v5.7h-3.5V13h3.3v1.5h0.1c0.5-0.9,1.7-1.9,3.4-1.9c3.6,0,4.3,2.4,4.3,5.5V24.3z"/>"""),

    MENU("""
    <rect x="4" y="6" width="24" height="2"/><rect x="4" y="24" width="24" height="2"/><rect x="4" y="12" width="24" height="2"/><rect x="4" y="18" width="24" height="2"/>"""),

    TRASH_CAN("""
    <rect x="12" y="12" width="2" height="12"/><rect x="18" y="12" width="2" height="12"/><path d="M4,6V8H6V28a2,2,0,0,0,2,2H24a2,2,0,0,0,2-2V8h2V6ZM8,28V8H24V28Z"/><rect x="12" y="2" width="8" height="2"/>"""),

    WARNING("""
    <path d="M16,2A14,14,0,1,0,30,16,14,14,0,0,0,16,2Zm0,26A12,12,0,1,1,28,16,12,12,0,0,1,16,28Z"/><path d="M15 8H17V19H15zM16 22a1.5 1.5 0 101.5 1.5A1.5 1.5 0 0016 22z"/>""");

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

  enum CarbonPlane implements ProgressIndicatorVariant {
    HORIZONTAL,

    VERTICAL;
  }

  /**
   * The horizontal plane.
   */
  public static final ProgressIndicatorVariant HORIZONTAL = CarbonPlane.HORIZONTAL;

  /**
   * The vertical plane.
   */
  public static final ProgressIndicatorVariant VERTICAL = CarbonPlane.VERTICAL;

  /**
   * A Carbon progress indicator variant.
   */
  public sealed interface ProgressIndicatorVariant {}

  /**
   * A Carbon progress step.
   */
  public sealed interface ProgressStep permits CarbonProgressStep {

    ProgressStep secondaryLabel(String value);

    ProgressStep invalid(boolean value);

  }

  /**
   * A Carbon progress step variant.
   */
  public sealed interface ProgressStepVariant {}

  enum CarbonProgressStepVariant implements ProgressStepVariant {

    STEP_COMPLETE,

    STEP_CURRENT,

    STEP_INCOMPLETE;

  }

  /**
   * The complete step.
   */
  public static final ProgressStepVariant STEP_COMPLETE = CarbonProgressStepVariant.STEP_COMPLETE;

  /**
   * The current step.
   */
  public static final ProgressStepVariant STEP_CURRENT = CarbonProgressStepVariant.STEP_CURRENT;

  /**
   * The incomplete step.
   */
  public static final ProgressStepVariant STEP_INCOMPLETE = CarbonProgressStepVariant.STEP_INCOMPLETE;

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

  /**
   * A Carbon tile variant.
   */
  public sealed interface TileVariant {}

  /**
   * The clickable tile variant.
   */
  public static final TileVariant TILE_CLICKABLE = CarbonTileVariant.TILE_CLICKABLE;

  enum CarbonTileVariant implements TileVariant {
    TILE_CLICKABLE;
  }

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

  private Html.ElementName element;

  private boolean flagIconOnly;

  private Icon icon;

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

  static Html.ElementInstruction renderIcon16(Html.TemplateBase tmpl, Icon icon, Html.Instruction... attributes) {
    return Carbon.renderIcon(tmpl, icon, "1rem", attributes);
  }

  static Html.ElementInstruction renderIcon20(Html.TemplateBase tmpl, Icon icon, Html.Instruction... attributes) {
    return Carbon.renderIcon(tmpl, icon, "1.25rem", attributes);
  }

  static Html.ElementInstruction renderIcon24(Html.TemplateBase tmpl, Icon icon, Html.Instruction... attributes) {
    return Carbon.renderIcon(tmpl, icon, "1.5rem", attributes);
  }

  static Html.ElementInstruction renderIcon32(Html.TemplateBase tmpl, Icon icon, Html.Instruction... attributes) {
    return Carbon.renderIcon(tmpl, icon, "2rem", attributes);
  }

  private static Html.ElementInstruction renderIcon(Html.TemplateBase tmpl, Icon icon, String size, Html.Instruction... attributes) {
    return tmpl.svg(
        tmpl.xmlns("http://www.w3.org/2000/svg"),
        tmpl.fill("currentColor"),
        tmpl.width(size), tmpl.height(size), tmpl.viewBox("0 0 32 32"),

        tmpl.flatten(attributes),

        tmpl.raw(icon.raw)
    );
  }

  //
  // Components
  //

  static final Html.AttributeObject ARIA_HIDDEN_TRUE = Html.attribute(HtmlAttributeName.ARIA_HIDDEN, "true");

  static final Html.AttributeObject ROLE_DIALOG = Html.attribute(HtmlAttributeName.ROLE, "dialog");

  static final Html.AttributeObject ROLE_PRESENTATION = Html.attribute(HtmlAttributeName.ROLE, "presentation");

  public final Html.NoOpInstruction iconOnly() {
    flagIconOnly = true;

    return Html.NOOP;
  }

  private boolean readIconOnly() {
    boolean res = flagIconOnly;

    flagIconOnly = false;

    return res;
  }

  public final Html.NoOpInstruction renderAs(Html.ElementName element) {
    this.element = Check.notNull(element, "element == null");

    return Html.NOOP;
  }

  private Html.ElementName readRenderAs(Html.ElementName defaultValue) {
    ElementName result = defaultValue;

    if (element != null) {
      result = element;

      element = null;
    }

    return result;
  }

  public final Html.NoOpInstruction renderIcon(Icon icon) {
    this.icon = Check.notNull(icon, "icon == null");

    return Html.NOOP;
  }

  private Icon readRenderIcon() {
    Icon result = icon;

    icon = null;

    return result;
  }

  //
  // B
  //

  public final Html.ElementInstruction button(Carbon.ButtonVariant variant, Carbon.ButtonSize size, Html.Instruction... contents) {
    Check.notNull(variant, "variant == null");
    Check.notNull(size, "size == null");

    Html.ElementName renderAs;
    renderAs = readRenderAs(Html.ElementName.BUTTON);

    CarbonButtonVariant thisVariant;
    thisVariant = (CarbonButtonVariant) variant;

    CarbonSize thisSize;
    thisSize = (CarbonSize) size;

    Icon icon = readRenderIcon();

    boolean iconOnly = readIconOnly();

    return CarbonButton.button(tmpl, renderAs, thisVariant, thisSize, icon, iconOnly, contents);
  }

  //
  // L
  //

  public final Html.ElementInstruction link(LinkStyle style, String text, String href) {
    Check.notNull(style, "style == null");
    Check.notNull(text, "text == null");
    Check.notNull(href, "href == null");

    return CarbonLink.render(tmpl, style, tmpl.href(href), tmpl.t(text));
  }

  public final Html.ElementInstruction link(LinkStyle style, Html.Instruction... contents) {
    Check.notNull(style, "style == null");

    return CarbonLink.render(tmpl, style, contents);
  }

  //
  // P
  //

  public final Html.ElementInstruction progressIndicator(ProgressIndicatorVariant variant, ProgressStep... steps) {
    Check.notNull(variant, "variant == null");

    boolean horizontal = variant == CarbonPlane.HORIZONTAL;

    Html.Instruction[] instructions = new Html.Instruction[steps.length];

    for (int i = 0; i < steps.length; i++) {
      ProgressStep step = Check.notNull(steps[i], "steps[", i, "] == null");

      CarbonProgressStep carbonStep = (CarbonProgressStep) step;

      instructions[i] = carbonStep.render(horizontal);
    }

    return tmpl.ul(
        horizontal ? CarbonProgressIndicator.HORIZONTAL : CarbonProgressIndicator.VERTICAL,

        tmpl.flatten(instructions)
    );
  }

  public final ProgressStep progressStep(ProgressStepVariant variant, String label) {
    Check.notNull(variant, "variant == null");
    Check.notNull(label, "label == null");

    return new CarbonProgressStep(tmpl, variant, label);
  }

  public final ProgressStep progressStep(ProgressStepVariant variant, String label, String secondaryLabel) {
    Check.notNull(variant, "variant == null");
    Check.notNull(label, "label == null");
    Check.notNull(secondaryLabel, "secondaryLabel == null");

    return new CarbonProgressStep(tmpl, variant, label).secondaryLabel(secondaryLabel);
  }

  //
  // T
  //

  // Tearsheet

  public static Script.Action showTearsheet(Html.Id id) {
    return CarbonTearsheet.showTearsheet(id);
  }

  public static Script.Action showTearsheetModal(Html.Id id) {
    return CarbonTearsheet.showTearsheetModal(id);
  }

  public final Html.ElementInstruction tearsheet(Html.Instruction... contents) {
    return CarbonTearsheet.tearsheet(tmpl, contents);
  }

  public final Html.ElementInstruction tearsheetModal(Html.Instruction... contents) {
    return CarbonTearsheet.tearsheetModal(tmpl, contents);
  }

  public final Html.ElementInstruction tearsheetHeader(Html.Instruction... contents) {
    return CarbonTearsheet.tearsheetHeader(tmpl, contents);
  }

  public final Html.ElementInstruction tearsheetHeaderTitle(String text) {
    return CarbonTearsheet.tearsheetHeaderTitle(tmpl, text);
  }

  public final Html.ElementInstruction tearsheetHeaderDescription(String text) {
    return CarbonTearsheet.tearsheetHeaderDescription(tmpl, text);
  }

  public final Html.ElementInstruction tearsheetBody(Html.Instruction... contents) {
    return CarbonTearsheet.tearsheetBody(tmpl, contents);
  }

  public final Html.ElementInstruction tearsheetInfluencer(Html.Instruction... contents) {
    return CarbonTearsheet.tearsheetInfluencer(tmpl, contents);
  }

  public final Html.ElementInstruction tearsheetRight(Html.Instruction... contents) {
    return CarbonTearsheet.tearsheetRight(tmpl, contents);
  }

  public final Html.ElementInstruction tearsheetMain(Html.Instruction... contents) {
    return CarbonTearsheet.tearsheetMain(tmpl, contents);
  }

  public final Html.ElementInstruction tearsheetActions(Html.Instruction... contents) {
    return CarbonTearsheet.tearsheetActions(tmpl, contents);
  }

  public final Html.ElementInstruction tearsheetCancelAction(String label) {
    return CarbonTearsheet.tearsheetCancelAction(tmpl, label);
  }

  public final Html.ElementInstruction tearsheetBackAction(String label) {
    return CarbonTearsheet.tearsheetBackAction(tmpl, label);
  }

  public final Html.ElementInstruction tearsheetNextAction(String label) {
    return CarbonTearsheet.tearsheetNextAction(tmpl, label);
  }

  // Tile

  public final Html.ElementInstruction tile(TileVariant variant, Html.Instruction... contents) {
    Check.notNull(variant, "variant == null");

    Icon icon = readRenderIcon();

    return CarbonTile.renderTile(tmpl, (CarbonTileVariant) variant, icon, contents);
  }

}