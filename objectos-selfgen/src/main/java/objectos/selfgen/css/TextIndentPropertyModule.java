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
final class TextIndentPropertyModule extends AbstractPropertyModule {

  @Override
  final void propertyDefinition() {
    var lengthPercentage = t("TextIndentValue", length, percentage);

    KeywordName eachLine = keyword("each-line");
    KeywordName hanging = keyword("hanging");

    var optional = t("TextIndentOptionalValue", eachLine, hanging);

    property(
      "text-indent",

      formal(
        Source.MDN,
        "<length-percentage> && hanging? && each-line?",

        "<length-percentage> = <length> | <percentage>"
      ),

      globalSig,

      sig(lengthPercentage, "value"),
      sig(lengthPercentage, "value", optional, "option"),
      sig(lengthPercentage, "value", optional, "option1", optional, "option2")
    );
  }

}