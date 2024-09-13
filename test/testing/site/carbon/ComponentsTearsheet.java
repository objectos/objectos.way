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

import java.io.IOException;
import objectos.way.Carbon;
import objectos.way.Css;
import objectos.way.Html;
import objectos.way.Http;

@Css.Source
final class ComponentsTearsheet extends CarbonPage {

  private static final String EXAMPLE = "example";

  private static final Html.Id SHEET_01 = Html.id("tearsheet-01");

  private static final Html.Id SHEET_01_MODAL = Html.id("tearsheet-01-modal");

  @Override
  protected final void handle() {
    switch (http.method()) {
      case Http.GET, Http.HEAD -> get();

      case Http.POST -> post();

      default -> http.methodNotAllowed();
    }
  }

  private void get() {
    http.ok(this);
  }

  private void post() {
    try {
      Http.FormUrlEncoded form;
      form = Http.parseFormUrlEncoded(http);

      String step;
      step = form.getOrDefault(EXAMPLE, "");

      switch (step) {
        case "01" -> postExample01(form);

        default -> http.unprocessableContent();
      }

      http.ok(this);
    } catch (IOException e) {
      http.internalServerError(e);
    } catch (Http.UnsupportedMediaTypeException e) {
      http.unsupportedMediaType();
    }
  }

  private void postExample01(Http.FormUrlEncoded form) {

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

    carbon.grid(
        Carbon.WIDE,
        carbon.gridColumns(2), carbon.gap(Carbon.SPACING_05),

        h2(
            Carbon.HEADING_03,

            t("Title + description + influencer")
        ),

        carbon.button(
            Carbon.PRIMARY, Carbon.LG,
            type("button"),
            dataOnClick(
                Carbon.showTearsheet(SHEET_01),
                Carbon.showTearsheetModal(SHEET_01_MODAL)
            ),
            t("Open Tearsheet")
        )
    );

    renderExample01();
  }

  private void renderExample01() {
    carbon.tearsheet(
        SHEET_01,

        carbon.tearsheetModal(
            SHEET_01_MODAL, ariaLabel("Example 01"),

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
                        carbon.tearsheetCancelAction("Cancel"),
                        carbon.tearsheetBackAction("Back"),
                        carbon.tearsheetNextAction("Next")
                    )
                )
            )
        )
    );
  }

}