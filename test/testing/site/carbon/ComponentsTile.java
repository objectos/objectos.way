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
final class ComponentsTile extends CarbonPage {

  @Override
  protected final void preRender() {
    topSection = TopSection.COMPONENTS;
  }

  @Override
  protected final void renderHead() {
    title("Tile - Objectos Carbon");
  }

  @Override
  protected final void renderContent() {
    carbon.pageHeader(
        Carbon.PAGE_HEADER_TITLE_ONLY,

        carbon.pageHeaderTitleRow(
            carbon.pageHeaderTitle("Tile")
        )
    );

    //

    carbon.grid(
        Carbon.WIDE,
        carbon.gridColumns(1),

        h2(
            Carbon.HEADING_03,

            t("Clickable Tile")
        )
    );

    carbon.grid(
        Carbon.WIDE,
        carbon.gridColumns(2, Carbon.MD, 4),
        carbon.gap(Carbon.SPACING_03),

        carbon.tile(
            Carbon.TILE_CLICKABLE, className("aspect-1x1"),
            href("#"),
            t("Clickable Tile")
        ),

        carbon.tile(
            Carbon.TILE_CLICKABLE, className("aspect-1x1"),
            href("#"),
            t("Clickable Tile with icon"),
            carbon.renderIcon(Carbon.Icon.ARROW_RIGHT)
        )
    );
  }

}