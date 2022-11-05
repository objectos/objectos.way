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
package objectos.code;

final class ByteCode {

  static final int NOP = -1;

  static final int COMPILATION_UNIT = -2;

  static final int IMPORT = -3;

  static final int IMPORT_ON_DEMAND = -4;

  static final int PACKAGE = -5;

  static final int CLASS = -6;

  static final int LIST = -7;

  static final int EOF = -8;

  static final int ANNOTATION = -9;

  static final int METHOD = -10;

  static final int LOCAL_VARIABLE = -11;

  static final int STRING_LITERAL = -12;

  static final int MODIFIER = -13;

  static final int METHOD_INVOCATION = -14;

  static final int LIST_CELL = -15;

  static final int JMP = -16;

  static final int NEW_LINE = -17;

  static final int EXPRESSION_NAME = -18;

  static final int LHEAD = -19;

  static final int LNEXT = -20;

  static final int LNULL = -21;

  static final int QUALIFIED_NAME = -22;

  static final int SIMPLE_NAME = -23;

  static final int IDENTIFIER = -24;

  static final int NO_TYPE = -25;

  private ByteCode() {}

}