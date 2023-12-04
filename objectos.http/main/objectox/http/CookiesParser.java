/*
 * Copyright (C) 2023 Objectos Software LTDA.
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

import java.util.HashMap;
import java.util.Map;
import objectos.http.Cookies;

public final class CookiesParser {

  private final Map<String, CookieValue> cookies = new HashMap<>();

  private CookieValue currentValue;

  private int index;

  private final StringBuilder sb = new StringBuilder();

  private final String source;

  private State state;

  public CookiesParser(String s) {
    this.source = s;
  }

  public final Cookies parse() {
    state = State.START;

    while (state != State.STOP) {
      state = execute();
    }

    if (cookies.isEmpty()) {
      return EmptyCookies.INSTANCE;
    } else {
      return new StandardCookies(cookies);
    }
  }

  private State execute() {
    return switch (state) {
      case START -> executeStart();

      case NAME -> executeName();

      case VALUE -> executeValue();

      case MALFORMED -> executeMalformed();

      case STOP -> throw new IllegalStateException("Non-executable state=" + state);
    };
  }

  private State executeName() {
    if (!hasNext()) {
      // name without value
      return State.MALFORMED;
    }

    char c;
    c = next();

    if (c == '=') {
      String name;
      name = sb.toString();

      sb.setLength(0);

      CookieValue value;
      value = cookies.get(name);

      if (value == null) {
        value = CookieValue.of(source, index);
      } else {
        value = value.beginNew(index);
      }

      cookies.put(name, value);

      currentValue = value;

      return State.VALUE;
    }

    sb.append(c);

    return state;
  }

  private State executeValue() {
    if (!hasNext()) {
      currentValue.endIndex(index);

      return State.STOP;
    }

    char c;
    c = next();

    if (c == ';') {
      currentValue.endIndex(index);

      return State.START;
    }

    return state;
  }

  private State executeStart() {
    if (!hasNext()) {
      return State.STOP;
    }

    char c;
    c = next();

    if (Character.isWhitespace(c)) {
      // trim initial whitespace
      return state;
    }

    if (c == '=') {
      // empty name
      return State.MALFORMED;
    }

    // name start
    sb.append(c);

    return State.NAME;
  }

  private State executeMalformed() {
    throw new UnsupportedOperationException("Implement me");
  }

  private boolean hasNext() {
    return index < source.length();
  }

  private char next() {
    return source.charAt(index++);
  }

  private enum State {
    START,

    NAME,

    VALUE,

    MALFORMED,

    STOP;
  }

}