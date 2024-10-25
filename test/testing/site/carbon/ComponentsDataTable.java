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
import objectos.way.Css;

@Css.Source
final class ComponentsDataTable extends CarbonPage {

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
    carbon.pageHeader(
        Carbon.PAGE_HEADER_TITLE_ONLY,

        carbon.pageHeaderTitleRow(
            carbon.pageHeaderTitle("Data table")
        )
    );

    carbon.grid(
        Carbon.WIDE,
        carbon.gridColumns(1), carbon.gap(Carbon.SPACING_05),

        h2(
            Carbon.HEADING_03,

            text("Basic")
        ),

        carbon.dataTable(
            ariaLabel("sample table"),

            carbon.dataTableHead(
                include(this::tableHead)
            ),

            carbon.dataTableBody(
                include(this::tableBody)
            )
        ),

        h2(
            Carbon.HEADING_03,

            text("Basic (xs)")
        ),

        carbon.dataTable(
            Carbon.XS,
            ariaLabel("sample table"),

            carbon.dataTableHead(
                include(this::tableHead)
            ),

            carbon.dataTableBody(
                include(this::tableBody)
            )
        ),

        h2(
            Carbon.HEADING_03,

            text("Basic (sm)")
        ),

        carbon.dataTable(
            Carbon.SM,
            ariaLabel("sample table"),

            carbon.dataTableHead(
                include(this::tableHead)
            ),

            carbon.dataTableBody(
                include(this::tableBody)
            )
        ),

        h2(
            Carbon.HEADING_03,

            text("Basic (md)")
        ),

        carbon.dataTable(
            Carbon.MD,
            ariaLabel("sample table"),

            carbon.dataTableHead(
                include(this::tableHead)
            ),

            carbon.dataTableBody(
                include(this::tableBody)
            )
        ),

        h2(
            Carbon.HEADING_03,

            text("Basic (xl)")
        ),

        carbon.dataTable(
            Carbon.XL,
            ariaLabel("sample table"),

            carbon.dataTableHead(
                include(this::tableHead)
            ),

            carbon.dataTableBody(
                include(this::tableBody)
            )
        )
    );
  }

  @SuppressWarnings("unused")
  private void renderTables() {
    div(
        className("data-table-container my-spacing-05"),

        div(
            className("data-table-header"),

            h2(
                className("data-table-header-title"),

                text("DataTable header")
            )
        ),

        table(
            className("data-table"),
            ariaLabel("sample table"),

            include(this::tableContents)
        )
    );

    div(
        className("data-table-container my-spacing-05"),

        div(
            className("data-table-header"),

            h2(
                className("data-table-header-title"),

                text("DataTable header")
            ),

            p(
                className("data-table-header-description"),

                text(
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent aliquam mi lacus, vel posuere eros lacinia vel. Suspendisse potenti. Donec eu porta est. Praesent aliquam nisl a sapien ultricies egestas. Duis luctus erat vel mi porttitor, et semper ligula placerat. Integer imperdiet, dui ac mattis mattis, quam libero dapibus nulla, sit amet malesuada justo nisl lobortis nibh. Maecenas eleifend lectus nibh, nec viverra diam pretium eu. Nam ultricies velit ante, maximus sodales magna pulvinar ut. Vivamus in iaculis velit. Nam quis accumsan erat. Etiam tristique eget libero sed varius. Ut sodales mollis varius. Ut bibendum in ex non pellentesque. Duis congue, sem molestie commodo consequat, lectus enim semper nunc, vel luctus lacus justo nec elit. Nullam porta, elit a auctor euismod, tellus elit pulvinar augue, eget cursus eros sapien at orci. Nullam blandit erat nec blandit feugiat.")
            )
        ),

        table(
            className("data-table"),
            ariaLabel("sample table"),

            include(this::tableContents)
        )
    );
  }

  private void tableContents() {
    thead(
        tr(
            th(/*scope("col"), */ text("Name")),
            th(/*scope("col"), */ text("Rule")),
            th(/*scope("col"), */ text("Status")),
            th(/*scope("col"), */ text("Other")),
            th(/*scope("col"), */ text("Example"))
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

  private void tableHead() {
    tr(
        th(/*scope("col"), */ text("Name")),
        th(/*scope("col"), */ text("Rule")),
        th(/*scope("col"), */ text("Status")),
        th(/*scope("col"), */ text("Other")),
        th(/*scope("col"), */ text("Example"))
    );
  }

  private void tableBody() {
    /*ariaLive("polite"),*/
    tr(td("Load Balancer 1"), td("Round robin"), td("Starting"), td("Test"), td("22"));
    tr(td("Load Balancer 2"), td("DNS delegation"), td("Active"), td("Test"), td("22"));
    tr(td("Load Balancer 3"), td("Round robin"), td("Disabled"), td("Test"), td("22"));
    tr(td("Load Balancer 4"), td("Round robin"), td("Disabled"), td("Test"), td("22"));
    tr(td("Load Balancer 5"), td("Round robin"), td("Disabled"), td("Test"), td("22"));
    tr(td("Load Balancer 6"), td("Round robin"), td("Disabled"), td("Test"), td("22"));
    tr(td("Load Balancer 7"), td("Round robin"), td("Disabled"), td("Test"), td("22"));
  }

}