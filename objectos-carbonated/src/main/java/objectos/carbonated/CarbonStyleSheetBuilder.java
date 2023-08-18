/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.carbonated;

import objectos.css.StyleSheet;
import objectos.css.util.Length;

public final class CarbonStyleSheetBuilder {

  private Breakpoints breakpoints;

  public final StyleSheet build() {
    if (breakpoints == null) {
      // standard breakpoints

      breakpoints = new Breakpoints(
        Length.px(320),

        Length.px(672),

        Length.px(1056),

        Length.px(1312),

        Length.px(1584)
      );
    }

    CarbonStyleSheet sheet;
    sheet = new CarbonStyleSheet(breakpoints);

    return sheet.compile();
  }

}