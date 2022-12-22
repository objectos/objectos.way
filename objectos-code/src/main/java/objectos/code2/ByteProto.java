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

  static final int EOF = -1;
  static final int POP = -2;
  static final int PROTOS = -3;

  static final int COMPILATION_UNIT = -4;
  static final int MODIFIER = -5;
  static final int CLASS0 = -6;
  static final int CLASS_DECLARATION = -7;
  static final int BODY = -8;

  private ByteProto() {}

}