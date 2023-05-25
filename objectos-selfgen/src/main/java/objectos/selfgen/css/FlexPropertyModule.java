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

import objectos.selfgen.css.spec.JavaType;
import objectos.selfgen.css.spec.KeywordName;
import objectos.selfgen.css.spec.Source;
import objectos.selfgen.css.spec.ValueType;

@DoNotOverwrite
final class FlexPropertyModule extends AbstractPropertyModule {

  @Override
  final void propertyDefinition() {
    KeywordName auto = keyword("auto");
    KeywordName none = keyword("none");

    ValueType width = heightOrWidthValue;

    property(
      "flex",

      formal(
        Source.MDN,
        "none | [ <\'flex-grow\'> <\'flex-shrink\'>? || <\'flex-basis\'> ]"
      ),

      globalSig,

      sig(t("FlexArity1Value", auto, none), "value"),
      sig(JavaType.DOUBLE, "grow"),
      sig(JavaType.INT, "grow"),

      sig(numberValue, "grow", numberValue, "shrink"),

      sig(numberValue, "grow", width, "basis"),

      sig(numberValue, "grow", numberValue, "shrink", width, "basis")
    );
  }

}