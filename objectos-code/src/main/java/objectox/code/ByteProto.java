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
package objectox.code;

final class ByteProto {

  static final int NULL = Integer.MIN_VALUE;

  static final int JMP = -1;
  static final int BREAK = -2;

  static final int COMPILATION_UNIT = -3;
  static final int PACKAGE_DECLARATION = -4;
  static final int ANNOTATION = -6;
  static final int MODIFIER = -7;
  static final int CLASS_DECLARATION = -8;
  static final int EXTENDS = -9;
  static final int METHOD_DECLARATION = -10;

  static final int IDENTIFIER = -11;
  static final int CLASS_NAME = -12;
  static final int STRING_LITERAL = -13;

  static final int LOCAL_VARIABLE = -14;
  static final int METHOD_INVOCATION = -15;
  static final int NEW_LINE = -16;
  static final int TYPE_NAME = -17;
  static final int EXPRESSION_NAME = -18;
  static final int PACKAGE_NAME = -19;

  private ByteProto() {}

}