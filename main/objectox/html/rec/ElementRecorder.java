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

import objectos.html.ElementName;
import objectos.way.Html;
import objectox.html.ByteArray;
import objectox.html.ObjectArray;

final class ElementRecorder {

  private final ElementNameRecorder elementNameRecorder;

  private final ElementValueEncoder elementValueEncoder;

  private final ElementValueRecorder elementValueRecorder;

  private final ForwardOffsetRecorder forwardOffsetRecorder;

  private final ReverseOffsetRecorder reverseOffsetRecorder;

  ElementRecorder(
      ElementNameRecorder elementNameRecorder,

      ElementValueEncoder elementValueEncoder,

      ElementValueRecorder elementValueRecorder,

      ForwardOffsetRecorder forwardOffsetRecorder,

      ReverseOffsetRecorder reverseOffsetRecorder
  ) {
    this.elementNameRecorder = elementNameRecorder;

    this.elementValueEncoder = elementValueEncoder;

    this.elementValueRecorder = elementValueRecorder;

    this.forwardOffsetRecorder = forwardOffsetRecorder;

    this.reverseOffsetRecorder = reverseOffsetRecorder;
  }

  public static ElementRecorder of(ByteArray aux, ByteArray main, ObjectArray objects) {
    return new ElementRecorder(
        new ElementNameRecorder(main),

        new ElementValueEncoder(
            aux,

            new Encoder(main)
        ),

        new ElementValueRecorder(
            new AttributeObjectRecorder(aux, objects),

            new ElementInternalRecorder(aux, main)
        ),

        new ForwardOffsetRecorder(main),

        new ReverseOffsetRecorder(main)
    );
  }

  public final void record(ElementName name, Html.Instruction... contents) {
    final int auxStart;
    auxStart = elementValueEncoder.auxStart();

    final int mainStart;
    mainStart = elementNameRecorder.mainStart();

    int mainContents;
    mainContents = mainStart;

    elementNameRecorder.record(name);

    for (int idx = 0; idx < contents.length; idx++) {
      final Html.Instruction instruction;
      instruction = contents[idx];

      if (instruction == null) {
        final String msg;
        msg = "contents[%d] == null".formatted(idx);

        throw new NullPointerException(msg);
      }

      mainContents = elementValueRecorder.record(mainContents, instruction);
    }

    elementValueEncoder.encode(auxStart, mainContents);

    reverseOffsetRecorder.record(mainContents);

    forwardOffsetRecorder.two(mainStart);
  }

}
