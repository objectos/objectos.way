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

import objectos.html.play.BeginDocument;
import objectos.html.play.Piece;
import objectox.html.HtmlByteProto;

public final class BeginDocumentState implements BeginDocument, State {

  private final Tape tape;

  BeginDocumentState(Tape tape) {
    this.tape = tape;
  }

  @Override
  public final State compute() {
    if (!tape.hasByte()) {
      return EndDocumentState.INSTANCE;
    }

    final byte proto;
    proto = tape.peekByte();

    final State step;
    step = switch (proto) {
      case HtmlByteProto.ELEMENT -> new StartElementState(tape);

      default -> throw State.implMe(proto);
    };

    return step.compute();
  }

  @Override
  public final boolean hasNext() {
    return true;
  }

  @Override
  public final Piece next() {
    return this;
  }

  @Override
  public final String toString() {
    return "BeginDocument";
  }

}
