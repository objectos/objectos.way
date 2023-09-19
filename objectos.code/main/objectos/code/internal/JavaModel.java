/*
 * Copyright (C) 2014-2023 Objectos Software LTDA.
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
package objectos.code.internal;

public final class JavaModel {

  private JavaModel() {}

  public static void checkIdentifier(String s) {
    if (s.isEmpty()) {
      throw new IllegalArgumentException("Identifier must not be empty");
    }

    checkName(s, false, "an invalid identifier");
  }

  public static void checkMethodName(String methodName) {
    if (methodName.isEmpty()) {
      throw new IllegalArgumentException("Method name must not be empty");
    }

    checkName(methodName, false, "an invalid method name");
  }

  public static void checkPackageName(String s) {
    checkName(s, true, "an invalid package name");
  }

  public static void checkSimpleName(String s) {
    if (s.isEmpty()) {
      throw new IllegalArgumentException("A simple name must not be empty");
    }

    checkName(s, false, "an invalid simple name");
  }

  public static void checkVarName(String name) {
    if (name.isEmpty()) {
      throw new IllegalArgumentException("Local variable name must not be empty");
    }

    checkName(name, false, "an invalid local variable name");
  }

  static void checkFieldName(String name) {
    if (name.isEmpty()) {
      throw new IllegalArgumentException("Field name must not be empty");
    }

    checkName(name, false, "an invalid field name");
  }

  private static void checkName(String s, boolean allowDots, String message) {
    var hasDot = false;

    enum Parser {
      ID_START,

      ID_PART,

      DOT,

      HIGH_SURROGATE_START,

      HIGH_SURROGATE_PART;
    }

    var state = Parser.ID_START;

    char high = '0';

    for (int i = 0, len = s.length(); i < len; i++) {
      char c = s.charAt(i);

      state = switch (state) {
        case ID_START, DOT -> {
          if (Character.isHighSurrogate(c)) {
            high = c;

            yield Parser.HIGH_SURROGATE_START;
          }

          if (!Character.isJavaIdentifierStart(c)) {
            throw new IllegalArgumentException("""
            The string %s is %s:

            Character '%s' is an invalid java identifier start.
            """.formatted(s, message, c));
          }

          yield Parser.ID_PART;
        }

        case ID_PART -> {
          if (c == '.') {
            hasDot = true;

            yield Parser.DOT;
          }

          if (Character.isHighSurrogate(c)) {
            high = c;

            yield Parser.HIGH_SURROGATE_PART;
          }

          if (!Character.isJavaIdentifierPart(c)) {
            throw new IllegalArgumentException("""
            The string %s is %s:

            Character '%s' is an invalid java identifier part.
            """.formatted(s, message, c));
          }

          yield Parser.ID_PART;
        }

        case HIGH_SURROGATE_START -> {
          if (!Character.isLowSurrogate(c)) {
            throw new IllegalArgumentException("""
            The string %s is %s:

            The unicode high-low surrogate sequence is invalid %s %s
            """.formatted(s, message, high, c));
          }

          int cp = Character.toCodePoint(high, c);

          if (!Character.isJavaIdentifierStart(cp)) {
            throw new IllegalArgumentException("""
            The string %s is %s:

            Character '%s' is an invalid java identifier start.
            """.formatted(s, message, Character.toString(cp)));
          }

          yield Parser.ID_PART;
        }

        case HIGH_SURROGATE_PART -> {
          if (!Character.isLowSurrogate(c)) {
            throw new IllegalArgumentException("""
            The string %s is %s:

            The unicode high-low surrogate sequence is invalid %s %s
            """.formatted(s, message, high, c));
          }

          int cp = Character.toCodePoint(high, c);

          if (!Character.isJavaIdentifierPart(c)) {
            throw new IllegalArgumentException("""
            The string %s is %s:

            Character '%s' is an invalid java identifier part.
            """.formatted(s, message, Character.toString(cp)));
          }

          yield Parser.ID_PART;
        }

        default -> throw new AssertionError("Unknown state=" + state + ";string=" + s);
      };
    }

    if (state == Parser.DOT) {
      throw new IllegalArgumentException("""
      The string %s is %s:

      It must not end with a '.' character.
      """.formatted(s, message));
    }

    if (hasDot && !allowDots) {
      throw new IllegalArgumentException("""
      The string %s is %s:

      It must not contain a '.' character.
      """.formatted(s, message));
    }
  }

}