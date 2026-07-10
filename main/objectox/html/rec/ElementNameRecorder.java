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

import objectos.html.ElementName;
import objectox.html.ByteArray;
import objectox.html.HtmlByteProto;
import objectox.html.HtmlBytes;
import objectox.html.elem.ElementNamePojo;

final class ElementNameRecorder {

  private final ByteArray main;

  private final ElementName name;

  public ElementNameRecorder(ByteArray main, ElementName name) {
    this.main = main;

    this.name = name;
  }

  public final void record() {
    main.add(
        HtmlByteProto.ELEMENT,

        // length takes 2 bytes
        HtmlByteProto.NULL,
        HtmlByteProto.NULL,

        HtmlByteProto.STANDARD_NAME,

        encodeName(name)
    );
  }

  private byte encodeName(ElementName name) {
    final ElementNamePojo pojo;
    pojo = (ElementNamePojo) name;

    final int ordinal;
    ordinal = pojo.index();

    return HtmlBytes.encodeInt0(ordinal);
  }

}
