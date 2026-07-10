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
import objectox.html.ByteArray;
import objectox.html.HtmlByteProto;
import objectox.html.HtmlBytes;
import objectox.html.HtmlInstruction;
import objectox.html.ObjectArray;

final class Attribute1Recorder {

  private final ByteArray main;

  private final ObjectArray objects;

  private final AttributeName name;

  private final Object value;

  Attribute1Recorder(ByteArray main, ObjectArray objects, AttributeName name, Object value) {
    this.main = main;

    this.objects = objects;

    this.name = name;

    this.value = Objects.requireNonNull(value, "value == null");
  }

  public final Html.Instruction record() {
    final int index;
    index = name.index();

    if (index >= 0) {
      final int valueIndex;
      valueIndex = objects.add(value);

      main.add(
          HtmlByteProto.ATTRIBUTE1,

          // name
          HtmlBytes.encodeInt0(index),

          // value
          HtmlBytes.encodeInt0(valueIndex),
          HtmlBytes.encodeInt1(valueIndex),

          HtmlByteProto.INTERNAL5
      );
    } else {
      final int customIndex;
      customIndex = objects.add(name);

      final int valueIndex;
      valueIndex = objects.add(value);

      main.add(
          HtmlByteProto.CUSTOM_ATTR1,

          // name
          HtmlBytes.encodeInt0(customIndex),
          HtmlBytes.encodeInt1(customIndex),

          // value
          HtmlBytes.encodeInt0(valueIndex),
          HtmlBytes.encodeInt1(valueIndex),

          HtmlByteProto.INTERNAL6
      );
    }

    return HtmlInstruction.ATTRIBUTE;
  }

}
