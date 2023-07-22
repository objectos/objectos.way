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
package objectos.css.internal;

final class ByteCode {

  //symbol

  public static final byte BLOCK_START = -1;
  public static final byte BLOCK_END = -2;
  public static final byte BLOCK_EMPTY = -3;
  public static final byte COMMA = -4;
  public static final byte NEXT_RULE = -5;
  public static final byte PARENS_OPEN = -6;
  public static final byte PARENS_CLOSE = -7;
  public static final byte SEMICOLON = -8;
  public static final byte SEMICOLON_OPTIONAL = -9;
  public static final byte SPACE = -10;
  public static final byte SPACE_OPTIONAL = -11;
  public static final byte TAB = -12;

  //media query

  public static final byte AT_MEDIA = -13;
  public static final byte MEDIA_QUERY = -14;

  //selectors

  public static final byte SELECTOR = -15;
  public static final byte SELECTOR_ATTR = -16;
  public static final byte SELECTOR_ATTR_VALUE = -17;
  public static final byte SELECTOR_CLASS = -18;
  public static final byte SELECTOR_PSEUDO_CLASS = -19;
  public static final byte SELECTOR_PSEUDO_ELEMENT = -20;
  public static final byte SELECTOR_TYPE = -21;

  //property

  public static final byte PROPERTY_NAME = -22;

  //property values

  public static final byte COLOR_HEX = -23;
  public static final byte DOUBLE_LITERAL = -24;
  public static final byte INT_LITERAL = -25;
  public static final byte KEYWORD = -26;
  public static final byte LENGTH_DOUBLE = -27;
  public static final byte LENGTH_INT = -28;
  public static final byte PERCENTAGE_DOUBLE = -29;
  public static final byte PERCENTAGE_INT = -30;
  public static final byte STRING_LITERAL = -31;
  public static final byte STRING_QUOTES_OPTIONAL = -32;
  public static final byte URL = -33;
  public static final byte ZERO = -34;

  private ByteCode() {}

}