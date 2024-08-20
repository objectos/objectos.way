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
package objectos.way;

import objectos.way.Carbon.CarbonProgressStepVariant;
import objectos.way.Carbon.Icon;
import objectos.way.Carbon.ProgressStep;
import objectos.way.Carbon.ProgressStepVariant;

final class CarbonProgressStep implements ProgressStep {

  private final Html.TemplateBase tmpl;

  final CarbonProgressStepVariant variant;

  final String label;

  CarbonProgressStep(Html.TemplateBase tmpl, ProgressStepVariant variant, String label) {
    this.tmpl = tmpl;

    this.variant = (CarbonProgressStepVariant) variant;

    this.label = label;
  }

  private static final Html.Instruction LI_HORIZONTAL = Html.NOOP;

  private static final Html.ClassName LI_VERTICAL = Html.classText("""
      relative inline-flex min-h-[3.625rem] overflow-visible
      """);

  private static final Html.Instruction BUTTON_HORIZONTAL = Html.NOOP;

  private static final Html.ClassName BUTTON_VERTICAL = Html.classText("""
      flex min-h-[3.625rem] content-start text-start
      """);

  private static final Html.Instruction ICON_HORIZONTAL = Html.NOOP;

  private static final Html.ClassName ICON_VERTICAL = Html.classText("""
    mt-1px mx-spacing-03 inline-block shrink-0 fill-interactive
    """);

  private static final Html.Instruction TEXT_HORIZONTAL = Html.NOOP;

  private static final Html.ClassName TEXT_VERTICAL = Html.classText("""
      flex flex-col
      """);

  private static final Html.Instruction LABEL_HORIZONTAL = Html.NOOP;

  private static final Html.ClassName LABEL_VERTICAL = Html.classText("""
      inline-block max-w-160px
      overflow-hidden text-ellipsis
      align-top leading-[1.45] text-text-primary
      """);

  public final Html.Instruction render(boolean horizontal) {
    return tmpl.li(
        horizontal ? LI_HORIZONTAL : LI_VERTICAL,

        tmpl.button(
            horizontal ? BUTTON_HORIZONTAL : BUTTON_VERTICAL,

            tmpl.type("button"),
            tmpl.title(label),

            Carbon.renderIcon16(
                tmpl,

                switch (variant) {
                  case STEP_COMPLETE -> Icon.CHECKMARK_OUTLINE;

                  case STEP_CURRENT -> Icon.INCOMPLETE;

                  case STEP_INCOMPLETE -> Icon.CIRCLE_DASH;
                },

                horizontal ? ICON_HORIZONTAL : ICON_VERTICAL,

                tmpl.ariaHidden("true")
            ),

            tmpl.div(
                horizontal ? TEXT_HORIZONTAL : TEXT_VERTICAL,

                tmpl.p(
                    Carbon.BODY_COMPACT_01,

                    horizontal ? LABEL_HORIZONTAL : LABEL_VERTICAL,

                    tmpl.t(label)
                )
            )
        )
    );
  }

}