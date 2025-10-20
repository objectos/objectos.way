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
import java.util.function.Consumer;
import java.util.stream.Collectors;
import objectos.way.CssEngine2.Ctx;
import objectos.way.CssEngine2.Keyframes;
import objectos.way.CssEngine2.PDecl;
import org.testng.annotations.Test;

public class CssEngine2Test09Gen {

  @Test
  public void testCase01() {
    test(
        gen -> {
          gen.utility(List.of(), "margin:0", "margin", "0");
        },

        ctx -> {
          assertEquals(ctx.keyframes(), List.of());
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
          gen.keywords(List.of(), Map.of("gray-100", "var(--color-gray-100)"));
          gen.utility(List.of(), "color:gray-100", "color", "gray-100");
        },

        ctx -> {
          assertEquals(ctx.keyframes(), List.of());
          assertEquals(ctx.rules(), List.of(
              CssEngine2.rule(".color\\:gray-100", List.of(), "color", "var(--color-gray-100)")
          ));
          final List<CssEngine2.Section> sections = ctx.sections();
          assertEquals(sections.size(), 1);
          final CssEngine2.Section s0 = sections.get(0);
          assertEquals(s0.selector(), List.of());
          assertEquals(v(s0.decls()), """
          gray-100: var(--color-gray-100)
          """);
        }
    );
  }

  @Test
  public void testCase03() {
    test(
        gen -> {
          gen.keywords(List.of(), Map.of("gray-100", "var(--color-gray-100)"));
          gen.utility(List.of(), "color:gray-100/20", "color", "gray-100/20");
        },

        ctx -> {
          assertEquals(ctx.keyframes(), List.of());
          assertEquals(ctx.rules(), List.of(
              CssEngine2.rule(".color\\:gray-100\\/20", List.of(), "color", "color-mix(in oklab, var(--color-gray-100) 20%, transparent)")
          ));
          final List<CssEngine2.Section> sections = ctx.sections();
          assertEquals(sections.size(), 1);
          final CssEngine2.Section s0 = sections.get(0);
          assertEquals(s0.selector(), List.of());
          assertEquals(v(s0.decls()), """
          gray-100: var(--color-gray-100)
          """);
        }
    );
  }

  @Test
  public void testCase04() {
    test(
        gen -> {
          gen.keyframes("fade-in");
          gen.utility(List.of(), "animation:3s_linear_1s_fade-in", "animation", "3s linear 1s fade-in");
        },

        ctx -> {
          assertEquals(ctx.keyframes(), List.of(
              CssEngine2.keyframes("fade-in", List.of())
          ));
          assertEquals(ctx.rules(), List.of(
              CssEngine2.rule(".animation\\:3s_linear_1s_fade-in", List.of(), "animation", "3s linear 1s fade-in")
          ));
          final List<CssEngine2.Section> sections = ctx.sections();
          assertEquals(sections.size(), 0);
        }
    );
  }

  private static final class Builder {

    final Map<String, CssEngine2.Keyframes> keyframes = new HashMap<>();

    final Map<String, CssEngine2.PDecl> keywords = new HashMap<>();

    final List<CssEngine2.PSection> sections = new ArrayList<>();

    final List<CssEngine2.Utility> utilities = new ArrayList<>();

    final Ctx build() {
      final CssEngine2.Gen gen;
      gen = new CssEngine2.Gen(keyframes, keywords, sections, utilities);

      return gen.generate();
    }

    final void keyframes(String name) {
      final Keyframes kf;
      kf = CssEngine2.keyframes(name, List.of());

      keyframes.put(name, kf);
    }

    final void keywords(List<String> selector, Map<String, String> kws) {
      final List<PDecl> decls;
      decls = new ArrayList<>();

      for (Map.Entry<String, String> entry : kws.entrySet()) {
        final String keyword;
        keyword = entry.getKey();

        final String value;
        value = entry.getValue();

        final PDecl decl;
        decl = CssEngine2.pdecl(keyword, value);

        keywords.put(keyword, decl);

        decls.add(decl);
      }

      final CssEngine2.PSection s;
      s = CssEngine2.psection(selector, decls);

      sections.add(s);
    }

    final void utility(
        List<CssEngine2.Variant> variants, String className, String property, String value) {
      final CssEngine2.Utility utility;
      utility = new CssEngine2.Utility(variants, className, property, value);

      utilities.add(utility);
    }

  }

  private String v(List<CssEngine2.Decl> values) {
    return values.stream()
        .map(v -> v.property() + ": " + v.value())
        .collect(Collectors.joining("\n", "", "\n"));
  }

  private void test(Consumer<? super Builder> config, Consumer<? super CssEngine2.Ctx> test) {
    final Builder builder;
    builder = new Builder();

    config.accept(builder);

    final CssEngine2.Ctx ctx;
    ctx = builder.build();

    test.accept(ctx);
  }

}