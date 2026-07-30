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
package objectox.html.rec;

public final class CommentEscaper {

  private final String value;

  public CommentEscaper(String value) {
    this.value = value;
  }

  public final String escape() {
    final int len;
    len = value.length();

    if (len == 0) {
      return value;
    } else {
      return escape0(len);
    }
  }

  private enum State {

    NORMAL,

    DASH1,

    DASH2,

    ESCAPE;

    final State consume(char c) {
      return switch (this) {
        case NORMAL -> c == '-' ? State.DASH1 : this;

        case DASH1 -> c == '-' ? State.DASH2 : State.NORMAL;

        case DASH2 -> c == '>' ? State.ESCAPE : State.NORMAL;

        case ESCAPE -> State.NORMAL;
      };
    }

  }

  private String escape0(int len) {
    State state;
    state = State.NORMAL;

    for (int idx = 0; idx < len; idx++) {
      final char c;
      c = value.charAt(idx);

      final State next;
      next = state.consume(c);

      if (next == State.ESCAPE) {
        return escape1(idx, len);
      }

      state = next;
    }

    return value;

  }

  private String escape1(int firstEscapeIndex, int len) {
    final StringBuilder out;
    out = new StringBuilder(len + 16);

    out.append(value, 0, firstEscapeIndex - 2);

    out.append("--&gt;");

    State state;
    state = State.NORMAL;

    for (int idx = firstEscapeIndex + 1; idx < len; idx++) {
      final char c;
      c = value.charAt(idx);

      final State next;
      next = state.consume(c);

      if (next == State.ESCAPE) {
        out.append(value, firstEscapeIndex + 1, idx - 2);

        out.append("--&gt;");

        firstEscapeIndex = idx;

        state = State.NORMAL;
      } else {
        state = next;
      }
    }

    out.append(value, firstEscapeIndex + 1, len);

    return out.toString();
  }

}