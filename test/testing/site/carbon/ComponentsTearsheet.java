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

  @Override
  protected final void renderContent() {
    carbon.pageHeader(
        Carbon.PAGE_HEADER_TITLE_ONLY,

        carbon.pageHeaderTitleRow(
            carbon.pageHeaderTitle("Tearsheet")
        )
    );

    final Html.Id sheet01;
    sheet01 = Html.id("tearsheet-01");

    final Html.Id sheet01Modal;
    sheet01Modal = Html.id("tearsheet-01-modal");

    carbon.gridWide(
        carbon.gridColumns(2), carbon.gap(Carbon.SPACING_05),

        h2(
            Carbon.HEADING_03,

            t("Title + description + influencer")
        ),

        carbon.button(
            Carbon.PRIMARY, Carbon.LG,
            type("button"),
            dataOnClick(
                Carbon.showTearsheet(sheet01),
                Carbon.showTearsheetModal(sheet01Modal)
            ),
            t("Open Tearsheet")
        )
    );

    carbon.tearsheet(
        sheet01,

        carbon.tearsheetModal(
            sheet01Modal, ariaLabel("Example 01"),

            carbon.tearsheetHeader(
                carbon.tearsheetHeaderTitle("Create topic"),
                carbon.tearsheetHeaderDescription("Specify details for the new topic you want to create")
            ),

            carbon.tearsheetBody(
                carbon.tearsheetInfluencer(
                    className("py-spacing-06 px-spacing-07"),

                    carbon.progressIndicator(
                        Carbon.VERTICAL,

                        carbon.progressStep(Carbon.STEP_CURRENT, "Topic name"),
                        carbon.progressStep(Carbon.STEP_INCOMPLETE, "Location"),
                        carbon.progressStep(Carbon.STEP_INCOMPLETE, "Partitions"),
                        carbon.progressStep(Carbon.STEP_INCOMPLETE, "Message retention")
                    )
                ),
                carbon.tearsheetRight(
                    carbon.tearsheetMain(),

                    carbon.tearsheetActions(
                      //carbon.tearsheetCancelAction("Cancel"),
                    //carbon.tearsheetBackAction("Back"),
                    //carbon.tearsheetNextAction("Next")
                    )
                )
            )
        )
    );
  }

}