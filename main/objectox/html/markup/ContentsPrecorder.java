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

import java.util.Arrays;
import objectos.html.AttributeName;
import objectos.html.AttributeObject;
import objectos.html.markup.Instruction;

final class ContentsPrecorder {

  private final AttributeRecorder attributeRecorder;

  private final BooleanAttributeRecorder booleanAttributeRecorder;

  ContentsPrecorder(AttributeRecorder attributeRecorder, BooleanAttributeRecorder booleanAttributeRecorder) {
    this.attributeRecorder = attributeRecorder;

    this.booleanAttributeRecorder = booleanAttributeRecorder;
  }

  public final Instruction[] precord(Instruction... instructions) {
    for (int idx = 0; idx < instructions.length; idx++) {
      final Instruction instruction;
      instruction = instructions[idx];

      if (!(instruction instanceof AttributeObject)) {
        continue;
      }

      return precord1(instructions, idx);
    }

    return instructions;
  }

  private Instruction[] precord1(Instruction[] instructions, final int startIndex) {
    final int length;
    length = instructions.length;

    final Instruction[] result;
    result = Arrays.copyOf(instructions, length);

    for (int idx = startIndex; idx < length; idx++) {
      final Instruction instruction;
      instruction = instructions[idx];

      if (!(instruction instanceof AttributeObject attr)) {
        continue;
      }

      final AttributeName name;
      name = attr.attrName();

      final String value;
      value = attr.attrValue();

      result[idx] = value == null
          ? booleanAttributeRecorder.record(name)
          : attributeRecorder.record(name, value);
    }

    return result;
  }

}
