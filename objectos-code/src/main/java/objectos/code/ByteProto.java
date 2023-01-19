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

  static final int AUTO_IMPORTS = -1;
  static final int DOT = -2;

  //types

  static final int ARRAY_TYPE = -3;
  static final int CLASS_TYPE = -4;
  static final int NO_TYPE = -5;
  static final int PARAMETERIZED_TYPE = -6;
  static final int PRIMITIVE_TYPE = -7;
  static final int TYPE_VARIABLE = -8;
  static final int VOID = -9;

  //type aux

  static final int ARRAY_DIMENSION = -10;
  static final int ARRAY_INITIALIZER = -11;
  static final int ELLIPSIS = -12;

  //declarations

  static final int ANNOTATION = -13;
  static final int BODY = -14;
  static final int CLASS = -15;
  static final int COMPILATION_UNIT = -16;
  static final int CONSTRUCTOR = -17;
  static final int ENUM = -18;
  static final int ENUM_CONSTANT = -19;
  static final int EXTENDS = -20;
  static final int FIELD_NAME = -21;
  static final int IDENTIFIER = -22;
  static final int IMPLEMENTS = -23;
  static final int INTERFACE = -24;
  static final int METHOD = -25;
  static final int MODIFIER = -26;
  static final int PACKAGE = -27;
  static final int TYPE_PARAMETER = -28;

  //stmt/exp aux

  static final int GETS = -29;
  static final int NEW = -30;
  static final int NEW_LINE = -31;
  static final int RETURN = -32;
  static final int SUPER = -33;
  static final int VAR = -34;

  //statements

  static final int BLOCK = -35;
  static final int LOCAL_VARIABLE = -36;
  static final int RETURN_STATEMENT = -37;
  static final int SUPER_INVOCATION = -38;

  //expressions

  static final int ARRAY_ACCESS_EXPRESSION = -39;
  static final int ASSIGNMENT_EXPRESSION = -40;
  static final int ASSIGNMENT_OPERATOR = -41;
  static final int CHAINED_METHOD_INVOCATION = -42;
  static final int CLASS_INSTANCE_CREATION = -43;
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
           CLASS_INSTANCE_CREATION,
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