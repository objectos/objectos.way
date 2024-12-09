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

import objectos.way.Css;
import objectos.way.Css.StyleSheet;
import objectos.way.Http;
import objectos.way.Note;
import testing.zite.TestingSiteInjector;

final class UiStyles implements Http.Handler {

  private final Note.Sink noteSink;

  UiStyles(TestingSiteInjector injector) {
    noteSink = injector.noteSink();
  }

  private StyleSheet generateStyleSheet() {
    return Css.generateStyleSheet(
        Css.classes(UiTemplate.class),

        Css.noteSink(noteSink),

        Css.baseLayer("""
        :root {
          --cds-layer: var(--cds-layer-01, #f4f4f4);
          --cds-layer-active: var(--cds-layer-active-01, #c6c6c6);
          --cds-layer-hover: var(--cds-layer-hover-01, #e8e8e8);
          --cds-layer-selected: var(--cds-layer-selected-01, #e0e0e0);
          --cds-layer-selected-hover: var(--cds-layer-selected-hover-01, #d1d1d1);
          --cds-layer-accent: var(--cds-layer-accent-01, #e0e0e0);
          --cds-layer-accent-hover: var(--cds-layer-accent-hover-01, #d1d1d1);
          --cds-layer-accent-active: var(--cds-layer-accent-active-01, #a8a8a8);
        }
        """),

        Css.baseLayer("""
        .theme-white {
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
        }
        """),

        Css.overrideColors("""
        transparent: transparent
        background: var(--cds-background)
        background-active: var(--cds-background-active)
        background-hover: var(--cds-background-hover)
        border-subtle: var(--cds-border-subtle)
        focus: var(--cds-focus)
        icon-primary: var(--cds-icon-primary)
        icon-secondary: var(--cds-icon-secondary)
        layer: var(--cds-layer)
        overlay: var(--cds-overlay)
        text-primary: var(--cds-text-primary)
        text-secondary: var(--cds-text-secondary)
        """),

        Css.overrideContent("""
        none: none
        empty: ""
        """),

        Css.overrideSpacing("""
        0px: 0px
        1px: 0.0625rem
        2px: 0.125rem
        4px: 0.25rem
        6px: 0.375rem
        8px: 0.5rem

        10px: 0.625rem
        12px: 0.75rem
        14px: 0.875rem
        16px: 1rem

        20px: 1.25rem
        24px: 1.5rem
        28px: 1.75rem

        32px: 2rem
        36px: 2.25rem

        40px: 2.5rem
        44px: 2.75rem
        48px: 3rem

        208px: 13rem
        224px: 14rem
        240px: 15rem
        256px: 16rem
        288px: 18rem
        """)
    );
  }

  @Override
  public final void handle(Http.Exchange http) {
    Css.StyleSheet s;
    s = generateStyleSheet();

    http.ok(s);
  }

}