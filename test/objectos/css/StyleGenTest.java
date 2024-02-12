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