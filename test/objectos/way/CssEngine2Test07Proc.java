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

public class CssEngine2Test07Proc {

  private static final Map.Entry<String, CssEngine2.Modifier> HOVER = Map.entry("hover", CssEngine2.suffix(":hover"));

  @Test
  public void testCase01() {
    test(
        Map.of(),

        List.of(
            list("margin", "0")
        ),

        Map.of(
            CssEngine2.ROOT, Map.of(
                CssEngine2.ROOT, List.of(
                    CssEngine2.utility(List.of(), "margin:0", "margin", "0")
                )
            )
        )
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

        Map.of(
            CssEngine2.ROOT, Map.of(
                CssEngine2.ROOT, List.of(
                    CssEngine2.utility(List.of(), "margin:0", "margin", "0"),
                    CssEngine2.utility(List.of(), "padding:0", "padding", "0")
                )
            )
        )
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

        Map.of(
            CssEngine2.ROOT, Map.of(
                CssEngine2.ROOT, List.of(
                    CssEngine2.utility(List.of(), "padding:0", "padding", "0")
                )
            )
        )
    );
  }

  @Test
  public void testCase04() {
    test(
        Map.ofEntries(HOVER),

        List.of(
            list("hover", "margin", "0"),
            list("padding", "0")
        ),

        Map.of(
            CssEngine2.ROOT, Map.of(
                CssEngine2.ROOT, List.of(
                    CssEngine2.utility(List.of(HOVER.getValue()), "hover:margin:0", "margin", "0"),
                    CssEngine2.utility(List.of(), "padding:0", "padding", "0")
                )
            )
        )
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
      Map<CssEngine2.MediaQuery, Map<CssEngine2.MediaQuery, List<CssEngine2.Utility>>> expected) {
    final Note.Sink noteSink;
    noteSink = Y.noteSink();

    final CssEngine2.Proc proc;
    proc = new CssEngine2.Proc(noteSink, variants);

    for (List<String> list : input) {
      proc.process(list);
    }

    assertEquals(proc.utilities, expected);
  }

}