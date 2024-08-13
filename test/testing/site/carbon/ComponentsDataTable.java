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
  protected final Carbon.ShellContent renderContent() {
    return carbon.shellContent(
        carbon.dataFrame("main", getClass().getSimpleName())
    );
  }

  protected final void renderContent0() {
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
    h2(
        className("heading-03 my-spacing-05"),

        t("Basic")
    );

    div(
        className("data-table-content"),
        tabindex("0"),

        table(
            className("data-table"),
            ariaLabel("sample table"),

            f(this::tableContents)
        )
    );

    h2(
        className("heading-03 my-spacing-05"),

        t("Basic (xs)")
    );

    div(
        className("data-table-content"),
        tabindex("0"),

        table(
            className("data-table-xs"),
            ariaLabel("sample table"),

            f(this::tableContents)
        )
    );

    h2(
        className("heading-03 my-spacing-05"),

        t("Basic (sm)")
    );

    div(
        className("data-table-content"),
        tabindex("0"),

        table(
            className("data-table-sm"),
            ariaLabel("sample table"),

            f(this::tableContents)
        )
    );

    h2(
        className("heading-03 my-spacing-05"),

        t("Basic (md)")
    );

    div(
        className("data-table-content"),
        tabindex("0"),

        table(
            className("data-table-md"),
            ariaLabel("sample table"),

            f(this::tableContents)
        )
    );

    h2(
        className("heading-03 my-spacing-05"),

        t("Basic (xl)")
    );

    div(
        className("data-table-content"),
        tabindex("0"),

        table(
            className("data-table-xl"),
            ariaLabel("sample table"),

            f(this::tableContents)
        )
    );

    div(
        className("data-table-container my-spacing-05"),

        div(
            className("data-table-header"),

            h2(
                className("data-table-header-title"),

                t("DataTable header")
            )
        ),

        table(
            className("data-table"),
            ariaLabel("sample table"),

            f(this::tableContents)
        )
    );

    div(
        className("data-table-container my-spacing-05"),

        div(
            className("data-table-header"),

            h2(
                className("data-table-header-title"),

                t("DataTable header")
            ),

            p(
                className("data-table-header-description"),

                t("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent aliquam mi lacus, vel posuere eros lacinia vel. Suspendisse potenti. Donec eu porta est. Praesent aliquam nisl a sapien ultricies egestas. Duis luctus erat vel mi porttitor, et semper ligula placerat. Integer imperdiet, dui ac mattis mattis, quam libero dapibus nulla, sit amet malesuada justo nisl lobortis nibh. Maecenas eleifend lectus nibh, nec viverra diam pretium eu. Nam ultricies velit ante, maximus sodales magna pulvinar ut. Vivamus in iaculis velit. Nam quis accumsan erat. Etiam tristique eget libero sed varius. Ut sodales mollis varius. Ut bibendum in ex non pellentesque. Duis congue, sem molestie commodo consequat, lectus enim semper nunc, vel luctus lacus justo nec elit. Nullam porta, elit a auctor euismod, tellus elit pulvinar augue, eget cursus eros sapien at orci. Nullam blandit erat nec blandit feugiat.")
            )
        ),

        table(
            className("data-table"),
            ariaLabel("sample table"),

            f(this::tableContents)
        )
    );
  }

  private void tableContents() {
    thead(
        tr(
            th(/*scope("col"), */ t("Name")),
            th(/*scope("col"), */ t("Rule")),
            th(/*scope("col"), */ t("Status")),
            th(/*scope("col"), */ t("Other")),
            th(/*scope("col"), */ t("Example"))
        )
    );

    tbody(
        /*ariaLive("polite"),*/
        tr(td("Load Balancer 1"), td("Round robin"), td("Starting"), td("Test"), td("22")),
        tr(td("Load Balancer 2"), td("DNS delegation"), td("Active"), td("Test"), td("22")),
        tr(td("Load Balancer 3"), td("Round robin"), td("Disabled"), td("Test"), td("22")),
        tr(td("Load Balancer 4"), td("Round robin"), td("Disabled"), td("Test"), td("22")),
        tr(td("Load Balancer 5"), td("Round robin"), td("Disabled"), td("Test"), td("22")),
        tr(td("Load Balancer 6"), td("Round robin"), td("Disabled"), td("Test"), td("22")),
        tr(td("Load Balancer 7"), td("Round robin"), td("Disabled"), td("Test"), td("22"))
    );
  }

}