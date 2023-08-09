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

public final class BaseTypography extends CssTemplate {

  @Override
  protected final void definition() {
    style(
      _root,

      set(Typography.FONT_WEIGHT_LIGHT, l(300)),
      set(Typography.FONT_WEIGHT_REGULAR, l(400)),
      set(Typography.FONT_WEIGHT_SEMIBOLD, l(600)),

      set(Typography.BODY_COMPACT_01_FONT_SIZE, rem(0.875)),
      set(Typography.BODY_COMPACT_01_FONT_WEIGHT, l(400)),
      set(Typography.BODY_COMPACT_01_LINE_HEIGHT, l(1.28572)),
      set(Typography.BODY_COMPACT_01_LETTER_SPACING, px(0.16)),
      set(Typography.BODY_01_FONT_SIZE, rem(0.875)),
      set(Typography.BODY_01_FONT_WEIGHT, l(400)),
      set(Typography.BODY_01_LINE_HEIGHT, l(1.42857)),
      set(Typography.BODY_01_LETTER_SPACING, px(0.16)),
      set(Typography.BODY_02_FONT_SIZE, rem(1)),
      set(Typography.BODY_02_FONT_WEIGHT, l(400)),
      set(Typography.BODY_02_LINE_HEIGHT, l(1.5)),
      set(Typography.BODY_02_LETTER_SPACING, px(0)),
      set(Typography.HEADING_COMPACT_01_FONT_SIZE, rem(0.875)),
      set(Typography.HEADING_COMPACT_01_FONT_WEIGHT, l(600)),
      set(Typography.HEADING_COMPACT_01_LINE_HEIGHT, l(1.28572)),
      set(Typography.HEADING_COMPACT_01_LETTER_SPACING, px(0.16)),
      set(Typography.HEADING_01_FONT_SIZE, rem(0.875)),
      set(Typography.HEADING_01_FONT_WEIGHT, l(600)),
      set(Typography.HEADING_01_LINE_HEIGHT, l(1.42857)),
      set(Typography.HEADING_01_LETTER_SPACING, px(0.16)),
      set(Typography.HEADING_02_FONT_SIZE, rem(1)),
      set(Typography.HEADING_02_FONT_WEIGHT, l(600)),
      set(Typography.HEADING_02_LINE_HEIGHT, l(1.5)),
      set(Typography.HEADING_02_LETTER_SPACING, px(0)),
      set(Typography.HEADING_03_FONT_SIZE, rem(1.25)),
      set(Typography.HEADING_03_FONT_WEIGHT, l(400)),
      set(Typography.HEADING_03_LINE_HEIGHT, l(1.4)),
      set(Typography.HEADING_03_LETTER_SPACING, px(0)),
      set(Typography.HEADING_04_FONT_SIZE, rem(1.75)),
      set(Typography.HEADING_04_FONT_WEIGHT, l(400)),
      set(Typography.HEADING_04_LINE_HEIGHT, l(1.28572)),
      set(Typography.HEADING_04_LETTER_SPACING, px(0)),
      set(Typography.HEADING_05_FONT_SIZE, rem(2)),
      set(Typography.HEADING_05_FONT_WEIGHT, l(400)),
      set(Typography.HEADING_05_LINE_HEIGHT, l(1.25)),
      set(Typography.HEADING_05_LETTER_SPACING, px(0)),
      set(Typography.HEADING_06_FONT_SIZE, rem(2.625)),
      set(Typography.HEADING_06_FONT_WEIGHT, l(300)),
      set(Typography.HEADING_06_LINE_HEIGHT, l(1.199)),
      set(Typography.HEADING_06_LETTER_SPACING, px(0))
    );

    style(
      html,

      fontSize(pct(100))
    );

    style(
      body,

      fontWeight(var(Typography.FONT_WEIGHT_REGULAR)),
      fontFamily(sansSerif)
    //-moz-osx-font-smoothing: grayscale;
    //-webkit-font-smoothing: antialiased;
    //text-rendering: optimizeLegibility;
    );

    style(
      code,

      fontFamily(monospace)
    );

    style(
      strong,

      fontWeight(var(Typography.FONT_WEIGHT_SEMIBOLD))
    );

    style(
      h1,

      fontSize(var(Typography.HEADING_06_FONT_SIZE)),
      fontWeight(var(Typography.HEADING_06_FONT_WEIGHT)),
      letterSpacing(var(Typography.HEADING_06_LETTER_SPACING)),
      lineHeight(var(Typography.HEADING_06_LINE_HEIGHT))
    );

    style(
      h2,

      fontSize(var(Typography.HEADING_05_FONT_SIZE)),
      fontWeight(var(Typography.HEADING_05_FONT_WEIGHT)),
      letterSpacing(var(Typography.HEADING_05_LETTER_SPACING)),
      lineHeight(var(Typography.HEADING_05_LINE_HEIGHT))
    );

    style(
      h3,

      fontSize(var(Typography.HEADING_04_FONT_SIZE)),
      fontWeight(var(Typography.HEADING_04_FONT_WEIGHT)),
      letterSpacing(var(Typography.HEADING_04_LETTER_SPACING)),
      lineHeight(var(Typography.HEADING_04_LINE_HEIGHT))
    );

    style(
      h4,

      fontSize(var(Typography.HEADING_03_FONT_SIZE)),
      fontWeight(var(Typography.HEADING_03_FONT_WEIGHT)),
      letterSpacing(var(Typography.HEADING_03_LETTER_SPACING)),
      lineHeight(var(Typography.HEADING_03_LINE_HEIGHT))
    );

    style(
      h5,

      fontSize(var(Typography.HEADING_02_FONT_SIZE)),
      fontWeight(var(Typography.HEADING_02_FONT_WEIGHT)),
      letterSpacing(var(Typography.HEADING_02_LETTER_SPACING)),
      lineHeight(var(Typography.HEADING_02_LINE_HEIGHT))
    );

    style(
      h6,

      fontSize(var(Typography.HEADING_01_FONT_SIZE)),
      fontWeight(var(Typography.HEADING_01_FONT_WEIGHT)),
      letterSpacing(var(Typography.HEADING_01_LETTER_SPACING)),
      lineHeight(var(Typography.HEADING_01_LINE_HEIGHT))
    );

    style(
      p,

      fontSize(var(Typography.BODY_02_FONT_SIZE)),
      fontWeight(var(Typography.BODY_02_FONT_WEIGHT)),
      letterSpacing(var(Typography.BODY_02_LETTER_SPACING)),
      lineHeight(var(Typography.BODY_02_LINE_HEIGHT))
    );

    style(
      a,

      color(var(Theme.LINK_PRIMARY))
    );

    style(
      em,

      fontStyle(italic)
    );
  }

}