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
package objectox.html.elem;

import java.util.Objects;
import objectos.html.AttributeObject;
import objectos.way.Html;
import objectos.way.Html.Instruction;
import objectox.html.ByteArray;
import objectox.html.HtmlByteProto;
import objectox.html.HtmlBytes;
import objectox.html.HtmlInstruction;
import objectox.html.ObjectArray;
import objectox.html.attr.AttributeObjectRecorder;

final class ElementValueRecorder {

  private final ByteArray aux;

  private final ByteArray main;

  private final ObjectArray objects;

  private final Instruction[] contents;

  public ElementValueRecorder(ByteArray aux, ByteArray main, ObjectArray objects, Html.Instruction... contents) {
    this.aux = aux;

    this.main = main;

    this.objects = objects;

    this.contents = Objects.requireNonNull(contents, "contents == null");
  }

  public final int record(int mainContents) {
    int result = mainContents;

    for (int idx = 0; idx < contents.length; idx++) {
      final Instruction instruction;
      instruction = contents[idx];

      if (instruction == null) {
        final String msg;
        msg = "contents[%d] == null".formatted(idx);

        throw new NullPointerException(msg);
      }

      result = record(result, instruction);
    }

    return result;
  }

  private int record(int mainContents, Html.Instruction value) {
    if (value == HtmlInstruction.ATTRIBUTE ||
        value == HtmlInstruction.ELEMENT ||
        value == HtmlInstruction.FRAGMENT) {
      // @ ByteProto
      mainContents--;

      byte proto;
      proto = main.get(mainContents--);

      switch (proto) {
        case HtmlByteProto.INTERNAL -> {
          int endIndex;
          endIndex = mainContents;

          byte maybeNeg;

          do {
            maybeNeg = main.get(mainContents--);
          } while (maybeNeg < 0);

          int length;
          length = HtmlBytes.decodeCommonEnd(main, mainContents, endIndex);

          mainContents -= length;
        }

        case HtmlByteProto.INTERNAL3 -> mainContents -= 3 - 2;

        case HtmlByteProto.INTERNAL4 -> mainContents -= 4 - 2;

        case HtmlByteProto.INTERNAL5 -> mainContents -= 5 - 2;

        case HtmlByteProto.INTERNAL6 -> mainContents -= 6 - 2;

        default -> throw new UnsupportedOperationException(
            "Implement me :: proto=" + proto
        );
      }

      aux.add(HtmlByteProto.INTERNAL);
    }

    else if (value instanceof AttributeObject ext) {
      final AttributeObjectRecorder recorder;
      recorder = new AttributeObjectRecorder(aux, objects, ext);

      recorder.record();
    }

    else if (value == HtmlInstruction.NOOP) {
      // no-op
    }

    else {
      throw new UnsupportedOperationException(
          "Implement me :: type=" + value.getClass()
      );
    }

    return mainContents;
  }

}
