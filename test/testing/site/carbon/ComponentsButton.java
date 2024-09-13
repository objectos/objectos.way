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
final class ComponentsButton extends CarbonPage {

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
    carbon.pageHeader(
        Carbon.PAGE_HEADER_TITLE_ONLY,

        carbon.pageHeaderTitleRow(
            carbon.pageHeaderTitle("Button")
        )
    );

    //

    renderButtons("Primary (all sizes)", Carbon.PRIMARY, "Primary");

    renderButtons("Secondary (all sizes)", Carbon.SECONDARY, "Secondary");

    renderButtons("Tertiary (all sizes)", Carbon.TERTIARY, "Tertiary");

    renderButtons("Ghost (all sizes)", Carbon.GHOST, "Ghost");

    renderButtons("Danger (all sizes)", Carbon.DANGER, "Danger");

    renderButtons("Danger tertiary (all sizes)", Carbon.DANGER_TERTIARY, "Danger tertiary");

    renderButtons("Danger ghost (all sizes)", Carbon.DANGER_GHOST, "Danger ghost");

    //

    renderWithIcon("Primary + Icon (all sizes)", Carbon.PRIMARY, "Primary", Carbon.Icon.ADD);

    renderWithIcon("Secondary + Icon (all sizes)", Carbon.SECONDARY, "Secondary", Carbon.Icon.ADD);

    renderWithIcon("Tertiary + Icon (all sizes)", Carbon.TERTIARY, "Tertiary", Carbon.Icon.ADD);

    renderWithIcon("Ghost + Icon (all sizes)", Carbon.GHOST, "Ghost", Carbon.Icon.ADD);

    renderWithIcon("Danger + Icon (all sizes)", Carbon.DANGER, "Danger", Carbon.Icon.ADD);

    renderWithIcon("Danger tertiary + Icon (all sizes)", Carbon.DANGER_TERTIARY, "Danger tertiary", Carbon.Icon.ADD);

    renderWithIcon("Danger ghost + Icon (all sizes)", Carbon.DANGER_GHOST, "Danger ghost", Carbon.Icon.ADD);

    //

    renderIconOnly("Icon Only (Primary)", Carbon.PRIMARY, Carbon.Icon.TRASH_CAN);

    renderIconOnly("Icon Only (Secondary)", Carbon.SECONDARY, Carbon.Icon.TRASH_CAN);

    renderIconOnly("Icon Only (Tertiary)", Carbon.TERTIARY, Carbon.Icon.TRASH_CAN);

    renderIconOnly("Icon Only (Ghost)", Carbon.GHOST, Carbon.Icon.TRASH_CAN);

    renderIconOnly("Icon Only (Danger)", Carbon.DANGER, Carbon.Icon.TRASH_CAN);

    renderIconOnly("Icon Only (Danger tertiary)", Carbon.DANGER_TERTIARY, Carbon.Icon.TRASH_CAN);

    renderIconOnly("Icon Only (Danger ghost)", Carbon.DANGER_GHOST, Carbon.Icon.TRASH_CAN);
  }

  private void renderButtons(String title, Carbon.ButtonVariant variant, String label) {
    carbon.grid(
        Carbon.WIDE,
        carbon.gridColumns(1),

        h2(
            Carbon.HEADING_03,

            t(title)
        )
    );

    carbon.grid(
        Carbon.WIDE,
        carbon.gridColumns(2, Carbon.MD, 3, Carbon.MAX, 6),
        carbon.gap(Carbon.SPACING_03),
        className("*:self-start"),

        carbon.button(variant, Carbon.SM, type("button"), t(label)),
        carbon.button(variant, Carbon.MD, type("button"), t(label)),
        carbon.button(variant, Carbon.LG, type("button"), t(label)),
        carbon.button(variant, Carbon.XL, type("button"), t(label)),
        carbon.button(variant, Carbon.MAX, type("button"), t(label)),

        div(className("col-span-full")),

        carbon.button(variant, Carbon.SM, disabled(), type("button"), t(label)),
        carbon.button(variant, Carbon.MD, disabled(), type("button"), t(label)),
        carbon.button(variant, Carbon.LG, disabled(), type("button"), t(label)),
        carbon.button(variant, Carbon.XL, disabled(), type("button"), t(label)),
        carbon.button(variant, Carbon.MAX, disabled(), type("button"), t(label))
    );
  }

  private void renderWithIcon(String title, Carbon.ButtonVariant variant, String label, Carbon.Icon icon) {
    carbon.grid(
        Carbon.WIDE,
        carbon.gridColumns(1),

        h2(
            Carbon.HEADING_03,

            t(title)
        )
    );

    carbon.grid(
        Carbon.WIDE,
        carbon.gridColumns(2, Carbon.MD, 3, Carbon.MAX, 6),
        carbon.gap(Carbon.SPACING_03),
        className("*:self-start"),

        carbon.button(variant, Carbon.SM, type("button"), t(label), carbon.renderIcon(icon)),
        carbon.button(variant, Carbon.MD, type("button"), t(label), carbon.renderIcon(icon)),
        carbon.button(variant, Carbon.LG, type("button"), t(label), carbon.renderIcon(icon)),
        carbon.button(variant, Carbon.XL, type("button"), t(label), carbon.renderIcon(icon)),
        carbon.button(variant, Carbon.MAX, type("button"), t(label), carbon.renderIcon(icon)),

        div(className("col-span-full")),

        carbon.button(variant, Carbon.SM, disabled(), type("button"), t(label), carbon.renderIcon(icon)),
        carbon.button(variant, Carbon.MD, disabled(), type("button"), t(label), carbon.renderIcon(icon)),
        carbon.button(variant, Carbon.LG, disabled(), type("button"), t(label), carbon.renderIcon(icon)),
        carbon.button(variant, Carbon.XL, disabled(), type("button"), t(label), carbon.renderIcon(icon)),
        carbon.button(variant, Carbon.MAX, disabled(), type("button"), t(label), carbon.renderIcon(icon))
    );
  }

  private void renderIconOnly(String title, Carbon.ButtonVariant variant, Carbon.Icon icon) {
    carbon.grid(
        Carbon.WIDE,
        carbon.gridColumns(1),

        h2(
            Carbon.HEADING_03,

            t(title)
        )
    );

    carbon.grid(
        Carbon.WIDE,
        carbon.gridColumns(2, Carbon.MD, 3, Carbon.MAX, 6),
        carbon.gap(Carbon.SPACING_03),
        className("*:self-start"),

        carbon.button(variant, Carbon.SM, type("button"), carbon.iconOnly(), carbon.renderIcon(icon)),
        carbon.button(variant, Carbon.MD, type("button"), carbon.iconOnly(), carbon.renderIcon(icon)),
        carbon.button(variant, Carbon.LG, type("button"), carbon.iconOnly(), carbon.renderIcon(icon)),
        carbon.button(variant, Carbon.XL, type("button"), carbon.iconOnly(), carbon.renderIcon(icon)),
        carbon.button(variant, Carbon.MAX, type("button"), carbon.iconOnly(), carbon.renderIcon(icon)),

        div(className("col-span-full")),

        carbon.button(variant, Carbon.SM, disabled(), type("button"), carbon.iconOnly(), carbon.renderIcon(icon)),
        carbon.button(variant, Carbon.MD, disabled(), type("button"), carbon.iconOnly(), carbon.renderIcon(icon)),
        carbon.button(variant, Carbon.LG, disabled(), type("button"), carbon.iconOnly(), carbon.renderIcon(icon)),
        carbon.button(variant, Carbon.XL, disabled(), type("button"), carbon.iconOnly(), carbon.renderIcon(icon)),
        carbon.button(variant, Carbon.MAX, disabled(), type("button"), carbon.iconOnly(), carbon.renderIcon(icon))
    );
  }

}