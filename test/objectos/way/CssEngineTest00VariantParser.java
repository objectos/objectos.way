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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CssEngineTest00VariantParser {

  @DataProvider
  public Object[][] parseValidProvider() {
    return new Object[][] {{
        "pseudo class: one",
        """
        active { &:active { {} } }
        """,
        List.of(
            CssEngine.variant(0, "active", "&:active { ", " }")
        )
    }, {
        "pseudo class: two",
        """
        active { &:active { {} } }
        checked { &:checked { {} } }
        """,
        List.of(
            CssEngine.variant(0, "active", "&:active { ", " }"),
            CssEngine.variant(1, "checked", "&:checked { ", " }")
        )
    }, {
        "nested",
        """
        hover { @media (hover: hover) { &:hover { {} } } }
        """,
        List.of(
            CssEngine.variant(0, "hover", "@media (hover: hover) { &:hover { ", " } }")
        )
    }};
  }

  @Test
  public void debug() {
    parseValid(
        "pseudo class: two",
        """
        active { &:active { {} } }
        checked { &:checked { {} } }
        """,
        List.of(
            CssEngine.variant(0, "active", "&:active { ", " }"),
            CssEngine.variant(1, "checked", "&:checked { ", " }")
        )
    );
  }

  @Test(dataProvider = "parseValidProvider")
  public void parseValid(
      String description,
      String input,
      @SuppressWarnings("exports") List<CssEngine.Variant> expected) {
    final CssEngine.VariantParser parser;
    parser = new CssEngine.VariantParser();

    parser.set(input);

    final List<CssEngine.Variant> result;
    result = parser.parse(0);

    assertEquals(result, expected);
  }

  @DataProvider
  public Object[][] parseOneValidProvider() {
    return new Object[][] {{
        "attr variant",
        "&[data-foo]",
        CssEngine.variant(0, "&[data-foo]", "&[data-foo] { ", " }")
    }, {
        "@media variant (add ws)",
        "@media(prefers-color-scheme:dark)",
        CssEngine.variant(0, "@media(prefers-color-scheme:dark)", "@media (prefers-color-scheme: dark) { ", " }")
    }, {
        "pseudo-class variant",
        "&:active",
        CssEngine.variant(0, "&:active", "&:active { ", " }")
    }};
  }

  @Test(dataProvider = "parseOneValidProvider")
  public void parseOneValid(
      String description,
      String input,
      @SuppressWarnings("exports") CssEngine.Variant expected) {
    final CssEngine.VariantParser parser;
    parser = new CssEngine.VariantParser();

    parser.set(input);

    final CssEngine.Variant result;
    result = parser.parseOne(0);

    assertEquals(result, expected);
  }

}