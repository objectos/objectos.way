/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
import org.testng.Assert;
import org.testng.annotations.Test;

public class CssEngineTestParseTheme {

  @Test(description = "breakpoint :: just one")
  public void breakpoint01() {
    List<CssEngine.ThemeEntry> entries;
    entries = test("""
    --breakpoint-sm: 40rem;
    """);

    assertEquals(entries.size(), 1);

    CssEngine.ThemeEntry entry0;
    entry0 = entries.get(0);

    assertEquals(entry0.index(), 0);
    assertEquals(entry0.toString(), "--breakpoint-sm: 40rem;");
    assertEquals(entry0.id(), "sm");
  }

  @Test(description = "colors :: just one")
  public void colors01() {
    List<CssEngine.ThemeEntry> entries;
    entries = test("""
    --color-stone-950: oklch(0.147 0.004 49.25);
    """);

    assertEquals(entries.size(), 1);

    CssEngine.ThemeEntry entry0;
    entry0 = entries.get(0);

    assertEquals(entry0.index(), 0);
    assertEquals(entry0.toString(), "--color-stone-950: oklch(0.147 0.004 49.25);");
    assertEquals(entry0.id(), "stone-950");
  }

  @Test(description = "colors :: two lines")
  public void colors02() {
    List<CssEngine.ThemeEntry> entries;
    entries = test("""
    --color-stone-950: oklch(0.147 0.004 49.25);
    --color-red-50: oklch(0.971 0.013 17.38);
    """);

    assertEquals(entries.size(), 2);

    CssEngine.ThemeEntry entry0;
    entry0 = entries.get(0);

    assertEquals(entry0.index(), 0);
    assertEquals(entry0.toString(), "--color-stone-950: oklch(0.147 0.004 49.25);");
    assertEquals(entry0.id(), "stone-950");

    CssEngine.ThemeEntry entry1;
    entry1 = entries.get(1);

    assertEquals(entry1.index(), 1);
    assertEquals(entry1.toString(), "--color-red-50: oklch(0.971 0.013 17.38);");
    assertEquals(entry1.id(), "red-50");
  }

  @Test(description = "colors :: multiple w/ blank lines")
  public void colors03() {
    List<CssEngine.ThemeEntry> entries;
    entries = test("""
    --color-orange-900: oklch(0.408 0.123 38.172);
    --color-orange-950: oklch(0.266 0.079 36.259);

    --color-amber-50: oklch(0.987 0.022 95.277);
    """);

    assertEquals(entries.size(), 3);

    CssEngine.ThemeEntry entry0;
    entry0 = entries.get(0);

    assertEquals(entry0.index(), 0);
    assertEquals(entry0.toString(), "--color-orange-900: oklch(0.408 0.123 38.172);");
    assertEquals(entry0.id(), "orange-900");

    CssEngine.ThemeEntry entry1;
    entry1 = entries.get(1);

    assertEquals(entry1.index(), 1);
    assertEquals(entry1.toString(), "--color-orange-950: oklch(0.266 0.079 36.259);");
    assertEquals(entry1.id(), "orange-950");

    CssEngine.ThemeEntry entry2;
    entry2 = entries.get(2);

    assertEquals(entry2.index(), 2);
    assertEquals(entry2.toString(), "--color-amber-50: oklch(0.987 0.022 95.277);");
    assertEquals(entry2.id(), "amber-50");
  }

  @Test(description = "colors :: it should trim the value")
  public void colors04() {
    List<CssEngine.ThemeEntry> entries;
    entries = test("""
    --color-orange-900:
       oklch(0.408 0.123        38.172)     ;
    """);

    assertEquals(entries.size(), 1);

    CssEngine.ThemeEntry entry0;
    entry0 = entries.get(0);

    assertEquals(entry0.index(), 0);
    assertEquals(entry0.toString(), "--color-orange-900: oklch(0.408 0.123 38.172);");
    assertEquals(entry0.id(), "orange-900");
  }

  @Test(description = "colors :: clear")
  public void colors05() {
    List<CssEngine.ThemeEntry> entries;
    entries = test("""
    --color-*: initial;
    """);

    assertEquals(entries.size(), 0);
  }

  @Test(description = "custom :: allow for values without a namespace")
  public void custom01() {
    List<CssEngine.ThemeEntry> entries;
    entries = test("""
    --rx: 16px;
    """);

    assertEquals(entries.size(), 1);

    CssEngine.ThemeEntry entry0;
    entry0 = entries.get(0);

    assertEquals(entry0.index(), 0);
    assertEquals(entry0.toString(), "--rx: 16px;");
    assertEquals(entry0.id(), null);
  }

  @Test(description = "font :: just one")
  public void font01() {
    List<CssEngine.ThemeEntry> entries;
    entries = test("""
    --font-display: Foo, "Foo bar";
    """);

    assertEquals(entries.size(), 1);

    CssEngine.ThemeEntry entry0;
    entry0 = entries.get(0);

    assertEquals(entry0.index(), 0);
    assertEquals(entry0.toString(), "--font-display: Foo, \"Foo bar\";");
    assertEquals(entry0.id(), "display");
  }

  @Test(description = "Incomplete variable declaration at the end")
  public void errors01() {
    try {
      test("""
      --color-orange-900: oklch(0.408 0.123 38.172);
      --color-orange-950:
      """);

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "Unexpected end of theme");
    }
  }

  @Test(description = "Full generation indeed")
  public void fullGeneration01() {
    CssEngine engine;
    engine = new CssEngine();

    engine.skipLayer(Css.Layer.BASE);
    engine.skipLayer(Css.Layer.COMPONENTS);
    engine.skipLayer(Css.Layer.UTILITIES);

    engine.execute();

    assertEquals(
        engine.generate(),

        """
        @layer theme {
          :root {
            --font-sans: ui-sans-serif, system-ui, sans-serif, 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol', 'Noto Color Emoji';
            --font-serif: ui-serif, Georgia, Cambria, 'Times New Roman', Times, serif;
            --font-mono: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, 'Liberation Mono', 'Courier New', monospace;
            --color-red-50: oklch(0.971 0.013 17.38);
            --color-red-100: oklch(0.936 0.032 17.717);
            --color-red-200: oklch(0.885 0.062 18.334);
            --color-red-300: oklch(0.808 0.114 19.571);
            --color-red-400: oklch(0.704 0.191 22.216);
            --color-red-500: oklch(0.637 0.237 25.331);
            --color-red-600: oklch(0.577 0.245 27.325);
            --color-red-700: oklch(0.505 0.213 27.518);
            --color-red-800: oklch(0.444 0.177 26.899);
            --color-red-900: oklch(0.396 0.141 25.723);
            --color-red-950: oklch(0.258 0.092 26.042);
            --color-orange-50: oklch(0.98 0.016 73.684);
            --color-orange-100: oklch(0.954 0.038 75.164);
            --color-orange-200: oklch(0.901 0.076 70.697);
            --color-orange-300: oklch(0.837 0.128 66.29);
            --color-orange-400: oklch(0.75 0.183 55.934);
            --color-orange-500: oklch(0.705 0.213 47.604);
            --color-orange-600: oklch(0.646 0.222 41.116);
            --color-orange-700: oklch(0.553 0.195 38.402);
            --color-orange-800: oklch(0.47 0.157 37.304);
            --color-orange-900: oklch(0.408 0.123 38.172);
            --color-orange-950: oklch(0.266 0.079 36.259);
            --color-amber-50: oklch(0.987 0.022 95.277);
            --color-amber-100: oklch(0.962 0.059 95.617);
            --color-amber-200: oklch(0.924 0.12 95.746);
            --color-amber-300: oklch(0.879 0.169 91.605);
            --color-amber-400: oklch(0.828 0.189 84.429);
            --color-amber-500: oklch(0.769 0.188 70.08);
            --color-amber-600: oklch(0.666 0.179 58.318);
            --color-amber-700: oklch(0.555 0.163 48.998);
            --color-amber-800: oklch(0.473 0.137 46.201);
            --color-amber-900: oklch(0.414 0.112 45.904);
            --color-amber-950: oklch(0.279 0.077 45.635);
            --color-yellow-50: oklch(0.987 0.026 102.212);
            --color-yellow-100: oklch(0.973 0.071 103.193);
            --color-yellow-200: oklch(0.945 0.129 101.54);
            --color-yellow-300: oklch(0.905 0.182 98.111);
            --color-yellow-400: oklch(0.852 0.199 91.936);
            --color-yellow-500: oklch(0.795 0.184 86.047);
            --color-yellow-600: oklch(0.681 0.162 75.834);
            --color-yellow-700: oklch(0.554 0.135 66.442);
            --color-yellow-800: oklch(0.476 0.114 61.907);
            --color-yellow-900: oklch(0.421 0.095 57.708);
            --color-yellow-950: oklch(0.286 0.066 53.813);
            --color-lime-50: oklch(0.986 0.031 120.757);
            --color-lime-100: oklch(0.967 0.067 122.328);
            --color-lime-200: oklch(0.938 0.127 124.321);
            --color-lime-300: oklch(0.897 0.196 126.665);
            --color-lime-400: oklch(0.841 0.238 128.85);
            --color-lime-500: oklch(0.768 0.233 130.85);
            --color-lime-600: oklch(0.648 0.2 131.684);
            --color-lime-700: oklch(0.532 0.157 131.589);
            --color-lime-800: oklch(0.453 0.124 130.933);
            --color-lime-900: oklch(0.405 0.101 131.063);
            --color-lime-950: oklch(0.274 0.072 132.109);
            --color-green-50: oklch(0.982 0.018 155.826);
            --color-green-100: oklch(0.962 0.044 156.743);
            --color-green-200: oklch(0.925 0.084 155.995);
            --color-green-300: oklch(0.871 0.15 154.449);
            --color-green-400: oklch(0.792 0.209 151.711);
            --color-green-500: oklch(0.723 0.219 149.579);
            --color-green-600: oklch(0.627 0.194 149.214);
            --color-green-700: oklch(0.527 0.154 150.069);
            --color-green-800: oklch(0.448 0.119 151.328);
            --color-green-900: oklch(0.393 0.095 152.535);
            --color-green-950: oklch(0.266 0.065 152.934);
            --color-emerald-50: oklch(0.979 0.021 166.113);
            --color-emerald-100: oklch(0.95 0.052 163.051);
            --color-emerald-200: oklch(0.905 0.093 164.15);
            --color-emerald-300: oklch(0.845 0.143 164.978);
            --color-emerald-400: oklch(0.765 0.177 163.223);
            --color-emerald-500: oklch(0.696 0.17 162.48);
            --color-emerald-600: oklch(0.596 0.145 163.225);
            --color-emerald-700: oklch(0.508 0.118 165.612);
            --color-emerald-800: oklch(0.432 0.095 166.913);
            --color-emerald-900: oklch(0.378 0.077 168.94);
            --color-emerald-950: oklch(0.262 0.051 172.552);
            --color-teal-50: oklch(0.984 0.014 180.72);
            --color-teal-100: oklch(0.953 0.051 180.801);
            --color-teal-200: oklch(0.91 0.096 180.426);
            --color-teal-300: oklch(0.855 0.138 181.071);
            --color-teal-400: oklch(0.777 0.152 181.912);
            --color-teal-500: oklch(0.704 0.14 182.503);
            --color-teal-600: oklch(0.6 0.118 184.704);
            --color-teal-700: oklch(0.511 0.096 186.391);
            --color-teal-800: oklch(0.437 0.078 188.216);
            --color-teal-900: oklch(0.386 0.063 188.416);
            --color-teal-950: oklch(0.277 0.046 192.524);
            --color-cyan-50: oklch(0.984 0.019 200.873);
            --color-cyan-100: oklch(0.956 0.045 203.388);
            --color-cyan-200: oklch(0.917 0.08 205.041);
            --color-cyan-300: oklch(0.865 0.127 207.078);
            --color-cyan-400: oklch(0.789 0.154 211.53);
            --color-cyan-500: oklch(0.715 0.143 215.221);
            --color-cyan-600: oklch(0.609 0.126 221.723);
            --color-cyan-700: oklch(0.52 0.105 223.128);
            --color-cyan-800: oklch(0.45 0.085 224.283);
            --color-cyan-900: oklch(0.398 0.07 227.392);
            --color-cyan-950: oklch(0.302 0.056 229.695);
            --color-sky-50: oklch(0.977 0.013 236.62);
            --color-sky-100: oklch(0.951 0.026 236.824);
            --color-sky-200: oklch(0.901 0.058 230.902);
            --color-sky-300: oklch(0.828 0.111 230.318);
            --color-sky-400: oklch(0.746 0.16 232.661);
            --color-sky-500: oklch(0.685 0.169 237.323);
            --color-sky-600: oklch(0.588 0.158 241.966);
            --color-sky-700: oklch(0.5 0.134 242.749);
            --color-sky-800: oklch(0.443 0.11 240.79);
            --color-sky-900: oklch(0.391 0.09 240.876);
            --color-sky-950: oklch(0.293 0.066 243.157);
            --color-blue-50: oklch(0.97 0.014 254.604);
            --color-blue-100: oklch(0.932 0.032 255.585);
            --color-blue-200: oklch(0.882 0.059 254.128);
            --color-blue-300: oklch(0.809 0.105 251.813);
            --color-blue-400: oklch(0.707 0.165 254.624);
            --color-blue-500: oklch(0.623 0.214 259.815);
            --color-blue-600: oklch(0.546 0.245 262.881);
            --color-blue-700: oklch(0.488 0.243 264.376);
            --color-blue-800: oklch(0.424 0.199 265.638);
            --color-blue-900: oklch(0.379 0.146 265.522);
            --color-blue-950: oklch(0.282 0.091 267.935);
            --color-indigo-50: oklch(0.962 0.018 272.314);
            --color-indigo-100: oklch(0.93 0.034 272.788);
            --color-indigo-200: oklch(0.87 0.065 274.039);
            --color-indigo-300: oklch(0.785 0.115 274.713);
            --color-indigo-400: oklch(0.673 0.182 276.935);
            --color-indigo-500: oklch(0.585 0.233 277.117);
            --color-indigo-600: oklch(0.511 0.262 276.966);
            --color-indigo-700: oklch(0.457 0.24 277.023);
            --color-indigo-800: oklch(0.398 0.195 277.366);
            --color-indigo-900: oklch(0.359 0.144 278.697);
            --color-indigo-950: oklch(0.257 0.09 281.288);
            --color-violet-50: oklch(0.969 0.016 293.756);
            --color-violet-100: oklch(0.943 0.029 294.588);
            --color-violet-200: oklch(0.894 0.057 293.283);
            --color-violet-300: oklch(0.811 0.111 293.571);
            --color-violet-400: oklch(0.702 0.183 293.541);
            --color-violet-500: oklch(0.606 0.25 292.717);
            --color-violet-600: oklch(0.541 0.281 293.009);
            --color-violet-700: oklch(0.491 0.27 292.581);
            --color-violet-800: oklch(0.432 0.232 292.759);
            --color-violet-900: oklch(0.38 0.189 293.745);
            --color-violet-950: oklch(0.283 0.141 291.089);
            --color-purple-50: oklch(0.977 0.014 308.299);
            --color-purple-100: oklch(0.946 0.033 307.174);
            --color-purple-200: oklch(0.902 0.063 306.703);
            --color-purple-300: oklch(0.827 0.119 306.383);
            --color-purple-400: oklch(0.714 0.203 305.504);
            --color-purple-500: oklch(0.627 0.265 303.9);
            --color-purple-600: oklch(0.558 0.288 302.321);
            --color-purple-700: oklch(0.496 0.265 301.924);
            --color-purple-800: oklch(0.438 0.218 303.724);
            --color-purple-900: oklch(0.381 0.176 304.987);
            --color-purple-950: oklch(0.291 0.149 302.717);
            --color-fuchsia-50: oklch(0.977 0.017 320.058);
            --color-fuchsia-100: oklch(0.952 0.037 318.852);
            --color-fuchsia-200: oklch(0.903 0.076 319.62);
            --color-fuchsia-300: oklch(0.833 0.145 321.434);
            --color-fuchsia-400: oklch(0.74 0.238 322.16);
            --color-fuchsia-500: oklch(0.667 0.295 322.15);
            --color-fuchsia-600: oklch(0.591 0.293 322.896);
            --color-fuchsia-700: oklch(0.518 0.253 323.949);
            --color-fuchsia-800: oklch(0.452 0.211 324.591);
            --color-fuchsia-900: oklch(0.401 0.17 325.612);
            --color-fuchsia-950: oklch(0.293 0.136 325.661);
            --color-pink-50: oklch(0.971 0.014 343.198);
            --color-pink-100: oklch(0.948 0.028 342.258);
            --color-pink-200: oklch(0.899 0.061 343.231);
            --color-pink-300: oklch(0.823 0.12 346.018);
            --color-pink-400: oklch(0.718 0.202 349.761);
            --color-pink-500: oklch(0.656 0.241 354.308);
            --color-pink-600: oklch(0.592 0.249 0.584);
            --color-pink-700: oklch(0.525 0.223 3.958);
            --color-pink-800: oklch(0.459 0.187 3.815);
            --color-pink-900: oklch(0.408 0.153 2.432);
            --color-pink-950: oklch(0.284 0.109 3.907);
            --color-rose-50: oklch(0.969 0.015 12.422);
            --color-rose-100: oklch(0.941 0.03 12.58);
            --color-rose-200: oklch(0.892 0.058 10.001);
            --color-rose-300: oklch(0.81 0.117 11.638);
            --color-rose-400: oklch(0.712 0.194 13.428);
            --color-rose-500: oklch(0.645 0.246 16.439);
            --color-rose-600: oklch(0.586 0.253 17.585);
            --color-rose-700: oklch(0.514 0.222 16.935);
            --color-rose-800: oklch(0.455 0.188 13.697);
            --color-rose-900: oklch(0.41 0.159 10.272);
            --color-rose-950: oklch(0.271 0.105 12.094);
            --color-slate-50: oklch(0.984 0.003 247.858);
            --color-slate-100: oklch(0.968 0.007 247.896);
            --color-slate-200: oklch(0.929 0.013 255.508);
            --color-slate-300: oklch(0.869 0.022 252.894);
            --color-slate-400: oklch(0.704 0.04 256.788);
            --color-slate-500: oklch(0.554 0.046 257.417);
            --color-slate-600: oklch(0.446 0.043 257.281);
            --color-slate-700: oklch(0.372 0.044 257.287);
            --color-slate-800: oklch(0.279 0.041 260.031);
            --color-slate-900: oklch(0.208 0.042 265.755);
            --color-slate-950: oklch(0.129 0.042 264.695);
            --color-gray-50: oklch(0.985 0.002 247.839);
            --color-gray-100: oklch(0.967 0.003 264.542);
            --color-gray-200: oklch(0.928 0.006 264.531);
            --color-gray-300: oklch(0.872 0.01 258.338);
            --color-gray-400: oklch(0.707 0.022 261.325);
            --color-gray-500: oklch(0.551 0.027 264.364);
            --color-gray-600: oklch(0.446 0.03 256.802);
            --color-gray-700: oklch(0.373 0.034 259.733);
            --color-gray-800: oklch(0.278 0.033 256.848);
            --color-gray-900: oklch(0.21 0.034 264.665);
            --color-gray-950: oklch(0.13 0.028 261.692);
            --color-zinc-50: oklch(0.985 0 0);
            --color-zinc-100: oklch(0.967 0.001 286.375);
            --color-zinc-200: oklch(0.92 0.004 286.32);
            --color-zinc-300: oklch(0.871 0.006 286.286);
            --color-zinc-400: oklch(0.705 0.015 286.067);
            --color-zinc-500: oklch(0.552 0.016 285.938);
            --color-zinc-600: oklch(0.442 0.017 285.786);
            --color-zinc-700: oklch(0.37 0.013 285.805);
            --color-zinc-800: oklch(0.274 0.006 286.033);
            --color-zinc-900: oklch(0.21 0.006 285.885);
            --color-zinc-950: oklch(0.141 0.005 285.823);
            --color-neutral-50: oklch(0.985 0 0);
            --color-neutral-100: oklch(0.97 0 0);
            --color-neutral-200: oklch(0.922 0 0);
            --color-neutral-300: oklch(0.87 0 0);
            --color-neutral-400: oklch(0.708 0 0);
            --color-neutral-500: oklch(0.556 0 0);
            --color-neutral-600: oklch(0.439 0 0);
            --color-neutral-700: oklch(0.371 0 0);
            --color-neutral-800: oklch(0.269 0 0);
            --color-neutral-900: oklch(0.205 0 0);
            --color-neutral-950: oklch(0.145 0 0);
            --color-stone-50: oklch(0.985 0.001 106.423);
            --color-stone-100: oklch(0.97 0.001 106.424);
            --color-stone-200: oklch(0.923 0.003 48.717);
            --color-stone-300: oklch(0.869 0.005 56.366);
            --color-stone-400: oklch(0.709 0.01 56.259);
            --color-stone-500: oklch(0.553 0.013 58.071);
            --color-stone-600: oklch(0.444 0.011 73.639);
            --color-stone-700: oklch(0.374 0.01 67.558);
            --color-stone-800: oklch(0.268 0.007 34.298);
            --color-stone-900: oklch(0.216 0.006 56.043);
            --color-stone-950: oklch(0.147 0.004 49.25);
            --color-black: #000;
            --color-white: #fff;
            --breakpoint-sm: 40rem;
            --breakpoint-md: 48rem;
            --breakpoint-lg: 64rem;
            --breakpoint-xl: 80rem;
            --breakpoint-2xl: 96rem;
            --default-font-family: var(--font-sans);
            --default-font-feature-settings: var(--font-sans--font-feature-settings);
            --default-font-variation-settings: var(--font-sans--font-variation-settings);
            --default-mono-font-family: var(--font-mono);
            --default-mono-font-feature-settings: var(--font-mono--font-feature-settings);
            --default-mono-font-variation-settings: var(--font-mono--font-variation-settings);
            --rx: 16;
          }
        }
        """
    );
  }

  @Test(description = "Full generation + clear")
  public void fullGeneration02() {
    CssEngine engine;
    engine = new CssEngine();

    engine.theme("""
    --color-*: initial;
    --color-foo: rebecapurple;
    """);

    engine.skipLayer(Css.Layer.BASE);
    engine.skipLayer(Css.Layer.COMPONENTS);
    engine.skipLayer(Css.Layer.UTILITIES);

    engine.execute();

    assertEquals(
        engine.generate(),

        """
        @layer theme {
          :root {
            --font-sans: ui-sans-serif, system-ui, sans-serif, 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol', 'Noto Color Emoji';
            --font-serif: ui-serif, Georgia, Cambria, 'Times New Roman', Times, serif;
            --font-mono: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, 'Liberation Mono', 'Courier New', monospace;
            --breakpoint-sm: 40rem;
            --breakpoint-md: 48rem;
            --breakpoint-lg: 64rem;
            --breakpoint-xl: 80rem;
            --breakpoint-2xl: 96rem;
            --default-font-family: var(--font-sans);
            --default-font-feature-settings: var(--font-sans--font-feature-settings);
            --default-font-variation-settings: var(--font-sans--font-variation-settings);
            --default-mono-font-family: var(--font-mono);
            --default-mono-font-feature-settings: var(--font-mono--font-feature-settings);
            --default-mono-font-variation-settings: var(--font-mono--font-variation-settings);
            --rx: 16;
            --color-foo: rebecapurple;
          }
        }
        """
    );
  }

  @Test(description = "Full generation + override")
  public void fullGeneration03() {
    CssEngine engine;
    engine = new CssEngine();

    engine.theme("""
    --color-*: initial;
    --breakpoint-*: initial;
    --font-sans: 'DM Sans';
    """);

    engine.skipLayer(Css.Layer.BASE);
    engine.skipLayer(Css.Layer.COMPONENTS);
    engine.skipLayer(Css.Layer.UTILITIES);

    engine.execute();

    assertEquals(
        engine.generate(),

        """
        @layer theme {
          :root {
            --font-sans: 'DM Sans';
            --font-serif: ui-serif, Georgia, Cambria, 'Times New Roman', Times, serif;
            --font-mono: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, 'Liberation Mono', 'Courier New', monospace;
            --default-font-family: var(--font-sans);
            --default-font-feature-settings: var(--font-sans--font-feature-settings);
            --default-font-variation-settings: var(--font-sans--font-variation-settings);
            --default-mono-font-family: var(--font-mono);
            --default-mono-font-feature-settings: var(--font-mono--font-feature-settings);
            --default-mono-font-variation-settings: var(--font-mono--font-variation-settings);
            --rx: 16;
          }
        }
        """
    );
  }

  private List<CssEngine.ThemeEntry> test(String theme) {
    CssEngine engine;
    engine = new CssEngine();

    engine.theme(theme);

    return engine.testThemeEntries();
  }

}