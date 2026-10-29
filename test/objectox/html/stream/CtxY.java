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
package objectox.html.stream;

import java.util.function.Consumer;
import objectos.html.stream.Event;
import objectos.internal.Util;
import objectox.html.IntSize;
import objectox.html.markup.HtmlSink;
import objectox.html.markup.HtmlSinkY;

final class CtxY {

  public boolean emitted = false;

  private Event result;

  private HtmlSink sink = new HtmlSink();

  private Frame[] stack = Ctx.EMPTY_STACK;

  private int stackIndex = -1;

  public static Ctx create(Consumer<? super CtxY> opts) {
    final CtxY y;
    y = new CtxY();

    opts.accept(y);

    return y.build();
  }

  public final int addByte(byte value) {
    return sink.addByte(value);
  }

  public final void addInt(IntSize size, int value) {
    sink.addInt(size, value);
  }

  public final void addInt16(int value) {
    sink.addInt16(value);
  }

  public final void addInt24(int value) {
    sink.addInt24(value);
  }

  public final void addInt8(int value) {
    sink.addInt8(value);
  }

  public final int addObject(Object value) {
    return sink.addObject(value);
  }

  public final void codeIndex(int value) {
    sink.codeIndex(value);
  }

  public final void objectsIndex(int value) {
    sink.objectsIndex(value);
  }

  public final void push(Consumer<? super FrameY> opts) {
    final Frame frame;
    frame = FrameY.create(opts);

    push(frame);
  }

  private void push(Frame value) {
    final int index;
    index = ++stackIndex;

    stack = Util.growIfNecessary(stack, index);

    stack[index] = value;
  }

  public final void result(Event value) {
    result = value;
  }

  public final void sink(Ctx ctx) {
    sink = HtmlSinkY.of(ctx.code, ctx.objects);
  }

  private Ctx build() {
    final Ctx ctx;
    ctx = new Ctx(
        sink.code(),

        sink.objects(),

        stack,

        stackIndex
    );

    ctx.emitted(emitted);

    if (result != null) {
      ctx.result(result);
    }

    return ctx;
  }

}
