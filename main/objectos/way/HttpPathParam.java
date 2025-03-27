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

sealed abstract class HttpPathParam {

  static final class Digits extends HttpPathParam {

    public Digits(String name) {
      super(name);
    }

    @Override
    final boolean test(String value) {
      int len;
      len = value.length();

      if (len == 0) {
        return false;
      }

      for (int i = 0; i < len; i++) {
        char c;
        c = value.charAt(i);

        if (!Character.isDigit(c)) {
          return false;
        }
      }

      return true;
    }

  }

  static final class NotEmpty extends HttpPathParam {

    public NotEmpty(String name) {
      super(name);
    }

    @Override
    final boolean test(String value) {
      return !value.isEmpty();
    }

  }

  static final class Regex extends HttpPathParam {

    private final Pattern pattern;

    public Regex(String name, Pattern pattern) {
      super(name);

      this.pattern = pattern;
    }

    @Override
    final boolean test(String value) {
      Matcher matcher;
      matcher = pattern.matcher(value);

      return matcher.matches();
    }

  }

  final String name;

  HttpPathParam(String name) {
    this.name = name;
  }

  final boolean test(HttpSupport path) {
    String value;
    value = path.pathParam(name);

    if (value == null) {
      return true;
    } else {
      return test(value);
    }
  }

  abstract boolean test(String value);

}