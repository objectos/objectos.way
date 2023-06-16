/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.css;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class PreflightTest {

  @Test
  public void test() {
    assertEquals(
      new Preflight().toString(),

      """
      *, ::before, ::after {
        box-sizing: border-box;
        border-width: 0;
        border-style: solid;
        border-color: currentcolor;
      }
      html {
        line-height: 1.5;
        -webkit-text-size-adjust: 100%;
        -moz-tab-size: 4;
        tab-size: 4;
        font-family: ui-sans-serif, system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, "Noto Sans", sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", "Noto Color Emoji";
        font-feature-settings: normal;
        font-variation-settings: normal;
      }
      body {
        margin: 0;
        line-height: inherit;
      }
      hr {
        height: 0;
        color: inherit;
        border-top-width: 1px;
      }
      h1, h2, h3, h4, h5, h6 {
        font-size: inherit;
        font-weight: inherit;
      }
      a {
        color: inherit;
        text-decoration: inherit;
      }
      b, strong {
        font-weight: bolder;
      }
      code, kbd, samp, pre {
        font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace;
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
      """
    );
  }

}