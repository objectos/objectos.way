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

final class CarbonUi extends Carbon.Ui {

  private boolean headerRendered;

  CarbonUi(Html.Template tmpl) {
    super(tmpl);
  }

  private static final Html.ClassName BUTTON_RESET = Html.className(
      "cursor-pointer", "appearance-none"
  );

  @Override
  final void renderContent(Pojo pojo) {
    tmpl.main(
        headerRendered ? tmpl.className("mt-header") : tmpl.noop(),

        pojo.renderTheme(),

        pojo.renderChildren()
    );
  }

  @Override
  final void renderHeader(Pojo pojo) {
    headerRendered = true;

    tmpl.header(
        tmpl.className("fixed inset-0px flex h-header"),
        tmpl.className("border-b border-subtle"),
        tmpl.className("bg"),

        pojo.renderTheme(),

        pojo.renderChildren()
    );
  }

  @Override
  final void renderHeaderMenuButton(Pojo pojo) {
    String title;
    title = pojo.stringValue(Carbon.AttributeKey.TITLE);

    tmpl.button(
        BUTTON_RESET,

        // header__action
        tmpl.className("size-header"),
        tmpl.className("border border-transparent"),
        tmpl.className("transition-colors duration-100"),
        tmpl.className("active:bg-active"),
        tmpl.className("focus:border-focus focus:outline-none"),
        tmpl.className("hover:bg-hover"),
        // header__menu-toggle
        tmpl.className("flex items-center justify-center"),
        // header__menu-toggle__hidden
        tmpl.className("lg:hidden"),
        // header__menu-trigger
        tmpl.className("svg:fill-primary"),

        title != null ? tmpl.ariaLabel(title) : tmpl.noop(),
        title != null ? tmpl.title(title) : tmpl.noop(),

        tmpl.type("button"),

        tmpl.include(() -> renderIcon(Carbon.Icon.MENU, Carbon.IconSize.PX20))
    );
  }

  @Override
  final void renderHeaderMenuItem(Pojo pojo) {
    boolean active;
    active = pojo.booleanValue(Carbon.AttributeKey.IS_ACTIVE);

    String href;
    href = pojo.stringValue(Carbon.AttributeKey.HREF);

    String name;
    name = pojo.stringValue(Carbon.AttributeKey.NAME);

    tmpl.li(
        tmpl.a(
            tmpl.className("relative flex h-32px select-none items-center"),
            tmpl.className("border-2 border-transparent"),
            tmpl.className("bg"),
            tmpl.className("px-16px"),
            tmpl.className("text-heading-compact-01"),
            tmpl.className("transition-colors duration-100"),
            tmpl.className("active:bg-active active:text-primary"),
            tmpl.className("focus:border-focus focus:outline-none"),
            tmpl.className("hover:bg-hover hover:text-primary"),
            tmpl.className("lg:h-full"),
            tmpl.className("lg:text-body-compact-01 lg:tracking-normal"),

            active
                ? tmpl.className("text-primary after:absolute after:-bottom-2px after:left-0px after:block after:w-full after:border-b-3 after:border-b-interactive after:content-empty")
                : tmpl.className("text-secondary"),

            href != null ? tmpl.href(href) : tmpl.noop(),

            tmpl.tabindex("0"),

            tmpl.span(
                name != null ? tmpl.t(name) : tmpl.noop()
            )
        )
    );
  }

  @Override
  final void renderHeaderName(Pojo pojo) {
    String href;
    href = pojo.stringValue(Carbon.AttributeKey.HREF);

    String name;
    name = pojo.stringValue(Carbon.AttributeKey.NAME);

    String prefix;
    prefix = pojo.stringValue(Carbon.AttributeKey.PREFIX);

    tmpl.a(
        tmpl.className("flex h-full select-none items-center"),
        tmpl.className("border-2 border-transparent"),
        tmpl.className("pr-32px pl-16px"),
        tmpl.className("text-body-compact-01 font-semibold"),
        tmpl.className("outline-none"),
        tmpl.className("transition-colors duration-100"),
        tmpl.className("focus:border-focus"),

        pojo.renderTheme(),

        href != null ? tmpl.href(href) : tmpl.noop(),

        prefix != null ? tmpl.span(
            tmpl.className("font-normal"),

            tmpl.t(prefix),

            tmpl.raw("&nbsp;")
        ) : tmpl.noop(),

        name != null ? tmpl.t(name) : tmpl.noop()
    );
  }

  @Override
  final void renderHeaderNavigation(Pojo pojo) {
    tmpl.nav(
        tmpl.className("hidden"),
        tmpl.className("w-256px top-header bottom-0px"),
        tmpl.className("flex-col"),
        tmpl.className("bg"),
        tmpl.className("lg:block"),
        tmpl.className("lg:bg-transparent"),
        tmpl.className("lg:pl-16px"),
        tmpl.className("lg:before:absolute lg:before:block lg:before:top-12px lg:before:left-0px"),
        tmpl.className("lg:before:h-24px lg:before:w-1px"),
        tmpl.className("lg:before:bg-border-subtle"),
        tmpl.className("lg:before:content-empty"),

        pojo.renderTheme(),

        tmpl.ul(
            tmpl.className("flex flex-col h-full"),
            tmpl.className("pt-16px"),
            tmpl.className("lg:flex-row"),
            tmpl.className("lg:pt-0px"),

            pojo.renderChildren()
        )
    );
  }

  @Override
  final void renderIcon(Carbon.Icon icon, Carbon.IconSize size) {
    switch (icon) {
      case MENU -> {
        switch (size) {
          case PX16 -> icon16("""
          <rect x="2" y="12" width="12" height="1"/><rect x="2" y="9" width="12" height="1"/><rect x="2" y="6" width="12" height="1"/><rect x="2" y="3" width="12" height="1"/>""");
          case PX20 -> icon20("""
          <rect x="2" y="14.8" width="16" height="1.2"/><rect x="2" y="11.2" width="16" height="1.2"/><rect x="2" y="7.6" width="16" height="1.2"/><rect x="2" y="4" width="16" height="1.2"/>""");
          case PX24 -> icon24("""
          <rect x="3" y="18" width="18" height="1.5"/><rect x="3" y="13.5" width="18" height="1.5"/><rect x="3" y="9" width="18" height="1.5"/><rect x="3" y="4.5" width="18" height="1.5"/>""");
          case PX32 -> icon32("""
          <rect x="4" y="6" width="24" height="2"/><rect x="4" y="24" width="24" height="2"/><rect x="4" y="12" width="24" height="2"/><rect x="4" y="18" width="24" height="2"/>""");
        }
      }
    }
  }

  private Html.ElementInstruction icon16(String raw) {
    return tmpl.svg(
        tmpl.xmlns("http://www.w3.org/2000/svg"),
        tmpl.fill("currentColor"),
        tmpl.width("16px"), tmpl.height("16px"), tmpl.viewBox("0 0 16 16"),
        tmpl.raw(raw)
    );
  }

  private Html.ElementInstruction icon20(String raw) {
    return tmpl.svg(
        tmpl.xmlns("http://www.w3.org/2000/svg"),
        tmpl.fill("currentColor"),
        tmpl.width("20px"), tmpl.height("20px"), tmpl.viewBox("0 0 20 20"),
        tmpl.raw(raw)
    );
  }

  private Html.ElementInstruction icon24(String raw) {
    return tmpl.svg(
        tmpl.xmlns("http://www.w3.org/2000/svg"),
        tmpl.fill("currentColor"),
        tmpl.width("24px"), tmpl.height("24px"), tmpl.viewBox("0 0 24 24"),
        tmpl.raw(raw)
    );
  }

  private Html.ElementInstruction icon32(String raw) {
    return tmpl.svg(
        tmpl.xmlns("http://www.w3.org/2000/svg"),
        tmpl.fill("currentColor"),
        tmpl.width("32px"), tmpl.height("32px"), tmpl.viewBox("0 0 32 32"),
        tmpl.raw(raw)
    );
  }

  @Override
  final void renderSideNav(Pojo pojo) {
    tmpl.div(
        tmpl.className("fixed h-screen w-screen top-header"),
        tmpl.className("bg-overlay"),
        tmpl.className("transition-opacity duration-300"),
        //"opacity-0 z-30"

        headerRendered ? tmpl.className("mt-header") : tmpl.noop()
    );

    tmpl.nav(
        tmpl.className("fixed bottom-0px w-256px"),
        tmpl.className("bg"),
        tmpl.className("text-secondary"),

        headerRendered ? tmpl.className("mt-header") : tmpl.noop(),

        tmpl.tabindex("-1"),

        pojo.renderTheme(),

        pojo.renderChildren()
    );
  }

  @Override
  final void renderSideNavItems(Pojo pojo) {
    tmpl.div(
    );
  }

  @Override
  final void renderSideNavMenuItem(Pojo pojo) {
    tmpl.div(
    );
  }

}