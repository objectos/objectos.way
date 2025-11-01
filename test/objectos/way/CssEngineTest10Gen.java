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
import org.testng.annotations.Test;

public class CssEngineTest10Gen {

  @Test(description = "no prop")
  public void testCase01() {
    test(
        gen -> {
          gen.utility(List.of(), ".margin\\:0", "margin", "0");
        },

        ctx -> {
          assertEquals(ctx.keyframes(), List.of());
          assertEquals(ctx.sections(), List.of());
          assertEquals(ctx.utilities(), List.of(
              CssEngine.utility(List.of(), ".margin\\:0", "margin", "0")
          ));
        }
    );
  }

  @Test(description = "prop")
  public void testCase02() {
    var gray100 = CssEngine.decl("--color-gray-100", "#f0f0f0");
    test(
        gen -> {
          gen.properties(List.of(":root"), gray100);
          gen.utility(List.of(), ".foo", "color", "var(--color-gray-100)");
        },

        ctx -> {
          assertEquals(ctx.keyframes(), List.of());
          final List<CssEngine.Section> sections = ctx.sections();
          assertEquals(sections.size(), 1);
          final CssEngine.Section s0 = sections.get(0);
          assertEquals(s0.selector(), List.of(":root"));
          assertEquals(s0.decls(), List.of(gray100));
          assertEquals(ctx.utilities(), List.of(
              CssEngine.utility(List.of(), ".foo", "color", "var(--color-gray-100)")
          ));
        }
    );
  }

  @Test(description = "prop not referenced")
  public void testCase03() {
    var gray100 = CssEngine.decl("--color-gray-100", "#f0f0f0");
    test(
        gen -> {
          gen.properties(List.of(":root"), gray100);
          gen.utility(List.of(), ".foo", "color", "#f0f0f0");
        },

        ctx -> {
          assertEquals(ctx.keyframes(), List.of());
          assertEquals(ctx.sections(), List.of());
          assertEquals(ctx.utilities(), List.of(
              CssEngine.utility(List.of(), ".foo", "color", "#f0f0f0")
          ));
        }
    );
  }

  @Test(description = "keyframes")
  public void testCase04() {
    test(
        gen -> {
          gen.keyframes("fade-in");
          gen.utility(List.of(), ".foo", "animation", "3s linear 1s fade-in");
        },

        ctx -> {
          assertEquals(ctx.keyframes(), List.of(
              CssEngine.keyframes("fade-in")
          ));
          final List<CssEngine.Section> sections = ctx.sections();
          assertEquals(sections.size(), 0);
          assertEquals(ctx.utilities(), List.of(
              CssEngine.utility(List.of(), ".foo", "animation", "3s linear 1s fade-in")
          ));
        }
    );
  }

  @Test(description = "keyframes: not referenced")
  public void testCase05() {
    test(
        gen -> {
          gen.keyframes("fade-in");
          gen.utility(List.of(), ".foo", "animation", "3s linear 1s fade-out");
        },

        ctx -> {
          assertEquals(ctx.keyframes(), List.of());
          final List<CssEngine.Section> sections = ctx.sections();
          assertEquals(sections.size(), 0);
          assertEquals(ctx.utilities(), List.of(
              CssEngine.utility(List.of(), ".foo", "animation", "3s linear 1s fade-out")
          ));
        }
    );
  }

  private static final class Builder {

    final Map<String, CssEngine.Keyframes> keyframes = new HashMap<>();

    final CssEngine.Properties properties = new CssEngine.Properties();

    final List<CssEngine.Section> sections = new ArrayList<>();

    final List<CssEngine.Utility> utilities = new ArrayList<>();

    final CssEngine.Ctx build() {
      final CssEngine.Gen gen;
      gen = new CssEngine.Gen(keyframes, properties, sections);

      return gen.generate(utilities);
    }

    final void keyframes(String name) {
      final CssEngine.Keyframes kf;
      kf = CssEngine.keyframes(name);

      keyframes.put(name, kf);
    }

    final void properties(
        List<String> selector,
        CssEngine.Decl... props) {
      final List<CssEngine.Decl> decls;
      decls = List.of(props);

      for (CssEngine.Decl decl : decls) {
        properties.merge(decl.property, decl);
      }

      final CssEngine.Section s;
      s = CssEngine.section(selector, decls);

      sections.add(s);
    }

    final void utility(
        List<CssEngine.Variant> variants, String className, String property, String value) {
      final CssEngine.Utility utility;
      utility = CssEngine.utility(variants, className, property, value);

      utilities.add(utility);
    }

  }

  private void test(Consumer<? super Builder> config, Consumer<? super CssEngine.Ctx> test) {
    final Builder builder;
    builder = new Builder();

    config.accept(builder);

    final CssEngine.Ctx ctx;
    ctx = builder.build();

    test.accept(ctx);
  }

}