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
package objectos.way;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

record MediaType(String type, String subtype, Map<String, String> params) implements Media.Type {

  public static final MediaType WILDCARD = new MediaType("*", "*", Map.of());

  static Media.Type parse(String s) {
    String type = null;
    String subtype = null;
    Map<String, String> params = new HashMap<>();

    enum Parser {
      START,

      TYPE,

      SLASH,

      SUBTYPE,

      SUBTYPE_TRIM,

      SEMICOLON,

      PARAM_NAME,

      PARAM_NAME_TRIM,

      PARAM_EQ,

      PARAM_VALUE,

      PARAM_DQUOTE,

      PARAM_DQUOTE_VALUE;
    }

    int startIndex = 0;
    Parser parser = Parser.START;
    String paramName = null;

    for (int idx = 0, len = s.length(); idx < len; idx++) {
      final char c;
      c = s.charAt(idx);

      switch (parser) {
        case START -> {
          if (Ascii.isLetter(c)) {
            parser = Parser.TYPE;

            startIndex = idx;
          }

          else if (c == ' ') {
            parser = Parser.START;
          }

          else {
            throw new IllegalArgumentException("Invalid character in type: " + c);
          }
        }

        case TYPE -> {
          if (Ascii.isLetter(c)) {
            parser = Parser.TYPE;
          }

          else if (c == '/') {
            parser = Parser.SLASH;

            type = s.substring(startIndex, idx);

            type = type.toLowerCase(Locale.US);
          }

          else {
            throw new IllegalArgumentException("Invalid character in type: " + c);
          }
        }

        case SLASH -> {
          if (Ascii.isLetter(c) || Ascii.isDigit(c)) {
            parser = Parser.SUBTYPE;

            startIndex = idx;
          }

          else {
            throw new IllegalArgumentException("Invalid media type, invalid character after slash: " + c);
          }
        }

        case SUBTYPE -> {
          if (Ascii.isLetter(c) || Ascii.isDigit(c) || c == '+' || c == '.') {
            parser = Parser.SUBTYPE;
          }

          else if (c == ' ') {
            parser = Parser.SUBTYPE_TRIM;

            subtype = s.substring(startIndex, idx);

            subtype = subtype.toLowerCase(Locale.US);
          }

          else if (c == ';') {
            parser = Parser.SEMICOLON;

            subtype = s.substring(startIndex, idx);

            subtype = subtype.toLowerCase(Locale.US);
          }

          else {
            throw new IllegalArgumentException("Invalid character in subtype: " + c);
          }
        }

        case SUBTYPE_TRIM -> {
          if (c == ' ') {
            parser = Parser.SUBTYPE_TRIM;
          }

          else if (c == ';') {
            parser = Parser.SEMICOLON;
          }

          else {
            throw new IllegalArgumentException("Invalid character after subtype: " + c);
          }
        }

        case SEMICOLON -> {
          if (Ascii.isLetter(c)) {
            parser = Parser.PARAM_NAME;

            startIndex = idx;
          }

          else if (c == ';') {
            parser = Parser.SEMICOLON;
          }

          else if (c == ' ') {
            parser = Parser.SEMICOLON;
          }

          else {
            throw new IllegalArgumentException("Invalid character after subtype: " + c);
          }
        }

        case PARAM_NAME -> {
          if (Ascii.isLetter(c)) {
            parser = Parser.PARAM_NAME;
          }

          else if (c == '=') {
            parser = Parser.PARAM_EQ;

            paramName = s.substring(startIndex, idx);

            paramName = paramName.toLowerCase(Locale.US);
          }

          else if (c == ' ') {
            parser = Parser.PARAM_NAME_TRIM;

            paramName = s.substring(startIndex, idx);

            paramName = paramName.toLowerCase(Locale.US);
          }

          else {
            throw new IllegalArgumentException("Invalid character in parameter name: " + c);
          }
        }

        case PARAM_NAME_TRIM -> {
          if (c == ' ') {
            parser = Parser.PARAM_NAME_TRIM;
          }

          else if (c == '=') {
            parser = Parser.PARAM_EQ;
          }

          else {
            throw new IllegalArgumentException("Invalid character in parameter name: " + c);
          }
        }

        case PARAM_EQ -> {
          if (c == ' ') {
            parser = Parser.PARAM_EQ;
          }

          else if (c == '"') {
            parser = Parser.PARAM_DQUOTE;
          }

          else {
            parser = Parser.PARAM_VALUE;

            startIndex = idx;
          }
        }

        case PARAM_DQUOTE -> {
          if (c == '"') {
            parser = Parser.SUBTYPE_TRIM;

            final String value;
            value = s.substring(startIndex, idx);

            params.put(paramName, value);
          } else {
            parser = Parser.PARAM_DQUOTE_VALUE;

            startIndex = idx;
          }
        }

        case PARAM_DQUOTE_VALUE -> {
          if (c == '"') {
            parser = Parser.SUBTYPE_TRIM;

            final String value;
            value = s.substring(startIndex, idx);

            params.put(paramName, value);
          } else {
            parser = Parser.PARAM_DQUOTE_VALUE;
          }
        }

        case PARAM_VALUE -> {
          if (c == ' ') {
            parser = Parser.SUBTYPE_TRIM;

            final String value;
            value = s.substring(startIndex, idx);

            params.put(paramName, value);
          }

          else if (c == ';') {
            parser = Parser.SEMICOLON;

            final String value;
            value = s.substring(startIndex, idx);

            params.put(paramName, value);
          }

          else {
            parser = Parser.PARAM_VALUE;
          }
        }
      }
    }

    switch (parser) {
      case SUBTYPE -> subtype = s.substring(startIndex).toLowerCase(Locale.US);

      case PARAM_EQ -> params.put(paramName, "");

      case PARAM_VALUE -> params.put(paramName, s.substring(startIndex));

      case TYPE, SLASH -> throw new IllegalArgumentException("Incomplete media type");

      default -> {}
    }

    if (type == null || subtype == null) {
      throw new IllegalArgumentException("Invalid media type format");
    }

    params = Map.copyOf(params);

    return new MediaType(type, subtype, params);
  }

  @Override
  public final String param(String name) {
    Objects.requireNonNull(name, "name == null");

    return params.get(name);
  }

  @Override
  public final String toString(byte[] bytes) {
    if ("text".equals(type)) {
      final String charsetName;
      charsetName = params.get("charset");

      if (charsetName != null) {
        try {
          return new String(bytes, charsetName);
        } catch (UnsupportedEncodingException e) {

        }
      }
    }

    return Arrays.toString(bytes);
  }

}