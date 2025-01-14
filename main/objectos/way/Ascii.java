/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
package objectos.way;

final class Ascii {

  private Ascii() {}

  public static boolean isDigit(char c) {
    return '0' <= c && c <= '9';
  }

  public static boolean isHexDigit(char c) {
    return isDigit(c)
        || 'A' <= c && c <= 'F'
        || 'a' <= c && c <= 'f';
  }

  public static boolean isLetter(char c) {
    return 'A' <= c && c <= 'Z'
        || 'a' <= c && c <= 'z';
  }

  public static boolean isLetterOrDigit(char c) {
    return isLetter(c) || isDigit(c);
  }

  public static boolean isWhitespace(char c) {
    return c == '\t' || c == '\r' || c == '\n' || c == ' ';
  }

  public static int digitToInt(char c) {
    return c - '0';
  }

}