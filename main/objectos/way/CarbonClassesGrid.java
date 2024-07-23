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

class CarbonClassesGrid extends CarbonClassesUtils {

  /**
   * The CSS grid component.
   */
  public static final Html.ClassName GRID = Html.className(
      "mx-auto grid w-full max-w-screen-max grid-cols-4",
      "px-0px",

      "md:grid-cols-8 md:px-16px",

      "lg:grid-cols-16",

      "max:px-24px",

      "*:mx-16px"
  );

}