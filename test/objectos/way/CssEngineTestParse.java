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

public class CssEngineTestParse {

  @Test(description = "colors :: just one")
  public void colors01() {
    CssEngine config;
    config = new CssEngine();

    config.theme("""
    @theme {
       --color-stone-950: oklch(0.147 0.004 49.25);
    }
    """);

    List<CssThemeEntry> entries;
    entries = config.testThemeEntries();

    assertEquals(entries.size(), 1);

    CssThemeEntryOfVariable entry0;
    entry0 = (CssThemeEntryOfVariable) entries.get(0);

    assertEquals(entry0.index(), 0);
    assertEquals(entry0.toString(), "--color-stone-950: oklch(0.147 0.004 49.25);");
    assertEquals(entry0.id(), "stone-950");
  }

  @Test(description = "colors :: two lines")
  public void colors02() {
    CssEngine config;
    config = new CssEngine();

    config.theme("""
    @theme {
      --color-stone-950: oklch(0.147 0.004 49.25);
      --color-red-50: oklch(0.971 0.013 17.38);
    }
    """);

    List<CssThemeEntry> entries;
    entries = config.testThemeEntries();

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
    CssEngine config;
    config = new CssEngine();

    config.theme("""
    @theme {
      --color-orange-900: oklch(0.408 0.123 38.172);
      --color-orange-950: oklch(0.266 0.079 36.259);

      --color-amber-50: oklch(0.987 0.022 95.277);
    }
    """);

    List<CssThemeEntry> entries;
    entries = config.testThemeEntries();

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
    CssEngine config;
    config = new CssEngine();

    config.theme("""
    @theme {
    --color-orange-900:
       oklch(0.408 0.123        38.172)     ;
    }
    """);

    List<CssThemeEntry> entries;
    entries = config.testThemeEntries();

    assertEquals(entries.size(), 1);

    CssThemeEntryOfVariable entry0;
    entry0 = (CssThemeEntryOfVariable) entries.get(0);

    assertEquals(entry0.index(), 0);
    assertEquals(entry0.toString(), "--color-orange-900: oklch(0.408 0.123 38.172);");
    assertEquals(entry0.id(), "orange-900");
  }

  private void variant(String css, Css.Variant... expected) {
    CssEngine config;
    config = new CssEngine();

    config.theme(css);

    List<CssThemeEntry> entries;
    entries = config.testThemeEntries();

    assertEquals(entries.size(), 0);

    List<Css.Variant> variants;
    variants = config.testThemeVariants();

    assertEquals(variants.size(), expected.length);

    for (int idx = 0, len = variants.size(); idx < len; idx++) {
      assertEquals(variants.get(idx), expected[idx]);
    }
  }

  @Test(description = "variants :: placeholder no body")
  public void variant01() {
    variant("@variant focus (&:focus);", new Css.ClassNameFormat("", ":focus"));
  }

  @Test(description = "variants :: multiple variants")
  public void variant02() {
    variant(
        """
        @variant focus (&:focus);
        @variant active (&:active);
        """,

        new Css.ClassNameFormat("", ":focus"),
        new Css.ClassNameFormat("", ":active")
    );
  }

  @Test(description = "variants :: ignore whitespace")
  public void variant03() {
    variant("@variant after ( &::after   );", new Css.ClassNameFormat("", "::after"));
  }

  @Test(description = "variants :: trim intra ws")
  public void variant04() {
    variant("@variant after ( &  >  table   );", new Css.ClassNameFormat("", " > table"));
  }

  private void invalidVariant(String css, String expectedMessage) {
    CssEngine config;
    config = new CssEngine();

    config.theme(css);

    try {
      config.testParse(css);

      Assert.fail("IllegalArgumentException was not thrown");
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), expectedMessage);
    }
  }

  @Test
  public void invalidVariant01() {
    invalidVariant("@variant ;", "Variant name must start with a letter");
  }

  @Test
  public void invalidVariant02() {
    invalidVariant("@variant foo;", "Variant name contains invalid character");
  }

  @Test
  public void invalidVariant03() {
    invalidVariant("@variant foo (;", "Empty variant definition");
  }

  @Test
  public void invalidVariant04() {
    invalidVariant("@variant foo ();", "Empty variant definition");
  }

  @Test
  public void invalidVariant05() {
    invalidVariant("@variant foo (&);", "Empty placeholder definition");
  }

  @Test
  public void invalidVariant06() {
    invalidVariant("@variant foo (& );", "Empty placeholder definition");
  }

  @Test
  public void invalidVariant07() {
    invalidVariant("@variant foo ( &);", "Empty placeholder definition");
  }

  @Test
  public void invalidVariant08() {
    invalidVariant("@variant foo ( &:foo)a;", "Invalid variant definition: expected the ';' character");
  }

  @Test
  public void invalidVariant09() {
    invalidVariant("@variant foo (&&);", "Multiple placeholders found");
  }

  @Test
  public void invalidVariant10() {
    invalidVariant("@variant foo (&::after::&);", "Multiple placeholders found");
  }

}