/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
package objectos.way;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class CssEngineTestBase {

  @Test(description = "Do not emit comments")
  public void comment01() {
    base(
        """
        /* some comment */
        """,

        """
        @layer base {
        }
        """
    );
  }

  @Test(description = "Comment after rule")
  public void comment02() {
    base(
        """
        *,
        ::after,
        ::before,
        ::backdrop,
        ::file-selector-button {
          box-sizing: border-box; /* 1 */
          margin: 0; /* 2 */
          padding: 0; /* 2 */
          border: 0 solid; /* 3 */
        }
        """,

        """
        @layer base {
          *, ::after, ::before, ::backdrop, ::file-selector-button {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
            border: 0 solid;
          }
        }
        """
    );
  }

  @Test(description = "single selector")
  public void rule01() {
    base(
        """
        body {
          line-height: inherit;
        }
        """,

        """
        @layer base {
          body {
            line-height: inherit;
          }
        }
        """
    );
  }

  @Test(description = "multiple selectors")
  public void rule02() {
    base(
        """
        html,
        :host {
          line-height: 1.5;
        }
        """,

        """
        @layer base {
          html, :host {
            line-height: 1.5;
          }
        }
        """
    );
  }

  @Test(description = "multiple rules")
  public void rule03() {
    base(
        """
        body {
          line-height: inherit;
        }

        /*
          1. Add the correct height in Firefox.
          2. Correct the inheritance of border color in Firefox. (https://bugzilla.mozilla.org/show_bug.cgi?id=190655)
          3. Reset the default border style to a 1px solid border.
        */

        hr {
          height: 0; /* 1 */
          color: inherit; /* 2 */
          border-top-width: 1px; /* 3 */
        }
        """,

        """
        @layer base {
          body {
            line-height: inherit;
          }
          hr {
            height: 0;
            color: inherit;
            border-top-width: 1px;
          }
        }
        """
    );
  }

  @Test
  public void fullGeneration() {
    CssEngine engine;
    engine = new CssEngine();

    engine.skipLayer(Css.Layer.THEME);
    engine.skipLayer(Css.Layer.COMPONENTS);
    engine.skipLayer(Css.Layer.UTILITIES);

    engine.execute();

    assertEquals(
        engine.generate(),

        """
        @layer base {
          *, ::after, ::before, ::backdrop, ::file-selector-button {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
            border: 0 solid;
          }
          html, :host {
            line-height: 1.5;
            -webkit-text-size-adjust: 100%;
            tab-size: 4;
            font-family: var( --default-font-family, ui-sans-serif, system-ui, sans-serif, 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol', 'Noto Color Emoji' );
            font-feature-settings: var(--default-font-feature-settings, normal);
            font-variation-settings: var(--default-font-variation-settings, normal);
            -webkit-tap-highlight-color: transparent;
          }
          body {
            line-height: inherit;
          }
          hr {
            height: 0;
            color: inherit;
            border-top-width: 1px;
          }
          abbr:where([title]) {
            -webkit-text-decoration: underline dotted;
            text-decoration: underline dotted;
          }
          h1, h2, h3, h4, h5, h6 {
            font-size: inherit;
            font-weight: inherit;
          }
          a {
            color: inherit;
            -webkit-text-decoration: inherit;
            text-decoration: inherit;
          }
          b, strong {
            font-weight: bolder;
          }
          code, kbd, samp, pre {
            font-family: var( --default-mono-font-family, ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, 'Liberation Mono', 'Courier New', monospace );
            font-feature-settings: var(--default-mono-font-feature-settings, normal);
            font-variation-settings: var(--default-mono-font-variation-settings, normal);
            font-size: 1em;
          }
          small {
            font-size: 80%;
          }
          sub, sup {
            font-size: 75%;
            line-height: 0;
            position: relative;
            vertical-align: baseline;
          }
          sub {
            bottom: -0.25em;
          }
          sup {
            top: -0.5em;
          }
          table {
            text-indent: 0;
            border-color: inherit;
            border-collapse: collapse;
          }
          :-moz-focusring {
            outline: auto;
          }
          progress {
            vertical-align: baseline;
          }
          summary {
            display: list-item;
          }
          ol, ul, menu {
            list-style: none;
          }
          img, svg, video, canvas, audio, iframe, embed, object {
            display: block;
            vertical-align: middle;
          }
          img, video {
            max-width: 100%;
            height: auto;
          }
          button, input, select, optgroup, textarea, ::file-selector-button {
            font: inherit;
            font-feature-settings: inherit;
            font-variation-settings: inherit;
            letter-spacing: inherit;
            color: inherit;
            border-radius: 0;
            background-color: transparent;
            opacity: 1;
          }
          :where(select:is([multiple], [size])) optgroup {
            font-weight: bolder;
          }
          :where(select:is([multiple], [size])) optgroup option {
            padding-inline-start: 20px;
          }
          ::file-selector-button {
            margin-inline-end: 4px;
          }
          ::placeholder {
            opacity: 1;
            color: color-mix(in oklab, currentColor 50%, transparent);
          }
          textarea {
            resize: vertical;
          }
          ::-webkit-search-decoration {
            -webkit-appearance: none;
          }
          ::-webkit-date-and-time-value {
            min-height: 1lh;
            text-align: inherit;
          }
          ::-webkit-datetime-edit {
            display: inline-flex;
          }
          ::-webkit-datetime-edit-fields-wrapper {
            padding: 0;
          }
          ::-webkit-datetime-edit, ::-webkit-datetime-edit-year-field, ::-webkit-datetime-edit-month-field, ::-webkit-datetime-edit-day-field, ::-webkit-datetime-edit-hour-field, ::-webkit-datetime-edit-minute-field, ::-webkit-datetime-edit-second-field, ::-webkit-datetime-edit-millisecond-field, ::-webkit-datetime-edit-meridiem-field {
            padding-block: 0;
          }
          :-moz-ui-invalid {
            box-shadow: none;
          }
          button, input:where([type='button'], [type='reset'], [type='submit']), ::file-selector-button {
            appearance: button;
          }
          ::-webkit-inner-spin-button, ::-webkit-outer-spin-button {
            height: auto;
          }
          [hidden]:where(:not([hidden='until-found'])) {
            display: none !important;
          }
        }
        """
    );
  }

  private void base(String base, String expected) {
    CssEngine engine;
    engine = new CssEngine();

    engine.noteSink(TestingNoteSink.INSTANCE);

    engine.base(base);

    engine.skipLayer(Css.Layer.THEME);
    engine.skipLayer(Css.Layer.COMPONENTS);
    engine.skipLayer(Css.Layer.UTILITIES);

    engine.execute();

    String result;
    result = engine.generate();

    assertEquals(result, expected);
  }

}