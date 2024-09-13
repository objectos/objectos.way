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
final class ComponentsLink extends CarbonPage {

  @Override
  protected final void preRender() {
    topSection = TopSection.COMPONENTS;
  }

  @Override
  protected final void renderHead() {
    title("Link - Objectos Carbon");
  }

  @Override
  protected final void renderContent() {
    carbon.pageHeader(
        Carbon.PAGE_HEADER_TITLE_ONLY,

        carbon.pageHeaderTitleRow(
            carbon.pageHeaderTitle("Link")
        )
    );

    carbon.grid(
        Carbon.WIDE,
        carbon.gridColumns(2, Carbon.MD, 3, Carbon.MAX, 6),

        div(
            carbon.link(Carbon.LINK_STANDARD, "Link", "https://www.objectos.com.br")
        ),

        div(
            carbon.link(Carbon.LINK_VISITED, "Link visited", "https://www.objectos.com.br")
        ),

        div(
            carbon.link(Carbon.LINK_INLINE, "Link inline", "https://www.objectos.com.br")
        ),

        div(
            carbon.link(Carbon.LINK_INLINE_VISITED, "Link inline-visited", "https://www.objectos.com.br")
        )
    );
  }

}