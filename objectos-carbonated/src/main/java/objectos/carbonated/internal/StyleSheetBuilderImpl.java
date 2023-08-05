/*
 * Copyright 2023 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package objectos.carbonated.internal;

import java.util.EnumSet;
import java.util.Set;
import objectos.carbonated.Carbon;
import objectos.carbonated.Carbon.StyleSheetBuilder;
import objectos.css.CssTemplate;
import objectos.css.StyleSheet;

public final class StyleSheetBuilderImpl extends CssTemplate implements Carbon.StyleSheetBuilder {

  private enum Feature {

    RESET;

  }

  private final Set<Feature> features = EnumSet.noneOf(Feature.class);

  @Override
  public final StyleSheet build() {
    return compile();
  }

  @Override
  public final StyleSheetBuilder reset() {
    features.add(Feature.RESET);

    return this;
  }

  @Override
  protected final void definition() {
    if (features.contains(Feature.RESET)) {
      $reset();
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

}