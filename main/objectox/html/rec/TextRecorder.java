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
import objectox.html.ByteArray;
import objectox.html.HtmlByteProto;
import objectox.html.HtmlBytes;
import objectox.html.ObjectArray;

final class TextRecorder {

  private final ByteArray main;

  private final ObjectArray objects;

  TextRecorder(ByteArray main, ObjectArray objects) {
    this.main = main;

    this.objects = objects;
  }

  public final void record(String value) {
    final String v = Objects.requireNonNull(value, "value == null");

    final int object;
    object = objects.add(v);

    main.add(
        HtmlByteProto.TEXT,

        HtmlBytes.encodeInt0(object),
        HtmlBytes.encodeInt1(object),

        HtmlByteProto.INTERNAL4
    );
  }

}
