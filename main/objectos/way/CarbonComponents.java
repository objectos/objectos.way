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

import objectos.way.Carbon.Breakpoint;
import objectos.way.Carbon.ButtonVariant;
import objectos.way.Carbon.CarbonButtonVariant;
import objectos.way.Carbon.CarbonMenuLink;
import objectos.way.Carbon.CarbonSize;
import objectos.way.Carbon.CarbonSpacing;
import objectos.way.Carbon.DataTableSize;
import objectos.way.Carbon.Icon;
import objectos.way.Carbon.Spacing;
import objectos.way.Html.TemplateBase;

abstract class CarbonComponents {

  final Html.TemplateBase tmpl;

  CarbonComponents(TemplateBase tmpl) {
    this.tmpl = tmpl;
  }

  //
  // Attributes
  //

  private static final Html.AttributeObject TABINDEX_0 = Html.attribute(HtmlAttributeName.TABINDEX, "0");

  //
  // Typography
  //

  /**
   * Typography: the {@code code-01} style class.
   */
  public static final Html.ClassName CODE_01 = Html.classText("""
  font-mono
  text-12px leading-16px font-400 tracking-0.32px
  """);

  /**
   * Typography: the {@code code-02} style class.
   */
  public static final Html.ClassName CODE_02 = Html.classText("""
  font-mono
  text-14px leading-20px font-400 tracking-0.32px
  """);

  /**
   * Typography: the {@code label-01} style class.
   */
  public static final Html.ClassName LABEL_01 = Html.classText("""
  text-12px leading-16px font-400 tracking-0.32px
  """);

  /**
   * Typography: the {@code label-02} style class.
   */
  public static final Html.ClassName LABEL_02 = Html.classText("""
  text-14px leading-18px font-400 tracking-0.16px
  """);

  /**
   * Typography: the {@code body-compact-01} style class.
   */
  public static final Html.ClassName BODY_COMPACT_01 = Html.classText("""
  text-14px leading-18px font-400 tracking-0.16px
  """);

  /**
   * Typography: the {@code body-compact-02} style class.
   */
  public static final Html.ClassName BODY_COMPACT_02 = Html.classText("""
  text-16px leading-22px font-400 tracking-0px
  """);

  /**
   * Typography: the {@code body-01} style class.
   */
  public static final Html.ClassName BODY_01 = Html.classText("""
  text-14px leading-20px font-400 tracking-0.16px
  """);

  /**
   * Typography: the {@code body-02} style class.
   */
  public static final Html.ClassName BODY_02 = Html.classText("""
  text-16px leading-24px font-400 tracking-0px
  """);

  /**
   * Typography: the {@code heading-compact-01} style class.
   */
  public static final Html.ClassName HEADING_COMPACT_01 = Html.classText("""
  text-14px leading-18px font-600 tracking-0.16px
  """);

  /**
   * Typography: the {@code heading-compact-02} style class.
   */
  public static final Html.ClassName HEADING_COMPACT_02 = Html.classText("""
  text-16px leading-22px font-600 tracking-0px
  """);

  /**
   * Typography: the {@code heading-01} style class.
   */
  public static final Html.ClassName HEADING_01 = Html.classText("""
  text-14px leading-20px font-600 tracking-0.16px
  """);

  /**
   * Typography: the {@code heading-02} style class.
   */
  public static final Html.ClassName HEADING_02 = Html.classText("""
  text-16px leading-24px font-600 tracking-0px
  """);

  /**
   * Typography: the {@code heading-03} style class.
   */
  public static final Html.ClassName HEADING_03 = Html.classText("""
  text-20px leading-28px font-400 tracking-0px
  """);

  /**
   * Typography: the {@code heading-04} style class.
   */
  public static final Html.ClassName HEADING_04 = Html.classText("""
  text-28px leading-36px font-400 tracking-0px
  """);

  /**
   * Typography: the {@code heading-05} style class.
   */
  public static final Html.ClassName HEADING_05 = Html.classText("""
  text-32px leading-40px font-400 tracking-0px
  """);

  /**
   * Typography: the {@code heading-06} style class.
   */
  public static final Html.ClassName HEADING_06 = Html.classText("""
  text-42px leading-50px font-300 tracking-0px
  """);

  /**
   * Typography: the {@code heading-07} style class.
   */
  public static final Html.ClassName HEADING_07 = Html.classText("""
  text-54px leading-64px font-300 tracking-0px
  """);

  /**
   * Typography: the {@code fluid-heading-03} style class.
   */
  public static final Html.ClassName FLUID_HEADING_03 = Html.classText("""
  text-20px leading-28px font-400 tracking-0px
  max:text-24px
  """);

  /**
   * Typography: the {@code fluid-heading-04} style class.
   */
  public static final Html.ClassName FLUID_HEADING_04 = Html.classText("""
  text-28px leading-36px font-400 tracking-0px
  xl:text-32px xl:leading-40px
  """);

  /**
   * Typography: the {@code fluid-heading-05} style class.
   */
  public static final Html.ClassName FLUID_HEADING_05 = Html.classText("""
  text-32px leading-40px font-400 tracking-0px
  md:text-36px md:leading-44px md:font-300
  lg:text-42px lg:leading-50px
  xl:text-48px xl:leading-56px
  max:text-60px max:leading-70px
  """);

  /**
   * Typography: the {@code fluid-heading-06} style class.
   */
  public static final Html.ClassName FLUID_HEADING_06 = Html.classText("""
  text-32px leading-40px font-600 tracking-0px
  md:text-36px md:leading-44px md:font-600
  lg:text-42px lg:leading-50px
  xl:text-48px xl:leading-56px
  max:text-60px max:leading-70px
  """);

  /**
   * Typography: the {@code fluid-paragraph-01} style class.
   */
  public static final Html.ClassName FLUID_PARAGRAPH_01 = Html.classText("""
  text-24px leading-30px font-300 tracking-0px
  lg:text-28px lg:leading-36px
  max:text-32px max:leading-40px
  """);

  /**
   * Typography: the {@code fluid-display-01} style class.
   */
  public static final Html.ClassName FLUID_DISPLAY_01 = Html.classText("""
  text-42px leading-50px font-300 tracking-0px
  lg:text-54px lg:leading-64px
  xl:text-60px xl:leading-70px
  max:text-76px max:leading-86px
  """);

  /**
   * Typography: the {@code fluid-display-02} style class.
   */
  public static final Html.ClassName FLUID_DISPLAY_02 = Html.classText("""
  text-42px leading-50px font-600 tracking-0px
  lg:text-54px lg:leading-64px
  xl:text-60px xl:leading-70px
  max:text-76px max:leading-86px
  """);

  /**
   * Typography: the {@code fluid-display-03} style class.
   */
  public static final Html.ClassName FLUID_DISPLAY_03 = Html.classText("""
  text-42px leading-50px font-300 tracking-0px
  md:text-54px md:leading-64px
  lg:text-60px lg:leading-70px lg:-leading-0.64px
  xl:text-76px xl:leading-86px
  max:text-84px max:leading-94px max:-leading-0.96px
  """);

  /**
   * Typography: the {@code fluid-display-04} style class.
   */
  public static final Html.ClassName FLUID_DISPLAY_04 = Html.classText("""
  text-42px leading-50px font-300 tracking-0px
  md:text-68px md:leading-78px
  lg:text-92px lg:leading-102px lg:-leading-0.64px
  xl:text-122px xl:leading-130px
  max:text-156px max:leading-164px max:-leading-0.96px
  """);

  //
  // Button
  //

  /**
   * The primary button variant.
   */
  public static final ButtonVariant PRIMARY = new CarbonButtonVariant(0);

  /**
   * The secondary button variant.
   */
  public static final ButtonVariant SECONDARY = new CarbonButtonVariant(1);

  /**
   * The tertiary button variant.
   */
  public static final ButtonVariant TERTIARY = new CarbonButtonVariant(2);

  /**
   * The ghost button variant.
   */
  public static final ButtonVariant GHOST = new CarbonButtonVariant(3);

  /**
   * The danger button variant.
   */
  public static final ButtonVariant DANGER = new CarbonButtonVariant(4);

  /**
   * The danger tertiary button variant.
   */
  public static final ButtonVariant DANGER_TERTIARY = new CarbonButtonVariant(5);

  /**
   * The danger ghost button variant.
   */
  public static final ButtonVariant DANGER_GHOST = new CarbonButtonVariant(6);

  //
  // DataTable
  //

  private static final Html.ClassName DATA_TABLE_CONTENT = Html.classText("""
      block overflow-x-auto
      outline outline-2 -outline-offset-2 outline-transparent
      focus:outline-focus
      """);

  private static final Html.ClassName __DATA_TABLE_BASE = Html.classText("""
      w-full border-collapse border-spacing-0

      tr:w-full tr:border-none
      """);

  private static final Html.ClassName __DATA_TABLE_XS = Html.classText("""
      tr:h-24px
      th:py-2px
      td:py-2px
      """);

  private static final Html.ClassName __DATA_TABLE_SM = Html.classText("""
      tr:h-32px
      th:py-[7px]
      td:py-[7px]
      """);

  private static final Html.ClassName __DATA_TABLE_MD = Html.classText("""
      tr:h-40px
      th:pt-[6px] th:pb-[7px]
      td:pt-[6px] td:pb-[7px]
      """);

  private static final Html.ClassName __DATA_TABLE_LG = Html.classText("""
      tr:h-48px
      """);

  private static final Html.ClassName __DATA_TABLE_XL = Html.classText("""
      tr:h-64px
      th:py-spacing-05 th:align-top
      td:py-spacing-05 td:align-top
      """);

  private static final Html.ClassName[] DATA_TABLE_SIZES = {
      __DATA_TABLE_XS, __DATA_TABLE_SM, __DATA_TABLE_MD, __DATA_TABLE_LG, __DATA_TABLE_XL
  };

  public final Html.ElementInstruction dataTable(Html.Instruction... contents) {
    return dataTable(Carbon.LG, contents);
  }

  public final Html.ElementInstruction dataTable(DataTableSize size, Html.Instruction... contents) {
    CarbonSize sizeImpl;
    sizeImpl = (CarbonSize) size;

    int sizeIndex;
    sizeIndex = sizeImpl.index();

    return tmpl.div(
        DATA_TABLE_CONTENT, TABINDEX_0,

        tmpl.table(
            __DATA_TABLE_BASE,
            DATA_TABLE_SIZES[sizeIndex],

            tmpl.flatten(contents)
        )
    );
  }

  private static final Html.ClassName DATA_TABLE_THEAD = Html.classText("""
      bg-layer-accent
      text-14px leading-18px font-600 tracking-0.16px

      th:bg-layer-accent
      th:px-16px
      th:text-start th:align-middle th:text-text-primary
      """);

  public final Html.ElementInstruction dataTableHead(Html.Instruction... contents) {
    return tmpl.thead(
        DATA_TABLE_THEAD,

        tmpl.flatten(contents)
    );
  }

  private static final Html.ClassName DATA_TABLE_TBODY = Html.classText("""
      w-full bg-layer
      text-14px leading-18px font-400 tracking-0.16px

      tr:transition-colors tr:duration-75
      tr:hover:bg-layer-hover

      td:border-solid
      td:border-t td:border-t-layer
      td:border-b td:border-b-border-subtle
      td:px-16px
      td:text-start td:align-middle td:text-text-secondary
      """);

  public final Html.ElementInstruction dataTableBody(Html.Instruction... contents) {
    return tmpl.thead(
        DATA_TABLE_TBODY,

        tmpl.flatten(contents)
    );
  }

  //
  // Expressive
  //

  private boolean expressive;

  public final Html.NoOpInstruction expressive() {
    expressive = true;

    return tmpl.noop();
  }

  private Html.ElementInstruction resetExpressive(Html.ElementInstruction component) {
    expressive = false;

    return component;
  }

  //
  // Gap
  //

  private static final String[][] GAP = {
      {"gap-spacing-01", "gap-spacing-02", "gap-spacing-03", "gap-spacing-04", "gap-spacing-05", "gap-spacing-06", "gap-spacing-07",
          "gap-spacing-08", "gap-spacing-09", "gap-spacing-10", "gap-spacing-11", "gap-spacing-12", "gap-spacing-13"},

      {"sm:gap-spacing-01", "sm:gap-spacing-02", "sm:gap-spacing-03", "sm:gap-spacing-04", "sm:gap-spacing-05", "sm:gap-spacing-06", "sm:gap-spacing-07",
          "sm:gap-spacing-08", "sm:gap-spacing-09", "sm:gap-spacing-10", "sm:gap-spacing-11", "sm:gap-spacing-12", "sm:gap-spacing-13"},

      {"md:gap-spacing-01", "md:gap-spacing-02", "md:gap-spacing-03", "md:gap-spacing-04", "md:gap-spacing-05", "md:gap-spacing-06", "md:gap-spacing-07",
          "md:gap-spacing-08", "md:gap-spacing-09", "md:gap-spacing-10", "md:gap-spacing-11", "md:gap-spacing-12", "md:gap-spacing-13"},

      {"lg:gap-spacing-01", "lg:gap-spacing-02", "lg:gap-spacing-03", "lg:gap-spacing-04", "lg:gap-spacing-05", "lg:gap-spacing-06", "lg:gap-spacing-07",
          "lg:gap-spacing-08", "lg:gap-spacing-09", "lg:gap-spacing-10", "lg:gap-spacing-11", "lg:gap-spacing-12", "lg:gap-spacing-13"},

      {"xl:gap-spacing-01", "xl:gap-spacing-02", "xl:gap-spacing-03", "xl:gap-spacing-04", "xl:gap-spacing-05", "xl:gap-spacing-06", "xl:gap-spacing-07",
          "xl:gap-spacing-08", "xl:gap-spacing-09", "xl:gap-spacing-10", "xl:gap-spacing-11", "xl:gap-spacing-12", "xl:gap-spacing-13"},

      {"max:gap-spacing-01", "max:gap-spacing-02", "max:gap-spacing-03", "max:gap-spacing-04", "max:gap-spacing-05", "max:gap-spacing-06", "max:gap-spacing-07",
          "max:gap-spacing-08", "max:gap-spacing-09", "max:gap-spacing-10", "max:gap-spacing-11", "max:gap-spacing-12", "max:gap-spacing-13"}
  };

  public final Html.AttributeInstruction gap(Spacing mobile) {
    String cn0 = checkGap(Carbon.NONE, mobile);

    return tmpl.className(cn0);
  }

  public final Html.AttributeInstruction gap(Spacing mobile, Breakpoint point1, Spacing value1) {
    String cn0 = checkGap(Carbon.NONE, mobile);
    String cn1 = checkGap(point1, value1);

    return tmpl.className(cn0, cn1);
  }

  public final Html.AttributeInstruction gap(Spacing mobile, Breakpoint point1, Spacing value1, Breakpoint point2, Spacing value2) {
    String cn0 = checkGap(Carbon.NONE, mobile);
    String cn1 = checkGap(point1, value1);
    String cn2 = checkGap(point2, value2);

    return tmpl.className(cn0, cn1, cn2);
  }

  private String checkGap(Breakpoint point, Spacing value) {
    int pointIndex;
    pointIndex = checkBreakpoint(point);

    CarbonSpacing spacing;
    spacing = (CarbonSpacing) value;

    int valueIndex;
    valueIndex = spacing.value() - 1;

    return GAP[pointIndex][valueIndex];
  }

  private int checkBreakpoint(Breakpoint point) {
    CarbonSize size;
    size = (CarbonSize) point;

    return size.index();
  }

  private static final String[][] GRID_COLUMNS = {
      {"grid-cols-1", "grid-cols-2", "grid-cols-3", "grid-cols-4", "grid-cols-5", "grid-cols-6",
          "grid-cols-7", "grid-cols-8", "grid-cols-9", "grid-cols-10", "grid-cols-11", "grid-cols-12"},

      {"sm:grid-cols-1", "sm:grid-cols-2", "sm:grid-cols-3", "sm:grid-cols-4", "sm:grid-cols-5", "sm:grid-cols-6",
          "sm:grid-cols-7", "sm:grid-cols-8", "sm:grid-cols-9", "sm:grid-cols-10", "sm:grid-cols-11", "sm:grid-cols-12"},

      {"md:grid-cols-1", "md:grid-cols-2", "md:grid-cols-3", "md:grid-cols-4", "md:grid-cols-5", "md:grid-cols-6",
          "md:grid-cols-7", "md:grid-cols-8", "md:grid-cols-9", "md:grid-cols-10", "md:grid-cols-11", "md:grid-cols-12"},

      {"lg:grid-cols-1", "lg:grid-cols-2", "lg:grid-cols-3", "lg:grid-cols-4", "lg:grid-cols-5", "lg:grid-cols-6",
          "lg:grid-cols-7", "lg:grid-cols-8", "lg:grid-cols-9", "lg:grid-cols-10", "lg:grid-cols-11", "lg:grid-cols-12"},

      {"xl:grid-cols-1", "xl:grid-cols-2", "xl:grid-cols-3", "xl:grid-cols-4", "xl:grid-cols-5", "xl:grid-cols-6",
          "xl:grid-cols-7", "xl:grid-cols-8", "xl:grid-cols-9", "xl:grid-cols-10", "xl:grid-cols-11", "xl:grid-cols-12"},

      {"max:grid-cols-1", "max:grid-cols-2", "max:grid-cols-3", "max:grid-cols-4", "max:grid-cols-5", "max:grid-cols-6",
          "max:grid-cols-7", "max:grid-cols-8", "max:grid-cols-9", "max:grid-cols-10", "max:grid-cols-11", "max:grid-cols-12"}
  };

  public final Html.AttributeInstruction gridColumns(int mobile) {
    String cn = checkGridColumns(Carbon.NONE, mobile);

    return tmpl.className(cn);
  }

  public final Html.AttributeInstruction gridColumns(int mobile, Breakpoint point1, int value1) {
    String cn0 = checkGridColumns(Carbon.NONE, mobile);
    String cn1 = checkGridColumns(point1, value1);

    return tmpl.className(cn0, cn1);
  }

  public final Html.AttributeInstruction gridColumns(int mobile, Breakpoint point1, int value1, Breakpoint point2, int value2) {
    String cn0 = checkGridColumns(Carbon.NONE, mobile);
    String cn1 = checkGridColumns(point1, value1);
    String cn2 = checkGridColumns(point2, value2);

    return tmpl.className(cn0, cn1, cn2);
  }

  public final Html.AttributeInstruction gridColumns(
      int mobile, Breakpoint point1, int value1, Breakpoint point2, int value2, Breakpoint point3, int value3) {
    String cn0 = checkGridColumns(Carbon.NONE, mobile);
    String cn1 = checkGridColumns(point1, value1);
    String cn2 = checkGridColumns(point2, value2);
    String cn3 = checkGridColumns(point3, value3);

    return tmpl.className(cn0, cn1, cn2, cn3);
  }

  private String checkGridColumns(Breakpoint point, int value) {
    int pointIndex;
    pointIndex = checkBreakpoint(point);

    if (value < 1) {
      throw new IllegalArgumentException("Grid columns must be equal or greater than 1");
    }

    if (value > 12) {
      throw new IllegalArgumentException("Grid columns must be equal or lesser than 12");
    }

    int valueIndex;
    valueIndex = value - 1;

    return GRID_COLUMNS[pointIndex][valueIndex];
  }

  //
  // Header
  //

  private static final Html.ClassName HEADER = Html.classText("""
      fixed top-0px right-0px left-0px z-header
      flex items-center h-header
      border-b border-b-border-subtle
      bg-background
      """);

  public final Html.ElementInstruction header(Html.Instruction... contents) {
    return tmpl.header(
        HEADER,

        tmpl.flatten(contents)
    );
  }

  public static final Html.ClassName HEADER_OFFSET = Html.className("mt-header");

  //
  // HeaderButton
  //

  private static final Html.ClassName _HEADER_BUTTON = Html.classText("""
      cursor-pointer appearance-none
      size-header items-center justify-center
      border border-transparent
      transition-colors duration-100
      active:bg-background-active
      focus:border-focus focus:outline-none
      hover:bg-background-hover

      lg:hidden
      """);

  public static Script.Action hideHeaderButton(Html.Id id) {
    return Script.replaceClass(id, "hidden", "flex", true);
  }

  public static Script.Action showHeaderButton(Html.Id id) {
    return Script.replaceClass(id, "hidden", "flex");
  }

  private static final Html.ClassName HEADER_CLOSE_BUTTON = Html.className(
      _HEADER_BUTTON, "hidden"
  );

  public final Html.ElementInstruction headerCloseButton(Html.Instruction... contents) {
    return tmpl.button(
        HEADER_CLOSE_BUTTON,

        tmpl.type("button"),

        tmpl.flatten(contents),

        icon20(
            Carbon.Icon.CLOSE,
            tmpl.className("fill-icon-primary"),
            tmpl.ariaHidden("true")
        )
    );
  }

  private static final Html.ClassName HEADER_MENU_BUTTON = Html.className(
      _HEADER_BUTTON, "flex"
  );

  public final Html.ElementInstruction headerMenuButton(Html.Instruction... contents) {
    return tmpl.button(
        HEADER_MENU_BUTTON,

        tmpl.type("button"),

        tmpl.flatten(contents),

        icon20(
            Carbon.Icon.MENU,
            tmpl.className("fill-icon-primary"),
            tmpl.ariaHidden("true")
        )
    );
  }

  //
  // HeaderMenuItem
  //

  private static final Html.ClassName __HEADER_NAV_LINK = Html.classText("""
      relative flex h-full select-none items-center
      border-2 border-transparent
      bg-background
      px-16px
      transition-colors duration-100
      active:bg-background-active active:text-text-primary
      focus:border-focus focus:outline-none
      hover:bg-background-hover hover:text-text-primary
      """);

  private static final Html.ClassName __HEADER_NAV_LINK_ACTIVE = Html.classText("""
      text-text-primary
      after:absolute after:-bottom-2px after:-left-2px after:-right-2px
      after:block after:border-b-3 after:border-b-border-interactive after:content-empty
      """);

  private static final Html.ClassName __HEADER_NAV_LINK_INACTIVE = Html.classText("""
      text-text-secondary
      """);

  private static final Html.ClassName __HEADER_NAV_LINK_PRODUCTIVE = Html.classText("""
      text-14px leading-18px font-400 tracking-0px
      """);

  static final Html.ClassName HEADER_NAV_LINK_ACTIVE = Html.className(
      __HEADER_NAV_LINK, __HEADER_NAV_LINK_ACTIVE, __HEADER_NAV_LINK_PRODUCTIVE
  );

  static final Html.ClassName HEADER_NAV_LINK_INACTIVE = Html.className(
      __HEADER_NAV_LINK, __HEADER_NAV_LINK_INACTIVE, __HEADER_NAV_LINK_PRODUCTIVE
  );

  static final Html.ClassName HEADER_NAV_LINK_ACTIVE_EXPRESSIVE = Html.className(
      __HEADER_NAV_LINK, __HEADER_NAV_LINK_ACTIVE, BODY_COMPACT_02
  );

  static final Html.ClassName HEADER_NAV_LINK_INACTIVE_EXPRESSIVE = Html.className(
      __HEADER_NAV_LINK, __HEADER_NAV_LINK_INACTIVE, BODY_COMPACT_02
  );

  private Html.ElementInstruction headerMenuItem(CarbonMenuLink link) {
    Script.Action onClick;
    onClick = link.onClick();

    return tmpl.li(
        tmpl.a(
            link.active()
                ? expressive ? HEADER_NAV_LINK_ACTIVE_EXPRESSIVE : HEADER_NAV_LINK_ACTIVE
                : expressive ? HEADER_NAV_LINK_INACTIVE_EXPRESSIVE : HEADER_NAV_LINK_INACTIVE,

            onClick != null ? tmpl.dataOnClick(onClick) : tmpl.noop(),
            onClick != null ? tmpl.dataOnClick(Script.location(link.href())) : tmpl.noop(),

            tmpl.href(link.href()),

            tmpl.tabindex("0"),

            tmpl.span(link.text())
        )
    );
  }

  //
  // HeaderName
  //

  private static final Html.ClassName HEADER_NAME = Html.classText("""
      flex h-full select-none items-center
      border-2 border-transparent
      px-16px
      text-14px font-600 leading-20px tracking-0.1px text-text-primary
      outline-none
      transition-colors duration-100
      focus:border-focus
      lg:pl-16px lg:pr-32px
      """);

  private static final Html.ClassName HEADER_NAME_PREFIX = Html.classText("""
      font-400
      """);

  public final Html.ElementInstruction headerName(Html.Instruction... contents) {
    return tmpl.a(
        HEADER_NAME,

        tmpl.flatten(contents)
    );
  }

  public final Html.ElementInstruction headerName(String prefix, String text, String href) {
    Check.notNull(prefix, "prefix == null");
    Check.notNull(text, "text == null");
    Check.notNull(href, "href == null");

    return tmpl.a(
        HEADER_NAME,

        tmpl.href(href),

        tmpl.span(
            HEADER_NAME_PREFIX,

            tmpl.t(prefix)
        ),

        tmpl.nbsp(),

        tmpl.t(text)
    );
  }

  public final Html.ElementInstruction headerName(String prefix, String text, String href, Script.Action onClick) {
    Check.notNull(prefix, "prefix == null");
    Check.notNull(text, "text == null");
    Check.notNull(href, "href == null");
    Check.notNull(onClick, "onClick == null");

    return tmpl.a(
        HEADER_NAME,

        tmpl.dataOnClick(onClick),
        tmpl.dataOnClick(Script.location(href)),

        tmpl.href(href),

        tmpl.span(
            HEADER_NAME_PREFIX,

            tmpl.t(prefix)
        ),

        tmpl.nbsp(),

        tmpl.t(text)
    );
  }

  //
  // HeaderNavigation
  //

  private static final Html.ClassName HEADER_NAVIGATION = Html.classText("""
      relative hidden h-full pl-16px

      lg:flex lg:items-center

      lg:before:relative lg:before:-left-16px lg:before:z-header lg:before:block
      lg:before:h-1/2 lg:before:w-1px
      lg:before:border-l lg:before:border-l-border-subtle
      lg:before:content-empty
      """);

  public final Html.ElementInstruction headerNavigation(Html.Instruction... contents) {
    return resetExpressive(
        tmpl.nav(
            HEADER_NAVIGATION,

            tmpl.flatten(contents)
        )
    );
  }

  //
  // HeaderNavigationItems
  //

  private static final Html.ClassName HEADER_NAVIGATION_ITEMS = Html.classText("""
      flex h-full
      text-text-secondary
      """);

  public final Html.ElementInstruction headerNavigationItems(Iterable<? extends Carbon.MenuElement> elements) {
    return tmpl.ul(
        HEADER_NAVIGATION_ITEMS,

        tmpl.f(this::renderHeaderNavigationItems, elements)
    );
  }

  private void renderHeaderNavigationItems(Iterable<? extends Carbon.MenuElement> elements) {
    for (var element : elements) {
      switch (element) {
        case CarbonMenuLink link -> headerMenuItem(link);
      }
    }
  }

  //
  // I
  //

  public final Html.ElementInstruction icon16(Icon icon, Html.Instruction... attributes) {
    return Carbon.renderIcon16(tmpl, icon, attributes);
  }

  public final Html.ElementInstruction icon20(Icon icon, Html.Instruction... attributes) {
    return Carbon.renderIcon20(tmpl, icon, attributes);
  }

  public final Html.ElementInstruction icon24(Icon icon, Html.Instruction... attributes) {
    return Carbon.renderIcon24(tmpl, icon, attributes);
  }

  public final Html.ElementInstruction icon32(Icon icon, Html.Instruction... attributes) {
    return Carbon.renderIcon32(tmpl, icon, attributes);
  }

  //
  // P
  //

  private static final Html.ClassName PAGE_HEADER = Html.classText("""
      sticky bg-layer
      shadow-[0_1px_0_var(--cds-layer-accent)]
      """);

  public static final Html.ClassName PAGE_HEADER_TITLE_ONLY = Html.classText("""
      py-32px
      """);

  public final Html.ElementInstruction pageHeader(Html.Instruction... contents) {
    return tmpl.div(
        PAGE_HEADER,

        tmpl.flatten(contents)
    );
  }

  private static final Html.ClassName PAGE_HEADER_TITLE = Html.classText("""
      col-span-full
      md:col-span-3
      """);

  public final Html.ElementInstruction pageHeaderTitle(String title) {
    Check.notNull(title, "title == null");

    return tmpl.h1(
        PAGE_HEADER_TITLE, HEADING_04,

        tmpl.t(title)
    );
  }

  private static final Html.ClassName __PAGE_HEADER_TITLE_ROW = Html.classText("""
      grid-cols-5 gap-y-spacing-05
      """);

  public final Html.ElementInstruction pageHeaderTitleRow(Html.Instruction... contents) {
    return tmpl.div(
        CarbonGrid.VARIANTS.get(Carbon.WIDE), __PAGE_HEADER_TITLE_ROW,

        tmpl.flatten(contents)
    );
  }

  //
  // S
  //

  private static final Html.ClassName SIDE_NAV = Html.classText("""
      invisible fixed inset-0px z-overlay
      bg-overlay
      opacity-0
      transition-opacity duration-300

      lg:hidden
      """);

  public static final Html.ClassName SIDE_NAV_OFFSET = Html.classText("""
      lg:ml-side-nav
      """);

  public static final Html.ClassName SIDE_NAV_PERSISTENT = Html.classText("""
      lg:visible lg:right-auto lg:w-side-nav lg:opacity-100

      lg:more:block
      """);

  public static Script.Action hideSideNav(Html.Id id) {
    return Script.actions(
        Script.replaceClass(id, "opacity-0", "opacity-100", true),
        Script.delay(
            300, Script.replaceClass(id, "invisible", "visible", true)
        )
    );
  }

  public static Script.Action showSideNav(Html.Id id) {
    return Script.actions(
        Script.replaceClass(id, "invisible", "visible"),
        Script.replaceClass(id, "opacity-0", "opacity-100")
    );
  }

  public final Html.ElementInstruction sideNav(Html.Instruction... contents) {
    return tmpl.div(
        SIDE_NAV,

        tmpl.flatten(contents)
    );
  }

  private static final Html.ClassName SIDE_NAV_BODY = Html.classText("""
      absolute top-0px bottom-0px left-0px z-header
      w-0px pt-16px
      bg-background
      text-text-secondary
      transition-all duration-100
      """);

  public static final Html.ClassName SIDE_NAV_BODY_PERSISTENT = Html.className("""
      lg:w-side-nav
      """);

  public static Script.Action hideSideNavBody(Html.Id id) {
    return Script.replaceClass(id, "w-0px", "w-side-nav", true);
  }

  public static Script.Action showSideNavBody(Html.Id id) {
    return Script.replaceClass(id, "w-0px", "w-side-nav");
  }

  public final Html.ElementInstruction sideNavBody(Html.Instruction... contents) {
    return tmpl.nav(
        SIDE_NAV_BODY,

        tmpl.flatten(contents)
    );
  }

  //
  // SideNavHeaderItem
  //

  private static final Html.ClassName SIDE_NAV_HEADER_ITEM = Html.classText("""
  w-auto h-auto overflow-hidden
  """);

  private static final Html.ClassName __SIDE_NAV_HEADER_LINK = Html.classText("""
  relative flex min-h-32px
  items-center justify-between whitespace-nowrap
  border-2 border-transparent
  px-16px
  text-text-secondary
  outline outline-2 -outline-offset-2 outline-transparent
  transition-colors duration-100
  active:bg-background-active active:text-text-primary
  focus:outline-focus
  hover:bg-background-hover hover:text-text-primary
  """);

  private static final Html.ClassName __SIDE_NAV_HEADER_LINK_ACTIVE = Html.classText("""
  after:absolute after:-top-2px after:-bottom-2px after:-left-2px
  after:block after:border-l-3 after:border-l-border-interactive after:content-empty
  """);

  private static final Html.ClassName __SIDE_NAV_HEADER_LINK_INACTIVE = Html.classText("""
  text-text-secondary
  """);

  private static final Html.ClassName SIDE_NAV_HEADER_LINK_ACTIVE = Html.className(
      __SIDE_NAV_HEADER_LINK, Carbon.HEADING_COMPACT_01, __SIDE_NAV_HEADER_LINK_ACTIVE
  );

  private static final Html.ClassName SIDE_NAV_HEADER_LINK_INACTIVE = Html.className(
      __SIDE_NAV_HEADER_LINK, Carbon.HEADING_COMPACT_01, __SIDE_NAV_HEADER_LINK_INACTIVE
  );

  private Html.ElementInstruction sideNavHeaderItem(CarbonMenuLink link) {
    Script.Action onClick;
    onClick = link.onClick();

    return tmpl.li(
        SIDE_NAV_HEADER_ITEM,

        tmpl.a(
            link.active() ? SIDE_NAV_HEADER_LINK_ACTIVE : SIDE_NAV_HEADER_LINK_INACTIVE,

            onClick != null ? tmpl.dataOnClick(onClick) : tmpl.noop(),
            onClick != null ? tmpl.dataOnClick(Script.location(link.href())) : tmpl.noop(),

            tmpl.href(link.href()),

            tmpl.tabindex("0"),

            tmpl.span(link.text())
        )
    );
  }

  //
  // SideNavHeaderItems
  //

  private static final Html.ClassName SIDE_NAV_HEADER_ITEMS = Html.classText("""
      mb-32px
      lg:hidden
      """);

  public final Html.ElementInstruction sideNavHeaderItems(Iterable<? extends Carbon.MenuElement> elements) {
    return tmpl.ul(
        SIDE_NAV_HEADER_ITEMS,

        tmpl.f(this::renderSideNavHeaderItems, elements)
    );
  }

  private void renderSideNavHeaderItems(Iterable<? extends Carbon.MenuElement> elements) {
    for (var element : elements) {
      switch (element) {
        case CarbonMenuLink link -> sideNavHeaderItem(link);
      }
    }
  }

  //
  // SideNavItems
  //

  public final Html.ElementInstruction sideNavItems(Iterable<? extends Carbon.MenuElement> elements) {
    return tmpl.ul(
        tmpl.f(this::renderSideNavItems, elements)
    );
  }

  private void renderSideNavItems(Iterable<? extends Carbon.MenuElement> elements) {
    for (var element : elements) {
      switch (element) {
        case CarbonMenuLink link -> sideNavLink(link);
      }
    }
  }

  //
  // SideNavLink
  //

  private static final Html.ClassName __SIDE_NAV_LINK = Html.classText("""
  relative flex min-h-32px
  items-center justify-between whitespace-nowrap
  px-16px
  outline outline-2 -outline-offset-2 outline-transparent
  transition-colors duration-100
  focus:outline-focus
  hover:bg-background-hover hover:text-text-primary
  span:select-none span:text-14px span:leading-20px span:tracking-0.1px span:truncate
  """);

  private static final Html.ClassName __SIDE_NAV_LINK_ACTIVE = Html.classText("""
  bg-selected text-link-primary font-600
  after:absolute after:top-0px after:bottom-0px after:left-0px
  after:block after:border-l-3 after:border-l-border-interactive after:content-empty
  span:text-text-primary
  """);

  private static final Html.ClassName __SIDE_NAV_LINK_INACTIVE = Html.classText("""
  span:text-text-secondary
  """);

  private static final Html.ClassName SIDE_NAV_LINK_ACTIVE = Html.className(
      __SIDE_NAV_LINK, Carbon.HEADING_COMPACT_01, __SIDE_NAV_LINK_ACTIVE
  );

  private static final Html.ClassName SIDE_NAV_LINK_INACTIVE = Html.className(
      __SIDE_NAV_LINK, Carbon.HEADING_COMPACT_01, __SIDE_NAV_LINK_INACTIVE
  );

  private Html.ElementInstruction sideNavLink(CarbonMenuLink link) {
    Script.Action onClick;
    onClick = link.onClick();

    return tmpl.li(
        tmpl.a(
            link.active() ? SIDE_NAV_LINK_ACTIVE : SIDE_NAV_LINK_INACTIVE,

            onClick != null ? tmpl.dataOnClick(onClick) : tmpl.noop(),
            onClick != null ? tmpl.dataOnClick(Script.location(link.href())) : tmpl.noop(),

            tmpl.href(link.href()),

            tmpl.span(link.text())
        )
    );
  }

}