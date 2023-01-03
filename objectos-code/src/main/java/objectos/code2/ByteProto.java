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
package objectos.code2;

final class ByteProto {

  static final int NULL = Integer.MIN_VALUE;

  //types

  static final int ARRAY_TYPE = -1;
  static final int CLASS_TYPE = -2;
  static final int PARAMETERIZED_TYPE = -3;
  static final int PRIMITIVE_TYPE = -4;
  static final int VOID = -5;

  //type aux

  static final int ARRAY_DIMENSION = -6;
  static final int ARRAY_INITIALIZER = -7;

  //compilation unit

  static final int AUTO_IMPORTS = -8;
  static final int EOF = -9;
  static final int PACKAGE = -10;

  //declarations

  static final int ANNOTATION = -11;
  static final int BODY = -12;
  static final int CLASS = -13;
  static final int EXTENDS = -14;
  static final int IDENTIFIER = -15;
  static final int IMPLEMENTS = -16;
  static final int METHOD_DECLARATION = -17;
  static final int MODIFIER = -18;
  static final int NEW_LINE = -19;
  static final int PERMITS = -20;

  //stmt builder

  static final int RETURN = -21;
  static final int STATEMENT = -22;

  //statements

  static final int BLOCK = -23;
  static final int RETURN_STATEMENT = -24;

  //expressions

  static final int CLASS_INSTANCE_CREATION = -25;
  static final int EXPRESSION_NAME = -26;
  static final int METHOD_INVOCATION = -27;
  static final int METHOD_INVOCATION_QUALIFIED = -28;
  static final int PRIMITIVE_LITERAL = -29;
  static final int STRING_LITERAL = -30;
  static final int THIS = -31;

  private ByteProto() {}

}