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
  static final int NEW_LINE = -6;
  static final int OBJECT_STRING = -7;

  //types

  static final int ARRAY_INITIALIZER = -8;
  static final int ARRAY_TYPE = -9;
  static final int CLASS_TYPE = -10;
  static final int DIM = -11;
  static final int NO_TYPE = -12;
  static final int PACKAGE_NAME = -13;
  static final int PARAMETERIZED_TYPE = -14;
  static final int PRIMITIVE_TYPE = -15;
  static final int TYPE_VARIABLE = -16;

  //declarations

  static final int COMPILATION_UNIT = -17;
  static final int IMPORT = -18;
  static final int PACKAGE = -19;
  static final int TYPE_LIST = -20;
  static final int TYPE_PARAMETER = -21;

  //class

  static final int CLASS = -22;
  static final int MODIFIER = -23;
  static final int EXTENDS_SINGLE = -24;

  //field

  static final int FIELD_DECLARATION = -25;
  static final int DECLARATOR_SIMPLE = -26;
  static final int DECLARATOR_FULL = -27;

  //method/constructor

  static final int METHOD_DECLARATION = -28;
  static final int FORMAL_PARAMETER = -29;
  static final int CONSTRUCTOR_DECLARATION = -30;
  static final int SUPER_CONSTRUCTOR_INVOCATION = -31;
  static final int THIS_CONSTRUCTOR_INVOCATION = -32;

  //enum

  static final int ENUM_DECLARATION = -33;
  static final int ENUM_CONSTANT = -34;

  //interface

  static final int INTERFACE_DECLARATION = -35;
  static final int EXTENDS_MANY = -36;

  //annotation

  static final int ANNOTATION = -37;

  //statements

  static final int BLOCK = -38;
  static final int EXPRESSION_STATEMENT = -39;
  static final int LOCAL_VARIABLE = -40;
  static final int RETURN_STATEMENT = -41;

  //expressions

  static final int ARRAY_ACCESS_EXPRESSION = -42;
  static final int ASSIGNMENT_EXPRESSION = -43;
  static final int CHAINED_METHOD_INVOCATION = -44;
  static final int CLASS_INSTANCE_CREATION = -45;
  static final int EXPRESSION_NAME = -46;
  static final int FIELD_ACCESS_EXPRESSION0 = -47;
  static final int METHOD_INVOCATION = -48;
  static final int PRIMITIVE_LITERAL = -49;
  static final int STRING_LITERAL = -50;
  static final int THIS = -51;

  //v2

  static final int AUTO_IMPORTS = -52;
  static final int IDENTIFIER = -53;
  static final int NOP1 = -54;
  static final int PSEUDO_ELEMENT = -55;
  static final int RESERVED_KEYWORD = -56;
  static final int SEPARATOR = -57;

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
          CLASS_TYPE,
          NO_TYPE,
          PARAMETERIZED_TYPE,
          PRIMITIVE_TYPE -> true;

      default -> false;
    };
  }

}