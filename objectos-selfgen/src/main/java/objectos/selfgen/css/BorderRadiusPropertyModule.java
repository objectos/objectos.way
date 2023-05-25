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
final class BorderRadiusPropertyModule extends AbstractPropertyModule {

  @Override
  final void propertyDefinition() {
    ValueType borderRadius = t(
      "BorderRadiusValue",
      length, percentage
    );

    property(
      "border-radius",

      formal(
        Source.MDN,
        "<length-percentage>{1,4} [ / <length-percentage>{1,4} ]?",

        "<length-percentage> = <length> | <percentage>"
      ),

      globalSig,

      sig(
        borderRadius, "all"
      ),
      sig(
        borderRadius, "topLeftBottomRight",
        borderRadius, "topRightBottomLeft"
      ),
      sig(
        borderRadius, "topLeft",
        borderRadius, "topRightBottomLeft",
        borderRadius, "bottomRight"
      ),
      sig(
        borderRadius, "topLeft",
        borderRadius, "topRight",
        borderRadius, "bottomRight",
        borderRadius, "bottomLeft"
      )
    );

    property(
      names(
        "border-top-left-radius",
        "border-top-right-radius",
        "border-bottom-right-radius",
        "border-bottom-left-radius"
      ),

      formal(
        Source.MDN,
        "<length-percentage>{1,4} [ / <length-percentage>{1,4} ]?",

        "<length-percentage> = <length> | <percentage>"
      ),

      globalSig,

      sig(borderRadius, "value"),
      sig(borderRadius, "horizontal", borderRadius, "vertical")
    );
  }

}