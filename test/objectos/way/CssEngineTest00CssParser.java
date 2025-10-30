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

import static objectos.way.CssEngine.decl;
import static objectos.way.CssEngine.fontFace;
import static objectos.way.CssEngine.fun;
import static objectos.way.CssEngine.keyframes;
import static objectos.way.CssEngine.number;
import static objectos.way.CssEngine.block;
import static objectos.way.CssEngine.tok;
import static objectos.way.CssEngine.Sep.COMMA;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CssEngineTest00CssParser {

  @DataProvider
  public Object[][] declsValidProvider() {
    return new Object[][] {{
        "breakpoint: just one",

        """
        --breakpoint-sm: 40rem;
        """,

        List.of(
            CssEngine.decl("--breakpoint-sm", tok("40rem"))
        )
    }, {
        "color: just one",

        """
        --color-stone-950: oklch(0.147 0.004 49.25);
        """,

        List.of(
            CssEngine.decl("--color-stone-950", fun("oklch", number("0.147"), number("0.004"), number("49.25")))
        )
    }, {
        "color: two lines",

        """
        --color-stone-950: oklch(0.147 0.004 49.25);
        --color-red-50: oklch(0.971 0.013 17.38);
        """,

        List.of(
            CssEngine.decl("--color-stone-950", fun("oklch", number("0.147"), number("0.004"), number("49.25"))),
            CssEngine.decl("--color-red-50", fun("oklch", number("0.971"), number("0.013"), number("17.38")))
        )
    }, {
        "color: clear",

        """
        --color-*: initial;
        """,

        List.of(
            CssEngine.decl("--color-*", tok("initial"))
        )
    }, {
        "custom: allow for values without a ns",

        """
        --carbon-grid-columns: 4;
        """,

        List.of(
            CssEngine.decl("--carbon-grid-columns", number("4"))
        )
    }, {
        "font: just one",

        """
        --font-display: Foo, "Foo bar";
        """,

        List.of(
            CssEngine.decl("--font-display", tok("Foo"), COMMA, tok("\"Foo bar\""))
        )
    }, {
        "global: valid",

        """
        --*: initial;
        """,

        List.of(
            CssEngine.decl("--*", tok("initial"))
        )
    }, {
        "regular",
        "opacity:0;",
        List.of(
            CssEngine.decl("opacity", number("0"))
        )
    }, {
        "ws: blank line between lines",

        """
        --color-orange-900: oklch(0.408 0.123 38.172);
        --color-orange-950: oklch(0.266 0.079 36.259);

        --color-amber-50: oklch(0.987 0.022 95.277);
        """,

        List.of(
            CssEngine.decl("--color-orange-900", fun("oklch", number("0.408"), number("0.123"), number("38.172"))),
            CssEngine.decl("--color-orange-950", fun("oklch", number("0.266"), number("0.079"), number("36.259"))),
            CssEngine.decl("--color-amber-50", fun("oklch", number("0.987"), number("0.022"), number("95.277")))
        )
    }, {
        "ws: it should trim the name",

        """
        \t\f\r\n --color-orange-900: oklch(0.408 0.123 38.172);
        """,

        List.of(
            CssEngine.decl("--color-orange-900", fun("oklch", number("0.408"), number("0.123"), number("38.172")))
        )
    }, {
        "ws: it should trim the name",

        """
        --color-orange-900\t\f\r\n : oklch(0.408 0.123 38.172);
        """,

        List.of(
            CssEngine.decl("--color-orange-900", fun("oklch", number("0.408"), number("0.123"), number("38.172")))
        )
    }, {
        "ws: it should trim the value",

        """
        --color-orange-900:
           oklch(0.408 0.123        38.172)     ;
        """,

        List.of(
            CssEngine.decl("--color-orange-900", fun("oklch", number("0.408"), number("0.123"), number("38.172")))
        )
    }};
  }

  @Test(dataProvider = "declsValidProvider")
  public void declsValid(
      String description,
      String src,
      @SuppressWarnings("exports") List<CssEngine.Decl> expected) {
    final String source;
    source = "foo { %s }".formatted(src);

    final CssEngine.CssParser parser;
    parser = new CssEngine.CssParser(source);

    final List<CssEngine.Top> top;
    top = parser.parse();

    assertEquals(top.size(), 1);

    final CssEngine.Top only;
    only = top.get(0);

    if (!(only instanceof CssEngine.Block(String selector, List<CssEngine.Stmt> stmts))) {
      throw new AssertionError();
    }

    assertEquals(selector, "foo");

    assertEquals(stmts, expected);
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
        "(theme) keyframes",
        """
        @keyframes fade-in {
          from {
            opacity: 0;
          }
          to {
            opacity: 1;
          }
        }
        """,
        List.of(
            keyframes("fade-in",
                block("from",
                    decl("opacity", number("0"))
                ),
                block("to",
                    decl("opacity", number("1"))
                )
            )
        )
    }, {
        "(theme) keyframes (percentage)",
        """
        @keyframes fade-in {
          0% {
            opacity: 0;
          }
          100% {
            opacity: 1;
          }
        }
        """,
        List.of(
            keyframes("fade-in",
                block("0%",
                    decl("opacity", number("0"))
                ),
                block("100%",
                    decl("opacity", number("1"))
                )
            )
        )
    }, {
        "(theme) @font-face",
        """
        @font-face {
          font-family: "IBM Plex Sans";
          font-style: normal;
          font-weight: 700;
          src: local("IBM Plex Sans Bold");
        }
        """,
        List.of(
            fontFace(
                decl("font-family", tok("\"IBM Plex Sans\"")),
                decl("font-style", tok("normal")),
                decl("font-weight", number("700")),
                decl("src", fun("local", tok("\"IBM Plex Sans Bold\"")))
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
        "(base) selector",
        """
        abbr:where([title]) {}
        """,
        List.of(
            block("abbr:where([title])")
        )
    }, {
        "(components) class selector",
        """
        .foo {}
        """,
        List.of(
            block(".foo")
        )
    }, {
        "(components) attr selector",
        """
        [foo] {}
        [foo=bar] {}
        """,
        List.of(
            block("[foo]"),
            block("[foo=bar]")
        )
    }, {
        "empty",
        "",
        List.of()
    }, {
        "comment",
        "/* comment */",
        List.of()
    }};
  }

  @Test(dataProvider = "parseValidProvider")
  public void parseValid(
      String description,
      String src,
      @SuppressWarnings("exports") List<CssEngine.Top> expected) {
    final CssEngine.CssParser parser;
    parser = new CssEngine.CssParser(src);

    assertEquals(parser.parse(), expected);
  }

  @DataProvider
  public Object[][] parseInvalidProvider() {
    return new Object[][] {{
        "EOF at declaration value",
        """
        foo {
          --color-orange-900: oklch(0.408 0.123 38.172);
          --color-orange-950:
        """,
        "EOF while parsing rule declarations"
    }, {
        "EOF at declaration value",
        """
        foo {
          --color-orange-900: oklch(0.408 0.123 38.172);
          --color-orange-950: okl""",
        "Invalid CSS declaration value"
    }, {
        "EOF at declaration name",
        """
        foo {
          --color-orange-900: oklch(0.408 0.123 38.172);
          --color-orange""",
        "EOF while parsing rule declarations"
    }, {
        "EOF before colon",
        """
        foo {
          --color-orange-900: oklch(0.408 0.123 38.172);
          --color-orange-950\040""",
        "Expected ':' after a CSS property name"
    }};
  }

  @Test(dataProvider = "parseInvalidProvider")
  public void parseInvalid(
      String description,
      String src,
      String message) {
    try {
      final CssEngine.CssParser parser;
      parser = new CssEngine.CssParser(src);

      parser.parse();

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      final String actual;
      actual = expected.getMessage();

      assertTrue(actual.startsWith(message));
    }
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
      @SuppressWarnings("exports") List<CssEngine.Value> expected) {
    final CssEngine.CssParser parser;
    parser = new CssEngine.CssParser(text);

    final List<CssEngine.Value> result;
    result = parser.parseValues();

    assertEquals(result, expected);
  }

}