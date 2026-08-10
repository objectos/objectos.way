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
package objectox.html.markup;

import objectos.html.Attribute;
import objectos.html.BooleanAttribute;
import objectos.html.ElementName;
import objectos.html.markup.ElementValue;
import objectos.html.markup.NoOpMarkup;
import objectox.html.AmbiguousName;
import objectox.html.elem.ElementNamePojo;

final class ElementChildrenRecorder {

  private final ChildAmbiguousRecorder childAmbiguousRecorder;

  private final ChildElementMarker childElementMarker;

  private final ChildElementRecorder childElementRecorder;

  ElementChildrenRecorder(
      ChildAmbiguousRecorder childAmbiguousRecorder,

      ChildElementMarker childElementMarker,

      ChildElementRecorder childElementRecorder
  ) {
    this.childAmbiguousRecorder = childAmbiguousRecorder;

    this.childElementMarker = childElementMarker;

    this.childElementRecorder = childElementRecorder;
  }

  public final void record(ElementName elementName, ElementValue... contents) {
    for (ElementValue value : contents) {
      record0(elementName, value);
    }
  }

  private void record0(ElementName elementName, ElementValue value) {
    switch (value) {
      case AmbiguousPojo(AmbiguousName name, String text) -> {
        if (name.isAttributeOf(elementName)) {
          break;
        }

        final ElementNamePojo ambiguousElementName;
        ambiguousElementName = name.element;

        childAmbiguousRecorder.record(ambiguousElementName, text);
      }

      case Attribute _ -> {}

      case BooleanAttribute _ -> {}

      case ElementPointer child -> {
        childElementMarker.mark(child);

        childElementRecorder.record(child);
      }

      case NoOpMarkup _ -> {}
    }
  }

}
