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
  static final int NEW_LINE = -3;

  //types

  static final int ARRAY_INITIALIZER = -4;
  static final int ARRAY_TYPE = -5;
  static final int CLASS_TYPE = -6;
  static final int DIM = -7;
  static final int NO_TYPE = -8;
  static final int PACKAGE_NAME = -9;
  static final int PARAMETERIZED_TYPE = -10;
  static final int PRIMITIVE_TYPE = -11;
  static final int SIMPLE_NAME = -12;
  static final int TYPE_VARIABLE = -13;

  //declarations

  static final int COMPILATION_UNIT = -14;
  static final int PACKAGE_DECLARATION = -15;
  static final int TYPE_PARAMETER = -16;

  //class

  static final int CLASS_DECLARATION = -17;
  static final int MODIFIER = -18;
  static final int IDENTIFIER = -19;
  static final int EXTENDS_SINGLE = -20;
  static final int IMPLEMENTS = -21;

  //field

  static final int FIELD_DECLARATION = -22;

  //method/constructor

  static final int METHOD_DECLARATION = -23;
  static final int FORMAL_PARAMETER = -24;
  static final int ELLIPSIS = -25;
  static final int CONSTRUCTOR_DECLARATION = -26;
  static final int THIS_INVOCATION = -27;
  static final int SUPER_INVOCATION = -28;
  static final int QUALIFIED_SUPER_INVOCATION = -29;

  //enum

  static final int ENUM_DECLARATION = -30;
  static final int ENUM_CONSTANT = -31;

  //interface

  static final int INTERFACE_DECLARATION = -32;
  static final int EXTENDS_MANY = -33;

  //annotation

  static final int ANNOTATION = -34;

  //statements

  static final int LOCAL_VARIABLE = -35;
  static final int RETURN_STATEMENT = -36;

  //expressions

  static final int ARRAY_ACCESS_EXPRESSION = -37;
  static final int ASSIGNMENT_EXPRESSION = -38;
  static final int ASSIGNMENT_OPERATOR = -39;
  static final int CHAINED_METHOD_INVOCATION = -40;
  static final int CLASS_INSTANCE_CREATION0 = -41;
  static final int EXPRESSION_NAME = -42;
  static final int FIELD_ACCESS_EXPRESSION0 = -43;
  static final int METHOD_INVOCATION = -44;
  static final int METHOD_INVOCATION_QUALIFIED = -45;
  static final int PRIMITIVE_LITERAL = -46;
  static final int STRING_LITERAL = -47;
  static final int THIS = -48;

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