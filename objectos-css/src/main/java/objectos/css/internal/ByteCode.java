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

final class ByteCode {

  public static final byte SELECTOR = -1;

  public static final byte BLOCK_START = -2;

  public static final byte BLOCK_END = -3;

  public static final byte TAB = -4;

  public static final byte PROPERTY_NAME = -5;

  public static final byte KEYWORD = -6;

  public static final byte SEMICOLON = -7;

  public static final byte SEMICOLON_OPTIONAL = -8;

  private ByteCode() {}

}