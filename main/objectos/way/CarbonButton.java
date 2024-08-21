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

import objectos.way.Carbon.CarbonButtonVariant;
import objectos.way.Carbon.CarbonSize;

final class CarbonButton {

  private static final Html.ClassName __BUTTON_BASE = Html.className(
      Carbon.BODY_COMPACT_01,

      Html.classText("""
      relative m-0px inline-flex shrink-0
      cursor-pointer appearance-none
      text-start align-top
      outline-0
      transition-all duration-100
      focus:border-focus
      focus:shadow-[inset_0_0_0_1px_var(--cds-focus),inset_0_0_0_2px_var(--cds-background)]
      """)
  );

  private static final Html.ClassName __BUTTON_JUSTIFY_STANDARD = Html.classText("""
      justify-between
      """);

  private static final Html.ClassName __BUTTON_PADDING_STANDARD = Html.classText("""
      pr-[63px] pl-[15px]
      """);

  private static final Html.ClassName __BUTTON_PADDING_GHOST = Html.classText("""
      pr-[15px] pl-[15px]
      """);

  private static final Html.ClassName __BUTTON_SIZE_SM = Html.classText("""
      w-max max-w-320px min-h-32px py-6px
      """);

  private static final Html.ClassName __BUTTON_SIZE_MD = Html.classText("""
      w-max max-w-320px min-h-40px py-10px
      """);

  private static final Html.ClassName __BUTTON_SIZE_LG = Html.classText("""
      w-max max-w-320px min-h-48px py-14px
      """);

  private static final Html.ClassName __BUTTON_SIZE_XL = Html.classText("""
      w-max max-w-320px min-h-64px py-14px
      """);

  private static final Html.ClassName __BUTTON_SIZE_MAX = Html.classText("""
      w-max max-w-320px min-h-80px py-14px
      """);

  private static final Html.ClassName __BUTTON_PRIMARY = Html.classText("""
      bg-button-primary
      border border-transparent
      text-text-on-color
      active:bg-button-primary-active
      hover:bg-button-primary-hover
      """);

  private static final Html.ClassName __BUTTON_SECONDARY = Html.classText("""
      bg-button-secondary
      border border-transparent
      text-text-on-color
      active:bg-button-secondary-active
      hover:bg-button-secondary-hover
      """);

  private static final Html.ClassName __BUTTON_TERTIARY = Html.classText("""
      bg-transparent
      border border-button-tertiary
      text-button-tertiary
      active:bg-button-tertiary-active
      hover:bg-button-tertiary-hover hover:text-text-inverse
      """);

  private static final Html.ClassName __BUTTON_GHOST = Html.classText("""
      bg-transparent
      border border-transparent
      text-link-primary
      active:bg-background-active active:text-link-primary-hover
      hover:bg-background-hover hover:text-link-primary-hover
      """);

  private static final Html.ClassName __BUTTON_DANGER = Html.classText("""
      bg-button-danger
      border border-transparent
      text-text-on-color
      active:bg-button-danger-active
      hover:bg-button-danger-hover
      """);

  private static final Html.ClassName __BUTTON_DANGER_TERTIARY = Html.classText("""
      bg-transparent
      border border-button-danger-secondary
      text-button-danger-secondary
      active:bg-button-danger-active active:border-button-danger-active active:text-text-on-color
      focus:bg-button-danger focus:text-text-on-color
      hover:bg-button-danger-hover hover:text-text-on-color
      """);

  private static final Html.ClassName __BUTTON_DANGER_GHOST = Html.classText("""
      bg-transparent
      border border-transparent
      text-button-danger-secondary
      active:bg-button-danger-active active:text-text-on-color
      hover:bg-button-danger-hover hover:text-text-on-color
      """);

  private static final Html.ClassName[] BUTTON_VARIANTS = {
      // primary
      Html.className(__BUTTON_BASE, __BUTTON_JUSTIFY_STANDARD, __BUTTON_PADDING_STANDARD, __BUTTON_PRIMARY),
      // secondary
      Html.className(__BUTTON_BASE, __BUTTON_JUSTIFY_STANDARD, __BUTTON_PADDING_STANDARD, __BUTTON_SECONDARY),
      // tertiary
      Html.className(__BUTTON_BASE, __BUTTON_JUSTIFY_STANDARD, __BUTTON_PADDING_STANDARD, __BUTTON_TERTIARY),
      // ghost
      Html.className(__BUTTON_BASE, __BUTTON_JUSTIFY_STANDARD, __BUTTON_PADDING_GHOST, __BUTTON_GHOST),
      // danger
      Html.className(__BUTTON_BASE, __BUTTON_JUSTIFY_STANDARD, __BUTTON_PADDING_STANDARD, __BUTTON_DANGER),
      // danger-tertiary
      Html.className(__BUTTON_BASE, __BUTTON_JUSTIFY_STANDARD, __BUTTON_PADDING_STANDARD, __BUTTON_DANGER_TERTIARY),
      // danger-ghost
      Html.className(__BUTTON_BASE, __BUTTON_JUSTIFY_STANDARD, __BUTTON_PADDING_GHOST, __BUTTON_DANGER_GHOST)
  };

  private static final Html.ClassName[] BUTTON_SIZES = {
      null, __BUTTON_SIZE_SM, __BUTTON_SIZE_MD, __BUTTON_SIZE_LG, __BUTTON_SIZE_XL, __BUTTON_SIZE_MAX
  };

  private static final Html.ClassName __BUTTON_HAS_ICON_BASE = Html.classText("""
      w-16px h-16px shrink-0
      """);

  private static final Html.ClassName __BUTTON_HAS_ICON_STANDARD = Html.className(
      __BUTTON_HAS_ICON_BASE,
      "absolute right-16px mt-1px"
  );

  private static final Html.ClassName __BUTTON_HAS_ICON_GHOST = Html.className(
      __BUTTON_HAS_ICON_BASE,
      "static ml-8px"
  );

  private static final Html.ClassName __BUTTON_HAS_ICON_SM = Html.classText("""
      top-6px
      """);

  private static final Html.ClassName __BUTTON_HAS_ICON_MD = Html.classText("""
      top-10px
      """);

  private static final Html.ClassName __BUTTON_HAS_ICON_LG = Html.classText("""
      top-14px
      """);

  private static final Html.ClassName __BUTTON_HAS_ICON_XL = Html.classText("""
      top-14px
      """);

  private static final Html.ClassName __BUTTON_HAS_ICON_MAX = Html.classText("""
      top-14px
      """);

  private static final Html.ClassName[] BUTTON_HAS_ICON_VARIANTS = {
      // primary
      __BUTTON_HAS_ICON_STANDARD,
      // secondary
      __BUTTON_HAS_ICON_STANDARD,
      // tertiary
      __BUTTON_HAS_ICON_STANDARD,
      // ghost
      __BUTTON_HAS_ICON_GHOST,
      // danger
      __BUTTON_HAS_ICON_STANDARD,
      // danger-tertiary
      __BUTTON_HAS_ICON_STANDARD,
      // danger-ghost
      __BUTTON_HAS_ICON_GHOST
  };

  private static final Html.ClassName[] BUTTON_HAS_ICON_SIZES = {
      null, __BUTTON_HAS_ICON_SM, __BUTTON_HAS_ICON_MD, __BUTTON_HAS_ICON_LG, __BUTTON_HAS_ICON_XL, __BUTTON_HAS_ICON_MAX
  };

  private static final Html.ClassName __BUTTON_JUSTIFY_ICON_ONLY = Html.classText("""
      justify-center
      """);

  private static final Html.ClassName __BUTTON_ICON_ONLY_FILL_PRIMARY = Html.classText("""
      svg:fill-icon-primary
      """);

  private static final Html.ClassName __BUTTON_ICON_ONLY_SM = Html.classText("""
      size-32px pt-[7px]
      """);

  private static final Html.ClassName __BUTTON_ICON_ONLY_MD = Html.classText("""
      size-40px pt-[11px]
      """);

  private static final Html.ClassName __BUTTON_ICON_ONLY_LG = Html.classText("""
      size-48px pt-14px
      """);

  private static final Html.ClassName __BUTTON_ICON_ONLY_XL = Html.classText("""
      size-64px pt-14px
      """);

  private static final Html.ClassName __BUTTON_ICON_ONLY_MAX = Html.classText("""
      size-80px pt-14px
      """);

  private static final Html.ClassName[] BUTTON_ICON_ONLY_VARIANTS = {
      // primary
      Html.className(__BUTTON_BASE, __BUTTON_JUSTIFY_ICON_ONLY, __BUTTON_PRIMARY),
      // secondary
      Html.className(__BUTTON_BASE, __BUTTON_JUSTIFY_ICON_ONLY, __BUTTON_SECONDARY),
      // tertiary
      Html.className(__BUTTON_BASE, __BUTTON_JUSTIFY_ICON_ONLY, __BUTTON_TERTIARY),
      // ghost
      Html.className(__BUTTON_BASE, __BUTTON_JUSTIFY_ICON_ONLY, __BUTTON_GHOST, __BUTTON_ICON_ONLY_FILL_PRIMARY),
      // danger
      Html.className(__BUTTON_BASE, __BUTTON_JUSTIFY_ICON_ONLY, __BUTTON_DANGER),
      // danger-tertiary
      Html.className(__BUTTON_BASE, __BUTTON_JUSTIFY_ICON_ONLY, __BUTTON_DANGER_TERTIARY),
      // danger-ghost
      Html.className(__BUTTON_BASE, __BUTTON_JUSTIFY_ICON_ONLY, __BUTTON_DANGER_GHOST)
  };

  private static final Html.ClassName[] BUTTON_ICON_ONLY_SIZES = {
      null, __BUTTON_ICON_ONLY_SM, __BUTTON_ICON_ONLY_MD, __BUTTON_ICON_ONLY_LG, __BUTTON_ICON_ONLY_XL, __BUTTON_ICON_ONLY_MAX
  };

  private CarbonButton() {}

  public static Html.ElementInstruction button(
      Html.TemplateBase tmpl,
      Html.ElementName renderAs, Carbon.ButtonVariant variant, Carbon.ButtonSize size,
      Carbon.Icon icon, boolean iconOnly, Html.Instruction... contents) {
    CarbonButtonVariant thisVariant;
    thisVariant = (CarbonButtonVariant) variant;

    int variantIndex;
    variantIndex = thisVariant.index();

    CarbonSize thisSize;
    thisSize = (CarbonSize) size;

    int sizeIndex;
    sizeIndex = thisSize.index();

    return tmpl.element(
        renderAs,
        iconOnly ? BUTTON_ICON_ONLY_VARIANTS[variantIndex] : BUTTON_VARIANTS[variantIndex],
        iconOnly ? BUTTON_ICON_ONLY_SIZES[sizeIndex] : BUTTON_SIZES[sizeIndex],

        tmpl.flatten(contents),

        icon != null
            ? Carbon.renderIcon16(
                tmpl, icon,

                Carbon.ARIA_HIDDEN_TRUE,

                iconOnly ? tmpl.noop() : BUTTON_HAS_ICON_VARIANTS[variantIndex],
                iconOnly ? tmpl.noop() : BUTTON_HAS_ICON_SIZES[sizeIndex]
            )
            : tmpl.noop()
    );
  }

}