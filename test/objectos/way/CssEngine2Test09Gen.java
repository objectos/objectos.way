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
import java.util.function.Consumer;
import java.util.stream.Collectors;
import objectos.way.CssEngine2.Value;
import org.testng.annotations.Test;

public class CssEngine2Test09Gen {

  @Test
  public void testCase01() {
    test(
        gen -> {
          gen.utility(List.of(), "margin:0", "margin", "0");
        },

        ctx -> {
          assertEquals(ctx.rules(), List.of(
              CssEngine2.rule(".margin\\:0", List.of(), "margin", "0")
          ));
          assertEquals(ctx.sections(), List.of());
        }
    );
  }

  @Test
  public void testCase02() {
    test(
        gen -> {
          gen.utility(List.of(), "color:gray-100", "color", "gray-100");
          gen.themeValue(CssEngine2.ROOT, 1, "color", "gray-100", "var(--color-gray-100)");
        },

        ctx -> {
          assertEquals(ctx.rules(), List.of(
              CssEngine2.rule(".color\\:gray-100", List.of(), "color", "var(--color-gray-100)")
          ));
          final List<CssEngine2.ThemeSection> sections = ctx.sections();
          assertEquals(sections.size(), 1);
          final CssEngine2.ThemeSection s0 = sections.get(0);
          assertEquals(s0.selector(), CssEngine2.ROOT);
          assertEquals(v(s0.values()), """
          --color-gray-100: var(--color-gray-100)
          """);
        }
    );
  }

  @Test
  public void testCase03() {
    test(
        gen -> {
          gen.utility(List.of(), "color:gray-100/20", "color", "gray-100/20");
          gen.themeValue(CssEngine2.ROOT, 1, "color", "gray-100", "var(--color-gray-100)");
        },

        ctx -> {
          assertEquals(ctx.rules(), List.of(
              CssEngine2.rule(".color\\:gray-100\\/20", List.of(), "color", "color-mix(in oklab, var(--color-gray-100) 20%, transparent)")
          ));
          final List<CssEngine2.ThemeSection> sections = ctx.sections();
          assertEquals(sections.size(), 1);
          final CssEngine2.ThemeSection s0 = sections.get(0);
          assertEquals(s0.selector(), CssEngine2.ROOT);
          assertEquals(v(s0.values()), """
          --color-gray-100: var(--color-gray-100)
          """);
        }
    );
  }

  private String v(List<Value> values) {
    return values.stream()
        .map(v -> v.name() + ": " + v.value())
        .collect(Collectors.joining("\n", "", "\n"));
  }

  private void test(Consumer<? super CssEngine2.Gen> config, Consumer<? super CssEngine2.Ctx> test) {
    final CssEngine2.Gen gen;
    gen = new CssEngine2.Gen();

    config.accept(gen);

    final CssEngine2.Ctx ctx;
    ctx = gen.generate();

    test.accept(ctx);
  }

}