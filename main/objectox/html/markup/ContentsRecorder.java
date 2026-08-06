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
package objectox.html.markup;

import java.util.OptionalInt;
import java.util.function.IntConsumer;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;
import objectos.html.AttributeName;
import objectos.html.Attribute;
import objectos.html.markup.Instruction;
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
    record(0, instructions);
  }

  public final void record(int offset, Instruction... instructions) {
    final int length;
    length = instructions.length;

    if (length == 0) {
      sink.addByte(HtmlBytes.VALUES8);

      sink.addInt8(0);
    } else {
      record0(offset, instructions, length);
    }
  }

  private void record0(int offset, Instruction[] instructions, int length) {
    final int lastIndex;
    lastIndex = length - 1;

    final Instruction last;
    last = instructions[lastIndex];

    final int optmisticMax;
    optmisticMax = last.value();

    final IntSize size;
    size = IntSize.of(optmisticMax);

    final byte proto;
    proto = proto(size);

    sink.addByte(proto);

    sink.addInt(size, length);

    final Recorder f;
    f = new Recorder(offset, length, size);

    final OptionalInt max;
    max = Stream.of(instructions).mapToInt(f).max();

    final int actualMax;
    actualMax = max.getAsInt();

    if (actualMax != optmisticMax) {
      throw new UnsupportedOperationException("Implement me");
    }
  }

  private byte proto(IntSize size) {
    return switch (size) {
      case BIT8 -> HtmlBytes.VALUES8;

      case BIT16 -> HtmlBytes.VALUES16;

      case BIT24 -> HtmlBytes.VALUES24;
    };
  }

  private class Recorder implements ToIntFunction<Instruction> {

    private AttributeRecorder attributeRecorder;

    private BooleanAttributeRecorder booleanAttributeRecorder;

    private HtmlSink offsetSink;

    final int offset;

    final int length;

    final IntSize size;

    Recorder(int offset, int length, IntSize size) {
      this.offset = offset;

      this.length = length;

      this.size = size;
    }

    @Override
    public final int applyAsInt(Instruction instruction) {
      final Instruction pre;
      pre = pre(instruction);

      final int result;
      result = pre.value();

      sink.addInt(size, result);

      marker.accept(result);

      return result;
    }

    private Instruction pre(Instruction instruction) {
      if (instruction instanceof Attribute attr) {
        return attr(attr);
      } else {
        return instruction;
      }
    }

    private Instruction attr(Attribute attr) {
      if (offsetSink == null) {
        offsetSink = sink.offset(offset, length, size);
      }

      final AttributeName name;
      name = attr.attrName();

      final String value;
      value = attr.attrValue();

      if (value == null) {
        if (booleanAttributeRecorder == null) {
          booleanAttributeRecorder = new BooleanAttributeRecorder(offsetSink);
        }

        return booleanAttributeRecorder.record(name);
      }

      else {
        if (attributeRecorder == null) {
          attributeRecorder = new AttributeRecorder(offsetSink);
        }

        return attributeRecorder.record(name, value);
      }
    }

  }

}
