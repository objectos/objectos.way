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

final class ComponentsButton extends CarbonPage {

  ComponentsButton(Http.Exchange http) {
    super(http);
  }

  @Override
  protected final void preRender() {
    topSection = TopSection.COMPONENTS;
  }

  @Override
  protected final void renderHead() {
    title("Button - Objectos Carbon");
  }

  @Override
  protected final void renderContent() {
    div(
        className("grid-wide max-w-full py-05 bg-layer"),

        h1(
            className("col-span-full heading-04"),

            t("Button")
        )
    );

    section(
        className("grid-wide"),

        h2(
            className("col-span-full heading-03"),

            t("Variants")
        ),

        div(
            className("col-span-full"),

            div(
                className("flex gap-x-03"),

                f(this::variants)
            )
        )
    );
  }

  private void variants() {
    button(
        className("button button-primary"),
        type("button"),

        t("Primary")
    );

    button(
        className("button button-secondary"),
        type("button"),

        t("Secondary")
    );

    button(
        className("button button-tertiary"),
        type("button"),

        t("Tertiary")
    );

    button(
        className("button button-danger"),
        type("button"),

        t("Danger")
    );
  }

}