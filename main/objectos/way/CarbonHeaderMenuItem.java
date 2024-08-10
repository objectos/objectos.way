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

import objectos.way.Carbon.HeaderMenuItem;
import objectos.way.Html.TemplateBase;

final class CarbonHeaderMenuItem implements HeaderMenuItem {

  private final boolean active;

  private final String href;

  private final String text;

  private final TemplateBase tmpl;

  CarbonHeaderMenuItem(Html.TemplateBase tmpl, String text, String href, boolean active) {
    this.tmpl = tmpl;
    this.text = text;
    this.href = href;
    this.active = active;
  }

  private static final Html.ClassName __HEADER_NAV_LINK = Html.className("""
  relative flex h-full select-none items-center
  border-2 border-transparent
  bg-background
  px-16px
  transition-colors duration-100
  active:bg-background-active active:text-text-primary
  focus:border-focus focus:outline-none
  hover:bg-background-hover hover:text-text-primary
  """);

  private static final Html.ClassName __HEADER_NAV_LINK_ACTIVE = Html.className("""
  text-text-primary
  after:absolute after:-bottom-2px after:-left-2px after:-right-2px
  after:block after:border-b-3 after:border-b-border-interactive after:content-empty
  """);

  private static final Html.ClassName __HEADER_NAV_LINK_INACTIVE = Html.className("""
  text-text-secondary
  """);

  private static final Html.ClassName __HEADER_NAV_LINK_PRODUCTIVE = Html.className("""
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

  public final Html.ElementInstruction renderHeader(boolean expressive) {
    return tmpl.li(
        tmpl.a(
            active
                ? expressive ? HEADER_NAV_LINK_ACTIVE_EXPRESSIVE : HEADER_NAV_LINK_ACTIVE
                : expressive ? HEADER_NAV_LINK_INACTIVE_EXPRESSIVE : HEADER_NAV_LINK_INACTIVE,

            tmpl.href(href),

            tmpl.tabindex("0"),

            tmpl.span(text)
        )
    );
  }

  static final Html.ClassName SIDE_NAV_HEADER_ITEM = Html.className("""
  w-auto h-auto overflow-hidden
  """);

  private static final Html.ClassName __SIDE_NAV_HEADER_LINK = Html.className("""
  relative flex min-h-32px
  items-center justify-between whitespace-nowrap
  border-2 border-transparent
  px-16px
  heading-compact-01 text-text-secondary
  outline outline-2 -outline-offset-2 outline-transparent
  transition-colors duration-100
  active:bg-background-active active:text-text-primary
  focus:outline-focus
  hover:bg-background-hover hover:text-text-primary
  """);

  private static final Html.ClassName __SIDE_NAV_HEADER_LINK_ACTIVE = Html.className("""
  after:absolute after:-top-2px after:-bottom-2px after:-left-2px
  after:block after:border-l-3 after:border-l-border-interactive after:content-empty
  """);

  private static final Html.ClassName __SIDE_NAV_HEADER_LINK_INACTIVE = Html.className("""
  text-text-secondary
  """);

  static final Html.ClassName SIDE_NAV_HEADER_LINK_ACTIVE = Html.className(
      __SIDE_NAV_HEADER_LINK, __SIDE_NAV_HEADER_LINK_ACTIVE
  );

  static final Html.ClassName SIDE_NAV_HEADER_LINK_INACTIVE = Html.className(
      __SIDE_NAV_HEADER_LINK, __SIDE_NAV_HEADER_LINK_INACTIVE
  );

  public final Html.ElementInstruction renderSideNav(Script.Action closeButtonAction, boolean expressive) {
    return tmpl.li(
        SIDE_NAV_HEADER_ITEM,

        tmpl.a(
            active ? SIDE_NAV_HEADER_LINK_ACTIVE : SIDE_NAV_HEADER_LINK_INACTIVE,

            closeButtonAction != null ? tmpl.dataOnClick(closeButtonAction) : tmpl.noop(),
            closeButtonAction != null ? tmpl.dataOnClick(Script.location(href)) : tmpl.noop(),

            tmpl.href(href),

            tmpl.tabindex("0"),

            tmpl.span(text)
        )
    );
  }

}