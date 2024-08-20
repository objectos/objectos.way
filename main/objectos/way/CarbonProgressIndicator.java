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

final class CarbonProgressIndicator {

  /*<ul>*/

  public static final Html.ClassName HORIZONTAL = Html.classText("""
      flex
      """);

  public static final Html.ClassName VERTICAL = Html.classText("""
      flex flex-col
      """);

  /*<li>*/

  private static final Html.ClassName __LI = Html.classText("""
      relative inline-flex overflow-visible
      """);

  private static final Html.ClassName __LI_VERTICAL = Html.classText("""
      min-h-[3.625rem]
      """);

  public static final Html.ClassName LI_HORIZONTAL = Html.className(
      __LI
  );

  public static final Html.ClassName LI_VERTICAL = Html.className(
      __LI, __LI_VERTICAL
  );

  /*<button>*/

  private static final Html.ClassName __BUTTON = Html.classText("""
      flex text-start
      """);

  private static final Html.ClassName __BUTTON_VERTICAL = Html.classText("""

      """);

  public static final Html.ClassName BUTTON_HORIZONTAL = Html.className(
      __BUTTON
  );

  public static final Html.ClassName BUTTON_VERTICAL = Html.className(
      __BUTTON, __BUTTON_VERTICAL
  );

  /*<svg>*/

  private static final Html.ClassName __ICON = Html.classText("""

      """);

  private static final Html.ClassName __ICON_VERTICAL = Html.classText("""

      """);

  public static final Html.ClassName ICON_VERTICAL = Html.className(
      __ICON, __ICON_VERTICAL
  );

  private CarbonProgressIndicator() {}

}