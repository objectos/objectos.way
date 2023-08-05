/*
 * Copyright 2023 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package objectos.carbonated;

import static org.testng.Assert.assertEquals;

import objectos.css.StyleSheet;
import org.testng.annotations.Test;

public class CarbonStyleSheetBuilderTest {

  @Test
  public void reset() {
    StyleSheet sheet;
    sheet = Carbon.styleSheetBuilder()
        .reset()
        .build();

    assertEquals(
      sheet.toString(),

      """
      html, body, div, span, object, iframe, h1, h2, h3, h4, h5, h6, p, blockquote, pre, a, abbr, acronym, address, big, cite, code, del, dfn, em, img, ins, kbd, q, samp, small, strike, strong, sub, sup, var, b, dl, dt, dd, ol, ul, li, fieldset, form, label, legend, table, caption, tbody, tfoot, thead, tr, th, td, article, aside, canvas, details, embed, figure, figcaption, footer, header, hgroup, menu, nav, output, ruby, section, summary, time, mark, audio, video,  {
        padding: 0;
        border: 0;
        margin: 0;
        font: inherit;
        font-size: 100%;
        vertical-align: baseline;
      }

      button, select, input, textarea {
        border-radius: 0;
        font-family: inherit;
      }

      article, aside, details, figcaption, figure, footer, header, hgroup, menu, nav, section {
        display: block;
      }

      body {
        line-height: 1;
      }

      ol, ul {
        list-style: none;
      }

      blockquote, q {
        quotes: none;
      }

      blockquote::before, blockquote::after, q::before, q::after {
        content: "";
        content: none;
      }

      table {
        border-collapse: collapse;
        border-spacing: 0;
      }

      html {
        box-sizing: border-box;
      }

      *, *::before, *::after {
        box-sizing: inherit;
      }
      """
    );
  }

}