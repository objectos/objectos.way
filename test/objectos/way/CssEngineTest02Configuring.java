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

import static objectos.way.CssEngine.block;
import static objectos.way.CssEngine.keyframes;
import static objectos.way.CssEngine.section;
import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.testng.annotations.Test;

public class CssEngineTest02Configuring {

  @Test(description = """
  breakpoint
  - it should create a keyword
  """)
  public void breakpoint01() {
    test(
        c -> {
          c.systemBase("");
          c.systemTheme("");
          c.systemVariants("");
          c.theme("""
          :root {
            --breakpoint-sm: 40rem;
          }
          """);
        },

        c -> {
          final CssEngine.Decl v;
          v = CssEngine.decl("--breakpoint-sm", "40rem");

          assertEquals(c.components(), List.of());
          assertEquals(c.fontFaces(), List.of());
          assertEquals(c.keyframes(), Map.of());
          assertEquals(c.propertiesMap(), Map.of("--breakpoint-sm", v));
          assertEquals(c.sections(), List.of(
              section(List.of(":root"), v)
          ));
          assertEquals(c.variants(), Map.of());
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
          c.systemBase("");
          c.systemTheme("""
          :root {
            --breakpoint-sm: 40rem;
          }
          """);
          c.systemVariants("");
          c.theme("""
          :root {
            --breakpoint-sm: 30rem;
          }
          """);
        },

        c -> {
          final CssEngine.Decl v0;
          v0 = CssEngine.decl("--breakpoint-sm", "40rem").replaced();

          final CssEngine.Decl v1;
          v1 = CssEngine.decl("--breakpoint-sm", "30rem");

          assertEquals(c.components(), List.of());
          assertEquals(c.fontFaces(), List.of());
          assertEquals(c.keyframes(), Map.of());
          assertEquals(c.propertiesMap(), Map.of("--breakpoint-sm", v1));
          assertEquals(c.sections(), List.of(
              section(List.of(":root"), v0, v1)
          ));
          assertEquals(c.variants(), Map.of());
        }
    );
  }

  @Test(description = """
  color
  - it should create a property
  """)
  public void colors01() {
    test(
        c -> {
          c.systemBase("");
          c.systemTheme("");
          c.systemVariants("");
          c.theme("""
          :root {
          --color-test: #cafeba;
          }
          """);
        },

        c -> {
          final CssEngine.Decl v;
          v = CssEngine.decl("--color-test", "#cafeba");

          assertEquals(c.components(), List.of());
          assertEquals(c.fontFaces(), List.of());
          assertEquals(c.keyframes(), Map.of());
          assertEquals(c.propertiesMap(), Map.of("--color-test", v));
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
        c -> {
          c.systemBase("");
          c.systemTheme("");
          c.systemVariants("");
          c.theme("""
          :root {
          --font-test: 'Comic Sans';
          }
          """);
        },

        c -> {
          final CssEngine.Decl v;
          v = CssEngine.decl("--font-test", "'Comic Sans'");

          assertEquals(c.components(), List.of());
          assertEquals(c.fontFaces(), List.of());
          assertEquals(c.keyframes(), Map.of());
          assertEquals(c.propertiesMap(), Map.of("--font-test", v));
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
        c -> {
          c.systemBase("");
          c.systemTheme("");
          c.systemVariants("");
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
              CssEngine.block("from", CssEngine.decl("opacity", "0")),
              CssEngine.block("to", CssEngine.decl("opacity", "1"))
          )));
          assertEquals(c.propertiesMap(), Map.of());
          assertEquals(c.sections(), List.of());
          assertEquals(c.variants(), Map.of());
        }
    );
  }

  private static final String DARK = "@media (prefers-color-scheme: dark)";

  @Test
  public void media01() {
    test(
        c -> {
          c.systemBase("");
          c.systemTheme("");
          c.systemVariants("");
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
          final CssEngine.Decl v0;
          v0 = CssEngine.decl("--color-primary", "#f0f0f0");

          final CssEngine.Decl v1;
          v1 = CssEngine.decl("--color-primary", "#1e1e1e");

          assertEquals(c.components(), List.of());
          assertEquals(c.fontFaces(), List.of());
          assertEquals(c.keyframes(), Map.of());
          assertEquals(c.propertiesMap(), Map.of("--color-primary", v0));
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
        c -> {
          c.systemBase("");
          c.systemTheme("""
          :root {
            --color-theme: #f0f0f0;
          }
          """);
          c.systemVariants("");
          c.components("""
          [data-theme=g90] {
            --color-background: var(--color-theme);
          }
          """);
        },

        c -> {
          final CssEngine.Decl v0;
          v0 = CssEngine.decl("--color-theme", "#f0f0f0").mark();

          final CssEngine.Decl v1;
          v1 = CssEngine.decl("--color-background", "var(--color-theme)").mark();

          assertEquals(c.components(), List.of(
              block("[data-theme=g90]", v1)
          ));
          assertEquals(c.fontFaces(), List.of());
          assertEquals(c.keyframes(), Map.of());
          assertEquals(c.propertiesMap(), Map.of(
              "--color-theme", v0
          ));
          assertEquals(c.sections(), List.of(
              section(List.of(":root"), v0)
          ));
          assertEquals(c.variants(), Map.of());
        }
    );
  }

  @Test
  public void fontFace01() {
    test(
        c -> {
          c.systemBase("");
          c.systemTheme("");
          c.systemVariants("");
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
              CssEngine.fontFace(
                  CssEngine.decl("font-family", "\"IBM Plex Sans\""),
                  CssEngine.decl("font-style", "normal"),
                  CssEngine.decl("font-weight", "700"),
                  CssEngine.decl("src", "local(\"IBM Plex Sans Bold\")")
              )
          ));
          assertEquals(c.keyframes(), Map.of());
          assertEquals(c.propertiesMap(), Map.of());
          assertEquals(c.sections(), List.of());
          assertEquals(c.variants(), Map.of());
        }
    );
  }

  @Test
  public void baseProps01() {
    test(
        c -> {
          c.systemBase("""
          html, :host {
            font-family: --theme(
              --default-font-family,
              ui-sans-serif
            ); /* 4 */
          }
          """);
          c.systemTheme("""
          :root {
          --font-sans: sans;
          --default-font-family: var(--font-sans);
          }
          """);
          c.systemVariants("");
        },

        c -> {
          final CssEngine.Decl v0;
          v0 = CssEngine.decl("--font-sans", "sans").mark();

          final CssEngine.Decl v1;
          v1 = CssEngine.decl("--default-font-family", "var(--font-sans)").mark();

          assertEquals(c.components(), List.of());
          assertEquals(c.fontFaces(), List.of());
          assertEquals(c.keyframes(), Map.of());
          assertEquals(c.propertiesMap(), Map.of("--font-sans", v0, "--default-font-family", v1));
          assertEquals(c.sections(), List.of(
              CssEngine.section(List.of(":root"), v0, v1)
          ));
          assertEquals(c.variants(), Map.of());
        }
    );
  }

  @Test
  public void themeProps01() {
    test(
        c -> {
          c.systemBase("");
          c.systemTheme("""
          :root {
          --color-text-primary: black;
          }
          """);
          c.systemVariants("");
          c.theme("""
          :root {
          color: var(--color-text-primary);
          }
          """);
        },

        c -> {
          final CssEngine.Decl v0;
          v0 = CssEngine.decl("--color-text-primary", "black").mark();

          final CssEngine.Decl v1;
          v1 = CssEngine.decl("color", "var(--color-text-primary)").mark();

          assertEquals(c.components(), List.of());
          assertEquals(c.fontFaces(), List.of());
          assertEquals(c.keyframes(), Map.of());
          assertEquals(c.propertiesMap(), Map.of("--color-text-primary", v0));
          assertEquals(c.sections(), List.of(
              CssEngine.section(List.of(":root"), v0, v1)
          ));
          assertEquals(c.variants(), Map.of());
        }
    );
  }

  @Test
  public void variants01() {
    test(
        c -> {
          c.systemBase("");
          c.systemTheme("");
          c.systemVariants("");
          c.variants("""
          hover { &:hover { {} } }
          """);
        },

        c -> {
          assertEquals(c.components(), List.of());
          assertEquals(c.fontFaces(), List.of());
          assertEquals(c.keyframes(), Map.of());
          assertEquals(c.propertiesMap(), Map.of());
          assertEquals(c.sections(), List.of());
          assertEquals(c.variants(), Map.of(
              "hover", CssEngine.variant(0, "hover", "&:hover { ", " }")
          ));
        }
    );
  }

  @Test(description = """
  variants:
  - it should replace system value
  """)
  public void variants02() {
    test(
        c -> {
          c.systemBase("");
          c.systemTheme("");
          c.systemVariants("""
          hover { @media (hover: hover) { &:hover { {} } } }
          """);
          c.variants("""
          hover { &:hover { {} } }
          """);
        },

        c -> {
          assertEquals(c.components(), List.of());
          assertEquals(c.fontFaces(), List.of());
          assertEquals(c.keyframes(), Map.of());
          assertEquals(c.propertiesMap(), Map.of());
          assertEquals(c.sections(), List.of());
          assertEquals(c.variants(), Map.of(
              "hover", CssEngine.variant(0, "hover", "&:hover { ", " }")
          ));
        }
    );
  }

  private void test(
      Consumer<? super CssEngine.Configuring> flags,
      Consumer<? super CssEngine.Config> test) {
    final CssEngine.Configuring pojo;
    pojo = new CssEngine.Configuring();

    pojo.noteSink(Y.noteSink());

    flags.accept(pojo);

    final CssEngine.Config config;
    config = pojo.configure();

    test.accept(config);
  }

}