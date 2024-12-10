/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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

final class LangChars {

  private LangChars() {}

  public static boolean isAsciiDigit(char c) {
    return '0' <= c && c <= '9';
  }

  public static boolean isAsciiLetter(char c) {
    return 'A' <= c && c <= 'Z'
        || 'a' <= c && c <= 'z';
  }

  public static boolean isAsciiLetterOrDigit(char c) {
    return isAsciiLetter(c) || isAsciiDigit(c);
  }

  public static boolean isAsciiWhitespace(char c) {
    return c == '\t' || c == '\r' || c == '\n' || c == ' ';
  }

}