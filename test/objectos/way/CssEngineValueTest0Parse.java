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
import org.testng.annotations.Test;

public class CssEngineValueTest0Parse {

  @Test(description = "colors :: just one")
  public void colors01() {
    test(
        """
        --color-stone-950: oklch(0.147 0.004 49.25);
        """,

        List.of(
            CssEngineValue.themeVar("color", "stone-950", "oklch(0.147 0.004 49.25)")
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
            CssEngineValue.themeVar("color", "stone-950", "oklch(0.147 0.004 49.25)"),
            CssEngineValue.themeVar("color", "red-50", "oklch(0.971 0.013 17.38)")
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

  @Test(description = "ws :: blank line between lines")
  public void ws01() {
    test(
        """
        --color-orange-900: oklch(0.408 0.123 38.172);
        --color-orange-950: oklch(0.266 0.079 36.259);

        --color-amber-50: oklch(0.987 0.022 95.277);
        """,

        List.of(
            CssEngineValue.themeVar("color", "orange-900", "oklch(0.408 0.123 38.172)"),
            CssEngineValue.themeVar("color", "orange-950", "oklch(0.266 0.079 36.259)"),
            CssEngineValue.themeVar("color", "amber-50", "oklch(0.987 0.022 95.277)")
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
            CssEngineValue.themeVar("color", "orange-900", "oklch(0.408 0.123 38.172)")
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
            CssEngineValue.themeVar("color", "orange-900", "oklch(0.408 0.123 38.172)")
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
            CssEngineValue.themeVar("color", "orange-900", "oklch(0.408 0.123 38.172)")
        )
    );
  }

  private void test(String value, List<CssEngineValue> expected) {
    final List<CssEngineValue> result;
    result = CssEngineValue.parse(value);

    assertEquals(result, expected);
  }

}