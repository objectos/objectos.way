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
import objectos.way.Carbon.CarbonMenuLink;

abstract class CarbonTemplate extends Html.Template implements Web.Action {

  protected final Carbon carbon;

  protected final Http.Exchange http;

  protected CarbonTemplate(Http.Exchange http) {
    this.http = http;

    carbon = new Carbon(this);
  }

  @Override
  public void execute() {
    http.ok(this);
  }

  /**
   * Renders the standard {@code head} contents.
   */
  protected final void renderStandardHead() {
    meta(charset("utf-8"));
    meta(httpEquiv("content-type"), content("text/html; charset=utf-8"));
    meta(name("viewport"), content("width=device-width, initial-scale=1"));
    link(rel("shortcut icon"), type("image/x-icon"), href("/favicon.png"));
    link(rel("stylesheet"), type("text/css"), href("/ui/carbon.css"));
    script(src("/ui/script.js"));
  }

  static final Html.ClassName HEADER = Html.classText("""
  fixed top-0px right-0px left-0px z-header
  flex items-center h-header
  border-b border-b-border-subtle
  bg-background
  """);

  protected final Html.ElementInstruction carbonHeader(Html.Instruction... contents) {
    return header(
        HEADER,

        flatten(contents)
    );
  }

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

  static final Html.ClassName HEADER_MENU_BUTTON = Html.className(
      _HEADER_BUTTON, "flex"
  );

  static final Html.ClassName HEADER_CLOSE_BUTTON = Html.className(
      _HEADER_BUTTON, "hidden"
  );

  static Script.Action hideHeaderButton(Html.Id id) {
    return Script.replaceClass(id, "hidden", "flex", true);
  }

  static Script.Action showHeaderButton(Html.Id id) {
    return Script.replaceClass(id, "hidden", "flex");
  }

  protected final Html.ElementInstruction carbonHeaderCloseButton(Html.Instruction... contents) {
    return button(
        HEADER_CLOSE_BUTTON,

        type("button"),

        flatten(contents),

        icon20(
            Carbon.Icon.CLOSE,
            className("fill-icon-primary"),
            ariaHidden("true")
        )
    );
  }

  protected final Html.ElementInstruction carbonHeaderMenuButton(Html.Instruction... contents) {
    return button(
        HEADER_MENU_BUTTON,

        type("button"),

        flatten(contents),

        icon20(
            Carbon.Icon.MENU,
            className("fill-icon-primary"),
            ariaHidden("true")
        )
    );
  }

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
      __HEADER_NAV_LINK, __HEADER_NAV_LINK_ACTIVE, CarbonClasses.BODY_COMPACT_02
  );

  static final Html.ClassName HEADER_NAV_LINK_INACTIVE_EXPRESSIVE = Html.className(
      __HEADER_NAV_LINK, __HEADER_NAV_LINK_INACTIVE, CarbonClasses.BODY_COMPACT_02
  );

  private Html.ElementInstruction carbonHeaderMenuItem(CarbonMenuLink link) {
    boolean expressive = false;

    Script.Action onClick;
    onClick = link.onClick();

    return li(
        a(
            link.active()
                ? expressive ? HEADER_NAV_LINK_ACTIVE_EXPRESSIVE : HEADER_NAV_LINK_ACTIVE
                : expressive ? HEADER_NAV_LINK_INACTIVE_EXPRESSIVE : HEADER_NAV_LINK_INACTIVE,

            onClick != null ? dataOnClick(onClick) : noop(),
            onClick != null ? dataOnClick(Script.location(link.href())) : noop(),

            href(link.href()),

            tabindex("0"),

            span(link.text())
        )
    );
  }

  static final Html.ClassName HEADER_NAME = Html.classText("""
  flex h-full select-none items-center
  border-2 border-transparent
  px-16px
  text-14px font-600 leading-20px tracking-0.1px text-text-primary
  outline-none
  transition-colors duration-100
  focus:border-focus
  lg:pl-16px lg:pr-32px
  """);

  static final Html.ClassName HEADER_NAME_PREFIX = Html.classText("""
  font-400
  """);

  protected final Html.ElementInstruction carbonHeaderName(String prefix, String text, String href) {
    Check.notNull(prefix, "prefix == null");
    Check.notNull(text, "text == null");
    Check.notNull(href, "href == null");

    return a(
        HEADER_NAME,

        href(href),

        span(
            HEADER_NAME_PREFIX,

            t(prefix)
        ),

        nbsp(),

        t(text)
    );
  }

  protected final Html.ElementInstruction carbonHeaderName(String prefix, String text, String href, Script.Action onClick) {
    Check.notNull(prefix, "prefix == null");
    Check.notNull(text, "text == null");
    Check.notNull(href, "href == null");
    Check.notNull(onClick, "onClick == null");

    return a(
        HEADER_NAME,

        dataOnClick(onClick),
        dataOnClick(Script.location(href)),

        href(href),

        span(
            HEADER_NAME_PREFIX,

            t(prefix)
        ),

        nbsp(),

        t(text)
    );
  }

  static final Html.ClassName HEADER_NAVIGATION = Html.classText("""
  relative hidden h-full pl-16px

  lg:flex lg:items-center

  lg:before:relative lg:before:-left-16px lg:before:z-header lg:before:block
  lg:before:h-1/2 lg:before:w-1px
  lg:before:border-l lg:before:border-l-border-subtle
  lg:before:content-empty
  """);

  protected final Html.ElementInstruction carbonHeaderNavigation(Html.Instruction... contents) {
    return nav(
        HEADER_NAVIGATION,

        flatten(contents)
    );
  }

  static final Html.ClassName HEADER_NAVIGATION_LIST = Html.classText("""
  flex h-full
  text-text-secondary
  """);

  protected final Html.ElementInstruction carbonHeaderNavigationItems(Iterable<? extends Carbon.MenuElement> elements) {
    return ul(
        HEADER_NAVIGATION_LIST,

        f(this::renderHeaderNavigationItems, elements)
    );
  }

  private void renderHeaderNavigationItems(Iterable<? extends Carbon.MenuElement> elements) {
    for (var element : elements) {
      switch (element) {
        case CarbonMenuLink link -> carbonHeaderMenuItem(link);
      }
    }
  }

  static final Html.ClassName SIDE_NAV = Html.classText("""
  invisible fixed inset-0px z-overlay
  bg-overlay
  opacity-0
  transition-opacity duration-300

  lg:hidden
  """);

  protected static final Html.ClassName SIDE_NAV_PERSISTENT = Html.className("""
  lg:visible lg:right-auto lg:w-side-nav lg:opacity-100

  lg:more:block
  """);

  static Script.Action hideSideNav(Html.Id id) {
    return Script.actions(
        Script.replaceClass(id, "opacity-0", "opacity-100", true),
        Script.delay(
            300, Script.replaceClass(id, "invisible", "visible", true)
        )
    );
  }

  static Script.Action showSideNav(Html.Id id) {
    return Script.actions(
        Script.replaceClass(id, "invisible", "visible"),
        Script.replaceClass(id, "opacity-0", "opacity-100")
    );
  }

  protected final Html.ElementInstruction carbonSideNav(Html.Instruction... contents) {
    return div(
        SIDE_NAV,

        flatten(contents)
    );
  }

  static final Html.ClassName SIDE_NAV_BODY = Html.classText("""
  absolute top-0px bottom-0px left-0px z-header
  w-0px pt-16px
  bg-background
  text-text-secondary
  transition-all duration-100
  """);

  protected static final Html.ClassName SIDE_NAV_BODY_PERSISTENT = Html.className("""
  lg:w-side-nav
  """);

  static Script.Action hideSideNavBody(Html.Id id) {
    return Script.replaceClass(id, "w-0px", "w-side-nav", true);
  }

  static Script.Action showSideNavBody(Html.Id id) {
    return Script.replaceClass(id, "w-0px", "w-side-nav");
  }

  protected final Html.ElementInstruction carbonSideNavBody(Html.Instruction... contents) {
    return nav(
        SIDE_NAV_BODY,

        flatten(contents)
    );
  }

  static final Html.ClassName SIDE_NAV_HEADER_LIST = Html.classText("""
  mb-32px
  lg:hidden
  """);

  protected final Html.ElementInstruction carbonSideNavHeaderItems(Iterable<? extends Carbon.MenuElement> elements) {
    return ul(
        SIDE_NAV_HEADER_LIST,

        f(this::renderSideNavHeaderItems, elements)
    );
  }

  private void renderSideNavHeaderItems(Iterable<? extends Carbon.MenuElement> elements) {
    for (var element : elements) {
      switch (element) {
        case CarbonMenuLink link -> carbonSideNavHeaderItem(link);
      }
    }
  }

  static final Html.ClassName SIDE_NAV_HEADER_ITEM = Html.classText("""
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

  static final Html.ClassName SIDE_NAV_HEADER_LINK_ACTIVE = Html.className(
      __SIDE_NAV_HEADER_LINK, Carbon.HEADING_COMPACT_01, __SIDE_NAV_HEADER_LINK_ACTIVE
  );

  static final Html.ClassName SIDE_NAV_HEADER_LINK_INACTIVE = Html.className(
      __SIDE_NAV_HEADER_LINK, Carbon.HEADING_COMPACT_01, __SIDE_NAV_HEADER_LINK_INACTIVE
  );

  private Html.ElementInstruction carbonSideNavHeaderItem(CarbonMenuLink link) {
    Script.Action onClick;
    onClick = link.onClick();

    return li(
        SIDE_NAV_HEADER_ITEM,

        a(
            link.active() ? SIDE_NAV_HEADER_LINK_ACTIVE : SIDE_NAV_HEADER_LINK_INACTIVE,

            onClick != null ? dataOnClick(onClick) : noop(),
            onClick != null ? dataOnClick(Script.location(link.href())) : noop(),

            href(link.href()),

            tabindex("0"),

            span(link.text())
        )
    );
  }

  protected final Html.ElementInstruction carbonSideNavItems(Iterable<? extends Carbon.MenuElement> elements) {
    return ul(
        f(this::renderSideNavItems, elements)
    );
  }

  private void renderSideNavItems(Iterable<? extends Carbon.MenuElement> elements) {
    for (var element : elements) {
      switch (element) {
        case CarbonMenuLink link -> carbonSideNavLink(link);
      }
    }
  }

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

  static final Html.ClassName SIDE_NAV_LINK_ACTIVE = Html.className(
      __SIDE_NAV_LINK, Carbon.HEADING_COMPACT_01, __SIDE_NAV_LINK_ACTIVE
  );

  static final Html.ClassName SIDE_NAV_LINK_INACTIVE = Html.className(
      __SIDE_NAV_LINK, Carbon.HEADING_COMPACT_01, __SIDE_NAV_LINK_INACTIVE
  );

  private Html.ElementInstruction carbonSideNavLink(CarbonMenuLink link) {
    Script.Action onClick;
    onClick = link.onClick();

    return li(
        a(
            link.active() ? SIDE_NAV_LINK_ACTIVE : SIDE_NAV_LINK_INACTIVE,

            onClick != null ? dataOnClick(onClick) : noop(),
            onClick != null ? dataOnClick(Script.location(link.href())) : noop(),

            href(link.href()),

            span(link.text())
        )
    );
  }

  protected final boolean currentPage(String href) {
    Http.Request.Target target;
    target = http.target();

    String path;
    path = target.path();

    return path.equals(href);
  }

  protected final boolean currentPageStartsWith(String href) {
    Http.Request.Target target;
    target = http.target();

    String path;
    path = target.path();

    return path.startsWith(href);
  }

  protected final Html.ElementInstruction icon16(Carbon.Icon icon, Html.AttributeInstruction... attributes) {
    return renderIcon(icon, "16px", attributes);
  }

  protected final Html.ElementInstruction icon20(Carbon.Icon icon, Html.AttributeInstruction... attributes) {
    return renderIcon(icon, "20px", attributes);
  }

  protected final Html.ElementInstruction icon24(Carbon.Icon icon, Html.AttributeInstruction... attributes) {
    return renderIcon(icon, "24px", attributes);
  }

  protected final Html.ElementInstruction icon32(Carbon.Icon icon, Html.AttributeInstruction... attributes) {
    return renderIcon(icon, "32px", attributes);
  }

  private Html.ElementInstruction renderIcon(Carbon.Icon icon, String size, Html.AttributeInstruction... attributes) {
    return svg(
        xmlns("http://www.w3.org/2000/svg"),
        fill("currentColor"),
        width(size), height(size), viewBox("0 0 32 32"),

        flatten(attributes),

        raw(icon.raw)
    );
  }

}