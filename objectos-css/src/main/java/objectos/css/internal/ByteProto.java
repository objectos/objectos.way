/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.css.internal;

final class ByteProto {

  public static final byte NULL = -1;

  public static final byte ROOT = -2;

  public static final byte ROOT_END = -3;

  public static final byte DECLARATION = -4;

  public static final byte DECLARATION_END = -5;

  public static final byte LENGTH_DOUBLE = -6;

  public static final byte LENGTH_INT = -7;

  public static final byte STANDARD_NAME = -8;

  public static final byte ZERO = -9;

  public static final byte STYLE_RULE = -10;

  public static final byte STYLE_RULE_END = -11;

  public static final byte MARKED = -12;

  public static final byte MARKED6 = -13;

  public static final byte MARKED10 = -14;

  private ByteProto() {}

  public static byte markedOf(int length) {
    return switch (length) {
      case 6 -> MARKED6;

      case 10 -> MARKED10;

      default -> throw new UnsupportedOperationException(
        "Implement me :: length=" + length
      );
    };
  }

}