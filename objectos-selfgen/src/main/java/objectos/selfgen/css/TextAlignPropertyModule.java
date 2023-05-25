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

@DoNotOverwrite
final class TextAlignPropertyModule extends AbstractPropertyModule {

  @Override
  final void propertyDefinition() {
    KeywordName center = keyword("center");
    KeywordName end = keyword("end");
    KeywordName justify = keyword("justify");
    KeywordName left = keyword("left");
    KeywordName matchParent = keyword("match-parent");
    KeywordName right = keyword("right");
    KeywordName start = keyword("start");

    property(
      "text-align",

      formal(
        Source.MDN,
        "start | end | left | right | center | justify | match-parent"
      ),

      globalSig,

      sig(t("TextAlignValue", start, end, left, right, center, justify, matchParent), "value")
    );
  }

}