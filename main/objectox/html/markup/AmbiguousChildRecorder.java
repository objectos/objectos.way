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

import objectox.html.IntSize;

final class AmbiguousChildRecorder {

  private final HtmlSink sink;

  AmbiguousChildRecorder(HtmlSink sink) {
    this.sink = sink;
  }

  public final int record(IntSize size, int value) {
    final byte b;
    b = sink.getByte(value);

    return switch (b) {
      case HtmlBytes.AMBIGUOUS88 -> ambiguous(HtmlBytes.eAMBIGUOUS88, size, value);

      case HtmlBytes.AMBIGUOUS816 -> ambiguous(HtmlBytes.eAMBIGUOUS816, size, value);

      case HtmlBytes.AMBIGUOUS824 -> ambiguous(HtmlBytes.eAMBIGUOUS824, size, value);

      case HtmlBytes.aAMBIGUOUS88,
           HtmlBytes.aAMBIGUOUS816,
           HtmlBytes.aAMBIGUOUS824 -> 0;

      default -> throw new IllegalStateException();
    };
  }

  private int ambiguous(byte proto, IntSize size, int value) {
    sink.setByte(value, proto);

    sink.addInt(size, value);

    return 1;
  }

}
