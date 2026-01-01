/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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

import static objectos.way.CssEngine.utility;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import objectos.way.CssEngine.Variant;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CssEngineTest09Proc {

  private static final CssEngine.Variant HOVER = CssEngine.variant(
      0, "hover", "&:hover { ", " }"
  );
  private static final CssEngine.Variant DARK = CssEngine.variant(
      1, "dark", "@media (prefers-color-scheme: dark) { ", " }"
  );
  private static final CssEngine.Variant MD = CssEngine.variant(
      2, "md", "@media (min-width: 48rem) { ", " }"
  );

  @DataProvider
  public Object[][] testProvider() {
    return new Object[][] {{
        "single utility",
        Map.of(),
        l(
            r("margin:0", l("0", "margin"))
        ),
        l(
            utility(List.of(), ".margin\\:0", "margin", "0")
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
            utility(List.of(), ".margin\\:0", "margin", "0"),
            utility(List.of(), ".padding\\:0", "padding", "0"),
            utility(List.of(), ".border\\:0", "border", "0")
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
            utility(List.of(), ".margin\\:0", "margin", "0"),
            utility(List.of(), ".padding\\:0", "padding", "0")
        )
    }, {
        "variant (class name)",
        Map.of("hover", HOVER),
        l(
            r("hover/margin:0", l("0", "margin", "hover")),
            r("padding:0", l("0", "padding"))
        ),
        l(
            utility(List.of(HOVER), ".hover\\/margin\\:0", "margin", "0"),
            utility(List.of(), ".padding\\:0", "padding", "0")
        )
    }, {
        "variant (at rule (1))",
        Map.of("dark", DARK),
        l(
            r("dark/color:gray-100", l("gray-100", "color", "dark")),
            r("padding:0", l("0", "padding"))
        ),
        l(
            utility(List.of(DARK), ".dark\\/color\\:gray-100", "color", "gray-100"),
            utility(List.of(), ".padding\\:0", "padding", "0")
        )
    }, {
        "variant (at rule (2))",
        Map.of("dark", DARK, "md", MD),
        l(
            r("dark/md/color:gray-100", l("gray-100", "color", "md", "dark")),
            r("padding:0", l("0", "padding"))
        ),
        l(
            utility(List.of(DARK, MD), ".dark\\/md\\/color\\:gray-100", "color", "gray-100"),
            utility(List.of(), ".padding\\:0", "padding", "0")
        )
    }, {
        "variant (custom))",
        Map.of(),
        l(
            r("&[data-foo]/padding:0", l("0", "padding", "&[data-foo]"))
        ),
        l(
            utility(
                List.of(CssEngine.variant(0, "&[data-foo]", "&[data-foo] { ", " }")),
                ".\\&\\[data-foo\\]\\/padding\\:0", "padding", "0"
            )
        )
    }, {
        "skip unknown property",
        Map.of(),
        l(
            r("foo:bar", l("bar", "foo"))
        ),
        l()
    }, {
        "allow custom properties",
        Map.of(),
        l(
            r("--foo:1234", l("1234", "--foo"))
        ),
        l(
            utility(List.of(), ".--foo\\:1234", "--foo", "1234")
        )
    }, {
        "rx length",
        Map.of(),
        l(
            r("margin:16rx", l("16rx", "margin")),
            r("margin:-16rx", l("-16rx", "margin"))
        ),
        l(
            utility(List.of(), ".margin\\:16rx", "margin", "calc(16 / 16 * 1rem)"),
            utility(List.of(), ".margin\\:-16rx", "margin", "calc(-16 / 16 * 1rem)")
        )
    }};
  }

  @Test(dataProvider = "testProvider")
  public void test(
      String description,
      @SuppressWarnings("exports") Map<String, CssEngine.Variant> variants,
      @SuppressWarnings("exports") List<Input> inputs,
      @SuppressWarnings("exports") List<CssEngine.Utility> expected) {
    final Set<String> cssProperties;
    cssProperties = CssProps.get();

    final Note.Sink noteSink;
    noteSink = Y.noteSink();

    final Map<String, Variant> mutable;
    mutable = new HashMap<>(variants);

    final CssEngine.Proc proc;
    proc = new CssEngine.Proc(cssProperties, noteSink, mutable);

    for (Input input : inputs) {
      proc.consume(input.className, input.slugs);
    }

    assertEquals(proc.result(), expected);
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