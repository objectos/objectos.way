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
package testing.site.ui;

import objectos.html.Api;
import objectos.html.Api.Element;
import objectos.html.ElementClass;
import objectos.html.ElementId;
import objectos.ui.Ui;
import testing.zite.TestingSiteInjector;

final class ShellPage extends UiTemplate {

  private final Ui ui;

  ShellPage(TestingSiteInjector injector) {
    ui = injector.ui(this);
  }

  private static final ElementClass HEADER = ElementClass.of(
      "fixed inset-0px flex h-48px",
      "border-b border-border-subtle",
      "bg-background"
  );

  private static final ElementClass HEADER_ACTION = ElementClass.of(
      "h-48px w-48px",
      "border border-transparent",
      "active:bg-background-active",
      "focus:border-focus focus:outline-none",
      "hover:bg-background-hover"
  );

  private static final ElementClass HEADER_NAME = ElementClass.of(
      "flex h-full select-none items-center",
      "border-2 border-transparent",
      "pr-32px pl-16px",
      "text-body-compact-01 font-semibold",
      "outline-none",
      "transition-colors duration-100",
      "focus:border-focus"
  );

  private static final ElementClass HEADER_NAME_PREFIX = ElementClass.of(
      "font-normal"
  );

  private static final ElementClass HEADER_NAV = ElementClass.of(
      "relative hidden",
      "pl-16px",
      "before:absolute before:block before:top-12px before:left-0px",
      "before:h-24px before:w-1px",
      "before:bg-border-subtle",
      "before:content-empty",
      "lg:block"
  );

  private static final ElementClass HEADER_MENU_BAR = ElementClass.of(
      "flex h-full"
  );

  private static final ElementClass HEADER_MENU_CLOSE = ElementClass.of(
      "hidden",
      "bg-layer",
      "border-x-border-subtle"
  );

  private static final ElementClass HEADER_MENU_TOGGLE = ElementClass.of(
      "flex items-center justify-center",
      "lg:hidden"
  );

  private static final ElementClass HEADER_MENU_TRIGGER_SVG = ElementClass.of(
      "fill-icon-primary"
  );

  private static final ElementClass SKIP_TO_CONTENT = ElementClass.of(
      "sr-only z-50",
      "text-text-secondary underline",
      "transition-colors duration-100",
      "focus:not-sr-only",
      "focus:absolute focus:flex focus:h-full focus:items-center",
      "focus:border-4 focus:border-focus",
      "focus:bg-background",
      "focus:px-16px",
      "focus:outline-none"
  );

  private static final ElementClass SIDE_NAV_OVERLAY = ElementClass.of(
      "fixed hidden",
      "h-screen w-screen top-48px",
      "bg-overlay",
      "z-30"
  );

  private static final ElementId OPEN = ElementId.of("open-menu");

  private static final ElementId CLOSE = ElementId.of("close-menu");

  private static final ElementId OVERLAY = ElementId.of("overlay-menu");

  @Override
  final void bodyImpl() {
    header(HEADER,
        a(SKIP_TO_CONTENT, href("#main-content"), tabindex("0"), t("Skip to main content")),

        button(OPEN,
            HEADER_ACTION, HEADER_MENU_TOGGLE,
            title("Open Menu"), type("button"),
            openMenu(),
            svg(HEADER_MENU_TRIGGER_SVG,
                openMenu(),
                xmlns("http://www.w3.org/2000/svg"), width("20px"), height("20px"), viewBox("0 0 20 20"),
                path(d("M2 14.8H18V16H2zM2 11.2H18V12.399999999999999H2zM2 7.6H18V8.799999999999999H2zM2 4H18V5.2H2z"))
            )
        ),
        button(CLOSE,
            HEADER_ACTION, HEADER_MENU_CLOSE, HEADER_MENU_TOGGLE,
            title("Close Menu"), type("button"),
            closeMenu(),
            svg(HEADER_MENU_TRIGGER_SVG,
                closeMenu(),
                xmlns("http://www.w3.org/2000/svg"), width("20"), height("20"), viewBox("0 0 32 32"),
                path(d("M17.4141 16L24 9.4141 22.5859 8 16 14.5859 9.4143 8 8 9.4141 14.5859 16 8 22.5859 9.4143 24 16 17.4141 22.5859 24 24 22.5859 17.4141 16z"))
            )
        ),

        a(HEADER_NAME, href("/ui"),
            span(HEADER_NAME_PREFIX, t("o7")),
            raw("&nbsp;"), t("[UI]")
        ),

        nav(HEADER_NAV,
            ul(HEADER_MENU_BAR,
                menuItem("Link 1"),
                menuItem("Link 2"),
                menuItem("Link 3")
            )
        ),

        div(OVERLAY, SIDE_NAV_OVERLAY)
    );

    main(id("main-content"),
        p("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed in rhoncus nunc. Quisque ligula magna, hendrerit id dignissim non, rutrum id lacus. Sed facilisis tempor tellus vel interdum. Duis malesuada vel enim non ultricies. Duis mattis, ante eu vehicula imperdiet, nibh nunc efficitur nulla, sit amet imperdiet orci tortor eget tortor. Maecenas egestas ut ex ac fringilla. Quisque neque orci, pretium et efficitur nec, vehicula non nisi. Aliquam et ullamcorper sem. Praesent non quam id massa porta feugiat."),

        p("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam quis sagittis felis. Aliquam ex nisi, molestie et commodo sit amet, auctor id sem. Mauris suscipit ligula ac consequat aliquam. Ut ornare quam quis placerat pharetra. In tempor mi vel molestie volutpat. Ut a dignissim odio. Phasellus ullamcorper, mauris quis venenatis ullamcorper, dui felis lobortis mi, et pretium ante ipsum eu diam. Quisque congue velit a molestie lacinia. Pellentesque vulputate ut nisl tempor lacinia. Morbi consequat felis vitae feugiat finibus. Praesent lectus purus, pharetra in semper eget, pulvinar vitae sapien.")
    );
  }

  private Api.GlobalAttribute openMenu() {
    return ui.click(
        ui.replaceClass(OPEN, "flex", "hidden"),
        ui.replaceClass(CLOSE, "hidden", "flex"),
        ui.replaceClass(OVERLAY, "hidden", "block")
    );
  }

  private Api.GlobalAttribute closeMenu() {
    return ui.click(
        ui.replaceClass(CLOSE, "flex", "hidden"),
        ui.replaceClass(OPEN, "hidden", "flex"),
        ui.replaceClass(OVERLAY, "block", "hidden")
    );
  }

  private static final ElementClass HEADER_MENU_ITEM = ElementClass.of(
      "flex h-full select-none items-center",
      "border-2 border-transparent",
      "bg-background",
      "px-16px",
      "text-body-compact-01 tracking-normal text-text-secondary",
      "transition-colors duration-100",
      "active:bg-background-active",
      "focus:border-focus focus:outline-none",
      "hover:bg-background-hover"
  );

  private Element menuItem(String text) {
    return li(a(HEADER_MENU_ITEM, href("#"), tabindex("0"),
        span(
            t(text)
        )
    ));
  }

}