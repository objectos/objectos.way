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

import java.util.ArrayList;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CssEngine2Test00ValueParser {

  @Test(description = "breakpoint :: just one")
  public void breakpoint01() {
    test(
        """
        --breakpoint-sm: 40rem;
        """,

        List.of(
            CssEngine2.themeProp("breakpoint", "sm", "40rem")
        )
    );
  }

  @Test(description = "colors :: just one")
  public void colors01() {
    test(
        """
        --color-stone-950: oklch(0.147 0.004 49.25);
        """,

        List.of(
            CssEngine2.themeProp("color", "stone-950", "oklch(0.147 0.004 49.25)")
        )
    );
  }

  @Test(description = "colors :: two lines")
  public void colors02() {
    test(
        """
        --color-stone-950: oklch(0.147 0.004 49.25);
        --color-red-50: oklch(0.971 0.013 17.38);
        """,

        List.of(
            CssEngine2.themeProp("color", "stone-950", "oklch(0.147 0.004 49.25)"),
            CssEngine2.themeProp("color", "red-50", "oklch(0.971 0.013 17.38)")
        )
    );
  }

  @Test(enabled = false, description = "colors :: clear")
  public void colors03() {
    test(
        """
        --color-*: initial;
        """,

        List.of(
          // TODO which value?
        )
    );
  }

  @Test(description = "custom :: allow for values without a namespace")
  public void custom01() {
    test(
        """
        --carbon-grid-columns: 4;
        """,

        List.of(
            CssEngine2.customProp("--carbon-grid-columns", "4")
        )
    );
  }

  @Test(description = "EOF at declaration value")
  public void errors01() {
    testIAE(
        """
        --color-orange-900: oklch(0.408 0.123 38.172);
        --color-orange-950:
        """,

        "Unexpected EOF while parsing a declaration value"
    );
  }

  @Test(description = "EOF at declaration value")
  public void errors02() {
    testIAE(
        """
        --color-orange-900: oklch(0.408 0.123 38.172);
        --color-orange-950: okl""",

        "Unexpected EOF while parsing a declaration value"
    );
  }

  @Test(description = "EOF at declaration name")
  public void errors03() {
    testIAE(
        """
        --color-orange-900: oklch(0.408 0.123 38.172);
        --color-orange""",

        "Unexpected EOF while parsing a custom property name"
    );
  }

  @Test(description = "EOF before colon")
  public void errors04() {
    testIAE(
        """
        --color-orange-900: oklch(0.408 0.123 38.172);
        --color-orange-950\040""",

        "Declaration with no ':' colon character"
    );
  }

  @Test(description = "font :: just one")
  public void font01() {
    test(
        """
        --font-display: Foo, "Foo bar";
        """,

        List.of(
            CssEngine2.themeProp("font", "display", "Foo, \"Foo bar\"")
        )
    );
  }

  @Test
  public void rx01() {
    test(
        """
        --rx: 16;
        """,

        List.of(
            CssEngine2.themeProp("rx", "", "16")
        )
    );
  }

  @Test
  public void skip01() {
    test(
        """
        --*: initial;
        """,

        List.of(
            CssEngine2.systemSkip("*")
        )
    );
  }

  @Test(description = "ws :: blank line between lines")
  public void ws01() {
    test(
        """
        --color-orange-900: oklch(0.408 0.123 38.172);
        --color-orange-950: oklch(0.266 0.079 36.259);

        --color-amber-50: oklch(0.987 0.022 95.277);
        """,

        List.of(
            CssEngine2.themeProp("color", "orange-900", "oklch(0.408 0.123 38.172)"),
            CssEngine2.themeProp("color", "orange-950", "oklch(0.266 0.079 36.259)"),
            CssEngine2.themeProp("color", "amber-50", "oklch(0.987 0.022 95.277)")
        )
    );
  }

  @Test(description = "ws :: it should trim the name")
  public void ws02() {
    test(
        """
        \t\f\r\n --color-orange-900: oklch(0.408 0.123 38.172);
        """,

        List.of(
            CssEngine2.themeProp("color", "orange-900", "oklch(0.408 0.123 38.172)")
        )
    );
  }

  @Test(description = "ws :: it should trim the name")
  public void ws03() {
    test(
        """
        --color-orange-900\t\f\r\n : oklch(0.408 0.123 38.172);
        """,

        List.of(
            CssEngine2.themeProp("color", "orange-900", "oklch(0.408 0.123 38.172)")
        )
    );
  }

  @Test(description = "ws :: it should trim the value")
  public void ws04() {
    test(
        """
        --color-orange-900:
           oklch(0.408 0.123        38.172)     ;
        """,

        List.of(
            CssEngine2.themeProp("color", "orange-900", "oklch(0.408 0.123 38.172)")
        )
    );
  }

  private void test(String value, List<CssEngine2.Syntax> expected) {
    final List<CssEngine2.Syntax> result;
    result = new ArrayList<>();

    final CssEngine2.SyntaxParser parser;
    parser = new CssEngine2.SyntaxParser(result, value);

    parser.parse();

    assertEquals(result, expected);
  }

  private void testIAE(String value, String expectedMsg) {
    try {
      final List<CssEngine2.Syntax> result;
      result = new ArrayList<>();

      final CssEngine2.SyntaxParser parser;
      parser = new CssEngine2.SyntaxParser(result, value);

      parser.parse();

      Assert.fail("it should have thrown");
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), expectedMsg);
    }
  }

}