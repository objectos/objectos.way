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

final class ComponentsGrid extends CarbonPage {

  ComponentsGrid(Http.Exchange http) {
    super(http);
  }

  @Override
  protected final void preRender() {
    topSection = TopSection.COMPONENTS;
  }

  @Override
  protected final void renderHead() {
    title("Grid - Objectos Carbon");
  }

  @Override
  protected final void renderContent() {
    carbon.pageHeader(
        Carbon.PAGE_HEADER_TITLE_ONLY,

        carbon.pageHeaderTitleRow(
            carbon.pageHeaderTitle("Grid")
        )
    );

    /*
    carbon.gridWide(
        carbon.gridColumns(1, Carbon.MD, 2, Carbon.LG, 4),
    
        h2(
            Carbon.HEADING_03,
    
            carbon.gridSpan(0),
    
            t("Grid (Wide)")
        ),
    
        carbon.tile(
            Carbon.ASPECT_2X1,
    
            t("Column 1")
        ),
    
        carbon.tile(
            Carbon.ASPECT_2X1,
    
            t("Column 2")
        ),
    
        carbon.tile(
            Carbon.ASPECT_2X1,
    
            t("Column 3")
        ),
    
        carbon.tile(
            Carbon.ASPECT_2X1,
    
            t("Column 4")
        )
    );
    */
  }

  protected final void renderContent0() {
    className("pt-spacing-07");

    div(
        className("grid-wide grid-cols-1"),

        h1(
            className("heading-03"),

            t("Grid")
        ),

        h2(
            className("heading-02"),

            t("Wide")
        )
    );

    div(
        className("grid-wide grid-cols-1 md:grid-cols-2 lg:grid-cols-4"),

        div(
            className("aspect-2x1 tile"),

            t("Column 1")
        ),

        div(
            className("aspect-2x1 tile"),

            t("Column 2")
        ),

        div(
            className("aspect-2x1 tile"),

            t("Column 3")
        ),

        div(
            className("aspect-2x1 tile"),

            t("Column 4")
        )
    );

    div(
        className("grid-wide grid-cols-1"),

        h2(
            className("heading-02"),

            t("Narrow")
        )
    );

    div(
        className("grid-narrow grid-cols-1 md:grid-cols-2 lg:grid-cols-4"),

        div(
            className("aspect-2x1 tile"),

            t("Column 1")
        ),

        div(
            className("aspect-2x1 tile"),

            t("Column 2")
        ),

        div(
            className("aspect-2x1 tile"),

            t("Column 3")
        ),

        div(
            className("aspect-2x1 tile"),

            t("Column 4")
        )
    );

    div(
        className("grid-wide grid-cols-1"),

        h2(
            className("heading-02"),

            t("Condensed")
        )
    );

    div(
        className("grid-condensed grid-cols-1 md:grid-cols-2 lg:grid-cols-4"),

        div(
            className("aspect-2x1 tile"),

            t("Column 1")
        ),

        div(
            className("aspect-2x1 tile"),

            t("Column 2")
        ),

        div(
            className("aspect-2x1 tile"),

            t("Column 3")
        ),

        div(
            className("aspect-2x1 tile"),

            t("Column 4")
        )
    );
  }

}