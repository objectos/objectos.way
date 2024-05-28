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
package objectos.css;

import java.util.List;
import java.util.Map;
import objectos.css.Variant.AppendTo;
import objectos.css.Variant.Breakpoint;
import objectos.lang.object.Check;
import objectos.notes.NoOpNoteSink;
import objectos.notes.NoteSink;
import objectos.util.map.GrowableMap;

public final class WayStyleGen extends WayStyleGenConfig implements StyleGen {

  private NoteSink noteSink = NoOpNoteSink.of();

  private final List<Breakpoint> breakpoints = List.of(
      new Breakpoint(0, "sm", "640px"),
      new Breakpoint(1, "md", "768px"),
      new Breakpoint(2, "lg", "1024px"),
      new Breakpoint(3, "xl", "1280px"),
      new Breakpoint(4, "2xl", "1536px")
  );

  private Map<String, String> borderSpacing;

  private Map<String, String> borderWidth;

  private Map<String, String> colors;

  private Map<String, String> content;

  private Map<String, String> cursor;

  private Map<String, String> flexGrow;

  private Map<String, String> fontSize;

  private Map<String, String> fontWeight;

  private Map<String, String> gap;
  
  private Map<String, String> gridColumn;
  private Map<String, String> gridColumnEnd;
  private Map<String, String> gridColumnStart;
  
  private Map<String, String> gridTemplateColumns;

  private Map<String, String> height;

  private Map<String, String> inset;

  private final Map<String, String> letterSpacing;
  private final Map<String, String> lineHeight;

  private Map<String, String> margin;

  private Map<String, String> maxWidth;

  private Map<String, String> opacity;

  private Map<String, String> outlineOffset;
  private Map<String, String> outlineWidth;

  private Map<String, String> padding;

  private Map<String, String> rules;

  private Map<String, String> spacing;

  private Map<String, String> transitionDuration;
  private Map<String, String> transitionProperty;

  private Map<String, String> utilities;

  private Map<String, Variant> variants;

  private Map<String, String> width;

  private Map<String, String> zIndex;

  public WayStyleGen() {
    // L
    letterSpacing = new GrowableMap<>();
    letterSpacing.put("tighter", "-0.05em");
    letterSpacing.put("tight", "-0.025em");
    letterSpacing.put("normal", "0em");
    letterSpacing.put("wide", "0.025em");
    letterSpacing.put("wider", "0.05em");
    letterSpacing.put("widest", "0.1em");

    lineHeight = new GrowableMap<>();
    lineHeight.put("3", "0.75rem");
    lineHeight.put("4", "1rem");
    lineHeight.put("5", "1.25rem");
    lineHeight.put("6", "1.5rem");
    lineHeight.put("7", "1.75rem");
    lineHeight.put("8", "2rem");
    lineHeight.put("9", "2.25rem");
    lineHeight.put("10", "2.5rem");
    lineHeight.put("none", "1");
    lineHeight.put("tight", "1.25");
    lineHeight.put("snug", "1.375");
    lineHeight.put("normal", "1.5");
    lineHeight.put("relaxed", "1.625");
    lineHeight.put("loose", "2");
  }

  public final WayStyleGen addRule(String selector, String contents) {
    Check.notNull(selector, "selector == null");
    Check.notNull(contents, "contents == null");

    if (rules == null) {
      rules = new GrowableMap<>();
    }

    rules.put(selector, contents);

    return this;
  }

  public final WayStyleGen addUtility(String className, String rule) {
    Check.notNull(className, "className == null");
    Check.notNull(rule, "rule == null");

    if (utilities == null) {
      utilities = new GrowableMap<>();
    }

    utilities.put(className, rule);

    return this;
  }

  public final WayStyleGen addVariant(String variantName, String formatString) {
    Check.notNull(variantName, "variantName == null");
    Check.notNull(formatString, "formatString == null");

    Variant variant;
    variant = Variant.parse(formatString);

    if (variant instanceof Variant.Invalid invalid) {
      throw new IllegalArgumentException("Invalid formatString: " + invalid.reason());
    }

    Map<String, Variant> map;
    map = variants();

    if (map.containsKey(variantName)) {
      throw new IllegalArgumentException("Variant already defined: " + variantName);
    }

    map.put(variantName, variant);

    return this;
  }

  public final WayStyleGen noteSink(NoteSink noteSink) {
    this.noteSink = Check.notNull(noteSink, "noteSink == null");

    return this;
  }

  @SafeVarargs
  @SuppressWarnings("varargs")
  public final WayStyleGen overrideColors(Map.Entry<String, String>... entries) {
    colors = Map.ofEntries(entries);

    return this;
  }

  @SafeVarargs
  @SuppressWarnings("varargs")
  public final WayStyleGen overrideContent(Map.Entry<String, String>... entries) {
    content = Map.ofEntries(entries);

    return this;
  }

  @SafeVarargs
  @SuppressWarnings("varargs")
  public final WayStyleGen overrideFontSize(Map.Entry<String, String>... entries) {
    fontSize = Map.ofEntries(entries);

    return this;
  }

  @SafeVarargs
  @SuppressWarnings("varargs")
  public final WayStyleGen overrideSpacing(Map.Entry<String, String>... entries) {
    spacing = Map.ofEntries(entries);

    return this;
  }
  
  public final void skipReset() {
    skipReset = true;
  }

  @Override
  public final String generate(Iterable<Class<?>> classes) {
    Check.notNull(classes, "classes == null");

    WayStyleGenRound round;
    round = new WayStyleGenRound(this);

    round.noteSink = noteSink;

    for (var clazz : classes) {
      round.scan(clazz);
    }

    return round.generate();
  }

  @Override
  final Variant getVariant(String variantName) {
    return variants().get(variantName);
  }

  @Override
  final Map<String, String> borderSpacing() {
    if (borderSpacing == null) {
      borderSpacing = spacing();
    }

    return borderSpacing;
  }

  @Override
  final Map<String, String> borderWidth() {
    if (borderWidth == null) {
      borderWidth = new GrowableMap<>();

      borderWidth.put("", "1px");
      borderWidth.put("0", "0px");
      borderWidth.put("2", "2px");
      borderWidth.put("4", "4px");
      borderWidth.put("8", "8px");
    }

    return borderWidth;
  }

  @Override
  final Map<String, String> colors() {
    if (colors == null) {
      colors = new GrowableMap<>();

      colors.put("inherit", "inherit");
      colors.put("current", "currentColor");
      colors.put("transparent", "transparent");

      colors.put("black", "#000000");
      colors.put("white", "#ffffff");

      colors.put("slate-50", "#f8fafc");
      colors.put("slate-100", "#f1f5f9");
      colors.put("slate-200", "#e2e8f0");
      colors.put("slate-300", "#cbd5e1");
      colors.put("slate-400", "#94a3b8");
      colors.put("slate-500", "#64748b");
      colors.put("slate-600", "#475569");
      colors.put("slate-700", "#334155");
      colors.put("slate-800", "#1e293b");
      colors.put("slate-900", "#0f172a");
      colors.put("slate-950", "#020617");

      colors.put("gray-50", "#f9fafb");
      colors.put("gray-100", "#f3f4f6");
      colors.put("gray-200", "#e5e7eb");
      colors.put("gray-300", "#d1d5db");
      colors.put("gray-400", "#9ca3af");
      colors.put("gray-500", "#6b7280");
      colors.put("gray-600", "#4b5563");
      colors.put("gray-700", "#374151");
      colors.put("gray-800", "#1f2937");
      colors.put("gray-900", "#111827");
      colors.put("gray-950", "#030712");

      colors.put("zinc-50", "#fafafa");
      colors.put("zinc-100", "#f4f4f5");
      colors.put("zinc-200", "#e4e4e7");
      colors.put("zinc-300", "#d4d4d8");
      colors.put("zinc-400", "#a1a1aa");
      colors.put("zinc-500", "#71717a");
      colors.put("zinc-600", "#52525b");
      colors.put("zinc-700", "#3f3f46");
      colors.put("zinc-800", "#27272a");
      colors.put("zinc-900", "#18181b");
      colors.put("zinc-950", "#09090b");

      colors.put("neutral-50", "#fafafa");
      colors.put("neutral-100", "#f5f5f5");
      colors.put("neutral-200", "#e5e5e5");
      colors.put("neutral-300", "#d4d4d4");
      colors.put("neutral-400", "#a3a3a3");
      colors.put("neutral-500", "#737373");
      colors.put("neutral-600", "#525252");
      colors.put("neutral-700", "#404040");
      colors.put("neutral-800", "#262626");
      colors.put("neutral-900", "#171717");
      colors.put("neutral-950", "#0a0a0a");

      colors.put("stone-50", "#fafaf9");
      colors.put("stone-100", "#f5f5f4");
      colors.put("stone-200", "#e7e5e4");
      colors.put("stone-300", "#d6d3d1");
      colors.put("stone-400", "#a8a29e");
      colors.put("stone-500", "#78716c");
      colors.put("stone-600", "#57534e");
      colors.put("stone-700", "#44403c");
      colors.put("stone-800", "#292524");
      colors.put("stone-900", "#1c1917");
      colors.put("stone-950", "#0c0a09");

      colors.put("red-50", "#fef2f2");
      colors.put("red-100", "#fee2e2");
      colors.put("red-200", "#fecaca");
      colors.put("red-300", "#fca5a5");
      colors.put("red-400", "#f87171");
      colors.put("red-500", "#ef4444");
      colors.put("red-600", "#dc2626");
      colors.put("red-700", "#b91c1c");
      colors.put("red-800", "#991b1b");
      colors.put("red-900", "#7f1d1d");
      colors.put("red-950", "#450a0a");

      colors.put("orange-50", "#fff7ed");
      colors.put("orange-100", "#ffedd5");
      colors.put("orange-200", "#fed7aa");
      colors.put("orange-300", "#fdba74");
      colors.put("orange-400", "#fb923c");
      colors.put("orange-500", "#f97316");
      colors.put("orange-600", "#ea580c");
      colors.put("orange-700", "#c2410c");
      colors.put("orange-800", "#9a3412");
      colors.put("orange-900", "#7c2d12");
      colors.put("orange-950", "#431407");

      colors.put("amber-50", "#fffbeb");
      colors.put("amber-100", "#fef3c7");
      colors.put("amber-200", "#fde68a");
      colors.put("amber-300", "#fcd34d");
      colors.put("amber-400", "#fbbf24");
      colors.put("amber-500", "#f59e0b");
      colors.put("amber-600", "#d97706");
      colors.put("amber-700", "#b45309");
      colors.put("amber-800", "#92400e");
      colors.put("amber-900", "#78350f");
      colors.put("amber-950", "#451a03");

      colors.put("yellow-50", "#fefce8");
      colors.put("yellow-100", "#fef9c3");
      colors.put("yellow-200", "#fef08a");
      colors.put("yellow-300", "#fde047");
      colors.put("yellow-400", "#facc15");
      colors.put("yellow-500", "#eab308");
      colors.put("yellow-600", "#ca8a04");
      colors.put("yellow-700", "#a16207");
      colors.put("yellow-800", "#854d0e");
      colors.put("yellow-900", "#713f12");
      colors.put("yellow-950", "#422006");

      colors.put("lime-50", "#f7fee7");
      colors.put("lime-100", "#ecfccb");
      colors.put("lime-200", "#d9f99d");
      colors.put("lime-300", "#bef264");
      colors.put("lime-400", "#a3e635");
      colors.put("lime-500", "#84cc16");
      colors.put("lime-600", "#65a30d");
      colors.put("lime-700", "#4d7c0f");
      colors.put("lime-800", "#3f6212");
      colors.put("lime-900", "#365314");
      colors.put("lime-950", "#1a2e05");

      colors.put("green-50", "#f0fdf4");
      colors.put("green-100", "#dcfce7");
      colors.put("green-200", "#bbf7d0");
      colors.put("green-300", "#86efac");
      colors.put("green-400", "#4ade80");
      colors.put("green-500", "#22c55e");
      colors.put("green-600", "#16a34a");
      colors.put("green-700", "#15803d");
      colors.put("green-800", "#166534");
      colors.put("green-900", "#14532d");
      colors.put("green-950", "#052e16");

      colors.put("emerald-50", "#ecfdf5");
      colors.put("emerald-100", "#d1fae5");
      colors.put("emerald-200", "#a7f3d0");
      colors.put("emerald-300", "#6ee7b7");
      colors.put("emerald-400", "#34d399");
      colors.put("emerald-500", "#10b981");
      colors.put("emerald-600", "#059669");
      colors.put("emerald-700", "#047857");
      colors.put("emerald-800", "#065f46");
      colors.put("emerald-900", "#064e3b");
      colors.put("emerald-950", "#022c22");

      colors.put("teal-50", "#f0fdfa");
      colors.put("teal-100", "#ccfbf1");
      colors.put("teal-200", "#99f6e4");
      colors.put("teal-300", "#5eead4");
      colors.put("teal-400", "#2dd4bf");
      colors.put("teal-500", "#14b8a6");
      colors.put("teal-600", "#0d9488");
      colors.put("teal-700", "#0f766e");
      colors.put("teal-800", "#115e59");
      colors.put("teal-900", "#134e4a");
      colors.put("teal-950", "#042f2e");

      colors.put("cyan-50", "#ecfeff");
      colors.put("cyan-100", "#cffafe");
      colors.put("cyan-200", "#a5f3fc");
      colors.put("cyan-300", "#67e8f9");
      colors.put("cyan-400", "#22d3ee");
      colors.put("cyan-500", "#06b6d4");
      colors.put("cyan-600", "#0891b2");
      colors.put("cyan-700", "#0e7490");
      colors.put("cyan-800", "#155e75");
      colors.put("cyan-900", "#164e63");
      colors.put("cyan-950", "#083344");

      colors.put("sky-50", "#f0f9ff");
      colors.put("sky-100", "#e0f2fe");
      colors.put("sky-200", "#bae6fd");
      colors.put("sky-300", "#7dd3fc");
      colors.put("sky-400", "#38bdf8");
      colors.put("sky-500", "#0ea5e9");
      colors.put("sky-600", "#0284c7");
      colors.put("sky-700", "#0369a1");
      colors.put("sky-800", "#075985");
      colors.put("sky-900", "#0c4a6e");
      colors.put("sky-950", "#082f49");

      colors.put("blue-50", "#eff6ff");
      colors.put("blue-100", "#dbeafe");
      colors.put("blue-200", "#bfdbfe");
      colors.put("blue-300", "#93c5fd");
      colors.put("blue-400", "#60a5fa");
      colors.put("blue-500", "#3b82f6");
      colors.put("blue-600", "#2563eb");
      colors.put("blue-700", "#1d4ed8");
      colors.put("blue-800", "#1e40af");
      colors.put("blue-900", "#1e3a8a");
      colors.put("blue-950", "#172554");

      colors.put("indigo-50", "#eef2ff");
      colors.put("indigo-100", "#e0e7ff");
      colors.put("indigo-200", "#c7d2fe");
      colors.put("indigo-300", "#a5b4fc");
      colors.put("indigo-400", "#818cf8");
      colors.put("indigo-500", "#6366f1");
      colors.put("indigo-600", "#4f46e5");
      colors.put("indigo-700", "#4338ca");
      colors.put("indigo-800", "#3730a3");
      colors.put("indigo-900", "#312e81");
      colors.put("indigo-950", "#1e1b4b");

      colors.put("violet-50", "#f5f3ff");
      colors.put("violet-100", "#ede9fe");
      colors.put("violet-200", "#ddd6fe");
      colors.put("violet-300", "#c4b5fd");
      colors.put("violet-400", "#a78bfa");
      colors.put("violet-500", "#8b5cf6");
      colors.put("violet-600", "#7c3aed");
      colors.put("violet-700", "#6d28d9");
      colors.put("violet-800", "#5b21b6");
      colors.put("violet-900", "#4c1d95");
      colors.put("violet-950", "#2e1065");

      colors.put("purple-50", "#faf5ff");
      colors.put("purple-100", "#f3e8ff");
      colors.put("purple-200", "#e9d5ff");
      colors.put("purple-300", "#d8b4fe");
      colors.put("purple-400", "#c084fc");
      colors.put("purple-500", "#a855f7");
      colors.put("purple-600", "#9333ea");
      colors.put("purple-700", "#7e22ce");
      colors.put("purple-800", "#6b21a8");
      colors.put("purple-900", "#581c87");
      colors.put("purple-950", "#3b0764");

      colors.put("fuchsia-50", "#fdf4ff");
      colors.put("fuchsia-100", "#fae8ff");
      colors.put("fuchsia-200", "#f5d0fe");
      colors.put("fuchsia-300", "#f0abfc");
      colors.put("fuchsia-400", "#e879f9");
      colors.put("fuchsia-500", "#d946ef");
      colors.put("fuchsia-600", "#c026d3");
      colors.put("fuchsia-700", "#a21caf");
      colors.put("fuchsia-800", "#86198f");
      colors.put("fuchsia-900", "#701a75");
      colors.put("fuchsia-950", "#4a044e");

      colors.put("pink-50", "#fdf2f8");
      colors.put("pink-100", "#fce7f3");
      colors.put("pink-200", "#fbcfe8");
      colors.put("pink-300", "#f9a8d4");
      colors.put("pink-400", "#f472b6");
      colors.put("pink-500", "#ec4899");
      colors.put("pink-600", "#db2777");
      colors.put("pink-700", "#be185d");
      colors.put("pink-800", "#9d174d");
      colors.put("pink-900", "#831843");
      colors.put("pink-950", "#500724");

      colors.put("rose-50", "#fff1f2");
      colors.put("rose-100", "#ffe4e6");
      colors.put("rose-200", "#fecdd3");
      colors.put("rose-300", "#fda4af");
      colors.put("rose-400", "#fb7185");
      colors.put("rose-500", "#f43f5e");
      colors.put("rose-600", "#e11d48");
      colors.put("rose-700", "#be123c");
      colors.put("rose-800", "#9f1239");
      colors.put("rose-900", "#881337");
      colors.put("rose-950", "#4c0519");
    }

    return colors;
  }

  @Override
  final Map<String, String> content() {
    if (content == null) {
      content = Map.of("none", "none");
    }

    return content;
  }

  @Override
  final Map<String, String> cursor() {
    if (cursor == null) {
      cursor = Map.ofEntries(
          Map.entry("auto", "auto"),
          Map.entry("default", "default"),
          Map.entry("pointer", "pointer"),
          Map.entry("wait", "wait"),
          Map.entry("text", "text"),
          Map.entry("move", "move"),
          Map.entry("help", "help"),
          Map.entry("not-allowed", "not-allowed"),
          Map.entry("none", "none"),
          Map.entry("context-menu", "context-menu"),
          Map.entry("progress", "progress"),
          Map.entry("cell", "cell"),
          Map.entry("crosshair", "crosshair"),
          Map.entry("vertical-text", "vertical-text"),
          Map.entry("alias", "alias"),
          Map.entry("copy", "copy"),
          Map.entry("no-drop", "no-drop"),
          Map.entry("grab", "grab"),
          Map.entry("grabbing", "grabbing"),
          Map.entry("all-scroll", "all-scroll"),
          Map.entry("col-resize", "col-resize"),
          Map.entry("row-resize", "row-resize"),
          Map.entry("n-resize", "n-resize"),
          Map.entry("e-resize", "e-resize"),
          Map.entry("s-resize", "s-resize"),
          Map.entry("w-resize", "w-resize"),
          Map.entry("ne-resize", "ne-resize"),
          Map.entry("nw-resize", "nw-resize"),
          Map.entry("se-resize", "se-resize"),
          Map.entry("sw-resize", "sw-resize"),
          Map.entry("ew-resize", "ew-resize"),
          Map.entry("ns-resize", "ns-resize"),
          Map.entry("nesw-resize", "nesw-resize"),
          Map.entry("nwse-resize", "nwse-resize"),
          Map.entry("zoom-in", "zoom-in"),
          Map.entry("zoom-out", "zoom-out")
      );
    }

    return cursor;
  }

  @Override
  final Map<String, String> flexGrow() {
    if (flexGrow == null) {
      flexGrow = Map.of(
          "", "1",
          "0", "0"
      );
    }

    return flexGrow;
  }

  @Override
  final Map<String, String> fontSize() {
    if (fontSize == null) {
      fontSize = new GrowableMap<>();

      fontSize.put("xs", "0.75rem/1rem");
      fontSize.put("sm", "0.875rem/1.25rem");
      fontSize.put("base", "1rem/1.5rem");
      fontSize.put("lg", "1.125rem/1.75rem");
      fontSize.put("xl", "1.25rem/1.75rem");
      fontSize.put("2xl", "1.5rem/2rem");
      fontSize.put("3xl", "1.875rem/2.25rem");
      fontSize.put("4xl", "2.25rem/2.5rem");
      fontSize.put("5xl", "3rem/1");
      fontSize.put("6xl", "3.75rem/1");
      fontSize.put("7xl", "4.5rem/1");
      fontSize.put("8xl", "6rem/1");
      fontSize.put("9xl", "8rem/1");
    }

    return fontSize;
  }

  @Override
  final Map<String, String> fontWeight() {
    if (fontWeight == null) {
      fontWeight = new GrowableMap<>();

      fontWeight.put("thin", "100");
      fontWeight.put("extralight", "200");
      fontWeight.put("light", "300");
      fontWeight.put("normal", "400");
      fontWeight.put("medium", "500");
      fontWeight.put("semibold", "600");
      fontWeight.put("bold", "700");
      fontWeight.put("extrabold", "800");
      fontWeight.put("black", "900");
    }

    return fontWeight;
  }

  @Override
  final Map<String, String> gap() {
    if (gap == null) {
      gap = spacing();
    }

    return gap;
  }
  
  @Override
  final Map<String, String> gridColumn() {
    if (gridColumn == null) {
      gridColumn = Map.ofEntries(
          Map.entry("auto", "auto"),
          Map.entry("span-1", "span 1 / span 1"),
          Map.entry("span-2", "span 2 / span 2"),
          Map.entry("span-3", "span 3 / span 3"),
          Map.entry("span-4", "span 4 / span 4"),
          Map.entry("span-5", "span 5 / span 5"),
          Map.entry("span-6", "span 6 / span 6"),
          Map.entry("span-7", "span 7 / span 7"),
          Map.entry("span-8", "span 8 / span 8"),
          Map.entry("span-9", "span 9 / span 9"),
          Map.entry("span-10", "span 10 / span 10"),
          Map.entry("span-11", "span 11 / span 11"),
          Map.entry("span-12", "span 12 / span 12"),
          Map.entry("span-full", "1 / -1")
      );
    }

    return gridColumn;
  }

  @Override
  final Map<String, String> gridColumnEnd() {
    if (gridColumnEnd == null) {
      gridColumnEnd = Map.ofEntries(
          Map.entry("auto", "auto"),
          Map.entry("1", "1"),
          Map.entry("2", "2"),
          Map.entry("3", "3"),
          Map.entry("4", "4"),
          Map.entry("5", "5"),
          Map.entry("6", "6"),
          Map.entry("7", "7"),
          Map.entry("8", "8"),
          Map.entry("9", "9"),
          Map.entry("10", "10"),
          Map.entry("11", "11"),
          Map.entry("12", "12"),
          Map.entry("13", "13")
      );
    }

    return gridColumnEnd;
  }

  @Override
  final Map<String, String> gridColumnStart() {
    if (gridColumnStart == null) {
      gridColumnStart = Map.ofEntries(
          Map.entry("auto", "auto"),
          Map.entry("1", "1"),
          Map.entry("2", "2"),
          Map.entry("3", "3"),
          Map.entry("4", "4"),
          Map.entry("5", "5"),
          Map.entry("6", "6"),
          Map.entry("7", "7"),
          Map.entry("8", "8"),
          Map.entry("9", "9"),
          Map.entry("10", "10"),
          Map.entry("11", "11"),
          Map.entry("12", "12"),
          Map.entry("13", "13")
      );
    }

    return gridColumnStart;
  }

  @Override
  final Map<String, String> gridTemplateColumns() {
    if (gridTemplateColumns == null) {
      gridTemplateColumns = new GrowableMap<>();

      gridTemplateColumns.put("none", "none");
      gridTemplateColumns.put("subgrid", "subgrid");
      gridTemplateColumns.put("1", "repeat(1, minmax(0, 1fr))");
      gridTemplateColumns.put("2", "repeat(2, minmax(0, 1fr))");
      gridTemplateColumns.put("3", "repeat(3, minmax(0, 1fr))");
      gridTemplateColumns.put("4", "repeat(4, minmax(0, 1fr))");
      gridTemplateColumns.put("5", "repeat(5, minmax(0, 1fr))");
      gridTemplateColumns.put("6", "repeat(6, minmax(0, 1fr))");
      gridTemplateColumns.put("7", "repeat(7, minmax(0, 1fr))");
      gridTemplateColumns.put("8", "repeat(8, minmax(0, 1fr))");
      gridTemplateColumns.put("9", "repeat(9, minmax(0, 1fr))");
      gridTemplateColumns.put("10", "repeat(10, minmax(0, 1fr))");
      gridTemplateColumns.put("11", "repeat(11, minmax(0, 1fr))");
      gridTemplateColumns.put("12", "repeat(12, minmax(0, 1fr))");
    }

    return gridTemplateColumns;
  }

  @Override
  final Map<String, String> height() {
    if (height == null) {
      height = new GrowableMap<>();

      height.putAll(spacing());
      height.put("auto", "auto");
      height.put("1/2", "50%");
      height.put("1/3", "33.333333%");
      height.put("2/3", "66.666667%");
      height.put("1/4", "25%");
      height.put("2/4", "50%");
      height.put("3/4", "75%");
      height.put("1/5", "20%");
      height.put("2/5", "40%");
      height.put("3/5", "60%");
      height.put("4/5", "80%");
      height.put("1/6", "16.666667%");
      height.put("2/6", "33.333333%");
      height.put("3/6", "50%");
      height.put("4/6", "66.666667%");
      height.put("5/6", "83.333333%");
      height.put("full", "100%");
      height.put("screen", "100vh");
      height.put("svh", "100svh");
      height.put("lvh", "100lvh");
      height.put("dvh", "100dvh");
      height.put("min", "min-content");
      height.put("max", "max-content");
      height.put("fit", "fit-content");
    }

    return height;
  }

  @Override
  final Map<String, String> inset() {
    if (inset == null) {
      inset = new GrowableMap<>();

      inset.putAll(spacing());
      inset.put("auto", "auto");
      inset.put("1/2", "50%");
      inset.put("1/3", "33.333333%");
      inset.put("2/3", "66.666667%");
      inset.put("1/4", "25%");
      inset.put("2/4", "50%");
      inset.put("3/4", "75%");
      inset.put("full", "100%");
    }

    return inset;
  }

  @Override
  final Map<String, String> letterSpacing() { return letterSpacing; }

  @Override
  final Map<String, String> lineHeight() { return lineHeight; }

  @Override
  final Map<String, String> margin() {
    if (margin == null) {
      margin = new GrowableMap<>();

      margin.putAll(spacing());
      margin.put("auto", "auto");
    }

    return margin;
  }

  @Override
  final Map<String, String> maxWidth() {
    if (maxWidth == null) {
      maxWidth = new GrowableMap<>();

      maxWidth.putAll(spacing());

      maxWidth.put("none", "none");
      maxWidth.put("xs", "20rem");
      maxWidth.put("sm", "24rem");
      maxWidth.put("md", "28rem");
      maxWidth.put("lg", "32rem");
      maxWidth.put("xl", "36rem");
      maxWidth.put("2xl", "42rem");
      maxWidth.put("3xl", "48rem");
      maxWidth.put("4xl", "56rem");
      maxWidth.put("5xl", "64rem");
      maxWidth.put("6xl", "72rem");
      maxWidth.put("7xl", "80rem");
      maxWidth.put("full", "100%");
      maxWidth.put("min", "min-content");
      maxWidth.put("max", "max-content");
      maxWidth.put("fit", "fit-content");
      maxWidth.put("prose", "65ch");

      for (var breakpoint : breakpoints) {
        String screen;
        screen = "screen-" + breakpoint.name();

        maxWidth.put(screen, breakpoint.value());
      }
    }

    return maxWidth;
  }

  @Override
  final Map<String, String> opacity() {
    if (opacity == null) {
      opacity = Map.ofEntries(
          Map.entry("0", "0"),
          Map.entry("5", "0.05"),
          Map.entry("10", "0.1"),
          Map.entry("15", "0.15"),
          Map.entry("20", "0.2"),
          Map.entry("25", "0.25"),
          Map.entry("30", "0.3"),
          Map.entry("35", "0.35"),
          Map.entry("40", "0.4"),
          Map.entry("45", "0.45"),
          Map.entry("50", "0.5"),
          Map.entry("55", "0.55"),
          Map.entry("60", "0.6"),
          Map.entry("65", "0.65"),
          Map.entry("70", "0.7"),
          Map.entry("75", "0.75"),
          Map.entry("80", "0.8"),
          Map.entry("85", "0.85"),
          Map.entry("90", "0.9"),
          Map.entry("95", "0.95"),
          Map.entry("100", "1")
      );
    }

    return opacity;
  }

  @Override
  final Map<String, String> outlineOffset() {
    if (outlineOffset == null) {
      outlineOffset = new GrowableMap<>();

      outlineOffset.put("0", "0px");
      outlineOffset.put("1", "1px");
      outlineOffset.put("2", "2px");
      outlineOffset.put("4", "4px");
      outlineOffset.put("8", "8px");
    }

    return outlineOffset;
  }

  @Override
  final Map<String, String> outlineWidth() {
    if (outlineWidth == null) {
      outlineWidth = new GrowableMap<>();

      outlineWidth.put("0", "0px");
      outlineWidth.put("1", "1px");
      outlineWidth.put("2", "2px");
      outlineWidth.put("4", "4px");
      outlineWidth.put("8", "8px");
    }

    return outlineWidth;
  }

  @Override
  final Map<String, String> padding() {
    if (padding == null) {
      padding = new GrowableMap<>();

      padding.putAll(spacing());
    }

    return padding;
  }

  @Override
  final Map<String, String> rules() {
    if (rules == null) {
      rules = Map.of();
    }

    return rules;
  }

  @Override
  final Map<String, String> transitionDuration() {
    if (transitionDuration == null) {
      transitionDuration = Map.ofEntries(
          Map.entry("0", "0s"),
          Map.entry("75", "75ms"),
          Map.entry("100", "100ms"),
          Map.entry("150", "150ms"),
          Map.entry("200", "200ms"),
          Map.entry("300", "300ms"),
          Map.entry("500", "500ms"),
          Map.entry("700", "700ms"),
          Map.entry("1000", "1000ms")
      );
    }

    return transitionDuration;
  }

  @Override
  final Map<String, String> transitionProperty() {
    if (transitionProperty == null) {
      transitionProperty = Map.of(
          "none", """
          transition-property: none;
          """,

          "all", """
          transition-property: all;
          transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
          transition-duration: 150ms;
          """,

          "", """
          transition-property: color, background-color, border-color, text-decoration-color, fill, stroke, opacity, box-shadow, transform, filter, backdrop-filter;
          transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
          transition-duration: 150ms;
          """,

          "colors", """
          transition-property: color, background-color, border-color, text-decoration-color, fill, stroke;
          transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
          transition-duration: 150ms;
          """,

          "opacity", """
          transition-property: opacity;
          transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
          transition-duration: 150ms;
          """,

          "shadow", """
          transition-property: box-shadow;
          transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
          transition-duration: 150ms;
          """,

          "transform", """
          transition-property: transform;
          transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
          transition-duration: 150ms;
          """
      );
    }

    return transitionProperty;
  }

  @Override
  final Map<String, String> utilities() {
    if (utilities == null) {
      utilities = Map.of();
    }

    return utilities;
  }

  private Map<String, Variant> variants() {
    if (variants == null) {
      variants = new GrowableMap<>();

      for (var breakpoint : breakpoints) {
        variants.put(breakpoint.name(), breakpoint);
      }

      variants.put("focus", new AppendTo(1, ":focus"));
      variants.put("hover", new AppendTo(2, ":hover"));
      variants.put("active", new AppendTo(3, ":active"));

      variants.put("after", new AppendTo(4, "::after"));
      variants.put("before", new AppendTo(5, "::before"));
    }

    return variants;
  }

  @Override
  final Map<String, String> width() {
    if (width == null) {
      width = new GrowableMap<>();

      width.putAll(spacing());

      width.put("auto", "auto");
      width.put("1/2", "50%");
      width.put("1/3", "33.333333%");
      width.put("2/3", "66.666667%");
      width.put("1/4", "25%");
      width.put("2/4", "50%");
      width.put("3/4", "75%");
      width.put("1/5", "20%");
      width.put("2/5", "40%");
      width.put("3/5", "60%");
      width.put("4/5", "80%");
      width.put("1/6", "16.666667%");
      width.put("2/6", "33.333333%");
      width.put("3/6", "50%");
      width.put("4/6", "66.666667%");
      width.put("5/6", "83.333333%");
      width.put("1/12", "8.333333%");
      width.put("2/12", "16.666667%");
      width.put("3/12", "25%");
      width.put("4/12", "33.333333%");
      width.put("5/12", "41.666667%");
      width.put("6/12", "50%");
      width.put("7/12", "58.333333%");
      width.put("8/12", "66.666667%");
      width.put("9/12", "75%");
      width.put("10/12", "83.333333%");
      width.put("11/12", "91.666667%");
      width.put("full", "100%");
      width.put("screen", "100vw");
      width.put("svw", "100svw");
      width.put("lvw", "100lvw");
      width.put("dvw", "100dvw");
      width.put("min", "min-content");
      width.put("max", "max-content");
      width.put("fit", "fit-content");
    }

    return width;
  }

  @Override
  final Map<String, String> zIndex() {
    if (zIndex == null) {
      zIndex = new GrowableMap<>();

      zIndex.put("0", "0");
      zIndex.put("10", "10");
      zIndex.put("20", "20");
      zIndex.put("30", "30");
      zIndex.put("40", "40");
      zIndex.put("50", "50");
      zIndex.put("auto", "auto");
    }

    return zIndex;
  }

  private Map<String, String> spacing() {
    if (spacing == null) {
      spacing = new GrowableMap<>();

      spacing.put("px", "1px");
      spacing.put("0", "0px");
      spacing.put("0.5", "0.125rem");
      spacing.put("1", "0.25rem");
      spacing.put("1.5", "0.375rem");
      spacing.put("2", "0.5rem");
      spacing.put("2.5", "0.625rem");
      spacing.put("3", "0.75rem");
      spacing.put("3.5", "0.875rem");
      spacing.put("4", "1rem");
      spacing.put("5", "1.25rem");
      spacing.put("6", "1.5rem");
      spacing.put("7", "1.75rem");
      spacing.put("8", "2rem");
      spacing.put("9", "2.25rem");
      spacing.put("10", "2.5rem");
      spacing.put("11", "2.75rem");
      spacing.put("12", "3rem");
      spacing.put("14", "3.5rem");
      spacing.put("16", "4rem");
      spacing.put("20", "5rem");
      spacing.put("24", "6rem");
      spacing.put("28", "7rem");
      spacing.put("32", "8rem");
      spacing.put("36", "9rem");
      spacing.put("40", "10rem");
      spacing.put("44", "11rem");
      spacing.put("48", "12rem");
      spacing.put("52", "13rem");
      spacing.put("56", "14rem");
      spacing.put("60", "15rem");
      spacing.put("64", "16rem");
      spacing.put("72", "18rem");
      spacing.put("80", "20rem");
      spacing.put("96", "24rem");
    }

    return spacing;
  }

}