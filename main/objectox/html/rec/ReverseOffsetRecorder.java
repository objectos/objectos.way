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

import objectox.html.HtmlByteProto;

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

    main.add(b0);

    main.addVarInt(offset);

    main.add(bx);
  }

}
