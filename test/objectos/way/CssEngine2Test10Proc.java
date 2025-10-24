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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import objectos.way.CssEngine2.Variant;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CssEngine2Test10Proc {

  private static final CssEngine2.Variant DARK = CssEngine2.simple("@media (prefers-color-scheme: dark)");
  private static final CssEngine2.Variant MD = CssEngine2.simple("@media (min-width: 48rem)");
  private static final CssEngine2.Variant HOVER = CssEngine2.simple("&:hover");

  @DataProvider
  public Object[][] testProvider() {
    return new Object[][] {{
        "single utility",
        Map.of(),
        l(
            r("margin:0", l("0", "margin"))
        ),
        l(
            CssEngine2.utility(List.of(), ".margin\\:0", "margin", "0")
        )
    }, {
        "multiple utilities",
        Map.of(),
        l(
            r("margin:0", l("0", "margin")),
            r("padding:0", l("0", "padding")),
            r("border:0", l("0", "border"))
        ),
        l(
            CssEngine2.utility(List.of(), ".margin\\:0", "margin", "0"),
            CssEngine2.utility(List.of(), ".padding\\:0", "padding", "0"),
            CssEngine2.utility(List.of(), ".border\\:0", "border", "0")
        )
    }, {
        "multiple utilities (skip already seen)",
        Map.of(),
        l(
            r("margin:0", l("0", "margin")),
            r("padding:0", l("0", "padding")),
            r("margin:0", l("0", "margin"))
        ),
        l(
            CssEngine2.utility(List.of(), ".margin\\:0", "margin", "0"),
            CssEngine2.utility(List.of(), ".padding\\:0", "padding", "0")
        )
    }, {
        "variant (class name)",
        Map.of("hover", HOVER),
        l(
            r("hover/margin:0", l("0", "margin", "hover")),
            r("padding:0", l("0", "padding"))
        ),
        l(
            CssEngine2.utility(List.of(HOVER), ".hover\\/margin\\:0", "margin", "0"),
            CssEngine2.utility(List.of(), ".padding\\:0", "padding", "0")
        )
    }, {
        "variant (at rule (1))",
        Map.of("dark", DARK),
        l(
            r("dark/color:gray-100", l("gray-100", "color", "dark")),
            r("padding:0", l("0", "padding"))
        ),
        l(
            CssEngine2.utility(List.of(DARK), ".dark\\/color\\:gray-100", "color", "gray-100"),
            CssEngine2.utility(List.of(), ".padding\\:0", "padding", "0")
        )
    }, {
        "variant (at rule (2))",
        Map.of("dark", DARK, "md", MD),
        l(
            r("dark/md/color:gray-100", l("gray-100", "color", "md", "dark")),
            r("padding:0", l("0", "padding"))
        ),
        l(
            CssEngine2.utility(List.of(DARK, MD), ".dark\\/md\\/color\\:gray-100", "color", "gray-100"),
            CssEngine2.utility(List.of(), ".padding\\:0", "padding", "0")
        )
    }, {
        "variant (custom))",
        Map.of(),
        l(
            r("&[data-foo]/padding:0", l("0", "padding", "&[data-foo]"))
        ),
        l(
            CssEngine2.utility(List.of(CssEngine2.simple("&[data-foo]")), ".\\&\\[data-foo\\]\\/padding\\:0", "padding", "0")
        )
    }};
  }

  @Test(dataProvider = "testProvider")
  public void test(
      String description,
      @SuppressWarnings("exports") Map<String, CssEngine2.Variant> variants,
      @SuppressWarnings("exports") List<Input> inputs,
      @SuppressWarnings("exports") List<CssEngine2.Utility> expected) {
    final Note.Sink noteSink;
    noteSink = Y.noteSink();

    final Map<String, Variant> mutable;
    mutable = new HashMap<>(variants);

    final CssEngine2.Proc proc;
    proc = new CssEngine2.Proc(noteSink, mutable);

    for (Input input : inputs) {
      proc.consume(input.className, input.slugs);
    }

    assertEquals(proc.utilities, expected);
  }

  @SafeVarargs
  private <T> List<T> l(T... values) {
    final List<T> l;
    l = new ArrayList<>();

    for (T v : values) {
      l.add(v);
    }

    return l;
  }

  private record Input(String className, List<String> slugs) {}

  private Input r(String className, List<String> slugs) {
    return new Input(className, slugs);
  }

}