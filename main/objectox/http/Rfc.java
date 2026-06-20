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

  public static int requiredHexDigits(int value) {
    final int leadingZeros;
    leadingZeros = Integer.numberOfLeadingZeros(value);

    final int magnitude;
    magnitude = Integer.SIZE - leadingZeros;

    final int chars;
    chars = ((magnitude + 3) / 4);

    return Math.max(chars, 1);
  }

  public static String tchar() {
    return "!#$%&'*+-.^`|~ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  }

  public static String urlEncode(String input) {
    final UrlEncoder encoder;
    encoder = new UrlEncoder(input);

    return encoder.encode();
  }

  public static byte[] utf8(String value) {
    return value.getBytes(StandardCharsets.UTF_8);
  }

  public static String vchar() {
    return Ascii.visible();
  }

}
