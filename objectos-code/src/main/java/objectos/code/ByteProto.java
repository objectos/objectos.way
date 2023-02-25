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

  //internal instructions

  static final int AUTO_IMPORTS = -1;
  static final int END_ELEMENT = -2;
  static final int NEW_LINE = -3;
  static final int NOOP = -4;
  static final int STOP = -5;

  //types

  static final int ARRAY_TYPE = -6;
  static final int CLASS_TYPE = -7;
  static final int NO_TYPE = -8;
  static final int PARAMETERIZED_TYPE = -9;
  static final int PRIMITIVE_TYPE = -10;
  static final int TYPE_VARIABLE = -11;
  static final int VOID = -12;

  //type aux

  static final int ARRAY_DIMENSION = -13;
  static final int ARRAY_INITIALIZER = -14;
  static final int ELLIPSIS = -15;
  static final int VALUE = -16;

  //declarations

  static final int ANNOTATION = -17;
  static final int BODY = -18;
  static final int CLASS = -19;
  static final int COMPILATION_UNIT = -20;
  static final int CONSTRUCTOR = -21;
  static final int CONSTRUCTOR_DECLARATION = -22;
  static final int DECLARATION_NAME = -23;
  static final int ENUM = -24;
  static final int ENUM_CONSTANT = -25;
  static final int EXTENDS = -26;
  static final int FIELD_DECLARATION = -27;
  static final int IDENTIFIER = -28;
  static final int IMPLEMENTS = -29;
  static final int INTERFACE = -30;
  static final int METHOD = -31;
  static final int METHOD_DECLARATION = -32;
  static final int MODIFIER = -33;
  static final int MODIFIERS = -34;
  static final int PACKAGE = -35;
  static final int PARAMETER = -36;
  static final int PARAMETER_SHORT = -37;
  static final int RETURN_TYPE = -38;
  static final int STATEMENT = -39;
  static final int TYPE_PARAMETER = -40;

  //statement start

  static final int BLOCK = -41;
  static final int IF_CONDITION = -42;
  static final int RETURN = -43;
  static final int SUPER = -44;
  static final int SUPER_INVOCATION = -45;
  static final int THROW = -46;
  static final int VAR = -47;

  //statement part

  static final int IF = -48;
  static final int ELSE = -49;

  //expression start

  static final int CLASS_INSTANCE_CREATION = -50;
  static final int EXPRESSION_NAME = -51;
  static final int INVOKE = -52;
  static final int NEW = -53;
  static final int NULL_LITERAL = -54;
  static final int PRIMITIVE_LITERAL = -55;
  static final int STRING_LITERAL = -56;
  static final int THIS = -57;
  static final int V = -58;

  //expression part

  static final int ARGUMENT = -59;
  static final int ARRAY_ACCESS = -60;
  static final int ASSIGNMENT_OPERATOR = -61;
  static final int EQUALITY_OPERATOR = -62;

  private ByteProto() {}

  public static boolean isBlockOrStatement(int proto) {
    return proto == BLOCK
        || proto == STATEMENT;
  }

  public static boolean isClassOrParameterizedType(int proto) {
    return proto == CLASS_TYPE
        || proto == PARAMETERIZED_TYPE;
  }

  public static boolean isExpressionStart(int proto) {
    return proto <= CLASS_INSTANCE_CREATION
        && proto >= THIS;
  }

  public static boolean isFormalParameter(int proto) {
    return proto == PARAMETER
        || proto == PARAMETER_SHORT;
  }

  public static boolean isImport(int proto) {
    return proto == AUTO_IMPORTS;
  }

  public static boolean isModifier(int proto) {
    return proto == MODIFIER
        || proto == MODIFIERS;
  }

  public static boolean isOperator(int proto) {
    return proto <= ASSIGNMENT_OPERATOR
        && proto >= EQUALITY_OPERATOR;
  }

  public static boolean isStatementStart(int proto) {
    return proto <= BLOCK
        && proto >= VAR;
  }

  public static boolean isType(int proto) {
    return proto <= ARRAY_TYPE
        && proto >= TYPE_VARIABLE;
  }

  public static boolean isWhitespace(int proto) {
    return proto == NEW_LINE;
  }

}