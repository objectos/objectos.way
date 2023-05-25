/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.selfgen.css;

import objectos.selfgen.css.spec.Source;
import objectos.selfgen.css.spec.ValueType;

@DoNotOverwrite
final class CursorPropertyModule extends AbstractPropertyModule {

  @Override
  final void propertyDefinition() {
    ValueType cursor = t(
      "CursorValue",
      keyword("alias"),
      keyword("all-scroll"),
      keyword("auto"),
      keyword("cell"),
      keyword("col-resize"),
      keyword("context-menu"),
      keyword("copy"),
      keyword("crosshair"),
      keyword("default"),
      keyword("e-resize"),
      keyword("ew-resize"),
      keyword("grab"),
      keyword("grabbing"),
      keyword("help"),
      keyword("move"),
      keyword("n-resize"),
      keyword("ne-resize"),
      keyword("nesw-resize"),
      keyword("no-drop"),
      keyword("none"),
      keyword("not-allowed"),
      keyword("ns-resize"),
      keyword("nw-resize"),
      keyword("nwse-resize"),
      keyword("pointer"),
      keyword("progress"),
      keyword("row-resize"),
      keyword("s-resize"),
      keyword("se-resize"),
      keyword("sw-resize"),
      keyword("text"),
      keyword("vertical-text"),
      keyword("w-resize"),
      keyword("wait"),
      keyword("zoom-in"),
      keyword("zoom-out")
    );

    property(
      "cursor",

      formal(
        Source.MDN,
        "[ [ <url> [ <x> <y> ]? , ]* [ auto | default | none | context-menu | help | pointer | progress | wait | cell | crosshair | text | vertical-text | alias | copy | move | no-drop | not-allowed | e-resize | n-resize | ne-resize | nw-resize | s-resize | se-resize | sw-resize | w-resize | ew-resize | ns-resize | nesw-resize | nwse-resize | col-resize | row-resize | all-scroll | zoom-in | zoom-out | grab | grabbing ] ] "
      ),

      globalSig,

      sig(cursor, "value")
    );
  }

}