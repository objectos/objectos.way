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
import objectos.http.Handler;
import objectos.http.HeaderName;
import objectos.http.ServerExchange;
import objectos.http.Status;
import objectos.notes.NoteSink;
import testing.zite.TestingSiteInjector;

final class UiStyles implements Handler {

  private final NoteSink noteSink;

  UiStyles(TestingSiteInjector injector) {
    noteSink = injector.noteSink();
  }

  @Override
  public final void handle(ServerExchange http) {
    WayStyleGen styleGen;
    styleGen = new WayStyleGen();

    styleGen.addUtility("theme-white", """
    --ui-background: #ffffff;
    --ui-background-active: rgba(141, 141, 141, 0.5);
    --ui-background-hover: rgba(141, 141, 141, 0.12);
    --ui-border-subtle-00: #e0e0e0;
    --ui-border-subtle-01: #c6c6c6;
    --ui-border-subtle-02: #e0e0e0;
    --ui-border-subtle-03: #c6c6c6;
    --ui-border-subtle: var(--ui-border-subtle-00);
    --ui-focus: #0f62fe;
    --ui-text-primary: #161616;
    --ui-text-secondary: #525252;
    """);

    styleGen.noteSink(noteSink);

    styleGen.overrideColors(
        Map.entry("transparent", "transparent"),
        Map.entry("background", "var(--ui-background)"),
        Map.entry("background-active", "var(--ui-background-active)"),
        Map.entry("background-hover", "var(--ui-background-hover)"),
        Map.entry("border-subtle", "var(--ui-border-subtle)"),
        Map.entry("focus", "var(--ui-focus)"),
        Map.entry("text-primary", "var(--ui-text-primary)"),
        Map.entry("text-secondary", "var(--ui-text-secondary)")
    );

    styleGen.overrideContent(
        Map.entry("none", "none"),
        Map.entry("empty", "\"\"")
    );

    styleGen.overrideFontSize(
        Map.entry("body-compact-01", """
        font-size: var(--ui-body-compact-01-font-size, 0.875rem);
        font-weight: var(--ui-body-compact-01-font-weight, 400);
        line-height: var(--ui-body-compact-01-line-height, 1.28572);
        letter-spacing: var(--ui-body-compact-01-letter-spacing, 0.16px);
        """)
    );

    styleGen.overrideSpacing(
        Map.entry("0px", "0px"),
        px(1), px(2), px(4), px(6), px(8),
        px(10), px(12), px(14), px(16),
        px(20), px(24), px(28),
        px(32), px(36),
        px(40), px(44), px(48)
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