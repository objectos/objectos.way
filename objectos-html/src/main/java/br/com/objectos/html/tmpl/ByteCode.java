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

class ByteCode {

  static final int START_ELEMENT = -1;
  
  static final int START_TAG = -2;

  static final int BOOLEAN_ATTR = -3;

  static final int BOOLEAN_STD = -4;
  
  static final int STRING_ATTR = -5;

  static final int STRING_STD = -6;
  
  static final int END_ELEMENT = -7;
  
  static final int END_TAG = -8;

  static final int GT = -9;

  static final int SELF_CLOSING_TAG = -10;

  static final int JMP_ELEMENT = -11;

  static final int RETURN = -12;

  static final int ROOT = -13;

  static final int TEXT = -14;

  static final int RAW = -15;

  private ByteCode() {}

}
