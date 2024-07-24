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
    shellTitle("Grid - Objectos Carbon");

    topSection = TopSection.COMPONENTS;
  }

  @Override
  protected final void renderContent() {
    div(
        Carbon.WIDE_GRID,
        Carbon.GRID_FULL_WIDTH,
        className("carbon-grid max-w-full mt-05 bg-layer"),

        h1(
            Carbon.COL_SPAN_FULL,
            Carbon.TILE,

            t("Span full")
        )
    );

    div(
        Carbon.WIDE_GRID,
        Carbon.GRID_FULL_WIDTH,

        p(
            Carbon.COL_SPAN_4,
            Carbon.TILE,

            t("Span 4")
        ),
        p(
            Carbon.COL_SPAN_2,
            Carbon.LG_COL_SPAN_4,
            Carbon.TILE,

            t("Span 2 / Span 4 (lg)")
        )
    );
  }

}