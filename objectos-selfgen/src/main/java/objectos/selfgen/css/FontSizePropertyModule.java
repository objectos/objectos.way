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
final class FontSizePropertyModule extends AbstractPropertyModule {

  @Override
  final void propertyDefinition() {
    KeywordName large = keyword("large");
    KeywordName larger = keyword("larger");
    KeywordName medium = keyword("medium");
    KeywordName small = keyword("small");
    KeywordName smaller = keyword("smaller");
    KeywordName xLarge = keyword("x-large");
    KeywordName xSmall = keyword("x-small");
    KeywordName xxLarge = keyword("xx-large");
    KeywordName xxSmall = keyword("xx-small");
    KeywordName xxxLarge = keyword("xxx-large");

    ValueType fontSize = t(
      "FontSizeValue",
      xxSmall, xSmall, small, medium, large, xLarge, xxLarge, xxxLarge,
      larger, smaller,
      length, percentage
    );

    property(
      "font-size",

      formal(
        Source.MDN,
        "<absolute-size> | <relative-size> | <length-percentage>",

        "<absolute-size> = xx-small | x-small | small | medium | large | x-large | xx-large | xxx-large",
        "<relative-size> = larger | smaller",
        "<length-percentage> = <length> | <percentage>"
      ),

      globalSig,

      sig(fontSize, "size")
    );
  }

}