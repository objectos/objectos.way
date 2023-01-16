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
  static final int VOID = -14;

  //type aux

  static final int ARRAY_DIMENSION = -15;
  static final int ARRAY_INITIALIZER = -16;
  static final int PACKAGE_NAME = -17;
  static final int SIMPLE_NAME = -18;

  //declarations v2

  static final int ANNOTATION = -19;
  static final int BODY = -20;
  static final int CLASS = -21;
  static final int CONSTRUCTOR = -22;
  static final int EXTENDS = -23;
  static final int IMPLEMENTS = -24;
  static final int INTERFACE = -25;
  static final int PACKAGE = -26;

  //declarations old

  static final int COMPILATION_UNIT = -27;
  static final int PACKAGE_DECLARATION = -28;
  static final int AUTO_IMPORTS = -29;
  static final int TYPE_PARAMETER = -30;

  //class

  static final int CLASS_DECLARATION = -31;
  static final int MODIFIER = -32;
  static final int IDENTIFIER = -33;
  static final int EXTENDS_SINGLE = -34;

  //field

  static final int FIELD_DECLARATION = -35;

  //method/constructor

  static final int METHOD_DECLARATION = -36;
  static final int FORMAL_PARAMETER = -37;
  static final int ELLIPSIS = -38;
  static final int CONSTRUCTOR_DECLARATION = -39;
  static final int THIS_INVOCATION = -40;
  static final int QUALIFIED_SUPER_INVOCATION = -41;

  //enum

  static final int ENUM_DECLARATION = -42;
  static final int ENUM_CONSTANT = -43;

  //interface

  static final int INTERFACE_DECLARATION = -44;
  static final int EXTENDS_MANY = -45;

  //stmt/exp aux

  static final int ARRAY_ACCESS = -46;
  static final int END = -47;
  static final int GETS = -48;
  static final int NEW = -49;
  static final int RETURN = -50;
  static final int SUPER = -51;
  static final int VAR = -52;

  //statements

  static final int BLOCK = -53;
  static final int LOCAL_VARIABLE = -54;
  static final int RETURN_STATEMENT = -55;
  static final int SUPER_INVOCATION = -56;

  //expressions

  static final int ARRAY_ACCESS_EXPRESSION = -57;
  static final int ASSIGNMENT_EXPRESSION = -58;
  static final int ASSIGNMENT_OPERATOR = -59;
  static final int CHAINED_METHOD_INVOCATION = -60;
  static final int CLASS_INSTANCE_CREATION = -61;
  static final int EXPRESSION_NAME = -62;
  static final int FIELD_ACCESS_EXPRESSION0 = -63;
  static final int METHOD_INVOCATION = -64;
  static final int METHOD_INVOCATION_QUALIFIED = -65;
  static final int PRIMITIVE_LITERAL = -66;
  static final int STRING_LITERAL = -67;
  static final int THIS = -68;

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