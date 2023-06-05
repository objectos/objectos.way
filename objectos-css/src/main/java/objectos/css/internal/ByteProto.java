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

final class ByteProto {

  static final int NULL = Integer.MIN_VALUE;

  static final int ROOT = -1;

  static final int ROOT_END = -2;

  static final int STYLE_RULE = -3;

  static final int STYLE_RULE_END = -4;

  static final int MARKED = -5;

  static final int TYPE_SELECTOR = -6;

  static final int ID_SELECTOR = -7;

  static final int ID_SELECTOR_EXTERNAL = -8;

  static final int CLASS_SELECTOR = -9;

  static final int CLASS_SELECTOR_EXTERNAL = -10;

  static final int COMBINATOR = -11;

  static final int PSEUDO_CLASS_SELECTOR = -12;

  static final int PSEUDO_ELEMENT_SELECTOR = -13;

  static final int ATTR_NAME_SELECTOR = -14;

  static final int ATTR_VALUE_SELECTOR = -15;

  static final int ATTR_VALUE_ELEMENT = -16;

  static final int DECLARATION = -17;

  static final int KEYWORD = -18;

  private ByteProto() {}

}