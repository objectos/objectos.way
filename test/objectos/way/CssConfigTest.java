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
package objectos.way;

import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.Map;
import org.testng.annotations.Test;

public class CssConfigTest {

  @Test
  public void staticUtility01() {
    CssConfig config;
    config = new CssConfig();

    config.staticUtility(
        Css.Key.ACCESSIBILITY,

        """
        sr-only     | position: absolute
                    | width: 1px
                    | height: 1px

        not-sr-only | position: static
                    | width: auto
                    | height: auto
        """
    );

    Css.StaticUtility util01;
    util01 = config.getStatic("sr-only");

    CssProperties props01;
    props01 = util01.properties();

    assertEquals(
        props01.entries(),

        List.of(
            Map.entry("position", "absolute"),
            Map.entry("width", "1px"),
            Map.entry("height", "1px")
        )
    );

    Css.StaticUtility util02;
    util02 = config.getStatic("not-sr-only");

    CssProperties props02;
    props02 = util02.properties();

    assertEquals(
        props02.entries(),

        List.of(
            Map.entry("position", "static"),
            Map.entry("width", "auto"),
            Map.entry("height", "auto")
        )
    );
  }

  @Test(description = "theme :: colors (just one)")
  public void theme01() {
    CssConfig config;
    config = new CssConfig();

    config.theme("""
    --color-stone-950: oklch(0.147 0.004 49.25);
    """);

    List<CssThemeEntry> entries;
    entries = config.themeEntries();

    assertEquals(entries.size(), 1);

    CssThemeEntryOfVariable entry0;
    entry0 = (CssThemeEntryOfVariable) entries.get(0);

    assertEquals(entry0.index(), 0);
    assertEquals(entry0.toString(), "--color-stone-950: oklch(0.147 0.004 49.25);");
  }

  @Test(description = "theme :: colors (two lines)")
  public void theme02() {
    CssConfig config;
    config = new CssConfig();

    config.theme("""
    --color-stone-950: oklch(0.147 0.004 49.25);
    --color-red-50: oklch(0.971 0.013 17.38);
    """);

    List<CssThemeEntry> entries;
    entries = config.themeEntries();

    assertEquals(entries.size(), 2);

    CssThemeEntryOfVariable entry0;
    entry0 = (CssThemeEntryOfVariable) entries.get(0);

    assertEquals(entry0.index(), 0);
    assertEquals(entry0.toString(), "--color-stone-950: oklch(0.147 0.004 49.25);");

    CssThemeEntryOfVariable entry1;
    entry1 = (CssThemeEntryOfVariable) entries.get(1);

    assertEquals(entry1.index(), 1);
    assertEquals(entry1.toString(), "--color-red-50: oklch(0.971 0.013 17.38);");
  }

  @Test(description = "theme :: colors (multiple w/ blank lines)")
  public void theme03() {
    CssConfig config;
    config = new CssConfig();

    config.theme("""
    --color-orange-900: oklch(0.408 0.123 38.172);
    --color-orange-950: oklch(0.266 0.079 36.259);

    --color-amber-50: oklch(0.987 0.022 95.277);
    """);

    List<CssThemeEntry> entries;
    entries = config.themeEntries();

    assertEquals(entries.size(), 3);

    CssThemeEntryOfVariable entry0;
    entry0 = (CssThemeEntryOfVariable) entries.get(0);

    assertEquals(entry0.index(), 0);
    assertEquals(entry0.toString(), "--color-orange-900: oklch(0.408 0.123 38.172);");

    CssThemeEntryOfVariable entry1;
    entry1 = (CssThemeEntryOfVariable) entries.get(1);

    assertEquals(entry1.index(), 1);
    assertEquals(entry1.toString(), "--color-orange-950: oklch(0.266 0.079 36.259);");

    CssThemeEntryOfVariable entry2;
    entry2 = (CssThemeEntryOfVariable) entries.get(2);

    assertEquals(entry2.index(), 2);
    assertEquals(entry2.toString(), "--color-amber-50: oklch(0.987 0.022 95.277);");
  }

  @Test(description = "theme :: colors (it should trim the value)")
  public void theme04() {
    CssConfig config;
    config = new CssConfig();

    config.theme("""
    --color-orange-900:
       oklch(0.408 0.123        38.172)     ;
    """);

    List<CssThemeEntry> entries;
    entries = config.themeEntries();

    assertEquals(entries.size(), 1);

    CssThemeEntryOfVariable entry0;
    entry0 = (CssThemeEntryOfVariable) entries.get(0);

    assertEquals(entry0.index(), 0);
    assertEquals(entry0.toString(), "--color-orange-900: oklch(0.408 0.123 38.172);");
  }

}
