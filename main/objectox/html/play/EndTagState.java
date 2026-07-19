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

import objectos.html.ElementName;
import objectos.html.play.EndTag;
import objectos.html.play.Piece;
import objectox.html.HtmlByteProto;

public final class EndTagState implements EndTag, State {

  private final Tape tape;

  private final ElementName name;

  EndTagState(Tape tape, ElementName name) {
    this.tape = tape;

    this.name = name;
  }

  @Override
  public final State compute() {
    tape.pop(); // return 2 parent

    final byte proto;
    proto = tape.nextByte();

    return switch (proto) {
      case HtmlByteProto.ROOT_ELEMENT -> {
        final int offset;
        offset = tape.nextInt16();

        tape.skip(offset);

        yield new NextDocumentNode(tape).compute();
      }

      default -> throw State.implMe(proto);
    };
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
  public final String name() {
    return name.name();
  }

  @Override
  public final String toString() {
    return "EndTag(" + name() + ")";
  }

}
