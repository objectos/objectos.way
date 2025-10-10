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
import java.util.Map;
import org.testng.annotations.Test;

public class CssEngine2Test07Utilities {

  private static final Map.Entry<String, CssEngine2.MediaQuery> DARK = Map.entry("dark", CssEngine2.mediaQuery(1, "@media (prefers-color-scheme: dark)"));
  private static final Map.Entry<String, CssEngine2.MediaQuery> MD = Map.entry("md", CssEngine2.mediaQuery(2, "@media (min-width: 48rem)"));
  private static final Map.Entry<String, CssEngine2.Modifier> HOVER = Map.entry("hover", CssEngine2.suffix(":hover"));

  @Test
  public void testCase01() {
    test(
        Map.of(),

        List.of(
            list("margin", "0")
        ),

        CssEngine2.utility(List.of(), "margin:0", "margin", "0")
    );
  }

  @Test
  public void testCase02() {
    test(
        Map.of(),

        List.of(
            list("margin", "0"),
            list("padding", "0"),
            list("margin", "0")
        ),

        CssEngine2.utility(List.of(), "margin:0", "margin", "0"),
        CssEngine2.utility(List.of(), "padding:0", "padding", "0")
    );
  }

  @Test
  public void testCase03() {
    test(
        Map.of(),

        List.of(
            list("hover", "margin", "0"),
            list("padding", "0")
        ),

        CssEngine2.utility(List.of(), "padding:0", "padding", "0")
    );
  }

  @Test(description = "system variant")
  public void testCase04() {
    test(
        Map.ofEntries(HOVER),

        List.of(
            list("hover", "margin", "0"),
            list("padding", "0")
        ),

        CssEngine2.utility(List.of(HOVER.getValue()), "hover:margin:0", "margin", "0"),
        CssEngine2.utility(List.of(), "padding:0", "padding", "0")
    );
  }

  @Test(description = "media query (1)")
  public void testCase05() {
    test(
        Map.ofEntries(DARK),

        List.of(
            list("dark", "color", "gray-100"),
            list("padding", "0")
        ),

        CssEngine2.utility(List.of(DARK.getValue()), "dark:color:gray-100", "color", "gray-100"),
        CssEngine2.utility(List.of(), "padding:0", "padding", "0")
    );
  }

  @Test(description = "media query (2)")
  public void testCase06() {
    test(
        Map.ofEntries(DARK, MD),

        List.of(
            list("dark", "md", "color", "gray-100"),
            list("padding", "0")
        ),

        CssEngine2.utility(List.of(DARK.getValue(), MD.getValue()), "dark:md:color:gray-100", "color", "gray-100"),
        CssEngine2.utility(List.of(), "padding:0", "padding", "0")
    );
  }

  private List<String> list(String... values) {
    final List<String> l;
    l = new ArrayList<>();

    for (String v : values) {
      l.add(v);
    }

    return l;
  }

  private void test(
      Map<String, CssEngine2.Variant> variants,
      List<List<String>> input,
      CssEngine2.Utility... expected) {
    final Note.Sink noteSink;
    noteSink = Y.noteSink();

    final CssEngine2.Utilities proc;
    proc = new CssEngine2.Utilities(noteSink, variants);

    for (List<String> list : input) {
      proc.consume(list);
    }

    assertEquals(proc.utilities, List.of(expected));
  }

}