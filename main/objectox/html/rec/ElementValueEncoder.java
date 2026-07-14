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

import objectox.html.ByteArray;
import objectox.html.HtmlByteProto;

final class ElementValueEncoder {

  private final ByteArray aux;

  private final ByteArray main;

  private final Encoder encoder;

  ElementValueEncoder(ByteArray aux, ByteArray main, Encoder encoder) {
    this.aux = aux;

    this.main = main;

    this.encoder = encoder;
  }

  public final int auxStart() {
    return aux.size();
  }

  public final void encode(final int auxStart, final int mainContents) {
    int index;
    index = auxStart;

    final int indexMax;
    indexMax = aux.size();

    int contents;
    contents = mainContents;

    while (index < indexMax) {
      final byte mark;
      mark = aux.get(index++);

      switch (mark) {
        case HtmlByteProto.ATTRIBUTE_EXT0 -> main.add(
            mark,

            aux.get(index++)
        );

        case HtmlByteProto.ATTRIBUTE_EXT1 -> main.add(
            mark,

            aux.get(index++),

            aux.get(index++),

            aux.get(index++)
        );

        case HtmlByteProto.INTERNAL -> contents = encoder.encode(contents);

        default -> throw new UnsupportedOperationException(
            "Implement me :: mark=" + mark
        );
      }
    }
  }

}
