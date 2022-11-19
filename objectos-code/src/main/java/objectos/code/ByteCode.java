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

  static final int ROOT = -1;
  static final int NOP = -2;
  static final int EOF = -3;
  static final int LHEAD = -4;
  static final int LNEXT = -5;
  static final int LNULL = -6;
  static final int JMP = -7;
  static final int NEW_LINE = -8;
  static final int OBJECT_STRING = -9;

  // types

  static final int ARRAY_TYPE = -10;
  static final int DIM = -11;
  static final int NO_TYPE = -12;
  static final int PRIMITIVE_TYPE = -13;
  static final int QUALIFIED_NAME = -14;
  static final int SIMPLE_NAME = -15;

  // declarations

  static final int COMPILATION_UNIT = -16;
  static final int IMPORT = -17;
  static final int PACKAGE = -18;

  // class

  static final int CLASS = -19;
  static final int MODIFIER = -20;
  static final int IDENTIFIER = -21;

  // field

  static final int FIELD_DECLARATION = -22;
  static final int DECLARATOR_SIMPLE = -23;
  static final int DECLARATOR_FULL = -24;

  // method/constructor

  static final int METHOD_DECLARATION = -25;
  static final int CONSTRUCTOR_DECLARATION = -26;
  static final int FORMAL_PARAMETER = -27;

  // enum

  static final int ENUM_DECLARATION = -28;
  static final int ENUM_CONSTANT = -29;

  // annotation

  static final int ANNOTATION = -30;

  // statements

  static final int LOCAL_VARIABLE = -31;
  static final int RETURN_STATEMENT = -32;

  // expressions

  static final int ARRAY_ACCESS_EXPRESSION = -33;
  static final int ASSIGNMENT_EXPRESSION = -34;
  static final int CLASS_INSTANCE_CREATION = -35;
  static final int EXPRESSION_NAME = -36;
  static final int FIELD_ACCESS_EXPRESSION0 = -37;
  static final int METHOD_INVOCATION = -38;
  static final int STRING_LITERAL = -39;
  static final int THIS = -40;

  private ByteCode() {}

  public static boolean isExpression(int code) {
    return code <= ARRAY_ACCESS_EXPRESSION
        && code >= THIS;
  }

  public static boolean isExpressionStatement(int code) {
    return switch (code) {
      case ASSIGNMENT_EXPRESSION,
          CLASS_INSTANCE_CREATION,
          METHOD_INVOCATION -> true;

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