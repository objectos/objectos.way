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

final class ReverseOffsetRecorder {

  private final ByteArray main;

  ReverseOffsetRecorder(ByteArray main) {
    this.main = main;
  }

  public final void record(int from) {
    // mark the end
    final byte b0;
    b0 = HtmlByteProto.END;

    final byte bx;
    bx = HtmlByteProto.INTERNAL;

    // store the distance to the contents (yes, reversed)
    final int mainIndex;
    mainIndex = main.size();

    final int offset;
    offset = mainIndex - from;

    if (offset <= HtmlBytes.VARINT_MAX1) {
      final byte b1;
      b1 = (byte) offset;

      main.add(b0, b1, bx);
    }

    else if (offset <= HtmlBytes.VARINT_MAX2) {
      final byte b1;
      b1 = HtmlBytes.encodeVarintHigh(offset, 7);

      final byte b2;
      b2 = HtmlBytes.encodeVarint(offset, 0);

      main.add(b0, b1, b2, bx);
    }

    else if (offset <= HtmlBytes.VARINT_MAX3) {
      final byte b1;
      b1 = HtmlBytes.encodeVarintHigh(offset, 14);

      final byte b2;
      b2 = HtmlBytes.encodeVarint(offset, 7);

      final byte b3;
      b3 = HtmlBytes.encodeVarint(offset, 0);

      main.add(b0, b1, b2, b3, bx);
    }

    else {
      final String msg;
      msg = "HTML is too large to record: offset=%d".formatted(offset);

      throw new IllegalArgumentException(msg);
    }
  }

}
