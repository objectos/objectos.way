/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.selfgen.util;

final class ToJavaIdentifier extends StringConversion {

  static final StringConversion INSTANCE = new ToJavaIdentifier();

  @Override
  protected final Object executeOne(Object state, StringBuilder builder, int c) {
    State s;
    s = (State) state;

    switch (s) {
      case IDENTIFIER_START:
        if (Character.isJavaIdentifierStart(c)) {
          builder.appendCodePoint(c);

          return State.IDENTIFIER_PART;
        } else {
          return state;
        }
      case IDENTIFIER_PART:
        if (Character.isJavaIdentifierPart(c)) {
          builder.appendCodePoint(c);
        }

        return state;
      default:
        throw new AssertionError("Unexpected case " + s);
    }
  }

  @Override
  protected final Object startingState() {
    return State.IDENTIFIER_START;
  }

  private enum State {

    IDENTIFIER_PART,

    IDENTIFIER_START;

  }

}