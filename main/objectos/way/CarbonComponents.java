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

import objectos.lang.object.Check;
import objectos.way.Carbon.Breakpoint;
import objectos.way.Carbon.CarbonMenuLink;
import objectos.way.Carbon.Icon;
import objectos.way.Html.ClassName;
import objectos.way.Html.TemplateBase;

abstract class CarbonComponents {

  private final Html.TemplateBase tmpl;

  CarbonComponents(TemplateBase tmpl) {
    this.tmpl = tmpl;
  }

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
    boolean expressive = false;

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
    return tmpl.nav(
        HEADER_NAVIGATION,

        tmpl.flatten(contents)
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
  // Icon
  //

  public final Html.ElementInstruction icon16(Icon icon, Html.AttributeInstruction... attributes) {
    return icon(icon, "1rem", attributes);
  }

  public final Html.ElementInstruction icon20(Icon icon, Html.AttributeInstruction... attributes) {
    return icon(icon, "1.25rem", attributes);
  }

  public final Html.ElementInstruction icon24(Icon icon, Html.AttributeInstruction... attributes) {
    return icon(icon, "1.5rem", attributes);
  }

  public final Html.ElementInstruction icon32(Icon icon, Html.AttributeInstruction... attributes) {
    return icon(icon, "2rem", attributes);
  }

  private Html.ElementInstruction icon(Icon icon, String size, Html.AttributeInstruction... attributes) {
    return tmpl.svg(
        tmpl.xmlns("http://www.w3.org/2000/svg"),
        tmpl.fill("currentColor"),
        tmpl.width(size), tmpl.height(size), tmpl.viewBox("0 0 32 32"),

        tmpl.flatten(attributes),

        tmpl.raw(icon.raw)
    );
  }

  //
  // SideNav
  //

  private static final Html.ClassName SIDE_NAV = Html.classText("""
      invisible fixed inset-0px z-overlay
      bg-overlay
      opacity-0
      transition-opacity duration-300

      lg:hidden
      """);

  public static final Html.ClassName SIDE_NAV_OFFSET = Html.className("""
      lg:ml-side-nav
      """);

  public static final Html.ClassName SIDE_NAV_PERSISTENT = Html.className("""
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

  public final Html.ClassName gridColumns(int mobile) {
    //return new CarbonClassName("grid-cols-" + mobile);
    throw new UnsupportedOperationException("Implement me");
  }

  public final ClassName gridColumns(int mobile, Breakpoint point1, int value1) {
    //return new CarbonClassName("grid-cols-" + mobile + " " + point1 + ":grid-cols-" + value1);
    throw new UnsupportedOperationException("Implement me");
  }

  public final ClassName gridColumns(int mobile, Breakpoint point1, int value1, Breakpoint point2, int value2) {
    //return new CarbonClassName("grid-cols-" + mobile + " " + point1 + ":grid-cols-" + value1 + " " + point2 + ":grid-cols-" + value2);
    throw new UnsupportedOperationException("Implement me");
  }

}