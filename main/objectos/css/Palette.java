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

import java.util.Map;
import objectos.util.map.GrowableSequencedMap;

public class Palette {

  static final Map<String, String> DEFAULT;

  static {
    GrowableSequencedMap<String, String> b;
    b = new GrowableSequencedMap<>();

    b.put("inherit", "inherit");
    b.put("current", "currentColor");
    b.put("transparent", "transparent");

    b.put("black", "#000000");
    b.put("white", "#ffffff");

    b.put("slate-50", "#f8fafc");
    b.put("slate-100", "#f1f5f9");
    b.put("slate-200", "#e2e8f0");
    b.put("slate-300", "#cbd5e1");
    b.put("slate-400", "#94a3b8");
    b.put("slate-500", "#64748b");
    b.put("slate-600", "#475569");
    b.put("slate-700", "#334155");
    b.put("slate-800", "#1e293b");
    b.put("slate-900", "#0f172a");
    b.put("slate-950", "#020617");

    b.put("gray-50", "#f9fafb");
    b.put("gray-100", "#f3f4f6");
    b.put("gray-200", "#e5e7eb");
    b.put("gray-300", "#d1d5db");
    b.put("gray-400", "#9ca3af");
    b.put("gray-500", "#6b7280");
    b.put("gray-600", "#4b5563");
    b.put("gray-700", "#374151");
    b.put("gray-800", "#1f2937");
    b.put("gray-900", "#111827");
    b.put("gray-950", "#030712");

    b.put("zinc-50", "#fafafa");
    b.put("zinc-100", "#f4f4f5");
    b.put("zinc-200", "#e4e4e7");
    b.put("zinc-300", "#d4d4d8");
    b.put("zinc-400", "#a1a1aa");
    b.put("zinc-500", "#71717a");
    b.put("zinc-600", "#52525b");
    b.put("zinc-700", "#3f3f46");
    b.put("zinc-800", "#27272a");
    b.put("zinc-900", "#18181b");
    b.put("zinc-950", "#09090b");

    b.put("neutral-50", "#fafafa");
    b.put("neutral-100", "#f5f5f5");
    b.put("neutral-200", "#e5e5e5");
    b.put("neutral-300", "#d4d4d4");
    b.put("neutral-400", "#a3a3a3");
    b.put("neutral-500", "#737373");
    b.put("neutral-600", "#525252");
    b.put("neutral-700", "#404040");
    b.put("neutral-800", "#262626");
    b.put("neutral-900", "#171717");
    b.put("neutral-950", "#0a0a0a");

    b.put("stone-50", "#fafaf9");
    b.put("stone-100", "#f5f5f4");
    b.put("stone-200", "#e7e5e4");
    b.put("stone-300", "#d6d3d1");
    b.put("stone-400", "#a8a29e");
    b.put("stone-500", "#78716c");
    b.put("stone-600", "#57534e");
    b.put("stone-700", "#44403c");
    b.put("stone-800", "#292524");
    b.put("stone-900", "#1c1917");
    b.put("stone-950", "#0c0a09");

    b.put("red-50", "#fef2f2");
    b.put("red-100", "#fee2e2");
    b.put("red-200", "#fecaca");
    b.put("red-300", "#fca5a5");
    b.put("red-400", "#f87171");
    b.put("red-500", "#ef4444");
    b.put("red-600", "#dc2626");
    b.put("red-700", "#b91c1c");
    b.put("red-800", "#991b1b");
    b.put("red-900", "#7f1d1d");
    b.put("red-950", "#450a0a");

    b.put("orange-50", "#fff7ed");
    b.put("orange-100", "#ffedd5");
    b.put("orange-200", "#fed7aa");
    b.put("orange-300", "#fdba74");
    b.put("orange-400", "#fb923c");
    b.put("orange-500", "#f97316");
    b.put("orange-600", "#ea580c");
    b.put("orange-700", "#c2410c");
    b.put("orange-800", "#9a3412");
    b.put("orange-900", "#7c2d12");
    b.put("orange-950", "#431407");

    b.put("amber-50", "#fffbeb");
    b.put("amber-100", "#fef3c7");
    b.put("amber-200", "#fde68a");
    b.put("amber-300", "#fcd34d");
    b.put("amber-400", "#fbbf24");
    b.put("amber-500", "#f59e0b");
    b.put("amber-600", "#d97706");
    b.put("amber-700", "#b45309");
    b.put("amber-800", "#92400e");
    b.put("amber-900", "#78350f");
    b.put("amber-950", "#451a03");

    b.put("yellow-50", "#fefce8");
    b.put("yellow-100", "#fef9c3");
    b.put("yellow-200", "#fef08a");
    b.put("yellow-300", "#fde047");
    b.put("yellow-400", "#facc15");
    b.put("yellow-500", "#eab308");
    b.put("yellow-600", "#ca8a04");
    b.put("yellow-700", "#a16207");
    b.put("yellow-800", "#854d0e");
    b.put("yellow-900", "#713f12");
    b.put("yellow-950", "#422006");

    b.put("lime-50", "#f7fee7");
    b.put("lime-100", "#ecfccb");
    b.put("lime-200", "#d9f99d");
    b.put("lime-300", "#bef264");
    b.put("lime-400", "#a3e635");
    b.put("lime-500", "#84cc16");
    b.put("lime-600", "#65a30d");
    b.put("lime-700", "#4d7c0f");
    b.put("lime-800", "#3f6212");
    b.put("lime-900", "#365314");
    b.put("lime-950", "#1a2e05");

    b.put("green-50", "#f0fdf4");
    b.put("green-100", "#dcfce7");
    b.put("green-200", "#bbf7d0");
    b.put("green-300", "#86efac");
    b.put("green-400", "#4ade80");
    b.put("green-500", "#22c55e");
    b.put("green-600", "#16a34a");
    b.put("green-700", "#15803d");
    b.put("green-800", "#166534");
    b.put("green-900", "#14532d");
    b.put("green-950", "#052e16");

    b.put("emerald-50", "#ecfdf5");
    b.put("emerald-100", "#d1fae5");
    b.put("emerald-200", "#a7f3d0");
    b.put("emerald-300", "#6ee7b7");
    b.put("emerald-400", "#34d399");
    b.put("emerald-500", "#10b981");
    b.put("emerald-600", "#059669");
    b.put("emerald-700", "#047857");
    b.put("emerald-800", "#065f46");
    b.put("emerald-900", "#064e3b");
    b.put("emerald-950", "#022c22");

    b.put("teal-50", "#f0fdfa");
    b.put("teal-100", "#ccfbf1");
    b.put("teal-200", "#99f6e4");
    b.put("teal-300", "#5eead4");
    b.put("teal-400", "#2dd4bf");
    b.put("teal-500", "#14b8a6");
    b.put("teal-600", "#0d9488");
    b.put("teal-700", "#0f766e");
    b.put("teal-800", "#115e59");
    b.put("teal-900", "#134e4a");
    b.put("teal-950", "#042f2e");

    b.put("cyan-50", "#ecfeff");
    b.put("cyan-100", "#cffafe");
    b.put("cyan-200", "#a5f3fc");
    b.put("cyan-300", "#67e8f9");
    b.put("cyan-400", "#22d3ee");
    b.put("cyan-500", "#06b6d4");
    b.put("cyan-600", "#0891b2");
    b.put("cyan-700", "#0e7490");
    b.put("cyan-800", "#155e75");
    b.put("cyan-900", "#164e63");
    b.put("cyan-950", "#083344");

    b.put("sky-50", "#f0f9ff");
    b.put("sky-100", "#e0f2fe");
    b.put("sky-200", "#bae6fd");
    b.put("sky-300", "#7dd3fc");
    b.put("sky-400", "#38bdf8");
    b.put("sky-500", "#0ea5e9");
    b.put("sky-600", "#0284c7");
    b.put("sky-700", "#0369a1");
    b.put("sky-800", "#075985");
    b.put("sky-900", "#0c4a6e");
    b.put("sky-950", "#082f49");

    b.put("blue-50", "#eff6ff");
    b.put("blue-100", "#dbeafe");
    b.put("blue-200", "#bfdbfe");
    b.put("blue-300", "#93c5fd");
    b.put("blue-400", "#60a5fa");
    b.put("blue-500", "#3b82f6");
    b.put("blue-600", "#2563eb");
    b.put("blue-700", "#1d4ed8");
    b.put("blue-800", "#1e40af");
    b.put("blue-900", "#1e3a8a");
    b.put("blue-950", "#172554");

    b.put("indigo-50", "#eef2ff");
    b.put("indigo-100", "#e0e7ff");
    b.put("indigo-200", "#c7d2fe");
    b.put("indigo-300", "#a5b4fc");
    b.put("indigo-400", "#818cf8");
    b.put("indigo-500", "#6366f1");
    b.put("indigo-600", "#4f46e5");
    b.put("indigo-700", "#4338ca");
    b.put("indigo-800", "#3730a3");
    b.put("indigo-900", "#312e81");
    b.put("indigo-950", "#1e1b4b");

    b.put("violet-50", "#f5f3ff");
    b.put("violet-100", "#ede9fe");
    b.put("violet-200", "#ddd6fe");
    b.put("violet-300", "#c4b5fd");
    b.put("violet-400", "#a78bfa");
    b.put("violet-500", "#8b5cf6");
    b.put("violet-600", "#7c3aed");
    b.put("violet-700", "#6d28d9");
    b.put("violet-800", "#5b21b6");
    b.put("violet-900", "#4c1d95");
    b.put("violet-950", "#2e1065");

    b.put("purple-50", "#faf5ff");
    b.put("purple-100", "#f3e8ff");
    b.put("purple-200", "#e9d5ff");
    b.put("purple-300", "#d8b4fe");
    b.put("purple-400", "#c084fc");
    b.put("purple-500", "#a855f7");
    b.put("purple-600", "#9333ea");
    b.put("purple-700", "#7e22ce");
    b.put("purple-800", "#6b21a8");
    b.put("purple-900", "#581c87");
    b.put("purple-950", "#3b0764");

    b.put("fuchsia-50", "#fdf4ff");
    b.put("fuchsia-100", "#fae8ff");
    b.put("fuchsia-200", "#f5d0fe");
    b.put("fuchsia-300", "#f0abfc");
    b.put("fuchsia-400", "#e879f9");
    b.put("fuchsia-500", "#d946ef");
    b.put("fuchsia-600", "#c026d3");
    b.put("fuchsia-700", "#a21caf");
    b.put("fuchsia-800", "#86198f");
    b.put("fuchsia-900", "#701a75");
    b.put("fuchsia-950", "#4a044e");

    b.put("pink-50", "#fdf2f8");
    b.put("pink-100", "#fce7f3");
    b.put("pink-200", "#fbcfe8");
    b.put("pink-300", "#f9a8d4");
    b.put("pink-400", "#f472b6");
    b.put("pink-500", "#ec4899");
    b.put("pink-600", "#db2777");
    b.put("pink-700", "#be185d");
    b.put("pink-800", "#9d174d");
    b.put("pink-900", "#831843");
    b.put("pink-950", "#500724");

    b.put("rose-50", "#fff1f2");
    b.put("rose-100", "#ffe4e6");
    b.put("rose-200", "#fecdd3");
    b.put("rose-300", "#fda4af");
    b.put("rose-400", "#fb7185");
    b.put("rose-500", "#f43f5e");
    b.put("rose-600", "#e11d48");
    b.put("rose-700", "#be123c");
    b.put("rose-800", "#9f1239");
    b.put("rose-900", "#881337");
    b.put("rose-950", "#4c0519");

    DEFAULT = b.toUnmodifiableMap();
  }

  private final Map<String, String> colors;

  private Palette(Map<String, String> colors) {
    this.colors = colors;
  }

  public static Palette defaultPalette() {
    return new Palette(DEFAULT);
  }

  final String get(String className) {
    return colors.get(className);
  }

}