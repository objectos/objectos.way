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
package objectos.code.internal;

public final class ByteProto {

  //internal instructions

  public static final int AUTO_IMPORTS = -1;
  public static final int END_ELEMENT = -2;
  public static final int NEW_LINE = -3;
  public static final int NOOP = -4;
  public static final int STOP = -5;

  //types

  public static final int ARRAY_TYPE = -6;
  public static final int CLASS_TYPE = -7;
  public static final int NO_TYPE = -8;
  public static final int PARAMETERIZED_TYPE = -9;
  public static final int PRIMITIVE_TYPE = -10;
  public static final int TYPE_VARIABLE = -11;
  public static final int VOID = -12;

  //type aux

  public static final int ARRAY_DIMENSION = -13;
  public static final int ARRAY_INITIALIZER = -14;
  public static final int ELLIPSIS = -15;
  public static final int VALUE = -16;

  //declarations

  public static final int ANNOTATION = -17;
  public static final int ANNOTATION_VALUE = -18;
  public static final int BODY = -19;
  public static final int CLASS = -20;
  public static final int CLASS_DECLARATION = -21;
  public static final int COMPILATION_UNIT = -22;
  public static final int CONSTRUCTOR_DECLARATION = -23;
  public static final int DECLARATION_NAME = -24;
  public static final int ENUM = -25;
  public static final int ENUM_CONSTANT = -26;
  public static final int ENUM_DECLARATION = -27;
  public static final int EXTENDS = -28;
  public static final int EXTENDS_CLAUSE = -29;
  public static final int FIELD_DECLARATION = -30;
  public static final int IDENTIFIER = -31;
  public static final int IMPLEMENTS = -32;
  public static final int IMPLEMENTS_CLAUSE = -33;
  public static final int INTERFACE = -34;
  public static final int INTERFACE_DECLARATION = -35;
  public static final int METHOD_DECLARATION = -36;
  public static final int MODIFIER = -37;
  public static final int PACKAGE = -38;
  public static final int PACKAGE_DECLARATION = -39;
  public static final int PARAMETER = -40;
  public static final int PARAMETER_DECLARATION = -41;
  public static final int PERMITS_CLAUSE = -42;
  public static final int STATEMENT = -43;
  public static final int TYPE_PARAMETER = -44;

  //statement start

  public static final int BLOCK = -45;
  public static final int IF_CONDITION = -46;
  public static final int RETURN = -47;
  public static final int SUPER = -48;
  public static final int SUPER_INVOCATION = -49;
  public static final int THROW = -50;
  public static final int VAR = -51;

  //statement part

  public static final int CONDITION = -52;
  public static final int IF = -53;
  public static final int ELSE = -54;

  //expression start

  public static final int CLASS_INSTANCE_CREATION = -55;
  public static final int EXPRESSION_NAME = -56;
  public static final int INVOKE = -57;
  public static final int NEW = -58;
  public static final int NULL_LITERAL = -59;
  public static final int PRIMITIVE_LITERAL = -60;
  public static final int STRING_LITERAL = -61;
  public static final int THIS = -62;
  public static final int V = -63;

  //expression part

  public static final int ARGUMENT = -64;
  public static final int ARRAY_ACCESS = -65;
  public static final int ASSIGNMENT_OPERATOR = -66;
  public static final int EQUALITY_OPERATOR = -67;

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

  public static boolean isImport(int proto) {
    return proto == AUTO_IMPORTS;
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

  public static boolean isContinuation(int proto) {
    return switch (proto) {
      case V -> true;

      default -> false;
    };
  }

}