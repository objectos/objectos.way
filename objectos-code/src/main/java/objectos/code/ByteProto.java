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

  //types

  static final int ARRAY_TYPE = -2;
  static final int CLASS_TYPE = -3;
  static final int NO_TYPE = -4;
  static final int PARAMETERIZED_TYPE = -5;
  static final int PRIMITIVE_TYPE = -6;
  static final int TYPE_VARIABLE = -7;
  static final int VOID = -8;

  //type aux

  static final int ARRAY_DIMENSION = -9;
  static final int ARRAY_INITIALIZER = -10;
  static final int ELLIPSIS = -11;

  //declarations

  static final int ANNOTATION = -12;
  static final int BODY = -13;
  static final int CLASS = -14;
  static final int COMPILATION_UNIT = -15;
  static final int CONSTRUCTOR = -16;
  static final int ENUM = -17;
  static final int ENUM_CONSTANT = -18;
  static final int EXTENDS = -19;
  static final int FIELD_NAME = -20;
  static final int IDENTIFIER = -21;
  static final int IMPLEMENTS = -22;
  static final int INTERFACE = -23;
  static final int METHOD = -24;
  static final int MODIFIER = -25;
  static final int PACKAGE = -26;
  static final int TYPE_PARAMETER = -27;

  //stmt/exp aux

  static final int NEW = -28;
  static final int NEW_LINE = -29;
  static final int RETURN = -30;
  static final int SUPER = -31;
  static final int VAR = -32;

  //statements

  static final int BLOCK = -33;
  static final int LOCAL_VARIABLE = -34;
  static final int RETURN_STATEMENT = -35;
  static final int SUPER_INVOCATION = -36;

  //expressions

  static final int ARRAY_ACCESS = -37;
  static final int ASSIGNMENT = -38;
  static final int CLASS_INSTANCE_CREATION = -39;
  static final int EXPRESSION_NAME = -40;
  static final int EXPRESSION_NAME_CHAIN = -41;
  static final int FIELD_ACCESS = -42;
  static final int METHOD_INVOCATION = -43;
  static final int PRIMITIVE_LITERAL = -44;
  static final int STRING_LITERAL = -45;
  static final int THIS = -46;

  private ByteProto() {}

  public static boolean isExpression(int proto) {
    return proto <= ARRAY_ACCESS
        && proto >= THIS;
  }

  public static boolean isExpressionStatement(int proto) {
    return switch (proto) {
      case ASSIGNMENT,
           CLASS_INSTANCE_CREATION,
           METHOD_INVOCATION -> true;

      default -> false;
    };
  }

  public static boolean isType(int proto) {
    return proto <= ARRAY_TYPE
        && proto >= TYPE_VARIABLE;
  }

}