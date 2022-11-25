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

  static final int ARRAY_TYPE = -4;
  static final int DIM = -5;
  static final int PACKAGE_NAME = -6;
  static final int CLASS_NAME = -7;
  static final int NO_TYPE = -8;
  static final int PARAMETERIZED_TYPE = -9;
  static final int PRIMITIVE_TYPE = -10;
  static final int ARRAY_INITIALIZER = -11;

  //declarations

  static final int COMPILATION_UNIT = -12;
  static final int PACKAGE_DECLARATION = -13;

  //class

  static final int CLASS_DECLARATION = -14;
  static final int MODIFIER = -15;
  static final int IDENTIFIER = -16;
  static final int EXTENDS_SINGLE = -17;
  static final int IMPLEMENTS = -18;

  //field

  static final int FIELD_DECLARATION = -19;

  //method/constructor

  static final int METHOD_DECLARATION = -20;
  static final int FORMAL_PARAMETER = -21;
  static final int ELLIPSIS = -22;
  static final int CONSTRUCTOR_DECLARATION = -23;
  static final int THIS_INVOCATION = -24;
  static final int SUPER_INVOCATION = -25;
  static final int QUALIFIED_SUPER_INVOCATION = -26;

  //enum

  static final int ENUM_DECLARATION = -27;
  static final int ENUM_CONSTANT = -28;

  //interface

  static final int INTERFACE_DECLARATION = -29;
  static final int EXTENDS_MANY = -30;

  //annotation

  static final int ANNOTATION = -31;

  //statements

  static final int LOCAL_VARIABLE = -32;
  static final int RETURN_STATEMENT = -33;

  //expressions

  static final int ARRAY_ACCESS_EXPRESSION = -34;
  static final int ASSIGNMENT_EXPRESSION = -35;
  static final int ASSIGNMENT_OPERATOR = -36;
  static final int CHAINED_METHOD_INVOCATION = -37;
  static final int CLASS_INSTANCE_CREATION0 = -38;
  static final int EXPRESSION_NAME = -39;
  static final int FIELD_ACCESS_EXPRESSION0 = -40;
  static final int METHOD_INVOCATION = -41;
  static final int METHOD_INVOCATION_QUALIFIED = -42;
  static final int PRIMITIVE_LITERAL = -43;
  static final int STRING_LITERAL = -44;
  static final int THIS = -45;

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