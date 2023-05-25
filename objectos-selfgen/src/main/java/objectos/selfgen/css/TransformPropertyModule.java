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
final class TransformPropertyModule extends AbstractPropertyModule {

  @Override
  final void propertyDefinition() {
    KeywordName none;
    none = keyword("none");

    ValueType transformValue;
    transformValue = t(
      "TransformValue",
      none,
      function("rotate", sig(angle, "angle")),
      function("rotateX", sig(angle, "angle")),
      function("rotateY", sig(angle, "angle")),
      function("rotateZ", sig(angle, "angle"))
    );

    property(
      "transform",

      formal(
        Source.MDN,
        "none | <transform-list>",

        "<transform-list> = <transform-function>+",
        "<transform-function> = <matrix()> | <translate()> | <translateX()> | <translateY()> | <scale()> | <scaleX()> | <scaleY()> | <rotate()> | <skew()> | <skewX()> | <skewY()> | <matrix3d()> | <translate3d()> | <translateZ()> | <scale3d()> | <scaleZ()> | <rotate3d()> | <rotateX()> | <rotateY()> | <rotateZ()> | <perspective()>",
        "<matrix()> = matrix( <number>#{6} )",
        "<translate()> = translate( <length-percentage> , <length-percentage>? )",
        "<translateX()> = translateX( <length-percentage> )",
        "<translateY()> = translateY( <length-percentage> )",
        "<scale()> = scale( <number> , <number>? )",
        "<scaleX()> = scaleX( <number> )",
        "<scaleY()> = scaleY( <number> )",
        "<rotate()> = rotate( [ <angle> | <zero> ] )",
        "<skew()> = skew( [ <angle> | <zero> ] , [ <angle> | <zero> ]? )",
        "<skewX()> = skewX( [ <angle> | <zero> ] )",
        "<skewY()> = skewY( [ <angle> | <zero> ] )",
        "<matrix3d()> = matrix3d( <number>#{16} )",
        "<translate3d()> = translate3d( <length-percentage> , <length-percentage> , <length> )",
        "<translateZ()> = translateZ( <length> )",
        "<scale3d()> = scale3d( <number> , <number> , <number> )",
        "<scaleZ()> = scaleZ( <number> )",
        "<rotate3d()> = rotate3d( <number> , <number> , <number> , [ <angle> | <zero> ] )",
        "<rotateX()> = rotateX( [ <angle> | <zero> ] )",
        "<rotateY()> = rotateY( [ <angle> | <zero> ] )",
        "<rotateZ()> = rotateZ( [ <angle> | <zero> ] )",
        "<perspective()> = perspective( <length> )",
        "<length-percentage> = <length> | <percentage>"
      ),

      globalSig,

      sig(transformValue, "value")
    );
  }

}