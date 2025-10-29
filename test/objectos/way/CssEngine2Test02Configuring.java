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

import static objectos.way.CssEngine2.fun;
import static objectos.way.CssEngine2.keyframes;
import static objectos.way.CssEngine2.number;
import static objectos.way.CssEngine2.section;
import static objectos.way.CssEngine2.tok;
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
        s -> {
          s.base = "";
          s.theme = "";
          s.variants = Map.of();
        },

        c -> {
          c.theme("""
          :root {
            --breakpoint-sm: 40rem;
          }
          """);
        },

        c -> {
          final CssEngine2.Decl v;
          v = CssEngine2.decl("--breakpoint-sm", tok("40rem"));

          assertEquals(c.components(), List.of());
          assertEquals(c.fontFaces(), List.of());
          assertEquals(c.keyframes(), Map.of());
          assertEquals(c.properties(), Map.of("--breakpoint-sm", v));
          assertEquals(c.sections(), List.of(
              section(List.of(":root"), v)
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
        s -> {
          s.base = "";
          s.theme = """
          :root {
          --breakpoint-sm: 40rem;

          """;
          s.variants = Map.of();
        },

        c -> {
          c.theme("""
          :root {
            --breakpoint-sm: 30rem;
          }
          """);
        },

        c -> {
          final CssEngine2.Decl v0;
          v0 = CssEngine2.decl("--breakpoint-sm", tok("40rem")).replaced();

          final CssEngine2.Decl v1;
          v1 = CssEngine2.decl("--breakpoint-sm", tok("30rem"));

          assertEquals(c.components(), List.of());
          assertEquals(c.fontFaces(), List.of());
          assertEquals(c.keyframes(), Map.of());
          assertEquals(c.properties(), Map.of("--breakpoint-sm", v1));
          assertEquals(c.sections(), List.of(
              section(List.of(":root"), v0, v1)
          ));
          assertEquals(c.variants(), Map.of(
              "sm", CssEngine2.simple("@media (min-width: 30rem)")
          ));
        }
    );
  }

  @Test(description = """
  color
  - it should create a property
  """)
  public void colors01() {
    test(
        s -> {
          s.base = "";
          s.theme = "";
          s.variants = Map.of();
        },

        c -> {
          c.theme("""
          :root {
          --color-test: #cafeba;
          }
          """);
        },

        c -> {
          final CssEngine2.Decl v;
          v = CssEngine2.decl("--color-test", tok("#cafeba"));

          assertEquals(c.components(), List.of());
          assertEquals(c.fontFaces(), List.of());
          assertEquals(c.keyframes(), Map.of());
          assertEquals(c.properties(), Map.of("--color-test", v));
          assertEquals(c.sections(), List.of(
              section(List.of(":root"), v)
          ));
          assertEquals(c.variants(), Map.of());
        }
    );
  }

  @Test(description = """
  font
  - it should create a property
  """)
  public void font01() {
    test(
        s -> {
          s.base = "";
          s.theme = "";
          s.variants = Map.of();
        },

        c -> {
          c.theme("""
          :root {
          --font-test: 'Comic Sans';
          }
          """);
        },

        c -> {
          final CssEngine2.Decl v;
          v = CssEngine2.decl("--font-test", tok("'Comic Sans'"));

          assertEquals(c.components(), List.of());
          assertEquals(c.fontFaces(), List.of());
          assertEquals(c.keyframes(), Map.of());
          assertEquals(c.properties(), Map.of("--font-test", v));
          assertEquals(c.sections(), List.of(
              section(List.of(":root"), v)
          ));
          assertEquals(c.variants(), Map.of());
        }
    );
  }

  @Test
  public void keyframes01() {
    test(
        s -> {
          s.base = "";
          s.theme = "";
          s.variants = Map.of();
        },

        c -> {
          c.theme("""
          @keyframes fade-in {
            from { opacity: 0; }
            to { opacity: 1; }
          }
          """);
        },

        c -> {
          assertEquals(c.components(), List.of());
          assertEquals(c.fontFaces(), List.of());
          assertEquals(c.keyframes(), Map.of("fade-in", keyframes("fade-in",
              CssEngine2.block("from", CssEngine2.decl("opacity", number("0"))),
              CssEngine2.block("to", CssEngine2.decl("opacity", number("1")))
          )));
          assertEquals(c.properties(), Map.of());
          assertEquals(c.sections(), List.of());
          assertEquals(c.variants(), Map.of());
        }
    );
  }

  private static final String DARK = "@media (prefers-color-scheme: dark)";

  @Test
  public void media01() {
    test(
        s -> {
          s.base = "";
          s.theme = "";
          s.variants = Map.of();
        },

        c -> {
          c.theme("""
          :root {
            --color-primary: #f0f0f0;
          }
          :root { @media (prefers-color-scheme: dark) {
            --color-primary: #1e1e1e;
          }}
          """);
        },

        c -> {
          final CssEngine2.Decl v0;
          v0 = CssEngine2.decl("--color-primary", tok("#f0f0f0"));

          final CssEngine2.Decl v1;
          v1 = CssEngine2.decl("--color-primary", tok("#1e1e1e"));

          assertEquals(c.components(), List.of());
          assertEquals(c.fontFaces(), List.of());
          assertEquals(c.keyframes(), Map.of());
          assertEquals(c.properties(), Map.of("--color-primary", v0));
          assertEquals(c.sections(), List.of(
              section(List.of(":root"), v0),
              section(List.of(":root", DARK), v1)
          ));
          assertEquals(c.variants(), Map.of());
        }
    );
  }

  @Test
  public void component01() {
    test(
        s -> {
          s.base = "";
          s.theme = "";
          s.variants = Map.of();
        },

        c -> {
          c.component("[data-theme=g90]", """
          --color-background: #262626;
          """);
        },

        c -> {
          assertEquals(c.components(), List.of(
              CssEngine2.parsedRule("[data-theme=g90]", List.of(
                  CssEngine2.decl("--color-background", tok("#262626"))
              ))
          ));
          assertEquals(c.fontFaces(), List.of());
          assertEquals(c.keyframes(), Map.of());
          assertEquals(c.properties(), Map.of());
          assertEquals(c.sections(), List.of());
          assertEquals(c.variants(), Map.of());
        }
    );
  }

  @Test
  public void fontFace01() {
    test(
        s -> {
          s.base = "";
          s.theme = "";
          s.variants = Map.of();
        },

        c -> {
          c.theme("""
          @font-face {
            font-family: "IBM Plex Sans";
            font-style: normal;
            font-weight: 700;
            src: local("IBM Plex Sans Bold");
          }
          """);
        },

        c -> {
          assertEquals(c.components(), List.of());
          assertEquals(c.fontFaces(), List.of(
              CssEngine2.fontFace(
                  CssEngine2.decl("font-family", tok("\"IBM Plex Sans\"")),
                  CssEngine2.decl("font-style", tok("normal")),
                  CssEngine2.decl("font-weight", number("700")),
                  CssEngine2.decl("src", fun("local", tok("\"IBM Plex Sans Bold\"")))
              )
          ));
          assertEquals(c.keyframes(), Map.of());
          assertEquals(c.properties(), Map.of());
          assertEquals(c.sections(), List.of());
          assertEquals(c.variants(), Map.of());
        }
    );
  }

  @Test
  public void baseProps01() {
    test(
        s -> {
          s.base = """
          html, :host {
            font-family: --theme(
              --default-font-family,
              ui-sans-serif
            ); /* 4 */
          }
          """;
          s.theme = """
          :root {
          --font-sans: sans;
          --default-font-family: var(--font-sans);
          }
          """;
          s.variants = Map.of();
        },

        c -> {},

        c -> {
          final CssEngine2.Decl v0;
          v0 = CssEngine2.decl("--font-sans", tok("sans")).mark();

          final CssEngine2.Decl v1;
          v1 = CssEngine2.decl("--default-font-family", fun("var", tok("--font-sans"))).mark();

          assertEquals(c.components(), List.of());
          assertEquals(c.fontFaces(), List.of());
          assertEquals(c.keyframes(), Map.of());
          assertEquals(c.properties(), Map.of("--font-sans", v0, "--default-font-family", v1));
          assertEquals(c.sections(), List.of(
              CssEngine2.section(List.of(":root"), v0, v1)
          ));
          assertEquals(c.variants(), Map.of());
        }
    );
  }

  private void test(
      Consumer<? super CssEngine2.System> systemConfig,
      Consumer<? super CssEngine2.Configuring> flags,
      Consumer<? super CssEngine2.Config> test) {
    final CssEngine2.System system;
    system = new CssEngine2.System();

    systemConfig.accept(system);

    final CssEngine2.Configuring pojo;
    pojo = new CssEngine2.Configuring(system);

    pojo.noteSink(Y.noteSink());

    flags.accept(pojo);

    final CssEngine2.Config config;
    config = pojo.configure();

    test.accept(config);
  }

}