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

final class ElementEncoder {

  private final ByteArray main;

  ElementEncoder(ByteArray main) {
    this.main = main;
  }

  public final int encode(final int startIndex) {
    int index;
    index = startIndex;

    // mark this element
    main.set(index++, HtmlByteProto.LENGTH2);

    // decode the length
    final byte len0;
    len0 = main.get(index++);

    final byte len1;
    len1 = main.get(index++);

    // point to next element
    final int offset;
    offset = HtmlBytes.decodeInt(len0, len1);

    final int nextIndex;
    nextIndex = index + offset;

    main.add(HtmlByteProto.ELEMENT);

    final int length;
    length = main.size() - startIndex;

    HtmlBytes.encodeOffset(main, length);

    return nextIndex;
  }

}
