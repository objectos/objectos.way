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

final class ByteProto2 {

  //internal instructions

  public static final byte END = -1;
  public static final byte INTERNAL = -2;
  public static final byte INTERNAL4 = -3;
  public static final byte INTERNAL5 = -4;
  public static final byte MARKED = -5;
  public static final byte MARKED4 = -6;
  public static final byte MARKED5 = -7;
  public static final byte NULL = -8;
  public static final byte STANDARD_NAME = -9;

  //elements

  public static final byte DOCTYPE = -10;
  public static final byte ELEMENT = -11;
  public static final byte FRAGMENT = -12;
  public static final byte TEXT = -13;

  //attributes

  public static final byte ATTRIBUTE0 = -14;
  public static final byte ATTRIBUTE1 = -15;
  public static final byte ATTRIBUTE_CLASS = -16;
  public static final byte ATTRIBUTE_ID = -17;

  private ByteProto2() {}

}