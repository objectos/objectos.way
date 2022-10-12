/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
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
package br.com.objectos.code.java;

final class Trim extends StringConversion {

  static final StringConversion INSTANCE = new Trim();

  private static final char SPACE = ' ';

  @Override
  protected final void executeLastRound(Object state, StringBuilder builder) {
    int length;
    length = builder.length();

    if (length == 0) {
      return;
    }

    int index;
    index = length - 1;

    char c;
    c = builder.charAt(index);

    while (c <= SPACE) {
      index--;

      c = builder.charAt(index);
    }

    int newLength;
    newLength = index + 1;

    if (newLength < length) {
      builder.setLength(newLength);
    }
  }

  @Override
  protected final Object executeOne(Object state, StringBuilder builder, int c) {
    State s;
    s = (State) state;

    switch (s) {
      case SKIP:
        if (c <= SPACE) {
          return state;
        }

        // fall through
      case APPEND:
        builder.appendCodePoint(c);

        return State.APPEND;
      default:
        throw new AssertionError("Unexpected case " + s);
    }
  }

  @Override
  protected final Object startingState() {
    return State.SKIP;
  }

  private enum State {

    APPEND,

    SKIP;

  }

}