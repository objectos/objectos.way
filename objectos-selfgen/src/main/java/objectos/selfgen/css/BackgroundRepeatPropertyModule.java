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
final class BackgroundRepeatPropertyModule extends AbstractPropertyModule {

  @Override
  final void propertyDefinition() {
    KeywordName noRepeat = keyword("no-repeat");
    KeywordName repeat = keyword("repeat");
    KeywordName repeatX = keyword("repeat-x");
    KeywordName repeatY = keyword("repeat-y");
    KeywordName round = keyword("round");
    KeywordName space = keyword("space");

    ValueType arity2 = t(
      "BackgroundRepeatArity2Value",
      repeat, space, round, noRepeat
    );

    ValueType arity1 = t(
      "BackgroundRepeatArity1Value",
      repeatX, repeatY,
      arity2
    );

    property(
      "background-repeat",

      formal(
        Source.MDN,
        "<repeat-style># ",

        "<repeat-style> = repeat-x | repeat-y | [ repeat | space | round | no-repeat ]{1,2}"
      ),

      globalSig,

      sig(arity1, "value"),
      sig(arity2, "value1", arity2, "value2")
    );
  }

}