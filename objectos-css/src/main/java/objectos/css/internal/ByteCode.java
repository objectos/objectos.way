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
  public static final byte SEMICOLON = -2;
  public static final byte SEMICOLON_OPTIONAL = -3;
  public static final byte SPACE = -4;
  public static final byte SPACE_OPTIONAL = -5;
  public static final byte TAB = -6;

  //selectors

  public static final byte SELECTOR = -7;
  public static final byte SELECTOR_ATTR = -8;
  public static final byte SELECTOR_ATTR_VALUE = -9;
  public static final byte SELECTOR_TYPE = -10;

  //blocks

  public static final byte BLOCK_START = -11;
  public static final byte BLOCK_END = -12;
  public static final byte BLOCK_EMPTY = -13;

  //property

  public static final byte PROPERTY_NAME = -14;

  //property values

  public static final byte JAVA_DOUBLE = -15;
  public static final byte JAVA_INT = -16;
  public static final byte JAVA_STRING = -17;
  public static final byte KEYWORD = -18;
  public static final byte LENGTH_DOUBLE = -19;
  public static final byte LENGTH_INT = -20;
  public static final byte PERCENTAGE_DOUBLE = -21;
  public static final byte PERCENTAGE_INT = -22;
  public static final byte ZERO = -23;

  private ByteCode() {}

}