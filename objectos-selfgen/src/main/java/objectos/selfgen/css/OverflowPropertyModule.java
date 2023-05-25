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
final class OverflowPropertyModule extends AbstractPropertyModule {

  @Override
  final void propertyDefinition() {
    KeywordName auto = keyword("auto");
    KeywordName clip = keyword("clip");
    KeywordName hidden = keyword("hidden");
    KeywordName scroll = keyword("scroll");
    KeywordName visible = keyword("visible");

    ValueType overflow = t("OverflowValue", auto, clip, hidden, scroll, visible);

    property(
      "overflow",

      formal(
        Source.MDN,
        "[ visible | hidden | clip | scroll | auto ]{1,2}"
      ),

      globalSig,

      sig(overflow, "xy"),
      sig(overflow, "x", overflow, "y")
    );

    property(
      names("overflow-block", "overflow-inline", "overflow-x", "overflow-y"),

      formal(
        Source.MDN,
        "visible | hidden | clip | scroll | auto"
      ),

      globalSig,

      sig(overflow, "value")
    );
  }

}