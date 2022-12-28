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

  static final int CLASS_TYPE = -1;
  static final int PRIMITIVE_TYPE = -2;
  static final int VOID = -3;

  //compilation unit

  static final int COMPILATION_UNIT = -4;
  static final int PACKAGE = -5;
  static final int AUTO_IMPORTS = -6;

  //declarations

  static final int ANNOTATION = -7;
  static final int BODY = -8;
  static final int CLASS0 = -9;
  static final int CLASS_BODY = -10;
  static final int CLASS_DECLARATION = -11;
  static final int EXTENDS = -12;
  static final int IDENTIFIER = -13;
  static final int IMPLEMENTS = -14;
  static final int METHOD_DECLARATION = -15;
  static final int MODIFIER = -16;

  //statements

  static final int BLOCK = -17;
  static final int RETURN_STATEMENT = -18;

  //expressions

  static final int STRING_LITERAL = -19;
  static final int THIS = -20;

  private ByteProto() {}

}