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

  public static final Theme INSTANCE = new ThemeWhite();

  private ThemeWhite() {}

  @Override
  protected final void definition() {
    style(
      Carbon.WHITE_THEME,

      background(var(BACKGROUND)),
      color(var(TEXT_PRIMARY)),

      set(BACKGROUND, Palette.WHITE),
      set(BACKGROUND_INVERSE, Palette.GRAY_80),

      set(TEXT_PRIMARY, Palette.GRAY_100),
      set(TEXT_INVERSE, Palette.WHITE),
      set(TEXT_ON_COLOR, Palette.WHITE),
      set(TEXT_ON_COLOR_DISABLED, Palette.GRAY_50),

      set(LINK_PRIMARY, Palette.BLUE_60),
      set(LINK_INVERSE, Palette.BLUE_40),

      set(SUPPORT_ERROR, Palette.RED_60),
      set(SUPPORT_SUCCESS, Palette.GREEN_50),
      set(SUPPORT_WARNING, Palette.YELLOW_30),
      set(SUPPORT_INFO, Palette.BLUE_70),
      set(SUPPORT_ERROR_INVERSE, Palette.RED_50),
      set(SUPPORT_SUCCESS_INVERSE, Palette.GREEN_40),
      set(SUPPORT_WARNING_INVERSE, Palette.YELLOW_30),
      set(SUPPORT_INFO_INVERSE, Palette.BLUE_50),

      set(FOCUS, Palette.BLUE_60),

      set(BUTTON_PRIMARY, hex("#0f62fe")),
      set(BUTTON_PRIMARY_HOVER, hex("#0050e6")),

      set(NOTIFICATION_BACKGROUND_ERROR, Palette.RED_10),
      set(NOTIFICATION_BACKGROUND_SUCCESS, Palette.GREEN_10),
      set(NOTIFICATION_BACKGROUND_INFO, Palette.BLUE_10),
      set(NOTIFICATION_BACKGROUND_WARNING, hex("#fdf6dd")) // color.mix(yellow30, white0, 15%)
    );
  }

}