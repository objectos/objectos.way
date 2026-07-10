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
import objectox.html.HtmlBytes;

final class ElementInternalRecorder {

  private final ByteArray aux;

  private final ByteArray main;

  ElementInternalRecorder(ByteArray aux, ByteArray main) {
    this.aux = aux;

    this.main = main;
  }

  public final int record(int contentsIndex) {
    aux.add(HtmlByteProto.INTERNAL);

    final int protoIndex;
    protoIndex = contentsIndex - 1;

    final byte proto;
    proto = main.get(protoIndex);

    return switch (proto) {
      case HtmlByteProto.INTERNAL -> {
        final int endIndex;
        endIndex = protoIndex - 1;

        int cursor;
        cursor = endIndex;

        byte maybeNeg;

        do {
          maybeNeg = main.get(cursor--);
        } while (maybeNeg < 0);

        final int length;
        length = HtmlBytes.decodeCommonEnd(main, cursor, endIndex);

        yield cursor - length;
      }

      case HtmlByteProto.INTERNAL3 -> contentsIndex - 3;

      case HtmlByteProto.INTERNAL4 -> contentsIndex - 4;

      case HtmlByteProto.INTERNAL5 -> contentsIndex - 5;

      case HtmlByteProto.INTERNAL6 -> contentsIndex - 6;

      default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
      );
    };
  }

}
