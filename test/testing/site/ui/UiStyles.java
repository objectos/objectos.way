/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package testing.site.ui;

import objectos.notes.NoteSink;
import objectos.way.Css;
import objectos.way.Css.StyleSheet;
import objectos.way.Http;
import testing.zite.TestingSiteInjector;

final class UiStyles implements Http.Handler {

  private final NoteSink noteSink;

  UiStyles(TestingSiteInjector injector) {
    noteSink = injector.noteSink();
  }

  private StyleSheet generateStyleSheet() {
    return Css.generateStyleSheet(
        Css.classes(UiTemplate.class, ShellPage.class),

        Css.noteSink(noteSink),

        Css.rule(":root", """
        --cds-layer: var(--cds-layer-01, #f4f4f4);
        --cds-layer-active: var(--cds-layer-active-01, #c6c6c6);
        --cds-layer-hover: var(--cds-layer-hover-01, #e8e8e8);
        --cds-layer-selected: var(--cds-layer-selected-01, #e0e0e0);
        --cds-layer-selected-hover: var(--cds-layer-selected-hover-01, #d1d1d1);
        --cds-layer-accent: var(--cds-layer-accent-01, #e0e0e0);
        --cds-layer-accent-hover: var(--cds-layer-accent-hover-01, #d1d1d1);
        --cds-layer-accent-active: var(--cds-layer-accent-active-01, #a8a8a8);
        """),

        Css.rule(".theme-white", """
        --cds-background: #ffffff;
        --cds-background-active: rgba(141, 141, 141, 0.5);
        --cds-background-hover: rgba(141, 141, 141, 0.12);
        --cds-border-subtle-00: #e0e0e0;
        --cds-border-subtle-01: #c6c6c6;
        --cds-border-subtle-02: #e0e0e0;
        --cds-border-subtle-03: #c6c6c6;
        --cds-border-subtle: var(--cds-border-subtle-00);
        --cds-focus: #0f62fe;
        --cds-icon-primary: #161616;
        --cds-icon-secondary: #525252;
        --cds-layer-01: #f4f4f4;
        --cds-layer-02: #ffffff;
        --cds-layer-03: #f4f4f4;
        --cds-overlay: rgba(22, 22, 22, 0.5);
        --cds-text-primary: #161616;
        --cds-text-secondary: #525252;
        """),

        Css.overrideColors(
            Css.kv("transparent", "transparent"),
            Css.kv("background", "var(--cds-background)"),
            Css.kv("background-active", "var(--cds-background-active)"),
            Css.kv("background-hover", "var(--cds-background-hover)"),
            Css.kv("border-subtle", "var(--cds-border-subtle)"),
            Css.kv("focus", "var(--cds-focus)"),
            Css.kv("icon-primary", "var(--cds-icon-primary)"),
            Css.kv("icon-secondary", "var(--cds-icon-secondary)"),
            Css.kv("layer", "var(--cds-layer)"),
            Css.kv("overlay", "var(--cds-overlay)"),
            Css.kv("text-primary", "var(--cds-text-primary)"),
            Css.kv("text-secondary", "var(--cds-text-secondary)")
        ),

        Css.overrideContent(
            Css.kv("none", "none"),
            Css.kv("empty", "\"\"")
        ),

        Css.overrideFontSize(
            Css.kv("body-compact-01", """
                font-size: var(--cds-body-compact-01-font-size, 0.875rem);
                font-weight: var(--cds-body-compact-01-font-weight, 400);
                line-height: var(--cds-body-compact-01-line-height, 1.28572);
                letter-spacing: var(--cds-body-compact-01-letter-spacing, 0.16px);
                """),

            Css.kv("heading-compact-01", """
                font-size: var(--cds-heading-compact-01-font-size, 0.875rem);
                font-weight: var(--cds-heading-compact-01-font-weight, 600);
                line-height: var(--cds-heading-compact-01-line-height, 1.28572);
                letter-spacing: var(--cds-heading-compact-01-letter-spacing, 0.16px);
                """)
        ),

        Css.overrideSpacing(
            Css.kv("0px", "0px"),
            px(1), px(2), px(4), px(6), px(8),
            px(10), px(12), px(14), px(16),
            px(20), px(24), px(28),
            px(32), px(36),
            px(40), px(44), px(48),
            px(208), px(224), px(240), px(256), px(288)
        )
    );
  }

  @Override
  public final void handle(Http.Exchange http) {
    Css.StyleSheet s;
    s = generateStyleSheet();

    byte[] bytes;
    bytes = s.toByteArray();

    http.status(Http.OK);

    http.dateNow();

    http.header(Http.CONTENT_TYPE, s.contentType());

    http.header(Http.CONTENT_LENGTH, bytes.length);

    http.send(bytes);
  }

  private Css.Generator.KeyValue px(int value) {
    String px;
    px = Integer.toString(value) + "px";

    double remValue;
    remValue = ((double) value) / 16;

    String rem;

    if (remValue == Math.rint(remValue)) {
      rem = Integer.toString((int) remValue);
    } else {
      rem = Double.toString(remValue);
    }

    return Css.kv(px, rem + "rem");
  }

}