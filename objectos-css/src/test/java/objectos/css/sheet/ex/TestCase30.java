/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.css.sheet.ex;

import objectos.css.sheet.AbstractStyleSheet;

public class TestCase30 extends AbstractStyleSheet {

  @Override
  protected final void definition() {
    style(
        cn("rgb"),

        // rgb: int
        color(rgb(0, 1, 2)),

        // rgb: double
        color(rgb(0, 100.1, 255)),

        // rgb: int / alpha
        color(rgb(0, 127, 255, 0.5)),

        // rgb: double / alpha
        color(rgb(0.1, 0.2, 0.3, 0.4)),

        // rgb: int / alpha
        color(rgba(0, 127, 255, 0.5)),

        // rgb: double / alpha
        color(rgba(0.1, 0.2, 0.3, 0.4))
    );
  }

}
