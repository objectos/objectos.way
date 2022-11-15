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

  // internal instructions

  static final int NOP = -1;
  static final int EOF = -2;
  static final int LHEAD = -3;
  static final int LNEXT = -4;
  static final int LNULL = -5;
  static final int JMP = -6;
  static final int NEW_LINE = -7;

  // types

  static final int ARRAY_TYPE = -8;
  static final int DIM = -9;
  static final int NO_TYPE = -10;
  static final int QUALIFIED_NAME = -11;
  static final int SIMPLE_NAME = -12;

  // declarations

  static final int COMPILATION_UNIT = -13;
  static final int IMPORT = -14;
  static final int PACKAGE = -15;

  // class

  static final int CLASS = -16;
  static final int MODIFIER = -17;
  static final int IDENTIFIER = -18;

  // field

  static final int FIELD_DECLARATION = -19;
  static final int DECLARATOR_SIMPLE = -20;
  static final int DECLARATOR_FULL = -21;

  // method

  static final int METHOD = -22;
  static final int FORMAL_PARAMETER = -23;

  // enum

  static final int ENUM_DECLARATION = -24;
  static final int ENUM_CONSTANT = -25;

  // annotation

  static final int ANNOTATION = -26;

  // statements

  static final int LOCAL_VARIABLE = -27;
  static final int RETURN_STATEMENT = -28;

  // expressions

  static final int ARRAY_ACCESS_EXPRESSION = -29;
  static final int EXPRESSION_NAME = -30;
  static final int METHOD_INVOCATION = -31;
  static final int STRING_LITERAL = -32;

  private ByteCode() {}

  public static boolean isExpression(int code) {
    return switch (code) {
      case ARRAY_ACCESS_EXPRESSION, METHOD_INVOCATION -> true;

      default -> false;
    };
  }

  public static boolean isExpressionStatement(int code) {
    return switch (code) {
      case METHOD_INVOCATION -> true;

      default -> false;
    };
  }

  public static boolean isTypeName(int code) {
    return switch (code) {
      case NO_TYPE, SIMPLE_NAME, QUALIFIED_NAME -> true;

      default -> false;
    };
  }

}