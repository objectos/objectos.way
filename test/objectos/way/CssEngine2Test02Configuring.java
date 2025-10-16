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
import java.util.Map;
import java.util.function.Consumer;
import org.testng.annotations.Test;

public class CssEngine2Test02Configuring {

  @Test(description = """
  breakpoint
  - it should create a keyword
  - it should create a variant
  """)
  public void breakpoint01() {
    test(
        c -> {
          c.systemTheme = "";
          c.systemVariants = Map.of();
          c.theme("""
          --breakpoint-sm: 40rem;
          """);
        },

        c -> {
          final CssEngine2.PDecl v;
          v = CssEngine2.pdecl("--breakpoint-sm", "40rem");

          assertEquals(c.keywords(), Map.of("screen-sm", v));
          assertEquals(c.rx(), false);
          assertEquals(c.sections(), List.of(
              CssEngine2.psection(List.of(), List.of(v))
          ));
          assertEquals(c.variants(), Map.of(
              "sm", CssEngine2.simple("@media (min-width: 40rem)")
          ));
        }
    );
  }

  @Test(description = """
  breakpoint
  - it should override system value
  """)
  public void breakpoint02() {
    test(
        c -> {
          c.systemTheme = """
          --breakpoint-sm: 40rem;
          """;
          c.systemVariants = Map.of();
          c.theme("""
          --breakpoint-sm: 30rem;
          """);
        },

        c -> {
          final CssEngine2.PDecl v;
          v = CssEngine2.pdecl("--breakpoint-sm", "30rem");

          assertEquals(c.keywords(), Map.of("screen-sm", v));
          assertEquals(c.rx(), false);
          assertEquals(c.sections(), List.of(
              CssEngine2.psection(List.of(), List.of(v))
          ));
          assertEquals(c.variants(), Map.of(
              "sm", CssEngine2.simple("@media (min-width: 30rem)")
          ));
        }
    );
  }

  @Test(description = """
  color
  - it should create a keyword
  """)
  public void colors01() {
    test(
        c -> {
          c.systemTheme = "";
          c.systemVariants = Map.of();
          c.theme("""
          --color-test: #cafeba;
          """);
        },

        c -> {
          final CssEngine2.PDecl v;
          v = CssEngine2.pdecl("--color-test", "#cafeba");

          assertEquals(c.keywords(), Map.of("test", v));
          assertEquals(c.rx(), false);
          assertEquals(c.sections(), List.of(
              CssEngine2.psection(List.of(), List.of(v))
          ));
          assertEquals(c.variants(), Map.of());
        }
    );
  }

  @Test(description = """
  font
  - it should create a keyword
  """)
  public void font01() {
    test(
        c -> {
          c.systemTheme = "";
          c.systemVariants = Map.of();
          c.theme("""
          --font-test: 'Comic Sans';
          """);
        },

        c -> {
          final CssEngine2.PDecl v;
          v = CssEngine2.pdecl("--font-test", "'Comic Sans'");

          assertEquals(c.keywords(), Map.of("test", v));
          assertEquals(c.rx(), false);
          assertEquals(c.sections(), List.of(
              CssEngine2.psection(List.of(), List.of(v))
          ));
          assertEquals(c.variants(), Map.of());
        }
    );
  }

  @Test(description = """
  rx
  - it should allow rx units
  """)
  public void rx01() {
    test(
        c -> {
          c.systemTheme = "";
          c.systemVariants = Map.of();
          c.theme("""
          --rx: 16;
          """);
        },

        c -> {
          final CssEngine2.PDecl v;
          v = CssEngine2.pdecl("--rx", "16");

          assertEquals(c.keywords(), Map.of());
          assertEquals(c.rx(), true);
          assertEquals(c.sections(), List.of(
              CssEngine2.psection(List.of(), List.of(v))
          ));
          assertEquals(c.variants(), Map.of());
        }
    );
  }

  private void test(
      Consumer<? super CssEngine2.Configuring> flags,
      Consumer<? super CssEngine2.Config> test) {
    final CssEngine2.Configuring pojo;
    pojo = new CssEngine2.Configuring();

    pojo.noteSink(Y.noteSink());

    flags.accept(pojo);

    final CssEngine2.Config config;
    config = pojo.configure();

    test.accept(config);
  }

}