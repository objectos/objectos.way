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
import objectos.html.rec.Instruction;
import objectox.html.Ambiguous;
import objectox.html.HtmlByteProto;
import objectox.html.HtmlInstruction;

final class AmbiguousRecorder {

  private final ByteArray main;

  private final ObjectArray objects;

  AmbiguousRecorder(ByteArray main, ObjectArray objects) {
    this.main = main;

    this.objects = objects;
  }

  public final Instruction.OfAmbiguous record(Ambiguous name, String value) {
    final String v;
    v = Objects.requireNonNull(value, "value == null");

    main.add(HtmlByteProto.AMBIGUOUS1);

    final int ordinal;
    ordinal = name.ordinal();

    main.addInt8(ordinal);

    final int object;
    object = objects.add(v);

    main.addInt16(object);

    main.add(HtmlByteProto.INTERNAL5);

    return HtmlInstruction.ELEMENT;
  }

}
