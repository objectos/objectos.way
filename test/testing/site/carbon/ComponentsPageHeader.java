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
package testing.site.carbon;

import objectos.way.Http;

final class ComponentsPageHeader extends CarbonPage {

  ComponentsPageHeader(Http.Exchange http) {
    super(http);
  }

  @Override
  protected final void preRender() {
    topSection = TopSection.COMPONENTS;
  }

  @Override
  protected final void renderHead() {
    title("PageHeader - Objectos Carbon");
  }

  @Override
  protected final void renderContent() {
    section(
        className("page-header page-header-title-only"),

        div(
            className("page-header-title-row"),

            h1(
                className("page-header-title"),

                t("Page title")
            ),

            div(
                className("page-header-actions"),

                button(
                    className("button-md button-primary"),
                    type("button"),

                    t("Primary action")
                )
            )
        )
    );

    div(
        className("grid-narrow grid-cols-4 mt-spacing-07"),

        div(
            className("tile min-h-screen col-span-1"),

            t("Column 1")
        ),

        div(
            className("tile min-h-screen col-span-1"),

            t("Column 2")
        ),

        div(
            className("tile min-h-screen col-span-2"),

            t("Column 3")
        )
    );
  }

}