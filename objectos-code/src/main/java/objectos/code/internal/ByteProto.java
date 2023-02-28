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
  public static final int CONSTRUCTOR = -23;
  public static final int CONSTRUCTOR_DECLARATION = -24;
  public static final int DECLARATION_NAME = -25;
  public static final int ENUM = -26;
  public static final int ENUM_CONSTANT = -27;
  public static final int ENUM_DECLARATION = -28;
  public static final int EXTENDS = -29;
  public static final int EXTENDS_CLAUSE = -30;
  public static final int FIELD_DECLARATION = -31;
  public static final int IDENTIFIER = -32;
  public static final int IMPLEMENTS = -33;
  public static final int IMPLEMENTS_CLAUSE = -34;
  public static final int INTERFACE = -35;
  public static final int INTERFACE_DECLARATION = -36;
  public static final int METHOD = -37;
  public static final int METHOD_DECLARATION = -38;
  public static final int MODIFIER = -39;
  public static final int MODIFIERS = -40;
  public static final int PACKAGE = -41;
  public static final int PACKAGE_DECLARATION = -42;
  public static final int PARAMETER = -43;
  public static final int PARAMETER_SHORT = -44;
  public static final int RETURN_TYPE = -45;
  public static final int STATEMENT = -46;
  public static final int TYPE_PARAMETER = -47;

  //statement start

  public static final int BLOCK = -48;
  public static final int IF_CONDITION = -49;
  public static final int RETURN = -50;
  public static final int SUPER = -51;
  public static final int SUPER_INVOCATION = -52;
  public static final int THROW = -53;
  public static final int VAR = -54;

  //statement part

  public static final int IF = -55;
  public static final int ELSE = -56;

  //expression start

  public static final int CLASS_INSTANCE_CREATION = -57;
  public static final int EXPRESSION_NAME = -58;
  public static final int INVOKE = -59;
  public static final int NEW = -60;
  public static final int NULL_LITERAL = -61;
  public static final int PRIMITIVE_LITERAL = -62;
  public static final int STRING_LITERAL = -63;
  public static final int THIS = -64;
  public static final int V = -65;

  //expression part

  public static final int ARGUMENT = -66;
  public static final int ARRAY_ACCESS = -67;
  public static final int ASSIGNMENT_OPERATOR = -68;
  public static final int EQUALITY_OPERATOR = -69;

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