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
  public static final byte INTERNAL5 = -3;
  public static final byte MARKED = -4;
  public static final byte MARKED5 = -5;
  public static final byte NULL = -6;
  public static final byte STANDARD_NAME = -7;

  //elements

  public static final byte DOCTYPE = -8;
  public static final byte ELEMENT = -9;
  public static final byte FRAGMENT = -10;

  //attributes

  public static final byte ATTRIBUTE0 = -11;
  public static final byte ATTRIBUTE1 = -12;
  public static final byte ATTRIBUTE_CLASS = -13;
  public static final byte ATTRIBUTE_ID = -14;

  private ByteProto2() {}

}