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
        s -> {
          s.base = "";
          s.theme = "";
          s.variants = Map.of();
        },

        c -> {
          c.theme("""
          --breakpoint-sm: 40rem;
          """);
        },

        c -> {
          final CssEngine2.Decl v;
          v = CssEngine2.decl("--breakpoint-sm", "40rem");

          assertEquals(c.components(), List.of());
          assertEquals(c.fontFaces(), List.of());
          assertEquals(c.keyframes(), Map.of());
          assertEquals(c.keywords(), Map.of("screen-sm", v));
          assertEquals(c.rx(), false);
          assertEquals(c.sections(), List.of(
              CssEngine2.section(List.of(), List.of(v))
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
          --breakpoint-sm: 40rem;
          """;
          s.variants = Map.of();
        },

        c -> {
          c.theme("""
          --breakpoint-sm: 30rem;
          """);
        },

        c -> {
          final CssEngine2.Decl v;
          v = CssEngine2.decl("--breakpoint-sm", "30rem");

          assertEquals(c.components(), List.of());
          assertEquals(c.fontFaces(), List.of());
          assertEquals(c.keyframes(), Map.of());
          assertEquals(c.keywords(), Map.of("screen-sm", v));
          assertEquals(c.rx(), false);
          assertEquals(c.sections(), List.of(
              CssEngine2.section(List.of(), List.of(v))
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
        s -> {
          s.base = "";
          s.theme = "";
          s.variants = Map.of();
        },

        c -> {
          c.theme("""
          --color-test: #cafeba;
          """);
        },

        c -> {
          final CssEngine2.Decl v;
          v = CssEngine2.decl("--color-test", "#cafeba");

          assertEquals(c.components(), List.of());
          assertEquals(c.fontFaces(), List.of());
          assertEquals(c.keyframes(), Map.of());
          assertEquals(c.keywords(), Map.of("test", v));
          assertEquals(c.rx(), false);
          assertEquals(c.sections(), List.of(
              CssEngine2.section(List.of(), List.of(v))
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
        s -> {
          s.base = "";
          s.theme = "";
          s.variants = Map.of();
        },

        c -> {
          c.theme("""
          --font-test: 'Comic Sans';
          """);
        },

        c -> {
          final CssEngine2.Decl v;
          v = CssEngine2.decl("--font-test", "'Comic Sans'");

          assertEquals(c.components(), List.of());
          assertEquals(c.fontFaces(), List.of());
          assertEquals(c.keyframes(), Map.of());
          assertEquals(c.keywords(), Map.of("test", v));
          assertEquals(c.rx(), false);
          assertEquals(c.sections(), List.of(
              CssEngine2.section(List.of(), List.of(v))
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
          c.keyframes("fade-in", frames -> {
            frames.from("""
            opacity: 0;
            """);
            frames.to("""
            opacity: 1;
            """);
          });
        },

        c -> {
          assertEquals(c.components(), List.of());
          assertEquals(c.fontFaces(), List.of());
          assertEquals(c.keyframes(), Map.of("fade-in", CssEngine2.keyframes("fade-in", List.of(
              CssEngine2.parsedRule("from", List.of(
                  CssEngine2.decl("opacity", "0")
              )),
              CssEngine2.parsedRule("to", List.of(
                  CssEngine2.decl("opacity", "1")
              ))
          ))));
          assertEquals(c.keywords(), Map.of());
          assertEquals(c.rx(), false);
          assertEquals(c.sections(), List.of(
              CssEngine2.section(List.of(), List.of())
          ));
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
          --color-primary: #f0f0f0;
          """);
          c.theme(DARK, """
          --color-primary: #1e1e1e;
          """);
        },

        c -> {
          final CssEngine2.Decl v0;
          v0 = CssEngine2.decl("--color-primary", "#f0f0f0");

          final CssEngine2.Decl v1;
          v1 = CssEngine2.decl("--color-primary", "#1e1e1e");

          assertEquals(c.components(), List.of());
          assertEquals(c.fontFaces(), List.of());
          assertEquals(c.keyframes(), Map.of());
          assertEquals(c.keywords(), Map.of("primary", v0));
          assertEquals(c.rx(), false);
          assertEquals(c.sections(), List.of(
              CssEngine2.section(List.of(), List.of(v0)),
              CssEngine2.section(List.of(DARK), List.of(v1))
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
        s -> {
          s.base = "";
          s.theme = """
          --rx: 16;
          """;
          s.variants = Map.of();
        },

        c -> {},

        c -> {
          final CssEngine2.Decl v;
          v = CssEngine2.decl("--rx", "16");

          assertEquals(c.components(), List.of());
          assertEquals(c.fontFaces(), List.of());
          assertEquals(c.keyframes(), Map.of());
          assertEquals(c.keywords(), Map.of());
          assertEquals(c.rx(), true);
          assertEquals(c.sections(), List.of(
              CssEngine2.section(List.of(), List.of(v))
          ));
          assertEquals(c.variants(), Map.of());
        }
    );
  }

  @Test(description = """
  rx
  - it should allow rx units
  """)
  public void rx02() {
    test(
        s -> {
          s.base = "";
          s.theme = "";
          s.variants = Map.of();
        },

        c -> {
          c.theme("""
          --rx: 16;
          """);
        },

        c -> {
          final CssEngine2.Decl v;
          v = CssEngine2.decl("--rx", "16");

          assertEquals(c.components(), List.of());
          assertEquals(c.fontFaces(), List.of());
          assertEquals(c.keyframes(), Map.of());
          assertEquals(c.keywords(), Map.of());
          assertEquals(c.rx(), true);
          assertEquals(c.sections(), List.of(
              CssEngine2.section(List.of(), List.of(v))
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
                  CssEngine2.decl("--color-background", "#262626")
              ))
          ));
          assertEquals(c.fontFaces(), List.of());
          assertEquals(c.keyframes(), Map.of());
          assertEquals(c.keywords(), Map.of());
          assertEquals(c.rx(), false);
          assertEquals(c.sections(), List.of(
              CssEngine2.section(List.of(), List.of())
          ));
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
          c.fontFace("""
          font-family: "IBM Plex Sans";
          font-style: normal;
          font-weight: 700;
          src: local("IBM Plex Sans Bold");
          """);
        },

        c -> {
          assertEquals(c.components(), List.of());
          assertEquals(c.fontFaces(), List.of(
              List.of(
                  CssEngine2.decl("font-family", "\"IBM Plex Sans\""),
                  CssEngine2.decl("font-style", "normal"),
                  CssEngine2.decl("font-weight", "700"),
                  CssEngine2.decl("src", "local(\"IBM Plex Sans Bold\")")
              )
          ));
          assertEquals(c.keyframes(), Map.of());
          assertEquals(c.keywords(), Map.of());
          assertEquals(c.rx(), false);
          assertEquals(c.sections(), List.of(
              CssEngine2.section(List.of(), List.of())
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