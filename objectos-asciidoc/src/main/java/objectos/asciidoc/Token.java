/*
 * Copyright (C) 2021-2023 Objectos Software LTDA.
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
package objectos.asciidoc;

final class Token {

  static final int EOF = -1;

  static final int HEADING = -4;

  static final int BLOB = -5;

  static final int LF = -6;

  static final int MONO_START = -7;
  static final int MONO_END = -8;

  static final int BOLD_START = -9;
  static final int BOLD_END = -10;

  static final int ITALIC_START = -11;
  static final int ITALIC_END = -12;

  static final int ATTR_LIST_START = -13;
  static final int ATTR_LIST_END = -14;

  static final int ATTR_NAME = -15;
  static final int ATTR_VALUE_START = -16;
  static final int ATTR_VALUE_END = -17;

  static final int LISTING_BLOCK_DELIM = -18;

  static final int SEPARATOR = -19;

  static final int ULIST_ASTERISK = -20;
  static final int ULIST_HYPHEN = -21;

  static final int INLINE_MACRO = -22;

  static final int DOCATTR = -23;

  static final int DQUOTE = -24;
  static final int APOSTROPHE = -25;

  static final int LITERALI = -26;

  private Token() {}

}