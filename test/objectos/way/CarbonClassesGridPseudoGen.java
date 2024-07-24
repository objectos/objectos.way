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

public class CarbonClassesGridPseudoGen {
  public static void main(String[] args) {
    colSpan("");
    colSpan("sm:");
    colSpan("md:");
    colSpan("lg:");
    colSpan("xl:");
    colSpan("max:");
  }

  private static void colSpan(String prefix) {
    String prop = prefix.replace(':', '_').toUpperCase();

    for (int i = 1; i <= 16; i++) {
      System.out.print("""
      /**
       * Causes an HTML element in a grid to span %d columns.
       */
      public static final Html.ClassName %sCOL_SPAN_%d = Html.className(\"%scol-span-%d\");

      """.formatted(i, prop, i, prefix, i));
    }
  }
}