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
  public static final byte SPACE = -3;

  //Tag

  public static final byte START_TAG = -4;
  public static final byte ATTR_NAME = -5;
  public static final byte ATTR_VALUE = -6;
  public static final byte ATTR_VALUE_START = -7;
  public static final byte ATTR_VALUE_END = -8;
  public static final byte END_TAG = -9;

  //Stuff

  public static final byte EMPTY_ELEMENT = -10;

  private ByteCode() {}

}