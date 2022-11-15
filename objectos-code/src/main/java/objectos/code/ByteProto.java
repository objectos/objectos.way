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

  // internal instructions

  static final int JMP = -1;
  static final int BREAK = -2;
  static final int NEW_LINE = -3;

  // types

  static final int PACKAGE_NAME = -4;
  static final int ARRAY_TYPE = -5;
  static final int DIM = -6;
  static final int CLASS_NAME = -7;
  static final int TYPE_NAME = -8;

  // declarations

  static final int COMPILATION_UNIT = -9;
  static final int PACKAGE_DECLARATION = -10;

  // class

  static final int CLASS_DECLARATION = -11;
  static final int MODIFIER = -12;
  static final int IDENTIFIER = -13;
  static final int EXTENDS = -14;
  static final int IMPLEMENTS = -15;

  // field

  static final int FIELD_DECLARATION = -16;

  // method

  static final int METHOD_DECLARATION = -17;
  static final int FORMAL_PARAMETER = -18;

  // enum

  static final int ENUM_DECLARATION = -19;
  static final int ENUM_CONSTANT = -20;

  // annotation

  static final int ANNOTATION = -21;

  // statements

  static final int LOCAL_VARIABLE = -22;
  static final int RETURN_STATEMENT = -23;

  // expressions

  static final int ARRAY_ACCESS_EXPRESSION = -24;
  static final int EXPRESSION_NAME = -25;
  static final int METHOD_INVOCATION = -26;
  static final int STRING_LITERAL = -27;

  private ByteProto() {}

  public static boolean isExpression(int proto) {
    return switch (proto) {
      case ARRAY_ACCESS_EXPRESSION,
          EXPRESSION_NAME,
          METHOD_INVOCATION,
          STRING_LITERAL -> true;

      default -> false;
    };
  }

  public static boolean isExpressionStatement(int proto) {
    return switch (proto) {
      case METHOD_INVOCATION -> true;

      default -> false;
    };
  }

}