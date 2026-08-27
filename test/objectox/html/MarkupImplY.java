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
package objectox.html;

import java.util.function.Consumer;
import objectox.html.markup.HtmlSink;

public final class MarkupImplY {

  private IntSize codeInitial = IntSize.BIT8;

  private IntSize objectsInitial = IntSize.BIT8;

  private MarkupImplY() {}

  public static MarkupImpl create(Consumer<? super MarkupImplY> opts) {
    final MarkupImplY y;
    y = new MarkupImplY();

    opts.accept(y);

    return y.build();
  }

  public final void code(IntSize initial) {
    codeInitial = initial;
  }

  public final void objects(IntSize initial) {
    objectsInitial = initial;
  }

  private MarkupImpl build() {
    final int codeLen;
    codeLen = len(codeInitial);

    final byte[] code;
    code = new byte[codeLen];

    final int objectsLen;
    objectsLen = len(objectsInitial);

    final Object[] objects;
    objects = new Object[objectsLen];

    final HtmlSink sink;
    sink = new HtmlSink(code, objects);

    return MarkupImpl.of(sink);
  }

  private int len(IntSize size) {
    return switch (size) {
      case BIT8 -> 0;

      case BIT16 -> HtmlSink.MAX_INT8 + 1;

      case BIT24 -> HtmlSink.MAX_INT16 + 1;
    };
  }

}
