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

import java.util.EnumSet;
import java.util.Set;
import objectos.carbonated.Carbon;
import objectos.carbonated.Carbon.StyleSheetBuilder;
import objectos.carbonated.Theme;
import objectos.carbonated.Typography;
import objectos.css.CssTemplate;
import objectos.css.StyleSheet;
import objectos.util.GrowableList;

public final class StyleSheetBuilderImpl extends CssTemplate implements Carbon.StyleSheetBuilder {

  private enum Feature {

    BUTTON,

    NOTIFICATION,

    RESET,

    TYPOGRAPHY;

  }

  private final Set<Feature> features = EnumSet.noneOf(Feature.class);

  private GrowableList<Theme> themes;

  @Override
  public final StyleSheet build() {
    return compile();
  }

  @Override
  public final StyleSheetBuilder button() {
    features.add(Feature.BUTTON);

    return this;
  }

  @Override
  public final StyleSheetBuilder notification() {
    features.add(Feature.NOTIFICATION);

    return this;
  }

  @Override
  public final StyleSheetBuilder reset() {
    features.add(Feature.RESET);

    return this;
  }

  @Override
  public final StyleSheetBuilder themes(Theme... values) {
    if (themes == null) {
      themes = new GrowableList<>();
    }

    // values implicit null-check
    for (int i = 0; i < values.length; i++) {
      Theme value;
      value = values[i];

      themes.addWithNullMessage(value, "values[", i, "] == null");
    }

    return this;
  }

  @Override
  public final StyleSheetBuilder typography() {
    features.add(Feature.TYPOGRAPHY);

    return this;
  }

  @Override
  protected final void definition() {
    if (features.contains(Feature.RESET)) {
      $reset();

      install(BaseLayout.STYLES);
    }

    if (features.contains(Feature.TYPOGRAPHY)) {
      $typography();
    }

    if (themes != null) {
      for (var theme : themes) {
        install(theme);
      }
    }

    if (features.contains(Feature.BUTTON)) {
      install(CompButton.STYLES);
    }

    if (features.contains(Feature.NOTIFICATION)) {
      install(NotificationImpl.STYLES);
    }
  }

  private void $reset() {
    // http://meyerweb.com/eric/tools/css/reset/
    // v2.0 | 20110126
    // License: none (public domain)

    style(
      html, OR,
      body, OR,
      div, OR,
      span, OR,
      //applet, OR,
      object, OR,
      iframe, OR,
      h1, OR,
      h2, OR,
      h3, OR,
      h4, OR,
      h5, OR,
      h6, OR,
      p, OR,
      blockquote, OR,
      pre, OR,
      a, OR,
      abbr, OR,
      acronym, OR,
      address, OR,
      big, OR,
      cite, OR,
      code, OR,
      del, OR,
      dfn, OR,
      em, OR,
      img, OR,
      ins, OR,
      kbd, OR,
      q, OR,
      //s, OR,
      samp, OR,
      small, OR,
      strike, OR,
      strong, OR,
      sub, OR,
      sup, OR,
      //tt, OR,
      var, OR,
      b, OR,
      //u, OR,
      //i, OR,
      //center, OR,
      dl, OR,
      dt, OR,
      dd, OR,
      ol, OR,
      ul, OR,
      li, OR,
      fieldset, OR,
      form, OR,
      label, OR,
      legend, OR,
      table, OR,
      caption, OR,
      tbody, OR,
      tfoot, OR,
      thead, OR,
      tr, OR,
      th, OR,
      td, OR,
      article, OR,
      aside, OR,
      canvas, OR,
      details, OR,
      embed, OR,
      figure, OR,
      figcaption, OR,
      footer, OR,
      header, OR,
      hgroup, OR,
      menu, OR,
      nav, OR,
      output, OR,
      ruby, OR,
      section, OR,
      summary, OR,
      time, OR,
      mark, OR,
      audio, OR,
      video, OR,

      padding($0),
      border($0),
      margin($0),
      font(inherit),
      fontSize(pct(100)),
      verticalAlign(baseline)
    );

    // Chrome 62 fix
    style(
      button, OR,
      select, OR,
      input, OR,
      textarea,

      borderRadius($0),
      fontFamily(inherit)
    );

    /* HTML5 display-role reset for older browsers */
    style(
      article, OR,
      aside, OR,
      details, OR,
      figcaption, OR,
      figure, OR,
      footer, OR,
      header, OR,
      hgroup, OR,
      menu, OR,
      nav, OR,
      section,

      display(block)
    );

    style(
      body,

      // move to Theme?
      //background-color: custom-property.get-var('background', #ffffff);
      //color: custom-property.get-var('text-primary', #161616);

      lineHeight(1)
    );

    style(
      ol, OR,
      ul,

      listStyle(none)
    );

    style(
      blockquote, OR,
      q,

      quotes(none)
    );

    style(
      blockquote, __before, OR,
      blockquote, __after, OR,
      q, __before, OR,
      q, __after,

      content(""),
      content(none)
    );

    style(
      table,

      borderCollapse(collapse),
      borderSpacing($0)
    );

    // End vendor reset

    style(
      html,

      boxSizing(borderBox)
    );

    style(
      any, OR,
      any, __before, OR,
      any, __after,

      boxSizing(inherit)
    );
  }

  private void $typography() {
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