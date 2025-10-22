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
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CssEngine2Test00CssParser {

  @DataProvider
  public Object[][] declsValidProvider() {
    return new Object[][] {{
        "breakpoint: just one",

        """
        --breakpoint-sm: 40rem;
        """,

        List.of(
            CssEngine2.decl("--breakpoint-sm", "40rem")
        )
    }, {
        "color: just one",

        """
        --color-stone-950: oklch(0.147 0.004 49.25);
        """,

        List.of(
            CssEngine2.decl("--color-stone-950", "oklch(0.147 0.004 49.25)")
        )
    }, {
        "color: two lines",

        """
        --color-stone-950: oklch(0.147 0.004 49.25);
        --color-red-50: oklch(0.971 0.013 17.38);
        """,

        List.of(
            CssEngine2.decl("--color-stone-950", "oklch(0.147 0.004 49.25)"),
            CssEngine2.decl("--color-red-50", "oklch(0.971 0.013 17.38)")
        )
    }, {
        "color: clear",

        """
        --color-*: initial;
        """,

        List.of(
            CssEngine2.decl("--color-*", "initial")
        )
    }, {
        "custom: allow for values without a ns",

        """
        --carbon-grid-columns: 4;
        """,

        List.of(
            CssEngine2.decl("--carbon-grid-columns", "4")
        )
    }, {
        "font: just one",

        """
        --font-display: Foo, "Foo bar";
        """,

        List.of(
            CssEngine2.decl("--font-display", "Foo, \"Foo bar\"")
        )
    }, {
        "global: valid",

        """
        --*: initial;
        """,

        List.of(
            CssEngine2.decl("--*", "initial")
        )
    }, {
        "regular",
        "opacity:0;",
        List.of(
            CssEngine2.decl("opacity", "0")
        )
    }, {
        "rx",

        """
        --rx: 16;
        """,

        List.of(
            CssEngine2.decl("--rx", "16")
        )
    }, {
        "ws: blank line between lines",

        """
        --color-orange-900: oklch(0.408 0.123 38.172);
        --color-orange-950: oklch(0.266 0.079 36.259);

        --color-amber-50: oklch(0.987 0.022 95.277);
        """,

        List.of(
            CssEngine2.decl("--color-orange-900", "oklch(0.408 0.123 38.172)"),
            CssEngine2.decl("--color-orange-950", "oklch(0.266 0.079 36.259)"),
            CssEngine2.decl("--color-amber-50", "oklch(0.987 0.022 95.277)")
        )
    }, {
        "ws: it should trim the name",

        """
        \t\f\r\n --color-orange-900: oklch(0.408 0.123 38.172);
        """,

        List.of(
            CssEngine2.decl("--color-orange-900", "oklch(0.408 0.123 38.172)")
        )
    }, {
        "ws: it should trim the name",

        """
        --color-orange-900\t\f\r\n : oklch(0.408 0.123 38.172);
        """,

        List.of(
            CssEngine2.decl("--color-orange-900", "oklch(0.408 0.123 38.172)")
        )
    }, {
        "ws: it should trim the value",

        """
        --color-orange-900:
           oklch(0.408 0.123        38.172)     ;
        """,

        List.of(
            CssEngine2.decl("--color-orange-900", "oklch(0.408 0.123 38.172)")
        )
    }};
  }

  @Test(dataProvider = "declsValidProvider")
  public void declsValid(
      String description,
      String src,
      @SuppressWarnings("exports") List<CssEngine2.Decl> expected) {
    final CssEngine2.CssParser parser;
    parser = new CssEngine2.CssParser(src);

    final List<CssEngine2.Decl> result;
    result = parser.parseDecls();

    assertEquals(result, expected);
  }

  @DataProvider
  public Object[][] idenValidProvider() {
    return new Object[][] {
        {"same", "fade-in", "fade-in"},
        {"trim leading", " \tfade-in", "fade-in"},
        {"trim trailing", "fade-in\f  ", "fade-in"},
        {"trim both", "  \nfade-in\r  ", "fade-in"}
    };
  }

  @Test(dataProvider = "idenValidProvider")
  public void idenValid(
      String description,
      String src,
      String expected) {
    final CssEngine2.CssParser parser;
    parser = new CssEngine2.CssParser(src);

    assertEquals(parser.parseIden(), expected);
  }

  @Test(description = "EOF at declaration value")
  public void errors01() {
    testIAE(
        """
        --color-orange-900: oklch(0.408 0.123 38.172);
        --color-orange-950:
        """,

        "EOF while parsing rule declarations"
    );
  }

  @Test(description = "EOF at declaration value")
  public void errors02() {
    testIAE(
        """
        --color-orange-900: oklch(0.408 0.123 38.172);
        --color-orange-950: okl""",

        "EOF while parsing rule declarations"
    );
  }

  @Test(description = "EOF at declaration name")
  public void errors03() {
    testIAE(
        """
        --color-orange-900: oklch(0.408 0.123 38.172);
        --color-orange""",

        "EOF while parsing rule declarations"
    );
  }

  @Test(description = "EOF before colon")
  public void errors04() {
    testIAE(
        """
        --color-orange-900: oklch(0.408 0.123 38.172);
        --color-orange-950\040""",

        "Expected ':' after a CSS property name"
    );
  }

  private void testIAE(String value, String expectedMsg) {
    try {
      final CssEngine2.CssParser parser;
      parser = new CssEngine2.CssParser(value);

      parser.parseDecls();

      Assert.fail("it should have thrown");
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), expectedMsg);
    }
  }

}