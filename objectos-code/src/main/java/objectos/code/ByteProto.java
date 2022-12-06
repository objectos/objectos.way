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

final class ByteProto {

  static final int NULL = Integer.MIN_VALUE;

  //internal instructions

  static final int JMP = -1;
  static final int BREAK = -2;
  static final int NEW_LINE = -3;
  static final int EOF = -4;

  //types

  static final int ARRAY_INITIALIZER = -5;
  static final int ARRAY_TYPE = -6;
  static final int CLASS_TYPE = -7;
  static final int DIM = -8;
  static final int NO_TYPE = -9;
  static final int PACKAGE_NAME = -10;
  static final int PARAMETERIZED_TYPE = -11;
  static final int PRIMITIVE_TYPE = -12;
  static final int SIMPLE_NAME = -13;
  static final int TYPE_VARIABLE = -14;

  //declarations

  static final int COMPILATION_UNIT = -15;
  static final int PACKAGE_DECLARATION = -16;
  static final int TYPE_PARAMETER = -17;

  //class

  static final int CLASS_DECLARATION = -18;
  static final int MODIFIER = -19;
  static final int IDENTIFIER = -20;
  static final int EXTENDS_SINGLE = -21;
  static final int IMPLEMENTS = -22;

  //field

  static final int FIELD_DECLARATION = -23;

  //method/constructor

  static final int METHOD_DECLARATION = -24;
  static final int FORMAL_PARAMETER = -25;
  static final int ELLIPSIS = -26;
  static final int CONSTRUCTOR_DECLARATION = -27;
  static final int THIS_INVOCATION = -28;
  static final int SUPER_INVOCATION = -29;
  static final int QUALIFIED_SUPER_INVOCATION = -30;

  //enum

  static final int ENUM_DECLARATION = -31;
  static final int ENUM_CONSTANT = -32;

  //interface

  static final int INTERFACE_DECLARATION = -33;
  static final int EXTENDS_MANY = -34;

  //annotation

  static final int ANNOTATION = -35;

  //statements

  static final int BLOCK = -36;
  static final int LOCAL_VARIABLE = -37;
  static final int RETURN_STATEMENT = -38;

  //expressions

  static final int ARRAY_ACCESS_EXPRESSION = -39;
  static final int ASSIGNMENT_EXPRESSION = -40;
  static final int ASSIGNMENT_OPERATOR = -41;
  static final int CHAINED_METHOD_INVOCATION = -42;
  static final int CLASS_INSTANCE_CREATION0 = -43;
  static final int EXPRESSION_NAME = -44;
  static final int FIELD_ACCESS_EXPRESSION0 = -45;
  static final int METHOD_INVOCATION = -46;
  static final int METHOD_INVOCATION_QUALIFIED = -47;
  static final int PRIMITIVE_LITERAL = -48;
  static final int STRING_LITERAL = -49;
  static final int THIS = -50;

  private ByteProto() {}

  public static boolean isExpression(int proto) {
    return proto <= ARRAY_ACCESS_EXPRESSION
        && proto >= THIS;
  }

  public static boolean isExpressionStatement(int proto) {
    return switch (proto) {
      case ASSIGNMENT_EXPRESSION,
          CHAINED_METHOD_INVOCATION,
          CLASS_INSTANCE_CREATION0,
          METHOD_INVOCATION,
          METHOD_INVOCATION_QUALIFIED -> true;

      default -> false;
    };
  }

}