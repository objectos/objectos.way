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

import objectos.css.CssTemplate;

public final class BaseReset extends CssTemplate {

  @Override
  protected final void definition() {
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