/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.http;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import objectos.internal.Ascii;
import objectos.internal.Util;

public final class Rfc {

  public static final byte[] HEADER_VALUE_TABLE;

  public static final byte HEADER_VALUE_VALID = 1;

  public static final byte HEADER_VALUE_WS = 2;

  public static final byte HEADER_VALUE_CR = 3;

  public static final byte HEADER_VALUE_LF = 4;

  static {
    final byte[] table;
    table = new byte[128];

    for (int b = 0x21; b < 0x7F; b++) {
      // VCHAR are valid
      table[b] = HEADER_VALUE_VALID;
    }

    // valid under certain circustances
    table[' '] = HEADER_VALUE_WS;

    table['\t'] = HEADER_VALUE_WS;

    table['\r'] = HEADER_VALUE_CR;

    table['\n'] = HEADER_VALUE_LF;

    HEADER_VALUE_TABLE = table;
  }

  private Rfc() {}

  @SuppressWarnings("unchecked")
  public static <K> void mapAdd(Map<K, Object> map, K key, String value) {
    Object oldValue;
    oldValue = map.put(key, value);

    if (oldValue == null) {
      return;
    }

    if (oldValue instanceof String s) {

      List<String> list;
      list = Util.createList();

      list.add(s);

      list.add(value);

      map.put(key, list);

    }

    else {
      List<String> list;
      list = (List<String>) oldValue;

      list.add(value);

      map.put(key, list);
    }
  }

  private static final byte DIGIT_0 = '0';

  private static final byte DIGIT_9 = '9';

  public static boolean isDigit(byte value) {
    return DIGIT_0 <= value && value <= DIGIT_9;
  }

  @SuppressWarnings("unchecked")
  public static void queryParamsAdd(Map<String, Object> map, String key, String value) {
    Object oldValue;
    oldValue = map.put(key, value);

    if (oldValue == null) {
      return;
    }

    if (oldValue instanceof String s) {

      List<String> list;
      list = Util.createList();

      list.add(s);

      list.add(value);

      map.put(key, list);

    }

    else {
      List<String> list;
      list = (List<String>) oldValue;

      list.add(value);

      map.put(key, list);
    }
  }

  public static String subDelims() {
    return "!$&'()*+,;=";
  }

  public static String unreserved() {
    return Ascii.alphaUpper() + Ascii.alphaLower() + Ascii.digit() + "-._~";
  }

  public static byte hexDigit(int nibble) {
    return (byte) (nibble < 10 ? '0' + nibble : 'A' + (nibble - 10));
  }

  public static IllegalArgumentException invalidFieldContent(int idx, char c) {
    return new IllegalArgumentException("Invalid character at index " + idx + ": " + c);
  }

  public static String pathDelim() {
    return "/-.";
  }

  public static <K> String queryParamsGet(Map<K, Object> params, K key) {
    Object maybe;
    maybe = params.get(key);

    return switch (maybe) {
      case null -> null;

      case String s -> s;

      case List<?> l -> (String) l.get(0);

      default -> throw new AssertionError(
          "Type should not have been put into the map: " + maybe.getClass()
      );
    };
  }

  @SuppressWarnings("unchecked")
  public static <K> List<String> queryParamsGetAll(Map<K, Object> params, K name) {
    Object maybe;
    maybe = params.get(name);

    return switch (maybe) {
      case null -> List.of();

      case String s -> List.of(s);

      case List<?> l -> (List<String>) l;

      default -> throw new AssertionError(
          "Type should not have been put into the map: " + maybe.getClass()
      );
    };
  }

  // URI RFC 3986

  public static String raw(String input) {
    final int len;
    len = input.length();

    if (len == 0) {
      return input;
    }

    int firstToEncode;
    firstToEncode = -1;

    for (int i = 0; i < len; i++) {
      final char c;
      c = input.charAt(i);

      if (c < 0x20) {
        // iso control
        firstToEncode = i;

        break;
      }

      if (c > 0x7F) {
        firstToEncode = i;

        break;
      }
    }

    if (firstToEncode == -1) {
      return input;
    }

    final int worstCaseChars;
    worstCaseChars = len - firstToEncode;

    final int initialBytesLength;
    initialBytesLength = (firstToEncode + 1) + (worstCaseChars * 3);

    byte[] bytes;
    bytes = new byte[initialBytesLength];

    int bytesIndex;
    bytesIndex = 0;

    for (int i = 0; i < firstToEncode; i++) {
      bytes[bytesIndex++] = (byte) input.charAt(i);
    }

    char highSurrogate;
    highSurrogate = 0;

    for (int i = firstToEncode; i < input.length(); i++) {
      final char c;
      c = input.charAt(i);

      if (c <= ' ') {
        highSurrogate = ensureZero(highSurrogate);

        bytes = ensureBytes(bytes, bytesIndex, 3);

        bytesIndex = raw(bytes, bytesIndex, c);
      }

      else if (c <= 0x7F) {
        highSurrogate = ensureZero(highSurrogate);

        bytes = ensureBytes(bytes, bytesIndex, 1);

        bytes[bytesIndex++] = (byte) c;
      }

      else if (c <= 0x7FF) {
        highSurrogate = ensureZero(highSurrogate);

        // 110xxxyy 10yyzzzz
        bytes = ensureBytes(bytes, bytesIndex, 6);

        final int byte0;
        byte0 = 0b1100_0000 | (c >> 6); // c <= 0x7FF, no higher bits set.

        final int byte1;
        byte1 = 0b1000_0000 | (c & 0b0011_1111);

        bytesIndex = raw(bytes, bytesIndex, byte0);

        bytesIndex = raw(bytes, bytesIndex, byte1);
      }

      else if (Character.isHighSurrogate(c)) {
        ensureZero(highSurrogate);

        highSurrogate = c;
      }

      else if (Character.isLowSurrogate(c)) {
        if (highSurrogate == 0) {
          throw new IllegalArgumentException("Low surrogate \\u" + Integer.toHexString(c) + " must be preceeded by a high surrogate.");
        }

        int codePoint;
        codePoint = Character.toCodePoint(highSurrogate, c);

        highSurrogate = 0;

        // 11110uvv 10vvwwww 10xxxxyy 10yyzzzz
        bytes = ensureBytes(bytes, bytesIndex, 12);

        final int byte0;
        byte0 = 0b1111_0000 | (codePoint >> 18);

        final int byte1;
        byte1 = 0b1000_0000 | ((codePoint >> 12) & 0b0011_1111);

        final int byte2;
        byte2 = 0b1000_0000 | ((codePoint >> 6) & 0b0011_1111);

        final int byte3;
        byte3 = 0b1000_0000 | (codePoint & 0b0011_1111);

        bytesIndex = raw(bytes, bytesIndex, byte0);

        bytesIndex = raw(bytes, bytesIndex, byte1);

        bytesIndex = raw(bytes, bytesIndex, byte2);

        bytesIndex = raw(bytes, bytesIndex, byte3);
      }

      else if (c <= 0xFFFF) {
        highSurrogate = ensureZero(highSurrogate);

        // 1110wwww 10xxxxyy 10yyzzzz
        bytes = ensureBytes(bytes, bytesIndex, 9);

        final int byte0;
        byte0 = 0b1110_0000 | (c >> 12);

        final int byte1;
        byte1 = 0b1000_0000 | ((c >> 6) & 0b0011_1111);

        final int byte2;
        byte2 = 0b1000_0000 | (c & 0b0011_1111);

        bytesIndex = raw(bytes, bytesIndex, byte0);

        bytesIndex = raw(bytes, bytesIndex, byte1);

        bytesIndex = raw(bytes, bytesIndex, byte2);
      }
    }

    if (highSurrogate != 0) {
      throw new IllegalArgumentException("Unmatched high surrogate at end of string");
    }

    return new String(bytes, 0, bytesIndex, StandardCharsets.US_ASCII);
  }

  private static char ensureZero(char highSurrogate) {
    if (highSurrogate != 0) {
      throw new IllegalArgumentException("High surrogate \\u" + Integer.toHexString(highSurrogate) + " must be followed by a low surrogate.");
    }

    return 0;
  }

  private static byte[] ensureBytes(byte[] bytes, int bytesIndex, int requiredLength) {
    final int requiredIndex;
    requiredIndex = bytesIndex + requiredLength - 1;

    return Util.growIfNecessary(bytes, requiredIndex);
  }

  private static int raw(byte[] bytes, int bytesIndex, int value) {
    // value is < 256
    bytes[bytesIndex++] = '%';
    bytes[bytesIndex++] = hexDigit(value >> 4);
    bytes[bytesIndex++] = hexDigit(value & 0b1111);

    return bytesIndex;
  }

  public static int requiredHexDigits(int value) {
    final int leadingZeros;
    leadingZeros = Integer.numberOfLeadingZeros(value);

    final int magnitude;
    magnitude = Integer.SIZE - leadingZeros;

    final int chars;
    chars = ((magnitude + 3) / 4);

    return Math.max(chars, 1);
  }

  private static final class Rfc8187 {

    static final byte[] TABLE = table();

    static final byte INVALID = 0;

    static final byte VALID = 1;

    private static byte[] table() {
      final byte[] table;
      table = new byte[128];

      final String attrChars;
      attrChars = "!#$&+-.^_`|~ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

      for (int idx = 0, len = attrChars.length(); idx < len; idx++) {
        final char c;
        c = attrChars.charAt(idx);

        table[c] = VALID;
      }

      return table;
    }

  }

  /// RFC-8187: encodes the UTF-8 value of a parameter of an HTTP header value.
  public static String rfc8187(String input) {
    final int len = input.length();

    if (input.isEmpty()) {
      return "UTF-8''";
    }

    int firstToEncode;
    firstToEncode = -1;

    for (int i = 0; i < len; i++) {
      final char c;
      c = input.charAt(i);

      if (c >= 128) {
        firstToEncode = i;

        break;
      }

      final byte flag;
      flag = Rfc8187.TABLE[c];

      if (flag == Rfc8187.INVALID) {
        firstToEncode = i;

        break;
      }
    }

    if (firstToEncode == -1) {
      return "UTF-8''" + input;
    }

    final int worstCaseBytes;
    worstCaseBytes = len - firstToEncode;

    final int initialBytesLength;
    initialBytesLength = (firstToEncode + 1) + (worstCaseBytes * 3);

    byte[] bytes;
    bytes = new byte[initialBytesLength];

    int bytesIndex;
    bytesIndex = 0;

    for (int i = 0; i < firstToEncode; i++) {
      bytes[bytesIndex++] = (byte) input.charAt(i);
    }

    char highSurrogate;
    highSurrogate = 0;

    for (int i = firstToEncode; i < input.length(); i++) {
      final char c;
      c = input.charAt(i);

      if (c <= 0x7F) {
        final byte flag;
        flag = Rfc8187.TABLE[c];

        if (flag == Rfc8187.INVALID) {
          highSurrogate = ensureZero(highSurrogate);

          bytes = ensureBytes(bytes, bytesIndex, 3);

          bytesIndex = raw(bytes, bytesIndex, c);
        } else {
          highSurrogate = ensureZero(highSurrogate);

          bytes = ensureBytes(bytes, bytesIndex, 1);

          bytes[bytesIndex++] = (byte) c;
        }
      }

      else if (c <= 0x7FF) {
        highSurrogate = ensureZero(highSurrogate);

        // 110xxxyy 10yyzzzz
        bytes = ensureBytes(bytes, bytesIndex, 6);

        final int byte0;
        byte0 = 0b1100_0000 | (c >> 6); // c <= 0x7FF, no higher bits set.

        final int byte1;
        byte1 = 0b1000_0000 | (c & 0b0011_1111);

        bytesIndex = raw(bytes, bytesIndex, byte0);

        bytesIndex = raw(bytes, bytesIndex, byte1);
      }

      else if (Character.isHighSurrogate(c)) {
        ensureZero(highSurrogate);

        highSurrogate = c;
      }

      else if (Character.isLowSurrogate(c)) {
        if (highSurrogate == 0) {
          throw new IllegalArgumentException("Low surrogate \\u" + Integer.toHexString(c) + " must be preceeded by a high surrogate.");
        }

        int codePoint;
        codePoint = Character.toCodePoint(highSurrogate, c);

        highSurrogate = 0;

        // 11110uvv 10vvwwww 10xxxxyy 10yyzzzz
        bytes = ensureBytes(bytes, bytesIndex, 12);

        final int byte0;
        byte0 = 0b1111_0000 | (codePoint >> 18);

        final int byte1;
        byte1 = 0b1000_0000 | ((codePoint >> 12) & 0b0011_1111);

        final int byte2;
        byte2 = 0b1000_0000 | ((codePoint >> 6) & 0b0011_1111);

        final int byte3;
        byte3 = 0b1000_0000 | (codePoint & 0b0011_1111);

        bytesIndex = raw(bytes, bytesIndex, byte0);

        bytesIndex = raw(bytes, bytesIndex, byte1);

        bytesIndex = raw(bytes, bytesIndex, byte2);

        bytesIndex = raw(bytes, bytesIndex, byte3);
      }

      else if (c <= 0xFFFF) {
        highSurrogate = ensureZero(highSurrogate);

        // 1110wwww 10xxxxyy 10yyzzzz
        bytes = ensureBytes(bytes, bytesIndex, 9);

        final int byte0;
        byte0 = 0b1110_0000 | (c >> 12);

        final int byte1;
        byte1 = 0b1000_0000 | ((c >> 6) & 0b0011_1111);

        final int byte2;
        byte2 = 0b1000_0000 | (c & 0b0011_1111);

        bytesIndex = raw(bytes, bytesIndex, byte0);

        bytesIndex = raw(bytes, bytesIndex, byte1);

        bytesIndex = raw(bytes, bytesIndex, byte2);
      }
    }

    if (highSurrogate != 0) {
      throw new IllegalArgumentException("Unmatched high surrogate at end of string");
    }

    final String value;
    value = new String(bytes, 0, bytesIndex, StandardCharsets.US_ASCII);

    return "UTF-8''" + value;
  }

  public static String tchar() {
    return "!#$%&'*+-.^`|~ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  }

  public static byte[] utf8(String value) {
    return value.getBytes(StandardCharsets.UTF_8);
  }

  public static String vchar() {
    return Ascii.visible();
  }

}
