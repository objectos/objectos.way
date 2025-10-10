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
import java.util.Set;
import java.util.function.Consumer;
import org.testng.annotations.Test;

public class CssEngine2Test08Gen {

  @Test
  public void testCase01() {
    test(
        gen -> {
          gen.utility(List.of(), "margin:0", "margin", "0");
        },

        ctx -> {
          final CssEngine2.Rules rules = ctx.rules;
          assertEquals(ctx.keywords, Set.of());
          assertEquals(rules.rules, List.of(
              CssEngine2.rule(".margin\\:0", "margin", "0")
          ));
        }
    );
  }

  @Test
  public void testCase02() {
    test(
        gen -> {
          gen.keyword("gray-100", "var(--color-gray-100)");
          gen.utility(List.of(), "color:gray-100", "color", "gray-100");
        },

        ctx -> {
          final CssEngine2.Rules rules = ctx.rules;
          assertEquals(ctx.keywords, Set.of(
              "gray-100"
          ));
          assertEquals(rules.rules, List.of(
              CssEngine2.rule(".color\\:gray-100", "color", "var(--color-gray-100)")
          ));
        }
    );
  }

  @Test
  public void testCase03() {
    test(
        gen -> {
          gen.keyword("gray-100", "var(--color-gray-100)");
          gen.utility(List.of(), "color:gray-100/20", "color", "gray-100/20");
        },

        ctx -> {
          final CssEngine2.Rules rules = ctx.rules;
          assertEquals(ctx.keywords, Set.of(
              "gray-100"
          ));
          assertEquals(rules.rules, List.of(
              CssEngine2.rule(".color\\:gray-100\\/20", "color", "color-mix(in oklab, var(--color-gray-100) 20%, transparent)")
          ));
        }
    );
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