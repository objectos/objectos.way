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

import objectos.selfgen.css.spec.CssSpec;
import objectos.selfgen.css.spec.KeywordName;
import objectos.selfgen.css.spec.MethodSignature;
import objectos.selfgen.css.spec.ParameterType;
import objectos.selfgen.css.spec.Primitive;
import objectos.selfgen.css.spec.PrimitiveType;
import objectos.selfgen.css.spec.ValueType;

abstract class AbstractPropertyModule extends CssSpec {

  protected PrimitiveType angle;

  protected PrimitiveType color;

  protected PrimitiveType doubleType;

  protected MethodSignature globalSig;

  protected ValueType heightOrWidthValue;

  protected PrimitiveType image;

  protected PrimitiveType integer;

  protected PrimitiveType length;

  protected ValueType numberValue;

  protected PrimitiveType percentage;

  protected PrimitiveType string;

  protected PrimitiveType uri;

  @Override
  protected final void definition() {
    angle = primitive(Primitive.ANGLE);

    color = primitive(Primitive.COLOR);

    doubleType = primitive(Primitive.DOUBLE);

    image = primitive(Primitive.IMAGE);

    integer = primitive(Primitive.INT);

    length = primitive(Primitive.LENGTH);

    percentage = primitive(Primitive.PERCENTAGE);

    string = primitive(Primitive.STRING);

    uri = primitive(Primitive.URI);

    ValueType globalKeyword;
    globalKeyword = t(
        "GlobalKeyword",
        keyword("inherit"), keyword("initial"), keyword("unset")
    );

    globalSig = sig(globalKeyword, "value");

    heightOrWidthValue = heightOrWidthValue();

    numberValue = t("NumberValue", doubleType, integer);

    propertyDefinition();
  }

  abstract void propertyDefinition();

  final MethodSignature sigXY1(ParameterType type) {
    return sig(type, "all");
  }

  final MethodSignature sigXY2(ParameterType type) {
    return sig(
        type, "vertical",
        type, "horizontal"
    );
  }

  final MethodSignature sigXY3(ParameterType type) {
    return sig(
        type, "top",
        type, "horizontal",
        type, "bottom"
    );
  }

  final MethodSignature sigXY4(ParameterType type) {
    return sig(
        type, "top",
        type, "right",
        type, "bottom",
        type, "left"
    );
  }

  private ValueType heightOrWidthValue() {
    KeywordName auto = keyword("auto");
    KeywordName maxContent = keyword("max-content");
    KeywordName minContent = keyword("min-content");
    return t("HeightOrWidthValue", auto, length, percentage, minContent, maxContent);
  }

}
