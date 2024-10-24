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

  boolean invalid;

  final String label;

  String secondaryLabel;

  CarbonProgressStep(Html.TemplateBase tmpl, ProgressStepVariant variant, String label) {
    this.tmpl = tmpl;

    this.variant = (CarbonProgressStepVariant) variant;

    this.label = label;
  }

  @Override
  public final ProgressStep invalid(boolean value) {
    invalid = value;

    return this;
  }

  @Override
  public final ProgressStep secondaryLabel(String value) {
    secondaryLabel = Check.notNull(value, "value == null");

    return this;
  }

  private static final Html.Instruction LI_HORIZONTAL = Html.NOOP;

  private static final Html.ClassName LI_VERTICAL = Html.ClassName.classText("""
      relative inline-flex min-h-[3.625rem] overflow-visible
      """);

  private static final Html.Instruction BUTTON_HORIZONTAL = Html.NOOP;

  private static final Html.ClassName BUTTON_VERTICAL = Html.ClassName.classText("""
      flex min-h-[3.625rem] content-start text-start
      """);

  private static final Html.Instruction ICON_HORIZONTAL = Html.NOOP;

  private static final Html.ClassName ICON_VERTICAL = Html.ClassName.classText("""
      mt-1px mx-spacing-03 inline-block shrink-0
      """);

  private static final Html.ClassName ICON_FILL_STANDARD = Html.ClassName.className(
      "fill-interactive"
  );

  private static final Html.ClassName ICON_FILL_WARNING = Html.ClassName.className(
      "fill-support-error"
  );

  private static final Html.Instruction TEXT_HORIZONTAL = Html.NOOP;

  private static final Html.ClassName TEXT_VERTICAL = Html.ClassName.classText("""
      flex flex-col
      """);

  private static final Html.Instruction LABEL_HORIZONTAL = Html.NOOP;

  private static final Html.ClassName LABEL_VERTICAL = Html.ClassName.classText("""
      inline-block max-w-160px
      overflow-hidden text-ellipsis
      align-top text-text-primary

      more:leading-[1.45]
      """);

  private static final Html.Instruction OPTIONAL_HORIZONTAL = Html.NOOP;

  private static final Html.ClassName OPTIONAL_VERTICAL = Html.ClassName.classText("""
      static w-full text-text-secondary
      """);

  private static final Html.Instruction LINE_COMPLETE_HORIZONTAL = Html.NOOP;

  private static final Html.ClassName LINE_COMPLETE_VERTICAL = Html.ClassName.classText("""
      top-0px left-0px absolute h-full w-1px
      border border-transparent

      bg-interactive
      """);

  private static final Html.Instruction LINE_CURRENT_HORIZONTAL = Html.NOOP;

  private static final Html.ClassName LINE_CURRENT_VERTICAL = Html.ClassName.classText("""
      top-0px left-0px absolute h-full w-1px
      border border-transparent

      bg-interactive
      """);

  private static final Html.Instruction LINE_INCOMPLETE_HORIZONTAL = Html.NOOP;

  private static final Html.ClassName LINE_INCOMPLETE_VERTICAL = Html.ClassName.classText("""
      top-0px left-0px absolute h-full w-1px
      border border-transparent

      bg-border-subtle
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

                !invalid
                    ? switch (variant) {
                      case STEP_COMPLETE -> Icon.CHECKMARK_OUTLINE;

                      case STEP_CURRENT -> Icon.INCOMPLETE;

                      case STEP_INCOMPLETE -> Icon.CIRCLE_DASH;
                    }
                    : Icon.WARNING,

                horizontal ? ICON_HORIZONTAL : ICON_VERTICAL,

                !invalid ? ICON_FILL_STANDARD : ICON_FILL_WARNING,

                tmpl.ariaHidden("true")
            ),

            tmpl.div(
                horizontal ? TEXT_HORIZONTAL : TEXT_VERTICAL,

                tmpl.p(
                    Carbon.BODY_COMPACT_01,

                    horizontal ? LABEL_HORIZONTAL : LABEL_VERTICAL,

                    tmpl.t(label)
                ),

                secondaryLabel != null
                    ? tmpl.p(
                        Carbon.LABEL_01,

                        horizontal ? OPTIONAL_HORIZONTAL : OPTIONAL_VERTICAL,

                        tmpl.t(secondaryLabel)
                    )
                    : tmpl.noop()
            ),

            tmpl.span(
                tmpl.className("sr-only"),

                switch (variant) {
                  case STEP_COMPLETE -> tmpl.t("Complete");

                  case STEP_CURRENT -> tmpl.t("Current");

                  case STEP_INCOMPLETE -> tmpl.t("Incomplete");
                }
            ),

            tmpl.span(
                switch (variant) {
                  case STEP_COMPLETE -> horizontal ? LINE_COMPLETE_HORIZONTAL : LINE_COMPLETE_VERTICAL;

                  case STEP_CURRENT -> horizontal ? LINE_CURRENT_HORIZONTAL : LINE_CURRENT_VERTICAL;

                  case STEP_INCOMPLETE -> horizontal ? LINE_INCOMPLETE_HORIZONTAL : LINE_INCOMPLETE_VERTICAL;
                }
            )
        )
    );
  }

}