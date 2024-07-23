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

import java.util.Map;
import java.util.Set;
import objectos.lang.object.Check;
import objectos.notes.NoteSink;
import objectos.util.map.GrowableMap;

/**
 * The <strong>Objectos CSS</strong> main class.
 */
public final class Css {

  /**
   * Generates a style sheet by scanning Java class files for predefined CSS
   * utility class names.
   */
  public sealed interface Generator permits CssGeneratorSpec {

    /**
     * The set of classes to scan.
     */
    public sealed interface Classes permits CssGeneratorOption {}

    /**
     * A style sheet generation option.
     */
    public sealed interface Option permits CssGeneratorOption {}

  }

  /**
   * A CSS style sheet.
   */
  public sealed interface StyleSheet permits CssStyleSheet {

    default String contentType() {
      return "text/css; charset=utf-8";
    }

    String css();

    byte[] toByteArray();

  }

  // private types

  private Css() {}

  public static String generateCss(Generator.Classes classes, Generator.Option... options) {
    int len;
    len = options.length; // implicit null-check

    CssConfig config;
    config = new CssConfig();

    CssGeneratorOption classesOption;
    classesOption = CssGeneratorOption.cast(classes);

    classesOption.acceptCssGenerator(config);

    for (int i = 0; i < len; i++) {
      Generator.Option o;
      o = Check.notNull(options[i], "options[", i, "] == null");

      CssGeneratorOption option;
      option = CssGeneratorOption.cast(o);

      option.acceptCssGenerator(config);
    }

    CssGeneratorSpec spec;
    spec = new CssGeneratorSpec(config);

    return spec.generate();
  }

  public static StyleSheet generateStyleSheet(Generator.Classes classes, Generator.Option... options) {
    String css;
    css = generateCss(classes, options);

    return new CssStyleSheet(css);
  }

  // options

  public static Generator.Option baseLayer(String contents) {
    Check.notNull(contents, "contents == null");

    return new CssGeneratorOption() {
      @Override
      final void acceptCssGenerator(CssConfig config) {
        config.baseLayer(contents);
      }
    };
  }

  public static Generator.Option breakpoints(String text) {
    CssProperties properties;
    properties = parseProperties(text);

    return new CssGeneratorOption() {
      @Override
      final void acceptCssGenerator(CssConfig config) {
        config.breakpoints(properties);
      }
    };
  }

  public static Generator.Classes classes(Class<?>... values) {
    Set<Class<?>> set;
    set = Set.of(values);

    return new CssGeneratorOption() {
      @Override
      final void acceptCssGenerator(CssConfig config) {
        config.classes(set);
      }
    };
  }

  public static Generator.Option noteSink(NoteSink noteSink) {
    Check.notNull(noteSink, "noteSink == null");

    return new CssGeneratorOption() {
      @Override
      final void acceptCssGenerator(CssConfig config) {
        config.noteSink(noteSink);
      }
    };
  }

  public static Generator.Option overrideBackgroundColor(String text) {
    return override(CssKey.BACKGROUND_COLOR, text);
  }

  public static Generator.Option overrideBorderColor(String text) {
    return override(CssKey.BORDER_COLOR, text);
  }

  public static Generator.Option overrideBorderWidth(String text) {
    return override(CssKey.BORDER_WIDTH, text);
  }

  public static Generator.Option overrideColors(String text) {
    return override(CssKey._COLORS, text);
  }

  public static Generator.Option overrideContent(String text) {
    return override(CssKey.CONTENT, text);
  }

  public static Generator.Option overrideFill(String text) {
    return override(CssKey.FILL, text);
  }

  public static Generator.Option overrideFontSize(String text) {
    return override(CssKey.FONT_SIZE, text);
  }

  public static Generator.Option overrideFontWeight(String text) {
    return override(CssKey.FONT_WEIGHT, text);
  }

  public static Generator.Option overrideGridColumn(String text) {
    return override(CssKey.GRID_COLUMN, text);
  }

  public static Generator.Option overrideGridColumnEnd(String text) {
    return override(CssKey.GRID_COLUMN_END, text);
  }

  public static Generator.Option overrideGridColumnStart(String text) {
    return override(CssKey.GRID_COLUMN_START, text);
  }

  public static Generator.Option overrideGridTemplateColumns(String text) {
    return override(CssKey.GRID_TEMPLATE_COLUMNS, text);
  }

  public static Generator.Option overrideGridTemplateRows(String text) {
    return override(CssKey.GRID_TEMPLATE_ROWS, text);
  }

  public static Generator.Option overrideLetterSpacing(String text) {
    return override(CssKey.LETTER_SPACING, text);
  }

  public static Generator.Option overrideLineHeight(String text) {
    return override(CssKey.LINE_HEIGHT, text);
  }

  public static Generator.Option overrideOutlineColor(String text) {
    return override(CssKey.OUTLINE_COLOR, text);
  }

  public static Generator.Option overrideSpacing(String text) {
    return override(CssKey._SPACING, text);
  }

  public static Generator.Option overrideTextColor(String text) {
    return override(CssKey.TEXT_COLOR, text);
  }

  public static Generator.Option overrideZIndex(String text) {
    return override(CssKey.Z_INDEX, text);
  }

  private static Generator.Option override(CssKey key, String text) {
    CssProperties properties;
    properties = parseProperties(text);

    return new CssGeneratorOption() {
      @Override
      final void acceptCssGenerator(CssConfig config) {
        config.override(key, properties);
      }
    };
  }

  public static Generator.Option skipReset() {
    return new CssGeneratorOption() {
      @Override
      final void acceptCssGenerator(CssConfig config) {
        config.skipReset(true);
      }
    };
  }

  public static Generator.Option useLogicalProperties() {
    return new CssGeneratorOption() {
      @Override
      final void acceptCssGenerator(CssConfig config) {
        config.useLogicalProperties();
      }
    };
  }

  public static Generator.Option utility(String className, String text) {
    Check.notNull(className, "className == null");

    CssProperties properties;
    properties = parseProperties(text);

    return new CssGeneratorOption() {
      @Override
      final void acceptCssGenerator(CssConfig config) {
        config.addUtility(className, properties);
      }
    };
  }

  public static Generator.Option variants(String text) {
    CssProperties props;
    props = parseProperties(text);

    return new CssGeneratorOption() {
      @Override
      final void acceptCssGenerator(CssConfig config) {
        config.addVariants(props);
      }
    };
  }

  // private stuff

  static CssProperties parseProperties(String text) {
    CssProperties.Builder builder;
    builder = new CssProperties.Builder();

    String[] lines;
    lines = text.split("\n");

    for (String line : lines) {
      if (line.isBlank()) {
        continue;
      }

      int colon;
      colon = line.indexOf(':');

      if (colon < 0) {
        throw new IllegalArgumentException(
            "The colon character ':' was not found in the line listed below:\n\n" + line + "\n"
        );
      }

      String key;
      key = line.substring(0, colon);

      String value;
      value = line.substring(colon + 1);

      builder.add(key.trim(), value.trim());
    }

    return builder.build();
  }

  static Map<String, CssProperties> parseTable(String text) {
    GrowableMap<String, CssProperties> map;
    map = new GrowableMap<>();

    String[] lines;
    lines = text.split("\n");

    String className;
    className = null;

    CssProperties.Builder props;
    props = null;

    for (String line : lines) {
      if (line.isBlank()) {
        continue;
      }

      int pipe;
      pipe = line.indexOf('|');

      if (pipe < 0) {
        throw new IllegalArgumentException(
            "The vertical bar character '|' was not found in the line listed below:\n\n" + line + "\n"
        );
      }

      String maybeClass;
      maybeClass = line.substring(0, pipe);

      maybeClass = maybeClass.trim();

      if (!maybeClass.isEmpty()) {
        if (className != null) {
          map.put(className, props.build());
        }

        className = maybeClass;

        props = new CssProperties.Builder();
      }

      int colon;
      colon = line.indexOf(':', pipe);

      if (colon < 0) {
        throw new IllegalArgumentException(
            "The colon character ':' was not found in the line listed below:\n\n" + line + "\n"
        );
      }

      String key;
      key = line.substring(pipe + 1, colon);

      String value;
      value = line.substring(colon + 1);

      props.add(key.trim(), value.trim());
    }

    if (className != null) {
      map.put(className, props.build());
    }

    return map.toUnmodifiableMap();
  }

  private static final int UNSIGNED = 0xFF;

  static int toUnsignedInt(byte b) {
    return b & UNSIGNED;
  }

  static int toBigEndianInt(byte b0, byte b1) {
    int v0;
    v0 = toInt(b0, 8);

    int v1;
    v1 = toInt(b1, 0);

    return v1 | v0;
  }

  static int toBigEndianInt(byte b0, byte b1, byte b2, byte b3) {
    int v0;
    v0 = toInt(b0, 24);

    int v1;
    v1 = toInt(b1, 16);

    int v2;
    v2 = toInt(b2, 8);

    int v3;
    v3 = toInt(b3, 0);

    return v3 | v2 | v1 | v0;
  }

  private static int toInt(byte b, int shift) {
    return toUnsignedInt(b) << shift;
  }

  static Map<String, String> merge(String text, Map<String, String> more) {
    CssProperties properties;
    properties = parseProperties(text);

    return properties.toMap(more);
  }

  static final String DEFAULT_COLORS = """
      inherit: inherit
      current: currentColor
      transparent: transparent

      black: #000000
      white: #ffffff

      slate-50: #f8fafc
      slate-100: #f1f5f9
      slate-200: #e2e8f0
      slate-300: #cbd5e1
      slate-400: #94a3b8
      slate-500: #64748b
      slate-600: #475569
      slate-700: #334155
      slate-800: #1e293b
      slate-900: #0f172a
      slate-950: #020617

      gray-50: #f9fafb
      gray-100: #f3f4f6
      gray-200: #e5e7eb
      gray-300: #d1d5db
      gray-400: #9ca3af
      gray-500: #6b7280
      gray-600: #4b5563
      gray-700: #374151
      gray-800: #1f2937
      gray-900: #111827
      gray-950: #030712

      zinc-50: #fafafa
      zinc-100: #f4f4f5
      zinc-200: #e4e4e7
      zinc-300: #d4d4d8
      zinc-400: #a1a1aa
      zinc-500: #71717a
      zinc-600: #52525b
      zinc-700: #3f3f46
      zinc-800: #27272a
      zinc-900: #18181b
      zinc-950: #09090b

      neutral-50: #fafafa
      neutral-100: #f5f5f5
      neutral-200: #e5e5e5
      neutral-300: #d4d4d4
      neutral-400: #a3a3a3
      neutral-500: #737373
      neutral-600: #525252
      neutral-700: #404040
      neutral-800: #262626
      neutral-900: #171717
      neutral-950: #0a0a0a

      stone-50: #fafaf9
      stone-100: #f5f5f4
      stone-200: #e7e5e4
      stone-300: #d6d3d1
      stone-400: #a8a29e
      stone-500: #78716c
      stone-600: #57534e
      stone-700: #44403c
      stone-800: #292524
      stone-900: #1c1917
      stone-950: #0c0a09

      red-50: #fef2f2
      red-100: #fee2e2
      red-200: #fecaca
      red-300: #fca5a5
      red-400: #f87171
      red-500: #ef4444
      red-600: #dc2626
      red-700: #b91c1c
      red-800: #991b1b
      red-900: #7f1d1d
      red-950: #450a0a

      orange-50: #fff7ed
      orange-100: #ffedd5
      orange-200: #fed7aa
      orange-300: #fdba74
      orange-400: #fb923c
      orange-500: #f97316
      orange-600: #ea580c
      orange-700: #c2410c
      orange-800: #9a3412
      orange-900: #7c2d12
      orange-950: #431407

      amber-50: #fffbeb
      amber-100: #fef3c7
      amber-200: #fde68a
      amber-300: #fcd34d
      amber-400: #fbbf24
      amber-500: #f59e0b
      amber-600: #d97706
      amber-700: #b45309
      amber-800: #92400e
      amber-900: #78350f
      amber-950: #451a03

      yellow-50: #fefce8
      yellow-100: #fef9c3
      yellow-200: #fef08a
      yellow-300: #fde047
      yellow-400: #facc15
      yellow-500: #eab308
      yellow-600: #ca8a04
      yellow-700: #a16207
      yellow-800: #854d0e
      yellow-900: #713f12
      yellow-950: #422006

      lime-50: #f7fee7
      lime-100: #ecfccb
      lime-200: #d9f99d
      lime-300: #bef264
      lime-400: #a3e635
      lime-500: #84cc16
      lime-600: #65a30d
      lime-700: #4d7c0f
      lime-800: #3f6212
      lime-900: #365314
      lime-950: #1a2e05

      green-50: #f0fdf4
      green-100: #dcfce7
      green-200: #bbf7d0
      green-300: #86efac
      green-400: #4ade80
      green-500: #22c55e
      green-600: #16a34a
      green-700: #15803d
      green-800: #166534
      green-900: #14532d
      green-950: #052e16

      emerald-50: #ecfdf5
      emerald-100: #d1fae5
      emerald-200: #a7f3d0
      emerald-300: #6ee7b7
      emerald-400: #34d399
      emerald-500: #10b981
      emerald-600: #059669
      emerald-700: #047857
      emerald-800: #065f46
      emerald-900: #064e3b
      emerald-950: #022c22

      teal-50: #f0fdfa
      teal-100: #ccfbf1
      teal-200: #99f6e4
      teal-300: #5eead4
      teal-400: #2dd4bf
      teal-500: #14b8a6
      teal-600: #0d9488
      teal-700: #0f766e
      teal-800: #115e59
      teal-900: #134e4a
      teal-950: #042f2e

      cyan-50: #ecfeff
      cyan-100: #cffafe
      cyan-200: #a5f3fc
      cyan-300: #67e8f9
      cyan-400: #22d3ee
      cyan-500: #06b6d4
      cyan-600: #0891b2
      cyan-700: #0e7490
      cyan-800: #155e75
      cyan-900: #164e63
      cyan-950: #083344

      sky-50: #f0f9ff
      sky-100: #e0f2fe
      sky-200: #bae6fd
      sky-300: #7dd3fc
      sky-400: #38bdf8
      sky-500: #0ea5e9
      sky-600: #0284c7
      sky-700: #0369a1
      sky-800: #075985
      sky-900: #0c4a6e
      sky-950: #082f49

      blue-50: #eff6ff
      blue-100: #dbeafe
      blue-200: #bfdbfe
      blue-300: #93c5fd
      blue-400: #60a5fa
      blue-500: #3b82f6
      blue-600: #2563eb
      blue-700: #1d4ed8
      blue-800: #1e40af
      blue-900: #1e3a8a
      blue-950: #172554

      indigo-50: #eef2ff
      indigo-100: #e0e7ff
      indigo-200: #c7d2fe
      indigo-300: #a5b4fc
      indigo-400: #818cf8
      indigo-500: #6366f1
      indigo-600: #4f46e5
      indigo-700: #4338ca
      indigo-800: #3730a3
      indigo-900: #312e81
      indigo-950: #1e1b4b

      violet-50: #f5f3ff
      violet-100: #ede9fe
      violet-200: #ddd6fe
      violet-300: #c4b5fd
      violet-400: #a78bfa
      violet-500: #8b5cf6
      violet-600: #7c3aed
      violet-700: #6d28d9
      violet-800: #5b21b6
      violet-900: #4c1d95
      violet-950: #2e1065

      purple-50: #faf5ff
      purple-100: #f3e8ff
      purple-200: #e9d5ff
      purple-300: #d8b4fe
      purple-400: #c084fc
      purple-500: #a855f7
      purple-600: #9333ea
      purple-700: #7e22ce
      purple-800: #6b21a8
      purple-900: #581c87
      purple-950: #3b0764

      fuchsia-50: #fdf4ff
      fuchsia-100: #fae8ff
      fuchsia-200: #f5d0fe
      fuchsia-300: #f0abfc
      fuchsia-400: #e879f9
      fuchsia-500: #d946ef
      fuchsia-600: #c026d3
      fuchsia-700: #a21caf
      fuchsia-800: #86198f
      fuchsia-900: #701a75
      fuchsia-950: #4a044e

      pink-50: #fdf2f8
      pink-100: #fce7f3
      pink-200: #fbcfe8
      pink-300: #f9a8d4
      pink-400: #f472b6
      pink-500: #ec4899
      pink-600: #db2777
      pink-700: #be185d
      pink-800: #9d174d
      pink-900: #831843
      pink-950: #500724

      rose-50: #fff1f2
      rose-100: #ffe4e6
      rose-200: #fecdd3
      rose-300: #fda4af
      rose-400: #fb7185
      rose-500: #f43f5e
      rose-600: #e11d48
      rose-700: #be123c
      rose-800: #9f1239
      rose-900: #881337
      rose-950: #4c0519
      """;

  static final String DEFAULT_SPACING = """
      px: 1px
      0: 0px
      0.5: 0.125rem
      1: 0.25rem
      1.5: 0.375rem
      2: 0.5rem
      2.5: 0.625rem
      3: 0.75rem
      3.5: 0.875rem
      4: 1rem
      5: 1.25rem
      6: 1.5rem
      7: 1.75rem
      8: 2rem
      9: 2.25rem
      10: 2.5rem
      11: 2.75rem
      12: 3rem
      14: 3.5rem
      16: 4rem
      20: 5rem
      24: 6rem
      28: 7rem
      32: 8rem
      36: 9rem
      40: 10rem
      44: 11rem
      48: 12rem
      52: 13rem
      56: 14rem
      60: 15rem
      64: 16rem
      72: 18rem
      80: 20rem
      96: 24rem
      """;

}