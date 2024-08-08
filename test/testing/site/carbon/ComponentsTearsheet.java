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

import objectos.way.Carbon;
import objectos.way.Html;
import objectos.way.Http;

final class ComponentsTearsheet extends CarbonPage {

  ComponentsTearsheet(Http.Exchange http) {
    super(http);
  }

  @Override
  protected final void preRender() {
    topSection = TopSection.COMPONENTS;
  }

  @Override
  protected final void renderHead() {
    title("Tearsheet - Objectos Carbon");
  }

  private static final Html.Id TEARSHEET_01 = Html.id("tearsheet-01");

  @Override
  protected final void renderContent() {
    section(
        className("page-header page-header-title-only"),

        div(
            className("page-header-title-row"),

            h1(
                className("page-header-title"),

                t("Tearsheet")
            )
        )
    );

    div(
        TEARSHEET_01, className("tearsheet-hidden"), noop("tearsheet tearsheet-transition"),
        role("presentation"), ariaHidden("true"),

        div(
            className("tearsheet-container"),
            role("dialog"), /*ariaModal("true"),*/ ariaLabel("Example 01"),

            div(
                className("tearsheet-header"),

                h3(
                    className("tearsheet-header-title"),

                    t("Create topic")
                ),

                p(
                    className("tearsheet-header-description"),

                    t("Specify details for the new topic you want to create")
                )
            ),

            div(
                className("tearsheet-body")
            )
        )
    );

    div(
        className("grid-wide mt-spacing-05 grid-cols-2 items-center"),

        f(this::renderExamples)
    );
  }

  private void renderExamples() {
    h2(
        className("heading-03"),

        t("Title + description + influencer")
    );

    button(
        className("button-primary"), type("button"),

        dataOnClick(Carbon.openTearsheet(TEARSHEET_01)),

        t("Open Tearsheet")
    );
  }

}