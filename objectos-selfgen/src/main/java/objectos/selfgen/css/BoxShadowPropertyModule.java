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
final class BoxShadowPropertyModule extends AbstractPropertyModule {

  @Override
  final void propertyDefinition() {
    // KeywordName currentcolor = keyword("currentcolor");
    KeywordName inset = keyword("inset");
    KeywordName none = keyword("none");

    propertyHash(
      "box-shadow",

      formal(
        Source.MDN,
        "none | <shadow>#",

        "<shadow> = inset? && <length>{2,4} && <color>?",
        "<color> = <rgb()> | <rgba()> | <hsl()> | <hsla()> | <hex-color> | <named-color> | currentcolor | <deprecated-system-color>",
        "<rgb()> = rgb( <percentage>{3} [ / <alpha-value> ]? ) | rgb( <number>{3} [ / <alpha-value> ]? ) | rgb( <percentage>#{3} , <alpha-value>? ) | rgb( <number>#{3} , <alpha-value>? )",
        "<rgba()> = rgba( <percentage>{3} [ / <alpha-value> ]? ) | rgba( <number>{3} [ / <alpha-value> ]? ) | rgba( <percentage>#{3} , <alpha-value>? ) | rgba( <number>#{3} , <alpha-value>? )",
        "<hsl()> = hsl( <hue> <percentage> <percentage> [ / <alpha-value> ]? ) | hsl( <hue>, <percentage>, <percentage>, <alpha-value>? )",
        "<hsla()> = hsla( <hue> <percentage> <percentage> [ / <alpha-value> ]? ) | hsla( <hue>, <percentage>, <percentage>, <alpha-value>? )",
        "<alpha-value> = <number> | <percentage>",
        "<hue> = <number> | <angle>"
      ),

      globalSig,

      sig(none, "none"),

      sig(
        length, "offsetX",
        length, "offsetY",
        color, "color"
      ),

      sig(
        length, "offsetX",
        length, "offsetY",
        length, "blurRadius",
        color, "color"
      ),

      sig(
        length, "offsetX",
        length, "offsetY",
        length, "blurRadius",
        length, "spreadRadius",
        color, "color"
      ),

      sig(
        inset, "inset",
        length, "offsetX",
        length, "offsetY",
        color, "color"
      ),

      sig(
        inset, "inset",
        length, "offsetX",
        length, "offsetY",
        length, "blurRadius",
        color, "color"
      ),

      sig(
        inset, "inset",
        length, "offsetX",
        length, "offsetY",
        length, "blurRadius",
        length, "spreadRadius",
        color, "color"
      ),

      sigHash()
    );
  }

}