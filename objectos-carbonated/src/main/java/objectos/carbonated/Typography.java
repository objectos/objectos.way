/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.carbonated;

import objectos.carbonated.internal.Namespace;
import objectos.css.tmpl.Api.DoubleLiteral;
import objectos.css.tmpl.Api.IntLiteral;
import objectos.css.tmpl.Api.LengthValue;
import objectos.css.util.CustomProperty;

/**
 * Defines the typography tokens.
 */
public sealed interface Typography permits Namespace {

  // @formatter:off

  CustomProperty<IntLiteral> FONT_WEIGHT_LIGHT = CustomProperty.randomName(5);
  CustomProperty<IntLiteral> FONT_WEIGHT_REGULAR = CustomProperty.randomName(5);
  CustomProperty<IntLiteral> FONT_WEIGHT_SEMIBOLD = CustomProperty.randomName(5);

  CustomProperty<LengthValue>   BODY_COMPACT_01_FONT_SIZE = CustomProperty.randomName(5);
  CustomProperty<IntLiteral>    BODY_COMPACT_01_FONT_WEIGHT = CustomProperty.randomName(5);
  CustomProperty<DoubleLiteral> BODY_COMPACT_01_LINE_HEIGHT = CustomProperty.randomName(5);
  CustomProperty<LengthValue>   BODY_COMPACT_01_LETTER_SPACING = CustomProperty.randomName(5);
  CustomProperty<LengthValue>   BODY_01_FONT_SIZE = CustomProperty.randomName(5);
  CustomProperty<IntLiteral>    BODY_01_FONT_WEIGHT = CustomProperty.randomName(5);
  CustomProperty<DoubleLiteral> BODY_01_LINE_HEIGHT = CustomProperty.randomName(5);
  CustomProperty<LengthValue>   BODY_01_LETTER_SPACING = CustomProperty.randomName(5);
  CustomProperty<LengthValue>   BODY_02_FONT_SIZE = CustomProperty.randomName(5);
  CustomProperty<IntLiteral>    BODY_02_FONT_WEIGHT = CustomProperty.randomName(5);
  CustomProperty<DoubleLiteral> BODY_02_LINE_HEIGHT = CustomProperty.randomName(5);
  CustomProperty<LengthValue>   BODY_02_LETTER_SPACING = CustomProperty.randomName(5);
  CustomProperty<LengthValue>   HEADING_COMPACT_01_FONT_SIZE = CustomProperty.randomName(5);
  CustomProperty<IntLiteral>    HEADING_COMPACT_01_FONT_WEIGHT = CustomProperty.randomName(5);
  CustomProperty<DoubleLiteral> HEADING_COMPACT_01_LINE_HEIGHT = CustomProperty.randomName(5);
  CustomProperty<LengthValue>   HEADING_COMPACT_01_LETTER_SPACING = CustomProperty.randomName(5);
  CustomProperty<LengthValue>   HEADING_01_FONT_SIZE = CustomProperty.randomName(5);
  CustomProperty<IntLiteral>    HEADING_01_FONT_WEIGHT = CustomProperty.randomName(5);
  CustomProperty<DoubleLiteral> HEADING_01_LINE_HEIGHT = CustomProperty.randomName(5);
  CustomProperty<LengthValue>   HEADING_01_LETTER_SPACING = CustomProperty.randomName(5);
  CustomProperty<LengthValue>   HEADING_02_FONT_SIZE = CustomProperty.randomName(5);
  CustomProperty<IntLiteral>    HEADING_02_FONT_WEIGHT = CustomProperty.randomName(5);
  CustomProperty<DoubleLiteral> HEADING_02_LINE_HEIGHT = CustomProperty.randomName(5);
  CustomProperty<LengthValue>   HEADING_02_LETTER_SPACING = CustomProperty.randomName(5);
  CustomProperty<LengthValue>   HEADING_03_FONT_SIZE = CustomProperty.randomName(5);
  CustomProperty<IntLiteral>    HEADING_03_FONT_WEIGHT = CustomProperty.randomName(5);
  CustomProperty<DoubleLiteral> HEADING_03_LINE_HEIGHT = CustomProperty.randomName(5);
  CustomProperty<LengthValue>   HEADING_03_LETTER_SPACING = CustomProperty.randomName(5);
  CustomProperty<LengthValue>   HEADING_04_FONT_SIZE = CustomProperty.randomName(5);
  CustomProperty<IntLiteral>    HEADING_04_FONT_WEIGHT = CustomProperty.randomName(5);
  CustomProperty<DoubleLiteral> HEADING_04_LINE_HEIGHT = CustomProperty.randomName(5);
  CustomProperty<LengthValue>   HEADING_04_LETTER_SPACING = CustomProperty.randomName(5);
  CustomProperty<LengthValue>   HEADING_05_FONT_SIZE = CustomProperty.randomName(5);
  CustomProperty<IntLiteral>    HEADING_05_FONT_WEIGHT = CustomProperty.randomName(5);
  CustomProperty<DoubleLiteral> HEADING_05_LINE_HEIGHT = CustomProperty.randomName(5);
  CustomProperty<LengthValue>   HEADING_05_LETTER_SPACING = CustomProperty.randomName(5);
  CustomProperty<LengthValue>   HEADING_06_FONT_SIZE = CustomProperty.randomName(5);
  CustomProperty<IntLiteral>    HEADING_06_FONT_WEIGHT = CustomProperty.randomName(5);
  CustomProperty<DoubleLiteral> HEADING_06_LINE_HEIGHT = CustomProperty.randomName(5);
  CustomProperty<LengthValue>   HEADING_06_LETTER_SPACING = CustomProperty.randomName(5);

  // @formatter:on

}