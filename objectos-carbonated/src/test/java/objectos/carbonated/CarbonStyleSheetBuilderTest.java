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

import static org.testng.Assert.assertEquals;

import objectos.carbonated.Carbon.StyleSheetBuilder;
import objectos.css.Css;
import objectos.css.StyleSheet;
import org.testng.annotations.Test;

public class CarbonStyleSheetBuilderTest {

  static {
    Css.randomSeed(84321674516L);
  }

  @Test
  public void notification() {
    StyleSheetBuilder b;
    b = Carbon.styleSheetBuilder();

    b.notification();

    StyleSheet sheet;
    sheet = b.build();

    assertEquals(
      sheet.toString(),

      """
      .qdtwb {
        color: var(--safqc);
        display: flex;
        flex-wrap: wrap;
        height: auto;
        max-width: 18rem;
        min-height: 3rem;
        min-width: 18rem;
        position: relative;
        width: 100%;
      }

      @media (min-width: 672px) {
        .qdtwb {
          flex-wrap: nowrap;
          max-width: 38rem;
        }
      }

      @media (min-width: 1056px) {
        .qdtwb {
          max-width: 46rem;
        }
      }

      @media (min-width: 1584px) {
        .qdtwb {
          max-width: 52rem;
        }
      }

      .qdtwb a {
        text-decoration: none;
      }

      .qdtwb a:hover {
        text-decoration: underline;
      }

      .qdtwb a:focus {
        outline: 1px solid var(--ckkls);
      }

      .qdtwb.uhxxi a:focus {
        outline: 1px solid var(--x13au);
      }

      .uhxxi {
        color: var(--vlofe);
      }

      .uhxxi::before {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        box-sizing: border-box;
        border-width: 1px 1px 1px 0;
        border-style: solid;
        content: "";
        -webkit-filter: opacity(0.4);
        filter: opacity(0.4);
        pointer-events: none;
      }

      .kclo6 {
        background: var(--wecdp);
        border-left: 3px solid var(--tnsoz);
      }

      .kclo6 .o3ugz {
        fill: var(--tnsoz);
      }

      .uhxxi.kclo6 {
        background: var(--trnvh);
        border-left: 3px solid var(--lsbuq);
      }

      .uhxxi.kclo6::before {
        border-color: var(--lsbuq);
      }

      .uhxxi.kclo6 .o3ugz {
        fill: var(--lsbuq);
      }

      .h3byd {
        background: var(--wecdp);
        border-left: 3px solid var(--k85sk);
      }

      .h3byd .o3ugz {
        fill: var(--k85sk);
      }

      .uhxxi.h3byd {
        background: var(--gmei0);
        border-left: 3px solid var(--lw7rp);
      }

      .uhxxi.h3byd::before {
        border-color: var(--lw7rp);
      }

      .uhxxi.h3byd .o3ugz {
        fill: var(--lw7rp);
      }

      .jg4cx, .lwfuy {
        background: var(--wecdp);
        border-left: 3px solid var(--qifxa);
      }

      .jg4cx .o3ugz {
        fill: var(--qifxa);
      }

      .uhxxi.jg4cx {
        background: var(--b36ib);
        border-left: 3px solid var(--mahxj);
      }

      .uhxxi.jg4cx::before {
        border-color: var(--mahxj);
      }

      .uhxxi.jg4cx .o3ugz {
        fill: var(--mahxj);
      }

      .j2jyg, .a7ns9 {
        background: var(--wecdp);
        border-left: 3px solid var(--jk6nm);
      }

      .j2jyg .o3ugz {
        fill: var(--jk6nm);
      }

      .uhxxi.j2jyg {
        background: var(--we65p);
        border-left: 3px solid var(--pnpum);
      }

      .uhxxi.j2jyg::before {
        border-color: var(--pnpum);
      }

      .uhxxi.j2jyg .o3ugz {
        fill: var(--pnpum);
      }

      .tx7x8 {
        display: flex;
        flex-grow: 1;
        margin: 0 3rem 0 0.8125rem;
      }

      @media (min-width: 672px) {
        .tx7x8 {
          margin: 0 0.8125rem;
        }
      }

      .o3ugz {
        flex-shrink: 0;
        margin-top: 0.875rem;
        margin-right: 1rem;
      }

      .skdp7 {
        display: flex;
        flex-wrap: wrap;
        padding: 0.9375rem 0;
      }

      .czrw0 {
        font-size: var(--m7ps2);
        font-weight: var(--xlxxh);
        letter-spacing: var(--f05mx);
        line-height: var(--zxtx7);
        margin: 0 0.25rem 0 0;
      }

      .wcdsj {
        font-size: var(--znlsq);
        font-weight: var(--tx1vy);
        letter-spacing: var(--nynp5);
        line-height: var(--azu7j);
        word-break: break-word;
      }
      """
    );
  }

  @Test
  public void reset() {
    StyleSheetBuilder b;
    b = Carbon.styleSheetBuilder();

    b.reset();

    StyleSheet sheet;
    sheet = b.build();

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
  public void theme() {
    StyleSheet sheet;
    sheet = Carbon.styleSheetBuilder()
        .themes(Theme.white())
        .build();

    assertEquals(
      sheet.toString(),

      """
      .w7m4p {
        background: var(--vuaez);
        color: var(--vlofe);
        --vuaez: #ffffff;
        --wecdp: #393939;
        --vlofe: #161616;
        --safqc: #ffffff;
        --dmdpg: #0f62fe;
        --ckkls: #78a9ff;
        --lsbuq: #da1e28;
        --lw7rp: #24a148;
        --pnpum: #f1c21b;
        --mahxj: #0043ce;
        --tnsoz: #fa4d56;
        --k85sk: #42be65;
        --jk6nm: #f1c21b;
        --qifxa: #4589ff;
        --x13au: #0f62fe;
        --trnvh: #fff1f1;
        --gmei0: #defbe6;
        --b36ib: #edf5ff;
        --we65p: #fdf6dd;
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
      :root {
        --fnqmz: 300;
        --ignrs: 400;
        --oywdo: 600;
        --znlsq: 0.875rem;
        --tx1vy: 400;
        --azu7j: 1.28572;
        --nynp5: 0.16px;
        --sil3c: 0.875rem;
        --ojbzp: 400;
        --lto8g: 1.42857;
        --klkac: 0.16px;
        --vpnso: 1rem;
        --uzzun: 400;
        --scae5: 1.5;
        --bwfy4: 0px;
        --m7ps2: 0.875rem;
        --xlxxh: 600;
        --zxtx7: 1.28572;
        --f05mx: 0.16px;
        --yi8ir: 0.875rem;
        --lluln: 600;
        --dtplk: 1.42857;
        --wc7vp: 0.16px;
        --g9prx: 1rem;
        --a9yj8: 600;
        --jscho: 1.5;
        --rsfnl: 0px;
        --oabvq: 1.25rem;
        --qca2u: 400;
        --kfzqj: 1.4;
        --edg1a: 0px;
        --djnuk: 1.75rem;
        --dixkg: 400;
        --oqrts: 1.28572;
        --xvlpe: 0px;
        --mchyw: 2rem;
        --ngdnt: 400;
        --cekhl: 1.25;
        --bwllq: 0px;
        --lprso: 2.625rem;
        --aacpf: 300;
        --zsw7m: 1.199;
        --jqhqo: 0px;
      }

      html {
        font-size: 100%;
      }

      body {
        font-weight: var(--ignrs);
        font-family: sans-serif;
      }

      code {
        font-family: monospace;
      }

      strong {
        font-weight: var(--oywdo);
      }

      h1 {
        font-size: var(--lprso);
        font-weight: var(--aacpf);
        letter-spacing: var(--jqhqo);
        line-height: var(--zsw7m);
      }

      h2 {
        font-size: var(--mchyw);
        font-weight: var(--ngdnt);
        letter-spacing: var(--bwllq);
        line-height: var(--cekhl);
      }

      h3 {
        font-size: var(--djnuk);
        font-weight: var(--dixkg);
        letter-spacing: var(--xvlpe);
        line-height: var(--oqrts);
      }

      h4 {
        font-size: var(--oabvq);
        font-weight: var(--qca2u);
        letter-spacing: var(--edg1a);
        line-height: var(--kfzqj);
      }

      h5 {
        font-size: var(--g9prx);
        font-weight: var(--a9yj8);
        letter-spacing: var(--rsfnl);
        line-height: var(--jscho);
      }

      h6 {
        font-size: var(--yi8ir);
        font-weight: var(--lluln);
        letter-spacing: var(--wc7vp);
        line-height: var(--dtplk);
      }

      p {
        font-size: var(--vpnso);
        font-weight: var(--uzzun);
        letter-spacing: var(--bwfy4);
        line-height: var(--scae5);
      }

      a {
        color: var(--dmdpg);
      }

      em {
        font-style: italic;
      }
      """
    );
  }

}