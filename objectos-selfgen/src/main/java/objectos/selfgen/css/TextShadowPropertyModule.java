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

import objectos.selfgen.css.spec.Source;

@DoNotOverwrite
final class TextShadowPropertyModule extends AbstractPropertyModule {

  @Override
  final void propertyDefinition() {
    property(
      "text-shadow",

      formal(
        Source.MDN,
        "none | <shadow-t>#",

        "<shadow-t> = [ <length>{2,3} && <color>? ]",
        "<color> = <rgb()> | <rgba()> | <hsl()> | <hsla()> | <hex-color> | <named-color> | currentcolor | <deprecated-system-color>",
        "<rgb()> = rgb( <percentage>{3} [ / <alpha-value> ]? ) | rgb( <number>{3} [ / <alpha-value> ]? ) | rgb( <percentage>#{3} , <alpha-value>? ) | rgb( <number>#{3} , <alpha-value>? )",
        "<rgba()> = rgba( <percentage>{3} [ / <alpha-value> ]? ) | rgba( <number>{3} [ / <alpha-value> ]? ) | rgba( <percentage>#{3} , <alpha-value>? ) | rgba( <number>#{3} , <alpha-value>? )",
        "<hsl()> = hsl( <hue> <percentage> <percentage> [ / <alpha-value> ]? ) | hsl( <hue>, <percentage>, <percentage>, <alpha-value>? )",
        "<hsla()> = hsla( <hue> <percentage> <percentage> [ / <alpha-value> ]? ) | hsla( <hue>, <percentage>, <percentage>, <alpha-value>? )",
        "<alpha-value> = <number> | <percentage>",
        "<hue> = <number> | <angle>"
      ),

      globalSig,

      sig(length, "offsetX", length, "offsetY"),
      sig(length, "offsetX", length, "offsetY", color, "color"),
      sig(length, "offsetX", length, "offsetY", length, "blurRadius"),
      sig(length, "offsetX", length, "offsetY", length, "blurRadius", color, "color")
    );
  }

}