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

  public static final byte COMMA = -1;
  public static final byte NEXT_RULE = -2;
  public static final byte SEMICOLON = -3;
  public static final byte SEMICOLON_OPTIONAL = -4;
  public static final byte SPACE = -5;
  public static final byte SPACE_OPTIONAL = -6;
  public static final byte TAB = -7;

  //media query

  public static final byte AT_MEDIA = -8;
  public static final byte MEDIA_QUERY = -9;

  //selectors

  public static final byte SELECTOR = -10;
  public static final byte SELECTOR_ATTR = -11;
  public static final byte SELECTOR_ATTR_VALUE = -12;
  public static final byte SELECTOR_PSEUDO_CLASS = -13;
  public static final byte SELECTOR_PSEUDO_ELEMENT = -14;
  public static final byte SELECTOR_TYPE = -15;

  //blocks

  public static final byte BLOCK_START = -16;
  public static final byte BLOCK_END = -17;
  public static final byte BLOCK_EMPTY = -18;

  //property

  public static final byte PROPERTY_NAME = -19;

  //property values

  public static final byte COLOR_HEX = -20;
  public static final byte DOUBLE_LITERAL = -21;
  public static final byte INT_LITERAL = -22;
  public static final byte KEYWORD = -23;
  public static final byte LENGTH_DOUBLE = -24;
  public static final byte LENGTH_INT = -25;
  public static final byte PERCENTAGE_DOUBLE = -26;
  public static final byte PERCENTAGE_INT = -27;
  public static final byte STRING_LITERAL = -28;
  public static final byte STRING_QUOTES_OPTIONAL = -29;
  public static final byte URL = -30;
  public static final byte ZERO = -31;

  private ByteCode() {}

}