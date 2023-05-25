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
final class BorderWidthPropertyModule extends AbstractPropertyModule {

  @Override
  final void propertyDefinition() {
    KeywordName medium = keyword("medium");
    KeywordName thick = keyword("thick");
    KeywordName thin = keyword("thin");

    ValueType borderWidth = t(
      "LineWidthValue",
      thin, medium, thick, length
    );

    property(
      "border-width",

      formal(
        Source.MDN,
        "<line-width>{1,4}",

        "<line-width> = <length> | thin | medium | thick"
      ),

      globalSig,

      sigXY1(borderWidth),
      sigXY2(borderWidth),
      sigXY3(borderWidth),
      sigXY4(borderWidth)
    );

    property(
      names("border-top-width", "border-right-width", "border-bottom-width", "border-left-width"),

      formal(
        Source.MDN,
        "<line-width>",

        "<line-width> = <length> | thin | medium | thick"
      ),

      globalSig,

      sig(borderWidth, "width")
    );
  }

}