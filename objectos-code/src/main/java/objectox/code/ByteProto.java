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

public final class ByteProto {

  public static final int NULL = Integer.MIN_VALUE;

  public static final int JMP = -1;
  public static final int BREAK = -2;

  public static final int COMPILATION_UNIT = -3;
  public static final int PACKAGE_DECLARATION = -4;
  public static final int ANNOTATION = -6;
  public static final int MODIFIER = -7;
  public static final int CLASS_DECLARATION = -8;
  public static final int EXTENDS = -9;
  public static final int METHOD_DECLARATION = -10;

  public static final int IDENTIFIER = -11;
  public static final int CLASS_NAME = -12;
  public static final int STRING_LITERAL = -13;

  public static final int LOCAL_VARIABLE = -14;
  public static final int METHOD_INVOCATION = -15;
  public static final int NEW_LINE = -16;
  public static final int TYPE_NAME = -17;
  public static final int EXPRESSION_NAME = -18;
  public static final int PACKAGE_NAME = -19;

  private ByteProto() {}

}