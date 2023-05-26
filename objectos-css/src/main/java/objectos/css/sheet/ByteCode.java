/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.css.sheet;

final class ByteCode {

  // please use PseudoCodeGen to populate

  static final int DECLARATION_START = -1;

  static final int FLOW_JMP = -2;

  static final int FLOW_RETURN = -3;

  static final int FUNCTION_END = -4;

  static final int FUNCTION_START = -5;

  static final int MEDIA_END = -6;

  static final int MEDIA_START = -7;

  static final int MEDIA_TYPE = -8;

  static final int MULTI_DECLARATION_SEPARATOR = -9;

  static final int ROOT = -10;

  static final int RULE_END = -11;

  static final int RULE_START = -12;

  static final int SELECTOR_ATTRIBUTE = -13;

  static final int SELECTOR_ATTRIBUTE_VALUE = -14;

  static final int SELECTOR_CLASS = -15;

  static final int SELECTOR_COMBINATOR = -16;

  static final int SELECTOR_ID = -17;

  static final int SELECTOR_PSEUDO_CLASS = -18;

  static final int SELECTOR_PSEUDO_ELEMENT = -19;

  static final int SELECTOR_TYPE = -20;

  static final int SELECTOR_UNIVERSAL = -21;

  static final int VALUE_ANGLE_DOUBLE = -22;

  static final int VALUE_ANGLE_INT = -23;

  static final int VALUE_COLOR_HEX = -24;

  static final int VALUE_COLOR_NAME = -25;

  static final int VALUE_DOUBLE = -26;

  static final int VALUE_INT = -27;

  static final int VALUE_KEYWORD = -28;

  static final int VALUE_KEYWORD_CUSTOM = -29;

  static final int VALUE_LENGTH_DOUBLE = -30;

  static final int VALUE_LENGTH_INT = -31;

  static final int VALUE_PERCENTAGE_DOUBLE = -32;

  static final int VALUE_PERCENTAGE_INT = -33;

  static final int VALUE_RGB_DOUBLE = -36;

  static final int VALUE_RGB_DOUBLE_ALPHA = -37;

  static final int VALUE_RGB_INT = -38;

  static final int VALUE_RGB_INT_ALPHA = -39;

  static final int VALUE_RGBA_DOUBLE = -34;

  static final int VALUE_RGBA_INT = -35;

  static final int VALUE_STRING = -40;

  static final int VALUE_URI = -41;

  private ByteCode() {}

}
