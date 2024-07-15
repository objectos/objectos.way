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

    String ariaLabel;
    ariaLabel = pojo.stringValue(Carbon.AttributeKey.ARIA_LABEL);

    tmpl.header(
        tmpl.className("fixed top-0px right-0px left-0px z-header"),
        tmpl.className("flex items-center h-header"),
        tmpl.className("border-b border-b-subtle"),
        tmpl.className("bg"),

        ariaLabel != null ? tmpl.ariaLabel(ariaLabel) : tmpl.noop(),

        pojo.renderTheme(),

        pojo.renderChildren()
    );
  }

  @Override
  final void renderHeaderMenuButton(Pojo pojo) {
    String ariaLabel;
    ariaLabel = pojo.stringValue(Carbon.AttributeKey.ARIA_LABEL);

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

        ariaLabel != null ? tmpl.ariaLabel(ariaLabel) : tmpl.noop(),

        title != null ? tmpl.title(title) : tmpl.noop(),

        tmpl.type("button"),

        thisIcon(Carbon.Icon.MENU, Carbon.IconSize.PX20, ariaHidden())
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

    tmpl.a(
        tmpl.className("relative flex h-full select-none items-center"),
        tmpl.className("border-2 border-transparent"),
        tmpl.className("bg"),
        tmpl.className("px-16px"),
        tmpl.className("text-14px/18px/normal/400"),
        tmpl.className("transition-colors duration-100"),
        tmpl.className("active:bg-active active:text-primary"),
        tmpl.className("focus:border-focus focus:outline-none"),
        tmpl.className("hover:bg-hover hover:text-primary"),

        active
            ? tmpl.className("text-primary after:absolute after:-bottom-2px after:-left-2px after:-right-2px after:block after:border-b-3 after:border-b-interactive after:content-empty")
            : tmpl.className("text-secondary"),

        href != null ? tmpl.href(href) : tmpl.noop(),

        tmpl.tabindex("0"),

        tmpl.span(
            name != null ? tmpl.t(name) : tmpl.noop()
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
        tmpl.className("px-16px"),
        tmpl.className("text-body-compact-01 text-primary font-600 leading-20px tracing-0.1px"),
        tmpl.className("outline-none"),
        tmpl.className("transition-colors duration-100"),
        tmpl.className("focus:border-focus"),
        tmpl.className("lg:pl-16px lg:pr-32px"),

        pojo.renderTheme(),

        href != null ? tmpl.href(href) : tmpl.noop(),

        prefix != null ? tmpl.span(
            tmpl.className("font-400"),

            tmpl.t(prefix),

            tmpl.raw("&nbsp;")
        ) : tmpl.noop(),

        name != null ? tmpl.t(name) : tmpl.noop()
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

        pojo.renderChildren()
    );
  }

  @Override
  final void renderIcon(Pojo pojo, Carbon.Icon icon, Carbon.IconSize size) {
    renderIcon0(pojo, icon, size);
  }

  private Html.ElementInstruction renderIcon0(Pojo pojo, Carbon.Icon icon, Carbon.IconSize size) {
    boolean ariaHidden;
    ariaHidden = pojo.booleanValue(Carbon.AttributeKey.ARIA_HIDDEN);

    return tmpl.svg(
        tmpl.xmlns("http://www.w3.org/2000/svg"),
        tmpl.fill("currentColor"),
        tmpl.width(size.size), tmpl.height(size.size), tmpl.viewBox(size.viewBox),

        ariaHidden ? tmpl.ariaHidden("true") : tmpl.noop(),

        tmpl.raw(
            renderIconRaw(icon, size)
        )
    );
  }

  private String renderIconRaw(Carbon.Icon icon, Carbon.IconSize size) {
    return switch (icon) {
      case MENU -> switch (size) {
        case PX16 -> """
          <rect x="2" y="12" width="12" height="1"/><rect x="2" y="9" width="12" height="1"/><rect x="2" y="6" width="12" height="1"/><rect x="2" y="3" width="12" height="1"/>""";
        case PX20 -> """
          <rect x="2" y="14.8" width="16" height="1.2"/><rect x="2" y="11.2" width="16" height="1.2"/><rect x="2" y="7.6" width="16" height="1.2"/><rect x="2" y="4" width="16" height="1.2"/>""";
        case PX24 -> """
          <rect x="3" y="18" width="18" height="1.5"/><rect x="3" y="13.5" width="18" height="1.5"/><rect x="3" y="9" width="18" height="1.5"/><rect x="3" y="4.5" width="18" height="1.5"/>""";
        case PX32 -> """
          <rect x="4" y="6" width="24" height="2"/><rect x="4" y="24" width="24" height="2"/><rect x="4" y="12" width="24" height="2"/><rect x="4" y="18" width="24" height="2"/>""";
      };
    };
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