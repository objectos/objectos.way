/*
 * Copyright (C) 2014-2023 Objectos Software LTDA.
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
  static final int END_ELEMENT = -2;
  static final int NOOP = -3;

  //types

  static final int ARRAY_TYPE = -4;
  static final int CLASS_TYPE = -5;
  static final int NO_TYPE = -6;
  static final int PARAMETERIZED_TYPE = -7;
  static final int PRIMITIVE_TYPE = -8;
  static final int TYPE_VARIABLE = -9;
  static final int VOID = -10;

  //type aux

  static final int ARRAY_DIMENSION = -11;
  static final int ARRAY_INITIALIZER = -12;
  static final int ELLIPSIS = -13;

  //declarations

  static final int ANNOTATION = -14;
  static final int BODY = -15;
  static final int CLASS = -16;
  static final int COMPILATION_UNIT = -17;
  static final int CONSTRUCTOR = -18;
  static final int ENUM = -19;
  static final int ENUM_CONSTANT = -20;
  static final int EXTENDS = -21;
  static final int IDENTIFIER = -22;
  static final int IMPLEMENTS = -23;
  static final int INTERFACE = -24;
  static final int METHOD = -25;
  static final int MODIFIER = -26;
  static final int PACKAGE = -27;
  static final int TYPE_PARAMETER = -28;

  //statements

  static final int BLOCK = -29;
  static final int LOCAL_VARIABLE = -30;
  static final int RETURN_STATEMENT = -31;
  static final int SUPER_INVOCATION = -32;

  //expression start

  static final int INVOKE = -33;
  static final int STRING_LITERAL = -34;

  //expressions

  static final int ASSIGNMENT = -35;
  static final int CLASS_INSTANCE_CREATION = -36;
  static final int EXPRESSION_NAME = -37;
  static final int EXPRESSION_NAME_CHAIN = -38;
  static final int FIELD_ACCESS = -39;
  static final int PRIMITIVE_LITERAL = -40;
  static final int THIS = -41;

  //stmt/exp

  static final int ARRAY_ACCESS = -42;
  static final int NEW = -43;
  static final int NEW_LINE = -44;
  static final int OPERATOR = -45;
  static final int RETURN = -46;
  static final int SUPER = -47;
  static final int VAR = -48;

  private ByteProto() {}

  public static boolean isExpressionStart(int proto) {
    return proto <= INVOKE
        && proto >= STRING_LITERAL;
  }

  public static boolean isImport(int proto) {
    return proto == AUTO_IMPORTS;
  }

  public static boolean isType(int proto) {
    return proto <= ARRAY_TYPE
        && proto >= TYPE_VARIABLE;
  }

  public static boolean primaryDot(int proto) {
    return switch (proto) {
      case INVOKE -> true;

      default -> false;
    };
  }

}