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
import objectox.html.HtmlByteProto;

final class NextElementNode {

  private final Tape tape;

  private final ElementName elementName;

  NextElementNode(Tape tape, ElementName elementName) {
    this.tape = tape;

    this.elementName = elementName;
  }

  public final State compute() {
    while (tape.hasByte()) {
      final byte proto;
      proto = tape.nextByte();

      switch (proto) {
        case HtmlByteProto.ATTRIBUTE1 -> tape.skipVarIntLE();

        case HtmlByteProto.ELEMENT -> {
          return computeElement();
        }

        case HtmlByteProto.END -> {
          return computeEnd();
        }

        default -> throw State.implMe(proto);
      }
    }

    throw new IllegalStateException();
  }

  private State computeElement() {
    final int offset;
    offset = tape.nextVarIntLE();

    tape.push(FrameKind.NEXT_ELEMENT_NODE);

    tape.skip(-offset);

    final NextElementElement next;
    next = new NextElementElement(tape, elementName);

    return next.compute();
  }

  private State computeEnd() {
    final FrameKind parentKind;
    parentKind = tape.pop();

    return new EndTagState(tape, elementName, parentKind);
  }

}
