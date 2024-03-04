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

import objectos.html.Api.Element;
import objectos.html.ClassName;
import testing.zite.TestingSiteInjector;

final class ShellPage extends UiTemplate {

  ShellPage(TestingSiteInjector injector) {

  }

  private static final ClassName HEADER = ClassName.of(
      "fixed inset-0px flex h-48px border-b border-border-subtle bg-background"
  );

  private static final ClassName HEADER_NAME = ClassName.of(
      "flex h-full items-center border-2 border-transparent pr-32px pl-16px text-body-compact-01 font-semibold outline-none focus:border-focus"
  );

  private static final ClassName HEADER_NAME_PREFIX = ClassName.of(
      "font-normal"
  );

  private static final ClassName HEADER_NAV = ClassName.of(
      "relative hidden lg:block"
  );

  private static final ClassName SKIP_TO_CONTENT = ClassName.of(
      "sr-only text-text-secondary underline focus:not-sr-only focus:absolute focus:flex focus:h-full focus:items-center focus:border-4 focus:border-focus focus:bg-background focus:px-16px focus:outline-none"
  );

  @Override
  final void bodyImpl() {
    header(HEADER,
        a(SKIP_TO_CONTENT,
            href("#main-content"),
            tabindex("0"),
            t("Skip to main content")
        ),

        a(HEADER_NAME, href("/ui"),
            span(HEADER_NAME_PREFIX, t("o7")),
            raw("&nbsp;"), t("[UI]")
        ),

        nav(HEADER_NAV,
            ul(
                menuItem("Link 1"),
                menuItem("Link 2"),
                menuItem("Link 3")
            )
        )
    );

    main(id("main-content"),
        p("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed in rhoncus nunc. Quisque ligula magna, hendrerit id dignissim non, rutrum id lacus. Sed facilisis tempor tellus vel interdum. Duis malesuada vel enim non ultricies. Duis mattis, ante eu vehicula imperdiet, nibh nunc efficitur nulla, sit amet imperdiet orci tortor eget tortor. Maecenas egestas ut ex ac fringilla. Quisque neque orci, pretium et efficitur nec, vehicula non nisi. Aliquam et ullamcorper sem. Praesent non quam id massa porta feugiat."),

        p("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam quis sagittis felis. Aliquam ex nisi, molestie et commodo sit amet, auctor id sem. Mauris suscipit ligula ac consequat aliquam. Ut ornare quam quis placerat pharetra. In tempor mi vel molestie volutpat. Ut a dignissim odio. Phasellus ullamcorper, mauris quis venenatis ullamcorper, dui felis lobortis mi, et pretium ante ipsum eu diam. Quisque congue velit a molestie lacinia. Pellentesque vulputate ut nisl tempor lacinia. Morbi consequat felis vitae feugiat finibus. Praesent lectus purus, pharetra in semper eget, pulvinar vitae sapien.")
    );
  }

  private Element menuItem(String text) {
    return li(a(href("#"), tabindex("0"),
        span(
            t(text)
        )
    ));
  }

}