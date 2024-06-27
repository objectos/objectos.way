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

class HttpModuleMatcherParser {

  private static final char SOLIDUS = '/';

  private static final String PATH_DOES_START_WITH_SOLIDUS = "Path does not start with a '/' character";

  private enum State {

    START,

    SOLIDUS,

    SEGMENT,

    EOF;

  }

  private int index;

  private int startIndex;

  private HttpModuleMatcher result;

  private String source;

  private State state;

  final HttpModuleMatcher matcher(String path) {
    set(path);

    while (state != State.EOF) {
      state = execute();
    }

    return result;
  }

  private void createExact() {
    String value;
    value = source.substring(startIndex, index);

    HttpModuleMatcher matcher;
    matcher = new HttpModuleMatcher.Exact(value);

    setResult(matcher);
  }

  private void setResult(HttpModuleMatcher matcher) {
    if (result != null) {
      throw new UnsupportedOperationException("Implement me");
    } else {
      result = matcher;
    }
  }

  private State execute() {
    if (!hasNext()) {
      return executeEOF();
    }

    char next;
    next = next();

    return switch (next) {
      case SOLIDUS -> executeSolidus();

      default -> executeDefault();
    };
  }

  private State executeDefault() {
    return switch (state) {
      case START -> throw illegal(PATH_DOES_START_WITH_SOLIDUS);

      case SOLIDUS -> State.SEGMENT;

      case SEGMENT -> state;

      case EOF -> throw new AssertionError();
    };
  }

  private State executeEOF() {
    return switch (state) {
      // empty string
      case START -> throw illegal(PATH_DOES_START_WITH_SOLIDUS);

      default -> {
        createExact();

        yield State.EOF;
      }
    };
  }

  private State executeSolidus() {
    return switch (state) {
      case START -> State.SOLIDUS;

      case SOLIDUS -> state;

      case SEGMENT -> State.SOLIDUS;

      case EOF -> throw new AssertionError();
    };
  }

  private IllegalArgumentException illegal(String prefix) {
    return new IllegalArgumentException(prefix + ": " + source);
  }

  private boolean hasNext() {
    return index < source.length();
  }

  private char next() {
    return source.charAt(index++);
  }

  private void set(String path) {
    index = startIndex = 0;

    result = null;

    source = path;

    state = State.START;
  }

}