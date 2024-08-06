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

final class ComponentsDataTable extends CarbonPage {

  ComponentsDataTable(Http.Exchange http) {
    super(http);
  }

  @Override
  protected final void preRender() {
    topSection = TopSection.COMPONENTS;
  }

  @Override
  protected final void renderHead() {
    title("DataTable - Objectos Carbon");
  }

  @Override
  protected final void renderContent() {
    section(
        className("page-header page-header-title-only"),

        div(
            className("page-header-title-row"),

            h1(
                className("page-header-title"),

                t("Data table")
            )
        )
    );

    div(
        className("grid-wide grid-cols-1"),

        f(this::renderTables)
    );
  }

  private void renderTables() {
    div(
        className("data-table-content"),
        tabindex("0"),

        table(
            className("data-table"),
            ariaLabel("sample table"),

            thead(
                tr(
                    th(/*scope("col"), */ t("Name")),
                    th(/*scope("col"), */ t("Rule")),
                    th(/*scope("col"), */ t("Status")),
                    th(/*scope("col"), */ t("Other")),
                    th(/*scope("col"), */ t("Example"))
                )
            ),

            tbody(
                /*ariaLive("polite"),*/
                tr(td("Load Balancer 1"), td("Round robin"), td("Starting"), td("Test"), td("22")),
                tr(td("Load Balancer 2"), td("DNS delegation"), td("Active"), td("Test"), td("22")),
                tr(td("Load Balancer 3"), td("Round robin"), td("Disabled"), td("Test"), td("22")),
                tr(td("Load Balancer 4"), td("Round robin"), td("Disabled"), td("Test"), td("22")),
                tr(td("Load Balancer 5"), td("Round robin"), td("Disabled"), td("Test"), td("22")),
                tr(td("Load Balancer 6"), td("Round robin"), td("Disabled"), td("Test"), td("22")),
                tr(td("Load Balancer 7"), td("Round robin"), td("Disabled"), td("Test"), td("22"))
            )
        )
    );
  }

}