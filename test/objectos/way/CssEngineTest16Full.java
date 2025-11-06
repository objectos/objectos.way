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

import java.util.function.Consumer;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SuppressWarnings("unused")
public class CssEngineTest16Full {

  private static final String DARK = "@media (prefers-color-scheme: dark)";

  @DataProvider
  public Object[][] generateProvider() {
    return new Object[][] {{
        "empty: scan classes",
        cfg(e -> {
          class Subject {}

          e.systemBase("");
          e.scanClass(Subject.class);
        }),
        ""
    }, {
        "base: scan classes",
        cfg(e -> {
          class Subject {}

          e.systemBase("""
          html {
            line-height: 1.5;
          }
          """);
          e.scanClass(Subject.class);
        }),
        """
        @layer base {
          html {
            line-height: 1.5;
          }
        }
        """
    }, {
        "utilities: scan classes",
        cfg(e -> {
          class Subject extends CssSubject {
            @Override
            final void classes() { css("margin:0"); }
          }

          e.systemBase("");
          e.scanClass(Subject.class);
        }),
        """
        @layer utilities {
          .margin\\:0 { margin: 0 }
        }
        """
    }, {
        "utilities: <ratio>",
        cfg(e -> {
          class Subject extends CssSubject {
            @Override
            final void classes() { css("aspect-ratio:2/3"); }
          }

          e.systemBase("");
          e.scanClass(Subject.class);
        }),
        """
        @layer utilities {
          .aspect-ratio\\:2\\/3 { aspect-ratio: 2 / 3 }
        }
        """
    }, {
        "theme: do not generate",
        cfg(e -> {
          class Subject extends CssSubject {
            @Override
            final void classes() { css("margin:0"); }
          }

          e.systemBase("");
          e.systemTheme("""
          :root {
            --color-red-50: oklch(97.1% 0.013 17.38);
          }
          """);
          e.scanClass(Subject.class);
        }),
        """
        @layer utilities {
          .margin\\:0 { margin: 0 }
        }
        """
    }, {
        "theme: generate referenced props",
        cfg(e -> {
          class Subject extends CssSubject {
            @Override
            final void classes() { css("color:var(--color-red-50) margin:0"); }
          }

          e.systemBase("");
          e.systemTheme("""
          :root {
            --color-red-50: oklch(97.1% 0.013 17.38);
          }
          """);
          e.scanClass(Subject.class);
        }),
        """
        @layer theme {
          :root {
            --color-red-50: oklch(97.1% 0.013 17.38);
          }
        }
        @layer utilities {
          .color\\:var\\(--color-red-50\\) { color: var(--color-red-50) }
          .margin\\:0 { margin: 0 }
        }
        """
    }, {
        "base: between layer and utilities",
        cfg(e -> {
          class Subject extends CssSubject {
            @Override
            final void classes() { css("color:var(--color-red-50) margin:0"); }
          }

          e.systemBase("""
          html {
            line-height: 1.5;
          }
          """);
          e.systemTheme("""
          :root {
            --color-red-50: oklch(97.1% 0.013 17.38);
          }
          """);
          e.scanClass(Subject.class);
        }),
        """
        @layer theme {
          :root {
            --color-red-50: oklch(97.1% 0.013 17.38);
          }
        }
        @layer base {
          html {
            line-height: 1.5;
          }
        }
        @layer utilities {
          .color\\:var\\(--color-red-50\\) { color: var(--color-red-50) }
          .margin\\:0 { margin: 0 }
        }
        """
    }, {
        "theme: user-provided w/ @media",
        cfg(e -> {
          class Subject extends CssSubject {
            @Override
            final void classes() { css("color:var(--color-primary)"); }
          }

          e.systemBase("");
          e.systemTheme("");
          e.theme("""
          :root {
            --color-primary: #f0f0f0;
          }
          :root { @media (prefers-color-scheme: dark) {
            --color-primary: #1e1e1e;
          }}
          """);
          e.scanClass(Subject.class);
        }),
        """
        @layer theme {
          :root {
            --color-primary: #f0f0f0;
          }
          :root {
            @media (prefers-color-scheme: dark) {
              --color-primary: #1e1e1e;
            }
          }
        }
        @layer utilities {
          .color\\:var\\(--color-primary\\) { color: var(--color-primary) }
        }
        """
    }, {
        "theme: @keyframes",
        cfg(e -> {
          class Subject extends CssSubject {
            @Override
            final void classes() { css("animation-name:fade-in"); }
          }

          e.systemBase("");
          e.systemTheme("");
          e.theme("""
          :root {
            --color-primary: #f0f0f0;
          }
          @keyframes fade-in {
            0% { opacity: 0; }
            100% { opacity: 1; }
          }
          """);
          e.scanClass(Subject.class);
        }),
        """
        @layer utilities {
          .animation-name\\:fade-in { animation-name: fade-in }
        }
        @keyframes fade-in {
          0% {
            opacity: 0;
          }
          100% {
            opacity: 1;
          }
        }
        """
    }, {
        "components",
        cfg(e -> {
          e.systemBase("");
          e.systemTheme("""
          :root {
            --color-theme: #f0f0f0;
          }
          """);
          e.components("""
          [data-theme=g90] {
            --color-background: var(--color-theme);
          }
          """);
        }),
        """
        @layer theme {
          :root {
            --color-theme: #f0f0f0;
          }
        }
        @layer components {
          [data-theme=g90] {
            --color-background: var(--color-theme);
          }
        }
        """
    }, {
        "theme: @font-face",
        cfg(e -> {
          e.systemBase("");
          e.systemTheme("");
          e.theme("""
          @font-face {
            font-family: "IBM Plex Sans";
            font-style: normal;
            font-weight: 700;
            src: local("IBM Plex Sans Bold");
          }
          """);
        }),
        """
        @font-face {
          font-family: "IBM Plex Sans";
          font-style: normal;
          font-weight: 700;
          src: local("IBM Plex Sans Bold");
        }
        """
    }, {
        "utilities: rx unit",
        cfg(e -> {
          class Subject extends CssSubject {
            @Override
            final void classes() { css("gap:16rx"); }
          }

          e.systemBase("");
          e.systemTheme("");
          e.scanClass(Subject.class);
        }),
        """
        @layer utilities {
          .gap\\:16rx { gap: calc(16 / 16 * 1rem) }
        }
        """
    }, {
        "theme: emit prop referenced in value",
        cfg(e -> {
          class Subject extends CssSubject {
            @Override
            final void classes() { css("gap:var(--custom-gap)"); }
          }

          e.systemBase("");
          e.systemTheme("");
          e.theme("""
          :root {
            --custom-gap: 1rem;
          }
          """);
          e.scanClass(Subject.class);
        }),
        """
        @layer theme {
          :root {
            --custom-gap: 1rem;
          }
        }
        @layer utilities {
          .gap\\:var\\(--custom-gap\\) { gap: var(--custom-gap) }
        }
        """
    }, {
        "theme: emit prop reference in base",
        cfg(e -> {
          e.systemBase("""
          html, :host {
            font-family: --theme(
              --default-font-family,
              ui-sans-serif
            ); /* 4 */
          }
          """);
          e.systemTheme("""
          :root {
            --color-ignore-me: #f0f0f0;
            --font-sans: sans;
            --default-font-family: var(--font-sans);
          }
          """);
        }),
        """
        @layer theme {
          :root {
            --font-sans: sans;
            --default-font-family: var(--font-sans);
          }
        }
        @layer base {
          html, :host {
            font-family: var(--default-font-family, ui-sans-serif);
          }
        }
        """
    }, {
        "scanClasses convenience",
        cfg(e -> {
          class Subj1 extends CssSubject {
            @Override
            final void classes() { css("margin:0"); }
          }
          class Subj2 extends CssSubject {
            @Override
            final void classes() { css("padding:0"); }
          }

          e.systemBase("");
          e.systemTheme("");
          e.scanClasses(Subj1.class, Subj2.class);
        }),
        """
        @layer utilities {
          .margin\\:0 { margin: 0 }
          .padding\\:0 { padding: 0 }
        }
        """
    }, {
        "cssPropertyNames",
        cfg(e -> {
          class Subject extends CssSubject {
            @Override
            final void classes() { css("foo:0"); }
          }

          e.cssPropertyNames("foo");
          e.systemBase("");
          e.systemTheme("");
          e.scanClasses(Subject.class);
        }),
        """
        @layer utilities {
          .foo\\:0 { foo: 0 }
        }
        """
    }, {
        "variants + sorting",
        cfg(e -> {
          class Subject extends CssSubject {
            @Override
            final void classes() {
              css("sm/width:0 x2/width:0 xl/width:0 lg/width:0 md/width:0 width:0");
            }
          }

          e.systemBase("");
          e.systemTheme("");
          e.scanClasses(Subject.class);
        }),
        """
        @layer utilities {
          .width\\:0 { width: 0 }
          .sm\\/width\\:0 { @media (min-width: 40rem) { width: 0 } }
          .md\\/width\\:0 { @media (min-width: 48rem) { width: 0 } }
          .lg\\/width\\:0 { @media (min-width: 64rem) { width: 0 } }
          .xl\\/width\\:0 { @media (min-width: 80rem) { width: 0 } }
          .x2\\/width\\:0 { @media (min-width: 96rem) { width: 0 } }
        }
        """
    }, {
        "variants + group",
        cfg(e -> {
          class Subject extends CssSubject {
            @Override
            final void classes() {
              css("group-hover/text-decoration:underline");
            }
          }

          e.systemBase("");
          e.systemTheme("");
          e.scanClasses(Subject.class);
        }),
        """
        @layer utilities {
          .group-hover\\/text-decoration\\:underline { &:is(:where(.group):hover *) { @media (hover: hover) { text-decoration: underline } } }
        }
        """
    }};
  }

  @Test(dataProvider = "generateProvider")
  public void generate(
      String description,
      Consumer<Css.StyleSheet.Options> module,
      String expected) {
    final Css.StyleSheet sheet;
    sheet = Css.StyleSheet.create(o -> {
      o.noteSink(Y.noteSink());

      module.accept(o);
    });

    final String result;
    result = sheet.generate();

    assertEquals(result, expected);
  }

  private Consumer<Css.StyleSheet.Options> cfg(Consumer<Css.StyleSheet.Options> module) {
    return module;
  }

}