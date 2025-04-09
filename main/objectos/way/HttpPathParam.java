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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class HttpPathParam {

  private enum Kind {

    DIGITS,

    NOT_EMPTY,

    REGEX;

  }

  private final Kind kind;

  private final String name;

  private final Object aux;

  private HttpPathParam(Kind kind, String name, Object aux) {
    this.kind = kind;
    this.name = name;
    this.aux = aux;
  }

  public static HttpPathParam digits(String name) {
    return new HttpPathParam(Kind.DIGITS, name, null);
  }

  public static HttpPathParam notEmpty(String name) {
    return new HttpPathParam(Kind.NOT_EMPTY, name, null);
  }

  public static HttpPathParam regex(String name, Pattern pattern) {
    return new HttpPathParam(Kind.REGEX, name, pattern);
  }

  public final boolean test(HttpExchange path) {
    String value;
    value = path.pathParam(name);

    if (value == null) {
      return true;
    } else {
      return test(value);
    }
  }

  private boolean test(String value) {
    return switch (kind) {
      case DIGITS -> {
        int len;
        len = value.length();

        if (len == 0) {
          yield false;
        }

        for (int i = 0; i < len; i++) {
          char c;
          c = value.charAt(i);

          if (!Character.isDigit(c)) {
            yield false;
          }
        }

        yield true;
      }

      case NOT_EMPTY -> !value.isEmpty();

      case REGEX -> {
        final Pattern pattern;
        pattern = (Pattern) aux;

        final Matcher matcher;
        matcher = pattern.matcher(value);

        yield matcher.matches();
      }
    };
  }

}