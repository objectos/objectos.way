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

  //internal instructions

  static final int ROOT = -1;
  static final int NOP = -2;
  static final int EOF = -3;
  static final int LHEAD = -4;
  static final int LNEXT = -5;
  static final int LNULL = -6;
  static final int JMP = -7;
  static final int NEW_LINE = -8;
  static final int OBJECT_STRING = -9;

  //types

  static final int ARRAY_INITIALIZER = -10;
  static final int ARRAY_TYPE = -11;
  static final int DIM = -12;
  static final int NO_TYPE = -13;
  static final int PARAMETERIZED_TYPE = -14;
  static final int PRIMITIVE_TYPE = -15;
  static final int QUALIFIED_NAME = -16;
  static final int SIMPLE_NAME = -17;

  //declarations

  static final int COMPILATION_UNIT = -18;
  static final int IMPORT = -19;
  static final int PACKAGE = -20;
  static final int TYPE_LIST = -21;
  static final int TYPE_PARAMETER = -22;

  //class

  static final int CLASS = -23;
  static final int MODIFIER = -24;
  static final int EXTENDS_SINGLE = -25;
  static final int IDENTIFIER = -26;

  //field

  static final int FIELD_DECLARATION = -27;
  static final int DECLARATOR_SIMPLE = -28;
  static final int DECLARATOR_FULL = -29;

  //method/constructor

  static final int METHOD_DECLARATION = -30;
  static final int FORMAL_PARAMETER = -31;
  static final int CONSTRUCTOR_DECLARATION = -32;
  static final int SUPER_CONSTRUCTOR_INVOCATION = -33;
  static final int THIS_CONSTRUCTOR_INVOCATION = -34;

  //enum

  static final int ENUM_DECLARATION = -35;
  static final int ENUM_CONSTANT = -36;

  //interface

  static final int INTERFACE_DECLARATION = -37;
  static final int EXTENDS_MANY = -38;

  //annotation

  static final int ANNOTATION = -39;

  //statements

  static final int EXPRESSION_STATEMENT = -40;
  static final int LOCAL_VARIABLE = -41;
  static final int RETURN_STATEMENT = -42;

  //expressions

  static final int ARRAY_ACCESS_EXPRESSION = -43;
  static final int ASSIGNMENT_EXPRESSION = -44;
  static final int CHAINED_METHOD_INVOCATION = -45;
  static final int CLASS_INSTANCE_CREATION = -46;
  static final int EXPRESSION_NAME = -47;
  static final int FIELD_ACCESS_EXPRESSION0 = -48;
  static final int METHOD_INVOCATION = -49;
  static final int PRIMITIVE_LITERAL = -50;
  static final int STRING_LITERAL = -51;
  static final int THIS = -52;

  private ByteCode() {}

  public static boolean isExpression(int code) {
    return code <= ARRAY_ACCESS_EXPRESSION
        && code >= THIS;
  }

  public static boolean isExpressionStatement(int code) {
    return switch (code) {
      case
          ASSIGNMENT_EXPRESSION,
          CHAINED_METHOD_INVOCATION,
          CLASS_INSTANCE_CREATION,
          METHOD_INVOCATION -> true;

      default -> false;
    };
  }

  public static boolean isTypeName(int code) {
    return switch (code) {
      case
          ARRAY_TYPE,
          NO_TYPE,
          PRIMITIVE_TYPE,
          SIMPLE_NAME,
          QUALIFIED_NAME -> true;

      default -> false;
    };
  }

}