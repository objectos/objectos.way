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

import static objectos.way.CssEngine.fun;
import static objectos.way.CssEngine.number;
import static objectos.way.CssEngine.tok;
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
          gen.utility(List.of(), ".margin\\:0", "margin", tok("0"));
        },

        ctx -> {
          assertEquals(ctx.keyframes(), List.of());
          assertEquals(ctx.rules(), List.of(
              CssEngine.rule(".margin\\:0", List.of(), "margin", "0")
          ));
          assertEquals(ctx.sections(), List.of());
        }
    );
  }

  @Test(description = "prop")
  public void testCase02() {
    var gray100 = CssEngine.decl("--color-gray-100", CssEngine.tok("#f0f0f0"));
    test(
        gen -> {
          gen.properties(List.of(":root"), gray100);
          gen.utility(List.of(), ".foo", "color", fun("var", tok("--color-gray-100")));
        },

        ctx -> {
          assertEquals(ctx.keyframes(), List.of());
          assertEquals(ctx.rules(), List.of(
              CssEngine.rule(".foo", List.of(), "color", "var(--color-gray-100)")
          ));
          final List<CssEngine.Section> sections = ctx.sections();
          assertEquals(sections.size(), 1);
          final CssEngine.Section s0 = sections.get(0);
          assertEquals(s0.selector(), List.of(":root"));
          assertEquals(s0.decls(), List.of(gray100));
        }
    );
  }

  @Test(description = "prop not referenced")
  public void testCase03() {
    var gray100 = CssEngine.decl("--color-gray-100", CssEngine.tok("#f0f0f0"));
    test(
        gen -> {
          gen.properties(List.of(":root"), gray100);
          gen.utility(List.of(), ".foo", "color", tok("#f0f0f0"));
        },

        ctx -> {
          assertEquals(ctx.keyframes(), List.of());
          assertEquals(ctx.rules(), List.of(
              CssEngine.rule(".foo", List.of(), "color", "#f0f0f0")
          ));
          assertEquals(ctx.sections(), List.of());
        }
    );
  }

  @Test(description = "keyframes")
  public void testCase04() {
    test(
        gen -> {
          gen.keyframes("fade-in");
          gen.utility(List.of(), ".foo", "animation", tok("3s"), tok("linear"), tok("1s"), tok("fade-in"));
        },

        ctx -> {
          assertEquals(ctx.keyframes(), List.of(
              CssEngine.keyframes("fade-in")
          ));
          assertEquals(ctx.rules(), List.of(
              CssEngine.rule(".foo", List.of(), "animation", "3s linear 1s fade-in")
          ));
          final List<CssEngine.Section> sections = ctx.sections();
          assertEquals(sections.size(), 0);
        }
    );
  }

  @Test(description = "keyframes: not referenced")
  public void testCase05() {
    test(
        gen -> {
          gen.keyframes("fade-in");
          gen.utility(List.of(), ".foo", "animation", tok("3s"), tok("linear"), tok("1s"), tok("fade-out"));
        },

        ctx -> {
          assertEquals(ctx.keyframes(), List.of());
          assertEquals(ctx.rules(), List.of(
              CssEngine.rule(".foo", List.of(), "animation", "3s linear 1s fade-out")
          ));
          final List<CssEngine.Section> sections = ctx.sections();
          assertEquals(sections.size(), 0);
        }
    );
  }

  @Test(description = "--rx() function")
  public void testCase06() {
    test(
        gen -> {
          gen.utility(List.of(), ".gap\\:--rx\\(16\\)", "gap", fun("--rx", number("16")));
        },

        ctx -> {
          assertEquals(ctx.keyframes(), List.of());
          assertEquals(ctx.rules(), List.of(
              CssEngine.rule(".gap\\:--rx\\(16\\)", List.of(), "gap", "calc(16 / 16 * 1rem)")
          ));
          assertEquals(ctx.sections(), List.of());
        }
    );
  }

  private static final class Builder {

    final Map<String, CssEngine.Keyframes> keyframes = new HashMap<>();

    final Map<String, CssEngine.Decl> properties = new HashMap<>();

    final List<CssEngine.Section> sections = new ArrayList<>();

    final List<CssEngine.Utility> utilities = new ArrayList<>();

    final CssEngine.Ctx build() {
      final CssEngine.Gen gen;
      gen = new CssEngine.Gen(keyframes, properties, sections, utilities);

      return gen.generate();
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
        properties.put(decl.property, decl);
      }

      final CssEngine.Section s;
      s = CssEngine.section(selector, decls);

      sections.add(s);
    }

    final void utility(
        List<CssEngine.Variant> variants, String className, String property, CssEngine.Value... values) {
      final CssEngine.Utility utility;
      utility = CssEngine.utility(variants, className, property, values);

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