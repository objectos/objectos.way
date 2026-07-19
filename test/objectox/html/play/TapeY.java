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

import java.util.function.Consumer;
import objectos.internal.Util;

final class TapeY {

  byte[] main = Util.EMPTY_BYTE_ARRAY;

  int mainIndex = 0;

  Object[] objects = Util.EMPTY_OBJECT_ARRAY;

  int[] stack = Util.EMPTY_INT_ARRAY;

  int stackIndex = -1;

  public static Tape create(Consumer<? super TapeY> opts) {
    final TapeY y;
    y = new TapeY();

    opts.accept(y);

    return y.build();
  }

  public final void main(byte... values) {
    main = values;
  }

  public final void stack(int... values) {
    stack = values;

    stackIndex = stack.length - 1;
  }

  private Tape build() {
    return new Tape(
        main,

        mainIndex,

        objects,

        stack,

        stackIndex
    );
  }

}
