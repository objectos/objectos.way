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
  static final int OBJECT_END = -3;
  static final int NEW_LINE = -4;
  static final int EOF = -5;
  static final int INVOKE_METHOD_NAME = -6;
  static final int VAR_NAME = -7;

  //types

  static final int ARRAY_TYPE = -8;
  static final int CLASS_TYPE = -9;
  static final int NO_TYPE = -10;
  static final int PARAMETERIZED_TYPE = -11;
  static final int PRIMITIVE_TYPE = -12;
  static final int TYPE_VARIABLE = -13;

  //type aux

  static final int ARRAY_INITIALIZER = -14;
  static final int DIM = -15;
  static final int PACKAGE_NAME = -16;
  static final int SIMPLE_NAME = -17;

  //declarations

  static final int BODY = -18;
  static final int CLASS = -19;

  //declarations v2

  static final int COMPILATION_UNIT = -20;
  static final int PACKAGE_DECLARATION = -21;
  static final int AUTO_IMPORTS = -22;
  static final int TYPE_PARAMETER = -23;

  //class

  static final int CLASS_DECLARATION = -24;
  static final int MODIFIER = -25;
  static final int IDENTIFIER = -26;
  static final int EXTENDS_SINGLE = -27;
  static final int IMPLEMENTS = -28;

  //field

  static final int FIELD_DECLARATION = -29;

  //method/constructor

  static final int METHOD_DECLARATION = -30;
  static final int FORMAL_PARAMETER = -31;
  static final int ELLIPSIS = -32;
  static final int CONSTRUCTOR_DECLARATION = -33;
  static final int THIS_INVOCATION = -34;
  static final int SUPER_INVOCATION = -35;
  static final int QUALIFIED_SUPER_INVOCATION = -36;

  //enum

  static final int ENUM_DECLARATION = -37;
  static final int ENUM_CONSTANT = -38;

  //interface

  static final int INTERFACE_DECLARATION = -39;
  static final int EXTENDS_MANY = -40;

  //annotation

  static final int ANNOTATION = -41;

  //statements

  static final int BLOCK = -42;
  static final int LOCAL_VARIABLE = -43;
  static final int RETURN_STATEMENT = -44;

  //expressions

  static final int ARRAY_ACCESS_EXPRESSION = -45;
  static final int ASSIGNMENT_EXPRESSION = -46;
  static final int ASSIGNMENT_OPERATOR = -47;
  static final int CHAINED_METHOD_INVOCATION = -48;
  static final int CLASS_INSTANCE_CREATION0 = -49;
  static final int EXPRESSION_NAME = -50;
  static final int FIELD_ACCESS_EXPRESSION0 = -51;
  static final int METHOD_INVOCATION = -52;
  static final int METHOD_INVOCATION_QUALIFIED = -53;
  static final int PRIMITIVE_LITERAL = -54;
  static final int STRING_LITERAL = -55;
  static final int THIS = -56;

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

  public static boolean isType(int proto) {
    return proto <= ARRAY_TYPE
        && proto >= TYPE_VARIABLE;
  }

}