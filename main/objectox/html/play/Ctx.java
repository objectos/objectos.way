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

import java.util.NoSuchElementException;
import objectos.html.play.HtmlEvent;
import objectos.internal.Util;

final class Ctx {

  private final byte[] code;

  private boolean computed;

  private final Object[] objects;

  private HtmlEvent result;

  private int[] stack = Util.EMPTY_INT_ARRAY;

  private int stackIndex = -1;

  Ctx(byte[] code, Object[] objects) {
    this.code = code;

    this.objects = objects;

    push(0);
  }

  public final boolean hasByte() {
    return stack[stackIndex] < code.length;
  }

  public final boolean hasEvent() {
    return result != null;
  }

  public final HtmlEvent nextEvent() {
    if (!computed) {
      throw new NoSuchElementException();
    }

    final HtmlEvent res;
    res = result;

    computed = false;

    result = null;

    return res;
  }

  public final byte nextByte() {
    final int idx;
    idx = stack[stackIndex]++;

    return code[idx];
  }

  public final void pop() {
    stackIndex--;
  }

  public final void push(int value) {
    final int index;
    index = ++stackIndex;

    stack = Util.growIfNecessary(stack, index);

    stack[index] = value;
  }

  public final boolean shouldCompute() {
    return !computed;
  }

  public final void skip(int value) {
    stack[stackIndex] += value;
  }

  public final void result(HtmlEvent value) {
    if (computed) {
      final String msg;
      msg = "Result has not been consumed yet";

      throw new IllegalStateException(msg);
    }

    computed = true;

    result = value;
  }

}
