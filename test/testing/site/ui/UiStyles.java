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

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import objectos.css.WayStyleGen;
import objectos.http.HeaderName;
import objectos.http.Status;
import objectos.notes.NoteSink;
import objectos.way.Http;
import testing.zite.TestingSiteInjector;

final class UiStyles implements Http.Handler {

  private final NoteSink noteSink;

  UiStyles(TestingSiteInjector injector) {
    noteSink = injector.noteSink();
  }

  @Override
  public final void handle(Http.Exchange http) {
    WayStyleGen styleGen;
    styleGen = new WayStyleGen();

    styleGen.noteSink(noteSink);

    styleGen.addRule(":root", """
    --cds-layer: var(--cds-layer-01, #f4f4f4);
    --cds-layer-active: var(--cds-layer-active-01, #c6c6c6);
    --cds-layer-hover: var(--cds-layer-hover-01, #e8e8e8);
    --cds-layer-selected: var(--cds-layer-selected-01, #e0e0e0);
    --cds-layer-selected-hover: var(--cds-layer-selected-hover-01, #d1d1d1);
    --cds-layer-accent: var(--cds-layer-accent-01, #e0e0e0);
    --cds-layer-accent-hover: var(--cds-layer-accent-hover-01, #d1d1d1);
    --cds-layer-accent-active: var(--cds-layer-accent-active-01, #a8a8a8);
    """);

    styleGen.addUtility("theme-white", """
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
    """);

    styleGen.overrideColors(
        Map.entry("transparent", "transparent"),
        Map.entry("background", "var(--cds-background)"),
        Map.entry("background-active", "var(--cds-background-active)"),
        Map.entry("background-hover", "var(--cds-background-hover)"),
        Map.entry("border-subtle", "var(--cds-border-subtle)"),
        Map.entry("focus", "var(--cds-focus)"),
        Map.entry("icon-primary", "var(--cds-icon-primary)"),
        Map.entry("icon-secondary", "var(--cds-icon-secondary)"),
        Map.entry("layer", "var(--cds-layer)"),
        Map.entry("overlay", "var(--cds-overlay)"),
        Map.entry("text-primary", "var(--cds-text-primary)"),
        Map.entry("text-secondary", "var(--cds-text-secondary)")
    );

    styleGen.overrideContent(
        Map.entry("none", "none"),
        Map.entry("empty", "\"\"")
    );

    styleGen.overrideFontSize(
        Map.entry("body-compact-01", """
        font-size: var(--cds-body-compact-01-font-size, 0.875rem);
        font-weight: var(--cds-body-compact-01-font-weight, 400);
        line-height: var(--cds-body-compact-01-line-height, 1.28572);
        letter-spacing: var(--cds-body-compact-01-letter-spacing, 0.16px);
        """),

        Map.entry("heading-compact-01", """
        font-size: var(--cds-heading-compact-01-font-size, 0.875rem);
        font-weight: var(--cds-heading-compact-01-font-weight, 600);
        line-height: var(--cds-heading-compact-01-line-height, 1.28572);
        letter-spacing: var(--cds-heading-compact-01-letter-spacing, 0.16px);
        """)
    );

    styleGen.overrideSpacing(
        Map.entry("0px", "0px"),
        px(1), px(2), px(4), px(6), px(8),
        px(10), px(12), px(14), px(16),
        px(20), px(24), px(28),
        px(32), px(36),
        px(40), px(44), px(48),
        px(208), px(224), px(240), px(256), px(288)
    );

    Set<Class<?>> classes;
    classes = Set.of(UiTemplate.class, ShellPage.class);

    String s;
    s = styleGen.generate(classes);

    byte[] bytes;
    bytes = s.getBytes(StandardCharsets.UTF_8);

    http.status(Status.OK);

    http.dateNow();

    http.header(HeaderName.CONTENT_TYPE, "text/css; charset=utf-8");

    http.header(HeaderName.CONTENT_LENGTH, bytes.length);

    http.send(bytes);
  }

  private Entry<String, String> px(int value) {
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

    return Map.entry(px, rem + "rem");
  }

}