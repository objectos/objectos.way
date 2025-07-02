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

import java.nio.charset.StandardCharsets;

final class Ascii {

  public static final char CR = '\r';
  public static final char LF = '\n';
  public static final char SP = ' ';
  public static final char TAB = '\t';

  private Ascii() {}

  public static void fill(byte[] array, String ascii, byte value) {
    final byte[] bytes;
    bytes = ascii.getBytes(StandardCharsets.US_ASCII);

    for (byte b : bytes) {
      array[b] = value;
    }
  }

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

  public static boolean isLineTerminator(char c) {
    return c == CR || c == LF;
  }

  public static boolean isLowerCase(char c) {
    return 'a' <= c && c <= 'z';
  }

  public static boolean isWhitespace(char c) {
    return c == '\t' || c == '\r' || c == '\n' || c == ' ';
  }

  public static int digitToInt(char c) {
    return c - '0';
  }

  public static String alphaUpper() {
    return "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  }

  public static String alphaLower() {
    return "abcdefghijklmnopqrstuvwxyz";
  }

  public static String digit() {
    return "0123456789";
  }

  public static String visible() {
    return "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
  }

}