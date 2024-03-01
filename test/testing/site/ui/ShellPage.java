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
import testing.zite.TestingSiteInjector;

final class ShellPage extends UiTemplate {

  ShellPage(TestingSiteInjector injector) {

  }

  @Override
  final void bodyImpl() {
    header(className("flex h-12 border-b border-border-subtle"),
        a(className("sr-only underline focus:not-sr-only focus:flex focus:h-full focus:items-center focus:border-4 focus:border-focus focus:outline-none"), href("#content"), tabindex("0"),
            t("Skip to content")
        ),

        a(className(""), href("/ui"),
            span(className(""),
                t("Objectos")
            ), t(" UI")
        ),

        nav(
            ul(
                menuItem("Link 1"),
                menuItem("Link 2"),
                menuItem("Link 3")
            )
        )
    );

    main(id("content"),
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