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

import java.util.Objects;
import objectos.html.AttributeName;
import objectos.way.Html;
import objectox.html.HtmlByteProto;
import objectox.html.HtmlInstruction;

final class Attribute1Recorder {

  private final ByteArray main;

  private final ObjectArray objects;

  Attribute1Recorder(ByteArray main, ObjectArray objects) {
    this.main = main;

    this.objects = objects;
  }

  public final Html.Instruction.OfAttribute record(AttributeName name, Object value) {
    final int index;
    index = name.index();

    Objects.requireNonNull(value, "value == null");

    if (index >= 0) {
      final int valueIndex;
      valueIndex = objects.add(value);

      main.add(HtmlByteProto.ATTRIBUTE1);

      main.addInt8(index);

      main.addInt16(valueIndex);

      main.add(HtmlByteProto.INTERNAL5);
    } else {
      final int customIndex;
      customIndex = objects.add(name);

      final int valueIndex;
      valueIndex = objects.add(value);

      main.add(HtmlByteProto.CUSTOM_ATTR1);

      main.addInt16(customIndex);

      main.addInt16(valueIndex);

      main.add(HtmlByteProto.INTERNAL6);
    }

    return HtmlInstruction.ATTRIBUTE;
  }

}
