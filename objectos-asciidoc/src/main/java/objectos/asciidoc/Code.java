/*
 * Copyright (C) 2021-2025 Objectos Software LTDA.
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

final class Code {

  static final int DOCUMENT_START = -1;
  static final int DOCUMENT_END = -2;

  static final int HEADING_START = -3;
  static final int HEADING_END = -4;

  static final int PREAMBLE_START = -5;
  static final int PREAMBLE_END = -6;

  static final int PARAGRAPH_START = -7;
  static final int PARAGRAPH_END = -8;

  static final int TOKENS = -9;

  static final int SECTION_START = -10;
  static final int SECTION_END = -11;

  static final int ATTR_POSITIONAL = -12;
  static final int ATTR_NAMED = -13;

  static final int LISTING_BLOCK_START = -14;
  static final int LISTING_BLOCK_END = -15;

  static final int VERBATIM = -16;

  static final int ULIST_START = -17;
  static final int ULIST_END = -18;

  static final int LI_START = -19;
  static final int LI_END = -20;

  static final int INLINE_MACRO = -21;
  static final int MACRO_TARGET = -22;

  static final int URL_MACRO = -23;
  static final int URL_TARGET_START = -24;
  static final int URL_TARGET_END = -25;

  private Code() {}

}