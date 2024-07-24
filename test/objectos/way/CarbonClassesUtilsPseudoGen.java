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

public class CarbonClassesUtilsPseudoGen {
  public static void main(String[] args) {
    spacing("gap-y-");
  }

  private static void spacing(String prefix) {
    String prop = prefix.replace('-', '_').toUpperCase();

    for (int i = 1; i <= 13; i++) {
      System.out.print("""
      public static final Html.ClassName %s%02d = Html.className(\"%s%02d\");

      """.formatted(prop, i, prefix, i));
    }
  }
}