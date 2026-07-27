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

import java.util.function.IntConsumer;
import objectos.html.rec.Instruction;
import objectox.html.IntSize;

final class ContentsRecorder {

  private final HtmlSink sink;

  private final IntConsumer marker;

  ContentsRecorder(HtmlSink sink) {
    this(sink, sink::consume);
  }

  ContentsRecorder(HtmlSink sink, IntConsumer marker) {
    this.sink = sink;

    this.marker = marker;
  }

  public final void record(Instruction... instructions) {
    final int length;
    length = instructions.length;

    if (length == 0) {
      sink.addByte(HtmlBytes.VALUES8);

      sink.addInt8(0);
    } else {
      record0(instructions, length);
    }
  }

  private void record0(Instruction[] instructions, int length) {
    final int lastIndex;
    lastIndex = length - 1;

    final Instruction last;
    last = instructions[lastIndex];

    final int optmisticMax;
    optmisticMax = last.value();

    final IntSize optmisticMaxSize;
    optmisticMaxSize = IntSize.of(optmisticMax);

    final int actualMax;
    actualMax = record1(instructions, length, optmisticMaxSize);

    if (actualMax != optmisticMax) {
      throw new UnsupportedOperationException("Implement me");
    }
  }

  private int record1(Instruction[] instructions, int length, IntSize size) {
    final byte proto;
    proto = proto(size);

    sink.addByte(proto);

    sink.addInt(size, length);

    int max;
    max = 0;

    for (Instruction instruction : instructions) {
      final int value;
      value = instruction.value();

      sink.addInt(size, value);

      marker.accept(value);

      max = Math.max(max, value);
    }

    return max;
  }

  private byte proto(IntSize size) {
    return switch (size) {
      case BIT8 -> HtmlBytes.VALUES8;

      case BIT16 -> HtmlBytes.VALUES16;

      case BIT24 -> HtmlBytes.VALUES24;
    };
  }

}
