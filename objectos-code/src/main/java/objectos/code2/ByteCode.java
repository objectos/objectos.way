/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
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
package objectos.code2;

final class ByteCode {

  static final int AUTO_IMPORTS0 = -1; // no package
  static final int AUTO_IMPORTS1 = -2; // package
  static final int COMMENT = -3;
  static final int COMMENT_EOL = -4;
  static final int EOF = -5;
  static final int IDENTIFIER = -6;
  static final int INDENTATION = -7;
  static final int KEYWORD = -8;
  static final int NOP0 = -9;
  static final int RAW = -10;
  static final int SEPARATOR = -12;
  static final int STRING_LITERAL = -13;
  static final int WHITESPACE = -14;

  private ByteCode() {}

}