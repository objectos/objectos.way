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
import objectos.selfgen.css.spec.Source;
import objectos.selfgen.css.spec.ValueType;

@DoNotOverwrite
final class ListStyleTypePropertyModule extends AbstractPropertyModule {

  @Override
  final void propertyDefinition() {
    ValueType counterStyle = t("CounterStyleValue",
      keyword("disc"),
      keyword("circle"),
      keyword("square"),
      keyword("decimal"),
      keyword("cjk-decimal"),
      keyword("decimal-leading-zero"),
      keyword("lower-roman"),
      keyword("upper-roman"),
      keyword("lower-greek"),
      keyword("lower-alpha"),
      keyword("lower-latin"),
      keyword("upper-alpha"),
      keyword("upper-latin"),
      keyword("arabic-indic"),
      keyword("-moz-arabic-indic"),
      keyword("armenian"),
      keyword("bengali"),
      keyword("-moz-bengali"),
      keyword("cambodian"),
      keyword("cjk-earthly-branch"),
      keyword("-moz-cjk-earthly-branch"),
      keyword("cjk-heavenly-stem"),
      keyword("-moz-cjk-heavenly-stem"),
      keyword("cjk-ideographic"),
      keyword("devanagari"),
      keyword("-moz-devanagari"),
      keyword("ethiopic-numeric"),
      keyword("georgian"),
      keyword("gujarati"),
      keyword("-moz-gujarati"),
      keyword("gurmukhi"),
      keyword("-moz-gurmukhi"),
      keyword("hebrew"),
      keyword("hiragana"),
      keyword("hiragana-iroha"),
      keyword("japanese-formal"),
      keyword("japanese-informal"),
      keyword("kannada"),
      keyword("-moz-kannada"),
      keyword("katakana"),
      keyword("katakana-iroha"),
      keyword("khmer"),
      keyword("-moz-khmer"),
      keyword("korean-hangul-formal"),
      keyword("korean-hanja-formal"),
      keyword("korean-hanja-informal"),
      keyword("lao"),
      keyword("-moz-lao"),
      keyword("lower-armenian"),
      keyword("malayalam"),
      keyword("-moz-malayalam"),
      keyword("mongolian"),
      keyword("myanmar"),
      keyword("-moz-myanmar"),
      keyword("oriya"),
      keyword("-moz-oriya"),
      keyword("persian"),
      keyword("-moz-persian"),
      keyword("simp-chinese-formal"),
      keyword("simp-chinese-informal"),
      keyword("tamil"),
      keyword("-moz-tamil"),
      keyword("telugu"),
      keyword("-moz-telugu"),
      keyword("thai"),
      keyword("-moz-thai"),
      keyword("tibetan"),
      keyword("trad-chinese-formal"),
      keyword("trad-chinese-informal"),
      keyword("upper-armenian"),
      keyword("disclosure-open"),
      keyword("disclosure-closed")
    );

    property(
      "list-style-type",

      formal(
        Source.MDN,
        "<counter-style> | <string> | none",

        "<counter-style> = <counter-style-name> | symbols()",
        "<counter-style-name> = <custom-ident>"
      ),

      globalSig,

      sig(t("ListStyleTypeValue", counterStyle, string, keyword("none")), "value"),
      sig(JavaType.STRING, "value")
    );
  }

}