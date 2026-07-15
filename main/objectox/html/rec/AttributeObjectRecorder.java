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
import objectos.html.AttributeObject;
import objectox.html.ByteArray;
import objectox.html.HtmlByteProto;
import objectox.html.ObjectArray;

public final class AttributeObjectRecorder {

  private final ByteArray aux;

  private final ObjectArray objects;

  public AttributeObjectRecorder(ByteArray aux, ObjectArray objects) {
    this.aux = aux;

    this.objects = objects;
  }

  public final void record(AttributeObject attr) {
    final AttributeName name;
    name = attr.attrName();

    if (name == null) {
      final String msg;
      msg = "Attribute object provided a null attribute name: " + attr;

      throw new NullPointerException(msg);
    }

    final int nameIndex;
    nameIndex = name.index();

    if (nameIndex < 0) {
      throw new UnsupportedOperationException("Custom attribute name");
    }

    final String attrValue;
    attrValue = attr.attrValue();

    if (attrValue == null) {
      aux.add(HtmlByteProto.ATTRIBUTE_EXT0);

      aux.addInt8(nameIndex);
    } else {
      final int valueIndex;
      valueIndex = objects.add(attrValue);

      aux.add(HtmlByteProto.ATTRIBUTE_EXT1);

      aux.addInt8(nameIndex);

      aux.addInt16(valueIndex);
    }
  }

}
