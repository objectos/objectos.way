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
package objectos.html.internal;

final class ByteCode {

  //Symbols

  public static final byte GT = -1;
  public static final byte NL = -2;
  public static final byte NL_OPTIONAL = -3;
  public static final byte SPACE = -4;
  public static final byte TAB = -5;
  public static final byte TAB_BLOCK = -6;

  //Tag

  public static final byte DOCTYPE = -7;
  public static final byte ATTR_NAME = -8;
  public static final byte ATTR_VALUE = -9;
  public static final byte ATTR_VALUE_START = -10;
  public static final byte ATTR_VALUE_END = -11;
  public static final byte START_TAG = -12;
  public static final byte END_TAG = -13;

  //Nodes

  public static final byte TEXT = -14;
  public static final byte TEXT_CSS = -15;

  private ByteCode() {}

}