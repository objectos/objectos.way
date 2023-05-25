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
final class AppearancePropertyModule extends AbstractPropertyModule {

  @Override
  final void propertyDefinition() {
    KeywordName auto = keyword("auto");
    KeywordName button = keyword("button");
    KeywordName buttonBevel = keyword("button-bevel");
    KeywordName checkbox = keyword("checkbox");
    KeywordName listbox = keyword("listbox");
    KeywordName menulist = keyword("menulist");
    KeywordName menulistButton = keyword("menulist-button");
    KeywordName meter = keyword("meter");
    KeywordName none = keyword("none");
    KeywordName progressBar = keyword("progress-bar");
    KeywordName pushButton = keyword("push-button");
    KeywordName radio = keyword("radio");
    KeywordName searchfield = keyword("searchfield");
    KeywordName sliderHorizontal = keyword("slider-horizontal");
    KeywordName squareButton = keyword("square-button");
    KeywordName textarea = keyword("textarea");
    KeywordName textfield = keyword("textfield");

    property(
      names("appearance", "-moz-appearance", "-webkit-appearance"),

      formal(
        Source.MDN,
        "none | auto | button | textfield | <compat>",

        "<compat> = searchfield | textarea | push-button | button-bevel | slider-horizontal | checkbox | radio | square-button | menulist | menulist-button | listbox | meter | progress-bar"
      ),

      globalSig,

      sig(
        t(
          "AppearanceValue",
          none, auto, button, textfield,

          searchfield, textarea, pushButton, buttonBevel, sliderHorizontal,
          checkbox, radio, squareButton, menulist,
          menulistButton, listbox, meter, progressBar
        ),
        "value"
      )
    );
  }

}