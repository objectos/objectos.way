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

import objectos.selfgen.css.spec.KeywordName;
import objectos.selfgen.css.spec.Source;
import objectos.selfgen.css.spec.ValueType;

@DoNotOverwrite
final class BackgroundPositionPropertyModule extends AbstractPropertyModule {

  @Override
  final void propertyDefinition() {
    KeywordName bottom = keyword("bottom");
    KeywordName center = keyword("center");
    KeywordName left = keyword("left");
    KeywordName right = keyword("right");
    KeywordName top = keyword("top");

    ValueType bgPosition = t(
      "BackgroundPositionValue",
      center,
      top, left, bottom, right,
      length, percentage
    );

    property(
      "background-position",

      formal(
        Source.MDN,
        "<bg-position>#",

        "<bg-position> = [ [ left | center | right | top | bottom | <length-percentage> ] | [ left | center | right | <length-percentage> ] [ top | center | bottom | <length-percentage> ] | [ center | [ left | right ] <length-percentage>? ] && [ center | [ top | bottom ] <length-percentage>? ] ]",
        "<length-percentage> = <length> | <percentage>"
      ),

      globalSig,

      sig(bgPosition, "value"),
      sig(bgPosition, "value1", bgPosition, "value2"),
      sig(bgPosition, "value1", bgPosition, "value2", bgPosition, "value3"),
      sig(bgPosition, "value1", bgPosition, "value2", bgPosition, "value3", bgPosition, "value4")
    );
  }

}