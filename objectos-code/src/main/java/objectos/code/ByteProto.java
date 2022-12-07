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

  //types

  static final int ARRAY_INITIALIZER = -6;
  static final int ARRAY_TYPE = -7;
  static final int CLASS_TYPE = -8;
  static final int DIM = -9;
  static final int NO_TYPE = -10;
  static final int PACKAGE_NAME = -11;
  static final int PARAMETERIZED_TYPE = -12;
  static final int PRIMITIVE_TYPE = -13;
  static final int SIMPLE_NAME = -14;
  static final int TYPE_VARIABLE = -15;

  //declarations

  static final int COMPILATION_UNIT = -16;
  static final int PACKAGE_DECLARATION = -17;
  static final int TYPE_PARAMETER = -18;

  //class

  static final int CLASS_DECLARATION = -19;
  static final int MODIFIER = -20;
  static final int IDENTIFIER = -21;
  static final int EXTENDS_SINGLE = -22;
  static final int IMPLEMENTS = -23;

  //field

  static final int FIELD_DECLARATION = -24;

  //method/constructor

  static final int METHOD_DECLARATION = -25;
  static final int FORMAL_PARAMETER = -26;
  static final int ELLIPSIS = -27;
  static final int CONSTRUCTOR_DECLARATION = -28;
  static final int THIS_INVOCATION = -29;
  static final int SUPER_INVOCATION = -30;
  static final int QUALIFIED_SUPER_INVOCATION = -31;

  //enum

  static final int ENUM_DECLARATION = -32;
  static final int ENUM_CONSTANT = -33;

  //interface

  static final int INTERFACE_DECLARATION = -34;
  static final int EXTENDS_MANY = -35;

  //annotation

  static final int ANNOTATION = -36;

  //statements

  static final int BLOCK = -37;
  static final int LOCAL_VARIABLE = -38;
  static final int RETURN_STATEMENT = -39;

  //expressions

  static final int ARRAY_ACCESS_EXPRESSION = -40;
  static final int ASSIGNMENT_EXPRESSION = -41;
  static final int ASSIGNMENT_OPERATOR = -42;
  static final int CHAINED_METHOD_INVOCATION = -43;
  static final int CLASS_INSTANCE_CREATION0 = -44;
  static final int EXPRESSION_NAME = -45;
  static final int FIELD_ACCESS_EXPRESSION0 = -46;
  static final int METHOD_INVOCATION = -47;
  static final int METHOD_INVOCATION_QUALIFIED = -48;
  static final int PRIMITIVE_LITERAL = -49;
  static final int STRING_LITERAL = -50;
  static final int THIS = -51;

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

}