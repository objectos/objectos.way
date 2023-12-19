/*
 * Copyright (C) 2023 Objectos Software LTDA.
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

final class BytePseudo {

  //document

  public static final byte DOCUMENT_ITERABLE = -1;
  public static final byte DOCUMENT_ITERATOR = -2;
  public static final byte DOCUMENT_HAS_NEXT = -3;
  public static final byte DOCUMENT_EXHAUSTED = -4;

  //instructions

  public static final byte DOCTYPE = -5;
  public static final byte NULL = -6;
  public static final byte RETURN = -7;

  //element

  public static final byte ELEMENT = -8;
  public static final byte ELEMENT_START = -9;
  public static final byte ELEMENT_ATTRS_ITERABLE = -10;
  public static final byte ELEMENT_ATTRS_ITERATOR = -11;
  public static final byte ELEMENT_ATTRS_HAS_NEXT = -12;
  public static final byte ELEMENT_ATTRS_EXHAUSTED = -13;
  public static final byte ELEMENT_NODES_ITERABLE = -14;
  public static final byte ELEMENT_NODES_ITERATOR = -15;
  public static final byte ELEMENT_NODES_HAS_NEXT = -16;
  public static final byte ELEMENT_NODES_EXHAUSTED = -17;

  //attribute

  public static final byte ATTR_NAME = -18;
  public static final byte ATTR_HAS_VALUE = -19;
  public static final byte ATTR_EXHAUSTED = -20;

  //text

  public static final byte TEXT = -21;
  public static final byte RAW = -22;

  private BytePseudo() {}

}
