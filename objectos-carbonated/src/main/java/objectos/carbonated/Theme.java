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
package objectos.carbonated;

import objectos.css.CssTemplate;
import objectos.css.tmpl.Api.ColorValue;
import objectos.css.tmpl.Api.DoubleLiteral;
import objectos.css.tmpl.Api.IntLiteral;
import objectos.css.tmpl.Api.LengthValue;
import objectos.css.util.ClassSelector;
import objectos.css.util.CustomProperty;

public final class Theme extends CssTemplate {

  public static final ClassSelector WHITE = ClassSelector.randomClassSelector(5);

  // @formatter:off

  static final CustomProperty<ColorValue> BACKGROUND = CustomProperty.randomName(5);
  static final CustomProperty<ColorValue> BACKGROUND_INVERSE = CustomProperty.randomName(5);
  static final CustomProperty<ColorValue> FOCUS = CustomProperty.randomName(5);
  static final CustomProperty<ColorValue> LINK_INVERSE = CustomProperty.randomName(5);
  static final CustomProperty<ColorValue> NOTIFICATION_BACKGROUND_ERROR = CustomProperty.randomName(5);
  static final CustomProperty<ColorValue> NOTIFICATION_BACKGROUND_SUCCESS = CustomProperty.randomName(5);
  static final CustomProperty<ColorValue> NOTIFICATION_BACKGROUND_INFO = CustomProperty.randomName(5);
  static final CustomProperty<ColorValue> NOTIFICATION_BACKGROUND_WARNING = CustomProperty.randomName(5);
  static final CustomProperty<ColorValue> SUPPORT_ERROR = CustomProperty.randomName(5);
  static final CustomProperty<ColorValue> SUPPORT_ERROR_INVERSE = CustomProperty.randomName(5);
  static final CustomProperty<ColorValue> SUPPORT_INFO = CustomProperty.randomName(5);
  static final CustomProperty<ColorValue> SUPPORT_INFO_INVERSE = CustomProperty.randomName(5);
  static final CustomProperty<ColorValue> SUPPORT_SUCCESS = CustomProperty.randomName(5);
  static final CustomProperty<ColorValue> SUPPORT_SUCCESS_INVERSE = CustomProperty.randomName(5);
  static final CustomProperty<ColorValue> SUPPORT_WARNING = CustomProperty.randomName(5);
  static final CustomProperty<ColorValue> SUPPORT_WARNING_INVERSE = CustomProperty.randomName(5);
  static final CustomProperty<ColorValue> TEXT_INVERSE = CustomProperty.randomName(5);
  static final CustomProperty<ColorValue> TEXT_PRIMARY = CustomProperty.randomName(5);

  static final CustomProperty<LengthValue>    BODY_COMPACT_01_FONT_SIZE = CustomProperty.randomName(5);
  static final CustomProperty<IntLiteral>     BODY_COMPACT_01_FONT_WEIGHT = CustomProperty.randomName(5);
  static final CustomProperty<DoubleLiteral>  BODY_COMPACT_01_LINE_HEIGHT = CustomProperty.randomName(5);
  static final CustomProperty<LengthValue>    BODY_COMPACT_01_LETTER_SPACING = CustomProperty.randomName(5);
  static final CustomProperty<LengthValue>    HEADING_COMPACT_01_FONT_SIZE = CustomProperty.randomName(5);
  static final CustomProperty<IntLiteral>     HEADING_COMPACT_01_FONT_WEIGHT = CustomProperty.randomName(5);
  static final CustomProperty<DoubleLiteral>  HEADING_COMPACT_01_LINE_HEIGHT = CustomProperty.randomName(5);
  static final CustomProperty<LengthValue>    HEADING_COMPACT_01_LETTER_SPACING = CustomProperty.randomName(5);

  // @formatter:on

  public Theme() {}

  @Override
  protected final void definition() {
    style(
      _root,

      set(BODY_COMPACT_01_FONT_SIZE, rem(0.875)),
      set(BODY_COMPACT_01_FONT_WEIGHT, l(400)),
      set(BODY_COMPACT_01_LINE_HEIGHT, l(1.28572)),
      set(BODY_COMPACT_01_LETTER_SPACING, px(0.16)),
      set(HEADING_COMPACT_01_FONT_SIZE, rem(0.875)),
      set(HEADING_COMPACT_01_FONT_WEIGHT, l(600)),
      set(HEADING_COMPACT_01_LINE_HEIGHT, l(1.28572)),
      set(HEADING_COMPACT_01_LETTER_SPACING, px(0.16))
    );

    style(
      WHITE,

      background(var(BACKGROUND)),
      color(var(TEXT_PRIMARY)),
      set(BACKGROUND, hex("#ffffff")),
      //--cds-background-active: rgba(141, 141, 141, 0.5);
      //--cds-background-brand: #0f62fe;
      //--cds-background-hover: rgba(141, 141, 141, 0.12);
      set(BACKGROUND_INVERSE, hex("#393939")),
      //--cds-background-inverse-hover: #474747;
      //--cds-background-selected: rgba(141, 141, 141, 0.2);
      //-cds-background-selected-hover: rgba(141, 141, 141, 0.32);
      set(FOCUS, hex("#0f62fe")),
      set(LINK_INVERSE, hex("#78a9ff")),
      set(NOTIFICATION_BACKGROUND_ERROR, hex("#fff1f1")),
      set(NOTIFICATION_BACKGROUND_SUCCESS, hex("#defbe6")),
      set(NOTIFICATION_BACKGROUND_INFO, hex("#edf5ff")),
      set(NOTIFICATION_BACKGROUND_WARNING, hex("#fdf6dd")),
      set(SUPPORT_ERROR, hex("#da1e28")),
      set(SUPPORT_ERROR_INVERSE, hex("#fa4d56")),
      set(SUPPORT_INFO, hex("#0043ce")),
      set(SUPPORT_INFO_INVERSE, hex("#4589ff")),
      set(SUPPORT_SUCCESS, hex("#24a148")),
      set(SUPPORT_SUCCESS_INVERSE, hex("#42be65")),
      set(SUPPORT_WARNING, hex("#f1c21b")),
      set(SUPPORT_WARNING_INVERSE, hex("#f1c21b")),
      set(TEXT_INVERSE, hex("#ffffff")),
      set(TEXT_PRIMARY, hex("#161616"))
    );
  }

}
