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
import static objectos.way.CssEngine.keyframes;
import static objectos.way.CssEngine.block;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CssEngineTest00CssParser {

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
                decl("--font-sans", "sans")
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
                decl("--font-sans", "sans"),
                decl("--font-mono", "monospace")
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
                    decl("--color-primary", "#f0f0f0")
                )
            )
        )
    }, {
        "(theme) ",
        """
        :root { @media(min-width: 42rem) { }}
        """,
        List.of(
            block(":root", block("@media (min-width: 42rem)"))
        )
    }, {
        "(theme) custom property without namespace",
        """
        :root {
          --carbon-grid-columns: 4;
        }
        """,
        List.of(
            block(":root",
                decl("--carbon-grid-columns", "4")
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
                    decl("opacity", "0")
                ),
                block("to",
                    decl("opacity", "1")
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
                    decl("opacity", "0")
                ),
                block("100%",
                    decl("opacity", "1")
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
                decl("font-family", "\"IBM Plex Sans\""),
                decl("font-style", "normal"),
                decl("font-weight", "700"),
                decl("src", "local(\"IBM Plex Sans Bold\")")
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
                decl("height", "0"),
                decl("color", "inherit")
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
                decl("line-height", "1.5")
            )
        )
    }, {
        "(base) replace --theme() with var()",
        """
        html,
        :host {
          font-family: --theme(
            --default-font-family,
            ui-sans-serif
          );
        }
        """,
        List.of(
            block("html, :host",
                decl("font-family", "var(--default-font-family, ui-sans-serif)")
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
        "(base) nth selector",
        """
        :nth-child(even) {}
        :nth-child(7) {}
        :nth-child(2n) {}
        :nth-child(2n + 1) {}
        :nth-child(2n+1) {}
        :nth-child(-n + 3) {}
        :nth-child(-n+3) {}
        """,
        List.of(
            block(":nth-child(even)"),
            block(":nth-child(7)"),
            block(":nth-child(2n)"),
            block(":nth-child(2n + 1)"),
            block(":nth-child(2n + 1)"),
            block(":nth-child(-n + 3)"),
            block(":nth-child(-n + 3)")
        )
    }, {
        "(base) @supports at-rule",
        """
        @supports (not (-webkit-appearance: -apple-pay-button)) /* Not Safari */ or
          (contain-intrinsic-size: 1px) /* Safari 17+ */ {}
        """,
        List.of(
            block("@supports (not (-webkit-appearance: -apple-pay-button)) or (contain-intrinsic-size: 1px)")
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
        "(ws) blank line between lines",
        """
        :root {
          --color-orange-900: oklch(0.408 0.123 38.172);
          --color-orange-950: oklch(0.266 0.079 36.259);

          --color-amber-50: oklch(0.987 0.022 95.277);
        }
        """,
        List.of(
            block(":root",
                decl("--color-orange-900", "oklch(0.408 0.123 38.172)"),
                decl("--color-orange-950", "oklch(0.266 0.079 36.259)"),
                decl("--color-amber-50", "oklch(0.987 0.022 95.277)")
            )
        )
    }, {
        "(ws) it should trim the name",
        """
        :root {
          \t\f\r\n --color-orange-900: oklch(0.408 0.123 38.172);
        }
        """,
        List.of(
            block(":root",
                decl("--color-orange-900", "oklch(0.408 0.123 38.172)")
            )
        )
    }, {
        "(ws) it should trim the name",
        """
        :root {
          --color-orange-900\t\f\r\n : oklch(0.408 0.123 38.172);
        }
        """,
        List.of(
            block(":root",
                decl("--color-orange-900", "oklch(0.408 0.123 38.172)")
            )
        )
    }, {
        "(ws) it should trim the value",

        """
        :root {
          --color-orange-900:
             oklch(0.408 0.123 38.172);
        }
        """,
        List.of(
            block(":root",
                decl("--color-orange-900", "oklch(0.408 0.123 38.172)")
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
    }};
  }

  @Test(dataProvider = "parseValidProvider")
  public void parseValid(
      String description,
      String src,
      @SuppressWarnings("exports") List<CssEngine.Top> expected) {
    final CssEngine.CssParser parser;
    parser = new CssEngine.CssParser();

    parser.set(src);

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
      parser = new CssEngine.CssParser();

      parser.set(src);

      parser.parse();

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      final String actual;
      actual = expected.getMessage();

      assertTrue(actual.startsWith(message));
    }
  }

}