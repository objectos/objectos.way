/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectos.way;

final class HtmlByteProto {

  // internal instructions

  public static final byte END = -1;
  public static final byte INTERNAL = -2;
  public static final byte INTERNAL3 = -3;
  public static final byte INTERNAL4 = -4;
  public static final byte INTERNAL5 = -5;
  public static final byte INTERNAL6 = -6;
  public static final byte LENGTH2 = -7;
  public static final byte LENGTH3 = -8;
  public static final byte MARKED3 = -9;
  public static final byte MARKED4 = -10;
  public static final byte MARKED5 = -11;
  public static final byte MARKED6 = -12;
  public static final byte NULL = -13;
  public static final byte STANDARD_NAME = -14;

  // elements

  public static final byte AMBIGUOUS1 = -15;
  public static final byte DOCTYPE = -16;
  public static final byte ELEMENT = -17;
  public static final byte FLATTEN = -18;
  public static final byte FRAGMENT = -19;
  public static final byte RAW = -20;
  public static final byte TEXT = -21;

  // attributes

  public static final byte ATTRIBUTE0 = -22;
  public static final byte ATTRIBUTE1 = -23;
  public static final byte ATTRIBUTE_EXT0 = -24;
  public static final byte ATTRIBUTE_EXT1 = -25;
  public static final byte CUSTOM_ATTR0 = -26;
  public static final byte CUSTOM_ATTR1 = -27;

  private HtmlByteProto() {}

}