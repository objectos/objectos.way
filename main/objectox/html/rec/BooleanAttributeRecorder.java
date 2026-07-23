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

import objectos.html.AttributeName;

final class BooleanAttributeRecorder {

  private final HtmlSink sink;

  BooleanAttributeRecorder(HtmlSink sink) {
    this.sink = sink;
  }

  public final AttributeInstruction record(AttributeName name) {
    final int startIndex;

    final int nameIndex;
    nameIndex = name.index();

    if (nameIndex <= HtmlSink.MAX_INT8) {
      startIndex = sink.addByte(HtmlBytes.BOOLEAN8);

      sink.addInt8(nameIndex);
    } else {
      throw new UnsupportedOperationException("Implement me");
    }

    return new AttributeInstruction(startIndex);
  }

}
