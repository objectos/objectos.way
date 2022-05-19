/*
 * Copyright (C) 2022-2022 Objectos Software LTDA.
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
package objectos.lang;

final class ToJavaCamelCase extends StringConversion {

  static final StringConversion LOWER = new ToJavaCamelCase(State.LOWER_START);

  static final StringConversion UPPER = new ToJavaCamelCase(State.UPPER_START);

  private final State startingState;

  ToJavaCamelCase(State startingState) {
    this.startingState = startingState;
  }

  @Override
  protected final Object executeOne(Object state, StringBuilder builder, int c) {
    State s;
    s = (State) state;

    int converted;

    switch (s) {
      case UPPER_START:
        if (Character.isJavaIdentifierStart(c)) {
          converted = Character.toUpperCase(c);

          builder.appendCodePoint(converted);

          return State.NAME_PART;
        } else {
          return state;
        }
      case NAME_PART:
        if (Character.isJavaIdentifierPart(c)) {
          builder.appendCodePoint(c);

          return state;
        } else {
          return State.NAME_UPPER;
        }
      case NAME_UPPER:
        if (Character.isJavaIdentifierPart(c)) {
          converted = Character.toUpperCase(c);

          builder.appendCodePoint(converted);

          return State.NAME_PART;
        } else {
          return state;
        }
      case LOWER_START:
        if (Character.isJavaIdentifierStart(c)) {
          converted = Character.toLowerCase(c);

          builder.appendCodePoint(converted);

          return State.NAME_PART;
        } else {
          return state;
        }
      default:
        throw new AssertionError("Unexpected case " + s);
    }
  }

  @Override
  protected final Object startingState() {
    return startingState;
  }

  private enum State {

    LOWER_START,

    NAME_PART,

    NAME_UPPER,

    UPPER_START;

  }

}