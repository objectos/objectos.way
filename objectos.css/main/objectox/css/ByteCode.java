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
package objectox.css;

final class ByteCode {

  //symbol

  public static final byte BLOCK_START = -1;
  public static final byte BLOCK_END = -2;
  public static final byte BLOCK_EMPTY = -3;
  public static final byte COLON = -4;
  public static final byte COMMA = -5;
  public static final byte NEXT_RULE = -6;
  public static final byte PARENS_OPEN = -7;
  public static final byte PARENS_CLOSE = -8;
  public static final byte SEMICOLON = -9;
  public static final byte SEMICOLON_OPTIONAL = -10;
  public static final byte SPACE = -11;
  public static final byte SPACE_OPTIONAL = -12;
  public static final byte TAB = -13;

  //media query

  public static final byte AT_MEDIA = -14;
  public static final byte MEDIA_QUERY = -15;

  //selectors

  public static final byte SELECTOR = -16;
  public static final byte SELECTOR_ATTR = -17;
  public static final byte SELECTOR_ATTR_VALUE = -18;
  public static final byte SELECTOR_CLASS = -19;
  public static final byte SELECTOR_COMBINATOR = -20;
  public static final byte SELECTOR_ID = -21;
  public static final byte SELECTOR_PSEUDO_CLASS = -22;
  public static final byte SELECTOR_PSEUDO_ELEMENT = -23;
  public static final byte SELECTOR_TYPE = -24;

  //property

  public static final byte FUNCTION_STANDARD = -25;
  public static final byte PROPERTY_CUSTOM = -26;
  public static final byte PROPERTY_STANDARD = -27;

  //property values

  public static final byte COLOR_HEX = -28;
  public static final byte FR_DOUBLE = -29;
  public static final byte FR_INT = -30;
  public static final byte KEYWORD = -31;
  public static final byte LENGTH_DOUBLE = -32;
  public static final byte LENGTH_INT = -33;
  public static final byte LITERAL_DOUBLE = -34;
  public static final byte LITERAL_INT = -35;
  public static final byte LITERAL_STRING = -36;
  public static final byte PERCENTAGE_DOUBLE = -37;
  public static final byte PERCENTAGE_INT = -38;
  public static final byte RAW = -39;
  public static final byte STRING_QUOTES_OPTIONAL = -40;
  public static final byte URL = -41;
  public static final byte VAR = -42;
  public static final byte ZERO = -43;

  private ByteCode() {}

}