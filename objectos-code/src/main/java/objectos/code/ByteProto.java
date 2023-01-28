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
  static final int END = -2;
  static final int END_ELEMENT = -3;
  static final int NOOP = -4;

  //types

  static final int ARRAY_TYPE = -5;
  static final int CLASS_TYPE = -6;
  static final int NO_TYPE = -7;
  static final int PARAMETERIZED_TYPE = -8;
  static final int PRIMITIVE_TYPE = -9;
  static final int TYPE_VARIABLE = -10;
  static final int VOID = -11;

  //type aux

  static final int ARRAY_DIMENSION = -12;
  static final int ARRAY_INITIALIZER = -13;
  static final int ELLIPSIS = -14;

  //declarations

  static final int ANNOTATION = -15;
  static final int BODY = -16;
  static final int CLASS = -17;
  static final int COMPILATION_UNIT = -18;
  static final int CONSTRUCTOR = -19;
  static final int ENUM = -20;
  static final int ENUM_CONSTANT = -21;
  static final int EXTENDS = -22;
  static final int IDENTIFIER = -23;
  static final int IMPLEMENTS = -24;
  static final int INTERFACE = -25;
  static final int METHOD = -26;
  static final int MODIFIER = -27;
  static final int PACKAGE = -28;
  static final int TYPE_PARAMETER = -29;

  //statements

  static final int BLOCK = -30;
  static final int LOCAL_VARIABLE = -31;
  static final int SUPER_INVOCATION = -32;

  //expression start

  static final int INVOKE = -33;
  static final int PRIMITIVE_LITERAL = -34;
  static final int STRING_LITERAL = -35;

  //expressions

  static final int ASSIGNMENT = -36;
  static final int CLASS_INSTANCE_CREATION = -37;
  static final int EXPRESSION_NAME = -38;
  static final int EXPRESSION_NAME_CHAIN = -39;
  static final int FIELD_ACCESS = -40;
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

  public static boolean isVariableInitializer(int proto) {
    return isExpressionStart(proto) || proto == ARRAY_INITIALIZER;
  }

  public static boolean primaryDot(int proto) {
    return switch (proto) {
      case INVOKE -> true;

      default -> false;
    };
  }

}