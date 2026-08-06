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

import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.function.IntConsumer;
import objectos.html.AttributeName;
import objectos.html.Attribute;
import objectos.html.markup.Instruction;
import objectox.html.elem.ElementNamePojo;
import org.testng.annotations.Test;

public class ContentsRecorderTest {

  private static final class Marker implements IntConsumer {
    private final int[] values = new int[10];
    private int cursor;

    @Override
    public final void accept(int value) {
      values[cursor++] = value;
    }

    public final int[] values() {
      return Arrays.copyOf(values, cursor);
    }
  }

  @Test
  public void record01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final Marker marker;
    marker = new Marker();

    final ContentsRecorder subject;
    subject = new ContentsRecorder(sink, marker);

    subject.record();

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
        })
    );

    assertEquals(marker.values(), new int[] {});
  }

  @Test
  public void record02() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final Marker marker;
    marker = new Marker();

    final ContentsRecorder subject;
    subject = new ContentsRecorder(sink, marker);

    subject.record(values(0, HtmlSink.MAX_INT8));

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(2);
          opts.addInt8(0);
          opts.addInt8(HtmlSink.MAX_INT8);
        })
    );

    assertEquals(marker.values(), new int[] {0, HtmlSink.MAX_INT8});
  }

  @Test
  public void record03() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final Marker marker;
    marker = new Marker();

    final ContentsRecorder subject;
    subject = new ContentsRecorder(sink, marker);

    subject.record(values(0, HtmlSink.MAX_INT16));

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.VALUES16);
          opts.addInt16(2);
          opts.addInt16(0);
          opts.addInt16(HtmlSink.MAX_INT16);
        })
    );

    assertEquals(marker.values(), new int[] {0, HtmlSink.MAX_INT16});
  }

  @Test
  public void record04() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final Marker marker;
    marker = new Marker();

    final ContentsRecorder subject;
    subject = new ContentsRecorder(sink, marker);

    subject.record(values(0, HtmlSink.MAX_INT24));

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.VALUES24);
          opts.addInt24(2);
          opts.addInt24(0);
          opts.addInt24(HtmlSink.MAX_INT24);
        })
    );

    assertEquals(marker.values(), new int[] {0, HtmlSink.MAX_INT24});
  }

  @Test
  public void record05() {
    final HtmlSink sink;
    sink = new HtmlSink();

    sink.addByte(HtmlBytes.STARTTAG8);
    sink.addInt8(ElementNamePojo.HTML.index());

    final Marker marker;
    marker = new Marker();

    final ContentsRecorder subject;
    subject = new ContentsRecorder(sink, marker);

    subject.record(
        2,
        new AttributePointer(11),
        Attribute.of(AttributeName.AUTOFOCUS),
        new AttributePointer(22)
    );

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.STARTTAG8);
          opts.addInt8(ElementNamePojo.HTML.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(3);
          opts.addInt8(11);
          opts.addInt8(9);
          opts.addInt8(22);
          opts.addByte((byte) 0);
          opts.addByte((byte) 0);
          opts.addByte(HtmlBytes.BOOLEAN8);
          opts.addInt8(AttributeNamePojo.AUTOFOCUS.index());
        })
    );

    assertEquals(marker.values(), new int[] {11, 9, 22});
  }

  private Instruction[] values(int... values) {
    final int length;
    length = values.length;

    final Instruction[] result;
    result = new Instruction[length];

    for (int idx = 0; idx < length; idx++) {
      final int value;
      value = values[idx];

      result[idx] = new AttributePointer(value);
    }

    return result;
  }

}
