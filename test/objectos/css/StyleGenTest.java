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

import static org.testng.Assert.assertEquals;

import java.util.Set;
import objectos.html.HtmlTemplate;
import objectos.way.TestingNoteSink;
import org.testng.annotations.Test;

public class StyleGenTest {

  private static abstract class AbstractSubject extends HtmlTemplate {
    @Override
    protected final void definition() {
      div(
          include(this::classes)
      );
    }

    abstract void classes();
  }

  @Test
  public void alignItems() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("items-start");
        className("items-end");
        className("items-center");
        className("items-baseline");
        className("items-stretch");
      }
    }

    test(
        Subject.class,

        """
        .items-start { align-items: flex-start }
        .items-end { align-items: flex-end }
        .items-center { align-items: center }
        .items-baseline { align-items: baseline }
        .items-stretch { align-items: stretch }
        """
    );
  }

  @Test
  public void backgroundColor() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("bg-inherit bg-current bg-transparent bg-black bg-white");
        className("bg-slate-50 bg-slate-100 bg-slate-200 bg-slate-300 bg-slate-400 bg-slate-500 bg-slate-600 bg-slate-700 bg-slate-800 bg-slate-900 bg-slate-950");
        className("bg-gray-50 bg-gray-100 bg-gray-200 bg-gray-300 bg-gray-400 bg-gray-500 bg-gray-600 bg-gray-700 bg-gray-800 bg-gray-900 bg-gray-950");
        className("bg-zinc-50 bg-zinc-100 bg-zinc-200 bg-zinc-300 bg-zinc-400 bg-zinc-500 bg-zinc-600 bg-zinc-700 bg-zinc-800 bg-zinc-900 bg-zinc-950");
        className("bg-neutral-50 bg-neutral-100 bg-neutral-200 bg-neutral-300 bg-neutral-400 bg-neutral-500 bg-neutral-600 bg-neutral-700 bg-neutral-800 bg-neutral-900 bg-neutral-950");
        className("bg-stone-50 bg-stone-100 bg-stone-200 bg-stone-300 bg-stone-400 bg-stone-500 bg-stone-600 bg-stone-700 bg-stone-800 bg-stone-900 bg-stone-950");
        className("bg-red-50 bg-red-100 bg-red-200 bg-red-300 bg-red-400 bg-red-500 bg-red-600 bg-red-700 bg-red-800 bg-red-900 bg-red-950");
        className("bg-orange-50 bg-orange-100 bg-orange-200 bg-orange-300 bg-orange-400 bg-orange-500 bg-orange-600 bg-orange-700 bg-orange-800 bg-orange-900 bg-orange-950");
        className("bg-amber-50 bg-amber-100 bg-amber-200 bg-amber-300 bg-amber-400 bg-amber-500 bg-amber-600 bg-amber-700 bg-amber-800 bg-amber-900 bg-amber-950");
        className("bg-yellow-50 bg-yellow-100 bg-yellow-200 bg-yellow-300 bg-yellow-400 bg-yellow-500 bg-yellow-600 bg-yellow-700 bg-yellow-800 bg-yellow-900 bg-yellow-950");
        className("bg-lime-50 bg-lime-100 bg-lime-200 bg-lime-300 bg-lime-400 bg-lime-500 bg-lime-600 bg-lime-700 bg-lime-800 bg-lime-900 bg-lime-950");
        className("bg-green-50 bg-green-100 bg-green-200 bg-green-300 bg-green-400 bg-green-500 bg-green-600 bg-green-700 bg-green-800 bg-green-900 bg-green-950");
        className("bg-emerald-50 bg-emerald-100 bg-emerald-200 bg-emerald-300 bg-emerald-400 bg-emerald-500 bg-emerald-600 bg-emerald-700 bg-emerald-800 bg-emerald-900 bg-emerald-950");
        className("bg-teal-50 bg-teal-100 bg-teal-200 bg-teal-300 bg-teal-400 bg-teal-500 bg-teal-600 bg-teal-700 bg-teal-800 bg-teal-900 bg-teal-950");
        className("bg-cyan-50 bg-cyan-100 bg-cyan-200 bg-cyan-300 bg-cyan-400 bg-cyan-500 bg-cyan-600 bg-cyan-700 bg-cyan-800 bg-cyan-900 bg-cyan-950");
        className("bg-sky-50 bg-sky-100 bg-sky-200 bg-sky-300 bg-sky-400 bg-sky-500 bg-sky-600 bg-sky-700 bg-sky-800 bg-sky-900 bg-sky-950");
        className("bg-blue-50 bg-blue-100 bg-blue-200 bg-blue-300 bg-blue-400 bg-blue-500 bg-blue-600 bg-blue-700 bg-blue-800 bg-blue-900 bg-blue-950");
        className("bg-indigo-50 bg-indigo-100 bg-indigo-200 bg-indigo-300 bg-indigo-400 bg-indigo-500 bg-indigo-600 bg-indigo-700 bg-indigo-800 bg-indigo-900 bg-indigo-950");
        className("bg-violet-50 bg-violet-100 bg-violet-200 bg-violet-300 bg-violet-400 bg-violet-500 bg-violet-600 bg-violet-700 bg-violet-800 bg-violet-900 bg-violet-950");
        className("bg-purple-50 bg-purple-100 bg-purple-200 bg-purple-300 bg-purple-400 bg-purple-500 bg-purple-600 bg-purple-700 bg-purple-800 bg-purple-900 bg-purple-950");
        className("bg-fuchsia-50 bg-fuchsia-100 bg-fuchsia-200 bg-fuchsia-300 bg-fuchsia-400 bg-fuchsia-500 bg-fuchsia-600 bg-fuchsia-700 bg-fuchsia-800 bg-fuchsia-900 bg-fuchsia-950");
        className("bg-pink-50 bg-pink-100 bg-pink-200 bg-pink-300 bg-pink-400 bg-pink-500 bg-pink-600 bg-pink-700 bg-pink-800 bg-pink-900 bg-pink-950");
        className("bg-rose-50 bg-rose-100 bg-rose-200 bg-rose-300 bg-rose-400 bg-rose-500 bg-rose-600 bg-rose-700 bg-rose-800 bg-rose-900 bg-rose-950");
      }
    }

    test(
        Subject.class,

        """
        .bg-inherit { background-color: inherit }
        .bg-current { background-color: currentColor }
        .bg-transparent { background-color: transparent }
        .bg-black { background-color: #000000 }
        .bg-white { background-color: #ffffff }
        .bg-slate-50 { background-color: #f8fafc }
        .bg-slate-100 { background-color: #f1f5f9 }
        .bg-slate-200 { background-color: #e2e8f0 }
        .bg-slate-300 { background-color: #cbd5e1 }
        .bg-slate-400 { background-color: #94a3b8 }
        .bg-slate-500 { background-color: #64748b }
        .bg-slate-600 { background-color: #475569 }
        .bg-slate-700 { background-color: #334155 }
        .bg-slate-800 { background-color: #1e293b }
        .bg-slate-900 { background-color: #0f172a }
        .bg-slate-950 { background-color: #020617 }
        .bg-gray-50 { background-color: #f9fafb }
        .bg-gray-100 { background-color: #f3f4f6 }
        .bg-gray-200 { background-color: #e5e7eb }
        .bg-gray-300 { background-color: #d1d5db }
        .bg-gray-400 { background-color: #9ca3af }
        .bg-gray-500 { background-color: #6b7280 }
        .bg-gray-600 { background-color: #4b5563 }
        .bg-gray-700 { background-color: #374151 }
        .bg-gray-800 { background-color: #1f2937 }
        .bg-gray-900 { background-color: #111827 }
        .bg-gray-950 { background-color: #030712 }
        .bg-zinc-50 { background-color: #fafafa }
        .bg-zinc-100 { background-color: #f4f4f5 }
        .bg-zinc-200 { background-color: #e4e4e7 }
        .bg-zinc-300 { background-color: #d4d4d8 }
        .bg-zinc-400 { background-color: #a1a1aa }
        .bg-zinc-500 { background-color: #71717a }
        .bg-zinc-600 { background-color: #52525b }
        .bg-zinc-700 { background-color: #3f3f46 }
        .bg-zinc-800 { background-color: #27272a }
        .bg-zinc-900 { background-color: #18181b }
        .bg-zinc-950 { background-color: #09090b }
        .bg-neutral-50 { background-color: #fafafa }
        .bg-neutral-100 { background-color: #f5f5f5 }
        .bg-neutral-200 { background-color: #e5e5e5 }
        .bg-neutral-300 { background-color: #d4d4d4 }
        .bg-neutral-400 { background-color: #a3a3a3 }
        .bg-neutral-500 { background-color: #737373 }
        .bg-neutral-600 { background-color: #525252 }
        .bg-neutral-700 { background-color: #404040 }
        .bg-neutral-800 { background-color: #262626 }
        .bg-neutral-900 { background-color: #171717 }
        .bg-neutral-950 { background-color: #0a0a0a }
        .bg-stone-50 { background-color: #fafaf9 }
        .bg-stone-100 { background-color: #f5f5f4 }
        .bg-stone-200 { background-color: #e7e5e4 }
        .bg-stone-300 { background-color: #d6d3d1 }
        .bg-stone-400 { background-color: #a8a29e }
        .bg-stone-500 { background-color: #78716c }
        .bg-stone-600 { background-color: #57534e }
        .bg-stone-700 { background-color: #44403c }
        .bg-stone-800 { background-color: #292524 }
        .bg-stone-900 { background-color: #1c1917 }
        .bg-stone-950 { background-color: #0c0a09 }
        .bg-red-50 { background-color: #fef2f2 }
        .bg-red-100 { background-color: #fee2e2 }
        .bg-red-200 { background-color: #fecaca }
        .bg-red-300 { background-color: #fca5a5 }
        .bg-red-400 { background-color: #f87171 }
        .bg-red-500 { background-color: #ef4444 }
        .bg-red-600 { background-color: #dc2626 }
        .bg-red-700 { background-color: #b91c1c }
        .bg-red-800 { background-color: #991b1b }
        .bg-red-900 { background-color: #7f1d1d }
        .bg-red-950 { background-color: #450a0a }
        .bg-orange-50 { background-color: #fff7ed }
        .bg-orange-100 { background-color: #ffedd5 }
        .bg-orange-200 { background-color: #fed7aa }
        .bg-orange-300 { background-color: #fdba74 }
        .bg-orange-400 { background-color: #fb923c }
        .bg-orange-500 { background-color: #f97316 }
        .bg-orange-600 { background-color: #ea580c }
        .bg-orange-700 { background-color: #c2410c }
        .bg-orange-800 { background-color: #9a3412 }
        .bg-orange-900 { background-color: #7c2d12 }
        .bg-orange-950 { background-color: #431407 }
        .bg-amber-50 { background-color: #fffbeb }
        .bg-amber-100 { background-color: #fef3c7 }
        .bg-amber-200 { background-color: #fde68a }
        .bg-amber-300 { background-color: #fcd34d }
        .bg-amber-400 { background-color: #fbbf24 }
        .bg-amber-500 { background-color: #f59e0b }
        .bg-amber-600 { background-color: #d97706 }
        .bg-amber-700 { background-color: #b45309 }
        .bg-amber-800 { background-color: #92400e }
        .bg-amber-900 { background-color: #78350f }
        .bg-amber-950 { background-color: #451a03 }
        .bg-yellow-50 { background-color: #fefce8 }
        .bg-yellow-100 { background-color: #fef9c3 }
        .bg-yellow-200 { background-color: #fef08a }
        .bg-yellow-300 { background-color: #fde047 }
        .bg-yellow-400 { background-color: #facc15 }
        .bg-yellow-500 { background-color: #eab308 }
        .bg-yellow-600 { background-color: #ca8a04 }
        .bg-yellow-700 { background-color: #a16207 }
        .bg-yellow-800 { background-color: #854d0e }
        .bg-yellow-900 { background-color: #713f12 }
        .bg-yellow-950 { background-color: #422006 }
        .bg-lime-50 { background-color: #f7fee7 }
        .bg-lime-100 { background-color: #ecfccb }
        .bg-lime-200 { background-color: #d9f99d }
        .bg-lime-300 { background-color: #bef264 }
        .bg-lime-400 { background-color: #a3e635 }
        .bg-lime-500 { background-color: #84cc16 }
        .bg-lime-600 { background-color: #65a30d }
        .bg-lime-700 { background-color: #4d7c0f }
        .bg-lime-800 { background-color: #3f6212 }
        .bg-lime-900 { background-color: #365314 }
        .bg-lime-950 { background-color: #1a2e05 }
        .bg-green-50 { background-color: #f0fdf4 }
        .bg-green-100 { background-color: #dcfce7 }
        .bg-green-200 { background-color: #bbf7d0 }
        .bg-green-300 { background-color: #86efac }
        .bg-green-400 { background-color: #4ade80 }
        .bg-green-500 { background-color: #22c55e }
        .bg-green-600 { background-color: #16a34a }
        .bg-green-700 { background-color: #15803d }
        .bg-green-800 { background-color: #166534 }
        .bg-green-900 { background-color: #14532d }
        .bg-green-950 { background-color: #052e16 }
        .bg-emerald-50 { background-color: #ecfdf5 }
        .bg-emerald-100 { background-color: #d1fae5 }
        .bg-emerald-200 { background-color: #a7f3d0 }
        .bg-emerald-300 { background-color: #6ee7b7 }
        .bg-emerald-400 { background-color: #34d399 }
        .bg-emerald-500 { background-color: #10b981 }
        .bg-emerald-600 { background-color: #059669 }
        .bg-emerald-700 { background-color: #047857 }
        .bg-emerald-800 { background-color: #065f46 }
        .bg-emerald-900 { background-color: #064e3b }
        .bg-emerald-950 { background-color: #022c22 }
        .bg-teal-50 { background-color: #f0fdfa }
        .bg-teal-100 { background-color: #ccfbf1 }
        .bg-teal-200 { background-color: #99f6e4 }
        .bg-teal-300 { background-color: #5eead4 }
        .bg-teal-400 { background-color: #2dd4bf }
        .bg-teal-500 { background-color: #14b8a6 }
        .bg-teal-600 { background-color: #0d9488 }
        .bg-teal-700 { background-color: #0f766e }
        .bg-teal-800 { background-color: #115e59 }
        .bg-teal-900 { background-color: #134e4a }
        .bg-teal-950 { background-color: #042f2e }
        .bg-cyan-50 { background-color: #ecfeff }
        .bg-cyan-100 { background-color: #cffafe }
        .bg-cyan-200 { background-color: #a5f3fc }
        .bg-cyan-300 { background-color: #67e8f9 }
        .bg-cyan-400 { background-color: #22d3ee }
        .bg-cyan-500 { background-color: #06b6d4 }
        .bg-cyan-600 { background-color: #0891b2 }
        .bg-cyan-700 { background-color: #0e7490 }
        .bg-cyan-800 { background-color: #155e75 }
        .bg-cyan-900 { background-color: #164e63 }
        .bg-cyan-950 { background-color: #083344 }
        .bg-sky-50 { background-color: #f0f9ff }
        .bg-sky-100 { background-color: #e0f2fe }
        .bg-sky-200 { background-color: #bae6fd }
        .bg-sky-300 { background-color: #7dd3fc }
        .bg-sky-400 { background-color: #38bdf8 }
        .bg-sky-500 { background-color: #0ea5e9 }
        .bg-sky-600 { background-color: #0284c7 }
        .bg-sky-700 { background-color: #0369a1 }
        .bg-sky-800 { background-color: #075985 }
        .bg-sky-900 { background-color: #0c4a6e }
        .bg-sky-950 { background-color: #082f49 }
        .bg-blue-50 { background-color: #eff6ff }
        .bg-blue-100 { background-color: #dbeafe }
        .bg-blue-200 { background-color: #bfdbfe }
        .bg-blue-300 { background-color: #93c5fd }
        .bg-blue-400 { background-color: #60a5fa }
        .bg-blue-500 { background-color: #3b82f6 }
        .bg-blue-600 { background-color: #2563eb }
        .bg-blue-700 { background-color: #1d4ed8 }
        .bg-blue-800 { background-color: #1e40af }
        .bg-blue-900 { background-color: #1e3a8a }
        .bg-blue-950 { background-color: #172554 }
        .bg-indigo-50 { background-color: #eef2ff }
        .bg-indigo-100 { background-color: #e0e7ff }
        .bg-indigo-200 { background-color: #c7d2fe }
        .bg-indigo-300 { background-color: #a5b4fc }
        .bg-indigo-400 { background-color: #818cf8 }
        .bg-indigo-500 { background-color: #6366f1 }
        .bg-indigo-600 { background-color: #4f46e5 }
        .bg-indigo-700 { background-color: #4338ca }
        .bg-indigo-800 { background-color: #3730a3 }
        .bg-indigo-900 { background-color: #312e81 }
        .bg-indigo-950 { background-color: #1e1b4b }
        .bg-violet-50 { background-color: #f5f3ff }
        .bg-violet-100 { background-color: #ede9fe }
        .bg-violet-200 { background-color: #ddd6fe }
        .bg-violet-300 { background-color: #c4b5fd }
        .bg-violet-400 { background-color: #a78bfa }
        .bg-violet-500 { background-color: #8b5cf6 }
        .bg-violet-600 { background-color: #7c3aed }
        .bg-violet-700 { background-color: #6d28d9 }
        .bg-violet-800 { background-color: #5b21b6 }
        .bg-violet-900 { background-color: #4c1d95 }
        .bg-violet-950 { background-color: #2e1065 }
        .bg-purple-50 { background-color: #faf5ff }
        .bg-purple-100 { background-color: #f3e8ff }
        .bg-purple-200 { background-color: #e9d5ff }
        .bg-purple-300 { background-color: #d8b4fe }
        .bg-purple-400 { background-color: #c084fc }
        .bg-purple-500 { background-color: #a855f7 }
        .bg-purple-600 { background-color: #9333ea }
        .bg-purple-700 { background-color: #7e22ce }
        .bg-purple-800 { background-color: #6b21a8 }
        .bg-purple-900 { background-color: #581c87 }
        .bg-purple-950 { background-color: #3b0764 }
        .bg-fuchsia-50 { background-color: #fdf4ff }
        .bg-fuchsia-100 { background-color: #fae8ff }
        .bg-fuchsia-200 { background-color: #f5d0fe }
        .bg-fuchsia-300 { background-color: #f0abfc }
        .bg-fuchsia-400 { background-color: #e879f9 }
        .bg-fuchsia-500 { background-color: #d946ef }
        .bg-fuchsia-600 { background-color: #c026d3 }
        .bg-fuchsia-700 { background-color: #a21caf }
        .bg-fuchsia-800 { background-color: #86198f }
        .bg-fuchsia-900 { background-color: #701a75 }
        .bg-fuchsia-950 { background-color: #4a044e }
        .bg-pink-50 { background-color: #fdf2f8 }
        .bg-pink-100 { background-color: #fce7f3 }
        .bg-pink-200 { background-color: #fbcfe8 }
        .bg-pink-300 { background-color: #f9a8d4 }
        .bg-pink-400 { background-color: #f472b6 }
        .bg-pink-500 { background-color: #ec4899 }
        .bg-pink-600 { background-color: #db2777 }
        .bg-pink-700 { background-color: #be185d }
        .bg-pink-800 { background-color: #9d174d }
        .bg-pink-900 { background-color: #831843 }
        .bg-pink-950 { background-color: #500724 }
        .bg-rose-50 { background-color: #fff1f2 }
        .bg-rose-100 { background-color: #ffe4e6 }
        .bg-rose-200 { background-color: #fecdd3 }
        .bg-rose-300 { background-color: #fda4af }
        .bg-rose-400 { background-color: #fb7185 }
        .bg-rose-500 { background-color: #f43f5e }
        .bg-rose-600 { background-color: #e11d48 }
        .bg-rose-700 { background-color: #be123c }
        .bg-rose-800 { background-color: #9f1239 }
        .bg-rose-900 { background-color: #881337 }
        .bg-rose-950 { background-color: #4c0519 }
        """
    );
  }

  @Test
  public void borderColor() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        // @formatter:off
        className("border-inherit border-current border-transparent border-black border-white");
        className("border-slate-50 border-slate-100 border-slate-200 border-slate-300 border-slate-400 border-slate-500 border-slate-600 border-slate-700 border-slate-800 border-slate-900 border-slate-950");
        className("border-gray-50 border-gray-100 border-gray-200 border-gray-300 border-gray-400 border-gray-500 border-gray-600 border-gray-700 border-gray-800 border-gray-900 border-gray-950");
        className("border-zinc-50 border-zinc-100 border-zinc-200 border-zinc-300 border-zinc-400 border-zinc-500 border-zinc-600 border-zinc-700 border-zinc-800 border-zinc-900 border-zinc-950");
        className("border-neutral-50 border-neutral-100 border-neutral-200 border-neutral-300 border-neutral-400 border-neutral-500 border-neutral-600 border-neutral-700 border-neutral-800 border-neutral-900 border-neutral-950");
        className("border-stone-50 border-stone-100 border-stone-200 border-stone-300 border-stone-400 border-stone-500 border-stone-600 border-stone-700 border-stone-800 border-stone-900 border-stone-950");
        className("border-red-50 border-red-100 border-red-200 border-red-300 border-red-400 border-red-500 border-red-600 border-red-700 border-red-800 border-red-900 border-red-950");
        className("border-orange-50 border-orange-100 border-orange-200 border-orange-300 border-orange-400 border-orange-500 border-orange-600 border-orange-700 border-orange-800 border-orange-900 border-orange-950");
        className("border-amber-50 border-amber-100 border-amber-200 border-amber-300 border-amber-400 border-amber-500 border-amber-600 border-amber-700 border-amber-800 border-amber-900 border-amber-950");
        className("border-yellow-50 border-yellow-100 border-yellow-200 border-yellow-300 border-yellow-400 border-yellow-500 border-yellow-600 border-yellow-700 border-yellow-800 border-yellow-900 border-yellow-950");
        className("border-lime-50 border-lime-100 border-lime-200 border-lime-300 border-lime-400 border-lime-500 border-lime-600 border-lime-700 border-lime-800 border-lime-900 border-lime-950");
        className("border-green-50 border-green-100 border-green-200 border-green-300 border-green-400 border-green-500 border-green-600 border-green-700 border-green-800 border-green-900 border-green-950");
        className("border-emerald-50 border-emerald-100 border-emerald-200 border-emerald-300 border-emerald-400 border-emerald-500 border-emerald-600 border-emerald-700 border-emerald-800 border-emerald-900 border-emerald-950");
        className("border-teal-50 border-teal-100 border-teal-200 border-teal-300 border-teal-400 border-teal-500 border-teal-600 border-teal-700 border-teal-800 border-teal-900 border-teal-950");
        className("border-cyan-50 border-cyan-100 border-cyan-200 border-cyan-300 border-cyan-400 border-cyan-500 border-cyan-600 border-cyan-700 border-cyan-800 border-cyan-900 border-cyan-950");
        className("border-sky-50 border-sky-100 border-sky-200 border-sky-300 border-sky-400 border-sky-500 border-sky-600 border-sky-700 border-sky-800 border-sky-900 border-sky-950");
        className("border-blue-50 border-blue-100 border-blue-200 border-blue-300 border-blue-400 border-blue-500 border-blue-600 border-blue-700 border-blue-800 border-blue-900 border-blue-950");
        className("border-indigo-50 border-indigo-100 border-indigo-200 border-indigo-300 border-indigo-400 border-indigo-500 border-indigo-600 border-indigo-700 border-indigo-800 border-indigo-900 border-indigo-950");
        className("border-violet-50 border-violet-100 border-violet-200 border-violet-300 border-violet-400 border-violet-500 border-violet-600 border-violet-700 border-violet-800 border-violet-900 border-violet-950");
        className("border-purple-50 border-purple-100 border-purple-200 border-purple-300 border-purple-400 border-purple-500 border-purple-600 border-purple-700 border-purple-800 border-purple-900 border-purple-950");
        className("border-fuchsia-50 border-fuchsia-100 border-fuchsia-200 border-fuchsia-300 border-fuchsia-400 border-fuchsia-500 border-fuchsia-600 border-fuchsia-700 border-fuchsia-800 border-fuchsia-900 border-fuchsia-950");
        className("border-pink-50 border-pink-100 border-pink-200 border-pink-300 border-pink-400 border-pink-500 border-pink-600 border-pink-700 border-pink-800 border-pink-900 border-pink-950");
        className("border-rose-50 border-rose-100 border-rose-200 border-rose-300 border-rose-400 border-rose-500 border-rose-600 border-rose-700 border-rose-800 border-rose-900 border-rose-950");
        // @formatter:on
      }
    }

    test(
        Subject.class,

        """
        .border-inherit { border-color: inherit }
        .border-current { border-color: currentColor }
        .border-transparent { border-color: transparent }
        .border-black { border-color: #000000 }
        .border-white { border-color: #ffffff }
        .border-slate-50 { border-color: #f8fafc }
        .border-slate-100 { border-color: #f1f5f9 }
        .border-slate-200 { border-color: #e2e8f0 }
        .border-slate-300 { border-color: #cbd5e1 }
        .border-slate-400 { border-color: #94a3b8 }
        .border-slate-500 { border-color: #64748b }
        .border-slate-600 { border-color: #475569 }
        .border-slate-700 { border-color: #334155 }
        .border-slate-800 { border-color: #1e293b }
        .border-slate-900 { border-color: #0f172a }
        .border-slate-950 { border-color: #020617 }
        .border-gray-50 { border-color: #f9fafb }
        .border-gray-100 { border-color: #f3f4f6 }
        .border-gray-200 { border-color: #e5e7eb }
        .border-gray-300 { border-color: #d1d5db }
        .border-gray-400 { border-color: #9ca3af }
        .border-gray-500 { border-color: #6b7280 }
        .border-gray-600 { border-color: #4b5563 }
        .border-gray-700 { border-color: #374151 }
        .border-gray-800 { border-color: #1f2937 }
        .border-gray-900 { border-color: #111827 }
        .border-gray-950 { border-color: #030712 }
        .border-zinc-50 { border-color: #fafafa }
        .border-zinc-100 { border-color: #f4f4f5 }
        .border-zinc-200 { border-color: #e4e4e7 }
        .border-zinc-300 { border-color: #d4d4d8 }
        .border-zinc-400 { border-color: #a1a1aa }
        .border-zinc-500 { border-color: #71717a }
        .border-zinc-600 { border-color: #52525b }
        .border-zinc-700 { border-color: #3f3f46 }
        .border-zinc-800 { border-color: #27272a }
        .border-zinc-900 { border-color: #18181b }
        .border-zinc-950 { border-color: #09090b }
        .border-neutral-50 { border-color: #fafafa }
        .border-neutral-100 { border-color: #f5f5f5 }
        .border-neutral-200 { border-color: #e5e5e5 }
        .border-neutral-300 { border-color: #d4d4d4 }
        .border-neutral-400 { border-color: #a3a3a3 }
        .border-neutral-500 { border-color: #737373 }
        .border-neutral-600 { border-color: #525252 }
        .border-neutral-700 { border-color: #404040 }
        .border-neutral-800 { border-color: #262626 }
        .border-neutral-900 { border-color: #171717 }
        .border-neutral-950 { border-color: #0a0a0a }
        .border-stone-50 { border-color: #fafaf9 }
        .border-stone-100 { border-color: #f5f5f4 }
        .border-stone-200 { border-color: #e7e5e4 }
        .border-stone-300 { border-color: #d6d3d1 }
        .border-stone-400 { border-color: #a8a29e }
        .border-stone-500 { border-color: #78716c }
        .border-stone-600 { border-color: #57534e }
        .border-stone-700 { border-color: #44403c }
        .border-stone-800 { border-color: #292524 }
        .border-stone-900 { border-color: #1c1917 }
        .border-stone-950 { border-color: #0c0a09 }
        .border-red-50 { border-color: #fef2f2 }
        .border-red-100 { border-color: #fee2e2 }
        .border-red-200 { border-color: #fecaca }
        .border-red-300 { border-color: #fca5a5 }
        .border-red-400 { border-color: #f87171 }
        .border-red-500 { border-color: #ef4444 }
        .border-red-600 { border-color: #dc2626 }
        .border-red-700 { border-color: #b91c1c }
        .border-red-800 { border-color: #991b1b }
        .border-red-900 { border-color: #7f1d1d }
        .border-red-950 { border-color: #450a0a }
        .border-orange-50 { border-color: #fff7ed }
        .border-orange-100 { border-color: #ffedd5 }
        .border-orange-200 { border-color: #fed7aa }
        .border-orange-300 { border-color: #fdba74 }
        .border-orange-400 { border-color: #fb923c }
        .border-orange-500 { border-color: #f97316 }
        .border-orange-600 { border-color: #ea580c }
        .border-orange-700 { border-color: #c2410c }
        .border-orange-800 { border-color: #9a3412 }
        .border-orange-900 { border-color: #7c2d12 }
        .border-orange-950 { border-color: #431407 }
        .border-amber-50 { border-color: #fffbeb }
        .border-amber-100 { border-color: #fef3c7 }
        .border-amber-200 { border-color: #fde68a }
        .border-amber-300 { border-color: #fcd34d }
        .border-amber-400 { border-color: #fbbf24 }
        .border-amber-500 { border-color: #f59e0b }
        .border-amber-600 { border-color: #d97706 }
        .border-amber-700 { border-color: #b45309 }
        .border-amber-800 { border-color: #92400e }
        .border-amber-900 { border-color: #78350f }
        .border-amber-950 { border-color: #451a03 }
        .border-yellow-50 { border-color: #fefce8 }
        .border-yellow-100 { border-color: #fef9c3 }
        .border-yellow-200 { border-color: #fef08a }
        .border-yellow-300 { border-color: #fde047 }
        .border-yellow-400 { border-color: #facc15 }
        .border-yellow-500 { border-color: #eab308 }
        .border-yellow-600 { border-color: #ca8a04 }
        .border-yellow-700 { border-color: #a16207 }
        .border-yellow-800 { border-color: #854d0e }
        .border-yellow-900 { border-color: #713f12 }
        .border-yellow-950 { border-color: #422006 }
        .border-lime-50 { border-color: #f7fee7 }
        .border-lime-100 { border-color: #ecfccb }
        .border-lime-200 { border-color: #d9f99d }
        .border-lime-300 { border-color: #bef264 }
        .border-lime-400 { border-color: #a3e635 }
        .border-lime-500 { border-color: #84cc16 }
        .border-lime-600 { border-color: #65a30d }
        .border-lime-700 { border-color: #4d7c0f }
        .border-lime-800 { border-color: #3f6212 }
        .border-lime-900 { border-color: #365314 }
        .border-lime-950 { border-color: #1a2e05 }
        .border-green-50 { border-color: #f0fdf4 }
        .border-green-100 { border-color: #dcfce7 }
        .border-green-200 { border-color: #bbf7d0 }
        .border-green-300 { border-color: #86efac }
        .border-green-400 { border-color: #4ade80 }
        .border-green-500 { border-color: #22c55e }
        .border-green-600 { border-color: #16a34a }
        .border-green-700 { border-color: #15803d }
        .border-green-800 { border-color: #166534 }
        .border-green-900 { border-color: #14532d }
        .border-green-950 { border-color: #052e16 }
        .border-emerald-50 { border-color: #ecfdf5 }
        .border-emerald-100 { border-color: #d1fae5 }
        .border-emerald-200 { border-color: #a7f3d0 }
        .border-emerald-300 { border-color: #6ee7b7 }
        .border-emerald-400 { border-color: #34d399 }
        .border-emerald-500 { border-color: #10b981 }
        .border-emerald-600 { border-color: #059669 }
        .border-emerald-700 { border-color: #047857 }
        .border-emerald-800 { border-color: #065f46 }
        .border-emerald-900 { border-color: #064e3b }
        .border-emerald-950 { border-color: #022c22 }
        .border-teal-50 { border-color: #f0fdfa }
        .border-teal-100 { border-color: #ccfbf1 }
        .border-teal-200 { border-color: #99f6e4 }
        .border-teal-300 { border-color: #5eead4 }
        .border-teal-400 { border-color: #2dd4bf }
        .border-teal-500 { border-color: #14b8a6 }
        .border-teal-600 { border-color: #0d9488 }
        .border-teal-700 { border-color: #0f766e }
        .border-teal-800 { border-color: #115e59 }
        .border-teal-900 { border-color: #134e4a }
        .border-teal-950 { border-color: #042f2e }
        .border-cyan-50 { border-color: #ecfeff }
        .border-cyan-100 { border-color: #cffafe }
        .border-cyan-200 { border-color: #a5f3fc }
        .border-cyan-300 { border-color: #67e8f9 }
        .border-cyan-400 { border-color: #22d3ee }
        .border-cyan-500 { border-color: #06b6d4 }
        .border-cyan-600 { border-color: #0891b2 }
        .border-cyan-700 { border-color: #0e7490 }
        .border-cyan-800 { border-color: #155e75 }
        .border-cyan-900 { border-color: #164e63 }
        .border-cyan-950 { border-color: #083344 }
        .border-sky-50 { border-color: #f0f9ff }
        .border-sky-100 { border-color: #e0f2fe }
        .border-sky-200 { border-color: #bae6fd }
        .border-sky-300 { border-color: #7dd3fc }
        .border-sky-400 { border-color: #38bdf8 }
        .border-sky-500 { border-color: #0ea5e9 }
        .border-sky-600 { border-color: #0284c7 }
        .border-sky-700 { border-color: #0369a1 }
        .border-sky-800 { border-color: #075985 }
        .border-sky-900 { border-color: #0c4a6e }
        .border-sky-950 { border-color: #082f49 }
        .border-blue-50 { border-color: #eff6ff }
        .border-blue-100 { border-color: #dbeafe }
        .border-blue-200 { border-color: #bfdbfe }
        .border-blue-300 { border-color: #93c5fd }
        .border-blue-400 { border-color: #60a5fa }
        .border-blue-500 { border-color: #3b82f6 }
        .border-blue-600 { border-color: #2563eb }
        .border-blue-700 { border-color: #1d4ed8 }
        .border-blue-800 { border-color: #1e40af }
        .border-blue-900 { border-color: #1e3a8a }
        .border-blue-950 { border-color: #172554 }
        .border-indigo-50 { border-color: #eef2ff }
        .border-indigo-100 { border-color: #e0e7ff }
        .border-indigo-200 { border-color: #c7d2fe }
        .border-indigo-300 { border-color: #a5b4fc }
        .border-indigo-400 { border-color: #818cf8 }
        .border-indigo-500 { border-color: #6366f1 }
        .border-indigo-600 { border-color: #4f46e5 }
        .border-indigo-700 { border-color: #4338ca }
        .border-indigo-800 { border-color: #3730a3 }
        .border-indigo-900 { border-color: #312e81 }
        .border-indigo-950 { border-color: #1e1b4b }
        .border-violet-50 { border-color: #f5f3ff }
        .border-violet-100 { border-color: #ede9fe }
        .border-violet-200 { border-color: #ddd6fe }
        .border-violet-300 { border-color: #c4b5fd }
        .border-violet-400 { border-color: #a78bfa }
        .border-violet-500 { border-color: #8b5cf6 }
        .border-violet-600 { border-color: #7c3aed }
        .border-violet-700 { border-color: #6d28d9 }
        .border-violet-800 { border-color: #5b21b6 }
        .border-violet-900 { border-color: #4c1d95 }
        .border-violet-950 { border-color: #2e1065 }
        .border-purple-50 { border-color: #faf5ff }
        .border-purple-100 { border-color: #f3e8ff }
        .border-purple-200 { border-color: #e9d5ff }
        .border-purple-300 { border-color: #d8b4fe }
        .border-purple-400 { border-color: #c084fc }
        .border-purple-500 { border-color: #a855f7 }
        .border-purple-600 { border-color: #9333ea }
        .border-purple-700 { border-color: #7e22ce }
        .border-purple-800 { border-color: #6b21a8 }
        .border-purple-900 { border-color: #581c87 }
        .border-purple-950 { border-color: #3b0764 }
        .border-fuchsia-50 { border-color: #fdf4ff }
        .border-fuchsia-100 { border-color: #fae8ff }
        .border-fuchsia-200 { border-color: #f5d0fe }
        .border-fuchsia-300 { border-color: #f0abfc }
        .border-fuchsia-400 { border-color: #e879f9 }
        .border-fuchsia-500 { border-color: #d946ef }
        .border-fuchsia-600 { border-color: #c026d3 }
        .border-fuchsia-700 { border-color: #a21caf }
        .border-fuchsia-800 { border-color: #86198f }
        .border-fuchsia-900 { border-color: #701a75 }
        .border-fuchsia-950 { border-color: #4a044e }
        .border-pink-50 { border-color: #fdf2f8 }
        .border-pink-100 { border-color: #fce7f3 }
        .border-pink-200 { border-color: #fbcfe8 }
        .border-pink-300 { border-color: #f9a8d4 }
        .border-pink-400 { border-color: #f472b6 }
        .border-pink-500 { border-color: #ec4899 }
        .border-pink-600 { border-color: #db2777 }
        .border-pink-700 { border-color: #be185d }
        .border-pink-800 { border-color: #9d174d }
        .border-pink-900 { border-color: #831843 }
        .border-pink-950 { border-color: #500724 }
        .border-rose-50 { border-color: #fff1f2 }
        .border-rose-100 { border-color: #ffe4e6 }
        .border-rose-200 { border-color: #fecdd3 }
        .border-rose-300 { border-color: #fda4af }
        .border-rose-400 { border-color: #fb7185 }
        .border-rose-500 { border-color: #f43f5e }
        .border-rose-600 { border-color: #e11d48 }
        .border-rose-700 { border-color: #be123c }
        .border-rose-800 { border-color: #9f1239 }
        .border-rose-900 { border-color: #881337 }
        .border-rose-950 { border-color: #4c0519 }
        """
    );
  }

  @Test
  public void borderWidth() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("border border-0 border-2 border-4 border-8");
        className("border-x border-x-0 border-x-2 border-x-4 border-x-8");
        className("border-y border-y-0 border-y-2 border-y-4 border-y-8");
        className("border-t border-t-0 border-t-2 border-t-4 border-t-8");
        className("border-r border-r-0 border-r-2 border-r-4 border-r-8");
        className("border-b border-b-0 border-b-2 border-b-4 border-b-8");
        className("border-l border-l-0 border-l-2 border-l-4 border-l-8");
      }
    }

    test(
        Subject.class,

        """
        .border { border-width: 1px }
        .border-0 { border-width: 0px }
        .border-2 { border-width: 2px }
        .border-4 { border-width: 4px }
        .border-8 { border-width: 8px }
        .border-x { border-left-width: 1px; border-right-width: 1px }
        .border-x-0 { border-left-width: 0px; border-right-width: 0px }
        .border-x-2 { border-left-width: 2px; border-right-width: 2px }
        .border-x-4 { border-left-width: 4px; border-right-width: 4px }
        .border-x-8 { border-left-width: 8px; border-right-width: 8px }
        .border-y { border-top-width: 1px; border-bottom-width: 1px }
        .border-y-0 { border-top-width: 0px; border-bottom-width: 0px }
        .border-y-2 { border-top-width: 2px; border-bottom-width: 2px }
        .border-y-4 { border-top-width: 4px; border-bottom-width: 4px }
        .border-y-8 { border-top-width: 8px; border-bottom-width: 8px }
        .border-t { border-top-width: 1px }
        .border-t-0 { border-top-width: 0px }
        .border-t-2 { border-top-width: 2px }
        .border-t-4 { border-top-width: 4px }
        .border-t-8 { border-top-width: 8px }
        .border-r { border-right-width: 1px }
        .border-r-0 { border-right-width: 0px }
        .border-r-2 { border-right-width: 2px }
        .border-r-4 { border-right-width: 4px }
        .border-r-8 { border-right-width: 8px }
        .border-b { border-bottom-width: 1px }
        .border-b-0 { border-bottom-width: 0px }
        .border-b-2 { border-bottom-width: 2px }
        .border-b-4 { border-bottom-width: 4px }
        .border-b-8 { border-bottom-width: 8px }
        .border-l { border-left-width: 1px }
        .border-l-0 { border-left-width: 0px }
        .border-l-2 { border-left-width: 2px }
        .border-l-4 { border-left-width: 4px }
        .border-l-8 { border-left-width: 8px }
        """
    );
  }

  @Test
  public void display() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("block");
        className("inline-block");
        className("inline");
        className("flex");
        className("inline-flex");
        className("table");
        className("inline-table");
        className("table-caption");
        className("table-cell");
        className("table-column");
        className("table-column-group");
        className("table-footer-group");
        className("table-header-group");
        className("table-row-group");
        className("table-row");
        className("flow-root");
        className("grid");
        className("inline-grid");
        className("contents");
        className("list-item");
        className("hidden");
      }
    }

    test(
        Subject.class,

        """
        .block { display: block }
        .inline-block { display: inline-block }
        .inline { display: inline }
        .flex { display: flex }
        .inline-flex { display: inline-flex }
        .table { display: table }
        .inline-table { display: inline-table }
        .table-caption { display: table-caption }
        .table-cell { display: table-cell }
        .table-column { display: table-column }
        .table-column-group { display: table-column-group }
        .table-footer-group { display: table-footer-group }
        .table-header-group { display: table-header-group }
        .table-row-group { display: table-row-group }
        .table-row { display: table-row }
        .flow-root { display: flow-root }
        .grid { display: grid }
        .inline-grid { display: inline-grid }
        .contents { display: contents }
        .list-item { display: list-item }
        .hidden { display: none }
        """
    );
  }

  @Test
  public void flexDirection() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("flex-row");
        className("flex-row-reverse");
        className("flex-col");
        className("flex-col-reverse");
      }
    }

    test(
        Subject.class,

        """
        .flex-row { flex-direction: row }
        .flex-row-reverse { flex-direction: row-reverse }
        .flex-col { flex-direction: column }
        .flex-col-reverse { flex-direction: column-reverse }
        """
    );
  }

  @Test
  public void fontSize() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("text-xs text-sm text-base text-lg text-xl text-2xl text-3xl text-4xl text-5xl text-6xl text-7xl text-8xl text-9xl");
      }
    }

    test(
        Subject.class,

        """
        .text-xs { font-size: 0.75rem; line-height: 1rem }
        .text-sm { font-size: 0.875rem; line-height: 1.25rem }
        .text-base { font-size: 1rem; line-height: 1.5rem }
        .text-lg { font-size: 1.125rem; line-height: 1.75rem }
        .text-xl { font-size: 1.25rem; line-height: 1.75rem }
        .text-2xl { font-size: 1.5rem; line-height: 2rem }
        .text-3xl { font-size: 1.875rem; line-height: 2.25rem }
        .text-4xl { font-size: 2.25rem; line-height: 2.5rem }
        .text-5xl { font-size: 3rem; line-height: 1 }
        .text-6xl { font-size: 3.75rem; line-height: 1 }
        .text-7xl { font-size: 4.5rem; line-height: 1 }
        .text-8xl { font-size: 6rem; line-height: 1 }
        .text-9xl { font-size: 8rem; line-height: 1 }
        """
    );
  }

  @Test
  public void height() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("h-auto");

        className("h-px");
        className("h-0");
        className("h-0.5");
        className("h-1");
        className("h-1.5");
        className("h-2");
        className("h-2.5");
        className("h-3");
        className("h-3.5");
        className("h-4");
        className("h-5");
        className("h-6");
        className("h-7");
        className("h-8");
        className("h-9");
        className("h-10");
        className("h-11");
        className("h-12");
        className("h-14");
        className("h-16");
        className("h-20");
        className("h-24");
        className("h-28");
        className("h-32");
        className("h-36");
        className("h-40");
        className("h-44");
        className("h-48");
        className("h-52");
        className("h-56");
        className("h-60");
        className("h-64");
        className("h-72");
        className("h-80");
        className("h-96");

        className("h-1/2");
        className("h-1/3");
        className("h-2/3");
        className("h-1/4");
        className("h-2/4");
        className("h-3/4");
        className("h-1/5");
        className("h-2/5");
        className("h-3/5");
        className("h-4/5");
        className("h-1/6");
        className("h-2/6");
        className("h-3/6");
        className("h-4/6");
        className("h-5/6");
        className("h-full");
        className("h-screen");
        className("h-svh");
        className("h-lvh");
        className("h-dvh");
        className("h-min");
        className("h-max");
        className("h-fit");
      }
    }

    test(
        Subject.class,

        """
        .h-auto { height: auto }
        .h-px { height: 1px }
        .h-0 { height: 0px }
        .h-0\\.5 { height: 0.125rem }
        .h-1 { height: 0.25rem }
        .h-1\\.5 { height: 0.375rem }
        .h-2 { height: 0.5rem }
        .h-2\\.5 { height: 0.625rem }
        .h-3 { height: 0.75rem }
        .h-3\\.5 { height: 0.875rem }
        .h-4 { height: 1rem }
        .h-5 { height: 1.25rem }
        .h-6 { height: 1.5rem }
        .h-7 { height: 1.75rem }
        .h-8 { height: 2rem }
        .h-9 { height: 2.25rem }
        .h-10 { height: 2.5rem }
        .h-11 { height: 2.75rem }
        .h-12 { height: 3rem }
        .h-14 { height: 3.5rem }
        .h-16 { height: 4rem }
        .h-20 { height: 5rem }
        .h-24 { height: 6rem }
        .h-28 { height: 7rem }
        .h-32 { height: 8rem }
        .h-36 { height: 9rem }
        .h-40 { height: 10rem }
        .h-44 { height: 11rem }
        .h-48 { height: 12rem }
        .h-52 { height: 13rem }
        .h-56 { height: 14rem }
        .h-60 { height: 15rem }
        .h-64 { height: 16rem }
        .h-72 { height: 18rem }
        .h-80 { height: 20rem }
        .h-96 { height: 24rem }
        .h-1\\/2 { height: 50% }
        .h-1\\/3 { height: 33.333333% }
        .h-2\\/3 { height: 66.666667% }
        .h-1\\/4 { height: 25% }
        .h-2\\/4 { height: 50% }
        .h-3\\/4 { height: 75% }
        .h-1\\/5 { height: 20% }
        .h-2\\/5 { height: 40% }
        .h-3\\/5 { height: 60% }
        .h-4\\/5 { height: 80% }
        .h-1\\/6 { height: 16.666667% }
        .h-2\\/6 { height: 33.333333% }
        .h-3\\/6 { height: 50% }
        .h-4\\/6 { height: 66.666667% }
        .h-5\\/6 { height: 83.333333% }
        .h-full { height: 100% }
        .h-screen { height: 100vh }
        .h-svh { height: 100svh }
        .h-lvh { height: 100lvh }
        .h-dvh { height: 100dvh }
        .h-min { height: min-content }
        .h-max { height: max-content }
        .h-fit { height: fit-content }
        """
    );
  }

  @Test
  public void inset() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        // @formatter:off
        className("inset-auto inset-1/2 inset-1/3 inset-2/3 inset-1/4 inset-2/4 inset-3/4 inset-full");
        className("inset-px inset-0 inset-0.5 inset-1 inset-1.5 inset-2 inset-2.5 inset-3 inset-3.5 inset-4 inset-5 inset-6 inset-7 inset-8 inset-9 inset-10 inset-11 inset-12 inset-14 inset-16 inset-20 inset-24 inset-28 inset-32 inset-36 inset-40 inset-44 inset-48 inset-52 inset-56 inset-60 inset-64 inset-72 inset-80 inset-96");
        className("inset-x-auto inset-x-1/2 inset-x-1/3 inset-x-2/3 inset-x-1/4 inset-x-2/4 inset-x-3/4 inset-x-full");
        className("inset-x-px inset-x-0 inset-x-0.5 inset-x-1 inset-x-1.5 inset-x-2 inset-x-2.5 inset-x-3 inset-x-3.5 inset-x-4 inset-x-5 inset-x-6 inset-x-7 inset-x-8 inset-x-9 inset-x-10 inset-x-11 inset-x-12 inset-x-14 inset-x-16 inset-x-20 inset-x-24 inset-x-28 inset-x-32 inset-x-36 inset-x-40 inset-x-44 inset-x-48 inset-x-52 inset-x-56 inset-x-60 inset-x-64 inset-x-72 inset-x-80 inset-x-96");
        className("inset-y-auto inset-y-1/2 inset-y-1/3 inset-y-2/3 inset-y-1/4 inset-y-2/4 inset-y-3/4 inset-y-full");
        className("inset-y-px inset-y-0 inset-y-0.5 inset-y-1 inset-y-1.5 inset-y-2 inset-y-2.5 inset-y-3 inset-y-3.5 inset-y-4 inset-y-5 inset-y-6 inset-y-7 inset-y-8 inset-y-9 inset-y-10 inset-y-11 inset-y-12 inset-y-14 inset-y-16 inset-y-20 inset-y-24 inset-y-28 inset-y-32 inset-y-36 inset-y-40 inset-y-44 inset-y-48 inset-y-52 inset-y-56 inset-y-60 inset-y-64 inset-y-72 inset-y-80 inset-y-96");
        className("start-auto start-1/2 start-1/3 start-2/3 start-1/4 start-2/4 start-3/4 start-full");
        className("start-px start-0 start-0.5 start-1 start-1.5 start-2 start-2.5 start-3 start-3.5 start-4 start-5 start-6 start-7 start-8 start-9 start-10 start-11 start-12 start-14 start-16 start-20 start-24 start-28 start-32 start-36 start-40 start-44 start-48 start-52 start-56 start-60 start-64 start-72 start-80 start-96");
        className("end-auto end-1/2 end-1/3 end-2/3 end-1/4 end-2/4 end-3/4 end-full");
        className("end-px end-0 end-0.5 end-1 end-1.5 end-2 end-2.5 end-3 end-3.5 end-4 end-5 end-6 end-7 end-8 end-9 end-10 end-11 end-12 end-14 end-16 end-20 end-24 end-28 end-32 end-36 end-40 end-44 end-48 end-52 end-56 end-60 end-64 end-72 end-80 end-96");
        className("top-auto top-1/2 top-1/3 top-2/3 top-1/4 top-2/4 top-3/4 top-full");
        className("top-px top-0 top-0.5 top-1 top-1.5 top-2 top-2.5 top-3 top-3.5 top-4 top-5 top-6 top-7 top-8 top-9 top-10 top-11 top-12 top-14 top-16 top-20 top-24 top-28 top-32 top-36 top-40 top-44 top-48 top-52 top-56 top-60 top-64 top-72 top-80 top-96");
        className("right-auto right-1/2 right-1/3 right-2/3 right-1/4 right-2/4 right-3/4 right-full");
        className("right-px right-0 right-0.5 right-1 right-1.5 right-2 right-2.5 right-3 right-3.5 right-4 right-5 right-6 right-7 right-8 right-9 right-10 right-11 right-12 right-14 right-16 right-20 right-24 right-28 right-32 right-36 right-40 right-44 right-48 right-52 right-56 right-60 right-64 right-72 right-80 right-96");
        className("bottom-auto bottom-1/2 bottom-1/3 bottom-2/3 bottom-1/4 bottom-2/4 bottom-3/4 bottom-full");
        className("bottom-px bottom-0 bottom-0.5 bottom-1 bottom-1.5 bottom-2 bottom-2.5 bottom-3 bottom-3.5 bottom-4 bottom-5 bottom-6 bottom-7 bottom-8 bottom-9 bottom-10 bottom-11 bottom-12 bottom-14 bottom-16 bottom-20 bottom-24 bottom-28 bottom-32 bottom-36 bottom-40 bottom-44 bottom-48 bottom-52 bottom-56 bottom-60 bottom-64 bottom-72 bottom-80 bottom-96");
        className("left-auto left-1/2 left-1/3 left-2/3 left-1/4 left-2/4 left-3/4 left-full");
        className("left-px left-0 left-0.5 left-1 left-1.5 left-2 left-2.5 left-3 left-3.5 left-4 left-5 left-6 left-7 left-8 left-9 left-10 left-11 left-12 left-14 left-16 left-20 left-24 left-28 left-32 left-36 left-40 left-44 left-48 left-52 left-56 left-60 left-64 left-72 left-80 left-96");
        // @formatter:on
      }
    }

    test(
        Subject.class,

        """
        .inset-auto { inset: auto }
        .inset-1\\/2 { inset: 50% }
        .inset-1\\/3 { inset: 33.333333% }
        .inset-2\\/3 { inset: 66.666667% }
        .inset-1\\/4 { inset: 25% }
        .inset-2\\/4 { inset: 50% }
        .inset-3\\/4 { inset: 75% }
        .inset-full { inset: 100% }
        .inset-px { inset: 1px }
        .inset-0 { inset: 0px }
        .inset-0\\.5 { inset: 0.125rem }
        .inset-1 { inset: 0.25rem }
        .inset-1\\.5 { inset: 0.375rem }
        .inset-2 { inset: 0.5rem }
        .inset-2\\.5 { inset: 0.625rem }
        .inset-3 { inset: 0.75rem }
        .inset-3\\.5 { inset: 0.875rem }
        .inset-4 { inset: 1rem }
        .inset-5 { inset: 1.25rem }
        .inset-6 { inset: 1.5rem }
        .inset-7 { inset: 1.75rem }
        .inset-8 { inset: 2rem }
        .inset-9 { inset: 2.25rem }
        .inset-10 { inset: 2.5rem }
        .inset-11 { inset: 2.75rem }
        .inset-12 { inset: 3rem }
        .inset-14 { inset: 3.5rem }
        .inset-16 { inset: 4rem }
        .inset-20 { inset: 5rem }
        .inset-24 { inset: 6rem }
        .inset-28 { inset: 7rem }
        .inset-32 { inset: 8rem }
        .inset-36 { inset: 9rem }
        .inset-40 { inset: 10rem }
        .inset-44 { inset: 11rem }
        .inset-48 { inset: 12rem }
        .inset-52 { inset: 13rem }
        .inset-56 { inset: 14rem }
        .inset-60 { inset: 15rem }
        .inset-64 { inset: 16rem }
        .inset-72 { inset: 18rem }
        .inset-80 { inset: 20rem }
        .inset-96 { inset: 24rem }
        .inset-x-auto { left: auto; right: auto }
        .inset-x-1\\/2 { left: 50%; right: 50% }
        .inset-x-1\\/3 { left: 33.333333%; right: 33.333333% }
        .inset-x-2\\/3 { left: 66.666667%; right: 66.666667% }
        .inset-x-1\\/4 { left: 25%; right: 25% }
        .inset-x-2\\/4 { left: 50%; right: 50% }
        .inset-x-3\\/4 { left: 75%; right: 75% }
        .inset-x-full { left: 100%; right: 100% }
        .inset-x-px { left: 1px; right: 1px }
        .inset-x-0 { left: 0px; right: 0px }
        .inset-x-0\\.5 { left: 0.125rem; right: 0.125rem }
        .inset-x-1 { left: 0.25rem; right: 0.25rem }
        .inset-x-1\\.5 { left: 0.375rem; right: 0.375rem }
        .inset-x-2 { left: 0.5rem; right: 0.5rem }
        .inset-x-2\\.5 { left: 0.625rem; right: 0.625rem }
        .inset-x-3 { left: 0.75rem; right: 0.75rem }
        .inset-x-3\\.5 { left: 0.875rem; right: 0.875rem }
        .inset-x-4 { left: 1rem; right: 1rem }
        .inset-x-5 { left: 1.25rem; right: 1.25rem }
        .inset-x-6 { left: 1.5rem; right: 1.5rem }
        .inset-x-7 { left: 1.75rem; right: 1.75rem }
        .inset-x-8 { left: 2rem; right: 2rem }
        .inset-x-9 { left: 2.25rem; right: 2.25rem }
        .inset-x-10 { left: 2.5rem; right: 2.5rem }
        .inset-x-11 { left: 2.75rem; right: 2.75rem }
        .inset-x-12 { left: 3rem; right: 3rem }
        .inset-x-14 { left: 3.5rem; right: 3.5rem }
        .inset-x-16 { left: 4rem; right: 4rem }
        .inset-x-20 { left: 5rem; right: 5rem }
        .inset-x-24 { left: 6rem; right: 6rem }
        .inset-x-28 { left: 7rem; right: 7rem }
        .inset-x-32 { left: 8rem; right: 8rem }
        .inset-x-36 { left: 9rem; right: 9rem }
        .inset-x-40 { left: 10rem; right: 10rem }
        .inset-x-44 { left: 11rem; right: 11rem }
        .inset-x-48 { left: 12rem; right: 12rem }
        .inset-x-52 { left: 13rem; right: 13rem }
        .inset-x-56 { left: 14rem; right: 14rem }
        .inset-x-60 { left: 15rem; right: 15rem }
        .inset-x-64 { left: 16rem; right: 16rem }
        .inset-x-72 { left: 18rem; right: 18rem }
        .inset-x-80 { left: 20rem; right: 20rem }
        .inset-x-96 { left: 24rem; right: 24rem }
        .inset-y-auto { top: auto; bottom: auto }
        .inset-y-1\\/2 { top: 50%; bottom: 50% }
        .inset-y-1\\/3 { top: 33.333333%; bottom: 33.333333% }
        .inset-y-2\\/3 { top: 66.666667%; bottom: 66.666667% }
        .inset-y-1\\/4 { top: 25%; bottom: 25% }
        .inset-y-2\\/4 { top: 50%; bottom: 50% }
        .inset-y-3\\/4 { top: 75%; bottom: 75% }
        .inset-y-full { top: 100%; bottom: 100% }
        .inset-y-px { top: 1px; bottom: 1px }
        .inset-y-0 { top: 0px; bottom: 0px }
        .inset-y-0\\.5 { top: 0.125rem; bottom: 0.125rem }
        .inset-y-1 { top: 0.25rem; bottom: 0.25rem }
        .inset-y-1\\.5 { top: 0.375rem; bottom: 0.375rem }
        .inset-y-2 { top: 0.5rem; bottom: 0.5rem }
        .inset-y-2\\.5 { top: 0.625rem; bottom: 0.625rem }
        .inset-y-3 { top: 0.75rem; bottom: 0.75rem }
        .inset-y-3\\.5 { top: 0.875rem; bottom: 0.875rem }
        .inset-y-4 { top: 1rem; bottom: 1rem }
        .inset-y-5 { top: 1.25rem; bottom: 1.25rem }
        .inset-y-6 { top: 1.5rem; bottom: 1.5rem }
        .inset-y-7 { top: 1.75rem; bottom: 1.75rem }
        .inset-y-8 { top: 2rem; bottom: 2rem }
        .inset-y-9 { top: 2.25rem; bottom: 2.25rem }
        .inset-y-10 { top: 2.5rem; bottom: 2.5rem }
        .inset-y-11 { top: 2.75rem; bottom: 2.75rem }
        .inset-y-12 { top: 3rem; bottom: 3rem }
        .inset-y-14 { top: 3.5rem; bottom: 3.5rem }
        .inset-y-16 { top: 4rem; bottom: 4rem }
        .inset-y-20 { top: 5rem; bottom: 5rem }
        .inset-y-24 { top: 6rem; bottom: 6rem }
        .inset-y-28 { top: 7rem; bottom: 7rem }
        .inset-y-32 { top: 8rem; bottom: 8rem }
        .inset-y-36 { top: 9rem; bottom: 9rem }
        .inset-y-40 { top: 10rem; bottom: 10rem }
        .inset-y-44 { top: 11rem; bottom: 11rem }
        .inset-y-48 { top: 12rem; bottom: 12rem }
        .inset-y-52 { top: 13rem; bottom: 13rem }
        .inset-y-56 { top: 14rem; bottom: 14rem }
        .inset-y-60 { top: 15rem; bottom: 15rem }
        .inset-y-64 { top: 16rem; bottom: 16rem }
        .inset-y-72 { top: 18rem; bottom: 18rem }
        .inset-y-80 { top: 20rem; bottom: 20rem }
        .inset-y-96 { top: 24rem; bottom: 24rem }
        .start-auto { inset-inline-start: auto }
        .start-1\\/2 { inset-inline-start: 50% }
        .start-1\\/3 { inset-inline-start: 33.333333% }
        .start-2\\/3 { inset-inline-start: 66.666667% }
        .start-1\\/4 { inset-inline-start: 25% }
        .start-2\\/4 { inset-inline-start: 50% }
        .start-3\\/4 { inset-inline-start: 75% }
        .start-full { inset-inline-start: 100% }
        .start-px { inset-inline-start: 1px }
        .start-0 { inset-inline-start: 0px }
        .start-0\\.5 { inset-inline-start: 0.125rem }
        .start-1 { inset-inline-start: 0.25rem }
        .start-1\\.5 { inset-inline-start: 0.375rem }
        .start-2 { inset-inline-start: 0.5rem }
        .start-2\\.5 { inset-inline-start: 0.625rem }
        .start-3 { inset-inline-start: 0.75rem }
        .start-3\\.5 { inset-inline-start: 0.875rem }
        .start-4 { inset-inline-start: 1rem }
        .start-5 { inset-inline-start: 1.25rem }
        .start-6 { inset-inline-start: 1.5rem }
        .start-7 { inset-inline-start: 1.75rem }
        .start-8 { inset-inline-start: 2rem }
        .start-9 { inset-inline-start: 2.25rem }
        .start-10 { inset-inline-start: 2.5rem }
        .start-11 { inset-inline-start: 2.75rem }
        .start-12 { inset-inline-start: 3rem }
        .start-14 { inset-inline-start: 3.5rem }
        .start-16 { inset-inline-start: 4rem }
        .start-20 { inset-inline-start: 5rem }
        .start-24 { inset-inline-start: 6rem }
        .start-28 { inset-inline-start: 7rem }
        .start-32 { inset-inline-start: 8rem }
        .start-36 { inset-inline-start: 9rem }
        .start-40 { inset-inline-start: 10rem }
        .start-44 { inset-inline-start: 11rem }
        .start-48 { inset-inline-start: 12rem }
        .start-52 { inset-inline-start: 13rem }
        .start-56 { inset-inline-start: 14rem }
        .start-60 { inset-inline-start: 15rem }
        .start-64 { inset-inline-start: 16rem }
        .start-72 { inset-inline-start: 18rem }
        .start-80 { inset-inline-start: 20rem }
        .start-96 { inset-inline-start: 24rem }
        .end-auto { inset-inline-start: auto }
        .end-1\\/2 { inset-inline-start: 50% }
        .end-1\\/3 { inset-inline-start: 33.333333% }
        .end-2\\/3 { inset-inline-start: 66.666667% }
        .end-1\\/4 { inset-inline-start: 25% }
        .end-2\\/4 { inset-inline-start: 50% }
        .end-3\\/4 { inset-inline-start: 75% }
        .end-full { inset-inline-start: 100% }
        .end-px { inset-inline-start: 1px }
        .end-0 { inset-inline-start: 0px }
        .end-0\\.5 { inset-inline-start: 0.125rem }
        .end-1 { inset-inline-start: 0.25rem }
        .end-1\\.5 { inset-inline-start: 0.375rem }
        .end-2 { inset-inline-start: 0.5rem }
        .end-2\\.5 { inset-inline-start: 0.625rem }
        .end-3 { inset-inline-start: 0.75rem }
        .end-3\\.5 { inset-inline-start: 0.875rem }
        .end-4 { inset-inline-start: 1rem }
        .end-5 { inset-inline-start: 1.25rem }
        .end-6 { inset-inline-start: 1.5rem }
        .end-7 { inset-inline-start: 1.75rem }
        .end-8 { inset-inline-start: 2rem }
        .end-9 { inset-inline-start: 2.25rem }
        .end-10 { inset-inline-start: 2.5rem }
        .end-11 { inset-inline-start: 2.75rem }
        .end-12 { inset-inline-start: 3rem }
        .end-14 { inset-inline-start: 3.5rem }
        .end-16 { inset-inline-start: 4rem }
        .end-20 { inset-inline-start: 5rem }
        .end-24 { inset-inline-start: 6rem }
        .end-28 { inset-inline-start: 7rem }
        .end-32 { inset-inline-start: 8rem }
        .end-36 { inset-inline-start: 9rem }
        .end-40 { inset-inline-start: 10rem }
        .end-44 { inset-inline-start: 11rem }
        .end-48 { inset-inline-start: 12rem }
        .end-52 { inset-inline-start: 13rem }
        .end-56 { inset-inline-start: 14rem }
        .end-60 { inset-inline-start: 15rem }
        .end-64 { inset-inline-start: 16rem }
        .end-72 { inset-inline-start: 18rem }
        .end-80 { inset-inline-start: 20rem }
        .end-96 { inset-inline-start: 24rem }
        .top-auto { top: auto }
        .top-1\\/2 { top: 50% }
        .top-1\\/3 { top: 33.333333% }
        .top-2\\/3 { top: 66.666667% }
        .top-1\\/4 { top: 25% }
        .top-2\\/4 { top: 50% }
        .top-3\\/4 { top: 75% }
        .top-full { top: 100% }
        .top-px { top: 1px }
        .top-0 { top: 0px }
        .top-0\\.5 { top: 0.125rem }
        .top-1 { top: 0.25rem }
        .top-1\\.5 { top: 0.375rem }
        .top-2 { top: 0.5rem }
        .top-2\\.5 { top: 0.625rem }
        .top-3 { top: 0.75rem }
        .top-3\\.5 { top: 0.875rem }
        .top-4 { top: 1rem }
        .top-5 { top: 1.25rem }
        .top-6 { top: 1.5rem }
        .top-7 { top: 1.75rem }
        .top-8 { top: 2rem }
        .top-9 { top: 2.25rem }
        .top-10 { top: 2.5rem }
        .top-11 { top: 2.75rem }
        .top-12 { top: 3rem }
        .top-14 { top: 3.5rem }
        .top-16 { top: 4rem }
        .top-20 { top: 5rem }
        .top-24 { top: 6rem }
        .top-28 { top: 7rem }
        .top-32 { top: 8rem }
        .top-36 { top: 9rem }
        .top-40 { top: 10rem }
        .top-44 { top: 11rem }
        .top-48 { top: 12rem }
        .top-52 { top: 13rem }
        .top-56 { top: 14rem }
        .top-60 { top: 15rem }
        .top-64 { top: 16rem }
        .top-72 { top: 18rem }
        .top-80 { top: 20rem }
        .top-96 { top: 24rem }
        .right-auto { right: auto }
        .right-1\\/2 { right: 50% }
        .right-1\\/3 { right: 33.333333% }
        .right-2\\/3 { right: 66.666667% }
        .right-1\\/4 { right: 25% }
        .right-2\\/4 { right: 50% }
        .right-3\\/4 { right: 75% }
        .right-full { right: 100% }
        .right-px { right: 1px }
        .right-0 { right: 0px }
        .right-0\\.5 { right: 0.125rem }
        .right-1 { right: 0.25rem }
        .right-1\\.5 { right: 0.375rem }
        .right-2 { right: 0.5rem }
        .right-2\\.5 { right: 0.625rem }
        .right-3 { right: 0.75rem }
        .right-3\\.5 { right: 0.875rem }
        .right-4 { right: 1rem }
        .right-5 { right: 1.25rem }
        .right-6 { right: 1.5rem }
        .right-7 { right: 1.75rem }
        .right-8 { right: 2rem }
        .right-9 { right: 2.25rem }
        .right-10 { right: 2.5rem }
        .right-11 { right: 2.75rem }
        .right-12 { right: 3rem }
        .right-14 { right: 3.5rem }
        .right-16 { right: 4rem }
        .right-20 { right: 5rem }
        .right-24 { right: 6rem }
        .right-28 { right: 7rem }
        .right-32 { right: 8rem }
        .right-36 { right: 9rem }
        .right-40 { right: 10rem }
        .right-44 { right: 11rem }
        .right-48 { right: 12rem }
        .right-52 { right: 13rem }
        .right-56 { right: 14rem }
        .right-60 { right: 15rem }
        .right-64 { right: 16rem }
        .right-72 { right: 18rem }
        .right-80 { right: 20rem }
        .right-96 { right: 24rem }
        .bottom-auto { bottom: auto }
        .bottom-1\\/2 { bottom: 50% }
        .bottom-1\\/3 { bottom: 33.333333% }
        .bottom-2\\/3 { bottom: 66.666667% }
        .bottom-1\\/4 { bottom: 25% }
        .bottom-2\\/4 { bottom: 50% }
        .bottom-3\\/4 { bottom: 75% }
        .bottom-full { bottom: 100% }
        .bottom-px { bottom: 1px }
        .bottom-0 { bottom: 0px }
        .bottom-0\\.5 { bottom: 0.125rem }
        .bottom-1 { bottom: 0.25rem }
        .bottom-1\\.5 { bottom: 0.375rem }
        .bottom-2 { bottom: 0.5rem }
        .bottom-2\\.5 { bottom: 0.625rem }
        .bottom-3 { bottom: 0.75rem }
        .bottom-3\\.5 { bottom: 0.875rem }
        .bottom-4 { bottom: 1rem }
        .bottom-5 { bottom: 1.25rem }
        .bottom-6 { bottom: 1.5rem }
        .bottom-7 { bottom: 1.75rem }
        .bottom-8 { bottom: 2rem }
        .bottom-9 { bottom: 2.25rem }
        .bottom-10 { bottom: 2.5rem }
        .bottom-11 { bottom: 2.75rem }
        .bottom-12 { bottom: 3rem }
        .bottom-14 { bottom: 3.5rem }
        .bottom-16 { bottom: 4rem }
        .bottom-20 { bottom: 5rem }
        .bottom-24 { bottom: 6rem }
        .bottom-28 { bottom: 7rem }
        .bottom-32 { bottom: 8rem }
        .bottom-36 { bottom: 9rem }
        .bottom-40 { bottom: 10rem }
        .bottom-44 { bottom: 11rem }
        .bottom-48 { bottom: 12rem }
        .bottom-52 { bottom: 13rem }
        .bottom-56 { bottom: 14rem }
        .bottom-60 { bottom: 15rem }
        .bottom-64 { bottom: 16rem }
        .bottom-72 { bottom: 18rem }
        .bottom-80 { bottom: 20rem }
        .bottom-96 { bottom: 24rem }
        .left-auto { left: auto }
        .left-1\\/2 { left: 50% }
        .left-1\\/3 { left: 33.333333% }
        .left-2\\/3 { left: 66.666667% }
        .left-1\\/4 { left: 25% }
        .left-2\\/4 { left: 50% }
        .left-3\\/4 { left: 75% }
        .left-full { left: 100% }
        .left-px { left: 1px }
        .left-0 { left: 0px }
        .left-0\\.5 { left: 0.125rem }
        .left-1 { left: 0.25rem }
        .left-1\\.5 { left: 0.375rem }
        .left-2 { left: 0.5rem }
        .left-2\\.5 { left: 0.625rem }
        .left-3 { left: 0.75rem }
        .left-3\\.5 { left: 0.875rem }
        .left-4 { left: 1rem }
        .left-5 { left: 1.25rem }
        .left-6 { left: 1.5rem }
        .left-7 { left: 1.75rem }
        .left-8 { left: 2rem }
        .left-9 { left: 2.25rem }
        .left-10 { left: 2.5rem }
        .left-11 { left: 2.75rem }
        .left-12 { left: 3rem }
        .left-14 { left: 3.5rem }
        .left-16 { left: 4rem }
        .left-20 { left: 5rem }
        .left-24 { left: 6rem }
        .left-28 { left: 7rem }
        .left-32 { left: 8rem }
        .left-36 { left: 9rem }
        .left-40 { left: 10rem }
        .left-44 { left: 11rem }
        .left-48 { left: 12rem }
        .left-52 { left: 13rem }
        .left-56 { left: 14rem }
        .left-60 { left: 15rem }
        .left-64 { left: 16rem }
        .left-72 { left: 18rem }
        .left-80 { left: 20rem }
        .left-96 { left: 24rem }
        """
    );
  }

  @Test
  public void letterSpacing() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("tracking-tighter");
        className("tracking-tight");
        className("tracking-normal");
        className("tracking-wide");
        className("tracking-wider");
        className("tracking-widest");
      }
    }

    test(
        Subject.class,

        """
        .tracking-tighter { letter-spacing: -0.05em }
        .tracking-tight { letter-spacing: -0.025em }
        .tracking-normal { letter-spacing: 0em }
        .tracking-wide { letter-spacing: 0.025em }
        .tracking-wider { letter-spacing: 0.05em }
        .tracking-widest { letter-spacing: 0.1em }
        """
    );
  }

  @Test
  public void lineHeight() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("leading-3 leading-4 leading-5 leading-6 leading-7 leading-8 leading-9 leading-10 leading-none leading-tight leading-snug leading-normal leading-relaxed leading-loose");
      }
    }

    test(
        Subject.class,

        """
        .leading-3 { line-height: 0.75rem }
        .leading-4 { line-height: 1rem }
        .leading-5 { line-height: 1.25rem }
        .leading-6 { line-height: 1.5rem }
        .leading-7 { line-height: 1.75rem }
        .leading-8 { line-height: 2rem }
        .leading-9 { line-height: 2.25rem }
        .leading-10 { line-height: 2.5rem }
        .leading-none { line-height: 1 }
        .leading-tight { line-height: 1.25 }
        .leading-snug { line-height: 1.375 }
        .leading-normal { line-height: 1.5 }
        .leading-relaxed { line-height: 1.625 }
        .leading-loose { line-height: 2 }
        """
    );
  }

  @Test
  public void margin() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        // @formatter:off
        className("m-auto m-px m-0 m-0.5 m-1 m-1.5 m-2 m-2.5 m-3 m-3.5 m-4 m-5 m-6 m-7 m-8 m-9 m-10 m-11 m-12 m-14 m-16 m-20 m-24 m-28 m-32 m-36 m-40 m-44 m-48 m-52 m-56 m-60 m-64 m-72 m-80 m-96");
        className("mx-auto mx-px mx-0 mx-0.5 mx-1 mx-1.5 mx-2 mx-2.5 mx-3 mx-3.5 mx-4 mx-5 mx-6 mx-7 mx-8 mx-9 mx-10 mx-11 mx-12 mx-14 mx-16 mx-20 mx-24 mx-28 mx-32 mx-36 mx-40 mx-44 mx-48 mx-52 mx-56 mx-60 mx-64 mx-72 mx-80 mx-96");
        className("my-auto my-px my-0 my-0.5 my-1 my-1.5 my-2 my-2.5 my-3 my-3.5 my-4 my-5 my-6 my-7 my-8 my-9 my-10 my-11 my-12 my-14 my-16 my-20 my-24 my-28 my-32 my-36 my-40 my-44 my-48 my-52 my-56 my-60 my-64 my-72 my-80 my-96");
        className("mt-auto mt-px mt-0 mt-0.5 mt-1 mt-1.5 mt-2 mt-2.5 mt-3 mt-3.5 mt-4 mt-5 mt-6 mt-7 mt-8 mt-9 mt-10 mt-11 mt-12 mt-14 mt-16 mt-20 mt-24 mt-28 mt-32 mt-36 mt-40 mt-44 mt-48 mt-52 mt-56 mt-60 mt-64 mt-72 mt-80 mt-96");
        className("mr-auto mr-px mr-0 mr-0.5 mr-1 mr-1.5 mr-2 mr-2.5 mr-3 mr-3.5 mr-4 mr-5 mr-6 mr-7 mr-8 mr-9 mr-10 mr-11 mr-12 mr-14 mr-16 mr-20 mr-24 mr-28 mr-32 mr-36 mr-40 mr-44 mr-48 mr-52 mr-56 mr-60 mr-64 mr-72 mr-80 mr-96");
        className("mb-auto mb-px mb-0 mb-0.5 mb-1 mb-1.5 mb-2 mb-2.5 mb-3 mb-3.5 mb-4 mb-5 mb-6 mb-7 mb-8 mb-9 mb-10 mb-11 mb-12 mb-14 mb-16 mb-20 mb-24 mb-28 mb-32 mb-36 mb-40 mb-44 mb-48 mb-52 mb-56 mb-60 mb-64 mb-72 mb-80 mb-96");
        className("ml-auto ml-px ml-0 ml-0.5 ml-1 ml-1.5 ml-2 ml-2.5 ml-3 ml-3.5 ml-4 ml-5 ml-6 ml-7 ml-8 ml-9 ml-10 ml-11 ml-12 ml-14 ml-16 ml-20 ml-24 ml-28 ml-32 ml-36 ml-40 ml-44 ml-48 ml-52 ml-56 ml-60 ml-64 ml-72 ml-80 ml-96");
        // @formatter:on
      }
    }

    test(
        Subject.class,

        """
        .m-auto { margin: auto }
        .m-px { margin: 1px }
        .m-0 { margin: 0px }
        .m-0\\.5 { margin: 0.125rem }
        .m-1 { margin: 0.25rem }
        .m-1\\.5 { margin: 0.375rem }
        .m-2 { margin: 0.5rem }
        .m-2\\.5 { margin: 0.625rem }
        .m-3 { margin: 0.75rem }
        .m-3\\.5 { margin: 0.875rem }
        .m-4 { margin: 1rem }
        .m-5 { margin: 1.25rem }
        .m-6 { margin: 1.5rem }
        .m-7 { margin: 1.75rem }
        .m-8 { margin: 2rem }
        .m-9 { margin: 2.25rem }
        .m-10 { margin: 2.5rem }
        .m-11 { margin: 2.75rem }
        .m-12 { margin: 3rem }
        .m-14 { margin: 3.5rem }
        .m-16 { margin: 4rem }
        .m-20 { margin: 5rem }
        .m-24 { margin: 6rem }
        .m-28 { margin: 7rem }
        .m-32 { margin: 8rem }
        .m-36 { margin: 9rem }
        .m-40 { margin: 10rem }
        .m-44 { margin: 11rem }
        .m-48 { margin: 12rem }
        .m-52 { margin: 13rem }
        .m-56 { margin: 14rem }
        .m-60 { margin: 15rem }
        .m-64 { margin: 16rem }
        .m-72 { margin: 18rem }
        .m-80 { margin: 20rem }
        .m-96 { margin: 24rem }
        .mx-auto { margin-left: auto; margin-right: auto }
        .mx-px { margin-left: 1px; margin-right: 1px }
        .mx-0 { margin-left: 0px; margin-right: 0px }
        .mx-0\\.5 { margin-left: 0.125rem; margin-right: 0.125rem }
        .mx-1 { margin-left: 0.25rem; margin-right: 0.25rem }
        .mx-1\\.5 { margin-left: 0.375rem; margin-right: 0.375rem }
        .mx-2 { margin-left: 0.5rem; margin-right: 0.5rem }
        .mx-2\\.5 { margin-left: 0.625rem; margin-right: 0.625rem }
        .mx-3 { margin-left: 0.75rem; margin-right: 0.75rem }
        .mx-3\\.5 { margin-left: 0.875rem; margin-right: 0.875rem }
        .mx-4 { margin-left: 1rem; margin-right: 1rem }
        .mx-5 { margin-left: 1.25rem; margin-right: 1.25rem }
        .mx-6 { margin-left: 1.5rem; margin-right: 1.5rem }
        .mx-7 { margin-left: 1.75rem; margin-right: 1.75rem }
        .mx-8 { margin-left: 2rem; margin-right: 2rem }
        .mx-9 { margin-left: 2.25rem; margin-right: 2.25rem }
        .mx-10 { margin-left: 2.5rem; margin-right: 2.5rem }
        .mx-11 { margin-left: 2.75rem; margin-right: 2.75rem }
        .mx-12 { margin-left: 3rem; margin-right: 3rem }
        .mx-14 { margin-left: 3.5rem; margin-right: 3.5rem }
        .mx-16 { margin-left: 4rem; margin-right: 4rem }
        .mx-20 { margin-left: 5rem; margin-right: 5rem }
        .mx-24 { margin-left: 6rem; margin-right: 6rem }
        .mx-28 { margin-left: 7rem; margin-right: 7rem }
        .mx-32 { margin-left: 8rem; margin-right: 8rem }
        .mx-36 { margin-left: 9rem; margin-right: 9rem }
        .mx-40 { margin-left: 10rem; margin-right: 10rem }
        .mx-44 { margin-left: 11rem; margin-right: 11rem }
        .mx-48 { margin-left: 12rem; margin-right: 12rem }
        .mx-52 { margin-left: 13rem; margin-right: 13rem }
        .mx-56 { margin-left: 14rem; margin-right: 14rem }
        .mx-60 { margin-left: 15rem; margin-right: 15rem }
        .mx-64 { margin-left: 16rem; margin-right: 16rem }
        .mx-72 { margin-left: 18rem; margin-right: 18rem }
        .mx-80 { margin-left: 20rem; margin-right: 20rem }
        .mx-96 { margin-left: 24rem; margin-right: 24rem }
        .my-auto { margin-top: auto; margin-bottom: auto }
        .my-px { margin-top: 1px; margin-bottom: 1px }
        .my-0 { margin-top: 0px; margin-bottom: 0px }
        .my-0\\.5 { margin-top: 0.125rem; margin-bottom: 0.125rem }
        .my-1 { margin-top: 0.25rem; margin-bottom: 0.25rem }
        .my-1\\.5 { margin-top: 0.375rem; margin-bottom: 0.375rem }
        .my-2 { margin-top: 0.5rem; margin-bottom: 0.5rem }
        .my-2\\.5 { margin-top: 0.625rem; margin-bottom: 0.625rem }
        .my-3 { margin-top: 0.75rem; margin-bottom: 0.75rem }
        .my-3\\.5 { margin-top: 0.875rem; margin-bottom: 0.875rem }
        .my-4 { margin-top: 1rem; margin-bottom: 1rem }
        .my-5 { margin-top: 1.25rem; margin-bottom: 1.25rem }
        .my-6 { margin-top: 1.5rem; margin-bottom: 1.5rem }
        .my-7 { margin-top: 1.75rem; margin-bottom: 1.75rem }
        .my-8 { margin-top: 2rem; margin-bottom: 2rem }
        .my-9 { margin-top: 2.25rem; margin-bottom: 2.25rem }
        .my-10 { margin-top: 2.5rem; margin-bottom: 2.5rem }
        .my-11 { margin-top: 2.75rem; margin-bottom: 2.75rem }
        .my-12 { margin-top: 3rem; margin-bottom: 3rem }
        .my-14 { margin-top: 3.5rem; margin-bottom: 3.5rem }
        .my-16 { margin-top: 4rem; margin-bottom: 4rem }
        .my-20 { margin-top: 5rem; margin-bottom: 5rem }
        .my-24 { margin-top: 6rem; margin-bottom: 6rem }
        .my-28 { margin-top: 7rem; margin-bottom: 7rem }
        .my-32 { margin-top: 8rem; margin-bottom: 8rem }
        .my-36 { margin-top: 9rem; margin-bottom: 9rem }
        .my-40 { margin-top: 10rem; margin-bottom: 10rem }
        .my-44 { margin-top: 11rem; margin-bottom: 11rem }
        .my-48 { margin-top: 12rem; margin-bottom: 12rem }
        .my-52 { margin-top: 13rem; margin-bottom: 13rem }
        .my-56 { margin-top: 14rem; margin-bottom: 14rem }
        .my-60 { margin-top: 15rem; margin-bottom: 15rem }
        .my-64 { margin-top: 16rem; margin-bottom: 16rem }
        .my-72 { margin-top: 18rem; margin-bottom: 18rem }
        .my-80 { margin-top: 20rem; margin-bottom: 20rem }
        .my-96 { margin-top: 24rem; margin-bottom: 24rem }
        .mt-auto { margin-top: auto }
        .mt-px { margin-top: 1px }
        .mt-0 { margin-top: 0px }
        .mt-0\\.5 { margin-top: 0.125rem }
        .mt-1 { margin-top: 0.25rem }
        .mt-1\\.5 { margin-top: 0.375rem }
        .mt-2 { margin-top: 0.5rem }
        .mt-2\\.5 { margin-top: 0.625rem }
        .mt-3 { margin-top: 0.75rem }
        .mt-3\\.5 { margin-top: 0.875rem }
        .mt-4 { margin-top: 1rem }
        .mt-5 { margin-top: 1.25rem }
        .mt-6 { margin-top: 1.5rem }
        .mt-7 { margin-top: 1.75rem }
        .mt-8 { margin-top: 2rem }
        .mt-9 { margin-top: 2.25rem }
        .mt-10 { margin-top: 2.5rem }
        .mt-11 { margin-top: 2.75rem }
        .mt-12 { margin-top: 3rem }
        .mt-14 { margin-top: 3.5rem }
        .mt-16 { margin-top: 4rem }
        .mt-20 { margin-top: 5rem }
        .mt-24 { margin-top: 6rem }
        .mt-28 { margin-top: 7rem }
        .mt-32 { margin-top: 8rem }
        .mt-36 { margin-top: 9rem }
        .mt-40 { margin-top: 10rem }
        .mt-44 { margin-top: 11rem }
        .mt-48 { margin-top: 12rem }
        .mt-52 { margin-top: 13rem }
        .mt-56 { margin-top: 14rem }
        .mt-60 { margin-top: 15rem }
        .mt-64 { margin-top: 16rem }
        .mt-72 { margin-top: 18rem }
        .mt-80 { margin-top: 20rem }
        .mt-96 { margin-top: 24rem }
        .mr-auto { margin-right: auto }
        .mr-px { margin-right: 1px }
        .mr-0 { margin-right: 0px }
        .mr-0\\.5 { margin-right: 0.125rem }
        .mr-1 { margin-right: 0.25rem }
        .mr-1\\.5 { margin-right: 0.375rem }
        .mr-2 { margin-right: 0.5rem }
        .mr-2\\.5 { margin-right: 0.625rem }
        .mr-3 { margin-right: 0.75rem }
        .mr-3\\.5 { margin-right: 0.875rem }
        .mr-4 { margin-right: 1rem }
        .mr-5 { margin-right: 1.25rem }
        .mr-6 { margin-right: 1.5rem }
        .mr-7 { margin-right: 1.75rem }
        .mr-8 { margin-right: 2rem }
        .mr-9 { margin-right: 2.25rem }
        .mr-10 { margin-right: 2.5rem }
        .mr-11 { margin-right: 2.75rem }
        .mr-12 { margin-right: 3rem }
        .mr-14 { margin-right: 3.5rem }
        .mr-16 { margin-right: 4rem }
        .mr-20 { margin-right: 5rem }
        .mr-24 { margin-right: 6rem }
        .mr-28 { margin-right: 7rem }
        .mr-32 { margin-right: 8rem }
        .mr-36 { margin-right: 9rem }
        .mr-40 { margin-right: 10rem }
        .mr-44 { margin-right: 11rem }
        .mr-48 { margin-right: 12rem }
        .mr-52 { margin-right: 13rem }
        .mr-56 { margin-right: 14rem }
        .mr-60 { margin-right: 15rem }
        .mr-64 { margin-right: 16rem }
        .mr-72 { margin-right: 18rem }
        .mr-80 { margin-right: 20rem }
        .mr-96 { margin-right: 24rem }
        .mb-auto { margin-bottom: auto }
        .mb-px { margin-bottom: 1px }
        .mb-0 { margin-bottom: 0px }
        .mb-0\\.5 { margin-bottom: 0.125rem }
        .mb-1 { margin-bottom: 0.25rem }
        .mb-1\\.5 { margin-bottom: 0.375rem }
        .mb-2 { margin-bottom: 0.5rem }
        .mb-2\\.5 { margin-bottom: 0.625rem }
        .mb-3 { margin-bottom: 0.75rem }
        .mb-3\\.5 { margin-bottom: 0.875rem }
        .mb-4 { margin-bottom: 1rem }
        .mb-5 { margin-bottom: 1.25rem }
        .mb-6 { margin-bottom: 1.5rem }
        .mb-7 { margin-bottom: 1.75rem }
        .mb-8 { margin-bottom: 2rem }
        .mb-9 { margin-bottom: 2.25rem }
        .mb-10 { margin-bottom: 2.5rem }
        .mb-11 { margin-bottom: 2.75rem }
        .mb-12 { margin-bottom: 3rem }
        .mb-14 { margin-bottom: 3.5rem }
        .mb-16 { margin-bottom: 4rem }
        .mb-20 { margin-bottom: 5rem }
        .mb-24 { margin-bottom: 6rem }
        .mb-28 { margin-bottom: 7rem }
        .mb-32 { margin-bottom: 8rem }
        .mb-36 { margin-bottom: 9rem }
        .mb-40 { margin-bottom: 10rem }
        .mb-44 { margin-bottom: 11rem }
        .mb-48 { margin-bottom: 12rem }
        .mb-52 { margin-bottom: 13rem }
        .mb-56 { margin-bottom: 14rem }
        .mb-60 { margin-bottom: 15rem }
        .mb-64 { margin-bottom: 16rem }
        .mb-72 { margin-bottom: 18rem }
        .mb-80 { margin-bottom: 20rem }
        .mb-96 { margin-bottom: 24rem }
        .ml-auto { margin-left: auto }
        .ml-px { margin-left: 1px }
        .ml-0 { margin-left: 0px }
        .ml-0\\.5 { margin-left: 0.125rem }
        .ml-1 { margin-left: 0.25rem }
        .ml-1\\.5 { margin-left: 0.375rem }
        .ml-2 { margin-left: 0.5rem }
        .ml-2\\.5 { margin-left: 0.625rem }
        .ml-3 { margin-left: 0.75rem }
        .ml-3\\.5 { margin-left: 0.875rem }
        .ml-4 { margin-left: 1rem }
        .ml-5 { margin-left: 1.25rem }
        .ml-6 { margin-left: 1.5rem }
        .ml-7 { margin-left: 1.75rem }
        .ml-8 { margin-left: 2rem }
        .ml-9 { margin-left: 2.25rem }
        .ml-10 { margin-left: 2.5rem }
        .ml-11 { margin-left: 2.75rem }
        .ml-12 { margin-left: 3rem }
        .ml-14 { margin-left: 3.5rem }
        .ml-16 { margin-left: 4rem }
        .ml-20 { margin-left: 5rem }
        .ml-24 { margin-left: 6rem }
        .ml-28 { margin-left: 7rem }
        .ml-32 { margin-left: 8rem }
        .ml-36 { margin-left: 9rem }
        .ml-40 { margin-left: 10rem }
        .ml-44 { margin-left: 11rem }
        .ml-48 { margin-left: 12rem }
        .ml-52 { margin-left: 13rem }
        .ml-56 { margin-left: 14rem }
        .ml-60 { margin-left: 15rem }
        .ml-64 { margin-left: 16rem }
        .ml-72 { margin-left: 18rem }
        .ml-80 { margin-left: 20rem }
        .ml-96 { margin-left: 24rem }
        """
    );
  }

  @Test
  public void padding() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        // @formatter:off
        className("p-px p-0 p-0.5 p-1 p-1.5 p-2 p-2.5 p-3 p-3.5 p-4 p-5 p-6 p-7 p-8 p-9 p-10 p-11 p-12 p-14 p-16 p-20 p-24 p-28 p-32 p-36 p-40 p-44 p-48 p-52 p-56 p-60 p-64 p-72 p-80 p-96");
        className("px-px px-0 px-0.5 px-1 px-1.5 px-2 px-2.5 px-3 px-3.5 px-4 px-5 px-6 px-7 px-8 px-9 px-10 px-11 px-12 px-14 px-16 px-20 px-24 px-28 px-32 px-36 px-40 px-44 px-48 px-52 px-56 px-60 px-64 px-72 px-80 px-96");
        className("py-px py-0 py-0.5 py-1 py-1.5 py-2 py-2.5 py-3 py-3.5 py-4 py-5 py-6 py-7 py-8 py-9 py-10 py-11 py-12 py-14 py-16 py-20 py-24 py-28 py-32 py-36 py-40 py-44 py-48 py-52 py-56 py-60 py-64 py-72 py-80 py-96");
        className("pt-px pt-0 pt-0.5 pt-1 pt-1.5 pt-2 pt-2.5 pt-3 pt-3.5 pt-4 pt-5 pt-6 pt-7 pt-8 pt-9 pt-10 pt-11 pt-12 pt-14 pt-16 pt-20 pt-24 pt-28 pt-32 pt-36 pt-40 pt-44 pt-48 pt-52 pt-56 pt-60 pt-64 pt-72 pt-80 pt-96");
        className("pr-px pr-0 pr-0.5 pr-1 pr-1.5 pr-2 pr-2.5 pr-3 pr-3.5 pr-4 pr-5 pr-6 pr-7 pr-8 pr-9 pr-10 pr-11 pr-12 pr-14 pr-16 pr-20 pr-24 pr-28 pr-32 pr-36 pr-40 pr-44 pr-48 pr-52 pr-56 pr-60 pr-64 pr-72 pr-80 pr-96");
        className("pb-px pb-0 pb-0.5 pb-1 pb-1.5 pb-2 pb-2.5 pb-3 pb-3.5 pb-4 pb-5 pb-6 pb-7 pb-8 pb-9 pb-10 pb-11 pb-12 pb-14 pb-16 pb-20 pb-24 pb-28 pb-32 pb-36 pb-40 pb-44 pb-48 pb-52 pb-56 pb-60 pb-64 pb-72 pb-80 pb-96");
        className("pl-px pl-0 pl-0.5 pl-1 pl-1.5 pl-2 pl-2.5 pl-3 pl-3.5 pl-4 pl-5 pl-6 pl-7 pl-8 pl-9 pl-10 pl-11 pl-12 pl-14 pl-16 pl-20 pl-24 pl-28 pl-32 pl-36 pl-40 pl-44 pl-48 pl-52 pl-56 pl-60 pl-64 pl-72 pl-80 pl-96");
        // @formatter:on
      }
    }

    test(
        Subject.class,

        """
        .p-px { padding: 1px }
        .p-0 { padding: 0px }
        .p-0\\.5 { padding: 0.125rem }
        .p-1 { padding: 0.25rem }
        .p-1\\.5 { padding: 0.375rem }
        .p-2 { padding: 0.5rem }
        .p-2\\.5 { padding: 0.625rem }
        .p-3 { padding: 0.75rem }
        .p-3\\.5 { padding: 0.875rem }
        .p-4 { padding: 1rem }
        .p-5 { padding: 1.25rem }
        .p-6 { padding: 1.5rem }
        .p-7 { padding: 1.75rem }
        .p-8 { padding: 2rem }
        .p-9 { padding: 2.25rem }
        .p-10 { padding: 2.5rem }
        .p-11 { padding: 2.75rem }
        .p-12 { padding: 3rem }
        .p-14 { padding: 3.5rem }
        .p-16 { padding: 4rem }
        .p-20 { padding: 5rem }
        .p-24 { padding: 6rem }
        .p-28 { padding: 7rem }
        .p-32 { padding: 8rem }
        .p-36 { padding: 9rem }
        .p-40 { padding: 10rem }
        .p-44 { padding: 11rem }
        .p-48 { padding: 12rem }
        .p-52 { padding: 13rem }
        .p-56 { padding: 14rem }
        .p-60 { padding: 15rem }
        .p-64 { padding: 16rem }
        .p-72 { padding: 18rem }
        .p-80 { padding: 20rem }
        .p-96 { padding: 24rem }
        .px-px { padding-left: 1px; padding-right: 1px }
        .px-0 { padding-left: 0px; padding-right: 0px }
        .px-0\\.5 { padding-left: 0.125rem; padding-right: 0.125rem }
        .px-1 { padding-left: 0.25rem; padding-right: 0.25rem }
        .px-1\\.5 { padding-left: 0.375rem; padding-right: 0.375rem }
        .px-2 { padding-left: 0.5rem; padding-right: 0.5rem }
        .px-2\\.5 { padding-left: 0.625rem; padding-right: 0.625rem }
        .px-3 { padding-left: 0.75rem; padding-right: 0.75rem }
        .px-3\\.5 { padding-left: 0.875rem; padding-right: 0.875rem }
        .px-4 { padding-left: 1rem; padding-right: 1rem }
        .px-5 { padding-left: 1.25rem; padding-right: 1.25rem }
        .px-6 { padding-left: 1.5rem; padding-right: 1.5rem }
        .px-7 { padding-left: 1.75rem; padding-right: 1.75rem }
        .px-8 { padding-left: 2rem; padding-right: 2rem }
        .px-9 { padding-left: 2.25rem; padding-right: 2.25rem }
        .px-10 { padding-left: 2.5rem; padding-right: 2.5rem }
        .px-11 { padding-left: 2.75rem; padding-right: 2.75rem }
        .px-12 { padding-left: 3rem; padding-right: 3rem }
        .px-14 { padding-left: 3.5rem; padding-right: 3.5rem }
        .px-16 { padding-left: 4rem; padding-right: 4rem }
        .px-20 { padding-left: 5rem; padding-right: 5rem }
        .px-24 { padding-left: 6rem; padding-right: 6rem }
        .px-28 { padding-left: 7rem; padding-right: 7rem }
        .px-32 { padding-left: 8rem; padding-right: 8rem }
        .px-36 { padding-left: 9rem; padding-right: 9rem }
        .px-40 { padding-left: 10rem; padding-right: 10rem }
        .px-44 { padding-left: 11rem; padding-right: 11rem }
        .px-48 { padding-left: 12rem; padding-right: 12rem }
        .px-52 { padding-left: 13rem; padding-right: 13rem }
        .px-56 { padding-left: 14rem; padding-right: 14rem }
        .px-60 { padding-left: 15rem; padding-right: 15rem }
        .px-64 { padding-left: 16rem; padding-right: 16rem }
        .px-72 { padding-left: 18rem; padding-right: 18rem }
        .px-80 { padding-left: 20rem; padding-right: 20rem }
        .px-96 { padding-left: 24rem; padding-right: 24rem }
        .py-px { padding-top: 1px; padding-bottom: 1px }
        .py-0 { padding-top: 0px; padding-bottom: 0px }
        .py-0\\.5 { padding-top: 0.125rem; padding-bottom: 0.125rem }
        .py-1 { padding-top: 0.25rem; padding-bottom: 0.25rem }
        .py-1\\.5 { padding-top: 0.375rem; padding-bottom: 0.375rem }
        .py-2 { padding-top: 0.5rem; padding-bottom: 0.5rem }
        .py-2\\.5 { padding-top: 0.625rem; padding-bottom: 0.625rem }
        .py-3 { padding-top: 0.75rem; padding-bottom: 0.75rem }
        .py-3\\.5 { padding-top: 0.875rem; padding-bottom: 0.875rem }
        .py-4 { padding-top: 1rem; padding-bottom: 1rem }
        .py-5 { padding-top: 1.25rem; padding-bottom: 1.25rem }
        .py-6 { padding-top: 1.5rem; padding-bottom: 1.5rem }
        .py-7 { padding-top: 1.75rem; padding-bottom: 1.75rem }
        .py-8 { padding-top: 2rem; padding-bottom: 2rem }
        .py-9 { padding-top: 2.25rem; padding-bottom: 2.25rem }
        .py-10 { padding-top: 2.5rem; padding-bottom: 2.5rem }
        .py-11 { padding-top: 2.75rem; padding-bottom: 2.75rem }
        .py-12 { padding-top: 3rem; padding-bottom: 3rem }
        .py-14 { padding-top: 3.5rem; padding-bottom: 3.5rem }
        .py-16 { padding-top: 4rem; padding-bottom: 4rem }
        .py-20 { padding-top: 5rem; padding-bottom: 5rem }
        .py-24 { padding-top: 6rem; padding-bottom: 6rem }
        .py-28 { padding-top: 7rem; padding-bottom: 7rem }
        .py-32 { padding-top: 8rem; padding-bottom: 8rem }
        .py-36 { padding-top: 9rem; padding-bottom: 9rem }
        .py-40 { padding-top: 10rem; padding-bottom: 10rem }
        .py-44 { padding-top: 11rem; padding-bottom: 11rem }
        .py-48 { padding-top: 12rem; padding-bottom: 12rem }
        .py-52 { padding-top: 13rem; padding-bottom: 13rem }
        .py-56 { padding-top: 14rem; padding-bottom: 14rem }
        .py-60 { padding-top: 15rem; padding-bottom: 15rem }
        .py-64 { padding-top: 16rem; padding-bottom: 16rem }
        .py-72 { padding-top: 18rem; padding-bottom: 18rem }
        .py-80 { padding-top: 20rem; padding-bottom: 20rem }
        .py-96 { padding-top: 24rem; padding-bottom: 24rem }
        .pt-px { padding-top: 1px }
        .pt-0 { padding-top: 0px }
        .pt-0\\.5 { padding-top: 0.125rem }
        .pt-1 { padding-top: 0.25rem }
        .pt-1\\.5 { padding-top: 0.375rem }
        .pt-2 { padding-top: 0.5rem }
        .pt-2\\.5 { padding-top: 0.625rem }
        .pt-3 { padding-top: 0.75rem }
        .pt-3\\.5 { padding-top: 0.875rem }
        .pt-4 { padding-top: 1rem }
        .pt-5 { padding-top: 1.25rem }
        .pt-6 { padding-top: 1.5rem }
        .pt-7 { padding-top: 1.75rem }
        .pt-8 { padding-top: 2rem }
        .pt-9 { padding-top: 2.25rem }
        .pt-10 { padding-top: 2.5rem }
        .pt-11 { padding-top: 2.75rem }
        .pt-12 { padding-top: 3rem }
        .pt-14 { padding-top: 3.5rem }
        .pt-16 { padding-top: 4rem }
        .pt-20 { padding-top: 5rem }
        .pt-24 { padding-top: 6rem }
        .pt-28 { padding-top: 7rem }
        .pt-32 { padding-top: 8rem }
        .pt-36 { padding-top: 9rem }
        .pt-40 { padding-top: 10rem }
        .pt-44 { padding-top: 11rem }
        .pt-48 { padding-top: 12rem }
        .pt-52 { padding-top: 13rem }
        .pt-56 { padding-top: 14rem }
        .pt-60 { padding-top: 15rem }
        .pt-64 { padding-top: 16rem }
        .pt-72 { padding-top: 18rem }
        .pt-80 { padding-top: 20rem }
        .pt-96 { padding-top: 24rem }
        .pr-px { padding-right: 1px }
        .pr-0 { padding-right: 0px }
        .pr-0\\.5 { padding-right: 0.125rem }
        .pr-1 { padding-right: 0.25rem }
        .pr-1\\.5 { padding-right: 0.375rem }
        .pr-2 { padding-right: 0.5rem }
        .pr-2\\.5 { padding-right: 0.625rem }
        .pr-3 { padding-right: 0.75rem }
        .pr-3\\.5 { padding-right: 0.875rem }
        .pr-4 { padding-right: 1rem }
        .pr-5 { padding-right: 1.25rem }
        .pr-6 { padding-right: 1.5rem }
        .pr-7 { padding-right: 1.75rem }
        .pr-8 { padding-right: 2rem }
        .pr-9 { padding-right: 2.25rem }
        .pr-10 { padding-right: 2.5rem }
        .pr-11 { padding-right: 2.75rem }
        .pr-12 { padding-right: 3rem }
        .pr-14 { padding-right: 3.5rem }
        .pr-16 { padding-right: 4rem }
        .pr-20 { padding-right: 5rem }
        .pr-24 { padding-right: 6rem }
        .pr-28 { padding-right: 7rem }
        .pr-32 { padding-right: 8rem }
        .pr-36 { padding-right: 9rem }
        .pr-40 { padding-right: 10rem }
        .pr-44 { padding-right: 11rem }
        .pr-48 { padding-right: 12rem }
        .pr-52 { padding-right: 13rem }
        .pr-56 { padding-right: 14rem }
        .pr-60 { padding-right: 15rem }
        .pr-64 { padding-right: 16rem }
        .pr-72 { padding-right: 18rem }
        .pr-80 { padding-right: 20rem }
        .pr-96 { padding-right: 24rem }
        .pb-px { padding-bottom: 1px }
        .pb-0 { padding-bottom: 0px }
        .pb-0\\.5 { padding-bottom: 0.125rem }
        .pb-1 { padding-bottom: 0.25rem }
        .pb-1\\.5 { padding-bottom: 0.375rem }
        .pb-2 { padding-bottom: 0.5rem }
        .pb-2\\.5 { padding-bottom: 0.625rem }
        .pb-3 { padding-bottom: 0.75rem }
        .pb-3\\.5 { padding-bottom: 0.875rem }
        .pb-4 { padding-bottom: 1rem }
        .pb-5 { padding-bottom: 1.25rem }
        .pb-6 { padding-bottom: 1.5rem }
        .pb-7 { padding-bottom: 1.75rem }
        .pb-8 { padding-bottom: 2rem }
        .pb-9 { padding-bottom: 2.25rem }
        .pb-10 { padding-bottom: 2.5rem }
        .pb-11 { padding-bottom: 2.75rem }
        .pb-12 { padding-bottom: 3rem }
        .pb-14 { padding-bottom: 3.5rem }
        .pb-16 { padding-bottom: 4rem }
        .pb-20 { padding-bottom: 5rem }
        .pb-24 { padding-bottom: 6rem }
        .pb-28 { padding-bottom: 7rem }
        .pb-32 { padding-bottom: 8rem }
        .pb-36 { padding-bottom: 9rem }
        .pb-40 { padding-bottom: 10rem }
        .pb-44 { padding-bottom: 11rem }
        .pb-48 { padding-bottom: 12rem }
        .pb-52 { padding-bottom: 13rem }
        .pb-56 { padding-bottom: 14rem }
        .pb-60 { padding-bottom: 15rem }
        .pb-64 { padding-bottom: 16rem }
        .pb-72 { padding-bottom: 18rem }
        .pb-80 { padding-bottom: 20rem }
        .pb-96 { padding-bottom: 24rem }
        .pl-px { padding-left: 1px }
        .pl-0 { padding-left: 0px }
        .pl-0\\.5 { padding-left: 0.125rem }
        .pl-1 { padding-left: 0.25rem }
        .pl-1\\.5 { padding-left: 0.375rem }
        .pl-2 { padding-left: 0.5rem }
        .pl-2\\.5 { padding-left: 0.625rem }
        .pl-3 { padding-left: 0.75rem }
        .pl-3\\.5 { padding-left: 0.875rem }
        .pl-4 { padding-left: 1rem }
        .pl-5 { padding-left: 1.25rem }
        .pl-6 { padding-left: 1.5rem }
        .pl-7 { padding-left: 1.75rem }
        .pl-8 { padding-left: 2rem }
        .pl-9 { padding-left: 2.25rem }
        .pl-10 { padding-left: 2.5rem }
        .pl-11 { padding-left: 2.75rem }
        .pl-12 { padding-left: 3rem }
        .pl-14 { padding-left: 3.5rem }
        .pl-16 { padding-left: 4rem }
        .pl-20 { padding-left: 5rem }
        .pl-24 { padding-left: 6rem }
        .pl-28 { padding-left: 7rem }
        .pl-32 { padding-left: 8rem }
        .pl-36 { padding-left: 9rem }
        .pl-40 { padding-left: 10rem }
        .pl-44 { padding-left: 11rem }
        .pl-48 { padding-left: 12rem }
        .pl-52 { padding-left: 13rem }
        .pl-56 { padding-left: 14rem }
        .pl-60 { padding-left: 15rem }
        .pl-64 { padding-left: 16rem }
        .pl-72 { padding-left: 18rem }
        .pl-80 { padding-left: 20rem }
        .pl-96 { padding-left: 24rem }
        """
    );
  }

  @Test
  public void position() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("static fixed absolute relative sticky");
      }
    }

    test(
        Subject.class,

        """
        .static { position: static }
        .fixed { position: fixed }
        .absolute { position: absolute }
        .relative { position: relative }
        .sticky { position: sticky }
        """
    );
  }

  @Test
  public void textColor() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        // @formatter:off
        className("text-inherit text-current text-transparent text-black text-white");
        className("text-slate-50 text-slate-100 text-slate-200 text-slate-300 text-slate-400 text-slate-500 text-slate-600 text-slate-700 text-slate-800 text-slate-900 text-slate-950");
        className("text-gray-50 text-gray-100 text-gray-200 text-gray-300 text-gray-400 text-gray-500 text-gray-600 text-gray-700 text-gray-800 text-gray-900 text-gray-950");
        className("text-zinc-50 text-zinc-100 text-zinc-200 text-zinc-300 text-zinc-400 text-zinc-500 text-zinc-600 text-zinc-700 text-zinc-800 text-zinc-900 text-zinc-950");
        className("text-neutral-50 text-neutral-100 text-neutral-200 text-neutral-300 text-neutral-400 text-neutral-500 text-neutral-600 text-neutral-700 text-neutral-800 text-neutral-900 text-neutral-950");
        className("text-stone-50 text-stone-100 text-stone-200 text-stone-300 text-stone-400 text-stone-500 text-stone-600 text-stone-700 text-stone-800 text-stone-900 text-stone-950");
        className("text-red-50 text-red-100 text-red-200 text-red-300 text-red-400 text-red-500 text-red-600 text-red-700 text-red-800 text-red-900 text-red-950");
        className("text-orange-50 text-orange-100 text-orange-200 text-orange-300 text-orange-400 text-orange-500 text-orange-600 text-orange-700 text-orange-800 text-orange-900 text-orange-950");
        className("text-amber-50 text-amber-100 text-amber-200 text-amber-300 text-amber-400 text-amber-500 text-amber-600 text-amber-700 text-amber-800 text-amber-900 text-amber-950");
        className("text-yellow-50 text-yellow-100 text-yellow-200 text-yellow-300 text-yellow-400 text-yellow-500 text-yellow-600 text-yellow-700 text-yellow-800 text-yellow-900 text-yellow-950");
        className("text-lime-50 text-lime-100 text-lime-200 text-lime-300 text-lime-400 text-lime-500 text-lime-600 text-lime-700 text-lime-800 text-lime-900 text-lime-950");
        className("text-green-50 text-green-100 text-green-200 text-green-300 text-green-400 text-green-500 text-green-600 text-green-700 text-green-800 text-green-900 text-green-950");
        className("text-emerald-50 text-emerald-100 text-emerald-200 text-emerald-300 text-emerald-400 text-emerald-500 text-emerald-600 text-emerald-700 text-emerald-800 text-emerald-900 text-emerald-950");
        className("text-teal-50 text-teal-100 text-teal-200 text-teal-300 text-teal-400 text-teal-500 text-teal-600 text-teal-700 text-teal-800 text-teal-900 text-teal-950");
        className("text-cyan-50 text-cyan-100 text-cyan-200 text-cyan-300 text-cyan-400 text-cyan-500 text-cyan-600 text-cyan-700 text-cyan-800 text-cyan-900 text-cyan-950");
        className("text-sky-50 text-sky-100 text-sky-200 text-sky-300 text-sky-400 text-sky-500 text-sky-600 text-sky-700 text-sky-800 text-sky-900 text-sky-950");
        className("text-blue-50 text-blue-100 text-blue-200 text-blue-300 text-blue-400 text-blue-500 text-blue-600 text-blue-700 text-blue-800 text-blue-900 text-blue-950");
        className("text-indigo-50 text-indigo-100 text-indigo-200 text-indigo-300 text-indigo-400 text-indigo-500 text-indigo-600 text-indigo-700 text-indigo-800 text-indigo-900 text-indigo-950");
        className("text-violet-50 text-violet-100 text-violet-200 text-violet-300 text-violet-400 text-violet-500 text-violet-600 text-violet-700 text-violet-800 text-violet-900 text-violet-950");
        className("text-purple-50 text-purple-100 text-purple-200 text-purple-300 text-purple-400 text-purple-500 text-purple-600 text-purple-700 text-purple-800 text-purple-900 text-purple-950");
        className("text-fuchsia-50 text-fuchsia-100 text-fuchsia-200 text-fuchsia-300 text-fuchsia-400 text-fuchsia-500 text-fuchsia-600 text-fuchsia-700 text-fuchsia-800 text-fuchsia-900 text-fuchsia-950");
        className("text-pink-50 text-pink-100 text-pink-200 text-pink-300 text-pink-400 text-pink-500 text-pink-600 text-pink-700 text-pink-800 text-pink-900 text-pink-950");
        className("text-rose-50 text-rose-100 text-rose-200 text-rose-300 text-rose-400 text-rose-500 text-rose-600 text-rose-700 text-rose-800 text-rose-900 text-rose-950");
        // @formatter:on
      }
    }

    test(
        Subject.class,

        """
        .text-inherit { color: inherit }
        .text-current { color: currentColor }
        .text-transparent { color: transparent }
        .text-black { color: #000000 }
        .text-white { color: #ffffff }
        .text-slate-50 { color: #f8fafc }
        .text-slate-100 { color: #f1f5f9 }
        .text-slate-200 { color: #e2e8f0 }
        .text-slate-300 { color: #cbd5e1 }
        .text-slate-400 { color: #94a3b8 }
        .text-slate-500 { color: #64748b }
        .text-slate-600 { color: #475569 }
        .text-slate-700 { color: #334155 }
        .text-slate-800 { color: #1e293b }
        .text-slate-900 { color: #0f172a }
        .text-slate-950 { color: #020617 }
        .text-gray-50 { color: #f9fafb }
        .text-gray-100 { color: #f3f4f6 }
        .text-gray-200 { color: #e5e7eb }
        .text-gray-300 { color: #d1d5db }
        .text-gray-400 { color: #9ca3af }
        .text-gray-500 { color: #6b7280 }
        .text-gray-600 { color: #4b5563 }
        .text-gray-700 { color: #374151 }
        .text-gray-800 { color: #1f2937 }
        .text-gray-900 { color: #111827 }
        .text-gray-950 { color: #030712 }
        .text-zinc-50 { color: #fafafa }
        .text-zinc-100 { color: #f4f4f5 }
        .text-zinc-200 { color: #e4e4e7 }
        .text-zinc-300 { color: #d4d4d8 }
        .text-zinc-400 { color: #a1a1aa }
        .text-zinc-500 { color: #71717a }
        .text-zinc-600 { color: #52525b }
        .text-zinc-700 { color: #3f3f46 }
        .text-zinc-800 { color: #27272a }
        .text-zinc-900 { color: #18181b }
        .text-zinc-950 { color: #09090b }
        .text-neutral-50 { color: #fafafa }
        .text-neutral-100 { color: #f5f5f5 }
        .text-neutral-200 { color: #e5e5e5 }
        .text-neutral-300 { color: #d4d4d4 }
        .text-neutral-400 { color: #a3a3a3 }
        .text-neutral-500 { color: #737373 }
        .text-neutral-600 { color: #525252 }
        .text-neutral-700 { color: #404040 }
        .text-neutral-800 { color: #262626 }
        .text-neutral-900 { color: #171717 }
        .text-neutral-950 { color: #0a0a0a }
        .text-stone-50 { color: #fafaf9 }
        .text-stone-100 { color: #f5f5f4 }
        .text-stone-200 { color: #e7e5e4 }
        .text-stone-300 { color: #d6d3d1 }
        .text-stone-400 { color: #a8a29e }
        .text-stone-500 { color: #78716c }
        .text-stone-600 { color: #57534e }
        .text-stone-700 { color: #44403c }
        .text-stone-800 { color: #292524 }
        .text-stone-900 { color: #1c1917 }
        .text-stone-950 { color: #0c0a09 }
        .text-red-50 { color: #fef2f2 }
        .text-red-100 { color: #fee2e2 }
        .text-red-200 { color: #fecaca }
        .text-red-300 { color: #fca5a5 }
        .text-red-400 { color: #f87171 }
        .text-red-500 { color: #ef4444 }
        .text-red-600 { color: #dc2626 }
        .text-red-700 { color: #b91c1c }
        .text-red-800 { color: #991b1b }
        .text-red-900 { color: #7f1d1d }
        .text-red-950 { color: #450a0a }
        .text-orange-50 { color: #fff7ed }
        .text-orange-100 { color: #ffedd5 }
        .text-orange-200 { color: #fed7aa }
        .text-orange-300 { color: #fdba74 }
        .text-orange-400 { color: #fb923c }
        .text-orange-500 { color: #f97316 }
        .text-orange-600 { color: #ea580c }
        .text-orange-700 { color: #c2410c }
        .text-orange-800 { color: #9a3412 }
        .text-orange-900 { color: #7c2d12 }
        .text-orange-950 { color: #431407 }
        .text-amber-50 { color: #fffbeb }
        .text-amber-100 { color: #fef3c7 }
        .text-amber-200 { color: #fde68a }
        .text-amber-300 { color: #fcd34d }
        .text-amber-400 { color: #fbbf24 }
        .text-amber-500 { color: #f59e0b }
        .text-amber-600 { color: #d97706 }
        .text-amber-700 { color: #b45309 }
        .text-amber-800 { color: #92400e }
        .text-amber-900 { color: #78350f }
        .text-amber-950 { color: #451a03 }
        .text-yellow-50 { color: #fefce8 }
        .text-yellow-100 { color: #fef9c3 }
        .text-yellow-200 { color: #fef08a }
        .text-yellow-300 { color: #fde047 }
        .text-yellow-400 { color: #facc15 }
        .text-yellow-500 { color: #eab308 }
        .text-yellow-600 { color: #ca8a04 }
        .text-yellow-700 { color: #a16207 }
        .text-yellow-800 { color: #854d0e }
        .text-yellow-900 { color: #713f12 }
        .text-yellow-950 { color: #422006 }
        .text-lime-50 { color: #f7fee7 }
        .text-lime-100 { color: #ecfccb }
        .text-lime-200 { color: #d9f99d }
        .text-lime-300 { color: #bef264 }
        .text-lime-400 { color: #a3e635 }
        .text-lime-500 { color: #84cc16 }
        .text-lime-600 { color: #65a30d }
        .text-lime-700 { color: #4d7c0f }
        .text-lime-800 { color: #3f6212 }
        .text-lime-900 { color: #365314 }
        .text-lime-950 { color: #1a2e05 }
        .text-green-50 { color: #f0fdf4 }
        .text-green-100 { color: #dcfce7 }
        .text-green-200 { color: #bbf7d0 }
        .text-green-300 { color: #86efac }
        .text-green-400 { color: #4ade80 }
        .text-green-500 { color: #22c55e }
        .text-green-600 { color: #16a34a }
        .text-green-700 { color: #15803d }
        .text-green-800 { color: #166534 }
        .text-green-900 { color: #14532d }
        .text-green-950 { color: #052e16 }
        .text-emerald-50 { color: #ecfdf5 }
        .text-emerald-100 { color: #d1fae5 }
        .text-emerald-200 { color: #a7f3d0 }
        .text-emerald-300 { color: #6ee7b7 }
        .text-emerald-400 { color: #34d399 }
        .text-emerald-500 { color: #10b981 }
        .text-emerald-600 { color: #059669 }
        .text-emerald-700 { color: #047857 }
        .text-emerald-800 { color: #065f46 }
        .text-emerald-900 { color: #064e3b }
        .text-emerald-950 { color: #022c22 }
        .text-teal-50 { color: #f0fdfa }
        .text-teal-100 { color: #ccfbf1 }
        .text-teal-200 { color: #99f6e4 }
        .text-teal-300 { color: #5eead4 }
        .text-teal-400 { color: #2dd4bf }
        .text-teal-500 { color: #14b8a6 }
        .text-teal-600 { color: #0d9488 }
        .text-teal-700 { color: #0f766e }
        .text-teal-800 { color: #115e59 }
        .text-teal-900 { color: #134e4a }
        .text-teal-950 { color: #042f2e }
        .text-cyan-50 { color: #ecfeff }
        .text-cyan-100 { color: #cffafe }
        .text-cyan-200 { color: #a5f3fc }
        .text-cyan-300 { color: #67e8f9 }
        .text-cyan-400 { color: #22d3ee }
        .text-cyan-500 { color: #06b6d4 }
        .text-cyan-600 { color: #0891b2 }
        .text-cyan-700 { color: #0e7490 }
        .text-cyan-800 { color: #155e75 }
        .text-cyan-900 { color: #164e63 }
        .text-cyan-950 { color: #083344 }
        .text-sky-50 { color: #f0f9ff }
        .text-sky-100 { color: #e0f2fe }
        .text-sky-200 { color: #bae6fd }
        .text-sky-300 { color: #7dd3fc }
        .text-sky-400 { color: #38bdf8 }
        .text-sky-500 { color: #0ea5e9 }
        .text-sky-600 { color: #0284c7 }
        .text-sky-700 { color: #0369a1 }
        .text-sky-800 { color: #075985 }
        .text-sky-900 { color: #0c4a6e }
        .text-sky-950 { color: #082f49 }
        .text-blue-50 { color: #eff6ff }
        .text-blue-100 { color: #dbeafe }
        .text-blue-200 { color: #bfdbfe }
        .text-blue-300 { color: #93c5fd }
        .text-blue-400 { color: #60a5fa }
        .text-blue-500 { color: #3b82f6 }
        .text-blue-600 { color: #2563eb }
        .text-blue-700 { color: #1d4ed8 }
        .text-blue-800 { color: #1e40af }
        .text-blue-900 { color: #1e3a8a }
        .text-blue-950 { color: #172554 }
        .text-indigo-50 { color: #eef2ff }
        .text-indigo-100 { color: #e0e7ff }
        .text-indigo-200 { color: #c7d2fe }
        .text-indigo-300 { color: #a5b4fc }
        .text-indigo-400 { color: #818cf8 }
        .text-indigo-500 { color: #6366f1 }
        .text-indigo-600 { color: #4f46e5 }
        .text-indigo-700 { color: #4338ca }
        .text-indigo-800 { color: #3730a3 }
        .text-indigo-900 { color: #312e81 }
        .text-indigo-950 { color: #1e1b4b }
        .text-violet-50 { color: #f5f3ff }
        .text-violet-100 { color: #ede9fe }
        .text-violet-200 { color: #ddd6fe }
        .text-violet-300 { color: #c4b5fd }
        .text-violet-400 { color: #a78bfa }
        .text-violet-500 { color: #8b5cf6 }
        .text-violet-600 { color: #7c3aed }
        .text-violet-700 { color: #6d28d9 }
        .text-violet-800 { color: #5b21b6 }
        .text-violet-900 { color: #4c1d95 }
        .text-violet-950 { color: #2e1065 }
        .text-purple-50 { color: #faf5ff }
        .text-purple-100 { color: #f3e8ff }
        .text-purple-200 { color: #e9d5ff }
        .text-purple-300 { color: #d8b4fe }
        .text-purple-400 { color: #c084fc }
        .text-purple-500 { color: #a855f7 }
        .text-purple-600 { color: #9333ea }
        .text-purple-700 { color: #7e22ce }
        .text-purple-800 { color: #6b21a8 }
        .text-purple-900 { color: #581c87 }
        .text-purple-950 { color: #3b0764 }
        .text-fuchsia-50 { color: #fdf4ff }
        .text-fuchsia-100 { color: #fae8ff }
        .text-fuchsia-200 { color: #f5d0fe }
        .text-fuchsia-300 { color: #f0abfc }
        .text-fuchsia-400 { color: #e879f9 }
        .text-fuchsia-500 { color: #d946ef }
        .text-fuchsia-600 { color: #c026d3 }
        .text-fuchsia-700 { color: #a21caf }
        .text-fuchsia-800 { color: #86198f }
        .text-fuchsia-900 { color: #701a75 }
        .text-fuchsia-950 { color: #4a044e }
        .text-pink-50 { color: #fdf2f8 }
        .text-pink-100 { color: #fce7f3 }
        .text-pink-200 { color: #fbcfe8 }
        .text-pink-300 { color: #f9a8d4 }
        .text-pink-400 { color: #f472b6 }
        .text-pink-500 { color: #ec4899 }
        .text-pink-600 { color: #db2777 }
        .text-pink-700 { color: #be185d }
        .text-pink-800 { color: #9d174d }
        .text-pink-900 { color: #831843 }
        .text-pink-950 { color: #500724 }
        .text-rose-50 { color: #fff1f2 }
        .text-rose-100 { color: #ffe4e6 }
        .text-rose-200 { color: #fecdd3 }
        .text-rose-300 { color: #fda4af }
        .text-rose-400 { color: #fb7185 }
        .text-rose-500 { color: #f43f5e }
        .text-rose-600 { color: #e11d48 }
        .text-rose-700 { color: #be123c }
        .text-rose-800 { color: #9f1239 }
        .text-rose-900 { color: #881337 }
        .text-rose-950 { color: #4c0519 }
        """
    );
  }

  @Test
  public void responsive() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("m-0 sm:block sm:m-1 md:m-2 lg:m-3 xl:m-4 2xl:m-5");
      }
    }

    test(
        Subject.class,

        """
        .m-0 { margin: 0px }

        @media (min-width: 640px) {
          .sm\\:m-1 { margin: 0.25rem }
          .sm\\:block { display: block }
        }

        @media (min-width: 768px) {
          .md\\:m-2 { margin: 0.5rem }
        }

        @media (min-width: 1024px) {
          .lg\\:m-3 { margin: 0.75rem }
        }

        @media (min-width: 1280px) {
          .xl\\:m-4 { margin: 1rem }
        }

        @media (min-width: 1536px) {
          .\\32xl\\:m-5 { margin: 1.25rem }
        }
        """
    );
  }

  private void test(Class<?> type, String expected) {
    WayStyleGen gen;
    gen = new WayStyleGen();

    gen.noteSink(TestingNoteSink.INSTANCE);

    String result;
    result = gen.generate(Set.of(type));

    assertEquals(result, expected);
  }

}