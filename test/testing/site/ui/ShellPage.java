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
    header(className("h-12"),
        a(className("sr-only focus:not-sr-only"), href("#content"), tabindex("0"), t("Skip to content")),

        nav(
            ul(
                menuItem("Link 1"),
                menuItem("Link 2"),
                menuItem("Link 3")
            )
        )
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