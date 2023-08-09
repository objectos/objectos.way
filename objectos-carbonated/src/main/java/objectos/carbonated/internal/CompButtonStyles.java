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

import objectos.carbonated.Theme;
import objectos.carbonated.Typography;
import objectos.css.CssTemplate;

final class CompButtonStyles extends CssTemplate {

  @Override
  protected final void definition() {
    style(
      CompButton.BTN,

      fontSize(var(Typography.BODY_COMPACT_01_FONT_SIZE)),
      fontWeight(var(Typography.BODY_COMPACT_01_FONT_WEIGHT)),
      letterSpacing(var(Typography.BODY_COMPACT_01_LETTER_SPACING)),
      lineHeight(var(Typography.BODY_COMPACT_01_LINE_HEIGHT)),

      //border-radius: $button-border-radius;
      cursor(pointer),
      display(inlineFlex),
      flexShrink(0),
      justifyContent(spaceBetween),
      margin($0),
      maxWidth(rem(320 / 16)),
      outline(none),
      paddingInline(px(15), px(63)),
      position(relative),
      textAlign(left),
      textDecoration(none),
      verticalAlign(top),
      width(maxContent)
    /*
    transition: background $duration-fast-01 motion(entrance, productive),
      box-shadow $duration-fast-01 motion(entrance, productive),
      border-color $duration-fast-01 motion(entrance, productive),
      outline $duration-fast-01 motion(entrance, productive);
    */
    );

    style(
      CompButton.SIZE_SM,

      minHeight(var(BaseLayout.SIZE_HEIGHT_SM)),
      paddingBlock(px(6))
    );

    style(
      CompButton.SIZE_MD,

      minHeight(var(BaseLayout.SIZE_HEIGHT_MD)),
      paddingBlock(px(10))
    );

    style(
      CompButton.SIZE_LG,

      minHeight(var(BaseLayout.SIZE_HEIGHT_LG)),
      paddingBlock(px(14))
    );

    style(
      CompButton.SIZE_XL,

      minHeight(var(BaseLayout.SIZE_HEIGHT_XL)),
      paddingBlock(px(14))
    );

    style(
      CompButton.SIZE_2XL,

      minHeight(var(BaseLayout.SIZE_HEIGHT_2XL)),
      paddingBlock(px(14))
    );

    style(
      CompButton.KIND_PRIMARY,

      borderWidth(px(1)),
      borderStyle(solid),
      borderColor(transparent),
      backgroundColor(var(Theme.BUTTON_PRIMARY, hex("#0f62fe"))),
      color(var(Theme.TEXT_ON_COLOR, hex("#ffffff")))
    );

    style(
      CompButton.KIND_PRIMARY, _active,

      backgroundColor(var(Theme.BUTTON_PRIMARY_ACTIVE, hex("#002d9c")))
    );

    style(
      CompButton.KIND_PRIMARY, _focus,

      borderColor(var(Theme.FOCUS, hex("#0f62fe"))),
      boxShadow(
        boxShadow(inset, $0, $0, $0, px(1), var(Theme.FOCUS, hex("#0f62fe"))),
        boxShadow(inset, $0, $0, $0, px(2), var(Theme.BACKGROUND, hex("#ffffff")))
      )
    );

    style(
      CompButton.KIND_PRIMARY, _hover,

      backgroundColor(var(Theme.BUTTON_PRIMARY_HOVER, hex("#0050e6")))
    );
  }

}