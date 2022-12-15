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

  static final int COMPILATION_UNIT = -18;
  static final int PACKAGE_DECLARATION = -19;
  static final int AUTO_IMPORTS = -20;
  static final int TYPE_PARAMETER = -21;

  //class

  static final int CLASS_DECLARATION = -22;
  static final int MODIFIER = -23;
  static final int IDENTIFIER = -24;
  static final int EXTENDS_SINGLE = -25;
  static final int IMPLEMENTS = -26;

  //field

  static final int FIELD_DECLARATION = -27;

  //method/constructor

  static final int METHOD_DECLARATION = -28;
  static final int FORMAL_PARAMETER = -29;
  static final int ELLIPSIS = -30;
  static final int CONSTRUCTOR_DECLARATION = -31;
  static final int THIS_INVOCATION = -32;
  static final int SUPER_INVOCATION = -33;
  static final int QUALIFIED_SUPER_INVOCATION = -34;

  //enum

  static final int ENUM_DECLARATION = -35;
  static final int ENUM_CONSTANT = -36;

  //interface

  static final int INTERFACE_DECLARATION = -37;
  static final int EXTENDS_MANY = -38;

  //annotation

  static final int ANNOTATION = -39;

  //statements

  static final int BLOCK = -40;
  static final int LOCAL_VARIABLE = -41;
  static final int RETURN_STATEMENT = -42;

  //expressions

  static final int ARRAY_ACCESS_EXPRESSION = -43;
  static final int ASSIGNMENT_EXPRESSION = -44;
  static final int ASSIGNMENT_OPERATOR = -45;
  static final int CHAINED_METHOD_INVOCATION = -46;
  static final int CLASS_INSTANCE_CREATION0 = -47;
  static final int EXPRESSION_NAME = -48;
  static final int FIELD_ACCESS_EXPRESSION0 = -49;
  static final int METHOD_INVOCATION = -50;
  static final int METHOD_INVOCATION_QUALIFIED = -51;
  static final int PRIMITIVE_LITERAL = -52;
  static final int STRING_LITERAL = -53;
  static final int THIS = -54;

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