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

  //internal instructions

  static final int EOF = -1;
  static final int POP = -2;
  static final int PROTOS = -3;

  //types

  static final int CLASS_TYPE = -4;

  //compilation unit

  static final int COMPILATION_UNIT = -5;
  static final int PACKAGE = -6;
  static final int AUTO_IMPORTS = -7;

  //type declarations

  static final int CLASS0 = -8;
  static final int CLASS_DECLARATION = -9;
  static final int MODIFIER = -10;
  static final int EXTENDS = -11;
  static final int BODY = -12;

  private ByteProto() {}

}