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

import objectos.carbonated.internal.CompButton;
import objectos.html.tmpl.Instruction.ElementContents;

public sealed interface Button permits CompButton {

  enum Kind {

    PRIMARY;

  }

  enum Size {

    SMALL,

    MEDIUM,

    LARGE,

    X_LARGE,

    X_LARGE_2;

  }

  static Button of() {
    return new CompButton();
  }

  Button kind(Kind value);

  ElementContents render();

  Button size(Size value);

  Button text(String value);

}