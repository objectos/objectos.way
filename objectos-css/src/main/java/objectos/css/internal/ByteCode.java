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

  //selectors

  public static final byte SELECTOR = -8;
  public static final byte SELECTOR_ATTR = -9;
  public static final byte SELECTOR_ATTR_VALUE = -10;
  public static final byte SELECTOR_PSEUDO_CLASS = -11;
  public static final byte SELECTOR_PSEUDO_ELEMENT = -12;
  public static final byte SELECTOR_TYPE = -13;

  //blocks

  public static final byte BLOCK_START = -14;
  public static final byte BLOCK_END = -15;
  public static final byte BLOCK_EMPTY = -16;

  //property

  public static final byte PROPERTY_NAME = -17;

  //property values

  public static final byte DOUBLE_LITERAL = -18;
  public static final byte INT_LITERAL = -19;
  public static final byte KEYWORD = -20;
  public static final byte LENGTH_DOUBLE = -21;
  public static final byte LENGTH_INT = -22;
  public static final byte PERCENTAGE_DOUBLE = -23;
  public static final byte PERCENTAGE_INT = -24;
  public static final byte STRING_LITERAL = -25;
  public static final byte STRING_QUOTES_OPTIONAL = -26;
  public static final byte URL = -27;
  public static final byte ZERO = -28;

  private ByteCode() {}

}