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

  //  internal instructions

  static final int ROOT = -1;
  static final int NOP = -2;
  static final int EOF = -3;
  static final int LHEAD = -4;
  static final int LNEXT = -5;
  static final int LNULL = -6;
  static final int JMP = -7;
  static final int NEW_LINE = -8;
  static final int OBJECT_STRING = -9;

  //  types

  static final int ARRAY_TYPE = -10;
  static final int DIM = -11;
  static final int NO_TYPE = -12;
  static final int PRIMITIVE_TYPE = -13;
  static final int QUALIFIED_NAME = -14;
  static final int SIMPLE_NAME = -15;

  //  declarations

  static final int COMPILATION_UNIT = -16;
  static final int IMPORT = -17;
  static final int PACKAGE = -18;

  //  class

  static final int CLASS = -19;
  static final int MODIFIER = -20;
  static final int EXTENDS = -21;
  static final int IDENTIFIER = -22;

  //  field

  static final int FIELD_DECLARATION = -23;
  static final int DECLARATOR_SIMPLE = -24;
  static final int DECLARATOR_FULL = -25;

  //  method/constructor

  static final int METHOD_DECLARATION = -26;
  static final int CONSTRUCTOR_DECLARATION = -27;
  static final int FORMAL_PARAMETER = -28;

  //  enum

  static final int ENUM_DECLARATION = -29;
  static final int ENUM_CONSTANT = -30;

  //  annotation

  static final int ANNOTATION = -31;

  // statements

  static final int EXPRESSION_STATEMENT = -32;
  static final int LOCAL_VARIABLE = -33;
  static final int RETURN_STATEMENT = -34;

  // expressions

  static final int ARRAY_ACCESS_EXPRESSION = -35;
  static final int ASSIGNMENT_EXPRESSION = -36;
  static final int CLASS_INSTANCE_CREATION = -37;
  static final int EXPRESSION_NAME = -38;
  static final int FIELD_ACCESS_EXPRESSION0 = -39;
  static final int METHOD_INVOCATION = -40;
  static final int STRING_LITERAL = -41;
  static final int THIS = -42;

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