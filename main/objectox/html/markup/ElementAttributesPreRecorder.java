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

final class ElementAttributesPreRecorder {

  private final AttributeRecorder attributeRecorder;

  private final BooleanAttributeRecorder booleanAttributeRecorder;

  ElementAttributesPreRecorder(AttributeRecorder attributeRecorder, BooleanAttributeRecorder booleanAttributeRecorder) {
    this.attributeRecorder = attributeRecorder;

    this.booleanAttributeRecorder = booleanAttributeRecorder;
  }

  public final void record(ElementValue... values) {
    for (int idx = 0; idx < values.length; idx++) {
      final ElementValue value;
      value = values[idx];

      final ElementValue updated;
      updated = record0(value);

      if (updated == null) {
        continue;
      }

      values[idx] = updated;
    }
  }

  private ElementValue record0(ElementValue value) {
    return switch (value) {
      case Attribute a -> attributeRecorder.record(a);

      case BooleanAttribute a -> booleanAttributeRecorder.record(a);

      default -> null;
    };
  }

}
