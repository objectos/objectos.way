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
import objectos.way.Carbon.HeaderMenuItem;

final class CarbonHeaderMenuItem extends CarbonComponent implements Carbon.HeaderMenuItem {

  private boolean active;

  private String href;

  private boolean sideNav;

  private String text;

  private Script.Action onClick;

  CarbonHeaderMenuItem(Html.Template tmpl) {
    super(tmpl);
  }

  @Override
  public final HeaderMenuItem active(boolean value) {
    active = value;
    return this;
  }

  @Override
  public final HeaderMenuItem href(String value) {
    href = Check.notNull(value, "value == null");
    return this;
  }

  @Override
  public final HeaderMenuItem text(String value) {
    text = Check.notNull(value, "value == null");
    return this;
  }

  final CarbonHeaderMenuItem dataOnClick0(Script.Action value) {
    onClick = value;
    return this;
  }

  final CarbonHeaderMenuItem sideNav() {
    sideNav = true;
    return this;
  }

  @Override
  public final void render() {
    tmpl.li(
        tmpl.a(
            !sideNav
                ? tmpl.flatten(
                    tmpl.className("relative flex h-full select-none items-center"),
                    tmpl.className("border-2 border-transparent"),
                    tmpl.className("bg"),
                    tmpl.className("px-16px"),
                    tmpl.className("text-14px leading-18px tracking-0 font-400"),
                    tmpl.className("transition-colors duration-100"),
                    tmpl.className("active:bg-active active:text-primary"),
                    tmpl.className("focus:border-focus focus:outline-none"),
                    tmpl.className("hover:bg-hover hover:text-primary")
                )
                : tmpl.flatten(
                    tmpl.className("relative flex min-h-32px"),
                    tmpl.className("items-center justify-between whitespace-nowrap"),
                    tmpl.className("border-2 border-transparent"),
                    tmpl.className("px-16px"),
                    tmpl.className("text-heading-compact-01 text-secondary"),
                    tmpl.className("outline outline-2 -outline-offset-2 outline-transparent"),
                    tmpl.className("transition-colors duration-100"),
                    tmpl.className("active:bg-active active:text-primary"),
                    tmpl.className("focus:outline-focus"),
                    tmpl.className("hover:bg-hover hover:text-primary")
                ),

            !sideNav
                ? active
                    ? tmpl.flatten(
                        tmpl.className("text-primary"),
                        tmpl.className("after:absolute after:-bottom-2px after:-left-2px after:-right-2px"),
                        tmpl.className("after:block after:border-b-3 after:border-b-interactive after:content-empty")
                    )
                    : tmpl.className("text-secondary")
                : active
                    ? tmpl.flatten(
                        tmpl.className("after:absolute after:-top-2px after:-bottom-2px after:-left-2px"),
                        tmpl.className("after:block after:border-l-3 after:border-l-interactive after:content-empty")
                    )
                    : tmpl.noop(),

            onClick != null ? tmpl.dataOnClick(onClick) : tmpl.noop(),

            href != null && onClick != null ? tmpl.dataOnClick(Script.location(href)) : tmpl.noop(),

            href != null ? tmpl.href(href) : tmpl.noop(),

            tmpl.tabindex("0"),

            text != null ? tmpl.span(text) : tmpl.noop()
        )
    );
  }

}
