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
final class JustifyContentPropertyModule extends AbstractAlignOrJustifyPropertyModule {

  @Override
  final void propertyDefinitionImpl() {
    ValueType contentPositionOrLeftOrRight = t(
      "ContentPositionOrLeftOrRight",
      contentPosition, left, right
    );

    ValueType justifyContent = t(
      "JustifyContentValue",
      normal, contentDistribution, contentPositionOrLeftOrRight
    );

    property(
      "justify-content",

      formal(
        Source.MDN,
        "normal | <content-distribution> | <overflow-position>? [ <content-position> | left | right ]",

        "<content-distribution> = space-between | space-around | space-evenly | stretch",
        "<overflow-position> = unsafe | safe",
        "<content-position> = center | start | end | flex-start | flex-end"
      ),

      globalSig,

      sig(justifyContent, "value"),
      sig(overflowPosition, "safeOrUnsafe", contentPositionOrLeftOrRight, "position")
    );
  }

}