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
import objectos.html.markup.ElementValue;
import objectos.html.markup.FragmentMarkup;
import objectos.html.markup.ElementMarkup;
import objectos.html.markup.NoOpMarkup;
import objectos.html.markup.RawMarkup;
import objectos.html.markup.TextMarkup;
import objectos.script.JsAction;
import objectox.html.AmbiguousName;
import objectox.html.attr.JsActionAttribute;
import objectox.html.elem.ElementName;
import objectox.html.attr.AttributeName;

final class ElementAttributesRecorder {

  private final AttributeRecorder attributeRecorder;

  private final BooleanAttributeRecorder booleanAttributeRecorder;

  ElementAttributesRecorder(
      AttributeRecorder attributeRecorder,

      BooleanAttributeRecorder booleanAttributeRecorder
  ) {
    this.attributeRecorder = attributeRecorder;

    this.booleanAttributeRecorder = booleanAttributeRecorder;
  }

  public final void record(ElementName elementName, ElementValue... values) {
    for (ElementValue value : values) {
      record0(elementName, value);
    }
  }

  private void record0(ElementName elementName, ElementValue value) {
    switch (value) {
      case AmbiguousPojo(AmbiguousName name, String text) -> {
        if (name.isAttributeOf(elementName)) {
          final AttributeName attributeName;
          attributeName = name.attribute;

          attributeRecorder.record(attributeName, text);
        }
      }

      case JsActionAttribute(AttributeName name, JsAction action) -> attributeRecorder.record(name, action);

      case Attribute attr -> attributeRecorder.record(attr);

      case BooleanAttribute attr -> booleanAttributeRecorder.record(attr);

      case ElementMarkup _ -> {}

      case FragmentMarkup _ -> {}

      case NoOpMarkup _ -> {}

      case RawMarkup _ -> {}

      case TextMarkup _ -> {}
    }
  }

}
