/*
 * Copyright 2023 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package objectos.carbonated;

import static org.testng.Assert.assertEquals;

import objectos.css.Css;
import objectos.css.StyleSheet;
import org.testng.annotations.Test;

public class CarbonStyleSheetBuilderTest {

  static {
    Css.randomSeed(84321674516L);
  }

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

  @Test
  public void typography() {
    StyleSheet sheet;
    sheet = Carbon.styleSheetBuilder()
        .typography()
        .build();

    assertEquals(
      sheet.toString(),

      """
      html {
        font-size: 100%;
      }

      body {
        font-weight: var(--lw7rp);
        font-family: sans-serif;
      }

      code {
        font-family: monospace;
      }

      strong {
        font-weight: var(--pnpum);
      }

      h1 {
        font-size: var(--rsfnl);
        font-weight: var(--oabvq);
        letter-spacing: var(--kfzqj);
        line-height: var(--qca2u);
      }

      h2 {
        font-size: var(--wc7vp);
        font-weight: var(--g9prx);
        letter-spacing: var(--jscho);
        line-height: var(--a9yj8);
      }

      h3 {
        font-size: var(--f05mx);
        font-weight: var(--yi8ir);
        letter-spacing: var(--dtplk);
        line-height: var(--lluln);
      }

      h4 {
        font-size: var(--bwfy4);
        font-weight: var(--m7ps2);
        letter-spacing: var(--zxtx7);
        line-height: var(--xlxxh);
      }

      h5 {
        font-size: var(--klkac);
        font-weight: var(--vpnso);
        letter-spacing: var(--scae5);
        line-height: var(--uzzun);
      }

      h6 {
        font-size: var(--nynp5);
        font-weight: var(--sil3c);
        letter-spacing: var(--lto8g);
        line-height: var(--ojbzp);
      }

      p {
        font-size: var(--b36ib);
        font-weight: var(--we65p);
        letter-spacing: var(--ignrs);
        line-height: var(--fnqmz);
      }

      a {
        color: var(--h3byd);
      }

      em {
        font-style: italic;
      }
      """
    );
  }

}