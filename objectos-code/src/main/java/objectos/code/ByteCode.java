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
  static final int PRIMITIVE_TYPE = -11;
  static final int QUALIFIED_NAME = -12;
  static final int SIMPLE_NAME = -13;

  // declarations

  static final int COMPILATION_UNIT = -14;
  static final int IMPORT = -15;
  static final int PACKAGE = -16;

  // class

  static final int CLASS = -17;
  static final int MODIFIER = -18;
  static final int IDENTIFIER = -19;

  // field

  static final int FIELD_DECLARATION = -20;
  static final int DECLARATOR_SIMPLE = -21;
  static final int DECLARATOR_FULL = -22;

  // method

  static final int METHOD = -23;
  static final int FORMAL_PARAMETER = -24;

  // enum

  static final int ENUM_DECLARATION = -25;
  static final int ENUM_CONSTANT = -26;

  // annotation

  static final int ANNOTATION = -27;

  // statements

  static final int LOCAL_VARIABLE = -28;
  static final int RETURN_STATEMENT = -29;

  // expressions

  static final int ARRAY_ACCESS_EXPRESSION = -30;
  static final int EXPRESSION_NAME = -31;
  static final int METHOD_INVOCATION = -32;
  static final int STRING_LITERAL = -33;

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