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
        className("grid-wide max-w-full py-spacing-05 bg-layer"),

        h1(
            className("col-span-full heading-04"),

            t("Button")
        )
    );

    section(
        className("grid-wide grid-cols-2 md:grid-cols-3 gap-spacing-03 max:grid-cols-6 *:self-start"),

        f(this::renderSection)
    );
  }

  private void renderSection() {
    renderVariants();

    renderVariantsWithIcons();

    renderIconOnly();
  }

  private void renderVariants() {
    h2(
        className("col-span-full heading-03"),

        t("Primary (all sizes)")
    );

    button(
        className("button-primary"),
        type("button"), t("Primary")
    );

    button(
        className("button-primary-sm"),
        type("button"), t("Primary")
    );

    button(
        className("button-primary-md"),
        type("button"), t("Primary")
    );

    button(
        className("button-primary-lg"),
        type("button"), t("Primary")
    );

    button(
        className("button-primary-xl"),
        type("button"), t("Primary")
    );

    button(
        className("button-primary-2xl"),
        type("button"), t("Primary")
    );

    h2(
        className("col-span-full heading-03"),

        t("Secondary (all sizes)")
    );

    button(
        className("button-secondary"),
        type("button"), t("Secondary")
    );

    button(
        className("button-secondary-sm"),
        type("button"), t("Secondary")
    );

    button(
        className("button-secondary-md"),
        type("button"), t("Secondary")
    );

    button(
        className("button-secondary-lg"),
        type("button"), t("Secondary")
    );

    button(
        className("button-secondary-xl"),
        type("button"), t("Secondary")
    );

    button(
        className("button-secondary-2xl"),
        type("button"), t("Secondary")
    );

    h2(
        className("col-span-full heading-03"),

        t("Tertiary (all sizes)")
    );

    button(
        className("button-tertiary"),
        type("button"), t("Tertiary")
    );

    button(
        className("button-tertiary-sm"),
        type("button"), t("Tertiary")
    );

    button(
        className("button-tertiary-md"),
        type("button"), t("Tertiary")
    );

    button(
        className("button-tertiary-lg"),
        type("button"), t("Tertiary")
    );

    button(
        className("button-tertiary-xl"),
        type("button"), t("Tertiary")
    );

    button(
        className("button-tertiary-2xl"),
        type("button"), t("Tertiary")
    );

    h2(
        className("col-span-full heading-03"),

        t("Ghost (all sizes)")
    );

    button(
        className("button-ghost"),
        type("button"), t("Ghost")
    );

    button(
        className("button-ghost-sm"),
        type("button"), t("Ghost")
    );

    button(
        className("button-ghost-md"),
        type("button"), t("Ghost")
    );

    button(
        className("button-ghost-lg"),
        type("button"), t("Ghost")
    );

    button(
        className("button-ghost-xl"),
        type("button"), t("Ghost")
    );

    button(
        className("button-ghost-2xl"),
        type("button"), t("Ghost")
    );

    h2(
        className("col-span-full heading-03"),

        t("Danger (all sizes)")
    );

    button(
        className("button-danger"),
        type("button"), t("Danger")
    );

    button(
        className("button-danger-sm"),
        type("button"), t("Danger")
    );

    button(
        className("button-danger-md"),
        type("button"), t("Danger")
    );

    button(
        className("button-danger-lg"),
        type("button"), t("Danger")
    );

    button(
        className("button-danger-xl"),
        type("button"), t("Danger")
    );

    button(
        className("button-danger-2xl"),
        type("button"), t("Danger")
    );

    h2(
        className("col-span-full heading-03"),

        t("Danger tertiary (all sizes)")
    );

    button(
        className("button-danger-tertiary"),
        type("button"), t("Danger tertiary")
    );

    button(
        className("button-danger-tertiary-sm"),
        type("button"), t("Danger tertiary")
    );

    button(
        className("button-danger-tertiary-md"),
        type("button"), t("Danger tertiary")
    );

    button(
        className("button-danger-tertiary-lg"),
        type("button"), t("Danger tertiary")
    );

    button(
        className("button-danger-tertiary-xl"),
        type("button"), t("Danger tertiary")
    );

    button(
        className("button-danger-tertiary-2xl"),
        type("button"), t("Danger tertiary")
    );

    h2(
        className("col-span-full heading-03"),

        t("Danger ghost (all sizes)")
    );

    button(
        className("button-danger-ghost"),
        type("button"), t("Danger ghost")
    );

    button(
        className("button-danger-ghost-sm"),
        type("button"), t("Danger ghost")
    );

    button(
        className("button-danger-ghost-md"),
        type("button"), t("Danger ghost")
    );

    button(
        className("button-danger-ghost-lg"),
        type("button"), t("Danger ghost")
    );

    button(
        className("button-danger-ghost-xl"),
        type("button"), t("Danger ghost")
    );

    button(
        className("button-danger-ghost-2xl"),
        type("button"), t("Danger ghost")
    );
  }

  private void renderVariantsWithIcons() {
    h2(
        className("col-span-full heading-03"),

        t("Primary + Icon (all sizes)")
    );

    button(
        className("button-primary"),
        type("button"), t("Primary"), icon16(Carbon.Icon.ADD)
    );

    button(
        className("button-primary-sm"),
        type("button"), t("Primary"), icon16(Carbon.Icon.ADD)
    );

    button(
        className("button-primary-md"),
        type("button"), t("Primary"), icon16(Carbon.Icon.ADD)
    );

    button(
        className("button-primary-lg"),
        type("button"), t("Primary"), icon16(Carbon.Icon.ADD)
    );

    button(
        className("button-primary-xl"),
        type("button"), t("Primary"), icon16(Carbon.Icon.ADD)
    );

    button(
        className("button-primary-2xl"),
        type("button"), t("Primary"), icon16(Carbon.Icon.ADD)
    );

    h2(
        className("col-span-full heading-03"),

        t("Secondary + Icon (all sizes)")
    );

    button(
        className("button-secondary"),
        type("button"), t("Secondary"), icon16(Carbon.Icon.ADD)
    );

    button(
        className("button-secondary-sm"),
        type("button"), t("Secondary"), icon16(Carbon.Icon.ADD)
    );

    button(
        className("button-secondary-md"),
        type("button"), t("Secondary"), icon16(Carbon.Icon.ADD)
    );

    button(
        className("button-secondary-lg"),
        type("button"), t("Secondary"), icon16(Carbon.Icon.ADD)
    );

    button(
        className("button-secondary-xl"),
        type("button"), t("Secondary"), icon16(Carbon.Icon.ADD)
    );

    button(
        className("button-secondary-2xl"),
        type("button"), t("Secondary"), icon16(Carbon.Icon.ADD)
    );

    h2(
        className("col-span-full heading-03"),

        t("Tertiary + Icon (all sizes)")
    );

    button(
        className("button-tertiary"),
        type("button"), t("Tertiary"), icon16(Carbon.Icon.ADD)
    );

    button(
        className("button-tertiary-sm"),
        type("button"), t("Tertiary"), icon16(Carbon.Icon.ADD)
    );

    button(
        className("button-tertiary-md"),
        type("button"), t("Tertiary"), icon16(Carbon.Icon.ADD)
    );

    button(
        className("button-tertiary-lg"),
        type("button"), t("Tertiary"), icon16(Carbon.Icon.ADD)
    );

    button(
        className("button-tertiary-xl"),
        type("button"), t("Tertiary"), icon16(Carbon.Icon.ADD)
    );

    button(
        className("button-tertiary-2xl"),
        type("button"), t("Tertiary"), icon16(Carbon.Icon.ADD)
    );

    h2(
        className("col-span-full heading-03"),

        t("Ghost + Icon (all sizes)")
    );

    button(
        className("button-ghost"),
        type("button"), t("Ghost"), icon16(Carbon.Icon.ADD)
    );

    button(
        className("button-ghost-sm"),
        type("button"), t("Ghost"), icon16(Carbon.Icon.ADD)
    );

    button(
        className("button-ghost-md"),
        type("button"), t("Ghost"), icon16(Carbon.Icon.ADD)
    );

    button(
        className("button-ghost-lg"),
        type("button"), t("Ghost"), icon16(Carbon.Icon.ADD)
    );

    button(
        className("button-ghost-xl"),
        type("button"), t("Ghost"), icon16(Carbon.Icon.ADD)
    );

    button(
        className("button-ghost-2xl"),
        type("button"), t("Ghost"), icon16(Carbon.Icon.ADD)
    );

    h2(
        className("col-span-full heading-03"),

        t("Danger + Icon (all sizes)")
    );

    button(
        className("button-danger"),
        type("button"), t("Danger"), icon16(Carbon.Icon.ADD)
    );

    button(
        className("button-danger-sm"),
        type("button"), t("Danger"), icon16(Carbon.Icon.ADD)
    );

    button(
        className("button-danger-md"),
        type("button"), t("Danger"), icon16(Carbon.Icon.ADD)
    );

    button(
        className("button-danger-lg"),
        type("button"), t("Danger"), icon16(Carbon.Icon.ADD)
    );

    button(
        className("button-danger-xl"),
        type("button"), t("Danger"), icon16(Carbon.Icon.ADD)
    );

    button(
        className("button-danger-2xl"),
        type("button"), t("Danger"), icon16(Carbon.Icon.ADD)
    );

    h2(
        className("col-span-full heading-03"),

        t("Danger tertiary + Icon (all sizes)")
    );

    button(
        className("button-danger-tertiary"),
        type("button"), t("Danger tertiary"), icon16(Carbon.Icon.ADD)
    );

    button(
        className("button-danger-tertiary-sm"),
        type("button"), t("Danger tertiary"), icon16(Carbon.Icon.ADD)
    );

    button(
        className("button-danger-tertiary-md"),
        type("button"), t("Danger tertiary"), icon16(Carbon.Icon.ADD)
    );

    button(
        className("button-danger-tertiary-lg"),
        type("button"), t("Danger tertiary"), icon16(Carbon.Icon.ADD)
    );

    button(
        className("button-danger-tertiary-xl"),
        type("button"), t("Danger tertiary"), icon16(Carbon.Icon.ADD)
    );

    button(
        className("button-danger-tertiary-2xl"),
        type("button"), t("Danger tertiary"), icon16(Carbon.Icon.ADD)
    );

    h2(
        className("col-span-full heading-03"),

        t("Danger ghost + Icon (all sizes)")
    );

    button(
        className("button-danger-ghost"),
        type("button"), t("Danger ghost"), icon16(Carbon.Icon.ADD)
    );

    button(
        className("button-danger-ghost-sm"),
        type("button"), t("Danger ghost"), icon16(Carbon.Icon.ADD)
    );

    button(
        className("button-danger-ghost-md"),
        type("button"), t("Danger ghost"), icon16(Carbon.Icon.ADD)
    );

    button(
        className("button-danger-ghost-lg"),
        type("button"), t("Danger ghost"), icon16(Carbon.Icon.ADD)
    );

    button(
        className("button-danger-ghost-xl"),
        type("button"), t("Danger ghost"), icon16(Carbon.Icon.ADD)
    );

    button(
        className("button-danger-ghost-2xl"),
        type("button"), t("Danger ghost"), icon16(Carbon.Icon.ADD)
    );
  }

  private void renderIconOnly() {
    h2(
        className("col-span-full heading-03"),

        t("Icon only (primary)")
    );

    button(
        className("button-icon-primary"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    button(
        className("button-icon-primary-sm"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    button(
        className("button-icon-primary-md"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    button(
        className("button-icon-primary-lg"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    button(
        className("button-icon-primary-xl"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    button(
        className("button-icon-primary-2xl"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    h2(
        className("col-span-full heading-03"),

        t("Icon only (secondary)")
    );

    button(
        className("button-icon-secondary"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    button(
        className("button-icon-secondary-sm"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    button(
        className("button-icon-secondary-md"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    button(
        className("button-icon-secondary-lg"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    button(
        className("button-icon-secondary-xl"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    button(
        className("button-icon-secondary-2xl"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    h2(
        className("col-span-full heading-03"),

        t("Icon only (tertiary)")
    );

    button(
        className("button-icon-tertiary"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    button(
        className("button-icon-tertiary-sm"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    button(
        className("button-icon-tertiary-md"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    button(
        className("button-icon-tertiary-lg"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    button(
        className("button-icon-tertiary-xl"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    button(
        className("button-icon-tertiary-2xl"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    h2(
        className("col-span-full heading-03"),

        t("Icon only (ghost)")
    );

    button(
        className("button-icon-ghost"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    button(
        className("button-icon-ghost-sm"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    button(
        className("button-icon-ghost-md"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    button(
        className("button-icon-ghost-lg"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    button(
        className("button-icon-ghost-xl"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    button(
        className("button-icon-ghost-2xl"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    h2(
        className("col-span-full heading-03"),

        t("Icon only (danger)")
    );

    button(
        className("button-icon-danger"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    button(
        className("button-icon-danger-sm"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    button(
        className("button-icon-danger-md"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    button(
        className("button-icon-danger-lg"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    button(
        className("button-icon-danger-xl"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    button(
        className("button-icon-danger-2xl"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    h2(
        className("col-span-full heading-03"),

        t("Icon only (danger tertiary)")
    );

    button(
        className("button-icon-danger-tertiary"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    button(
        className("button-icon-danger-tertiary-sm"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    button(
        className("button-icon-danger-tertiary-md"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    button(
        className("button-icon-danger-tertiary-lg"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    button(
        className("button-icon-danger-tertiary-xl"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    button(
        className("button-icon-danger-tertiary-2xl"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    h2(
        className("col-span-full heading-03"),

        t("Icon only (danger ghost)")
    );

    button(
        className("button-icon-danger-ghost"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    button(
        className("button-icon-danger-ghost-sm"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    button(
        className("button-icon-danger-ghost-md"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    button(
        className("button-icon-danger-ghost-lg"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    button(
        className("button-icon-danger-ghost-xl"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );

    button(
        className("button-icon-danger-ghost-2xl"),
        type("button"), icon16(Carbon.Icon.TRASH_CAN)
    );
  }

}