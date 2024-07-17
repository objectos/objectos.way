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

  private static final String _BUTTON_RESET = "cursor-pointer appearance-none";

  static final Html.ClassName HEADER = Html.className(
      "fixed top-0px right-0px left-0px z-header",
      "flex items-center h-header",
      "border-b border-b-subtle",
      "bg"
  );

  static final Html.ClassName HEADER_MENU_BUTTON = Html.className(
      _BUTTON_RESET,

      // header__action
      "size-header",
      "border border-transparent",
      "transition-colors duration-100",
      "active:bg-active",
      "focus:border-focus focus:outline-none",
      "hover:bg-hover",
      // header__menu-toggle
      "flex items-center justify-center",
      // header__menu-toggle__hidden
      "lg:hidden",
      // header__menu-trigger
      "svg:fill-primary"
  );

  static final Html.ClassName HEADER_MENU_ITEM = Html.className(
      "relative flex h-full select-none items-center",
      "border-2 border-transparent",
      "bg",
      "px-16px",
      "text-14px leading-18px tracking-0 font-400",
      "transition-colors duration-100",
      "active:bg-active active:text-primary",
      "focus:border-focus focus:outline-none",
      "hover:bg-hover hover:text-primary"
  );

  static final Html.ClassName HEADER_MENU_ITEM_ACTIVE = Html.className(
      "text-primary",
      "after:absolute after:-bottom-2px after:-left-2px after:-right-2px",
      "after:block after:border-b-3 after:border-b-interactive after:content-empty"
  );

  static final Html.ClassName HEADER_MENU_ITEM_INACTIVE = Html.className(
      "text-secondary"
  );

  static final Html.ClassName HEADER_NAME = Html.className(
      "flex h-full select-none items-center",
      "border-2 border-transparent",
      "px-16px",
      "text-body-compact-01 text-primary font-600 leading-20px tracking-0.1px",
      "outline-none",
      "transition-colors duration-100",
      "focus:border-focus",
      "lg:pl-16px lg:pr-32px",

      "span:font-400"
  );

  static final Html.ClassName HEADER_NAV = Html.className(
      "relative hidden h-full pl-16px",
      "lg:flex lg:items-center",
      "lg:before:relative lg:before:-left-16px lg:before:z-header lg:before:block",
      "lg:before:h-1/2 lg:before:w-1px",
      "lg:before:border-l lg:before:border-l-subtle",
      "lg:before:content-empty"
  );

  static final Html.ClassName HEADER_NAV_LIST = Html.className(
      "h-full flex text-secondary"
  );

  static final Html.ClassName OVERLAY = Html.className(
      "fixed inset-0px block hidden z-overlay",
      "bg-overlay opacity-0",
      "transition-opacity duration-300"
  );

  static final Html.ClassName SIDE_NAV = Html.className(
      "fixed top-0px bottom-0px left-0px z-header hidden",
      "w-256px",
      "bg",
      "text-secondary"
  );

  static final Html.ClassName SIDE_NAV_HEADER_LIST = Html.className(
  );

  static final Html.ClassName SIDE_NAV_HEADER_ITEM = Html.className(
      "relative flex h-full select-none items-center",
      "border-2 border-transparent",
      "bg",
      "px-16px",
      "text-14px leading-18px tracking-0 font-400",
      "transition-colors duration-100",
      "active:bg-active active:text-primary",
      "focus:border-focus focus:outline-none",
      "hover:bg-hover hover:text-primary"
  );

  static final Html.ClassName SIDE_NAV_HEADER_ITEM_ACTIVE = Html.className(
  );

  static final Html.ClassName SIDE_NAV_HEADER_ITEM_INACTIVE = Html.className(
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
        HEADER,

        pojo.renderTheme(),
        pojo.renderChildren()
    );
  }

  @Override
  final void renderHeaderMenuButton(Pojo pojo) {
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

        tmpl.type("button"),

        thisIcon(Carbon.Icon.MENU, Carbon.IconSize.PX20, ariaHidden()),

        pojo.renderTheme(),
        pojo.renderChildren()
    );
  }

  @Override
  final void renderHeaderMenuItem(Pojo pojo) {
    boolean active;
    active = pojo.booleanValue(Carbon.AttributeKey.IS_ACTIVE);

    String name;
    name = pojo.stringValue(Carbon.AttributeKey.NAME);

    tmpl.li(
        tmpl.a(
            tmpl.className("relative flex h-full select-none items-center"),
            tmpl.className("border-2 border-transparent"),
            tmpl.className("bg"),
            tmpl.className("px-16px"),
            tmpl.className("text-14px leading-18px tracking-0 font-400"),
            tmpl.className("transition-colors duration-100"),
            tmpl.className("active:bg-active active:text-primary"),
            tmpl.className("focus:border-focus focus:outline-none"),
            tmpl.className("hover:bg-hover hover:text-primary"),

            active
                ? tmpl.className("text-primary after:absolute after:-bottom-2px after:-left-2px after:-right-2px after:block after:border-b-3 after:border-b-interactive after:content-empty")
                : tmpl.className("text-secondary"),

            tmpl.tabindex("0"),

            tmpl.span(
                name != null ? tmpl.t(name) : tmpl.noop()
            ),

            pojo.renderTheme(),
            pojo.renderChildren()
        )
    );
  }

  @Override
  final void renderHeaderName(Pojo pojo) {
    String name;
    name = pojo.stringValue(Carbon.AttributeKey.NAME);

    String prefix;
    prefix = pojo.stringValue(Carbon.AttributeKey.PREFIX);

    tmpl.a(
        tmpl.className("flex h-full select-none items-center"),
        tmpl.className("border-2 border-transparent"),
        tmpl.className("px-16px"),
        tmpl.className("text-body-compact-01 text-primary font-600 leading-20px tracking-0.1px"),
        tmpl.className("outline-none"),
        tmpl.className("transition-colors duration-100"),
        tmpl.className("focus:border-focus"),
        tmpl.className("lg:pl-16px lg:pr-32px"),

        prefix != null ? tmpl.span(
            tmpl.className("font-400"),

            tmpl.t(prefix),

            tmpl.raw("&nbsp;")
        ) : tmpl.noop(),

        name != null ? tmpl.t(name) : tmpl.noop(),

        pojo.renderTheme(),
        pojo.renderChildren()
    );
  }

  @Override
  final void renderHeaderNavigation(Pojo pojo) {
    tmpl.nav(
        tmpl.className("relative hidden h-full pl-16px"),
        tmpl.className("lg:flex lg:items-center"),
        tmpl.className("lg:before:relative lg:before:-left-16px lg:before:z-header lg:before:block"),
        tmpl.className("lg:before:h-1/2 lg:before:w-1px"),
        tmpl.className("lg:before:border-l lg:before:border-l-subtle"),
        tmpl.className("lg:before:content-empty"),

        pojo.renderTheme(),

        tmpl.ul(
            tmpl.className("h-full flex"),

            pojo.renderChildren()
        )
    );
  }

  @Override
  final void renderIcon(Pojo pojo, Carbon.Icon icon, Carbon.IconSize size) {
    renderIcon0(pojo, icon, size);
  }

  private Html.ElementInstruction renderIcon0(Pojo pojo, Carbon.Icon icon, Carbon.IconSize size) {
    return tmpl.svg(
        tmpl.xmlns("http://www.w3.org/2000/svg"),
        tmpl.fill("currentColor"),
        tmpl.width(size.size), tmpl.height(size.size), tmpl.viewBox("0 0 32 32"),

        tmpl.raw(
            renderIconRaw(icon, size)
        ),

        pojo.renderChildren()
    );
  }

  private String renderIconRaw(Carbon.Icon icon, Carbon.IconSize size) {
    return switch (icon) {
      case CLOSE -> """
      <polygon points="17.4141 16 24 9.4141 22.5859 8 16 14.5859 9.4143 8 8 9.4141 14.5859 16 8 22.5859 9.4143 24 16 17.4141 22.5859 24 24 22.5859 17.4141 16"/>""";

      case MENU -> """
      <rect x="4" y="6" width="24" height="2"/><rect x="4" y="24" width="24" height="2"/><rect x="4" y="12" width="24" height="2"/><rect x="4" y="18" width="24" height="2"/>""";
    };
  }

  @Override
  final void renderOverlay(Pojo pojo) {
    tmpl.div(
        tmpl.className("fixed inset-0px block hidden z-overlay"),
        tmpl.className("bg-overlay opacity-0"),
        tmpl.className("transition-opacity duration-300"),

        headerRendered ? tmpl.className("mt-header") : tmpl.noop(),

        pojo.renderTheme(),
        pojo.renderChildren()
    );
  }

  @Override
  final void renderSideNav(Pojo pojo) {
    tmpl.nav(
        tmpl.className("fixed top-0px bottom-0px left-0px z-header hidden"),
        tmpl.className("w-256px"),
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

  private Html.Instruction thisIcon(Carbon.Icon icon, Carbon.IconSize size, Carbon.Component... components) {
    return renderIcon0(
        new Pojo(components) {
          @Override
          final void render() {}
        },

        icon, size
    );
  }

}