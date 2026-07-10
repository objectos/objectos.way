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

import objectos.html.AttributeObject;
import objectos.way.Html;
import objectox.html.HtmlInstruction;

final class ElementValueRecorder {

  private final AttributeObjectRecorder attributeObjectRecorder;

  private final ElementInternalRecorder elementInternalRecorder;

  ElementValueRecorder(AttributeObjectRecorder attributeObjectRecorder, ElementInternalRecorder elementInternalRecorder) {
    this.attributeObjectRecorder = attributeObjectRecorder;

    this.elementInternalRecorder = elementInternalRecorder;
  }

  public final int record(int mainContents, Html.Instruction value) {
    if (value == HtmlInstruction.ATTRIBUTE ||
        value == HtmlInstruction.ELEMENT ||
        value == HtmlInstruction.FRAGMENT) {
      return elementInternalRecorder.record(mainContents);
    }

    if (value instanceof AttributeObject attr) {
      attributeObjectRecorder.record(attr);

      return mainContents;
    }

    if (value == HtmlInstruction.NOOP) {
      // no-op

      return mainContents;
    }

    final String msg;
    msg = "Unsupported type %s".formatted(value.getClass());

    throw new UnsupportedOperationException(msg);
  }

}
