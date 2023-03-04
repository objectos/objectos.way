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
package br.com.objectos.html.tmpl;

class ByteProto {

  static final int START_ELEMENT = -1;

  static final int START_TAG = -2;

  static final int BOOLEAN_STD = -3;

  static final int STRING_STD = -4;

  static final int BOOLEAN_ATTR = -5;

  static final int STRING_ATTR = -6;

  static final int MARK_BOOLEAN_STD = -7;

  static final int MARK_STRING_STD = -8;

  static final int MARK_BOOLEAN_ATTR = -9;

  static final int MARK_STRING_ATTR = -10;

  static final int MARK_ELEMENT = -11;

  static final int MARK_TAG = -12;

  static final int GT = -13;

  static final int END_ELEMENT = -14;

  static final int END_TAG = -15;

  static final int SELF_CLOSING_TAG = -16;

  static final int ROOT_START = -17;

  static final int ROOT_END = -18;

  static final int TEXT = -19;

  static final int MARK_TEXT_ELEMENT = -20;

  static final int TEXT_ELEMENT = -21;

  static final int MARK_MAYBE_ATTR = -22;

  static final int MARK_MAYBE_ELEMENT = -23;

  static final int ATTR_OR_ELEMENT = -24;

  static final int MARK_TEMPLATE = -25;

  static final int RAW_ELEMENT = -26;

  static final int MARK_RAW_ELEMENT = -27;

  private ByteProto() {}

}
