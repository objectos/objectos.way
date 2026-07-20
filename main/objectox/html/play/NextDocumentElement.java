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
import objectox.html.elem.ElementNamePojo;

final class NextDocumentElement {

  private final Tape tape;

  NextDocumentElement(Tape tape) {
    this.tape = tape;
  }

  public final BeginStartTagState compute() {
    final int offset;
    offset = tape.nextInt16();

    tape.push(FrameKind.DOC_ELEMENT, offset);

    final byte standardName;
    standardName = tape.nextByte();

    assert standardName == HtmlByteProto.STANDARD_NAME;

    final int ordinal;
    ordinal = tape.nextInt8();

    final ElementNamePojo name;
    name = ElementNamePojo.get(ordinal);

    tape.push(FrameKind.ELEMENT_NODES);

    return new BeginStartTagState(tape, name);
  }

}
