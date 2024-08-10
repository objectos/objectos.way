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

import objectos.way.Carbon.SideNavMenuItem;

final class CarbonSideNavMenuItem implements SideNavMenuItem {

  private final boolean active;

  private final String href;

  private final String text;

  private final Html.TemplateBase tmpl;

  CarbonSideNavMenuItem(Html.TemplateBase tmpl, String text, String href, boolean active) {
    this.tmpl = tmpl;
    this.text = text;
    this.href = href;
    this.active = active;
  }

  private static final Html.ClassName __SIDE_NAV_LINK = Html.className("""
  relative flex min-h-32px
  items-center justify-between whitespace-nowrap
  px-16px
  heading-compact-01
  outline outline-2 -outline-offset-2 outline-transparent
  transition-colors duration-100
  focus:outline-focus
  hover:bg-background-hover hover:text-text-primary
  span:select-none span:text-14px span:leading-20px span:tracking-0.1px span:truncate
  """);

  private static final Html.ClassName __SIDE_NAV_LINK_ACTIVE = Html.className("""
  bg-selected text-link-primary font-600
  after:absolute after:top-0px after:bottom-0px after:left-0px
  after:block after:border-l-3 after:border-l-border-interactive after:content-empty
  span:text-text-primary
  """);

  private static final Html.ClassName __SIDE_NAV_LINK_INACTIVE = Html.className("""
  span:text-text-secondary
  """);

  static final Html.ClassName SIDE_NAV_LINK_ACTIVE = Html.className(
      __SIDE_NAV_LINK, __SIDE_NAV_LINK_ACTIVE
  );

  static final Html.ClassName SIDE_NAV_LINK_INACTIVE = Html.className(
      __SIDE_NAV_LINK, __SIDE_NAV_LINK_INACTIVE
  );

  public final Html.ElementInstruction render(Script.Action closeButtonAction) {
    return tmpl.li(
        tmpl.a(
            active ? SIDE_NAV_LINK_ACTIVE : SIDE_NAV_LINK_INACTIVE,

            closeButtonAction != null ? tmpl.dataOnClick(closeButtonAction) : tmpl.noop(),
            closeButtonAction != null ? tmpl.dataOnClick(Script.location(href)) : tmpl.noop(),

            tmpl.href(href),

            tmpl.span(text)
        )
    );
  }

}
