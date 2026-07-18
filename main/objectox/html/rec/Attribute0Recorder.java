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
import objectos.way.Html;
import objectox.html.HtmlByteProto;
import objectox.html.HtmlInstruction;

final class Attribute0Recorder {

  private final ByteArray main;

  private final ObjectArray objects;

  Attribute0Recorder(ByteArray main, ObjectArray objects) {
    this.main = main;

    this.objects = objects;
  }

  public final Html.Instruction.OfAttribute record(AttributeName name) {
    final int index;
    index = name.index();

    if (index >= 0) {
      main.add(HtmlByteProto.ATTRIBUTE0);

      main.addInt8(index);

      main.add(HtmlByteProto.INTERNAL3);
    } else {
      final int customIndex;
      customIndex = objects.add(name);

      main.add(HtmlByteProto.CUSTOM_ATTR0);

      main.addInt16(customIndex);

      main.add(HtmlByteProto.INTERNAL4);
    }

    return HtmlInstruction.ATTRIBUTE;
  }

}
