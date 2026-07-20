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

import java.util.Objects;
import objectos.html.rec.ElementMarkup;
import objectos.html.rec.Instruction;
import objectox.html.HtmlByteProto;
import objectox.html.HtmlInstruction;

final class FlattenRecorder {

  private final ByteArray main;

  private final ElementValueEncoder elementValueEncoder;

  private final ElementValueRecorder elementValueRecorder;

  private final ForwardOffsetRecorder forwardOffsetRecorder;

  private final ReverseOffsetRecorder reverseOffsetRecorder;

  FlattenRecorder(
      ByteArray main,

      ElementValueEncoder elementValueEncoder,

      ElementValueRecorder elementValueRecorder,

      ForwardOffsetRecorder forwardOffsetRecorder,

      ReverseOffsetRecorder reverseOffsetRecorder
  ) {
    this.main = main;

    this.elementValueEncoder = elementValueEncoder;

    this.elementValueRecorder = elementValueRecorder;

    this.forwardOffsetRecorder = forwardOffsetRecorder;

    this.reverseOffsetRecorder = reverseOffsetRecorder;
  }

  public final ElementMarkup record(Instruction... contents) {
    final Instruction[] values;
    values = Objects.requireNonNull(contents, "contents == null");

    final int auxStart;
    auxStart = elementValueEncoder.auxStart();

    final int mainStart;
    mainStart = main.size();

    int mainContents;
    mainContents = mainStart;

    main.add(HtmlByteProto.FLATTEN);

    main.addInt16(ByteArray.MAX_INT16);

    for (int idx = 0; idx < values.length; idx++) {
      final Instruction instruction;
      instruction = values[idx];

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

    return HtmlInstruction.ELEMENT;
  }

  public final ElementMarkup record(Iterable<? extends Instruction> contents) {
    final Iterable<? extends Instruction> values;
    values = Objects.requireNonNull(contents, "contents == null");

    final int auxStart;
    auxStart = elementValueEncoder.auxStart();

    final int mainStart;
    mainStart = main.size();

    int mainContents;
    mainContents = mainStart;

    main.add(HtmlByteProto.FLATTEN);

    main.addInt16(ByteArray.MAX_INT16);

    for (Instruction value : values) {
      if (value == null) {
        final String msg;
        msg = "contents provided a null instruction";

        throw new NullPointerException(msg);
      }

      mainContents = elementValueRecorder.record(mainContents, value);
    }

    elementValueEncoder.encode(auxStart, mainContents);

    reverseOffsetRecorder.record(mainContents);

    forwardOffsetRecorder.two(mainStart);

    return HtmlInstruction.ELEMENT;
  }

}
