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

  //   internal instructions

  static final int JMP = -1;
  static final int BREAK = -2;
  static final int NEW_LINE = -3;

  // types

  static final int ARRAY_TYPE = -4;
  static final int DIM = -5;
  static final int PACKAGE_NAME = -6;
  static final int CLASS_NAME = -7;
  static final int NO_TYPE = -8;
  static final int PARAMETERIZED_TYPE = -9;
  static final int PRIMITIVE_TYPE = -10;
  static final int TYPE_NAME = -11;

  // declarations

  static final int COMPILATION_UNIT = -12;
  static final int PACKAGE_DECLARATION = -13;

  // class

  static final int CLASS_DECLARATION = -14;
  static final int MODIFIER = -15;
  static final int IDENTIFIER = -16;
  static final int EXTENDS = -17;
  static final int IMPLEMENTS = -18;

  // field

  static final int FIELD_DECLARATION = -19;

  // method/constructor

  static final int METHOD_DECLARATION = -20;
  static final int CONSTRUCTOR_DECLARATION = -21;
  static final int FORMAL_PARAMETER = -22;

  // enum

  static final int ENUM_DECLARATION = -23;
  static final int ENUM_CONSTANT = -24;

  // annotation

  static final int ANNOTATION = -25;

  // statements

  static final int LOCAL_VARIABLE = -26;
  static final int RETURN_STATEMENT = -27;

  // expressions

  static final int ARRAY_ACCESS_EXPRESSION = -28;
  static final int ASSIGNMENT_EXPRESSION = -29;
  static final int ASSIGNMENT_OPERATOR = -30;
  static final int CLASS_INSTANCE_CREATION0 = -31;
  static final int EXPRESSION_NAME = -32;
  static final int FIELD_ACCESS_EXPRESSION0 = -33;
  static final int METHOD_INVOCATION = -34;
  static final int STRING_LITERAL = -35;
  static final int THIS = -36;

  private ByteProto() {}

  public static boolean isExpression(int proto) {
    return proto <= ARRAY_ACCESS_EXPRESSION
        && proto >= THIS;
  }

  public static boolean isExpressionStatement(int proto) {
    return switch (proto) {
      case ASSIGNMENT_EXPRESSION,
          CLASS_INSTANCE_CREATION0,
          METHOD_INVOCATION -> true;

      default -> false;
    };
  }

}