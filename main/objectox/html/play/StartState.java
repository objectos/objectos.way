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
package objectox.html.play;

import objectox.html.HtmlByteProto;

final class StartState implements State {

  private final Tape tape;

  StartState(Tape tape) {
    this.tape = tape;
  }

  @Override
  public final State compute() {
    while (tape.hasByte()) {
      final byte proto;
      proto = tape.peekByte();

      switch (proto) {
        case HtmlByteProto.LENGTH2 -> {
          tape.skipByte();

          final int offset;
          offset = tape.nextInt16();

          tape.skip(offset);

          continue;
        }

        case HtmlByteProto.LENGTH3 -> { throw new UnsupportedOperationException("Implement me"); }

        case HtmlByteProto.MARKED3 -> { tape.skip(3); continue; }

        case HtmlByteProto.MARKED4 -> { tape.skip(4); continue; }

        case HtmlByteProto.MARKED5 -> { tape.skip(5); continue; }

        case HtmlByteProto.MARKED6 -> { tape.skip(6); continue; }
      }

      break;
    }

    return new BeginDocumentState(tape);
  }

}
