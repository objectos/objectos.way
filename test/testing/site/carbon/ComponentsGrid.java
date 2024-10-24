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
final class ComponentsGrid extends CarbonPage {

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

    carbon.grid(
        Carbon.WIDE, carbon.gridColumns(1),

        h2(
            Carbon.HEADING_03,

            text("Grid (Wide)")
        )
    );

    carbon.grid(
        Carbon.WIDE, carbon.gridColumns(1, Carbon.MD, 2, Carbon.LG, 4),

        div(
            className("aspect-2x1 tile"),

            text("Column 1")
        ),

        div(
            className("aspect-2x1 tile"),

            text("Column 2")
        ),

        div(
            className("aspect-2x1 tile"),

            text("Column 3")
        ),

        div(
            className("aspect-2x1 tile"),

            text("Column 4")
        )
    );

    carbon.grid(
        Carbon.WIDE, carbon.gridColumns(1),

        h2(
            Carbon.HEADING_03,

            text("Grid (Narrow)")
        )
    );

    carbon.grid(
        Carbon.NARROW, carbon.gridColumns(1, Carbon.MD, 2, Carbon.LG, 4),

        div(
            className("aspect-2x1 tile"),

            text("Column 1")
        ),

        div(
            className("aspect-2x1 tile"),

            text("Column 2")
        ),

        div(
            className("aspect-2x1 tile"),

            text("Column 3")
        ),

        div(
            className("aspect-2x1 tile"),

            text("Column 4")
        )
    );

    carbon.grid(
        Carbon.WIDE, carbon.gridColumns(1),

        h2(
            Carbon.HEADING_03,

            text("Grid (Condensed)")
        )
    );

    carbon.grid(
        Carbon.CONDENSED, carbon.gridColumns(1, Carbon.MD, 2, Carbon.LG, 4),

        div(
            className("aspect-2x1 tile"),

            text("Column 1")
        ),

        div(
            className("aspect-2x1 tile"),

            text("Column 2")
        ),

        div(
            className("aspect-2x1 tile"),

            text("Column 3")
        ),

        div(
            className("aspect-2x1 tile"),

            text("Column 4")
        )
    );
  }

}