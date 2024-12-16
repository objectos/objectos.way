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
import org.testng.Assert;
import org.testng.annotations.Test;

public class CssEngineTestParseTheme {

  @Test(description = "colors :: just one")
  public void colors01() {
    List<CssThemeEntry> entries;
    entries = test("""
    --color-stone-950: oklch(0.147 0.004 49.25);
    """);

    assertEquals(entries.size(), 1);

    CssThemeEntryOfVariable entry0;
    entry0 = (CssThemeEntryOfVariable) entries.get(0);

    assertEquals(entry0.index(), 0);
    assertEquals(entry0.toString(), "--color-stone-950: oklch(0.147 0.004 49.25);");
    assertEquals(entry0.id(), "stone-950");
  }

  @Test(description = "colors :: two lines")
  public void colors02() {
    List<CssThemeEntry> entries;
    entries = test("""
    --color-stone-950: oklch(0.147 0.004 49.25);
    --color-red-50: oklch(0.971 0.013 17.38);
    """);

    assertEquals(entries.size(), 2);

    CssThemeEntryOfVariable entry0;
    entry0 = (CssThemeEntryOfVariable) entries.get(0);

    assertEquals(entry0.index(), 0);
    assertEquals(entry0.toString(), "--color-stone-950: oklch(0.147 0.004 49.25);");
    assertEquals(entry0.id(), "stone-950");

    CssThemeEntryOfVariable entry1;
    entry1 = (CssThemeEntryOfVariable) entries.get(1);

    assertEquals(entry1.index(), 1);
    assertEquals(entry1.toString(), "--color-red-50: oklch(0.971 0.013 17.38);");
    assertEquals(entry1.id(), "red-50");
  }

  @Test(description = "colors :: multiple w/ blank lines")
  public void colors03() {
    List<CssThemeEntry> entries;
    entries = test("""
    --color-orange-900: oklch(0.408 0.123 38.172);
    --color-orange-950: oklch(0.266 0.079 36.259);

    --color-amber-50: oklch(0.987 0.022 95.277);
    """);

    assertEquals(entries.size(), 3);

    CssThemeEntryOfVariable entry0;
    entry0 = (CssThemeEntryOfVariable) entries.get(0);

    assertEquals(entry0.index(), 0);
    assertEquals(entry0.toString(), "--color-orange-900: oklch(0.408 0.123 38.172);");
    assertEquals(entry0.id(), "orange-900");

    CssThemeEntryOfVariable entry1;
    entry1 = (CssThemeEntryOfVariable) entries.get(1);

    assertEquals(entry1.index(), 1);
    assertEquals(entry1.toString(), "--color-orange-950: oklch(0.266 0.079 36.259);");
    assertEquals(entry1.id(), "orange-950");

    CssThemeEntryOfVariable entry2;
    entry2 = (CssThemeEntryOfVariable) entries.get(2);

    assertEquals(entry2.index(), 2);
    assertEquals(entry2.toString(), "--color-amber-50: oklch(0.987 0.022 95.277);");
    assertEquals(entry2.id(), "amber-50");
  }

  @Test(description = "colors :: it should trim the value")
  public void colors04() {
    List<CssThemeEntry> entries;
    entries = test("""
    --color-orange-900:
       oklch(0.408 0.123        38.172)     ;
    """);

    assertEquals(entries.size(), 1);

    CssThemeEntryOfVariable entry0;
    entry0 = (CssThemeEntryOfVariable) entries.get(0);

    assertEquals(entry0.index(), 0);
    assertEquals(entry0.toString(), "--color-orange-900: oklch(0.408 0.123 38.172);");
    assertEquals(entry0.id(), "orange-900");
  }

  @Test(description = "Incomplete variable declaration at the end")
  public void errors01() {
    try {
      test("""
      --color-orange-900: oklch(0.408 0.123 38.172);
      --color-orange-950:
      """);

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "Unexpected end of theme");
    }
  }

  private List<CssThemeEntry> test(String theme) {
    CssEngine engine;
    engine = new CssEngine();

    engine.theme(theme);

    return engine.testThemeEntries();
  }

}