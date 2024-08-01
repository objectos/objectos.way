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
            className("flex flex-wrap col-span-full gap-03"),

            f(this::variants)
        ),

        h2(
            className("col-span-full heading-03"),

            t("Icon buttons")
        ),

        div(
            className("flex flex-wrap col-span-full gap-03"),

            f(this::iconButtons)
        ),

        h2(
            className("col-span-full heading-03"),

            t("Sizes")
        ),

        div(
            className("min-h-80px flex flex-wrap col-span-full gap-03"),

            f(this::sizes)
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
        className("button button-ghost"),
        type("button"),

        t("Ghost")
    );

    button(
        className("button button-danger"),
        type("button"),

        t("Danger")
    );

    button(
        className("button button-danger-tertiary"),
        type("button"),

        t("Danger tertiary")
    );

    button(
        className("button button-danger-ghost"),
        type("button"),

        t("Danger ghost")
    );
  }

  private void iconButtons() {
    button(
        className("button button-primary"),
        type("button"),

        t("Add"),

        icon16(Carbon.Icon.ADD)
    );

    button(
        className("button button-danger"),
        type("button"),

        t("Remove"),

        icon16(Carbon.Icon.TRASH_CAN)
    );
  }

  private void sizes() {
    div(
        button(
            className("button button-primary"),
            type("button"),

            t("Submit")
        )
    );

    div(
        button(
            className("button-sm button-primary"),
            type("button"),

            t("Submit")
        )
    );

    div(
        button(
            className("button-md button-primary"),
            type("button"),

            t("Submit")
        )
    );

    div(
        button(
            className("button-xl button-primary"),
            type("button"),

            t("Submit")
        )
    );

    div(
        button(
            className("button-2xl button-primary"),
            type("button"),

            t("Submit")
        )
    );
  }

}