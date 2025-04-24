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

import java.util.List;
import java.util.stream.Collectors;
import objectos.way.CssEngine.ThemeQueryEntry;
import org.testng.annotations.Test;

public class CssEngineTestParseThemeQuery {

  @Test(description = "single entry")
  public void query01() {
    test(
        "@media (prefers-color-scheme: dark)",

        """
        --color-background: var(--color-gray-800);
        """,

        """
        --color-background: var(--color-gray-800);
        """
    );
  }

  @Test(description = "multiple entries")
  public void query02() {
    test(
        "@media (prefers-color-scheme: dark)",

        """
        --color-background: var(--color-gray-800);

        --color-foreground: var(--color-gray-100);
        --color-border: var(--color-gray-600);
        """,

        """
        --color-background: var(--color-gray-800);
        --color-foreground: var(--color-gray-100);
        --color-border: var(--color-gray-600);
        """
    );
  }

  @Test(description = "Full generation + override")
  public void fullGeneration01() {
    CssEngine engine;
    engine = CssEngine.create(config -> {
      config.theme("""
      --color-*: initial;
      --breakpoint-*: initial;
      --font-*: initial;
      """);

      config.theme("@media (prefers-color-scheme: dark)", """
      --color-background: var(--color-gray-800);
      """);

      config.skipLayer(Css.Layer.BASE);
      config.skipLayer(Css.Layer.COMPONENTS);
      config.skipLayer(Css.Layer.UTILITIES);
    });

    engine.execute();

    assertEquals(
        engine.generate(),

        """
        @layer theme {
          :root {
            --default-font-sans: ui-sans-serif, system-ui, sans-serif, 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol', 'Noto Color Emoji';
            --default-font-serif: ui-serif, Georgia, Cambria, 'Times New Roman', Times, serif;
            --default-font-mono: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, 'Liberation Mono', 'Courier New', monospace;
            --default-font-family: var(--font-sans);
            --default-font-feature-settings: var(--font-sans--font-feature-settings);
            --default-font-variation-settings: var(--font-sans--font-variation-settings);
            --default-mono-font-family: var(--font-mono);
            --default-mono-font-feature-settings: var(--font-mono--font-feature-settings);
            --default-mono-font-variation-settings: var(--font-mono--font-variation-settings);
            --rx: 16;
          }
          @media (prefers-color-scheme: dark) {
            :root {
              --color-background: var(--color-gray-800);
            }
          }
        }
        """
    );
  }

  private void test(String query, String theme, String expected) {
    CssEngine engine;
    engine = CssEngine.create(config -> {
      config.noteSink(Y.noteSink());

      config.theme(query, theme);
    });

    List<ThemeQueryEntry> list = engine.testThemeQueryEntries(query);

    assertEquals(
        list.stream()
            .map(Object::toString)
            .collect(Collectors.joining("\n", "", "\n")),

        expected
    );
  }

}