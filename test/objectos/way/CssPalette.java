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

import java.util.Map;
import java.util.SequencedMap;

public class CssPalette {

  static final Map<String, String> DEFAULT;

  static {
    SequencedMap<String, String> colors;
    colors = Util.createSequencedMap();

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

    DEFAULT = Util.toUnmodifiableMap(colors);
  }

  private final Map<String, String> colors;

  private CssPalette(Map<String, String> colors) {
    this.colors = colors;
  }

  public static CssPalette defaultPalette() {
    return new CssPalette(DEFAULT);
  }

  final String get(String className) {
    return colors.get(className);
  }

}