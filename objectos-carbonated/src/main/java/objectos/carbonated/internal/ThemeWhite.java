/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.carbonated.internal;

import objectos.carbonated.Carbon;
import objectos.carbonated.Palette;
import objectos.carbonated.Theme;

public final class ThemeWhite extends Theme {

  ThemeWhite() {}

  @Override
  protected final void definition() {
    style(
      Carbon.WHITE_THEME,

      background(var(BACKGROUND)),
      color(var(TEXT_PRIMARY)),

      set(BACKGROUND, Palette.WHITE),
      set(BACKGROUND_INVERSE, Palette.GRAY_80),

      set(BUTTON_PRIMARY, hex("#0f62fe")),
      set(BUTTON_PRIMARY_ACTIVE, hex("#002d9c")),
      set(BUTTON_PRIMARY_HOVER, hex("#0050e6")),

      set(FOCUS, Palette.BLUE_60),

      set(LINK_PRIMARY, Palette.BLUE_60),
      set(LINK_INVERSE, Palette.BLUE_40),

      set(NOTIFICATION_BACKGROUND_ERROR, Palette.RED_10),
      set(NOTIFICATION_BACKGROUND_SUCCESS, Palette.GREEN_10),
      set(NOTIFICATION_BACKGROUND_INFO, Palette.BLUE_10),
      set(NOTIFICATION_BACKGROUND_WARNING, hex("#fdf6dd")), // color.mix(yellow30, white0, 15%)

      set(SUPPORT_ERROR, Palette.RED_60),
      set(SUPPORT_SUCCESS, Palette.GREEN_50),
      set(SUPPORT_WARNING, Palette.YELLOW_30),
      set(SUPPORT_INFO, Palette.BLUE_70),
      set(SUPPORT_ERROR_INVERSE, Palette.RED_50),
      set(SUPPORT_SUCCESS_INVERSE, Palette.GREEN_40),
      set(SUPPORT_WARNING_INVERSE, Palette.YELLOW_30),
      set(SUPPORT_INFO_INVERSE, Palette.BLUE_50),

      set(TEXT_PRIMARY, Palette.GRAY_100),
      set(TEXT_INVERSE, Palette.WHITE),
      set(TEXT_ON_COLOR, Palette.WHITE),
      set(TEXT_ON_COLOR_DISABLED, Palette.GRAY_50)
    );
  }

  /*

  packages/themes/src/white.js

  import  {
  // Blue
  blue20,
  blue30,
  blue40,
  blue60,
  blue70,

  // Gray
  gray10,
  gray20,
  gray20Hover,
  gray30,
  gray40,
  gray50,
  gray60,
  gray70,
  gray80,
  gray100,

  // Support
  blue50,
  green40,
  green50,
  yellow30,
  orange40,
  red50,
  red60,
  purple60,

  // Constants
  white,
  whiteHover,
  gray80Hover,
  gray10Hover,
  }from'@carbon/colors';import
  { adjustAlpha }from'./tools';

  // Background
  export const background=white;export const backgroundInverse=gray80;export const backgroundBrand=blue60;export const backgroundActive=

  adjustAlpha(gray50, 0.5);
  export const backgroundHover = adjustAlpha(gray50, 0.12);
  export const backgroundInverseHover = gray80Hover;
  export const backgroundSelected = adjustAlpha(gray50, 0.2);
  export const backgroundSelectedHover = adjustAlpha(gray50, 0.32);

  // Layer
  // layer-01
  export const layer01 = gray10;
  export const layerActive01 = gray30;
  export const layerHover01 = gray10Hover;
  export const layerSelected01 = gray20;
  export const layerSelectedHover01 = gray20Hover;

  // layer-02
  export const layer02 = white;
  export const layerActive02 = gray30;
  export const layerHover02 = whiteHover;
  export const layerSelected02 = gray20;
  export const layerSelectedHover02 = gray20Hover;

  // layer-03
  export const layer03 = gray10;
  export const layerActive03 = gray30;
  export const layerHover03 = gray10Hover;
  export const layerSelected03 = gray20;
  export const layerSelectedHover03 = gray20Hover;

  // layer
  export const layerSelectedInverse = gray100;
  export const layerSelectedDisabled = gray50;

  // layer-accent-01
  export const layerAccent01 = gray20;
  export const layerAccentActive01 = gray40;
  export const layerAccentHover01 = gray20Hover;

  // layer-accent-02
  export const layerAccent02 = gray20;
  export const layerAccentActive02 = gray40;
  export const layerAccentHover02 = gray20Hover;

  // layer-accent-03
  export const layerAccent03 = gray20;
  export const layerAccentActive03 = gray40;
  export const layerAccentHover03 = gray20Hover;

  // Field
  // field-01
  export const field01 = gray10;
  export const fieldHover01 = gray10Hover;

  // field-02
  export const field02 = white;
  export const fieldHover02 = whiteHover;

  // field-03
  export const field03 = gray10;
  export const fieldHover03 = gray10Hover;

  // Border
  // border-subtle-00
  export const borderSubtle00 = gray20;

  // border-subtle-01
  export const borderSubtle01 = gray30;
  export const borderSubtleSelected01 = gray30;

  // border-subtle-02
  export const borderSubtle02 = gray20;
  export const borderSubtleSelected02 = gray30;

  // border-subtle-03
  export const borderSubtle03 = gray30;
  export const borderSubtleSelected03 = gray30;

  // border-strong
  export const borderStrong01 = gray50;
  export const borderStrong02 = gray50;
  export const borderStrong03 = gray50;

  // border-tile
  export const borderTile01 = gray30;
  export const borderTile02 = gray40;
  export const borderTile03 = gray30;

  // border-inverse
  export const borderInverse = gray100;

  // border-interactive
  export const borderInteractive = blue60;

  // border
  export const borderDisabled = gray30;

  // Text
  export const textPrimary = gray100;
  export const textSecondary = gray70;
  export const textPlaceholder = adjustAlpha(textPrimary, 0.4);
  export const textHelper = gray60;
  export const textError = red60;
  export const textInverse = white;
  export const textOnColor = white;
  export const textOnColorDisabled = gray50;
  export const textDisabled = adjustAlpha(textPrimary, 0.25);

  // Link
  export const linkPrimary = blue60;
  export const linkPrimaryHover = blue70;
  export const linkSecondary = blue70;
  export const linkInverse = blue40;
  export const linkVisited = purple60;
  export const linkInverseActive = gray10;
  export const linkInverseHover = blue30;

  // Icon
  export const iconPrimary = gray100;
  export const iconSecondary = gray70;
  export const iconInverse = white;
  export const iconOnColor = white;
  export const iconOnColorDisabled = gray50;
  export const iconDisabled = adjustAlpha(iconPrimary, 0.25);
  export const iconInteractive = blue60;

  // Support
  export const supportError = red60;
  export const supportSuccess = green50;
  export const supportWarning = yellow30;
  export const supportInfo = blue70;
  export const supportErrorInverse = red50;
  export const supportSuccessInverse = green40;
  export const supportWarningInverse = yellow30;
  export const supportInfoInverse = blue50;
  export const supportCautionMinor = yellow30;
  export const supportCautionMajor = orange40;
  export const supportCautionUndefined = purple60;

  // Focus
  export const focus = blue60;
  export const focusInset = white;
  export const focusInverse = white;

  // Skeleton
  export const skeletonBackground = whiteHover;
  export const skeletonElement = gray30;

  // Misc
  export const interactive = blue60;
  export const highlight = blue20;
  export const overlay = 'rgba(22, 22, 22, 0.5)';
  export const toggleOff = gray50;
  export const shadow = 'rgba(0, 0, 0, 0.3)';

  // Type
  export {
  caption01,
  caption02,
  label01,
  label02,
  helperText01,
  helperText02,
  bodyShort01,
  bodyLong01,
  bodyShort02,
  bodyLong02,
  code01,
  code02,
  heading01,
  productiveHeading01,
  heading02,
  productiveHeading02,
  productiveHeading03,
  productiveHeading04,
  productiveHeading05,
  productiveHeading06,
  productiveHeading07,
  expressiveHeading01,
  expressiveHeading02,
  expressiveHeading03,
  expressiveHeading04,
  expressiveHeading05,
  expressiveHeading06,
  expressiveParagraph01,
  quotation01,
  quotation02,
  display01,
  display02,
  display03,
  display04,
  } from '@carbon/type';

  // Layout
  // Spacing
  export {
  spacing01,
  spacing02,
  spacing03,
  spacing04,
  spacing05,
  spacing06,
  spacing07,
  spacing08,
  spacing09,
  spacing10,
  spacing11,
  spacing12,
  spacing13,
  // Fluid spacing
  fluidSpacing01,
  fluidSpacing02,
  fluidSpacing03,
  fluidSpacing04,
  // Containers
  container01,
  container02,
  container03,
  container04,
  container05,
  sizeXSmall,
  sizeSmall,
  sizeMedium,
  sizeLarge,
  sizeXLarge,
  size2XLarge,
  // Icon sizes
  iconSize01,
  iconSize02,
  // Layout
  // Deprecated ☠️
  layout01,
  layout02,
  layout03,
  layout04,
  layout05,
  layout06,
  layout07,
  } from '@carbon/layout';

  packages/themes/src/component-tokens/button/tokens.js

  const buttonSeparator = {
  fallback: '#e0e0e0',
  whiteTheme: '#e0e0e0',
  g10: '#e0e0e0',
  g90: '#161616',
  g100: '#161616',
  };

  const buttonPrimary = {
  whiteTheme: '#0f62fe',
  g10: '#0f62fe',
  g90: '#0f62fe',
  g100: '#0f62fe',
  };

  const buttonSecondary = {
  whiteTheme: '#393939',
  g10: '#393939',
  g90: '#6f6f6f',
  g100: '#6f6f6f',
  };

  const buttonTertiary = {
  whiteTheme: '#0f62fe',
  g10: '#0f62fe',
  g90: '#ffffff',
  g100: '#ffffff',
  };

  const buttonDangerPrimary = {
  whiteTheme: '#da1e28',
  g10: '#da1e28',
  g90: '#da1e28',
  g100: '#da1e28',
  };

  const buttonDangerSecondary = {
  whiteTheme: '#da1e28',
  g10: '#da1e28',
  g90: '#ff8389',
  g100: '#fa4d56',
  };

  const buttonDangerActive = {
  whiteTheme: '#750e13',
  g10: '#750e13',
  g90: '#750e13',
  g100: '#750e13',
  };

  const buttonPrimaryActive = {
  whiteTheme: '#002d9c',
  g10: '#002d9c',
  g90: '#002d9c',
  g100: '#002d9c',
  };

  const buttonSecondaryActive = {
  whiteTheme: '#6f6f6f',
  g10: '#6f6f6f',
  g90: '#393939',
  g100: '#393939',
  };

  const buttonTertiaryActive = {
  whiteTheme: '#002d9c',
  g10: '#002d9c',
  g90: '#c6c6c6',
  g100: '#c6c6c6',
  };

  const buttonDangerHover = {
  whiteTheme: '#b81921',
  g10: '#b81921',
  g90: '#b81921',
  g100: '#b81921',
  };

  const buttonPrimaryHover = {
  whiteTheme: '#0050e6',
  g10: '#0050e6',
  g90: '#0050e6',
  g100: '#0050e6',
  };

  const buttonSecondaryHover = {
  whiteTheme: '#474747',
  g10: '#474747',
  g90: '#5e5e5e',
  g100: '#5e5e5e',
  };

  const buttonTertiaryHover = {
  whiteTheme: '#0050e6',
  g10: '#0050e6',
  g90: '#f4f4f4',
  g100: '#f4f4f4',
  };

  const buttonDisabled = {
  whiteTheme: '#c6c6c6',
  g10: '#c6c6c6',
  g90: 'rgb(141 141 141 / 30%)',
  g100: 'rgb(141 141 141 / 30%)',
  };

  export {
  buttonSeparator,
  buttonPrimary,
  buttonSecondary,
  buttonTertiary,
  buttonDangerPrimary,
  buttonDangerSecondary,
  buttonDangerActive,
  buttonPrimaryActive,
  buttonSecondaryActive,
  buttonTertiaryActive,
  buttonDangerHover,
  buttonPrimaryHover,
  buttonSecondaryHover,
  buttonTertiaryHover,
  buttonDisabled,
  };

   */

}