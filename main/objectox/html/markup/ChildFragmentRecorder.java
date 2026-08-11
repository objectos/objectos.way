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

import objectox.html.HtmlPojo;
import objectox.html.IntSize;

final class ChildFragmentRecorder {

  private final HtmlSink sink;

  ChildFragmentRecorder(HtmlSink sink) {
    this.sink = sink;
  }

  public final void record(HtmlPojo html) {
    final int fragIndex;
    fragIndex = sink.addObject(html);

    final IntSize fragSize;
    fragSize = IntSize.of(fragIndex);

    switch (fragSize) {
      case BIT8 -> {
        sink.addByte(HtmlBytes.FRAG8);

        sink.addInt8(fragIndex);
      }

      case BIT16 -> {
        sink.addByte(HtmlBytes.FRAG16);

        sink.addInt16(fragIndex);
      }

      case BIT24 -> {
        sink.addByte(HtmlBytes.FRAG24);

        sink.addInt24(fragIndex);
      }
    }
  }

}
