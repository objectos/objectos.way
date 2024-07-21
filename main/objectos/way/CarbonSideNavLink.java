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
import objectos.way.Carbon.SideNavLink;

final class CarbonSideNavLink extends CarbonComponent implements SideNavLink {

  private boolean active;

  private String href;

  private String text;

  CarbonSideNavLink(Html.Template tmpl) {
    super(tmpl);
  }

  @Override
  public final SideNavLink active(boolean value) {
    active = value;
    return this;
  }

  @Override
  public final SideNavLink href(String value) {
    href = Check.notNull(value, "value == null");
    return this;
  }

  @Override
  public final SideNavLink text(String value) {
    text = Check.notNull(value, "value == null");
    return this;
  }

  @Override
  public final void render() {
    tmpl.li(
        tmpl.className("w-auto h-auto overflow-hidden"),

        tmpl.a(
            tmpl.className("relative flex min-h-32px"),
            tmpl.className("items-center justify-between whitespace-nowrap"),
            tmpl.className("px-16px"),
            tmpl.className("text-heading-compact-01"),
            tmpl.className("outline outline-2 -outline-offset-2 outline-transparent"),
            tmpl.className("transition-colors duration-100"),

            tmpl.className("focus:outline-focus"),
            tmpl.className("hover:bg-hover hover:text-primary"),

            active
                ? tmpl.flatten(
                    tmpl.className("bg-selected text-link-primary font-600"),
                    tmpl.className("after:absolute after:top-0px after:bottom-0px after:left-0px"),
                    tmpl.className("after:block after:border-l-3 after:border-l-interactive after:content-empty")
                )
                : tmpl.noop(),

            href != null ? tmpl.href(href) : tmpl.noop(),

            text != null
                ? tmpl.span(
                    tmpl.className("select-none text-14px leading-20px tracking-0.1px truncate"),

                    active
                        ? tmpl.className("text-primary")
                        : tmpl.className("text-secondary"),

                    tmpl.t(text)
                )
                : tmpl.noop()
        )
    );
  }

}
