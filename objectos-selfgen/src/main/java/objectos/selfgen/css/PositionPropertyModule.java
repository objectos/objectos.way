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
final class PositionPropertyModule extends AbstractPropertyModule {

  @Override
  final void propertyDefinition() {
    KeywordName absolute = keyword("absolute");
    KeywordName fixed = keyword("fixed");
    KeywordName relative = keyword("relative");
    KeywordName staticKw = keyword("static");
    KeywordName sticky = keyword("sticky");

    property(
      "position",

      formal(
        Source.MDN,
        "static | relative | absolute | sticky | fixed"
      ),

      globalSig,

      sig(t("PositionValue", staticKw, relative, absolute, sticky, fixed), "value")
    );
  }

}