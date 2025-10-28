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

import static objectos.way.CssEngine2.decl;
import static objectos.way.CssEngine2.fun;
import static objectos.way.CssEngine2.number;
import static objectos.way.CssEngine2.block;
import static objectos.way.CssEngine2.tok;
import static objectos.way.CssEngine2.Sep.COMMA;
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
            CssEngine2.decl("--breakpoint-sm", tok("40rem"))
        )
    }, {
        "color: just one",

        """
        --color-stone-950: oklch(0.147 0.004 49.25);
        """,

        List.of(
            CssEngine2.decl("--color-stone-950", fun("oklch", number("0.147"), number("0.004"), number("49.25")))
        )
    }, {
        "color: two lines",

        """
        --color-stone-950: oklch(0.147 0.004 49.25);
        --color-red-50: oklch(0.971 0.013 17.38);
        """,

        List.of(
            CssEngine2.decl("--color-stone-950", fun("oklch", number("0.147"), number("0.004"), number("49.25"))),
            CssEngine2.decl("--color-red-50", fun("oklch", number("0.971"), number("0.013"), number("17.38")))
        )
    }, {
        "color: clear",

        """
        --color-*: initial;
        """,

        List.of(
            CssEngine2.decl("--color-*", tok("initial"))
        )
    }, {
        "custom: allow for values without a ns",

        """
        --carbon-grid-columns: 4;
        """,

        List.of(
            CssEngine2.decl("--carbon-grid-columns", number("4"))
        )
    }, {
        "font: just one",

        """
        --font-display: Foo, "Foo bar";
        """,

        List.of(
            CssEngine2.decl("--font-display", tok("Foo"), COMMA, tok("\"Foo bar\""))
        )
    }, {
        "global: valid",

        """
        --*: initial;
        """,

        List.of(
            CssEngine2.decl("--*", tok("initial"))
        )
    }, {
        "regular",
        "opacity:0;",
        List.of(
            CssEngine2.decl("opacity", number("0"))
        )
    }, {
        "ws: blank line between lines",

        """
        --color-orange-900: oklch(0.408 0.123 38.172);
        --color-orange-950: oklch(0.266 0.079 36.259);

        --color-amber-50: oklch(0.987 0.022 95.277);
        """,

        List.of(
            CssEngine2.decl("--color-orange-900", fun("oklch", number("0.408"), number("0.123"), number("38.172"))),
            CssEngine2.decl("--color-orange-950", fun("oklch", number("0.266"), number("0.079"), number("36.259"))),
            CssEngine2.decl("--color-amber-50", fun("oklch", number("0.987"), number("0.022"), number("95.277")))
        )
    }, {
        "ws: it should trim the name",

        """
        \t\f\r\n --color-orange-900: oklch(0.408 0.123 38.172);
        """,

        List.of(
            CssEngine2.decl("--color-orange-900", fun("oklch", number("0.408"), number("0.123"), number("38.172")))
        )
    }, {
        "ws: it should trim the name",

        """
        --color-orange-900\t\f\r\n : oklch(0.408 0.123 38.172);
        """,

        List.of(
            CssEngine2.decl("--color-orange-900", fun("oklch", number("0.408"), number("0.123"), number("38.172")))
        )
    }, {
        "ws: it should trim the value",

        """
        --color-orange-900:
           oklch(0.408 0.123        38.172)     ;
        """,

        List.of(
            CssEngine2.decl("--color-orange-900", fun("oklch", number("0.408"), number("0.123"), number("38.172")))
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

  @DataProvider
  public Object[][] parseValidProvider() {
    return new Object[][] {{
        "(theme) :root w/ 1 prop",
        """
        :root {
          --font-sans: sans;
        }
        """,
        List.of(
            block(":root",
                decl("--font-sans", tok("sans"))
            )
        )
    }, {
        "(theme) :root w/ 2 props",
        """
        :root {
          --font-sans: sans;
          --font-mono: monospace;
        }
        """,
        List.of(
            block(":root",
                decl("--font-sans", tok("sans")),
                decl("--font-mono", tok("monospace"))
            )
        )
    }, {
        "(theme) dark mode",
        """
        :root { @media (prefers-color-scheme: dark) {
          --color-primary: #f0f0f0;
        }}
        """,
        List.of(
            block(":root",
                block("@media (prefers-color-scheme: dark)",
                    decl("--color-primary", tok("#f0f0f0"))
                )
            )
        )
    }, {
        "(base) type selector",
        """
        /*
         bla
         */

        hr {
          height: 0; /* 1 */
          color: inherit; /* 2 */
        }
        """,
        List.of(
            block("hr",
                decl("height", number("0")),
                decl("color", tok("inherit"))
            )
        )
    }, {
        "(base) list selector",
        """
        html,
        :host {
          line-height: 1.5; /* 1 */
        }
        """,
        List.of(
            block("html, :host",
                decl("line-height", number("1.5"))
            )
        )
    }, {
        "empty",
        "",
        List.of()
    }, {
        "comment",
        "/* comment */",
        List.of()
        //    }, {
        //        "font-feature-settings",
        //        "font-feature-settings: --theme(--default-font-feature-settings, normal)",
        //        Set.of("--default-font-feature-settings")
        //    }, {
        //        "ignore if in comment",
        //        "/* --theme(--foo) */",
        //        Set.of()
        //    }, {
        //        "font-family",
        //        """
        //        html, :host {
        //          font-family: --theme(
        //            --default-font-family,
        //            ui-sans-serif
        //          ); /* 4 */
        //        }
        //        """,
        //        Set.of("--default-font-family")
    }};
  }

  @Test(dataProvider = "parseValidProvider")
  public void parseValid(
      String description,
      String src,
      @SuppressWarnings("exports") List<CssEngine2.Top> expected) {
    final CssEngine2.CssParser parser;
    parser = new CssEngine2.CssParser(src);

    assertEquals(parser.parse(), expected);
  }

  @DataProvider
  public Object[][] valuesValidProvider() {
    return new Object[][] {{
        "1 kw",
        "red-50",
        List.of(
            tok("red-50")
        )
    }, {
        "2 kws",
        "red-50 dashed",
        List.of(
            tok("red-50"), tok("dashed")
        )
    }, {
        "hex-color: 3-value",
        "#f09",
        List.of(
            tok("#f09")
        )
    }, {
        "hex-color: 4-value",
        "#f09a #F09a",
        List.of(
            tok("#f09a"), tok("#F09a")
        )
    }, {
        "hex-color: 6-value & 8-value",
        "#ff0099 #FF0099AA",
        List.of(
            tok("#ff0099"), tok("#FF0099AA")
        )
    }, {
        "length: integer",
        "16rem",
        List.of(
            tok("16rem")
        )
    }, {
        "length: double",
        "14.2pt",
        List.of(
            tok("14.2pt")
        )
    }, {
        "number: integer",
        "16",
        List.of(
            number("16")
        )
    }, {
        "number: neg integer",
        "-16",
        List.of(
            number("-16")
        )
    }, {
        "number: double",
        "16.78",
        List.of(
            number("16.78")
        )
    }, {
        "number: neg double",
        "-16.78",
        List.of(
            number("-16.78")
        )
    }, {
        "number: double no leading zero",
        ".78",
        List.of(
            number(".78")
        )
    }, {
        "percentage: integer",
        "16%",
        List.of(
            tok("16%")
        )
    }, {
        "percentage: double",
        "16.34%",
        List.of(
            tok("16.34%")
        )
    }, {
        "string: double quote",
        "\"Foo\"",
        List.of(
            tok("\"Foo\"")
        )
    }, {
        "string: double quote w/ escaped double quote",
        "\"Foo\\\"Bar\"",
        List.of(
            tok("\"Foo\\\"Bar\"")
        )
    }, {
        "string: single quote",
        "'Foo'",
        List.of(
            tok("'Foo'")
        )
    }, {
        "fun: 1 number",
        "blur(0)",
        List.of(
            fun("blur", number("0"))
        )
    }, {
        "fun: 1 var",
        "var(--foo)",
        List.of(
            fun("var", tok("--foo"))
        )
    }, {
        "fun: 2 vars",
        "var(--foo,var(--bar))",
        List.of(
            fun("var", tok("--foo"), COMMA, fun("var", tok("--bar")))
        )
    }, {
        "fun: 2 vars + ws",
        "var(--foo, var(--bar))",
        List.of(
            fun("var", tok("--foo"), COMMA, fun("var", tok("--bar")))
        )
    }, {
        "--rx custom function",
        "--rx(16)",
        List.of(
            fun("--rx", number("16"))
        )
    }};
  }

  @Test(dataProvider = "valuesValidProvider")
  public void valuesValid(
      String description,
      String text,
      @SuppressWarnings("exports") List<CssEngine2.Value> expected) {
    final CssEngine2.CssParser parser;
    parser = new CssEngine2.CssParser(text);

    final List<CssEngine2.Value> result;
    result = parser.parseValues();

    assertEquals(result, expected);
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

        "Invalid CSS declaration value"
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