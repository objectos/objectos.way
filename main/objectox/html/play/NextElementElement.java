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
import objectox.html.elem.ElementNamePojo;

final class NextElementElement {

  private final Tape tape;

  @SuppressWarnings("unused")
  private final ElementName elementName;

  NextElementElement(Tape tape, ElementName elementName) {
    this.tape = tape;

    this.elementName = elementName;
  }

  public final BeginStartTagState compute() {
    tape.skipInt16();

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
