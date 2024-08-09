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

final class ComponentsProgressIndicator extends CarbonPage {

  ComponentsProgressIndicator(Http.Exchange http) {
    super(http);
  }

  @Override
  protected final void preRender() {
    topSection = TopSection.COMPONENTS;
  }

  @Override
  protected final void renderHead() {
    title("Progress indicator - Objectos Carbon");
  }

  @Override
  protected final void renderContent() {
    section(
        className("page-header page-header-title-only"),

        div(
            className("page-header-title-row"),

            h1(
                className("page-header-title"),

                t("Progress indicator")
            )
        )
    );

    section(
        className("grid-wide grid-cols-1"),

        f(this::renderSection)
    );
  }

  private void renderSection() {
    h2(
        className("heading-03 my-spacing-05"),

        t("Vertical")
    );

    carbon.progressIndicator()
        .vertical()
        .currentIndex(1)
        .step("First step", "Step 1: Getting started with Carbon Design System", "Optional label")
        .step("Second step with tooltip", "Step 2: Getting started with Carbon Design System")
        .step("Third step with tooltip", "Step 3: Getting started with Carbon Design System")
        .step("Fourth step", "Step 4: Getting started with Carbon Design System", "Example invalid step").valid(false)
        .step("Fifth step", "Step 5: Getting started with Carbon Design System").enabled(false)
        .render();
  }

}